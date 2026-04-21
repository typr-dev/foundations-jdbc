package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.Operation
import dev.typr.foundations.pg.PgPipelinePool
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.Future

// ==================== Configuration ====================

const val PA_POOL_SIZE = 10
const val PA_TASK_COUNT = 1_000
const val PA_WARMUP_OPS = 300
const val PA_MEASURED_ITERATIONS = 3
const val PA_LATENCY_MS = 5L
val PA_RTT = PA_LATENCY_MS * 2

// ==================== Main ====================

fun main() = runPipeliningAdvantageBenchmark()

fun runPipeliningAdvantageBenchmark() {
    println("=== Pipelining Advantage Benchmark ===")
    println("PostgreSQL at $PG_HOST:$PG_PORT/$PG_DB")
    println("Pool size: $PA_POOL_SIZE connections, latency: ${PA_RTT}ms RTT")
    println("Concurrency: $PA_TASK_COUNT tasks, $PA_MEASURED_ITERATIONS iterations")
    println()

    val selectByIdTemplate = selectByIdTemplate()
    val insertTemplate = insertTemplate()
    val rawSelectSql = RAW_SELECT_SQL
    val rawInsertSql = RAW_INSERT_SQL

    val proxyPort = 17437
    val proxy = LatencyProxy(proxyPort, PG_HOST, PG_PORT, PA_LATENCY_MS)
    proxy.start()

    val report = MarkdownReport("Pipelining Advantage Benchmark")

    val throughputResults = runThroughputUnderLatency(selectByIdTemplate, rawSelectSql, proxyPort)
    report.section("Throughput @ ${PA_RTT}ms RTT", "$PA_TASK_COUNT point reads, $PA_POOL_SIZE connections, ${PA_RTT}ms simulated RTT.", throughputResults, "ops/sec",
        """
        pool.execute(selectById.on(42))
        """.trimIndent()
    )

    val fanOutResults = runCombineFanOut(selectByIdTemplate, rawSelectSql, proxyPort)
    for ((label, results) in fanOutResults) {
        report.section(label, "$PA_TASK_COUNT tasks, $PA_POOL_SIZE connections, ${PA_RTT}ms RTT.", results, "ops/sec",
            """
            pool.transact(tx -> tx.execute(
                selectById.on(1)
                    .combine(selectById.on(2))
                    .combine(selectById.on(3))
            ));
            """.trimIndent()
        )
    }

    val multiStmtResults = runMultiStatementScaling(selectByIdTemplate, insertTemplate, rawSelectSql, rawInsertSql, proxyPort)
    for ((label, results) in multiStmtResults) {
        report.section(label, "$PA_TASK_COUNT tasks, $PA_POOL_SIZE connections, ${PA_RTT}ms RTT.", results, "ops/sec",
            """
            pool.transact(tx -> {
                tx.execute(selectById.on(1));
                tx.execute(insertRow.on("Widget", price, true, now, "Cat"));
                tx.execute(selectById.on(2));
            });
            """.trimIndent()
        )
    }

    val scarcityResults = runExtremeScarcity(selectByIdTemplate, rawSelectSql, proxyPort)
    report.section("Extreme Scarcity (2 conns)", "$PA_TASK_COUNT tasks, 2 connections, ${PA_RTT}ms RTT.", scarcityResults, "ops/sec",
        """
        pool.execute(selectById.on(42))
        """.trimIndent()
    )

    proxy.close()

    report.writeTo(REPORT_DIR, "pipelining-advantage.md")
}

// ==================== 1. Throughput Under Latency ====================

