package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.PgTypes
import dev.typr.foundations.Template
import dev.typr.foundations.pg.PgPipelinePool
import io.vertx.sqlclient.Tuple as VxTuple
import java.math.BigDecimal
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future

// ==================== Configuration ====================

private object UpdateCfg {
    const val LOCAL_POOL_SIZE = 50
    const val LOCAL_TASK_COUNT = 10_000
    const val LATENCY_POOL_SIZE = 10
    const val LATENCY_TASK_COUNT = 1_000
    const val SCARCE_POOL_SIZE = 5
    const val SCARCE_TASK_COUNT = 5_000
    const val WARMUP_OPS = 1_000
    const val MEASURED_ITERATIONS = 5
    const val LATENCY_MS = 5L
}

private const val VX_UPDATE_SQL = "UPDATE bench_read SET value = \$1, name = \$2 WHERE id = \$3"

// ==================== Templates ====================

private fun updateTemplate(): Template.Update3<BigDecimal, String, Int> =
    Fragment.of("UPDATE bench_read SET value = ")
        .param(PgTypes.numeric).append(", name = ")
        .param(PgTypes.text).append(" WHERE id = ")
        .param(PgTypes.int4)
        .update()

// ==================== Main ====================

fun main() = runUpdateBenchmark()

fun runUpdateBenchmark() {
    println("=== UPDATE Throughput Benchmark ===")
    println("PostgreSQL at $PG_HOST:$PG_PORT/$PG_DB")
    println()

    setupBenchTables(PG_PORT)

    val localResults = runLocalUpdateBenchmark()
    val latencyResults = runLatencyUpdateBenchmark()
    val scarceResults = runScarceUpdateBenchmark()

    val report = MarkdownReport("UPDATE Benchmark")
    report.section("Local UPDATE", "${UpdateCfg.LOCAL_TASK_COUNT} tasks, ${UpdateCfg.LOCAL_POOL_SIZE} connections.", localResults, "ops/sec",
        """
        pool.execute(updateTemplate.on(newValue, newName, id))
        """.trimIndent()
    )
    report.section("UPDATE @ ${UpdateCfg.LATENCY_MS * 2}ms RTT", "${UpdateCfg.LATENCY_TASK_COUNT} tasks, ${UpdateCfg.LATENCY_POOL_SIZE} connections.", latencyResults, "ops/sec",
        """
        pool.execute(updateTemplate.on(newValue, newName, id))
        """.trimIndent()
    )
    report.section("Scarce UPDATE", "${UpdateCfg.SCARCE_TASK_COUNT} tasks, ${UpdateCfg.SCARCE_POOL_SIZE} connections.", scarceResults, "ops/sec",
        """
        pool.execute(updateTemplate.on(newValue, newName, id))
        """.trimIndent()
    )
    report.writeTo(REPORT_DIR, "update.md")
}

// ==================== 1. Local ====================

