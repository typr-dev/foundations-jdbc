package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.PgTypes
import dev.typr.foundations.pg.PgPipelinePool
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ThreadLocalRandom

// ==================== Configuration ====================

private object CacheCfg {
    const val POOL_SIZE = 10
    const val TASK_COUNT = 5_000
    const val WARMUP_OPS = 500
    const val MEASURED_ITERATIONS = 5
    val WORKING_SET_SIZES = listOf(8, 64, 256, 1024)
}

// ==================== Query Generation ====================

private fun generateDistinctQueries(count: Int): List<String> =
    (0 until count).map { k ->
        "SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ? AND category = '${CATEGORIES[k % CATEGORIES.size]}'"
    }

private fun generatePipeQueries(count: Int): List<Fragment> =
    (0 until count).map { k ->
        Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
            .append(Fragment.value(1, PgTypes.int4))
            .append(" AND category = '${CATEGORIES[k % CATEGORIES.size]}'")
    }

private fun generatePipeTemplates(count: Int): List<dev.typr.foundations.Template.Query1<Int, java.util.Optional<BenchRow>>> =
    (0 until count).map { k ->
        Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
            .param(PgTypes.int4)
            .append(" AND category = '${CATEGORIES[k % CATEGORIES.size]}'")
            .query(benchReadCodec().first())
    }

// ==================== Main ====================

fun main() = runCacheChurnBenchmark()

fun runCacheChurnBenchmark() {
    println("=== Prepared Statement Cache Churn Benchmark ===")
    println("PostgreSQL at $PG_HOST:$PG_PORT/$PG_DB")
    println("Pool size: ${CacheCfg.POOL_SIZE} connections")
    println("Tasks per working set: ${CacheCfg.TASK_COUNT}")
    println()

    setupBenchTables(PG_PORT)

    val report = MarkdownReport("Cache Churn Benchmark")

    for (workingSetSize in CacheCfg.WORKING_SET_SIZES) {
        val results = runCacheChurnScenario(workingSetSize)
        report.section(
            "$workingSetSize Distinct Queries",
            "${CacheCfg.TASK_COUNT} tasks, ${CacheCfg.POOL_SIZE} connections.",
            results,
            "ops/sec",
            """
            pool.execute(queries[random.nextInt(workingSetSize)].on(id))
            """.trimIndent()
        )
    }

    report.writeTo(REPORT_DIR, "cache-churn.md")
}

// ==================== Scenario ====================

private fun runCacheChurnScenario(workingSetSize: Int): List<BenchResult> {
    println("=".repeat(70))
    println("=== Working Set: $workingSetSize distinct queries (${CacheCfg.TASK_COUNT} tasks, ${CacheCfg.POOL_SIZE} conns) ===")
    println("=".repeat(70))

    val results = mutableListOf<BenchResult>()

    val pipeTemplates = generatePipeTemplates(workingSetSize)
    val pipePool = createPipelinePool(PG_PORT, CacheCfg.POOL_SIZE)

    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..CacheCfg.WARMUP_OPS).map { i -> exec.submit<Any?> {
            val tpl = pipeTemplates[i % workingSetSize]
            pipePool.execute(tpl.on((i % ROW_COUNT) + 1))
        }}.forEach(Future<*>::get)
    }

    println("  PgPipe (readonly)...")
    results.add(runBenchmark("Foundations+PgPipe (readonly)", CacheCfg.TASK_COUNT, CacheCfg.WARMUP_OPS, CacheCfg.MEASURED_ITERATIONS) { i ->
        val idx = ThreadLocalRandom.current().nextInt(workingSetSize)
        val tpl = pipeTemplates[idx]
        pipePool.transactRead { conn -> conn.execute(tpl.on((i % ROW_COUNT) + 1)) }
    })

    println("  PgPipe (mutable)...")
    results.add(runBenchmark("Foundations+PgPipe (mutable)", CacheCfg.TASK_COUNT, CacheCfg.WARMUP_OPS, CacheCfg.MEASURED_ITERATIONS) { i ->
        val idx = ThreadLocalRandom.current().nextInt(workingSetSize)
        val tpl = pipeTemplates[idx]
        pipePool.transact { conn -> conn.execute(tpl.on((i % ROW_COUNT) + 1)) }
    })
    pipePool.close()

    val rawQueries = generateDistinctQueries(workingSetSize)
    val rawDs = createHikariPool(PG_PORT, CacheCfg.POOL_SIZE)

    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..CacheCfg.WARMUP_OPS).map { i -> exec.submit<Any?> {
            rawDs.connection.use { conn ->
                conn.prepareStatement(rawQueries[i % workingSetSize]).use { ps ->
                    ps.setInt(1, (i % ROW_COUNT) + 1)
                    ps.executeQuery().use { rs -> rs.next() }
                }
            }
        }}.forEach(Future<*>::get)
    }

    println("  Raw JDBC (server-side)...")
    results.add(runBenchmark("Raw JDBC", CacheCfg.TASK_COUNT, CacheCfg.WARMUP_OPS, CacheCfg.MEASURED_ITERATIONS) { i ->
        val idx = ThreadLocalRandom.current().nextInt(workingSetSize)
        rawDs.connection.use { conn ->
            conn.prepareStatement(rawQueries[idx]).use { ps ->
                ps.setInt(1, (i % ROW_COUNT) + 1)
                ps.executeQuery().use { rs ->
                    if (rs.next()) readBenchRow(rs) else null
                }
            }
        }
    })
    rawDs.close()

    printResults("Cache Churn ($workingSetSize queries)", results, "ops/sec")
    println()
    return results
}