private fun runThroughputUnderLatency(
    selectByIdTemplate: dev.typr.foundations.Template.Query1<Int, java.util.Optional<BenchRow>>,
    rawSelectSql: String,
    proxyPort: Int,
): List<BenchResult> {
    println("=" .repeat(70))
    println("=== 1. Throughput Under ${PA_RTT}ms RTT ($PA_TASK_COUNT point reads, $PA_POOL_SIZE conns) ===")
    println("=" .repeat(70))

    val results = mutableListOf<BenchResult>()

    val hikariDs = createHikariPool(proxyPort, PA_POOL_SIZE)
    warmupJdbc(hikariDs, rawSelectSql)

    println("  Raw JDBC...")
    results.add(runPaBenchmark("Raw JDBC", PA_TASK_COUNT) { i ->
        hikariDs.connection.use { conn ->
            conn.prepareStatement(rawSelectSql).use { ps ->
                ps.setInt(1, (i % ROW_COUNT) + 1)
                ps.executeQuery().use { rs ->
                    if (rs.next()) readBenchRow(rs) else null
                }
            }
        }
    })
    hikariDs.close()

    val (fndDs, fndTx) = createTransactor(proxyPort, PA_POOL_SIZE)
    warmupFoundations(fndTx, selectByIdTemplate)

    println("  Foundations+Hikari...")
    results.add(runPaBenchmark("Foundations+Hikari", PA_TASK_COUNT) { i ->
        fndTx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
    })
    fndDs.close()

    val pipePool = createPipelinePool(proxyPort, PA_POOL_SIZE)
    warmupPipe(pipePool, selectByIdTemplate)

    println("  PgPipeline (readonly)...")
    results.add(runPaBenchmark("Foundations+PgPipe (readonly)", PA_TASK_COUNT) { i ->
        pipePool.transactRead { conn -> conn.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) }
    })

    println("  PgPipeline (mutable)...")
    results.add(runPaBenchmark("Foundations+PgPipe (mutable)", PA_TASK_COUNT) { i ->
        pipePool.transact { conn -> conn.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) }
    })
    pipePool.close()

    val sf = createHibernateSessionFactory(proxyPort, PA_POOL_SIZE)
    warmupHibernate(sf)

    println("  Hibernate...")
    results.add(runPaBenchmark("Hibernate", PA_TASK_COUNT) { i ->
        sf.openSession().use { session ->
            session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
        }
    })
    sf.close()

    printResults("Throughput @ ${PA_RTT}ms RTT", results, "ops/sec")
    return results
}

// ==================== 2. combine() Fan-Out in Transactions ====================

private fun runCombineFanOut(
    selectByIdTemplate: dev.typr.foundations.Template.Query1<Int, java.util.Optional<BenchRow>>,
    rawSelectSql: String,
    proxyPort: Int,
): List<Pair<String, List<BenchResult>>> {
    val allResults = mutableListOf<Pair<String, List<BenchResult>>>()

    for (fanOutSize in listOf(5, 10)) {
        println()
        println("=" .repeat(70))
        println("=== 2. combine() Fan-Out: $fanOutSize queries/tx (${PA_RTT}ms RTT, $PA_TASK_COUNT tasks) ===")
        println("=" .repeat(70))

        val results = mutableListOf<BenchResult>()

        val pipePool = createPipelinePoolWithTx(proxyPort, PA_POOL_SIZE, PA_POOL_SIZE - 1)
        warmupPipe(pipePool, selectByIdTemplate)

        println("  PgPipeline readonly combine()...")
        results.add(runPaBenchmark("Foundations+PgPipe (readonly combine)", PA_TASK_COUNT) { i ->
            pipePool.transactRead { conn ->
                val ops = (0 until fanOutSize).map { j ->
                    selectByIdTemplate.on(((i + j) % ROW_COUNT) + 1)
                }
                @Suppress("UNCHECKED_CAST")
                val combined = ops.drop(1).fold(ops.first() as Operation<*>) { acc, op ->
                    acc.combine(op)
                }
                conn.execute(combined)
            }
        })

        println("  PgPipeline mutable combine()...")
        results.add(runPaBenchmark("Foundations+PgPipe (mutable combine)", PA_TASK_COUNT) { i ->
            pipePool.transact { tx ->
                val ops = (0 until fanOutSize).map { j ->
                    selectByIdTemplate.on(((i + j) % ROW_COUNT) + 1)
                }
                @Suppress("UNCHECKED_CAST")
                val combined = ops.drop(1).fold(ops.first() as Operation<*>) { acc, op ->
                    acc.combine(op)
                }
                tx.execute(combined)
            }
        })

        println("  PgPipeline sequential...")
        results.add(runPaBenchmark("Foundations+PgPipe (seq)", PA_TASK_COUNT) { i ->
            pipePool.transact { tx ->
                for (j in 0 until fanOutSize) {
                    tx.execute(selectByIdTemplate.on(((i + j) % ROW_COUNT) + 1))
                }
            }
        })
        pipePool.close()

        val hikariDs = createHikariPool(proxyPort, PA_POOL_SIZE)
        warmupJdbc(hikariDs, rawSelectSql)

        println("  Raw JDBC sequential...")
        results.add(runPaBenchmark("Raw JDBC (seq)", PA_TASK_COUNT) { i ->
            hikariDs.connection.use { conn ->
                conn.autoCommit = false
                try {
                    for (j in 0 until fanOutSize) {
                        conn.prepareStatement(rawSelectSql).use { ps ->
                            ps.setInt(1, ((i + j) % ROW_COUNT) + 1)
                            ps.executeQuery().use { rs ->
                                if (rs.next()) readBenchRow(rs) else null
                            }
                        }
                    }
                    conn.commit()
                } catch (e: Exception) {
                    conn.rollback()
                    throw e
                }
            }
        })
        hikariDs.close()

        val sf = createHibernateSessionFactory(proxyPort, PA_POOL_SIZE)
        warmupHibernate(sf)

        println("  Hibernate sequential tx...")
        results.add(runPaBenchmark("Hibernate (seq)", PA_TASK_COUNT) { i ->
            sf.openSession().use { session ->
                session.beginTransaction()
                for (j in 0 until fanOutSize) {
                    session.find(BenchRowEntity::class.java, ((i + j) % ROW_COUNT) + 1)
                }
                session.transaction.commit()
            }
        })
        sf.close()

        printResults("combine() $fanOutSize queries (${PA_RTT}ms RTT)", results, "ops/sec")
        allResults.add(Pair("combine() Fan-Out: $fanOutSize queries/tx", results))
    }

    return allResults
}

