package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.MariaTypes
import dev.typr.foundations.RowCodec
import dev.typr.foundations.Template
import dev.typr.foundations.TransactorJdbc
import dev.typr.foundations.connect.MariaConfig
import dev.typr.foundations.hikari.HikariDataSourceFactory
import dev.typr.foundations.hikari.PoolConfig
import io.vertx.core.Vertx
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.Tuple as VxTuple
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.Future

// ==================== Configuration ====================

const val MARIA_HOST = "localhost"
const val MARIA_PORT = 3307
const val MARIA_DB = "typr"
const val MARIA_USER = "typr"
const val MARIA_PASS = "password"

private object MariaCfg {
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
    println("=== MariaDB Concurrent Throughput Benchmark ===")
    println("MariaDB at $MARIA_HOST:$MARIA_PORT/$MARIA_DB")
    println("Pool size: ${MariaCfg.POOL_SIZE} connections (both sides)")
    println("Concurrency: ${MariaCfg.TASK_COUNT} concurrent tasks")
    println("Warmup: ${MariaCfg.WARMUP_OPS} ops, Measured: ${MariaCfg.TASK_COUNT} ops x ${MariaCfg.MEASURED_ITERATIONS} iterations")
    println()

    setupMariaDatabase()

    val pointReadResults = mutableListOf<BenchResult>()
    val insertResults = mutableListOf<BenchResult>()
    val mixedResults = mutableListOf<BenchResult>()

    runMariaFoundationsBenchmark(pointReadResults, insertResults, mixedResults)
    runMariaVertxBenchmark(pointReadResults, insertResults, mixedResults)

    printResults("MariaDB: Point Read (SELECT WHERE id = ?)", pointReadResults, "ops/sec")
    printResults("MariaDB: Single Insert", insertResults, "ops/sec")
    printResults("MariaDB: Mixed 80/20 Read/Write", mixedResults, "ops/sec")

    println()
    println("=" .repeat(70))
    println("=== Pipelining Advantage: Scarce Connections & Batch Fan-Out ===")
    println("=" .repeat(70))

    val scarceResults = mutableListOf<BenchResult>()
    val batchResults = mutableListOf<BenchResult>()

    runMariaScarceConnectionsBenchmark(scarceResults, batchResults)

    printResults("MariaDB: Scarce Connections: ${MariaCfg.SCARCE_TASK_COUNT} point reads, ${MariaCfg.SCARCE_CONNECTIONS} conns", scarceResults, "ops/sec")
    printResults("MariaDB: Batch Fan-Out: ${MariaCfg.BATCH_TASK_COUNT} page loads x ${MariaCfg.BATCH_SIZE} queries, ${MariaCfg.BATCH_CONNECTIONS} conns", batchResults, "ops/sec")
}

// ==================== Database Setup ====================

