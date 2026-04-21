package dev.typr.foundations.benchmark

import dev.typr.foundations.Cursor
import dev.typr.foundations.Fragment
import dev.typr.foundations.Operation
import dev.typr.foundations.hikari.HikariDataSourceFactory
import dev.typr.foundations.hikari.PoolConfig
import dev.typr.foundations.pg.PgPipelineConfig
import dev.typr.foundations.pg.PgPipelinePool
import io.vertx.core.Vertx
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import org.HdrHistogram.ConcurrentHistogram
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

// ==================== Configuration ====================

const val STREAM_POOL_SIZE = 10
const val STREAM_ROW_COUNT = 10_000
const val STREAM_MEASURED_ITERATIONS = 3
const val STREAM_WARMUP = 1

val FETCH_SIZES = listOf(100, 1000)
val CONCURRENCY_LEVELS = listOf(1, 10, 50)
val LATENCIES_MS = listOf(0L, 1L, 5L)

const val PROXY_BASE_PORT = 16432

// ==================== Main ====================

fun main() = runStreamingBenchmark()

fun runStreamingBenchmark() {
    println("=== Streaming Cursor Benchmark ===")
    println("PostgreSQL at $PG_HOST:$PG_PORT/$PG_DB")
    println("Pool size: $STREAM_POOL_SIZE connections")
    println("Table: bench_read ($STREAM_ROW_COUNT rows, 6 columns)")
    println("Fetch sizes: $FETCH_SIZES")
    println("Concurrency levels: $CONCURRENCY_LEVELS")
    println("Simulated one-way latencies: $LATENCIES_MS ms (RTT = 2x)")
    println("Measured iterations: $STREAM_MEASURED_ITERATIONS")
    println()

    val selectAllSql = "SELECT id, name, value, active, created_at, category FROM bench_read ORDER BY id"

    val report = MarkdownReport("Streaming Cursor Benchmark")

    for (latencyMs in LATENCIES_MS) {
        val proxyPort = PROXY_BASE_PORT + latencyMs.toInt()
        val proxy = if (latencyMs > 0) {
            LatencyProxy(proxyPort, PG_HOST, PG_PORT, latencyMs).also { it.start() }
        } else null
        val effectivePort = if (latencyMs > 0) proxyPort else PG_PORT
        val rttLabel = if (latencyMs > 0) "${latencyMs * 2}ms RTT" else "local"

        val concurrencyLevels = if (latencyMs == 0L) CONCURRENCY_LEVELS
            else if (latencyMs <= 2) listOf(1, 10)
            else listOf(1)

        println("========== Latency: $rttLabel ==========")
        println()

        for (fetchSize in FETCH_SIZES) {
            for (concurrency in concurrencyLevels) {
                val results = mutableListOf<BenchResult>()

                println("--- fetchSize=$fetchSize, concurrency=$concurrency ---")

                results.add(runRawJdbcStreaming(selectAllSql, fetchSize, concurrency, effectivePort))
                results.add(runPgPipelineStreaming(selectAllSql, fetchSize, concurrency, effectivePort))
                results.add(runVertxCursorStreaming(selectAllSql, fetchSize, concurrency, effectivePort))

                val title = "$rttLabel, $STREAM_ROW_COUNT rows, fetchSize=$fetchSize, concurrency=$concurrency"
                printStreamResults(title, results)

                report.section(
                    "$rttLabel, fetchSize=$fetchSize, concurrency=$concurrency",
                    "$STREAM_ROW_COUNT rows, $STREAM_MEASURED_ITERATIONS iterations.",
                    results,
                    "scans/sec",
                    """
                    pool.execute(Operation.streaming(selectAll, codec, fetchSize))
                        .use(cursor -> {
                            while (cursor.hasNext()) cursor.next();
                        });
                    """.trimIndent()
                )
            }
        }

        proxy?.close()
    }

    report.writeTo(REPORT_DIR, "streaming.md")
}

// ==================== Raw JDBC Streaming ====================