// ==================== 3. Multi-Statement Transaction Scaling ====================

private fun runMultiStatementScaling(
    selectByIdTemplate: dev.typr.foundations.Template.Query1<Int, java.util.Optional<BenchRow>>,
    insertTemplate: dev.typr.foundations.Template.Update5<String, BigDecimal, Boolean, LocalDateTime, String>,
    rawSelectSql: String,
    rawInsertSql: String,
    proxyPort: Int,
): List<Pair<String, List<BenchResult>>> {
    val allResults = mutableListOf<Pair<String, List<BenchResult>>>()

    for (opsPerTx in listOf(3, 5, 10)) {
        println()
        println("=" .repeat(70))
        println("=== 3. Multi-Statement Tx: $opsPerTx ops/tx (${PA_RTT}ms RTT, $PA_TASK_COUNT tasks) ===")
        println("=" .repeat(70))

        val results = mutableListOf<BenchResult>()

        val pipePool = createPipelinePoolWithTx(proxyPort, PA_POOL_SIZE, PA_POOL_SIZE - 1)
        warmupPipe(pipePool, selectByIdTemplate)

        pipePool.update(Fragment.of("TRUNCATE bench_write"))
        println("  PgPipeline transact...")
        results.add(runPaBenchmark("Foundations+PgPipe (tx)", PA_TASK_COUNT) { i ->
            pipePool.transact { tx ->
                for (j in 0 until opsPerTx) {
                    if (j % 2 == 0) {
                        tx.execute(selectByIdTemplate.on(((i + j) % ROW_COUNT) + 1))
                    } else {
                        tx.execute(insertTemplate.on("tx-$i-$j", BigDecimal("${i % 1000}.99"), j % 3 != 0, LocalDateTime.now(), "Bench"))
                    }
                }
            }
        })
        pipePool.close()

        val hikariDs = createHikariPool(proxyPort, PA_POOL_SIZE)
        warmupJdbc(hikariDs, rawSelectSql)

        hikariDs.connection.use { c -> c.createStatement().use { it.execute("TRUNCATE bench_write") } }
        println("  Raw JDBC tx...")
        results.add(runPaBenchmark("Raw JDBC (tx)", PA_TASK_COUNT) { i ->
            hikariDs.connection.use { conn ->
                conn.autoCommit = false
                try {
                    for (j in 0 until opsPerTx) {
                        if (j % 2 == 0) {
                            conn.prepareStatement(rawSelectSql).use { ps ->
                                ps.setInt(1, ((i + j) % ROW_COUNT) + 1)
                                ps.executeQuery().use { rs ->
                                    if (rs.next()) readBenchRow(rs) else null
                                }
                            }
                        } else {
                            conn.prepareStatement(rawInsertSql).use { ps ->
                                ps.setString(1, "tx-$i-$j")
                                ps.setBigDecimal(2, BigDecimal("${i % 1000}.99"))
                                ps.setBoolean(3, j % 3 != 0)
                                ps.setObject(4, LocalDateTime.now())
                                ps.setString(5, "Bench")
                                ps.executeUpdate()
                            }
                        }
                    }
                    conn.commit()
                } catch (e: Exception) {
                    conn.rollback()
                    throw e
                }
            }
        })
        hikariDs.close()

        val sf = createHibernateSessionFactory(proxyPort, PA_POOL_SIZE)
        warmupHibernate(sf)

        sf.openSession().use { s -> s.beginTransaction(); s.createNativeMutationQuery("TRUNCATE bench_write").executeUpdate(); s.transaction.commit() }
        println("  Hibernate tx...")
        results.add(runPaBenchmark("Hibernate (tx)", PA_TASK_COUNT) { i ->
            sf.openSession().use { session ->
                session.beginTransaction()
                for (j in 0 until opsPerTx) {
                    if (j % 2 == 0) {
                        session.find(BenchRowEntity::class.java, ((i + j) % ROW_COUNT) + 1)
                    } else {
                        val entity = WriteRowEntity()
                        entity.name = "tx-$i-$j"
                        entity.value = BigDecimal("${i % 1000}.99")
                        entity.active = j % 3 != 0
                        entity.createdAt = LocalDateTime.now()
                        entity.category = "Bench"
                        session.persist(entity)
                    }
                }
                session.transaction.commit()
            }
        })
        sf.close()

        printResults("$opsPerTx-op tx (${PA_RTT}ms RTT)", results, "ops/sec")
        allResults.add(Pair("$opsPerTx-op Transaction", results))
    }

    return allResults
}

