package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.PgTypes
import dev.typr.foundations.RowCodec
import dev.typr.foundations.Template
import dev.typr.foundations.pg.PgPipelinePool
import io.vertx.sqlclient.Tuple as VxTuple
import jakarta.persistence.*
import java.math.BigDecimal
import java.sql.ResultSet
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future

// ==================== Configuration ====================

private object LargeCfg {
    const val LOCAL_POOL_SIZE = 50
    const val LOCAL_TASK_COUNT = 10_000
    const val LATENCY_POOL_SIZE = 10
    const val LATENCY_TASK_COUNT = 1_000
    const val WARMUP_OPS = 1_000
    const val MEASURED_ITERATIONS = 5
    const val LATENCY_MS = 5L
    const val LARGE_ROW_COUNT = 10_000
}

// ==================== Data Model ====================

data class LargeRow(
    val id: Int,
    val colStr1: String, val colStr2: String, val colStr3: String, val colStr4: String,
    val colNum1: BigDecimal, val colNum2: BigDecimal, val colNum3: BigDecimal, val colNum4: BigDecimal,
    val colBool1: Boolean, val colBool2: Boolean, val colBool3: Boolean, val colBool4: Boolean,
    val colTs1: LocalDateTime, val colTs2: LocalDateTime, val colTs3: LocalDateTime, val colTs4: LocalDateTime,
    val colText1: String, val colText2: String, val colText3: String, val colText4: String,
)

@Entity
@Table(name = "bench_large")
class LargeRowEntity {
    @Id
    var id: Int = 0
    @Column(name = "col_str1") var colStr1: String = ""
    @Column(name = "col_str2") var colStr2: String = ""
    @Column(name = "col_str3") var colStr3: String = ""
    @Column(name = "col_str4") var colStr4: String = ""
    @Column(name = "col_num1") var colNum1: BigDecimal = BigDecimal.ZERO
    @Column(name = "col_num2") var colNum2: BigDecimal = BigDecimal.ZERO
    @Column(name = "col_num3") var colNum3: BigDecimal = BigDecimal.ZERO
    @Column(name = "col_num4") var colNum4: BigDecimal = BigDecimal.ZERO
    @Column(name = "col_bool1") var colBool1: Boolean = false
    @Column(name = "col_bool2") var colBool2: Boolean = false
    @Column(name = "col_bool3") var colBool3: Boolean = false
    @Column(name = "col_bool4") var colBool4: Boolean = false
    @Column(name = "col_ts1") var colTs1: LocalDateTime = LocalDateTime.MIN
    @Column(name = "col_ts2") var colTs2: LocalDateTime = LocalDateTime.MIN
    @Column(name = "col_ts3") var colTs3: LocalDateTime = LocalDateTime.MIN
    @Column(name = "col_ts4") var colTs4: LocalDateTime = LocalDateTime.MIN
    @Column(name = "col_text1") var colText1: String = ""
    @Column(name = "col_text2") var colText2: String = ""
    @Column(name = "col_text3") var colText3: String = ""
    @Column(name = "col_text4") var colText4: String = ""
}

// ==================== SQL & Codecs ====================

private const val LARGE_SELECT_SQL = """SELECT id,
    col_str1, col_str2, col_str3, col_str4,
    col_num1, col_num2, col_num3, col_num4,
    col_bool1, col_bool2, col_bool3, col_bool4,
    col_ts1, col_ts2, col_ts3, col_ts4,
    col_text1, col_text2, col_text3, col_text4
    FROM bench_large WHERE id = ?"""

private const val VX_LARGE_SELECT_SQL = """SELECT id,
    col_str1, col_str2, col_str3, col_str4,
    col_num1, col_num2, col_num3, col_num4,
    col_bool1, col_bool2, col_bool3, col_bool4,
    col_ts1, col_ts2, col_ts3, col_ts4,
    col_text1, col_text2, col_text3, col_text4
    FROM bench_large WHERE id = ${'$'}1"""

private fun readLargeRow(rs: ResultSet): LargeRow = LargeRow(
    rs.getInt(1),
    rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
    rs.getBigDecimal(6), rs.getBigDecimal(7), rs.getBigDecimal(8), rs.getBigDecimal(9),
    rs.getBoolean(10), rs.getBoolean(11), rs.getBoolean(12), rs.getBoolean(13),
    rs.getTimestamp(14).toLocalDateTime(), rs.getTimestamp(15).toLocalDateTime(),
    rs.getTimestamp(16).toLocalDateTime(), rs.getTimestamp(17).toLocalDateTime(),
    rs.getString(18), rs.getString(19), rs.getString(20), rs.getString(21),
)

