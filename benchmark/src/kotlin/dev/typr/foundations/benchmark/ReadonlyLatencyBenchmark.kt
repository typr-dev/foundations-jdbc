package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.pg.PgPipelinePool
import java.util.concurrent.Executors
import java.util.concurrent.Future

fun main() {
    println("=== Readonly combine() vs sequential @ 10ms RTT ===")
    println("5 queries per task, $TX_POOL_SIZE connections, 10ms RTT")
    println()

    val selectTpl = selectByIdTemplate()
    val rawSelectSql = RAW_SELECT_SQL

    val report = MarkdownReport("Readonly Latency Benchmark")

    proxiedPort(5L, 18432).use { proxied ->
        val pipePool = createPipelinePoolWithTx(proxied.port, TX_POOL_SIZE, TX_POOL_SIZE - 1)
        for (i in 1..20) {
            pipePool.execute(selectTpl.on((i % ROW_COUNT) + 1))
        }

        val hikariDs = createHikariPool(proxied.port, TX_POOL_SIZE)
        for (i in 1..20) {
            hikariDs.connection.use { conn ->
                conn.prepareStatement(rawSelectSql).use { ps ->
                    ps.setInt(1, (i % ROW_COUNT) + 1)
                    ps.executeQuery().use { rs -> rs.next() }
                }
            }
        }

        val results = mutableListOf<BenchResult>()

        println("  transactRead + combine (5 queries)...")
        results.add(runTimedBenchmark("readonly + combine", CONCURRENCY, WARMUP_SECONDS, MEASURE_SECONDS) { i ->
            pipePool.transactRead { conn ->
                @Suppress("UNCHECKED_CAST")
                val ops = (0 until 5).map { j ->
                    selectTpl.on(((i + j) % ROW_COUNT) + 1)
                }
                val combined = ops.drop(1).fold(ops.first() as dev.typr.foundations.Operation<*>) { acc, op ->
                    acc.combine(op)
                }
                conn.execute(combined)
            }
        })

        println("  transactRead + sequential (5 queries)...")
        results.add(runTimedBenchmark("readonly + sequential", CONCURRENCY, WARMUP_SECONDS, MEASURE_SECONDS) { i ->
            pipePool.transactRead { conn ->
                for (j in 0 until 5) {
                    conn.queryFirst(
                        Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
                            .append(Fragment.value(((i + j) % ROW_COUNT) + 1, dev.typr.foundations.PgTypes.int4)),
                        benchReadCodec()
                    )
                }
            }
        })

        println("  transact + combine (5 queries)...")
        results.add(runTimedBenchmark("mutable + combine", CONCURRENCY, WARMUP_SECONDS, MEASURE_SECONDS) { i ->
            pipePool.transact { conn ->
                @Suppress("UNCHECKED_CAST")
                val ops = (0 until 5).map { j ->
                    selectTpl.on(((i + j) % ROW_COUNT) + 1)
                }
                val combined = ops.drop(1).fold(ops.first() as dev.typr.foundations.Operation<*>) { acc, op ->
                    acc.combine(op)
                }
                conn.execute(combined)
            }
        })

        println("  transact + sequential (5 queries)...")
        results.add(runTimedBenchmark("mutable + sequential", CONCURRENCY, WARMUP_SECONDS, MEASURE_SECONDS) { i ->
            pipePool.transact { conn ->
                for (j in 0 until 5) {
                    conn.execute(selectTpl.on(((i + j) % ROW_COUNT) + 1))
                }
            }
        })

        println("  Raw JDBC (5 queries)...")
        results.add(runTimedBenchmark("Raw JDBC (5 queries)", CONCURRENCY, WARMUP_SECONDS, MEASURE_SECONDS) { i ->
            hikariDs.connection.use { conn ->
                for (j in 0 until 5) {
                    conn.prepareStatement(rawSelectSql).use { ps ->
                        ps.setInt(1, ((i + j) % ROW_COUNT) + 1)
                        ps.executeQuery().use { rs -> rs.next() }
                    }
                }
            }
        })

        printResults("Readonly combine() vs sequential (5 queries, 10ms RTT)", results, "ops/sec")

        report.section("Readonly combine() vs sequential (5 queries, 10ms RTT)",
            "5 queries per task, $TX_POOL_SIZE connections, 10ms RTT.",
            results, "ops/sec",
            """
            // readonly + combine: 5 queries fan out, ~1 RTT total
            pool.transactRead(conn -> conn.execute(
                q1.combine(q2).combine(q3).combine(q4).combine(q5)
            ));

            // sequential: 5 queries, 5 RTTs = ~50ms per task
            pool.transactRead(conn -> {
                conn.queryFirst(sql1, codec);
                conn.queryFirst(sql2, codec);
                // ...
            });
            """.trimIndent()
        )

        pipePool.close()
        hikariDs.close()
    }

    report.writeTo(REPORT_DIR, "readonly-latency.md")
}