// ==================== 4. Extreme Connection Scarcity ====================

private fun runExtremeScarcity(
    selectByIdTemplate: dev.typr.foundations.Template.Query1<Int, java.util.Optional<BenchRow>>,
    rawSelectSql: String,
    proxyPort: Int,
): List<BenchResult> {
    val scarceConns = 2

    println()
    println("=" .repeat(70))
    println("=== 4. Extreme Scarcity: $scarceConns conns, $PA_TASK_COUNT tasks (${PA_RTT}ms RTT) ===")
    println("=" .repeat(70))

    val results = mutableListOf<BenchResult>()

    val hikariDs = createHikariPool(proxyPort, scarceConns)
    warmupJdbc(hikariDs, rawSelectSql)

    println("  Raw JDBC ($scarceConns conns)...")
    results.add(runPaBenchmark("Raw JDBC", PA_TASK_COUNT) { i ->
        hikariDs.connection.use { conn ->
            conn.prepareStatement(rawSelectSql).use { ps ->
                ps.setInt(1, (i % ROW_COUNT) + 1)
                ps.executeQuery().use { rs ->
                    if (rs.next()) readBenchRow(rs) else null
                }
            }
        }
    })
    hikariDs.close()

    val pipePool = createPipelinePool(proxyPort, scarceConns)

    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..100).map { i -> exec.submit<Any?> {
            pipePool.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
        }}.forEach(Future<*>::get)
    }

    println("  PgPipeline (readonly, $scarceConns conns)...")
    results.add(runPaBenchmark("Foundations+PgPipe (readonly)", PA_TASK_COUNT) { i ->
        pipePool.transactRead { conn -> conn.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) }
    })

    println("  PgPipeline (mutable, $scarceConns conns)...")
    results.add(runPaBenchmark("Foundations+PgPipe (mutable)", PA_TASK_COUNT) { i ->
        pipePool.transact { conn -> conn.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) }
    })
    pipePool.close()

    val sf = createHibernateSessionFactory(proxyPort, scarceConns)
    warmupHibernate(sf)

    println("  Hibernate ($scarceConns conns)...")
    results.add(runPaBenchmark("Hibernate", PA_TASK_COUNT) { i ->
        sf.openSession().use { session ->
            session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
        }
    })
    sf.close()

    printResults("Extreme Scarcity ($scarceConns conns, ${PA_RTT}ms RTT)", results, "ops/sec")
    return results
}

// ==================== Helpers ====================

private fun warmupPipe(pool: PgPipelinePool, tpl: dev.typr.foundations.Template.Query1<Int, java.util.Optional<BenchRow>>) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..PA_WARMUP_OPS).map { i -> exec.submit<Any?> {
            pool.execute(tpl.on((i % ROW_COUNT) + 1))
        }}.forEach(Future<*>::get)
    }
}

private fun warmupHibernate(sf: org.hibernate.SessionFactory) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..PA_WARMUP_OPS).map { i -> exec.submit<Any?> {
            sf.openSession().use { session ->
                session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
            }
        }}.forEach(Future<*>::get)
    }
}

private fun warmupJdbc(ds: javax.sql.DataSource, rawSelectSql: String) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..PA_WARMUP_OPS).map { i -> exec.submit<Any?> {
            ds.connection.use { conn ->
                conn.prepareStatement(rawSelectSql).use { ps ->
                    ps.setInt(1, (i % ROW_COUNT) + 1)
                    ps.executeQuery().use { rs -> rs.next() }
                }
            }
        }}.forEach(Future<*>::get)
    }
}

private fun warmupFoundations(tx: dev.typr.foundations.Transactor, tpl: dev.typr.foundations.Template.Query1<Int, java.util.Optional<BenchRow>>) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..PA_WARMUP_OPS).map { i -> exec.submit<Any?> {
            tx.execute(tpl.on((i % ROW_COUNT) + 1))
        }}.forEach(Future<*>::get)
    }
}

// ==================== Benchmark Harness ====================

private fun runPaBenchmark(name: String, taskCount: Int, op: (Int) -> Any?): BenchResult {
    return runBenchmark(name, taskCount, PA_WARMUP_OPS, PA_MEASURED_ITERATIONS, op)
}
