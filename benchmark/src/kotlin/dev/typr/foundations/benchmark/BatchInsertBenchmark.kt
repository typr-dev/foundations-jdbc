package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.PgText
import dev.typr.foundations.StreamingInsert
import dev.typr.foundations.Template
import dev.typr.foundations.connect.PgConfig
import dev.typr.foundations.hikari.HikariDataSourceFactory
import dev.typr.foundations.hikari.PoolConfig
import dev.typr.foundations.pg.PgPipelineConfig
import dev.typr.foundations.pg.PgPipelinePool
import io.vertx.sqlclient.Tuple as VxTuple
import org.HdrHistogram.ConcurrentHistogram
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.Executors
import java.util.concurrent.Future

// ==================== Configuration ====================

const val BATCH_INSERT_POOL_SIZE = 10
const val BATCH_INSERT_TOTAL_ROWS = 10_000
const val BATCH_INSERT_WARMUP_ITERATIONS = 3
const val BATCH_INSERT_MEASURED_ITERATIONS = 5

val BATCH_SIZES = listOf(100, 1000)
val CONCURRENCY_BATCH = listOf(1, 10)

// ==================== Main ====================

fun main() = runBatchInsertBenchmark()

fun runBatchInsertBenchmark() {
    println("=== Batch Insert Benchmark ===")
    println("PostgreSQL at $PG_HOST:$PG_PORT/$PG_DB")
    println("Pool size: $BATCH_INSERT_POOL_SIZE connections")
    println("Total rows per iteration: $BATCH_INSERT_TOTAL_ROWS")
    println("Batch sizes: $BATCH_SIZES")
    println("Concurrency levels: $CONCURRENCY_BATCH")
    println("Measured iterations: $BATCH_INSERT_MEASURED_ITERATIONS")
    println()

    val pgConfig = pgConfig(PG_PORT)

    val setupDs = HikariDataSourceFactory.create(pgConfig, PoolConfig.builder().maximumPoolSize(2).minimumIdle(2).build())
    setupDs.connection.use { conn ->
        conn.createStatement().use { stmt ->
            stmt.execute("DROP TABLE IF EXISTS bench_write")
            stmt.execute("""
                CREATE TABLE bench_write (
                    id BIGSERIAL PRIMARY KEY,
                    name VARCHAR(200) NOT NULL,
                    value DECIMAL(12,2) NOT NULL,
                    active BOOLEAN NOT NULL,
                    created_at TIMESTAMP NOT NULL,
                    category VARCHAR(50) NOT NULL
                )
            """.trimIndent())
            stmt.execute("ALTER SEQUENCE bench_write_id_seq INCREMENT BY 50")
        }
    }
    setupDs.close()

    val writeCodec = writeNamedCodec()
    val insertTemplate = Fragment.insertInto("bench_write", writeCodec)
    val pgText = PgText.from(writeCodec)

    val report = MarkdownReport("Batch Insert Benchmark")

    for (batchSize in BATCH_SIZES) {
        for (concurrency in CONCURRENCY_BATCH) {
            val totalRows = BATCH_INSERT_TOTAL_ROWS.toLong()
            val rowsPerTask = (totalRows / concurrency).toInt()

            println("--- batchSize=$batchSize, concurrency=$concurrency ($rowsPerTask rows/task) ---")

            val results = mutableListOf<BenchResult>()

            results.add(runRawJdbcBatch(pgConfig, batchSize, concurrency, rowsPerTask))
            results.add(runFoundationsBatch(pgConfig, insertTemplate, batchSize, concurrency, rowsPerTask))
            results.add(runPgPipelineBatch(pgConfig, insertTemplate, batchSize, concurrency, rowsPerTask))
            results.add(runPgPipelineCopy(pgConfig, writeCodec, pgText, concurrency, rowsPerTask))
            results.add(runVertxBatch(pgConfig, batchSize, concurrency, rowsPerTask))
            results.add(runHibernateBatch(pgConfig, batchSize, concurrency, rowsPerTask))

            val title = "Batch Insert: batchSize=$batchSize, concurrency=$concurrency, $totalRows rows"
            printResults(title, results, "rows/sec")

            report.section(
                "batchSize=$batchSize, concurrency=$concurrency",
                "$totalRows total rows, $rowsPerTask rows/task.",
                results,
                "rows/sec",
                """
                pool.execute(StreamingInsert.of(
                    "COPY bench_write (...) FROM STDIN",
                    1000, rows.iterator(), pgText
                ))
                """.trimIndent()
            )
        }
    }

    report.writeTo(REPORT_DIR, "batch-insert.md")
}

// ==================== Raw JDBC executeBatch ====================