fun runRawJdbcStreaming(sql: String, fetchSize: Int, concurrency: Int, port: Int): BenchResult {
    val pgConfig = pgConfig(port)
    val poolSize = maxOf(STREAM_POOL_SIZE, concurrency)
    val poolConfig = PoolConfig.builder()
        .maximumPoolSize(poolSize)
        .minimumIdle(poolSize)
        .build()
    val ds = HikariDataSourceFactory.create(pgConfig, poolConfig)

    for (w in 0 until STREAM_WARMUP) {
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (1..concurrency).map {
                exec.submit<Any?> {
                    ds.connection.use { conn ->
                        conn.autoCommit = false
                        conn.prepareStatement(
                            sql,
                            java.sql.ResultSet.TYPE_FORWARD_ONLY,
                            java.sql.ResultSet.CONCUR_READ_ONLY,
                        ).use { ps ->
                            ps.fetchSize = fetchSize
                            ps.executeQuery().use { rs ->
                                var count = 0
                                while (rs.next()) {
                                    readBenchRow(rs)
                                    count++
                                }
                                count
                            }
                        }
                    }
                }
            }
            futures.forEach(Future<*>::get)
        }
    }

    val histogram = ConcurrentHistogram(1, 60_000_000_000L, 3)
    val wallTimes = LongArray(STREAM_MEASURED_ITERATIONS)

    for (iter in 0 until STREAM_MEASURED_ITERATIONS) {
        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (1..concurrency).map {
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    ds.connection.use { conn ->
                        conn.autoCommit = false
                        conn.prepareStatement(
                            sql,
                            java.sql.ResultSet.TYPE_FORWARD_ONLY,
                            java.sql.ResultSet.CONCUR_READ_ONLY,
                        ).use { ps ->
                            ps.fetchSize = fetchSize
                            ps.executeQuery().use { rs ->
                                var count = 0
                                while (rs.next()) {
                                    readBenchRow(rs)
                                    count++
                                }
                                count
                            }
                        }
                    }
                    histogram.recordValue(System.nanoTime() - start)
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    ds.close()

    val medianWall = wallTimes.sorted()[STREAM_MEASURED_ITERATIONS / 2]
    val totalScans = concurrency.toLong()
    return BenchResult("Raw JDBC", totalScans, medianWall, histogram)
}

// ==================== PgPipeline Streaming ====================

fun runPgPipelineStreaming(sql: String, fetchSize: Int, concurrency: Int, port: Int): BenchResult {
    val pgConfig = pgConfig(port)
    val poolSize = maxOf(STREAM_POOL_SIZE, concurrency)
    val pipelineConfig = PgPipelineConfig.builder()
        .connectionCount(poolSize)
        .pipeliningLimit(256)
        .defaultFetchSize(fetchSize)
        .build()
    val pool = PgPipelinePool.create(pgConfig, pipelineConfig)

    val readCodec = benchReadCodec()

    val streamOp: Operation<Cursor<BenchRow>> = Operation.Streaming(
        Fragment.of(sql),
        readCodec,
        fetchSize,
    )

    for (w in 0 until STREAM_WARMUP) {
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (1..concurrency).map {
                exec.submit<Any?> {
                    pool.execute(streamOp).use { cursor ->
                        var count = 0
                        while (cursor.hasNext()) {
                            cursor.next()
                            count++
                        }
                        count
                    }
                }
            }
            futures.forEach(Future<*>::get)
        }
    }

    val histogram = ConcurrentHistogram(1, 60_000_000_000L, 3)
    val wallTimes = LongArray(STREAM_MEASURED_ITERATIONS)

    for (iter in 0 until STREAM_MEASURED_ITERATIONS) {
        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (1..concurrency).map {
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    pool.execute(streamOp).use { cursor ->
                        var count = 0
                        while (cursor.hasNext()) {
                            cursor.next()
                            count++
                        }
                        count
                    }
                    histogram.recordValue(System.nanoTime() - start)
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    pool.close()

    val medianWall = wallTimes.sorted()[STREAM_MEASURED_ITERATIONS / 2]
    val totalScans = concurrency.toLong()
    return BenchResult("Foundations+PgPipe", totalScans, medianWall, histogram)
}

// ==================== Vert.x Cursor Streaming ====================

fun runVertxCursorStreaming(sql: String, fetchSize: Int, concurrency: Int, port: Int): BenchResult {
    val vertx = Vertx.vertx()

    val connectOptions = PgConnectOptions()
        .setHost(PG_HOST)
        .setPort(port)
        .setDatabase(PG_DB)
        .setUser(PG_USER)
        .setPassword(PG_PASS)
        .setCachePreparedStatements(true)
        .setPreparedStatementCacheMaxSize(256)

    val poolOptions = PoolOptions().setMaxSize(maxOf(STREAM_POOL_SIZE, concurrency))
    val pool = PgPool.pool(vertx, connectOptions, poolOptions)

    val pgSql = sql

    for (w in 0 until STREAM_WARMUP) {
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (1..concurrency).map {
                exec.submit<Any?> {
                    val conn = pool.connection.toCompletionStage().toCompletableFuture().join()
                    val tx = conn.begin().toCompletionStage().toCompletableFuture().join()
                    val prepared = conn.prepare(pgSql).toCompletionStage().toCompletableFuture().join()
                    val cursor = prepared.cursor()
                    var count = 0
                    while (true) {
                        val rowSet = cursor.read(fetchSize).toCompletionStage().toCompletableFuture().join()
                        for (row in rowSet) {
                            BenchRow(
                                row.getInteger("id"),
                                row.getString("name"),
                                row.getNumeric("value").bigDecimalValue(),
                                row.getBoolean("active"),
                                row.getLocalDateTime("created_at"),
                                row.getString("category"),
                            )
                            count++
                        }
                        if (!cursor.hasMore()) break
                    }
                    cursor.close().toCompletionStage().toCompletableFuture().join()
                    tx.commit().toCompletionStage().toCompletableFuture().join()
                    conn.close().toCompletionStage().toCompletableFuture().join()
                    count
                }
            }
            futures.forEach(Future<*>::get)
        }
    }

    val histogram = ConcurrentHistogram(1, 60_000_000_000L, 3)
    val wallTimes = LongArray(STREAM_MEASURED_ITERATIONS)

    for (iter in 0 until STREAM_MEASURED_ITERATIONS) {
        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (1..concurrency).map {
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    val conn = pool.connection.toCompletionStage().toCompletableFuture().join()
                    val tx = conn.begin().toCompletionStage().toCompletableFuture().join()
                    val prepared = conn.prepare(pgSql).toCompletionStage().toCompletableFuture().join()
                    val cursor = prepared.cursor()
                    var count = 0
                    while (true) {
                        val rowSet = cursor.read(fetchSize).toCompletionStage().toCompletableFuture().join()
                        for (row in rowSet) {
                            BenchRow(
                                row.getInteger("id"),
                                row.getString("name"),
                                row.getNumeric("value").bigDecimalValue(),
                                row.getBoolean("active"),
                                row.getLocalDateTime("created_at"),
                                row.getString("category"),
                            )
                            count++
                        }
                        if (!cursor.hasMore()) break
                    }
                    cursor.close().toCompletionStage().toCompletableFuture().join()
                    tx.commit().toCompletionStage().toCompletableFuture().join()
                    conn.close().toCompletionStage().toCompletableFuture().join()
                    histogram.recordValue(System.nanoTime() - start)
                    count
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    try { pool.close().toCompletionStage().toCompletableFuture().get(5, TimeUnit.SECONDS) } catch (_: Exception) {}
    try { vertx.close().toCompletionStage().toCompletableFuture().get(5, TimeUnit.SECONDS) } catch (_: Exception) {}

    val medianWall = wallTimes.sorted()[STREAM_MEASURED_ITERATIONS / 2]
    val totalScans = concurrency.toLong()
    return BenchResult("Vert.x (cursor)", totalScans, medianWall, histogram)
}

// ==================== Reporting ====================

private fun printStreamResults(title: String, results: List<BenchResult>) {
    println()
    println("=== Streaming: $title ===")
    println("%-16s %12s %10s %10s %10s %10s".format(
        "", "scans/sec", "p50", "p95", "p99", "wall"
    ))
    for (r in results) {
        println("%-16s %,12.1f %8dus %8dus %8dus %7dms".format(
            r.name,
            r.opsPerSec,
            r.p50us,
            r.p95us,
            r.p99us,
            r.wallTimeMs,
        ))
    }
    println()
}
