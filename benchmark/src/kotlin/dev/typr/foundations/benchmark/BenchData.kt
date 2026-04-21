package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.PgTypes
import dev.typr.foundations.RowCodec
import dev.typr.foundations.RowCodecNamed
import dev.typr.foundations.Template
import dev.typr.foundations.connect.PgConfig
import dev.typr.foundations.hikari.HikariDataSourceFactory
import dev.typr.foundations.hikari.PoolConfig
import java.math.BigDecimal
import java.sql.Connection
import java.sql.ResultSet
import java.time.LocalDateTime

// ==================== Data Model ====================

data class BenchRow(
    val id: Int,
    val name: String,
    val value: BigDecimal,
    val active: Boolean,
    val createdAt: LocalDateTime,
    val category: String,
)

data class WriteRow(
    val name: String,
    val value: BigDecimal,
    val active: Boolean,
    val createdAt: LocalDateTime,
    val category: String,
)

val CATEGORIES = listOf("Electronics", "Books", "Clothing", "Home", "Sports", "Food", "Auto", "Toys")
val BASE_DATE: LocalDateTime = LocalDateTime.of(2024, 1, 1, 0, 0)

fun readBenchRow(rs: ResultSet): BenchRow = BenchRow(
    rs.getInt(1),
    rs.getString(2),
    rs.getBigDecimal(3),
    rs.getBoolean(4),
    rs.getTimestamp(5).toLocalDateTime(),
    rs.getString(6),
)

fun generateRows(count: Int): List<WriteRow> =
    (1..count).map { i ->
        WriteRow(
            "Product #$i — ${CATEGORIES[i % CATEGORIES.size]} item",
            BigDecimal("${i % 10000}.${i % 100}"),
            i % 3 != 0,
            BASE_DATE.plusMinutes(i.toLong()),
            CATEGORIES[i % CATEGORIES.size],
        )
    }

// ==================== Codecs & Templates ====================

fun benchReadCodec(): RowCodec<BenchRow> =
    RowCodec.builder<BenchRow>()
        .field(PgTypes.int4, BenchRow::id)
        .field(PgTypes.text, BenchRow::name)
        .field(PgTypes.numeric, BenchRow::value)
        .field(PgTypes.bool, BenchRow::active)
        .field(PgTypes.timestamp, BenchRow::createdAt)
        .field(PgTypes.text, BenchRow::category)
        .build(::BenchRow)

fun selectByIdTemplate(): Template.Query1<Int, java.util.Optional<BenchRow>> =
    Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
        .param(PgTypes.int4)
        .query(benchReadCodec().first())

fun insertTemplate(): Template.Update5<String, BigDecimal, Boolean, LocalDateTime, String> =
    Fragment.of("INSERT INTO bench_write (name, value, active, created_at, category) VALUES (")
        .param(PgTypes.text).append(", ")
        .param(PgTypes.numeric).append(", ")
        .param(PgTypes.bool).append(", ")
        .param(PgTypes.timestamp).append(", ")
        .param(PgTypes.text).append(")")
        .update()

fun writeNamedCodec(): RowCodecNamed<WriteRow> =
    RowCodec.namedBuilder<WriteRow>()
        .field("name", PgTypes.text, WriteRow::name)
        .field("value", PgTypes.numeric, WriteRow::value)
        .field("active", PgTypes.bool, WriteRow::active)
        .field("created_at", PgTypes.timestamp, WriteRow::createdAt)
        .field("category", PgTypes.text, WriteRow::category)
        .build(::WriteRow)

// ==================== Database Setup ====================

fun setupBenchTables(port: Int) {
    val pgConfig = PgConfig.builder(PG_HOST, port, PG_DB, PG_USER, PG_PASS).build()
    val ds = HikariDataSourceFactory.create(pgConfig, PoolConfig.builder().maximumPoolSize(2).minimumIdle(2).build())

    ds.connection.use { conn ->
        setupBenchTablesOnConnection(conn)
    }

    ds.close()
}

fun setupBenchTablesOnConnection(conn: Connection) {
    conn.createStatement().use { stmt ->
        stmt.execute("DROP TABLE IF EXISTS bench_read")
        stmt.execute("""
            CREATE TABLE bench_read (
                id INTEGER PRIMARY KEY,
                name VARCHAR(200) NOT NULL,
                value DECIMAL(12,2) NOT NULL,
                active BOOLEAN NOT NULL,
                created_at TIMESTAMP NOT NULL,
                category VARCHAR(50) NOT NULL
            )
        """.trimIndent())

        stmt.execute("DROP TABLE IF EXISTS bench_write")
        stmt.execute("""
            CREATE TABLE bench_write (
                id BIGSERIAL PRIMARY KEY,
                name VARCHAR(200) NOT NULL,
                value DECIMAL(12,2) NOT NULL,
                active BOOLEAN NOT NULL,
                created_at TIMESTAMP NOT NULL,
                category VARCHAR(50) NOT NULL
            )
        """.trimIndent())
        stmt.execute("ALTER SEQUENCE bench_write_id_seq INCREMENT BY 50")
    }

    conn.prepareStatement("INSERT INTO bench_read (id, name, value, active, created_at, category) VALUES (?, ?, ?, ?, ?, ?)").use { ps ->
        for (i in 1..ROW_COUNT) {
            ps.setInt(1, i)
            ps.setString(2, "Product #$i — ${CATEGORIES[i % CATEGORIES.size]} item with detailed description")
            ps.setBigDecimal(3, BigDecimal("${i % 10000}.${i % 100}"))
            ps.setBoolean(4, i % 3 != 0)
            ps.setObject(5, BASE_DATE.plusMinutes(i.toLong()))
            ps.setString(6, CATEGORIES[i % CATEGORIES.size])
            ps.addBatch()
            if (i % 5000 == 0) ps.executeBatch()
        }
        ps.executeBatch()
    }

    println("Seeded $ROW_COUNT rows into bench_read")
}

// ==================== Raw SQL Strings ====================

const val RAW_SELECT_SQL = "SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ?"
const val RAW_INSERT_SQL = "INSERT INTO bench_write (name, value, active, created_at, category) VALUES (?, ?, ?, ?, ?)"
const val RAW_UPDATE_SQL = "UPDATE bench_read SET value = ?, name = ? WHERE id = ?"

const val VX_SELECT_SQL = "SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = \$1"
const val VX_INSERT_SQL = "INSERT INTO bench_write (name, value, active, created_at, category) VALUES (\$1, \$2, \$3, \$4, \$5)"