fun runRawJdbcBatch(
    pgConfig: PgConfig,
    batchSize: Int,
    concurrency: Int,
    rowsPerTask: Int,
): BenchResult {
    val ds = HikariDataSourceFactory.create(
        pgConfig,
        PoolConfig.builder().maximumPoolSize(BATCH_INSERT_POOL_SIZE).minimumIdle(BATCH_INSERT_POOL_SIZE).build(),
    )
    try {
    val sql = RAW_INSERT_SQL

    for (w in 0 until BATCH_INSERT_WARMUP_ITERATIONS) {
        ds.connection.use { c -> c.createStatement().use { it.execute("TRUNCATE bench_write") } }
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map { taskIdx ->
                exec.submit<Any?> {
                    val rows = generateRows(rowsPerTask)
                    ds.connection.use { conn ->
                        conn.autoCommit = false
                        conn.prepareStatement(sql).use { ps ->
                            var count = 0
                            for (row in rows) {
                                ps.setString(1, row.name)
                                ps.setBigDecimal(2, row.value)
                                ps.setBoolean(3, row.active)
                                ps.setObject(4, row.createdAt)
                                ps.setString(5, row.category)
                                ps.addBatch()
                                count++
                                if (count % batchSize == 0) ps.executeBatch()
                            }
                            if (count % batchSize != 0) ps.executeBatch()
                        }
                        conn.commit()
                    }
                }
            }
            futures.forEach(Future<*>::get)
        }
    }

    val histogram = ConcurrentHistogram(1, 60_000_000_000L, 3)
    val wallTimes = LongArray(BATCH_INSERT_MEASURED_ITERATIONS)

    for (iter in 0 until BATCH_INSERT_MEASURED_ITERATIONS) {
        ds.connection.use { c -> c.createStatement().use { it.execute("TRUNCATE bench_write") } }

        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map { taskIdx ->
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    val rows = generateRows(rowsPerTask)
                    ds.connection.use { conn ->
                        conn.autoCommit = false
                        conn.prepareStatement(sql).use { ps ->
                            var count = 0
                            for (row in rows) {
                                ps.setString(1, row.name)
                                ps.setBigDecimal(2, row.value)
                                ps.setBoolean(3, row.active)
                                ps.setObject(4, row.createdAt)
                                ps.setString(5, row.category)
                                ps.addBatch()
                                count++
                                if (count % batchSize == 0) ps.executeBatch()
                            }
                            if (count % batchSize != 0) ps.executeBatch()
                        }
                        conn.commit()
                    }
                    histogram.recordValue(System.nanoTime() - start)
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    val medianWall = wallTimes.sorted()[BATCH_INSERT_MEASURED_ITERATIONS / 2]
    return BenchResult("Raw JDBC (batch)", concurrency.toLong() * rowsPerTask, medianWall, histogram)
    } finally {
        closeQuietly("JDBC batch HikariCP") { ds.close() }
    }
}

// ==================== Foundations+HikariCP updateMany ====================

fun runFoundationsBatch(
    pgConfig: PgConfig,
    insertTemplate: Template<WriteRow, Int>,
    batchSize: Int,
    concurrency: Int,
    rowsPerTask: Int,
): BenchResult {
    val ds = HikariDataSourceFactory.create(
        pgConfig,
        PoolConfig.builder().maximumPoolSize(BATCH_INSERT_POOL_SIZE).minimumIdle(BATCH_INSERT_POOL_SIZE).build(),
    )
    try {
    val tx = ds.transactor()

    @Suppress("UNCHECKED_CAST")
    val rowTemplate = insertTemplate as dev.typr.foundations.RowTemplate.Update<WriteRow>

    for (w in 0 until BATCH_INSERT_WARMUP_ITERATIONS) {
        ds.connection.use { c -> c.createStatement().use { it.execute("TRUNCATE bench_write") } }
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val rows = generateRows(rowsPerTask)
                    tx.execute(rowTemplate.onMany(rows.iterator()))
                }
            }
            futures.forEach(Future<*>::get)
        }
    }

    val histogram = ConcurrentHistogram(1, 60_000_000_000L, 3)
    val wallTimes = LongArray(BATCH_INSERT_MEASURED_ITERATIONS)

    for (iter in 0 until BATCH_INSERT_MEASURED_ITERATIONS) {
        ds.connection.use { c -> c.createStatement().use { it.execute("TRUNCATE bench_write") } }

        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    val rows = generateRows(rowsPerTask)
                    tx.execute(rowTemplate.onMany(rows.iterator()))
                    histogram.recordValue(System.nanoTime() - start)
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    val medianWall = wallTimes.sorted()[BATCH_INSERT_MEASURED_ITERATIONS / 2]
    return BenchResult("Foundations+Hikari", concurrency.toLong() * rowsPerTask, medianWall, histogram)
    } finally {
        closeQuietly("Fnd+Hikari batch HikariCP") { ds.close() }
    }
}

