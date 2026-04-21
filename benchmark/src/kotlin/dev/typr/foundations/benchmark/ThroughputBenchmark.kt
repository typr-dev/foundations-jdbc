package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.TransactorJdbc
import dev.typr.foundations.pg.PgPipelinePool
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple as VxTuple
import org.HdrHistogram.ConcurrentHistogram
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future

// ==================== Local Constants ====================

private object Cfg {
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

// ==================== Between-iteration setup variant ====================

private fun runBenchmarkWithIterSetup(
    name: String,
    taskCount: Int,
    warmupOps: Int,
    iterations: Int,
    iterSetup: () -> Unit,
    op: (Int) -> Any?,
): BenchResult {
    warmup(warmupOps, op)
    iterSetup()

    val histogram = ConcurrentHistogram(1, 600_000_000_000L, 3)
    val wallTimes = LongArray(iterations)

    for (iter in 0 until iterations) {
        iterSetup()
        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (1..taskCount).map { i ->
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    op(i)
                    histogram.recordValue(System.nanoTime() - start)
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    val medianWall = wallTimes.sorted()[iterations / 2]
    return BenchResult(name, taskCount.toLong(), medianWall, histogram)
}

// ==================== Main ====================

fun main() = runThroughputBenchmark()

fun runThroughputBenchmark() {
    println("=== Concurrent Throughput Benchmark ===")
    println("PostgreSQL at $PG_HOST:$PG_PORT/$PG_DB")
    println("Pool size: ${Cfg.POOL_SIZE} connections (both sides)")
    println("Concurrency: ${Cfg.TASK_COUNT} concurrent tasks")
    println("Warmup: ${Cfg.WARMUP_OPS} ops, Measured: ${Cfg.TASK_COUNT} ops x ${Cfg.MEASURED_ITERATIONS} iterations")
    println()

    setupBenchTables(PG_PORT)
    verifyPgPipelineCorrectness()

    val pointReadResults = mutableListOf<BenchResult>()
    val insertResults = mutableListOf<BenchResult>()
    val mixedResults = mutableListOf<BenchResult>()

    runRawJdbcBenchmark(pointReadResults, insertResults, mixedResults)
    runFoundationsBenchmark(pointReadResults, insertResults, mixedResults)
    runFoundationsPgPipelineBenchmark(pointReadResults, insertResults, mixedResults)
    runVertxAsyncBenchmark(pointReadResults, insertResults, mixedResults)
    runHibernateBenchmark(pointReadResults, insertResults, mixedResults)

    printResults("Point Read (SELECT WHERE id = ?)", pointReadResults, "ops/sec")
    printResults("Single Insert", insertResults, "ops/sec")
    printResults("Mixed 80/20 Read/Write", mixedResults, "ops/sec")

    println()
    println("=".repeat(70))
    println("=== Pipelining Advantage: Scarce Connections & Batch Fan-Out ===")
    println("=".repeat(70))

    val scarceResults = mutableListOf<BenchResult>()
    val batchResults = mutableListOf<BenchResult>()

    runScarceConnectionsBenchmark(scarceResults, batchResults)

    printResults("Scarce Connections: ${Cfg.SCARCE_TASK_COUNT} point reads, ${Cfg.SCARCE_CONNECTIONS} conns", scarceResults, "ops/sec")
    printResults("Batch Fan-Out: ${Cfg.BATCH_TASK_COUNT} page loads x ${Cfg.BATCH_SIZE} queries, ${Cfg.BATCH_CONNECTIONS} conns", batchResults, "ops/sec")

    val report = MarkdownReport("Throughput Benchmark")
    report.section("Point Read", "${Cfg.TASK_COUNT} concurrent reads, ${Cfg.POOL_SIZE} connections.", pointReadResults, "ops/sec",
        """
        pool.execute(selectById.on(42))
        """.trimIndent()
    )
    report.section("Single Insert", "${Cfg.TASK_COUNT} concurrent inserts, ${Cfg.POOL_SIZE} connections.", insertResults, "ops/sec",
        """
        pool.execute(insertProduct.on("Widget", price, true, now, "Electronics"))
        """.trimIndent()
    )
    report.section("Mixed 80/20 Read/Write", "${Cfg.TASK_COUNT} concurrent ops (80% reads, 20% writes), ${Cfg.POOL_SIZE} connections.", mixedResults, "ops/sec",
        """
        pool.execute(selectById.on(42))
        pool.execute(insertProduct.on("Widget", price, true, now, "Electronics"))
        """.trimIndent()
    )
    report.section("Scarce Connections", "${Cfg.SCARCE_TASK_COUNT} point reads, ${Cfg.SCARCE_CONNECTIONS} connections.", scarceResults, "ops/sec",
        """
        pool.execute(selectById.on(42))
        """.trimIndent()
    )
    report.section("Batch Fan-Out", "${Cfg.BATCH_TASK_COUNT} page loads x ${Cfg.BATCH_SIZE} queries, ${Cfg.BATCH_CONNECTIONS} connections.", batchResults, "ops/sec",
        """
        pool.execute(
            selectById.on(1)
                .combine(selectById.on(2))
                .combine(selectById.on(3))
        )
        """.trimIndent()
    )
    report.writeTo(REPORT_DIR, "throughput.md")
}

// ==================== Correctness Verification ====================

fun verifyPgPipelineCorrectness() {
    println("=== CORRECTNESS VERIFICATION ===")
    println()

    val pool = createPipelinePool(PG_PORT, 5)
    val (jdbcDs, tx) = createTransactor(PG_PORT, 2)
    try {
    val selectByIdTemplate = selectByIdTemplate()

    println("Test 1: Sequential reads of 100 different IDs, compare PgPipe vs JDBC...")
    for (id in 1..100) {
        val pipeResult = pool.execute(selectByIdTemplate.on(id))
        val jdbcResult = tx.execute(selectByIdTemplate.on(id))

        check(pipeResult.isPresent) { "PgPipe: missing row id=$id" }
        check(jdbcResult.isPresent) { "JDBC: missing row id=$id" }

        val p = pipeResult.get()
        val j = jdbcResult.get()

        check(p.id == id) { "PgPipe: expected id=$id, got ${p.id}" }
        check(p.id == j.id) { "ID mismatch: PgPipe=${p.id}, JDBC=${j.id}" }
        check(p.name == j.name) { "Name mismatch for id=$id: PgPipe='${p.name}', JDBC='${j.name}'" }
        check(p.value.compareTo(j.value) == 0) { "Value mismatch for id=$id: PgPipe=${p.value}, JDBC=${j.value}" }
        check(p.active == j.active) { "Active mismatch for id=$id: PgPipe=${p.active}, JDBC=${j.active}" }
        check(p.createdAt == j.createdAt) { "CreatedAt mismatch for id=$id: PgPipe=${p.createdAt}, JDBC=${j.createdAt}" }
        check(p.category == j.category) { "Category mismatch for id=$id: PgPipe='${p.category}', JDBC='${j.category}'" }
    }
    println("  PASS: All 100 rows match JDBC exactly (all 6 columns verified)")

    println("Test 2: 1000 concurrent reads, verify each returns correct id...")
    val errors = java.util.concurrent.ConcurrentLinkedQueue<String>()
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        val futures = (1..1000).map { i ->
            val id = (i % ROW_COUNT) + 1
            exec.submit<Any?> {
                val result = pool.execute(selectByIdTemplate.on(id))
                if (!result.isPresent) {
                    errors.add("id=$id: empty result")
                } else {
                    val row = result.get()
                    if (row.id != id) errors.add("id=$id: got id=${row.id}")
                    val expectedName = "Product #$id — ${CATEGORIES[id % CATEGORIES.size]} item with detailed description"
                    if (row.name != expectedName) errors.add("id=$id: expected name='$expectedName', got '${row.name}'")
                    val expectedCategory = CATEGORIES[id % CATEGORIES.size]
                    if (row.category != expectedCategory) errors.add("id=$id: expected category='$expectedCategory', got '${row.category}'")
                    val expectedActive = id % 3 != 0
                    if (row.active != expectedActive) errors.add("id=$id: expected active=$expectedActive, got ${row.active}")
                }
            }
        }
        futures.forEach(Future<*>::get)
    }
    if (errors.isNotEmpty()) {
        println("  FAIL: ${errors.size} errors:")
        errors.take(20).forEach { println("    $it") }
        throw IllegalStateException("Correctness verification failed!")
    }
    println("  PASS: All 1000 concurrent reads returned correct id, name, category, active")

    println("Test 3: Query for non-existent id=999999...")
    val missing = pool.execute(selectByIdTemplate.on(999999))
    check(!missing.isPresent) { "PgPipe: expected empty for id=999999, got ${missing.get()}" }
    println("  PASS: Empty Optional returned")

    println("Test 4: Repeated same-id query 100x, verify consistency...")
    val firstResult = pool.execute(selectByIdTemplate.on(42)).get()
    for (i in 1..100) {
        val r = pool.execute(selectByIdTemplate.on(42)).get()
        check(r.id == 42) { "Repeat $i: expected id=42, got ${r.id}" }
        check(r.name == firstResult.name) { "Repeat $i: name changed" }
        check(r.value.compareTo(firstResult.value) == 0) { "Repeat $i: value changed" }
    }
    println("  PASS: 100 repeats of id=42 all identical")

    println("Test 5: Interleaved queries (id=1, id=9999, id=1, id=9999) x50...")
    for (i in 1..50) {
        val r1 = pool.execute(selectByIdTemplate.on(1)).get()
        val r9999 = pool.execute(selectByIdTemplate.on(9999)).get()
        check(r1.id == 1) { "Interleave $i: expected id=1, got ${r1.id}" }
        check(r9999.id == 9999) { "Interleave $i: expected id=9999, got ${r9999.id}" }
        check(r1.name != r9999.name) { "Interleave $i: id=1 and id=9999 have same name!" }
    }
    println("  PASS: No cross-contamination between interleaved queries")

    println("Test 6: 5000 concurrent queries across all 10000 IDs, verify every result...")
    val errors2 = java.util.concurrent.ConcurrentLinkedQueue<String>()
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        val futures = (1..5000).map { i ->
            val id = (i % ROW_COUNT) + 1
            exec.submit<Any?> {
                val result = pool.execute(selectByIdTemplate.on(id))
                if (!result.isPresent) {
                    errors2.add("id=$id: empty result")
                } else {
                    val row = result.get()
                    if (row.id != id) errors2.add("id=$id: got id=${row.id} (WRONG ROW!)")
                }
            }
        }
        futures.forEach(Future<*>::get)
    }
    if (errors2.isNotEmpty()) {
        println("  FAIL: ${errors2.size} errors:")
        errors2.take(20).forEach { println("    $it") }
        throw IllegalStateException("Correctness verification failed!")
    }
    println("  PASS: 5000 concurrent queries all returned correct rows")

    println("Test 7: Insert via PgPipe, read back via JDBC and PgPipe, compare...")
    pool.update(Fragment.of("DELETE FROM bench_write WHERE name = 'pgpipe_verify_test'"))
    val insertSql = Fragment.of("INSERT INTO bench_write (name, value, active, created_at, category) VALUES (")
        .append(Fragment.value("pgpipe_verify_test", dev.typr.foundations.PgTypes.text)).append(", ")
        .append(Fragment.value(BigDecimal("123.45"), dev.typr.foundations.PgTypes.numeric)).append(", ")
        .append(Fragment.value(true, dev.typr.foundations.PgTypes.bool)).append(", ")
        .append(Fragment.value(LocalDateTime.of(2025, 6, 15, 12, 30), dev.typr.foundations.PgTypes.timestamp)).append(", ")
        .append(Fragment.value("TestCategory", dev.typr.foundations.PgTypes.text)).append(")")

    pool.update(insertSql)

    val readBackQuery = Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_write WHERE name = ")
        .append(Fragment.value("pgpipe_verify_test", dev.typr.foundations.PgTypes.text))
        .query(benchReadCodec().first())

    val pipeReadBack = pool.execute(readBackQuery)
    val jdbcReadBack = tx.execute(readBackQuery)

    check(pipeReadBack.isPresent) { "PgPipe read-back: empty" }
    check(jdbcReadBack.isPresent) { "JDBC read-back: empty" }

    val pr = pipeReadBack.get()
    val jr = jdbcReadBack.get()
    check(pr.name == "pgpipe_verify_test") { "PgPipe read-back name: ${pr.name}" }
    check(pr.value.compareTo(BigDecimal("123.45")) == 0) { "PgPipe read-back value: ${pr.value}" }
    check(pr.active == true) { "PgPipe read-back active: ${pr.active}" }
    check(pr.category == "TestCategory") { "PgPipe read-back category: ${pr.category}" }
    check(pr.name == jr.name) { "Read-back name mismatch: PgPipe=${pr.name}, JDBC=${jr.name}" }
    check(pr.value.compareTo(jr.value) == 0) { "Read-back value mismatch" }
    println("  PASS: Insert + read-back matches between PgPipe and JDBC")

    println()
    println("=== ALL CORRECTNESS TESTS PASSED ===")
    println()

    } finally {
        closeQuietly("correctness PgPipeline") { pool.close() }
        closeQuietly("correctness JDBC") { jdbcDs.close() }
    }
}

// ==================== Truncation Helpers ====================

private fun truncateViaDs(ds: javax.sql.DataSource) {
    ds.connection.use { conn -> conn.createStatement().use { it.execute("TRUNCATE bench_write") } }
}

private fun truncateViaTx(tx: TransactorJdbc) {
    tx.executeJdbc { conn -> conn.createStatement().use { it.execute("TRUNCATE bench_write") }; null }
}

private fun truncateViaPgPipe(pool: PgPipelinePool) {
    pool.update(Fragment.of("TRUNCATE bench_write"))
}

private fun truncateViaPgPool(pool: PgPool) {
    pool.query("TRUNCATE bench_write").execute().toCompletionStage().toCompletableFuture().join()
}

private fun truncateViaHibernate(sf: org.hibernate.SessionFactory) {
    sf.openSession().use { session ->
        session.beginTransaction()
        session.createNativeMutationQuery("TRUNCATE bench_write").executeUpdate()
        session.transaction.commit()
    }
}

// ==================== Raw JDBC ====================

private fun runRawJdbcBenchmark(
    pointReadResults: MutableList<BenchResult>,
    insertResults: MutableList<BenchResult>,
    mixedResults: MutableList<BenchResult>,
) {
    val ds = createHikariPool(PG_PORT, Cfg.POOL_SIZE)
    try {

    println("--- Raw JDBC + HikariCP + Virtual Threads ---")
    println("  (PreparedStatement, positional get/set)")

    println("  Warming up pool...")
    warmup(Cfg.POOL_SIZE) {
        ds.connection.use { conn ->
            conn.prepareStatement("SELECT 1").use { ps -> ps.executeQuery().close() }
        }
    }

    println("  Running point reads...")
    pointReadResults.add(runBenchmark("Raw JDBC", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS) { i ->
        ds.connection.use { conn ->
            conn.prepareStatement(RAW_SELECT_SQL).use { ps ->
                ps.setInt(1, (i % ROW_COUNT) + 1)
                ps.executeQuery().use { rs -> if (rs.next()) readBenchRow(rs) else null }
            }
        }
    })

    println("  Running inserts...")
    truncateViaDs(ds)
    insertResults.add(runBenchmarkWithIterSetup("Raw JDBC", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS, { truncateViaDs(ds) }) { i ->
        ds.connection.use { conn ->
            conn.prepareStatement(RAW_INSERT_SQL).use { ps ->
                ps.setString(1, "Item $i")
                ps.setBigDecimal(2, BigDecimal("${i % 1000}.99"))
                ps.setBoolean(3, i % 3 != 0)
                ps.setObject(4, LocalDateTime.now())
                ps.setString(5, "Bench")
                ps.executeUpdate()
            }
        }
    })

    println("  Running mixed 80/20...")
    truncateViaDs(ds)
    mixedResults.add(runBenchmarkWithIterSetup("Raw JDBC", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS, { truncateViaDs(ds) }) { i ->
        if (i % 5 == 0) {
            ds.connection.use { conn ->
                conn.prepareStatement(RAW_INSERT_SQL).use { ps ->
                    ps.setString(1, "Item $i")
                    ps.setBigDecimal(2, BigDecimal("${i % 1000}.99"))
                    ps.setBoolean(3, i % 3 != 0)
                    ps.setObject(4, LocalDateTime.now())
                    ps.setString(5, "Bench")
                    ps.executeUpdate()
                }
            }
        } else {
            ds.connection.use { conn ->
                conn.prepareStatement(RAW_SELECT_SQL).use { ps ->
                    ps.setInt(1, (i % ROW_COUNT) + 1)
                    ps.executeQuery().use { rs -> if (rs.next()) readBenchRow(rs) else null }
                }
            }
        }
    })

    } finally {
        closeQuietly("Raw JDBC HikariCP") { ds.close() }
    }
}

// ==================== Foundations + HikariCP ====================

private fun runFoundationsBenchmark(
    pointReadResults: MutableList<BenchResult>,
    insertResults: MutableList<BenchResult>,
    mixedResults: MutableList<BenchResult>,
) {
    val (ds, tx) = createTransactor(PG_PORT, Cfg.POOL_SIZE)
    try {
    val selectTpl = selectByIdTemplate()
    val insertTpl = insertTemplate()

    println("--- Foundations + HikariCP + Virtual Threads ---")
    println("  (prepareThreshold=1, templates)")

    println("  Running point reads...")
    pointReadResults.add(runBenchmark("Foundations+Hikari", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS) { i ->
        tx.execute(selectTpl.on((i % ROW_COUNT) + 1))
    })

    println("  Running inserts...")
    truncateViaTx(tx)
    insertResults.add(runBenchmarkWithIterSetup("Foundations+Hikari", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS, { truncateViaTx(tx) }) { i ->
        tx.execute(insertTpl.on("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
    })

    println("  Running mixed 80/20...")
    truncateViaTx(tx)
    mixedResults.add(runBenchmarkWithIterSetup("Foundations+Hikari", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS, { truncateViaTx(tx) }) { i ->
        if (i % 5 == 0) tx.execute(insertTpl.on("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
        else tx.execute(selectTpl.on((i % ROW_COUNT) + 1))
    })

    } finally {
        closeQuietly("Fnd+Hikari HikariCP") { ds.close() }
    }
}

// ==================== Foundations + PG Pipeline ====================

private fun runFoundationsPgPipelineBenchmark(
    pointReadResults: MutableList<BenchResult>,
    insertResults: MutableList<BenchResult>,
    mixedResults: MutableList<BenchResult>,
) {
    val pool = createPipelinePool(PG_PORT, Cfg.POOL_SIZE)
    try {
    val selectTpl = selectByIdTemplate()
    val insertTpl = insertTemplate()

    println("\n--- Foundations + PG Pipeline (zero deps) ---")
    println("  (pipelining=256, connections=${Cfg.POOL_SIZE})")

    println("  Running point reads (readonly)...")
    pointReadResults.add(runBenchmark("Foundations+PgPipe (readonly)", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS) { i ->
        pool.transactRead { conn -> conn.execute(selectTpl.on((i % ROW_COUNT) + 1)) }
    })

    println("  Running point reads (mutable)...")
    pointReadResults.add(runBenchmark("Foundations+PgPipe (mutable)", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS) { i ->
        pool.transact { conn -> conn.execute(selectTpl.on((i % ROW_COUNT) + 1)) }
    })

    println("  Running inserts...")
    truncateViaPgPipe(pool)
    insertResults.add(runBenchmarkWithIterSetup("Foundations+PgPipe (mutable)", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS, { truncateViaPgPipe(pool) }) { i ->
        pool.transact { conn -> conn.execute(insertTpl.on("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench")) }
    })

    println("  Running mixed 80/20...")
    truncateViaPgPipe(pool)
    mixedResults.add(runBenchmarkWithIterSetup("Foundations+PgPipe (mixed)", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS, { truncateViaPgPipe(pool) }) { i ->
        if (i % 5 == 0) pool.transact { conn -> conn.execute(insertTpl.on("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench")) }
        else pool.transactRead { conn -> conn.execute(selectTpl.on((i % ROW_COUNT) + 1)) }
    })

    } finally {
        closeQuietly("Foundations+PgPipe") { pool.close() }
    }
}

// ==================== Vert.x Row Helper ====================

private fun readVxBenchRow(row: io.vertx.sqlclient.Row): BenchRow = BenchRow(
    row.getInteger("id"),
    row.getString("name"),
    row.getNumeric("value").bigDecimalValue(),
    row.getBoolean("active"),
    row.getLocalDateTime("created_at"),
    row.getString("category"),
)

// ==================== Vert.x Pure Async (no virtual threads) ====================

private fun runVertxAsyncBenchmark(
    pointReadResults: MutableList<BenchResult>,
    insertResults: MutableList<BenchResult>,
    mixedResults: MutableList<BenchResult>,
) {
    val rawVx = createRawVertxPool(PG_PORT, Cfg.POOL_SIZE)
    try {
    val pool = rawVx.pool

    println("\n--- Vert.x Pure Async (no virtual threads) ---")
    println("  (pipelining=256, prepared stmt cache=256)")

    println("  Warming up pool...")
    for (i in 1..Cfg.POOL_SIZE) {
        pool.query("SELECT 1").execute().toCompletionStage().toCompletableFuture().join()
    }

    println("  Running point reads...")
    pointReadResults.add(runAsyncBenchmark("Vert.x (async)", Cfg.TASK_COUNT, Cfg.MEASURED_ITERATIONS) { i ->
        pool.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of((i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    })

    println("  Running inserts...")
    truncateViaPgPool(pool)
    insertResults.add(runAsyncBenchmarkWithIterSetup("Vert.x (async)", Cfg.TASK_COUNT, Cfg.MEASURED_ITERATIONS, { truncateViaPgPool(pool) }) { i ->
        pool.preparedQuery(VX_INSERT_SQL)
            .execute(VxTuple.of("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
            .toCompletionStage().toCompletableFuture()
    })

    println("  Running mixed 80/20...")
    truncateViaPgPool(pool)
    mixedResults.add(runAsyncBenchmarkWithIterSetup("Vert.x (async)", Cfg.TASK_COUNT, Cfg.MEASURED_ITERATIONS, { truncateViaPgPool(pool) }) { i ->
        if (i % 5 == 0) {
            pool.preparedQuery(VX_INSERT_SQL)
                .execute(VxTuple.of("Item $i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
                .toCompletionStage().toCompletableFuture()
        } else {
            pool.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of((i % ROW_COUNT) + 1))
                .toCompletionStage().toCompletableFuture()
        }
    })

    } finally {
        closeQuietly("Vert.x async") { rawVx.close() }
    }
}

private fun runAsyncBenchmarkWithIterSetup(
    name: String,
    taskCount: Int,
    iterations: Int,
    iterSetup: () -> Unit,
    op: (Int) -> CompletableFuture<*>,
): BenchResult {
    iterSetup()
    val histogram = org.HdrHistogram.ConcurrentHistogram(1, 600_000_000_000L, 3)
    val wallTimes = LongArray(iterations)

    for (iter in 0 until iterations) {
        iterSetup()
        val wallStart = System.nanoTime()
        val starts = LongArray(taskCount)
        val futures = (1..taskCount).map { i ->
            starts[i - 1] = System.nanoTime()
            val idx = i - 1
            op(i).thenApply { histogram.recordValue(System.nanoTime() - starts[idx]) }
        }
        CompletableFuture.allOf(*futures.toTypedArray()).join()
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    val medianWall = wallTimes.sorted()[iterations / 2]
    return BenchResult(name, taskCount.toLong(), medianWall, histogram)
}

// ==================== Hibernate ====================

private fun runHibernateBenchmark(
    pointReadResults: MutableList<BenchResult>,
    insertResults: MutableList<BenchResult>,
    mixedResults: MutableList<BenchResult>,
) {
    val sf = createHibernateSessionFactory(PG_PORT, Cfg.POOL_SIZE)
    try {

    println("\n--- Hibernate + HikariCP + Virtual Threads ---")
    println("  (batch_size=25, order_inserts=true)")

    println("  Warming up pool...")
    warmup(Cfg.POOL_SIZE) {
        sf.openSession().use { session ->
            session.find(BenchRowEntity::class.java, 1)
        }
    }

    println("  Running point reads...")
    pointReadResults.add(runBenchmark("Hibernate", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS) { i ->
        sf.openSession().use { session ->
            session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
        }
    })

    println("  Running inserts...")
    truncateViaHibernate(sf)
    insertResults.add(runBenchmarkWithIterSetup("Hibernate", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS, { truncateViaHibernate(sf) }) { i ->
        sf.openSession().use { session ->
            session.beginTransaction()
            val entity = WriteRowEntity()
            entity.name = "Item $i"
            entity.value = java.math.BigDecimal("${i % 1000}.99")
            entity.active = i % 3 != 0
            entity.createdAt = java.time.LocalDateTime.now()
            entity.category = "Bench"
            session.persist(entity)
            session.transaction.commit()
        }
    })

    println("  Running mixed 80/20...")
    truncateViaHibernate(sf)
    mixedResults.add(runBenchmarkWithIterSetup("Hibernate", Cfg.TASK_COUNT, Cfg.WARMUP_OPS, Cfg.MEASURED_ITERATIONS, { truncateViaHibernate(sf) }) { i ->
        if (i % 5 == 0) {
            sf.openSession().use { session ->
                session.beginTransaction()
                val entity = WriteRowEntity()
                entity.name = "Item $i"
                entity.value = java.math.BigDecimal("${i % 1000}.99")
                entity.active = i % 3 != 0
                entity.createdAt = java.time.LocalDateTime.now()
                entity.category = "Bench"
                session.persist(entity)
                session.transaction.commit()
            }
        } else {
            sf.openSession().use { session ->
                session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
            }
        }
    })

    } finally {
        closeQuietly("Hibernate") { sf.close() }
    }
}

// ==================== Pipelining-Advantage Benchmarks ====================

private fun runScarceConnectionsBenchmark(
    scarceResults: MutableList<BenchResult>,
    batchResults: MutableList<BenchResult>,
) {
    val selectTpl = selectByIdTemplate()

    // --- Raw JDBC ---
    run {
    println("\n--- Scarce Connections: Raw JDBC (${Cfg.SCARCE_CONNECTIONS} conns) ---")
    val rawScarceDs = createHikariPool(PG_PORT, Cfg.SCARCE_CONNECTIONS)
    try {

    warmup(500) { i ->
        rawScarceDs.connection.use { conn ->
            conn.prepareStatement(RAW_SELECT_SQL).use { ps ->
                ps.setInt(1, (i % ROW_COUNT) + 1)
                ps.executeQuery().use { rs -> rs.next() }
            }
        }
    }

    println("  Running ${Cfg.SCARCE_TASK_COUNT} point reads...")
    scarceResults.add(runBenchmarkNoWarmup("Raw JDBC", Cfg.SCARCE_TASK_COUNT, Cfg.MEASURED_ITERATIONS) { i ->
        rawScarceDs.connection.use { conn ->
            conn.prepareStatement(RAW_SELECT_SQL).use { ps ->
                ps.setInt(1, (i % ROW_COUNT) + 1)
                ps.executeQuery().use { rs -> if (rs.next()) readBenchRow(rs) else null }
            }
        }
    })

    println("  Running batch fan-out (${Cfg.BATCH_TASK_COUNT} x ${Cfg.BATCH_SIZE})...")
    batchResults.add(runBatchFanOutBenchmark("Raw JDBC", Cfg.BATCH_TASK_COUNT, Cfg.BATCH_SIZE, 0, Cfg.MEASURED_ITERATIONS) { taskIdx, batchIdx ->
        rawScarceDs.connection.use { conn ->
            conn.prepareStatement(RAW_SELECT_SQL).use { ps ->
                ps.setInt(1, ((taskIdx * Cfg.BATCH_SIZE + batchIdx) % ROW_COUNT) + 1)
                ps.executeQuery().use { rs -> if (rs.next()) readBenchRow(rs) else null }
            }
        }
    })

    } finally {
        closeQuietly("scarce Raw JDBC") { rawScarceDs.close() }
    }
    }

    // --- Foundations + HikariCP ---
    run {
    println("\n--- Scarce Connections: Foundations + HikariCP (${Cfg.SCARCE_CONNECTIONS} conns) ---")
    val (scarceDs, scarceTx) = createTransactor(PG_PORT, Cfg.SCARCE_CONNECTIONS)
    try {

    warmup(500) { i -> scarceTx.execute(selectTpl.on((i % ROW_COUNT) + 1)) }

    println("  Running ${Cfg.SCARCE_TASK_COUNT} point reads...")
    scarceResults.add(runBenchmarkNoWarmup("Foundations+Hikari", Cfg.SCARCE_TASK_COUNT, Cfg.MEASURED_ITERATIONS) { i ->
        scarceTx.execute(selectTpl.on((i % ROW_COUNT) + 1))
    })

    println("  Running batch fan-out (${Cfg.BATCH_TASK_COUNT} x ${Cfg.BATCH_SIZE})...")
    batchResults.add(runBatchFanOutBenchmark("Foundations+Hikari", Cfg.BATCH_TASK_COUNT, Cfg.BATCH_SIZE, 0, Cfg.MEASURED_ITERATIONS) { taskIdx, batchIdx ->
        scarceTx.execute(selectTpl.on(((taskIdx * Cfg.BATCH_SIZE + batchIdx) % ROW_COUNT) + 1))
    })

    } finally {
        closeQuietly("scarce Fnd+Hikari") { scarceDs.close() }
    }
    }

    // --- Foundations + PG Pipeline ---
    run {
    println("\n--- Scarce Connections: Foundations + PG Pipeline (${Cfg.SCARCE_CONNECTIONS} conns) ---")
    val pgPipePool = createPipelinePool(PG_PORT, Cfg.SCARCE_CONNECTIONS)
    try {

    warmup(500) { i -> pgPipePool.execute(selectTpl.on((i % ROW_COUNT) + 1)) }

    println("  Running ${Cfg.SCARCE_TASK_COUNT} point reads (readonly)...")
    scarceResults.add(runBenchmarkNoWarmup("Foundations+PgPipe (readonly)", Cfg.SCARCE_TASK_COUNT, Cfg.MEASURED_ITERATIONS) { i ->
        pgPipePool.transactRead { conn -> conn.execute(selectTpl.on((i % ROW_COUNT) + 1)) }
    })

    println("  Running ${Cfg.SCARCE_TASK_COUNT} point reads (mutable)...")
    scarceResults.add(runBenchmarkNoWarmup("Foundations+PgPipe (mutable)", Cfg.SCARCE_TASK_COUNT, Cfg.MEASURED_ITERATIONS) { i ->
        pgPipePool.transact { conn -> conn.execute(selectTpl.on((i % ROW_COUNT) + 1)) }
    })

    println("  Running batch fan-out (${Cfg.BATCH_TASK_COUNT} x ${Cfg.BATCH_SIZE})...")
    batchResults.add(runBatchFanOutBenchmark("Foundations+PgPipe", Cfg.BATCH_TASK_COUNT, Cfg.BATCH_SIZE, 0, Cfg.MEASURED_ITERATIONS) { taskIdx, batchIdx ->
        pgPipePool.execute(selectTpl.on(((taskIdx * Cfg.BATCH_SIZE + batchIdx) % ROW_COUNT) + 1))
    })

    } finally {
        closeQuietly("scarce PgPipeline") { pgPipePool.close() }
    }
    }

    // --- Vert.x async ---
    run {
    println("\n--- Scarce Connections: Vert.x Async (${Cfg.SCARCE_CONNECTIONS} conns) ---")
    val rawVx2 = createRawVertxPool(PG_PORT, Cfg.SCARCE_CONNECTIONS)
    try {
    val vxPool2 = rawVx2.pool

    for (i in 1..Cfg.SCARCE_CONNECTIONS) {
        vxPool2.query("SELECT 1").execute().toCompletionStage().toCompletableFuture().join()
    }
    val vxAsyncWarmupFutures = (1..500).map { i ->
        vxPool2.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of((i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    }
    CompletableFuture.allOf(*vxAsyncWarmupFutures.toTypedArray()).join()

    println("  Running ${Cfg.SCARCE_TASK_COUNT} point reads...")
    scarceResults.add(runAsyncBenchmark("Vert.x (async)", Cfg.SCARCE_TASK_COUNT, Cfg.MEASURED_ITERATIONS) { i ->
        vxPool2.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of((i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    })

    println("  Running batch fan-out (${Cfg.BATCH_TASK_COUNT} x ${Cfg.BATCH_SIZE})...")
    batchResults.add(runAsyncBatchBenchmark("Vert.x (async)", Cfg.BATCH_TASK_COUNT, Cfg.BATCH_SIZE, Cfg.MEASURED_ITERATIONS) { taskIdx, batchIdx ->
        vxPool2.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(((taskIdx * Cfg.BATCH_SIZE + batchIdx) % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    })

    } finally {
        closeQuietly("scarce Vert.x") { rawVx2.close() }
    }
    }

    // --- Hibernate ---
    run {
    println("\n--- Scarce Connections: Hibernate (${Cfg.SCARCE_CONNECTIONS} conns) ---")
    val sf = createHibernateSessionFactory(PG_PORT, Cfg.SCARCE_CONNECTIONS)
    try {

    warmup(500) { i ->
        sf.openSession().use { session ->
            session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
        }
    }

    println("  Running ${Cfg.SCARCE_TASK_COUNT} point reads...")
    scarceResults.add(runBenchmarkNoWarmup("Hibernate", Cfg.SCARCE_TASK_COUNT, Cfg.MEASURED_ITERATIONS) { i ->
        sf.openSession().use { session ->
            session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
        }
    })

    println("  Running batch fan-out (${Cfg.BATCH_TASK_COUNT} x ${Cfg.BATCH_SIZE})...")
    batchResults.add(runBenchmarkNoWarmup("Hibernate", Cfg.BATCH_TASK_COUNT, Cfg.MEASURED_ITERATIONS) { taskIdx ->
        sf.openSession().use { session ->
            for (j in 0 until Cfg.BATCH_SIZE) {
                session.find(BenchRowEntity::class.java, ((taskIdx * Cfg.BATCH_SIZE + j) % ROW_COUNT) + 1)
            }
        }
    })

    } finally {
        closeQuietly("scarce Hibernate") { sf.close() }
    }
    }
}
