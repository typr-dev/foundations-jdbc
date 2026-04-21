package dev.typr.foundations.benchmark

import dev.typr.foundations.Db2Types
import dev.typr.foundations.Fragment
import dev.typr.foundations.RowCodec
import dev.typr.foundations.Template
import dev.typr.foundations.TransactorJdbc
import dev.typr.foundations.connect.Db2Config
import dev.typr.foundations.hikari.HikariDataSourceFactory
import dev.typr.foundations.hikari.PoolConfig
import io.vertx.core.Vertx
import io.vertx.db2client.DB2ConnectOptions
import io.vertx.db2client.DB2Pool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.Tuple as VxTuple
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.Future

// ==================== Configuration ====================

const val DB2_HOST = "localhost"
const val DB2_PORT = 50000
const val DB2_DB = "typr"
const val DB2_USER = "db2inst1"
const val DB2_PASS = "password"

private object Db2Cfg {
    const val POOL_SIZE = 50
    const val TASK_COUNT = 10_000
    const val WARMUP_OPS = 5_000
    const val MEASURED_ITERATIONS = 5
    const val SCARCE_CONNECTIONS = 5
    const val SCARCE_TASK_COUNT = 5_000
    const val BATCH_SIZE = 10
    const val BATCH_TASK_COUNT = 1_000
    const val BATCH_CONNECTIONS = 5
}

// ==================== Main ====================

fun main() {
    println("=== DB2 Concurrent Throughput Benchmark ===")
    println("DB2 at $DB2_HOST:$DB2_PORT/$DB2_DB")
    println("Pool size: ${Db2Cfg.POOL_SIZE} connections (both sides)")
    println("Concurrency: ${Db2Cfg.TASK_COUNT} concurrent tasks")
    println("Warmup: ${Db2Cfg.WARMUP_OPS} ops, Measured: ${Db2Cfg.TASK_COUNT} ops x ${Db2Cfg.MEASURED_ITERATIONS} iterations")
    println()

    setupDb2Database()

    val pointReadResults = mutableListOf<BenchResult>()
    val insertResults = mutableListOf<BenchResult>()
    val mixedResults = mutableListOf<BenchResult>()

    runDb2FoundationsBenchmark(pointReadResults, insertResults, mixedResults)
    runDb2VertxBenchmark(pointReadResults, insertResults, mixedResults)

    printResults("DB2: Point Read (SELECT WHERE id = ?)", pointReadResults, "ops/sec")
    printResults("DB2: Single Insert", insertResults, "ops/sec")
    printResults("DB2: Mixed 80/20 Read/Write", mixedResults, "ops/sec")

    println()
    println("=" .repeat(70))
    println("=== Pipelining Advantage: Scarce Connections & Batch Fan-Out ===")
    println("=" .repeat(70))

    val scarceResults = mutableListOf<BenchResult>()
    val batchResults = mutableListOf<BenchResult>()

    runDb2ScarceConnectionsBenchmark(scarceResults, batchResults)

    printResults("DB2: Scarce Connections: ${Db2Cfg.SCARCE_TASK_COUNT} point reads, ${Db2Cfg.SCARCE_CONNECTIONS} conns", scarceResults, "ops/sec")
    printResults("DB2: Batch Fan-Out: ${Db2Cfg.BATCH_TASK_COUNT} page loads x ${Db2Cfg.BATCH_SIZE} queries, ${Db2Cfg.BATCH_CONNECTIONS} conns", batchResults, "ops/sec")
}

// ==================== Database Setup ====================