private fun runLocalUpdateBenchmark(): List<BenchResult> {
    println("=".repeat(70))
    println("=== 1. Local UPDATE (${UpdateCfg.LOCAL_TASK_COUNT} tasks, ${UpdateCfg.LOCAL_POOL_SIZE} conns) ===")
    println("=".repeat(70))

    val results = mutableListOf<BenchResult>()
    val updateTpl = updateTemplate()

    val rawDs = createHikariPool(PG_PORT, UpdateCfg.LOCAL_POOL_SIZE)
    warmupRawUpdate(rawDs)
    println("  Raw JDBC...")
    results.add(runBenchmark("Raw JDBC", UpdateCfg.LOCAL_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        rawDs.connection.use { conn ->
            conn.prepareStatement(RAW_UPDATE_SQL).use { ps ->
                ps.setBigDecimal(1, BigDecimal("${i % 1000}.50"))
                ps.setString(2, "Updated #$i")
                ps.setInt(3, (i % ROW_COUNT) + 1)
                ps.executeUpdate()
            }
        }
    })
    rawDs.close()

    val (hikariDs, tx) = createTransactor(PG_PORT, UpdateCfg.LOCAL_POOL_SIZE)
    warmupFoundationsUpdate(tx, updateTpl)
    println("  Foundations+Hikari...")
    results.add(runBenchmark("Foundations+Hikari", UpdateCfg.LOCAL_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        tx.execute(updateTpl.on(BigDecimal("${i % 1000}.50"), "Updated #$i", (i % ROW_COUNT) + 1))
    })
    hikariDs.close()

    val pipePool = createPipelinePool(PG_PORT, UpdateCfg.LOCAL_POOL_SIZE)
    warmupPipeUpdate(pipePool, updateTpl)
    println("  PgPipe (mutable)...")
    results.add(runBenchmark("Foundations+PgPipe (mutable)", UpdateCfg.LOCAL_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        pipePool.transact { conn -> conn.execute(updateTpl.on(BigDecimal("${i % 1000}.50"), "Updated #$i", (i % ROW_COUNT) + 1)) }
    })
    pipePool.close()

    val rawVx = createRawVertxPool(PG_PORT, UpdateCfg.LOCAL_POOL_SIZE)
    warmupVertxUpdate(rawVx.pool, UpdateCfg.LOCAL_POOL_SIZE)
    println("  Vert.x (async)...")
    results.add(runAsyncBenchmark("Vert.x (async)", UpdateCfg.LOCAL_TASK_COUNT, UpdateCfg.MEASURED_ITERATIONS) { i ->
        rawVx.pool.preparedQuery(VX_UPDATE_SQL)
            .execute(VxTuple.of(BigDecimal("${i % 1000}.50"), "Updated #$i", (i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    })
    rawVx.close()

    val sf = createHibernateSessionFactory(PG_PORT, UpdateCfg.LOCAL_POOL_SIZE)
    warmupHibernateUpdate(sf)
    println("  Hibernate...")
    results.add(runBenchmark("Hibernate", UpdateCfg.LOCAL_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        sf.openSession().use { session ->
            session.beginTransaction()
            session.createMutationQuery("UPDATE BenchRowEntity SET value = :v, name = :n WHERE id = :id")
                .setParameter("v", BigDecimal("${i % 1000}.50"))
                .setParameter("n", "Updated #$i")
                .setParameter("id", (i % ROW_COUNT) + 1)
                .executeUpdate()
            session.transaction.commit()
        }
    })
    sf.close()

    printResults("Local UPDATE", results, "ops/sec")
    return results
}

// ==================== 2. Under 10ms RTT ====================

private fun runLatencyUpdateBenchmark(): List<BenchResult> {
    println()
    println("=".repeat(70))
    println("=== 2. UPDATE @ ${UpdateCfg.LATENCY_MS * 2}ms RTT (${UpdateCfg.LATENCY_TASK_COUNT} tasks, ${UpdateCfg.LATENCY_POOL_SIZE} conns) ===")
    println("=".repeat(70))

    val proxyPort = proxiedPort(UpdateCfg.LATENCY_MS, 18200)
    val results = mutableListOf<BenchResult>()
    val updateTpl = updateTemplate()

    val rawDs = createHikariPool(proxyPort.port, UpdateCfg.LATENCY_POOL_SIZE)
    warmupRawUpdate(rawDs)
    println("  Raw JDBC...")
    results.add(runBenchmark("Raw JDBC", UpdateCfg.LATENCY_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        rawDs.connection.use { conn ->
            conn.prepareStatement(RAW_UPDATE_SQL).use { ps ->
                ps.setBigDecimal(1, BigDecimal("${i % 1000}.50"))
                ps.setString(2, "Updated #$i")
                ps.setInt(3, (i % ROW_COUNT) + 1)
                ps.executeUpdate()
            }
        }
    })
    rawDs.close()

    val (hikariDs, fndTx) = createTransactor(proxyPort.port, UpdateCfg.LATENCY_POOL_SIZE)
    warmupFoundationsUpdate(fndTx, updateTpl)
    println("  Foundations+Hikari...")
    results.add(runBenchmark("Foundations+Hikari", UpdateCfg.LATENCY_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        fndTx.execute(updateTpl.on(BigDecimal("${i % 1000}.50"), "Updated #$i", (i % ROW_COUNT) + 1))
    })
    hikariDs.close()

    val pipePool = createPipelinePool(proxyPort.port, UpdateCfg.LATENCY_POOL_SIZE)
    warmupPipeUpdate(pipePool, updateTpl)
    println("  PgPipe (mutable)...")
    results.add(runBenchmark("Foundations+PgPipe (mutable)", UpdateCfg.LATENCY_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        pipePool.transact { conn -> conn.execute(updateTpl.on(BigDecimal("${i % 1000}.50"), "Updated #$i", (i % ROW_COUNT) + 1)) }
    })
    pipePool.close()

    val rawVx = createRawVertxPool(proxyPort.port, UpdateCfg.LATENCY_POOL_SIZE)
    warmupVertxUpdate(rawVx.pool, UpdateCfg.LATENCY_POOL_SIZE)
    println("  Vert.x (async)...")
    results.add(runAsyncBenchmark("Vert.x (async)", UpdateCfg.LATENCY_TASK_COUNT, UpdateCfg.MEASURED_ITERATIONS) { i ->
        rawVx.pool.preparedQuery(VX_UPDATE_SQL)
            .execute(VxTuple.of(BigDecimal("${i % 1000}.50"), "Updated #$i", (i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    })
    rawVx.close()

    val sf = createHibernateSessionFactory(proxyPort.port, UpdateCfg.LATENCY_POOL_SIZE)
    warmupHibernateUpdate(sf)
    println("  Hibernate...")
    results.add(runBenchmark("Hibernate", UpdateCfg.LATENCY_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        sf.openSession().use { session ->
            session.beginTransaction()
            session.createMutationQuery("UPDATE BenchRowEntity SET value = :v, name = :n WHERE id = :id")
                .setParameter("v", BigDecimal("${i % 1000}.50"))
                .setParameter("n", "Updated #$i")
                .setParameter("id", (i % ROW_COUNT) + 1)
                .executeUpdate()
            session.transaction.commit()
        }
    })
    sf.close()

    proxyPort.close()
    printResults("UPDATE @ ${UpdateCfg.LATENCY_MS * 2}ms RTT", results, "ops/sec")
    return results
}

// ==================== 3. Scarce Connections ====================

private fun runScarceUpdateBenchmark(): List<BenchResult> {
    println()
    println("=".repeat(70))
    println("=== 3. Scarce UPDATE (${UpdateCfg.SCARCE_TASK_COUNT} tasks, ${UpdateCfg.SCARCE_POOL_SIZE} conns, local) ===")
    println("=".repeat(70))

    val results = mutableListOf<BenchResult>()
    val updateTpl = updateTemplate()

    val rawDs = createHikariPool(PG_PORT, UpdateCfg.SCARCE_POOL_SIZE)
    warmupRawUpdate(rawDs)
    println("  Raw JDBC...")
    results.add(runBenchmark("Raw JDBC", UpdateCfg.SCARCE_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        rawDs.connection.use { conn ->
            conn.prepareStatement(RAW_UPDATE_SQL).use { ps ->
                ps.setBigDecimal(1, BigDecimal("${i % 1000}.50"))
                ps.setString(2, "Updated #$i")
                ps.setInt(3, (i % ROW_COUNT) + 1)
                ps.executeUpdate()
            }
        }
    })
    rawDs.close()

    val (hikariDs, fndTx) = createTransactor(PG_PORT, UpdateCfg.SCARCE_POOL_SIZE)
    warmupFoundationsUpdate(fndTx, updateTpl)
    println("  Foundations+Hikari...")
    results.add(runBenchmark("Foundations+Hikari", UpdateCfg.SCARCE_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        fndTx.execute(updateTpl.on(BigDecimal("${i % 1000}.50"), "Updated #$i", (i % ROW_COUNT) + 1))
    })
    hikariDs.close()

    val pipePool = createPipelinePool(PG_PORT, UpdateCfg.SCARCE_POOL_SIZE)
    warmupPipeUpdate(pipePool, updateTpl)
    println("  PgPipe (mutable)...")
    results.add(runBenchmark("Foundations+PgPipe (mutable)", UpdateCfg.SCARCE_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        pipePool.transact { conn -> conn.execute(updateTpl.on(BigDecimal("${i % 1000}.50"), "Updated #$i", (i % ROW_COUNT) + 1)) }
    })
    pipePool.close()

    val rawVx = createRawVertxPool(PG_PORT, UpdateCfg.SCARCE_POOL_SIZE)
    warmupVertxUpdate(rawVx.pool, UpdateCfg.SCARCE_POOL_SIZE)
    println("  Vert.x (async)...")
    results.add(runAsyncBenchmark("Vert.x (async)", UpdateCfg.SCARCE_TASK_COUNT, UpdateCfg.MEASURED_ITERATIONS) { i ->
        rawVx.pool.preparedQuery(VX_UPDATE_SQL)
            .execute(VxTuple.of(BigDecimal("${i % 1000}.50"), "Updated #$i", (i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    })
    rawVx.close()

    val sf = createHibernateSessionFactory(PG_PORT, UpdateCfg.SCARCE_POOL_SIZE)
    warmupHibernateUpdate(sf)
    println("  Hibernate...")
    results.add(runBenchmark("Hibernate", UpdateCfg.SCARCE_TASK_COUNT, UpdateCfg.WARMUP_OPS, UpdateCfg.MEASURED_ITERATIONS) { i ->
        sf.openSession().use { session ->
            session.beginTransaction()
            session.createMutationQuery("UPDATE BenchRowEntity SET value = :v, name = :n WHERE id = :id")
                .setParameter("v", BigDecimal("${i % 1000}.50"))
                .setParameter("n", "Updated #$i")
                .setParameter("id", (i % ROW_COUNT) + 1)
                .executeUpdate()
            session.transaction.commit()
        }
    })
    sf.close()

    printResults("Scarce UPDATE (${UpdateCfg.SCARCE_POOL_SIZE} conns)", results, "ops/sec")
    return results
}

// ==================== Warmup Helpers ====================

private fun warmupRawUpdate(ds: javax.sql.DataSource) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..UpdateCfg.WARMUP_OPS).map { i -> exec.submit<Any?> {
            ds.connection.use { conn ->
                conn.prepareStatement(RAW_UPDATE_SQL).use { ps ->
                    ps.setBigDecimal(1, BigDecimal("0.01"))
                    ps.setString(2, "warmup")
                    ps.setInt(3, (i % ROW_COUNT) + 1)
                    ps.executeUpdate()
                }
            }
        }}.forEach(Future<*>::get)
    }
}

private fun warmupFoundationsUpdate(tx: dev.typr.foundations.Transactor, tpl: Template.Update3<BigDecimal, String, Int>) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..UpdateCfg.WARMUP_OPS).map { i -> exec.submit<Any?> {
            tx.execute(tpl.on(BigDecimal("0.01"), "warmup", (i % ROW_COUNT) + 1))
        }}.forEach(Future<*>::get)
    }
}

private fun warmupPipeUpdate(pool: PgPipelinePool, tpl: Template.Update3<BigDecimal, String, Int>) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..UpdateCfg.WARMUP_OPS).map { i -> exec.submit<Any?> {
            pool.execute(tpl.on(BigDecimal("0.01"), "warmup", (i % ROW_COUNT) + 1))
        }}.forEach(Future<*>::get)
    }
}

private fun warmupVertxUpdate(pool: io.vertx.pgclient.PgPool, poolSize: Int) {
    for (i in 1..poolSize) {
        pool.query("SELECT 1").execute().toCompletionStage().toCompletableFuture().join()
    }
    val warmupFutures = (1..UpdateCfg.WARMUP_OPS).map { i ->
        pool.preparedQuery(VX_UPDATE_SQL)
            .execute(VxTuple.of(BigDecimal("0.01"), "warmup", (i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    }
    CompletableFuture.allOf(*warmupFutures.toTypedArray()).join()
}

private fun warmupHibernateUpdate(sf: org.hibernate.SessionFactory) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..UpdateCfg.WARMUP_OPS).map { i -> exec.submit<Any?> {
            sf.openSession().use { session ->
                session.beginTransaction()
                session.createMutationQuery("UPDATE BenchRowEntity SET value = :v, name = :n WHERE id = :id")
                    .setParameter("v", BigDecimal("0.01"))
                    .setParameter("n", "warmup")
                    .setParameter("id", (i % ROW_COUNT) + 1)
                    .executeUpdate()
                session.transaction.commit()
            }
        }}.forEach(Future<*>::get)
    }
}
