package dev.typr.foundations.benchmark

import org.HdrHistogram.ConcurrentHistogram
import java.util.concurrent.Executors
import java.util.concurrent.Future

fun main() = runTxLatencyBenchmark()

fun runTxLatencyBenchmark() {
    println("=== Transaction Latency Benchmark: Sequential single-read tx ===")
    println()

    val conns = 10
    val ops = 200
    val warmup = 50
    val iterations = 5

    val selectByIdTemplate = selectByIdTemplate()
    val rawSelectSql = RAW_SELECT_SQL

    val report = MarkdownReport("Transaction Latency Benchmark")

    for (latencyMs in listOf(0L, 1L, 5L)) {
        val rtt = latencyMs * 2
        val rttLabel = if (latencyMs == 0L) "local" else "${rtt}ms RTT"

        proxiedPort(latencyMs, 16432).use { proxied ->
            val pipePool = createPipelinePoolWithTx(proxied.port, conns, conns - 1)
            val hikariDs = createHikariPool(proxied.port, conns)

            // Warmup
            Executors.newVirtualThreadPerTaskExecutor().use { exec ->
                (1..warmup).map { i -> exec.submit<Any?> {
                    pipePool.transact { tx -> tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) }
                }}.forEach(Future<*>::get)
            }
            Executors.newVirtualThreadPerTaskExecutor().use { exec ->
                (1..warmup).map { i -> exec.submit<Any?> {
                    hikariDs.connection.use { conn ->
                        conn.autoCommit = false
                        conn.prepareStatement(rawSelectSql).use { ps ->
                            ps.setInt(1, (i % ROW_COUNT) + 1)
                            ps.executeQuery().use { rs -> rs.next() }
                        }
                        conn.commit()
                    }
                }}.forEach(Future<*>::get)
            }

            // Measure PgPipe
            val pipeHist = ConcurrentHistogram(1, 600_000_000_000L, 3)
            val pipeWallStart = System.nanoTime()
            for (iter in 0 until iterations) {
                for (i in 0 until ops) {
                    val start = System.nanoTime()
                    pipePool.transact { tx -> tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) }
                    pipeHist.recordValue(System.nanoTime() - start)
                }
            }
            val pipeWallMs = (System.nanoTime() - pipeWallStart) / 1_000_000
            val pipeResult = BenchResult("Foundations+PgPipe (mutable)", (ops * iterations).toLong(), pipeWallMs, pipeHist)

            // Measure PgPipe readonly
            val pipeReadonlyHist = ConcurrentHistogram(1, 600_000_000_000L, 3)
            val pipeReadonlyWallStart = System.nanoTime()
            for (iter in 0 until iterations) {
                for (i in 0 until ops) {
                    val start = System.nanoTime()
                    pipePool.transactRead { conn -> conn.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) }
                    pipeReadonlyHist.recordValue(System.nanoTime() - start)
                }
            }
            val pipeReadonlyWallMs = (System.nanoTime() - pipeReadonlyWallStart) / 1_000_000
            val pipeReadonlyResult = BenchResult("Foundations+PgPipe (readonly)", (ops * iterations).toLong(), pipeReadonlyWallMs, pipeReadonlyHist)

            // Measure JDBC
            val jdbcHist = ConcurrentHistogram(1, 600_000_000_000L, 3)
            val jdbcWallStart = System.nanoTime()
            for (iter in 0 until iterations) {
                for (i in 0 until ops) {
                    val start = System.nanoTime()
                    hikariDs.connection.use { conn ->
                        conn.autoCommit = false
                        try {
                            conn.prepareStatement(rawSelectSql).use { ps ->
                                ps.setInt(1, (i % ROW_COUNT) + 1)
                                ps.executeQuery().use { rs ->
                                    if (rs.next()) readBenchRow(rs) else null
                                }
                            }
                            conn.commit()
                        } catch (e: Exception) {
                            conn.rollback()
                            throw e
                        }
                    }
                    jdbcHist.recordValue(System.nanoTime() - start)
                }
            }
            val jdbcWallMs = (System.nanoTime() - jdbcWallStart) / 1_000_000
            val jdbcResult = BenchResult("Raw JDBC (tx)", (ops * iterations).toLong(), jdbcWallMs, jdbcHist)

            val results = listOf(pipeReadonlyResult, pipeResult, jdbcResult)
            printResults("Single-Read Tx ($rttLabel, sequential)", results, "ops/sec")

            report.section(
                "Single-Read Tx ($rttLabel)",
                "Sequential execution, $ops ops x $iterations iterations, $conns connections.",
                results,
                "ops/sec",
                """
                pool.transact(tx -> tx.execute(selectById.on(42)))
                """.trimIndent()
            )

            pipePool.close()
            hikariDs.close()
        }
    }

    report.writeTo(REPORT_DIR, "tx-latency.md")
}