private fun largeRowCodec(): RowCodec<LargeRow> =
    RowCodec.builder<LargeRow>()
        .field(PgTypes.int4, LargeRow::id)
        .field(PgTypes.text, LargeRow::colStr1)
        .field(PgTypes.text, LargeRow::colStr2)
        .field(PgTypes.text, LargeRow::colStr3)
        .field(PgTypes.text, LargeRow::colStr4)
        .field(PgTypes.numeric, LargeRow::colNum1)
        .field(PgTypes.numeric, LargeRow::colNum2)
        .field(PgTypes.numeric, LargeRow::colNum3)
        .field(PgTypes.numeric, LargeRow::colNum4)
        .field(PgTypes.bool, LargeRow::colBool1)
        .field(PgTypes.bool, LargeRow::colBool2)
        .field(PgTypes.bool, LargeRow::colBool3)
        .field(PgTypes.bool, LargeRow::colBool4)
        .field(PgTypes.timestamp, LargeRow::colTs1)
        .field(PgTypes.timestamp, LargeRow::colTs2)
        .field(PgTypes.timestamp, LargeRow::colTs3)
        .field(PgTypes.timestamp, LargeRow::colTs4)
        .field(PgTypes.text, LargeRow::colText1)
        .field(PgTypes.text, LargeRow::colText2)
        .field(PgTypes.text, LargeRow::colText3)
        .field(PgTypes.text, LargeRow::colText4)
        .build(::LargeRow)

private fun largeSelectTemplate(): Template.Query1<Int, java.util.Optional<LargeRow>> =
    Fragment.of("SELECT id, col_str1, col_str2, col_str3, col_str4, col_num1, col_num2, col_num3, col_num4, col_bool1, col_bool2, col_bool3, col_bool4, col_ts1, col_ts2, col_ts3, col_ts4, col_text1, col_text2, col_text3, col_text4 FROM bench_large WHERE id = ")
        .param(PgTypes.int4)
        .query(largeRowCodec().first())

// ==================== Database Setup ====================

private fun setupLargeTable(port: Int) {
    val ds = dev.typr.foundations.hikari.HikariDataSourceFactory.create(
        dev.typr.foundations.connect.PgConfig.builder(PG_HOST, port, PG_DB, PG_USER, PG_PASS).build(),
        dev.typr.foundations.hikari.PoolConfig.builder().maximumPoolSize(2).minimumIdle(2).build()
    )

    ds.connection.use { conn ->
        conn.createStatement().use { stmt ->
            stmt.execute("DROP TABLE IF EXISTS bench_large")
            stmt.execute("""
                CREATE TABLE bench_large (
                    id INTEGER PRIMARY KEY,
                    col_str1 VARCHAR(200), col_str2 VARCHAR(200), col_str3 VARCHAR(200), col_str4 VARCHAR(200),
                    col_num1 DECIMAL(12,2), col_num2 DECIMAL(12,2), col_num3 DECIMAL(12,2), col_num4 DECIMAL(12,2),
                    col_bool1 BOOLEAN, col_bool2 BOOLEAN, col_bool3 BOOLEAN, col_bool4 BOOLEAN,
                    col_ts1 TIMESTAMP, col_ts2 TIMESTAMP, col_ts3 TIMESTAMP, col_ts4 TIMESTAMP,
                    col_text1 TEXT, col_text2 TEXT, col_text3 TEXT, col_text4 TEXT
                )
            """.trimIndent())
        }

        val baseTs = LocalDateTime.of(2024, 1, 1, 0, 0)
        conn.prepareStatement("""
            INSERT INTO bench_large (id,
                col_str1, col_str2, col_str3, col_str4,
                col_num1, col_num2, col_num3, col_num4,
                col_bool1, col_bool2, col_bool3, col_bool4,
                col_ts1, col_ts2, col_ts3, col_ts4,
                col_text1, col_text2, col_text3, col_text4
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """.trimIndent()).use { ps ->
            for (i in 1..LargeCfg.LARGE_ROW_COUNT) {
                ps.setInt(1, i)
                ps.setString(2, "str1_$i")
                ps.setString(3, "str2_$i")
                ps.setString(4, "str3_$i")
                ps.setString(5, "str4_$i")
                ps.setBigDecimal(6, BigDecimal("${i % 10000}.${i % 100}"))
                ps.setBigDecimal(7, BigDecimal("${(i + 1) % 10000}.${(i + 1) % 100}"))
                ps.setBigDecimal(8, BigDecimal("${(i + 2) % 10000}.${(i + 2) % 100}"))
                ps.setBigDecimal(9, BigDecimal("${(i + 3) % 10000}.${(i + 3) % 100}"))
                ps.setBoolean(10, i % 2 == 0)
                ps.setBoolean(11, i % 3 == 0)
                ps.setBoolean(12, i % 5 == 0)
                ps.setBoolean(13, i % 7 == 0)
                ps.setObject(14, baseTs.plusMinutes(i.toLong()))
                ps.setObject(15, baseTs.plusMinutes(i.toLong() * 2))
                ps.setObject(16, baseTs.plusMinutes(i.toLong() * 3))
                ps.setObject(17, baseTs.plusMinutes(i.toLong() * 4))
                ps.setString(18, "Long text content for row $i field 1 with some padding to simulate real data")
                ps.setString(19, "Long text content for row $i field 2 with some padding to simulate real data")
                ps.setString(20, "Long text content for row $i field 3 with some padding to simulate real data")
                ps.setString(21, "Long text content for row $i field 4 with some padding to simulate real data")
                ps.addBatch()
                if (i % 5000 == 0) ps.executeBatch()
            }
            ps.executeBatch()
        }
    }

    ds.close()
    println("Seeded ${LargeCfg.LARGE_ROW_COUNT} rows into bench_large (20 columns)")
}