fun setupMariaDatabase() {
    val mariaConfig = MariaConfig.builder(MARIA_HOST, MARIA_PORT, MARIA_DB, MARIA_USER, MARIA_PASS).build()
    val ds = HikariDataSourceFactory.create(mariaConfig, PoolConfig.builder().maximumPoolSize(2).minimumIdle(2).build())

    ds.getConnection().use { conn ->
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
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
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

fun runMariaFoundationsBenchmark(
    pointReadResults: MutableList<BenchResult>,
    insertResults: MutableList<BenchResult>,
    mixedResults: MutableList<BenchResult>,
) {
    val mariaConfig = MariaConfig.builder(MARIA_HOST, MARIA_PORT, MARIA_DB, MARIA_USER, MARIA_PASS).build()
    val poolConfig = PoolConfig.builder()
        .maximumPoolSize(MariaCfg.POOL_SIZE)
        .minimumIdle(MariaCfg.POOL_SIZE)
        .build()
    val ds = HikariDataSourceFactory.create(mariaConfig, poolConfig)
    val tx = ds.transactor()

    val readCodec = RowCodec.builder<BenchRow>()
        .field(MariaTypes.int_, BenchRow::id)
        .field(MariaTypes.varchar, BenchRow::name)
        .field(MariaTypes.decimal, BenchRow::value)
        .field(MariaTypes.bool, BenchRow::active)
        .field(MariaTypes.timestamp, BenchRow::createdAt)
        .field(MariaTypes.varchar, BenchRow::category)
        .build(::BenchRow)

    val selectByIdTemplate: Template.Query1<Int, java.util.Optional<BenchRow>> =
        Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
            .param(MariaTypes.int_)
            .query(readCodec.first())

    val insertTemplate: Template.Update5<String, BigDecimal, Boolean, LocalDateTime, String> =
        Fragment.of("INSERT INTO bench_write (name, value, active, created_at, category) VALUES (")
            .param(MariaTypes.varchar).append(", ")
            .param(MariaTypes.decimal).append(", ")
            .param(MariaTypes.bool).append(", ")
            .param(MariaTypes.timestamp).append(", ")
            .param(MariaTypes.varchar).append(")")
            .update()

    println("--- Foundations + HikariCP + Virtual Threads ---")

    println("  Warming up pool...")
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        val futures = (1..MariaCfg.POOL_SIZE).map {
            exec.submit<Any?> {
                tx.executeJdbc { conn ->
                    conn.prepareStatement("SELECT 1").use { ps -> ps.executeQuery().close() }
                    null
                }
            }
        }
        futures.forEach(Future<*>::get)
    }

    println("  Running point reads...")
    pointReadResults.add(runMariaFoundationsPointRead(tx, selectByIdTemplate))

    println("  Running inserts...")
    insertResults.add(runMariaFoundationsInsert(tx, insertTemplate))

    println("  Running mixed 80/20...")
    mixedResults.add(runMariaFoundationsMixed(tx, selectByIdTemplate, insertTemplate))

    ds.close()
}

fun runMariaFoundationsPointRead(
    tx: TransactorJdbc,
    selectByIdTemplate: Template.Query1<Int, java.util.Optional<BenchRow>>,
): BenchResult {
    return runBenchmark("Foundations+VT", MariaCfg.TASK_COUNT, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { i ->
        tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
    }
}

fun runMariaFoundationsInsert(
    tx: TransactorJdbc,
    insertTemplate: Template.Update5<String, BigDecimal, Boolean, LocalDateTime, String>,
): BenchResult {
    tx.executeJdbc { conn -> conn.createStatement().use { it.execute("TRUNCATE TABLE bench_write") }; null }

    return runBenchmark("Foundations+VT", MariaCfg.TASK_COUNT, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { i ->
        tx.execute(insertTemplate.on("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
    }
}

fun runMariaFoundationsMixed(
    tx: TransactorJdbc,
    selectByIdTemplate: Template.Query1<Int, java.util.Optional<BenchRow>>,
    insertTemplate: Template.Update5<String, BigDecimal, Boolean, LocalDateTime, String>,
): BenchResult {
    tx.executeJdbc { conn -> conn.createStatement().use { it.execute("TRUNCATE TABLE bench_write") }; null }

    return runBenchmark("Foundations+VT", MariaCfg.TASK_COUNT, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { i ->
        if (i % 5 == 0) {
            tx.execute(insertTemplate.on("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
        } else {
            tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
        }
    }
}

// ==================== Raw Vert.x Benchmark ====================

fun runMariaVertxBenchmark(
    pointReadResults: MutableList<BenchResult>,
    insertResults: MutableList<BenchResult>,
    mixedResults: MutableList<BenchResult>,
) {
    val vertx = Vertx.vertx()

    val connectOptions = MySQLConnectOptions()
        .setHost(MARIA_HOST)
        .setPort(MARIA_PORT)
        .setDatabase(MARIA_DB)
        .setUser(MARIA_USER)
        .setPassword(MARIA_PASS)
        .setCachePreparedStatements(true)
        .setPreparedStatementCacheMaxSize(256)
        .setPipeliningLimit(256)

    val poolOptions = PoolOptions()
        .setMaxSize(MariaCfg.POOL_SIZE)

    val pool = MySQLPool.pool(vertx, connectOptions, poolOptions)

    println("\n--- Vert.x + Virtual Threads ---")
    println("  (pipelining=256, prepared stmt cache=256)")

    println("  Warming up pool...")
    for (i in 1..MariaCfg.POOL_SIZE) {
        pool.query("SELECT 1").execute().toCompletionStage().toCompletableFuture().join()
    }
    println("  Pool warm (${MariaCfg.POOL_SIZE} sequential queries done)")

    println("  Running point reads...")
    pointReadResults.add(runMariaVertxPointRead(pool))

    println("  Running inserts...")
    insertResults.add(runMariaVertxInsert(pool))

    println("  Running mixed 80/20...")
    mixedResults.add(runMariaVertxMixed(pool))

    pool.close().toCompletionStage().toCompletableFuture().join()
    vertx.close().toCompletionStage().toCompletableFuture().join()
}

fun runMariaVertxPointRead(pool: MySQLPool): BenchResult {
    val sql = "SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ?"

    return runBenchmark("Vert.x", MariaCfg.TASK_COUNT, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { i ->
        pool.preparedQuery(sql)
            .execute(VxTuple.of((i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture().join()
    }
}

fun runMariaVertxInsert(pool: MySQLPool): BenchResult {
    pool.query("TRUNCATE TABLE bench_write").execute().toCompletionStage().toCompletableFuture().join()

    val sql = "INSERT INTO bench_write (name, value, active, created_at, category) VALUES (?, ?, ?, ?, ?)"

    return runBenchmark("Vert.x", MariaCfg.TASK_COUNT, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { i ->
        pool.preparedQuery(sql)
            .execute(VxTuple.of("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
            .toCompletionStage().toCompletableFuture().join()
    }
}

fun runMariaVertxMixed(pool: MySQLPool): BenchResult {
    pool.query("TRUNCATE TABLE bench_write").execute().toCompletionStage().toCompletableFuture().join()

    val selectSql = "SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ?"
    val insertSql = "INSERT INTO bench_write (name, value, active, created_at, category) VALUES (?, ?, ?, ?, ?)"

    return runBenchmark("Vert.x", MariaCfg.TASK_COUNT, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { i ->
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

fun runMariaScarceConnectionsBenchmark(
    scarceResults: MutableList<BenchResult>,
    batchResults: MutableList<BenchResult>,
) {
    val readCodec = RowCodec.builder<BenchRow>()
        .field(MariaTypes.int_, BenchRow::id)
        .field(MariaTypes.varchar, BenchRow::name)
        .field(MariaTypes.decimal, BenchRow::value)
        .field(MariaTypes.bool, BenchRow::active)
        .field(MariaTypes.timestamp, BenchRow::createdAt)
        .field(MariaTypes.varchar, BenchRow::category)
        .build(::BenchRow)

    val selectByIdTemplate: Template.Query1<Int, java.util.Optional<BenchRow>> =
        Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
            .param(MariaTypes.int_)
            .query(readCodec.first())

    val selectSql = "SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ?"

    // --- JDBC with scarce connections ---
    println("\n--- Scarce Connections: Foundations + HikariCP (${MariaCfg.SCARCE_CONNECTIONS} conns) ---")
    val mariaConfig = MariaConfig.builder(MARIA_HOST, MARIA_PORT, MARIA_DB, MARIA_USER, MARIA_PASS).build()
    val scarceDs = HikariDataSourceFactory.create(mariaConfig,
        PoolConfig.builder().maximumPoolSize(MariaCfg.SCARCE_CONNECTIONS).minimumIdle(MariaCfg.SCARCE_CONNECTIONS)
            .connectionTimeout(java.time.Duration.ofSeconds(120)).build())
    val scarceTx = scarceDs.transactor()

    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..500).map { i -> exec.submit<Any?> { scarceTx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) } }
            .forEach(Future<*>::get)
    }

    println("  Running ${MariaCfg.SCARCE_TASK_COUNT} point reads...")
    scarceResults.add(runBenchmark("Foundations+VT", MariaCfg.SCARCE_TASK_COUNT, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { i ->
        scarceTx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
    })

    println("  Running batch fan-out (${MariaCfg.BATCH_TASK_COUNT} x ${MariaCfg.BATCH_SIZE})...")
    batchResults.add(runBatchFanOutBenchmark("Foundations+VT", MariaCfg.BATCH_TASK_COUNT, MariaCfg.BATCH_SIZE, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { taskIdx, batchIdx ->
        scarceTx.execute(selectByIdTemplate.on(((taskIdx * MariaCfg.BATCH_SIZE + batchIdx) % ROW_COUNT) + 1))
    })

    scarceDs.close()

    // --- Vert.x with scarce connections ---
    println("\n--- Scarce Connections: Vert.x (${MariaCfg.SCARCE_CONNECTIONS} conns) ---")
    val vertx = Vertx.vertx()
    val connectOptions = MySQLConnectOptions()
        .setHost(MARIA_HOST).setPort(MARIA_PORT).setDatabase(MARIA_DB)
        .setUser(MARIA_USER).setPassword(MARIA_PASS)
        .setCachePreparedStatements(true).setPreparedStatementCacheMaxSize(256)
        .setPipeliningLimit(256)
    val vxPool = MySQLPool.pool(vertx, connectOptions, PoolOptions().setMaxSize(MariaCfg.SCARCE_CONNECTIONS))

    for (i in 1..MariaCfg.SCARCE_CONNECTIONS) {
        vxPool.query("SELECT 1").execute().toCompletionStage().toCompletableFuture().join()
    }
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..500).map { i ->
            exec.submit<Any?> {
                vxPool.preparedQuery(selectSql).execute(VxTuple.of((i % ROW_COUNT) + 1))
                    .toCompletionStage().toCompletableFuture().join()
            }
        }.forEach(Future<*>::get)
    }

    println("  Running ${MariaCfg.SCARCE_TASK_COUNT} point reads...")
    scarceResults.add(runBenchmark("Vert.x", MariaCfg.SCARCE_TASK_COUNT, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { i ->
        vxPool.preparedQuery(selectSql).execute(VxTuple.of((i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture().join()
    })

    println("  Running batch fan-out (${MariaCfg.BATCH_TASK_COUNT} x ${MariaCfg.BATCH_SIZE})...")
    batchResults.add(runBatchFanOutBenchmark("Vert.x", MariaCfg.BATCH_TASK_COUNT, MariaCfg.BATCH_SIZE, MariaCfg.WARMUP_OPS, MariaCfg.MEASURED_ITERATIONS) { taskIdx, batchIdx ->
        vxPool.preparedQuery(selectSql).execute(VxTuple.of(((taskIdx * MariaCfg.BATCH_SIZE + batchIdx) % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture().join()
    })

    vxPool.close().toCompletionStage().toCompletableFuture().join()
    vertx.close().toCompletionStage().toCompletableFuture().join()
}