// ==================== PgPipeline updateMany (pipelined) ====================

fun runPgPipelineBatch(
    pgConfig: PgConfig,
    insertTemplate: Template<WriteRow, Int>,
    batchSize: Int,
    concurrency: Int,
    rowsPerTask: Int,
): BenchResult {
    val pipelineConfig = PgPipelineConfig.builder()
        .connectionCount(BATCH_INSERT_POOL_SIZE)
        .pipeliningLimit(256)
        .queryTimeout(java.time.Duration.ofMinutes(5))
        .build()
    val pool = PgPipelinePool.create(pgConfig, pipelineConfig)
    try {

    @Suppress("UNCHECKED_CAST")
    val rowTemplate = insertTemplate as dev.typr.foundations.RowTemplate.Update<WriteRow>

    for (w in 0 until BATCH_INSERT_WARMUP_ITERATIONS) {
        pool.update(Fragment.of("TRUNCATE bench_write"))
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val rows = generateRows(rowsPerTask)
                    pool.transact { conn -> conn.execute(rowTemplate.onMany(rows.iterator())) }
                }
            }
            futures.forEach(Future<*>::get)
        }
    }

    val histogram = ConcurrentHistogram(1, 60_000_000_000L, 3)
    val wallTimes = LongArray(BATCH_INSERT_MEASURED_ITERATIONS)

    for (iter in 0 until BATCH_INSERT_MEASURED_ITERATIONS) {
        pool.update(Fragment.of("TRUNCATE bench_write"))

        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    val rows = generateRows(rowsPerTask)
                    pool.transact { conn -> conn.execute(rowTemplate.onMany(rows.iterator())) }
                    histogram.recordValue(System.nanoTime() - start)
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    val medianWall = wallTimes.sorted()[BATCH_INSERT_MEASURED_ITERATIONS / 2]
    return BenchResult("Foundations+PgPipe (mutable batch)", concurrency.toLong() * rowsPerTask, medianWall, histogram)
    } finally {
        closeQuietly("Foundations+PgPipe (mutable batch)") { pool.close() }
    }
}

// ==================== PgPipeline COPY ====================

fun runPgPipelineCopy(
    pgConfig: PgConfig,
    writeCodec: dev.typr.foundations.RowCodecNamed<WriteRow>,
    pgText: PgText<WriteRow>,
    concurrency: Int,
    rowsPerTask: Int,
): BenchResult {
    val pipelineConfig = PgPipelineConfig.builder()
        .connectionCount(BATCH_INSERT_POOL_SIZE)
        .pipeliningLimit(256)
        .queryTimeout(java.time.Duration.ofMinutes(5))
        .build()
    val pool = PgPipelinePool.create(pgConfig, pipelineConfig)
    try {

    val copyCmd = "COPY bench_write (name, value, active, created_at, category) FROM STDIN"

    for (w in 0 until BATCH_INSERT_WARMUP_ITERATIONS) {
        pool.update(Fragment.of("TRUNCATE bench_write"))
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val rows = generateRows(rowsPerTask)
                    pool.transact { conn -> conn.execute(StreamingInsert.of(copyCmd, 1000, rows.iterator(), pgText)) }
                }
            }
            futures.forEach(Future<*>::get)
        }
    }

    val histogram = ConcurrentHistogram(1, 60_000_000_000L, 3)
    val wallTimes = LongArray(BATCH_INSERT_MEASURED_ITERATIONS)

    for (iter in 0 until BATCH_INSERT_MEASURED_ITERATIONS) {
        pool.update(Fragment.of("TRUNCATE bench_write"))

        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    val rows = generateRows(rowsPerTask)
                    pool.transact { conn -> conn.execute(StreamingInsert.of(copyCmd, 1000, rows.iterator(), pgText)) }
                    histogram.recordValue(System.nanoTime() - start)
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    val medianWall = wallTimes.sorted()[BATCH_INSERT_MEASURED_ITERATIONS / 2]
    return BenchResult("Foundations+PgPipe (mutable COPY)", concurrency.toLong() * rowsPerTask, medianWall, histogram)
    } finally {
        closeQuietly("Foundations+PgPipe (mutable COPY)") { pool.close() }
    }
}

// ==================== Vert.x executeBatch ====================