// ==================== Hibernate SessionFactory with LargeRowEntity ====================

private fun createLargeHibernateSessionFactory(port: Int, poolSize: Int): org.hibernate.SessionFactory {
    val cfg = org.hibernate.cfg.Configuration()
    cfg.setProperty("hibernate.connection.url", "jdbc:postgresql://$PG_HOST:$port/$PG_DB")
    cfg.setProperty("hibernate.connection.username", PG_USER)
    cfg.setProperty("hibernate.connection.password", PG_PASS)
    cfg.setProperty("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")
    cfg.setProperty("hibernate.hikari.maximumPoolSize", poolSize.toString())
    cfg.setProperty("hibernate.hikari.minimumIdle", poolSize.toString())
    cfg.setProperty("hibernate.show_sql", "false")
    cfg.setProperty("hibernate.hbm2ddl.auto", "none")
    cfg.addAnnotatedClass(LargeRowEntity::class.java)
    return cfg.buildSessionFactory()
}

// ==================== Main ====================

fun main() = runLargeRowBenchmark()

fun runLargeRowBenchmark() {
    println("=== Large Row (20 columns) Benchmark ===")
    println("PostgreSQL at $PG_HOST:$PG_PORT/$PG_DB")
    println()

    setupLargeTable(PG_PORT)

    val localResults = runLocalLargeRowBenchmark()
    val latencyResults = runLatencyLargeRowBenchmark()

    val report = MarkdownReport("Large Row Benchmark")
    report.section("Local Large Row Read (20 cols)", "${LargeCfg.LOCAL_TASK_COUNT} tasks, ${LargeCfg.LOCAL_POOL_SIZE} connections.", localResults, "ops/sec",
        """
        pool.execute(selectLargeById.on(42))
        """.trimIndent()
    )
    report.section("Large Row @ ${LargeCfg.LATENCY_MS * 2}ms RTT (20 cols)", "${LargeCfg.LATENCY_TASK_COUNT} tasks, ${LargeCfg.LATENCY_POOL_SIZE} connections.", latencyResults, "ops/sec",
        """
        pool.execute(selectLargeById.on(42))
        """.trimIndent()
    )
    report.writeTo(REPORT_DIR, "large-row.md")
}

// ==================== 1. Local ====================