fun setupDb2Database() {
    val db2Config = Db2Config.builder(DB2_HOST, DB2_PORT, DB2_DB, DB2_USER, DB2_PASS).build()
    val ds = HikariDataSourceFactory.create(db2Config, PoolConfig.builder().maximumPoolSize(2).minimumIdle(2).build())

    ds.getConnection().use { conn ->
        conn.createStatement().use { stmt ->
            try { stmt.execute("DROP TABLE bench_read") } catch (_: Exception) {}
            stmt.execute("""
                CREATE TABLE bench_read (
                    id INTEGER NOT NULL PRIMARY KEY,
                    name VARCHAR(200) NOT NULL,
                    value DECIMAL(12,2) NOT NULL,
                    active BOOLEAN NOT NULL,
                    created_at TIMESTAMP NOT NULL,
                    category VARCHAR(50) NOT NULL
                )
            """.trimIndent())

            try { stmt.execute("DROP TABLE bench_write") } catch (_: Exception) {}
            stmt.execute("""
                CREATE TABLE bench_write (
                    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                    name VARCHAR(200) NOT NULL,
                    value DECIMAL(12,2) NOT NULL,
                    active BOOLEAN NOT NULL,
                    created_at TIMESTAMP NOT NULL,
                    category VARCHAR(50) NOT NULL
                )
            """.trimIndent())
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

    ds.close()
}

// ==================== Foundations Benchmark ====================

fun runDb2FoundationsBenchmark(
    pointReadResults: MutableList<BenchResult>,
    insertResults: MutableList<BenchResult>,
    mixedResults: MutableList<BenchResult>,
) {
    val db2Config = Db2Config.builder(DB2_HOST, DB2_PORT, DB2_DB, DB2_USER, DB2_PASS).build()
    val poolConfig = PoolConfig.builder()
        .maximumPoolSize(Db2Cfg.POOL_SIZE)
        .minimumIdle(Db2Cfg.POOL_SIZE)
        .build()
    val ds = HikariDataSourceFactory.create(db2Config, poolConfig)
    val tx = ds.transactor()

    val readCodec = RowCodec.builder<BenchRow>()
        .field(Db2Types.integer, BenchRow::id)
        .field(Db2Types.varchar, BenchRow::name)
        .field(Db2Types.decimal, BenchRow::value)
        .field(Db2Types.boolean_, BenchRow::active)
        .field(Db2Types.timestamp, BenchRow::createdAt)
        .field(Db2Types.varchar, BenchRow::category)
        .build(::BenchRow)

    val selectByIdTemplate: Template.Query1<Int, java.util.Optional<BenchRow>> =
        Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
            .param(Db2Types.integer)
            .query(readCodec.first())

    val insertTemplate: Template.Update5<String, BigDecimal, Boolean, LocalDateTime, String> =
        Fragment.of("INSERT INTO bench_write (name, value, active, created_at, category) VALUES (")
            .param(Db2Types.varchar).append(", ")
            .param(Db2Types.decimal).append(", ")
            .param(Db2Types.boolean_).append(", ")
            .param(Db2Types.timestamp).append(", ")
            .param(Db2Types.varchar).append(")")
            .update()

    println("--- Foundations + HikariCP + Virtual Threads ---")

    println("  Warming up pool...")
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        val futures = (1..Db2Cfg.POOL_SIZE).map {
            exec.submit<Any?> {
                tx.executeJdbc { conn ->
                    conn.prepareStatement("SELECT 1 FROM SYSIBM.SYSDUMMY1").use { ps -> ps.executeQuery().close() }
                    null
                }
            }
        }
        futures.forEach(Future<*>::get)
    }

    println("  Running point reads...")
    pointReadResults.add(runDb2FoundationsPointRead(tx, selectByIdTemplate))

    println("  Running inserts...")
    insertResults.add(runDb2FoundationsInsert(tx, insertTemplate))

    println("  Running mixed 80/20...")
    mixedResults.add(runDb2FoundationsMixed(tx, selectByIdTemplate, insertTemplate))

    ds.close()
}

fun runDb2FoundationsPointRead(
    tx: TransactorJdbc,
    selectByIdTemplate: Template.Query1<Int, java.util.Optional<BenchRow>>,
): BenchResult {
    return runBenchmark("Foundations+VT", Db2Cfg.TASK_COUNT, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { i ->
        tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
    }
}

fun runDb2FoundationsInsert(
    tx: TransactorJdbc,
    insertTemplate: Template.Update5<String, BigDecimal, Boolean, LocalDateTime, String>,
): BenchResult {
    tx.executeJdbc { conn -> conn.createStatement().use { it.execute("DELETE FROM bench_write") }; null }

    return runBenchmark("Foundations+VT", Db2Cfg.TASK_COUNT, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { i ->
        tx.execute(insertTemplate.on("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
    }
}

fun runDb2FoundationsMixed(
    tx: TransactorJdbc,
    selectByIdTemplate: Template.Query1<Int, java.util.Optional<BenchRow>>,
    insertTemplate: Template.Update5<String, BigDecimal, Boolean, LocalDateTime, String>,
): BenchResult {
    tx.executeJdbc { conn -> conn.createStatement().use { it.execute("DELETE FROM bench_write") }; null }

    return runBenchmark("Foundations+VT", Db2Cfg.TASK_COUNT, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { i ->
        if (i % 5 == 0) {
            tx.execute(insertTemplate.on("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
        } else {
            tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
        }
    }
}

// ==================== Raw Vert.x Benchmark ====================

fun runDb2VertxBenchmark(
    pointReadResults: MutableList<BenchResult>,
    insertResults: MutableList<BenchResult>,
    mixedResults: MutableList<BenchResult>,
) {
    val vertx = Vertx.vertx()

    val connectOptions = DB2ConnectOptions()
        .setHost(DB2_HOST)
        .setPort(DB2_PORT)
        .setDatabase(DB2_DB)
        .setUser(DB2_USER)
        .setPassword(DB2_PASS)
        .setCachePreparedStatements(true)
        .setPreparedStatementCacheMaxSize(256)
        .setPipeliningLimit(256)

    val poolOptions = PoolOptions()
        .setMaxSize(Db2Cfg.POOL_SIZE)

    val pool = DB2Pool.pool(vertx, connectOptions, poolOptions)

    println("\n--- Vert.x + Virtual Threads ---")
    println("  (pipelining=256, prepared stmt cache=256)")

    println("  Warming up pool...")
    for (i in 1..Db2Cfg.POOL_SIZE) {
        pool.query("SELECT 1 FROM SYSIBM.SYSDUMMY1").execute().toCompletionStage().toCompletableFuture().join()
    }
    println("  Pool warm (${Db2Cfg.POOL_SIZE} sequential queries done)")

    println("  Running point reads...")
    pointReadResults.add(runDb2VertxPointRead(pool))

    println("  Running inserts...")
    insertResults.add(runDb2VertxInsert(pool))

    println("  Running mixed 80/20...")
    mixedResults.add(runDb2VertxMixed(pool))

    pool.close().toCompletionStage().toCompletableFuture().join()
    vertx.close().toCompletionStage().toCompletableFuture().join()
}

fun runDb2VertxPointRead(pool: DB2Pool): BenchResult {
    val sql = "SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ?"

    return runBenchmark("Vert.x", Db2Cfg.TASK_COUNT, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { i ->
        pool.preparedQuery(sql)
            .execute(VxTuple.of((i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture().join()
    }
}

fun runDb2VertxInsert(pool: DB2Pool): BenchResult {
    pool.query("DELETE FROM bench_write").execute().toCompletionStage().toCompletableFuture().join()

    val sql = "INSERT INTO bench_write (name, value, active, created_at, category) VALUES (?, ?, ?, ?, ?)"

    return runBenchmark("Vert.x", Db2Cfg.TASK_COUNT, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { i ->
        pool.preparedQuery(sql)
            .execute(VxTuple.of("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
            .toCompletionStage().toCompletableFuture().join()
    }
}

fun runDb2VertxMixed(pool: DB2Pool): BenchResult {
    pool.query("DELETE FROM bench_write").execute().toCompletionStage().toCompletableFuture().join()

    val selectSql = "SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ?"
    val insertSql = "INSERT INTO bench_write (name, value, active, created_at, category) VALUES (?, ?, ?, ?, ?)"

    return runBenchmark("Vert.x", Db2Cfg.TASK_COUNT, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { i ->
        if (i % 5 == 0) {
            pool.preparedQuery(insertSql)
                .execute(VxTuple.of("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
                .toCompletionStage().toCompletableFuture().join()
        } else {
            pool.preparedQuery(selectSql)
                .execute(VxTuple.of((i % ROW_COUNT) + 1))
                .toCompletionStage().toCompletableFuture().join()
        }
    }
}

// ==================== Pipelining-Advantage Benchmarks ====================

fun runDb2ScarceConnectionsBenchmark(
    scarceResults: MutableList<BenchResult>,
    batchResults: MutableList<BenchResult>,
) {
    val readCodec = RowCodec.builder<BenchRow>()
        .field(Db2Types.integer, BenchRow::id)
        .field(Db2Types.varchar, BenchRow::name)
        .field(Db2Types.decimal, BenchRow::value)
        .field(Db2Types.boolean_, BenchRow::active)
        .field(Db2Types.timestamp, BenchRow::createdAt)
        .field(Db2Types.varchar, BenchRow::category)
        .build(::BenchRow)

    val selectByIdTemplate: Template.Query1<Int, java.util.Optional<BenchRow>> =
        Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
            .param(Db2Types.integer)
            .query(readCodec.first())

    val selectSql = "SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ?"

    // --- JDBC with scarce connections ---
    println("\n--- Scarce Connections: Foundations + HikariCP (${Db2Cfg.SCARCE_CONNECTIONS} conns) ---")
    val db2Config = Db2Config.builder(DB2_HOST, DB2_PORT, DB2_DB, DB2_USER, DB2_PASS).build()
    val scarceDs = HikariDataSourceFactory.create(db2Config,
        PoolConfig.builder().maximumPoolSize(Db2Cfg.SCARCE_CONNECTIONS).minimumIdle(Db2Cfg.SCARCE_CONNECTIONS)
            .connectionTimeout(java.time.Duration.ofSeconds(120)).build())
    val scarceTx = scarceDs.transactor()

    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..500).map { i -> exec.submit<Any?> { scarceTx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) } }
            .forEach(Future<*>::get)
    }

    println("  Running ${Db2Cfg.SCARCE_TASK_COUNT} point reads...")
    scarceResults.add(runBenchmark("Foundations+VT", Db2Cfg.SCARCE_TASK_COUNT, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { i ->
        scarceTx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
    })

    println("  Running batch fan-out (${Db2Cfg.BATCH_TASK_COUNT} x ${Db2Cfg.BATCH_SIZE})...")
    batchResults.add(runBatchFanOutBenchmark("Foundations+VT", Db2Cfg.BATCH_TASK_COUNT, Db2Cfg.BATCH_SIZE, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { taskIdx, batchIdx ->
        scarceTx.execute(selectByIdTemplate.on(((taskIdx * Db2Cfg.BATCH_SIZE + batchIdx) % ROW_COUNT) + 1))
    })

    scarceDs.close()

    // --- Vert.x with scarce connections ---
    println("\n--- Scarce Connections: Vert.x (${Db2Cfg.SCARCE_CONNECTIONS} conns) ---")
    val vertx = Vertx.vertx()
    val connectOptions = DB2ConnectOptions()
        .setHost(DB2_HOST).setPort(DB2_PORT).setDatabase(DB2_DB)
        .setUser(DB2_USER).setPassword(DB2_PASS)
        .setCachePreparedStatements(true).setPreparedStatementCacheMaxSize(256)
        .setPipeliningLimit(256)
    val vxPool = DB2Pool.pool(vertx, connectOptions, PoolOptions().setMaxSize(Db2Cfg.SCARCE_CONNECTIONS))

    for (i in 1..Db2Cfg.SCARCE_CONNECTIONS) {
        vxPool.query("SELECT 1 FROM SYSIBM.SYSDUMMY1").execute().toCompletionStage().toCompletableFuture().join()
    }
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..500).map { i ->
            exec.submit<Any?> {
                vxPool.preparedQuery(selectSql).execute(VxTuple.of((i % ROW_COUNT) + 1))
                    .toCompletionStage().toCompletableFuture().join()
            }
        }.forEach(Future<*>::get)
    }

    println("  Running ${Db2Cfg.SCARCE_TASK_COUNT} point reads...")
    scarceResults.add(runBenchmark("Vert.x", Db2Cfg.SCARCE_TASK_COUNT, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { i ->
        vxPool.preparedQuery(selectSql).execute(VxTuple.of((i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture().join()
    })

    println("  Running batch fan-out (${Db2Cfg.BATCH_TASK_COUNT} x ${Db2Cfg.BATCH_SIZE})...")
    batchResults.add(runBatchFanOutBenchmark("Vert.x", Db2Cfg.BATCH_TASK_COUNT, Db2Cfg.BATCH_SIZE, Db2Cfg.WARMUP_OPS, Db2Cfg.MEASURED_ITERATIONS) { taskIdx, batchIdx ->
        vxPool.preparedQuery(selectSql).execute(VxTuple.of(((taskIdx * Db2Cfg.BATCH_SIZE + batchIdx) % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture().join()
    })

    vxPool.close().toCompletionStage().toCompletableFuture().join()
    vertx.close().toCompletionStage().toCompletableFuture().join()
}