fun runVertxBatch(
    pgConfig: PgConfig,
    batchSize: Int,
    concurrency: Int,
    rowsPerTask: Int,
): BenchResult {
    val rawVx = createRawVertxPool(PG_PORT, BATCH_INSERT_POOL_SIZE)
    try {
    val pool = rawVx.pool

    val sql = "INSERT INTO bench_write (name, value, active, created_at, category) VALUES ($1, $2, $3, $4, $5)"

    for (w in 0 until BATCH_INSERT_WARMUP_ITERATIONS) {
        pool.query("TRUNCATE bench_write").execute().toCompletionStage().toCompletableFuture().join()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val rows = generateRows(rowsPerTask)
                    for (chunk in rows.chunked(batchSize)) {
                        val tuples = chunk.map { r ->
                            VxTuple.of(r.name, r.value, r.active, r.createdAt, r.category)
                        }
                        pool.preparedQuery(sql).executeBatch(tuples)
                            .toCompletionStage().toCompletableFuture().join()
                    }
                }
            }
            futures.forEach(Future<*>::get)
        }
    }

    val histogram = ConcurrentHistogram(1, 60_000_000_000L, 3)
    val wallTimes = LongArray(BATCH_INSERT_MEASURED_ITERATIONS)

    for (iter in 0 until BATCH_INSERT_MEASURED_ITERATIONS) {
        pool.query("TRUNCATE bench_write").execute().toCompletionStage().toCompletableFuture().join()

        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    val rows = generateRows(rowsPerTask)
                    for (chunk in rows.chunked(batchSize)) {
                        val tuples = chunk.map { r ->
                            VxTuple.of(r.name, r.value, r.active, r.createdAt, r.category)
                        }
                        pool.preparedQuery(sql).executeBatch(tuples)
                            .toCompletionStage().toCompletableFuture().join()
                    }
                    histogram.recordValue(System.nanoTime() - start)
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    val medianWall = wallTimes.sorted()[BATCH_INSERT_MEASURED_ITERATIONS / 2]
    return BenchResult("Vert.x (batch)", concurrency.toLong() * rowsPerTask, medianWall, histogram)
    } finally {
        closeQuietly("Vert.x (batch)") { rawVx.close() }
    }
}

// ==================== Hibernate batch persist ====================

fun runHibernateBatch(
    pgConfig: PgConfig,
    batchSize: Int,
    concurrency: Int,
    rowsPerTask: Int,
): BenchResult {
    val sf = createHibernateSessionFactory(PG_PORT, BATCH_INSERT_POOL_SIZE)
    try {

    for (w in 0 until BATCH_INSERT_WARMUP_ITERATIONS) {
        sf.openSession().use { s -> s.beginTransaction(); s.createNativeMutationQuery("TRUNCATE bench_write").executeUpdate(); s.transaction.commit() }
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val rows = generateRows(rowsPerTask)
                    sf.openSession().use { session ->
                        session.beginTransaction()
                        var count = 0
                        for (row in rows) {
                            val entity = WriteRowEntity()
                            entity.name = row.name
                            entity.value = row.value
                            entity.active = row.active
                            entity.createdAt = row.createdAt
                            entity.category = row.category
                            session.persist(entity)
                            count++
                            if (count % batchSize == 0) {
                                session.flush()
                                session.clear()
                            }
                        }
                        session.transaction.commit()
                    }
                }
            }
            futures.forEach(Future<*>::get)
        }
    }

    val histogram = ConcurrentHistogram(1, 60_000_000_000L, 3)
    val wallTimes = LongArray(BATCH_INSERT_MEASURED_ITERATIONS)

    for (iter in 0 until BATCH_INSERT_MEASURED_ITERATIONS) {
        sf.openSession().use { s -> s.beginTransaction(); s.createNativeMutationQuery("TRUNCATE bench_write").executeUpdate(); s.transaction.commit() }

        val wallStart = System.nanoTime()
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (0 until concurrency).map {
                exec.submit<Any?> {
                    val start = System.nanoTime()
                    val rows = generateRows(rowsPerTask)
                    sf.openSession().use { session ->
                        session.beginTransaction()
                        var count = 0
                        for (row in rows) {
                            val entity = WriteRowEntity()
                            entity.name = row.name
                            entity.value = row.value
                            entity.active = row.active
                            entity.createdAt = row.createdAt
                            entity.category = row.category
                            session.persist(entity)
                            count++
                            if (count % batchSize == 0) {
                                session.flush()
                                session.clear()
                            }
                        }
                        session.transaction.commit()
                    }
                    histogram.recordValue(System.nanoTime() - start)
                }
            }
            futures.forEach(Future<*>::get)
        }
        wallTimes[iter] = (System.nanoTime() - wallStart) / 1_000_000
    }

    val medianWall = wallTimes.sorted()[BATCH_INSERT_MEASURED_ITERATIONS / 2]
    return BenchResult("Hibernate (batch)", concurrency.toLong() * rowsPerTask, medianWall, histogram)
    } finally {
        closeQuietly("Hibernate (batch)") { sf.close() }
    }
}