private fun runLocalLargeRowBenchmark(): List<BenchResult> {
    println("=".repeat(70))
    println("=== 1. Local Large Row Read (${LargeCfg.LOCAL_TASK_COUNT} tasks, ${LargeCfg.LOCAL_POOL_SIZE} conns) ===")
    println("=".repeat(70))

    val results = mutableListOf<BenchResult>()
    val selectTpl = largeSelectTemplate()

    val rawDs = createHikariPool(PG_PORT, LargeCfg.LOCAL_POOL_SIZE)
    println("  Raw JDBC...")
    results.add(runBenchmark("Raw JDBC", LargeCfg.LOCAL_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        rawDs.connection.use { conn ->
            conn.prepareStatement(LARGE_SELECT_SQL).use { ps ->
                ps.setInt(1, (i % LargeCfg.LARGE_ROW_COUNT) + 1)
                ps.executeQuery().use { rs -> if (rs.next()) readLargeRow(rs) else null }
            }
        }
    })
    rawDs.close()

    val (hikariDs, fndTx) = createTransactor(PG_PORT, LargeCfg.LOCAL_POOL_SIZE)
    warmupFoundationsLarge(fndTx, selectTpl)
    println("  Foundations+Hikari...")
    results.add(runBenchmark("Foundations+Hikari", LargeCfg.LOCAL_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        fndTx.execute(selectTpl.on((i % LargeCfg.LARGE_ROW_COUNT) + 1))
    })
    hikariDs.close()

    val pipePool = createPipelinePool(PG_PORT, LargeCfg.LOCAL_POOL_SIZE)
    warmupPipeLarge(pipePool, selectTpl)

    println("  PgPipe (readonly)...")
    results.add(runBenchmark("Foundations+PgPipe (readonly)", LargeCfg.LOCAL_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        pipePool.transactRead { conn -> conn.execute(selectTpl.on((i % LargeCfg.LARGE_ROW_COUNT) + 1)) }
    })

    println("  PgPipe (mutable)...")
    results.add(runBenchmark("Foundations+PgPipe (mutable)", LargeCfg.LOCAL_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        pipePool.transact { conn -> conn.execute(selectTpl.on((i % LargeCfg.LARGE_ROW_COUNT) + 1)) }
    })
    pipePool.close()

    val rawVx = createRawVertxPool(PG_PORT, LargeCfg.LOCAL_POOL_SIZE)
    warmupVertxLarge(rawVx.pool, LargeCfg.LOCAL_POOL_SIZE)
    println("  Vert.x (async)...")
    results.add(runAsyncBenchmark("Vert.x (async)", LargeCfg.LOCAL_TASK_COUNT, LargeCfg.MEASURED_ITERATIONS) { i ->
        rawVx.pool.preparedQuery(VX_LARGE_SELECT_SQL).execute(VxTuple.of((i % LargeCfg.LARGE_ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    })
    rawVx.close()

    val sf = createLargeHibernateSessionFactory(PG_PORT, LargeCfg.LOCAL_POOL_SIZE)
    warmupHibernateLarge(sf)
    println("  Hibernate...")
    results.add(runBenchmark("Hibernate", LargeCfg.LOCAL_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        sf.openSession().use { session ->
            session.find(LargeRowEntity::class.java, (i % LargeCfg.LARGE_ROW_COUNT) + 1)
        }
    })
    sf.close()

    printResults("Local Large Row Read (20 cols)", results, "ops/sec")
    return results
}

// ==================== 2. Under 10ms RTT ====================

private fun runLatencyLargeRowBenchmark(): List<BenchResult> {
    println()
    println("=".repeat(70))
    println("=== 2. Large Row @ ${LargeCfg.LATENCY_MS * 2}ms RTT (${LargeCfg.LATENCY_TASK_COUNT} tasks, ${LargeCfg.LATENCY_POOL_SIZE} conns) ===")
    println("=".repeat(70))

    val proxyPort = proxiedPort(LargeCfg.LATENCY_MS, 18300)
    val results = mutableListOf<BenchResult>()
    val selectTpl = largeSelectTemplate()

    val rawDs = createHikariPool(proxyPort.port, LargeCfg.LATENCY_POOL_SIZE)
    println("  Raw JDBC...")
    results.add(runBenchmark("Raw JDBC", LargeCfg.LATENCY_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        rawDs.connection.use { conn ->
            conn.prepareStatement(LARGE_SELECT_SQL).use { ps ->
                ps.setInt(1, (i % LargeCfg.LARGE_ROW_COUNT) + 1)
                ps.executeQuery().use { rs -> if (rs.next()) readLargeRow(rs) else null }
            }
        }
    })
    rawDs.close()

    val (hikariDs, fndTx) = createTransactor(proxyPort.port, LargeCfg.LATENCY_POOL_SIZE)
    warmupFoundationsLarge(fndTx, selectTpl)
    println("  Foundations+Hikari...")
    results.add(runBenchmark("Foundations+Hikari", LargeCfg.LATENCY_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        fndTx.execute(selectTpl.on((i % LargeCfg.LARGE_ROW_COUNT) + 1))
    })
    hikariDs.close()

    val pipePool = createPipelinePool(proxyPort.port, LargeCfg.LATENCY_POOL_SIZE)
    warmupPipeLarge(pipePool, selectTpl)

    println("  PgPipe (readonly)...")
    results.add(runBenchmark("Foundations+PgPipe (readonly)", LargeCfg.LATENCY_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        pipePool.transactRead { conn -> conn.execute(selectTpl.on((i % LargeCfg.LARGE_ROW_COUNT) + 1)) }
    })

    println("  PgPipe (mutable)...")
    results.add(runBenchmark("Foundations+PgPipe (mutable)", LargeCfg.LATENCY_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        pipePool.transact { conn -> conn.execute(selectTpl.on((i % LargeCfg.LARGE_ROW_COUNT) + 1)) }
    })
    pipePool.close()

    val rawVx = createRawVertxPool(proxyPort.port, LargeCfg.LATENCY_POOL_SIZE)
    warmupVertxLarge(rawVx.pool, LargeCfg.LATENCY_POOL_SIZE)
    println("  Vert.x (async)...")
    results.add(runAsyncBenchmark("Vert.x (async)", LargeCfg.LATENCY_TASK_COUNT, LargeCfg.MEASURED_ITERATIONS) { i ->
        rawVx.pool.preparedQuery(VX_LARGE_SELECT_SQL).execute(VxTuple.of((i % LargeCfg.LARGE_ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    })
    rawVx.close()

    val sf = createLargeHibernateSessionFactory(proxyPort.port, LargeCfg.LATENCY_POOL_SIZE)
    warmupHibernateLarge(sf)
    println("  Hibernate...")
    results.add(runBenchmark("Hibernate", LargeCfg.LATENCY_TASK_COUNT, LargeCfg.WARMUP_OPS, LargeCfg.MEASURED_ITERATIONS) { i ->
        sf.openSession().use { session ->
            session.find(LargeRowEntity::class.java, (i % LargeCfg.LARGE_ROW_COUNT) + 1)
        }
    })
    sf.close()

    proxyPort.close()
    printResults("Large Row @ ${LargeCfg.LATENCY_MS * 2}ms RTT (20 cols)", results, "ops/sec")
    return results
}

// ==================== Warmup Helpers ====================

private fun warmupPipeLarge(pool: PgPipelinePool, tpl: Template.Query1<Int, java.util.Optional<LargeRow>>) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..LargeCfg.WARMUP_OPS).map { i -> exec.submit<Any?> {
            pool.execute(tpl.on((i % LargeCfg.LARGE_ROW_COUNT) + 1))
        }}.forEach(Future<*>::get)
    }
}

private fun warmupFoundationsLarge(tx: dev.typr.foundations.Transactor, tpl: Template.Query1<Int, java.util.Optional<LargeRow>>) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..LargeCfg.WARMUP_OPS).map { i -> exec.submit<Any?> {
            tx.execute(tpl.on((i % LargeCfg.LARGE_ROW_COUNT) + 1))
        }}.forEach(Future<*>::get)
    }
}

private fun warmupVertxLarge(pool: io.vertx.pgclient.PgPool, poolSize: Int) {
    for (i in 1..poolSize) {
        pool.query("SELECT 1").execute().toCompletionStage().toCompletableFuture().join()
    }
    val warmupFutures = (1..LargeCfg.WARMUP_OPS).map { i ->
        pool.preparedQuery(VX_LARGE_SELECT_SQL).execute(VxTuple.of((i % LargeCfg.LARGE_ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    }
    CompletableFuture.allOf(*warmupFutures.toTypedArray()).join()
}

private fun warmupHibernateLarge(sf: org.hibernate.SessionFactory) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..LargeCfg.WARMUP_OPS).map { i -> exec.submit<Any?> {
            sf.openSession().use { session ->
                session.find(LargeRowEntity::class.java, (i % LargeCfg.LARGE_ROW_COUNT) + 1)
            }
        }}.forEach(Future<*>::get)
    }
}
