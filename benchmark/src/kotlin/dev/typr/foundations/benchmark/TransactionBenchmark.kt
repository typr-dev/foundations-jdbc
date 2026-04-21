package dev.typr.foundations.benchmark

import dev.typr.foundations.Fragment
import dev.typr.foundations.Transactor
import dev.typr.foundations.connect.PgConfig
import dev.typr.foundations.hikari.HikariDataSourceFactory
import dev.typr.foundations.hikari.PoolConfig
import dev.typr.foundations.pg.PgPipelinePool
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.Tuple as VxTuple
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future

// ==================== Configuration ====================

const val TX_POOL_SIZE = 10
const val TX_TASK_COUNT = 5_000
const val TX_WARMUP_OPS = 1_000
const val TX_MEASURED_ITERATIONS = 5

const val TX_OPS_PER_TRANSACTION = 5

// ==================== Main ====================

fun main() = runTransactionBenchmark()

fun runTransactionBenchmark() {
    println("=== Transaction Benchmark: PgPipeline vs Vert.x vs HikariCP vs Hibernate ===")
    println("PostgreSQL at $PG_HOST:$PG_PORT/$PG_DB")
    println("Pool size: $TX_POOL_SIZE connections")
    println("Concurrency: $TX_TASK_COUNT concurrent tasks")
    println("Warmup: $TX_WARMUP_OPS ops, Measured: $TX_TASK_COUNT ops x $TX_MEASURED_ITERATIONS iterations")
    println()

    setupTransactionBenchmarkDatabase()

    val selectByIdTemplate = selectByIdTemplate()
    val insertTemplate = insertTemplate()

    val rawSelectSql = RAW_SELECT_SQL
    val rawInsertSql = RAW_INSERT_SQL

    val baselineResults = mutableListOf<BenchResult>()
    val singleTxResults = mutableListOf<BenchResult>()
    val multiOpResults = mutableListOf<BenchResult>()
    val realisticResults = mutableListOf<BenchResult>()
    val multiReadResults = mutableListOf<BenchResult>()

    // ========== Setup all pools (with guaranteed cleanup) ==========

    val pipePool = createPipelinePoolWithTx(PG_PORT, TX_POOL_SIZE, TX_POOL_SIZE - 1)
    val rawVx = createRawVertxPool(PG_PORT, TX_POOL_SIZE)
    val hikariDs = createHikariPool(PG_PORT, TX_POOL_SIZE)
    val sf = createHibernateSessionFactory(PG_PORT, TX_POOL_SIZE)

    try {
    val vxPool = rawVx.pool
    val hikariTx = hikariDs.transactor()

    warmupPool(pipePool, selectByIdTemplate)
    warmupRawVertx(vxPool, VX_SELECT_SQL)
    warmupHikari(hikariTx, selectByIdTemplate)
    warmupHibernateTx(sf)

    // ========== 1. Non-transactional point reads ==========
    println("=" .repeat(70))
    println("=== 1. Non-Transactional Point Reads ===")
    println("=" .repeat(70))

    println("  Raw JDBC...")
    baselineResults.add(runTimedTxBenchmark("Raw JDBC", TX_TASK_COUNT) { i ->
        hikariDs.connection.use { conn ->
            conn.prepareStatement(rawSelectSql).use { ps ->
                ps.setInt(1, (i % ROW_COUNT) + 1)
                ps.executeQuery().use { rs ->
                    if (rs.next()) readBenchRow(rs) else null
                }
            }
        }
    })

    println("  Foundations+HikariCP...")
    baselineResults.add(runTimedTxBenchmark("Foundations+Hikari", TX_TASK_COUNT) { i ->
        hikariTx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
    })

    println("  PgPipeline (readonly)...")
    baselineResults.add(runTimedTxBenchmark("Foundations+PgPipe (readonly)", TX_TASK_COUNT) { i ->
        pipePool.transactRead { conn -> conn.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) }
    })

    println("  Vert.x (async)...")
    baselineResults.add(runAsyncBenchmark("Vert.x (async)", TX_TASK_COUNT, TX_MEASURED_ITERATIONS) { i ->
        vxPool.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of((i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    })

    println("  Hibernate...")
    baselineResults.add(runTimedTxBenchmark("Hibernate", TX_TASK_COUNT) { i ->
        sf.openSession().use { session ->
            session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
        }
    })

    printResults("Non-Transactional Point Reads", baselineResults, "ops/sec")

    // ========== 2. Single-read transactions ==========
    println()
    println("=" .repeat(70))
    println("=== 2. Transaction: Single Read (BEGIN + read + COMMIT) ===")
    println("=" .repeat(70))

    println("  Raw JDBC manual tx...")
    singleTxResults.add(runTimedTxBenchmark("Raw JDBC (tx)", TX_TASK_COUNT) { i ->
        hikariDs.connection.use { conn ->
            conn.autoCommit = false
            try {
                conn.prepareStatement(rawSelectSql).use { ps ->
                    ps.setInt(1, (i % ROW_COUNT) + 1)
                    ps.executeQuery().use { rs ->
                        val result = if (rs.next()) readBenchRow(rs) else null
                        conn.commit()
                        result
                    }
                }
            } catch (e: Exception) {
                conn.rollback()
                throw e
            }
        }
    })

    println("  Fnd+Hikari tx...")
    singleTxResults.add(runTimedTxBenchmark("Foundations+Hikari (tx)", TX_TASK_COUNT) { i ->
        hikariDs.connection.use { conn ->
            conn.autoCommit = false
            try {
                conn.prepareStatement(rawSelectSql).use { ps ->
                    ps.setInt(1, (i % ROW_COUNT) + 1)
                    ps.executeQuery().use { rs ->
                        val result = if (rs.next()) readBenchRow(rs) else null
                        conn.commit()
                        result
                    }
                }
            } catch (e: Exception) {
                conn.rollback()
                throw e
            }
        }
    })

    println("  PgPipeline transact...")
    singleTxResults.add(runTimedTxBenchmark("Foundations+PgPipe (tx)", TX_TASK_COUNT) { i ->
        pipePool.transact { tx ->
            tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
        }
    })

    println("  Vert.x (async) withTransaction...")
    singleTxResults.add(runAsyncBenchmark("Vert.x (tx)", TX_TASK_COUNT, TX_MEASURED_ITERATIONS) { i ->
        vxPool.withTransaction { conn ->
            conn.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of((i % ROW_COUNT) + 1))
        }.toCompletionStage().toCompletableFuture()
    })

    println("  Hibernate tx...")
    singleTxResults.add(runTimedTxBenchmark("Hibernate (tx)", TX_TASK_COUNT) { i ->
        sf.openSession().use { session ->
            session.beginTransaction()
            val result = session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
            session.transaction.commit()
            result
        }
    })

    printResults("Transaction: Single Read", singleTxResults, "ops/sec")

    // ========== 3. Multi-op transaction (read-write-read-write-read) ==========
    println()
    println("=" .repeat(70))
    println("=== 3. Transaction: $TX_OPS_PER_TRANSACTION Ops (read-write-read-write-read) ===")
    println("=" .repeat(70))

    hikariDs.connection.use { c -> c.createStatement().use { it.execute("TRUNCATE bench_write") } }
    println("  Raw JDBC manual tx...")
    multiOpResults.add(runTimedTxBenchmark("Raw JDBC (tx)", TX_TASK_COUNT) { i ->
        hikariDs.connection.use { conn ->
            conn.autoCommit = false
            try {
                val selPs = conn.prepareStatement(rawSelectSql)
                val insPs = conn.prepareStatement(rawInsertSql)

                selPs.setInt(1, (i % ROW_COUNT) + 1)
                selPs.executeQuery().use { it.next() }

                insPs.setString(1, "tx-$i-1"); insPs.setBigDecimal(2, BigDecimal("${i % 1000}.99"))
                insPs.setBoolean(3, true); insPs.setObject(4, LocalDateTime.now()); insPs.setString(5, "Bench")
                insPs.executeUpdate()

                selPs.setInt(1, ((i + 1) % ROW_COUNT) + 1)
                selPs.executeQuery().use { it.next() }

                insPs.setString(1, "tx-$i-2"); insPs.setBigDecimal(2, BigDecimal("${i % 1000}.50"))
                insPs.setBoolean(3, false); insPs.setObject(4, LocalDateTime.now()); insPs.setString(5, "Bench")
                insPs.executeUpdate()

                selPs.setInt(1, ((i + 2) % ROW_COUNT) + 1)
                selPs.executeQuery().use { it.next() }

                selPs.close(); insPs.close()
                conn.commit()
            } catch (e: Exception) {
                conn.rollback()
                throw e
            }
        }
    })

    pipePool.update(Fragment.of("TRUNCATE bench_write"))
    println("  PgPipeline transact...")
    multiOpResults.add(runTimedTxBenchmark("Foundations+PgPipe (tx)", TX_TASK_COUNT) { i ->
        pipePool.transact { tx ->
            tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
            tx.execute(insertTemplate.on("tx-$i-1", BigDecimal("${i % 1000}.99"), true, LocalDateTime.now(), "Bench"))
            tx.execute(selectByIdTemplate.on(((i + 1) % ROW_COUNT) + 1))
            tx.execute(insertTemplate.on("tx-$i-2", BigDecimal("${i % 1000}.50"), false, LocalDateTime.now(), "Bench"))
            tx.execute(selectByIdTemplate.on(((i + 2) % ROW_COUNT) + 1))
        }
    })

    vxPool.query("TRUNCATE bench_write").execute().toCompletionStage().toCompletableFuture().join()
    println("  Vert.x (async) withTransaction...")
    multiOpResults.add(runAsyncBenchmark("Vert.x (tx)", TX_TASK_COUNT, TX_MEASURED_ITERATIONS) { i ->
        val id1 = (i % ROW_COUNT) + 1
        val id2 = ((i + 1) % ROW_COUNT) + 1
        val id3 = ((i + 2) % ROW_COUNT) + 1
        vxPool.withTransaction { conn ->
            conn.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(id1))
                .compose { conn.preparedQuery(VX_INSERT_SQL).execute(VxTuple.of("tx-$i-1", BigDecimal("${i % 1000}.99"), true, LocalDateTime.now(), "Bench")) }
                .compose { conn.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(id2)) }
                .compose { conn.preparedQuery(VX_INSERT_SQL).execute(VxTuple.of("tx-$i-2", BigDecimal("${i % 1000}.50"), false, LocalDateTime.now(), "Bench")) }
                .compose { conn.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(id3)) }
        }.toCompletionStage().toCompletableFuture()
    })

    sf.openSession().use { s -> s.beginTransaction(); s.createNativeMutationQuery("TRUNCATE bench_write").executeUpdate(); s.transaction.commit() }
    println("  Hibernate tx...")
    multiOpResults.add(runTimedTxBenchmark("Hibernate (tx)", TX_TASK_COUNT) { i ->
        sf.openSession().use { session ->
            session.beginTransaction()
            session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
            val e1 = WriteRowEntity(); e1.name = "tx-$i-1"; e1.value = BigDecimal("${i % 1000}.99"); e1.active = true; e1.createdAt = LocalDateTime.now(); e1.category = "Bench"
            session.persist(e1)
            session.find(BenchRowEntity::class.java, ((i + 1) % ROW_COUNT) + 1)
            val e2 = WriteRowEntity(); e2.name = "tx-$i-2"; e2.value = BigDecimal("${i % 1000}.50"); e2.active = false; e2.createdAt = LocalDateTime.now(); e2.category = "Bench"
            session.persist(e2)
            session.find(BenchRowEntity::class.java, ((i + 2) % ROW_COUNT) + 1)
            session.transaction.commit()
        }
    })

    printResults("Transaction: $TX_OPS_PER_TRANSACTION Ops (r-w-r-w-r)", multiOpResults, "ops/sec")

    // ========== 4. Realistic mixed: 80% non-tx reads + 20% tx read-write ==========
    println()
    println("=" .repeat(70))
    println("=== 4. Realistic Mixed: 80% Non-Tx Reads + 20% Tx Read-Write ===")
    println("=" .repeat(70))

    hikariDs.connection.use { c -> c.createStatement().use { it.execute("TRUNCATE bench_write") } }
    println("  Raw JDBC...")
    realisticResults.add(runTimedTxBenchmark("Raw JDBC", TX_TASK_COUNT) { i ->
        if (i % 5 == 0) {
            hikariDs.connection.use { conn ->
                conn.autoCommit = false
                try {
                    conn.prepareStatement(rawSelectSql).use { ps ->
                        ps.setInt(1, (i % ROW_COUNT) + 1)
                        ps.executeQuery().use { it.next() }
                    }
                    conn.prepareStatement(rawInsertSql).use { ps ->
                        ps.setString(1, "item-$i"); ps.setBigDecimal(2, BigDecimal("${i % 1000}.99"))
                        ps.setBoolean(3, i % 3 != 0); ps.setObject(4, LocalDateTime.now()); ps.setString(5, "Bench")
                        ps.executeUpdate()
                    }
                    conn.commit()
                } catch (e: Exception) {
                    conn.rollback()
                    throw e
                }
            }
        } else {
            hikariDs.connection.use { conn ->
                conn.prepareStatement(rawSelectSql).use { ps ->
                    ps.setInt(1, (i % ROW_COUNT) + 1)
                    ps.executeQuery().use { rs ->
                        if (rs.next()) readBenchRow(rs) else null
                    }
                }
            }
        }
    })

    pipePool.update(Fragment.of("TRUNCATE bench_write"))
    println("  PgPipeline (mixed)...")
    realisticResults.add(runTimedTxBenchmark("Foundations+PgPipe (mixed)", TX_TASK_COUNT) { i ->
        if (i % 5 == 0) {
            pipePool.transact { tx ->
                tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
                tx.execute(insertTemplate.on("item-$i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench"))
            }
        } else {
            pipePool.transactRead { conn -> conn.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1)) }
        }
    })

    vxPool.query("TRUNCATE bench_write").execute().toCompletionStage().toCompletableFuture().join()
    println("  Vert.x (async)...")
    realisticResults.add(runAsyncBenchmark("Vert.x (async)", TX_TASK_COUNT, TX_MEASURED_ITERATIONS) { i ->
        val id = (i % ROW_COUNT) + 1
        if (i % 5 == 0) {
            vxPool.withTransaction { conn ->
                conn.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(id))
                    .compose { conn.preparedQuery(VX_INSERT_SQL).execute(VxTuple.of("item-$i", BigDecimal("${i % 1000}.99"), i % 3 != 0, LocalDateTime.now(), "Bench")) }
            }.toCompletionStage().toCompletableFuture()
        } else {
            vxPool.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(id))
                .toCompletionStage().toCompletableFuture()
        }
    })

    sf.openSession().use { s -> s.beginTransaction(); s.createNativeMutationQuery("TRUNCATE bench_write").executeUpdate(); s.transaction.commit() }
    println("  Hibernate...")
    realisticResults.add(runTimedTxBenchmark("Hibernate", TX_TASK_COUNT) { i ->
        if (i % 5 == 0) {
            sf.openSession().use { session ->
                session.beginTransaction()
                session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
                val entity = WriteRowEntity(); entity.name = "item-$i"; entity.value = BigDecimal("${i % 1000}.99"); entity.active = i % 3 != 0; entity.createdAt = LocalDateTime.now(); entity.category = "Bench"
                session.persist(entity)
                session.transaction.commit()
            }
        } else {
            sf.openSession().use { session ->
                session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
            }
        }
    })

    printResults("Realistic Mixed: 80% reads + 20% tx writes", realisticResults, "ops/sec")

    // ========== 5. Non-tx multi-op (5 reads) -- pipelining advantage ==========
    println()
    println("=" .repeat(70))
    println("=== 5. Non-Transactional: 5 Sequential Reads (pipelining advantage) ===")
    println("=" .repeat(70))

    println("  Raw JDBC...")
    multiReadResults.add(runTimedTxBenchmark("Raw JDBC", TX_TASK_COUNT) { i ->
        for (j in 0 until 5) {
            hikariDs.connection.use { conn ->
                conn.prepareStatement(rawSelectSql).use { ps ->
                    ps.setInt(1, ((i + j) % ROW_COUNT) + 1)
                    ps.executeQuery().use { rs ->
                        if (rs.next()) readBenchRow(rs) else null
                    }
                }
            }
        }
    })

    println("  Foundations+HikariCP...")
    multiReadResults.add(runTimedTxBenchmark("Foundations+Hikari", TX_TASK_COUNT) { i ->
        hikariTx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
        hikariTx.execute(selectByIdTemplate.on(((i + 1) % ROW_COUNT) + 1))
        hikariTx.execute(selectByIdTemplate.on(((i + 2) % ROW_COUNT) + 1))
        hikariTx.execute(selectByIdTemplate.on(((i + 3) % ROW_COUNT) + 1))
        hikariTx.execute(selectByIdTemplate.on(((i + 4) % ROW_COUNT) + 1))
    })

    println("  PgPipeline (readonly)...")
    multiReadResults.add(runTimedTxBenchmark("Foundations+PgPipe (readonly)", TX_TASK_COUNT) { i ->
        pipePool.transactRead { conn ->
            conn.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
            conn.execute(selectByIdTemplate.on(((i + 1) % ROW_COUNT) + 1))
            conn.execute(selectByIdTemplate.on(((i + 2) % ROW_COUNT) + 1))
            conn.execute(selectByIdTemplate.on(((i + 3) % ROW_COUNT) + 1))
            conn.execute(selectByIdTemplate.on(((i + 4) % ROW_COUNT) + 1))
        }
    })

    println("  Vert.x (async)...")
    multiReadResults.add(runAsyncBenchmark("Vert.x (async)", TX_TASK_COUNT, TX_MEASURED_ITERATIONS) { i ->
        vxPool.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of((i % ROW_COUNT) + 1))
            .compose { vxPool.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(((i + 1) % ROW_COUNT) + 1)) }
            .compose { vxPool.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(((i + 2) % ROW_COUNT) + 1)) }
            .compose { vxPool.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(((i + 3) % ROW_COUNT) + 1)) }
            .compose { vxPool.preparedQuery(VX_SELECT_SQL).execute(VxTuple.of(((i + 4) % ROW_COUNT) + 1)) }
            .toCompletionStage().toCompletableFuture()
    })

    println("  Hibernate...")
    multiReadResults.add(runTimedTxBenchmark("Hibernate", TX_TASK_COUNT) { i ->
        for (j in 0 until 5) {
            sf.openSession().use { session ->
                session.find(BenchRowEntity::class.java, ((i + j) % ROW_COUNT) + 1)
            }
        }
    })

    printResults("Non-Transactional: 5 Sequential Reads", multiReadResults, "ops/sec")

    } finally {
        closeQuietly("PgPipelinePool") { pipePool.close() }
        closeQuietly("Vert.x") { rawVx.close() }
        closeQuietly("HikariCP") { hikariDs.close() }
        closeQuietly("Hibernate") { sf.close() }
    }

    // ========== 6. Latency test: single-read tx at 2ms and 10ms RTT ==========
    val latTaskCount = 500

    for (latencyMs in listOf(1L, 5L)) {
        val rtt = latencyMs * 2
        println()
        println("=" .repeat(70))
        println("=== 6. Single-Read Transaction @ ${rtt}ms RTT ($latTaskCount ops, $TX_POOL_SIZE conns) ===")
        println("=" .repeat(70))

        proxiedPort(latencyMs, 16432).use { proxied ->
            val latPipePool = createPipelinePoolWithTx(proxied.port, TX_POOL_SIZE, TX_POOL_SIZE - 1)
            try {
                warmupPool(latPipePool, selectByIdTemplate)

                val latResults = mutableListOf<BenchResult>()

                println("  PgPipeline transact...")
                latResults.add(runTimedTxBenchmark("Foundations+PgPipe (tx)", latTaskCount) { i ->
                    latPipePool.transact { tx ->
                        tx.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
                    }
                })

                val latHikariDs = createHikariPool(proxied.port, TX_POOL_SIZE)
                try {
                    warmupHikari(latHikariDs.transactor(), selectByIdTemplate)

                    println("  Raw JDBC manual tx...")
                    latResults.add(runTimedTxBenchmark("Raw JDBC (tx)", latTaskCount) { i ->
                        latHikariDs.connection.use { conn ->
                            conn.autoCommit = false
                            try {
                                conn.prepareStatement(rawSelectSql).use { ps ->
                                    ps.setInt(1, (i % ROW_COUNT) + 1)
                                    ps.executeQuery().use { rs ->
                                        val result = if (rs.next()) readBenchRow(rs) else null
                                        conn.commit()
                                        result
                                    }
                                }
                            } catch (e: Exception) {
                                conn.rollback()
                                throw e
                            }
                        }
                    })

                    printResults("Single-Read Tx @ ${rtt}ms RTT", latResults, "ops/sec")
                } finally {
                    closeQuietly("latency HikariCP") { latHikariDs.close() }
                }
            } finally {
                closeQuietly("latency PgPipelinePool") { latPipePool.close() }
            }
        }
    }

    // ========== 7. ReadonlyTransact vs Transact ==========
    println()
    println("=" .repeat(70))
    println("=== 7. ReadonlyTransact vs Transact: Single Read ($TX_TASK_COUNT tasks, $TX_POOL_SIZE conns) ===")
    println("=" .repeat(70))

    val readonlyResults = mutableListOf<BenchResult>()

    println("  Foundations+PgPipe transactRead...")
    readonlyResults.add(runTimedTxBenchmark("Foundations+PgPipe (readonly)", TX_TASK_COUNT) { i ->
        pipePool.transactRead { conn ->
            conn.queryFirst(
                Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
                    .append(Fragment.value((i % ROW_COUNT) + 1, dev.typr.foundations.PgTypes.int4)),
                benchReadCodec()
            )
        }
    })

    println("  Foundations+PgPipe transact (mutable)...")
    readonlyResults.add(runTimedTxBenchmark("Foundations+PgPipe (mutable)", TX_TASK_COUNT) { i ->
        pipePool.transact { conn ->
            conn.execute(selectByIdTemplate.on((i % ROW_COUNT) + 1))
        }
    })

    println("  Raw JDBC...")
    readonlyResults.add(runTimedTxBenchmark("Raw JDBC", TX_TASK_COUNT) { i ->
        hikariDs.connection.use { conn ->
            conn.prepareStatement(rawSelectSql).use { ps ->
                ps.setInt(1, (i % ROW_COUNT) + 1)
                ps.executeQuery().use { rs ->
                    if (rs.next()) readBenchRow(rs) else null
                }
            }
        }
    })

    println("  Hibernate...")
    readonlyResults.add(runTimedTxBenchmark("Hibernate", TX_TASK_COUNT) { i ->
        sf.openSession().use { session ->
            session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
        }
    })

    printResults("ReadonlyTransact vs Transact", readonlyResults, "ops/sec")

    // ========== 8. ReadonlyTransact: combine() vs sequential (5 queries) ==========
    println()
    println("=" .repeat(70))
    println("=== 8. Readonly combine() vs sequential: 5 queries ($TX_TASK_COUNT tasks, $TX_POOL_SIZE conns) ===")
    println("=" .repeat(70))

    val readonlyCombineResults = mutableListOf<BenchResult>()

    println("  transactRead + combine (5 queries)...")
    readonlyCombineResults.add(runTimedTxBenchmark("readonly + combine", TX_TASK_COUNT) { i ->
        pipePool.transactRead { conn ->
            @Suppress("UNCHECKED_CAST")
            val ops = (0 until 5).map { j ->
                selectByIdTemplate.on(((i + j) % ROW_COUNT) + 1)
            }
            val combined = ops.drop(1).fold(ops.first() as dev.typr.foundations.Operation<*>) { acc, op ->
                acc.combine(op)
            }
            conn.execute(combined)
        }
    })

    println("  transactRead + sequential queryFirst (5 calls)...")
    readonlyCombineResults.add(runTimedTxBenchmark("readonly + sequential", TX_TASK_COUNT) { i ->
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

    println("  transact + combine (5 queries, with tx)...")
    readonlyCombineResults.add(runTimedTxBenchmark("mutable + combine", TX_TASK_COUNT) { i ->
        pipePool.transact { conn ->
            @Suppress("UNCHECKED_CAST")
            val ops = (0 until 5).map { j ->
                selectByIdTemplate.on(((i + j) % ROW_COUNT) + 1)
            }
            val combined = ops.drop(1).fold(ops.first() as dev.typr.foundations.Operation<*>) { acc, op ->
                acc.combine(op)
            }
            conn.execute(combined)
        }
    })

    println("  transact + sequential (5 queries, with tx)...")
    readonlyCombineResults.add(runTimedTxBenchmark("mutable + sequential", TX_TASK_COUNT) { i ->
        pipePool.transact { conn ->
            for (j in 0 until 5) {
                conn.execute(selectByIdTemplate.on(((i + j) % ROW_COUNT) + 1))
            }
        }
    })

    println("  Raw JDBC (5 queries, no tx)...")
    readonlyCombineResults.add(runTimedTxBenchmark("Raw JDBC (5 queries)", TX_TASK_COUNT) { i ->
        hikariDs.connection.use { conn ->
            for (j in 0 until 5) {
                conn.prepareStatement(rawSelectSql).use { ps ->
                    ps.setInt(1, ((i + j) % ROW_COUNT) + 1)
                    ps.executeQuery().use { rs -> rs.next() }
                }
            }
        }
    })

    printResults("Readonly combine() vs sequential (5 queries, local)", readonlyCombineResults, "ops/sec")

    // ========== 9. Same as 8 but with 10ms RTT ==========
    println()
    println("=" .repeat(70))
    println("=== 9. Readonly combine() vs sequential: 5 queries @ 10ms RTT ($TX_TASK_COUNT tasks, $TX_POOL_SIZE conns) ===")
    println("=" .repeat(70))

    val readonlyCombineLatencyResults = mutableListOf<BenchResult>()

    proxiedPort(5L, 16432).use { proxied ->
        val latPipePool = createPipelinePoolWithTx(proxied.port, TX_POOL_SIZE, TX_POOL_SIZE - 1)
        val latSelectTpl = selectByIdTemplate()
        warmupPool(latPipePool, latSelectTpl)

        val latHikariDs = createHikariPool(proxied.port, TX_POOL_SIZE)
        warmupHikari(latHikariDs.transactor(), latSelectTpl)

        println("  transactRead + combine (5 queries, 10ms RTT)...")
        readonlyCombineLatencyResults.add(runTimedTxBenchmark("readonly + combine", TX_TASK_COUNT) { i ->
            latPipePool.transactRead { conn ->
                @Suppress("UNCHECKED_CAST")
                val ops = (0 until 5).map { j ->
                    latSelectTpl.on(((i + j) % ROW_COUNT) + 1)
                }
                val combined = ops.drop(1).fold(ops.first() as dev.typr.foundations.Operation<*>) { acc, op ->
                    acc.combine(op)
                }
                conn.execute(combined)
            }
        })

        println("  transactRead + sequential (5 queries, 10ms RTT)...")
        readonlyCombineLatencyResults.add(runTimedTxBenchmark("readonly + sequential", TX_TASK_COUNT) { i ->
            latPipePool.transactRead { conn ->
                for (j in 0 until 5) {
                    conn.queryFirst(
                        Fragment.of("SELECT id, name, value, active, created_at, category FROM bench_read WHERE id = ")
                            .append(Fragment.value(((i + j) % ROW_COUNT) + 1, dev.typr.foundations.PgTypes.int4)),
                        benchReadCodec()
                    )
                }
            }
        })

        println("  transact + combine (5 queries, 10ms RTT)...")
        readonlyCombineLatencyResults.add(runTimedTxBenchmark("mutable + combine", TX_TASK_COUNT) { i ->
            latPipePool.transact { conn ->
                @Suppress("UNCHECKED_CAST")
                val ops = (0 until 5).map { j ->
                    latSelectTpl.on(((i + j) % ROW_COUNT) + 1)
                }
                val combined = ops.drop(1).fold(ops.first() as dev.typr.foundations.Operation<*>) { acc, op ->
                    acc.combine(op)
                }
                conn.execute(combined)
            }
        })

        println("  transact + sequential (5 queries, 10ms RTT)...")
        readonlyCombineLatencyResults.add(runTimedTxBenchmark("mutable + sequential", TX_TASK_COUNT) { i ->
            latPipePool.transact { conn ->
                for (j in 0 until 5) {
                    conn.execute(latSelectTpl.on(((i + j) % ROW_COUNT) + 1))
                }
            }
        })

        println("  Raw JDBC (5 queries, 10ms RTT)...")
        readonlyCombineLatencyResults.add(runTimedTxBenchmark("Raw JDBC (5 queries)", TX_TASK_COUNT) { i ->
            latHikariDs.connection.use { conn ->
                for (j in 0 until 5) {
                    conn.prepareStatement(rawSelectSql).use { ps ->
                        ps.setInt(1, ((i + j) % ROW_COUNT) + 1)
                        ps.executeQuery().use { rs -> rs.next() }
                    }
                }
            }
        })

        printResults("Readonly combine() vs sequential (5 queries, 10ms RTT)", readonlyCombineLatencyResults, "ops/sec")

        latPipePool.close()
        latHikariDs.close()
    }

    val report = MarkdownReport("Transaction Benchmark")
    report.section("Non-Transactional Point Reads", "$TX_TASK_COUNT concurrent reads, $TX_POOL_SIZE connections.", baselineResults, "ops/sec",
        """
        pool.execute(selectById.on(42))
        """.trimIndent()
    )
    report.section("Single-Read Transaction", "$TX_TASK_COUNT concurrent single-read transactions, $TX_POOL_SIZE connections.", singleTxResults, "ops/sec",
        """
        pool.transact(tx -> tx.execute(selectById.on(42)))
        """.trimIndent()
    )
    report.section("Multi-Op Transaction ($TX_OPS_PER_TRANSACTION ops)", "$TX_TASK_COUNT concurrent r-w-r-w-r transactions, $TX_POOL_SIZE connections.", multiOpResults, "ops/sec",
        """
        pool.transact(tx -> {
            tx.execute(selectById.on(1));
            tx.execute(insertRow.on("Widget", price, true, now, "Cat"));
            tx.execute(selectById.on(2));
            tx.execute(insertRow.on("Gadget", price, false, now, "Cat"));
            tx.execute(selectById.on(3));
        });
        """.trimIndent()
    )
    report.section("Realistic Mixed (80/20)", "$TX_TASK_COUNT concurrent ops, 80% non-tx reads + 20% tx writes, $TX_POOL_SIZE connections.", realisticResults, "ops/sec",
        """
        pool.execute(selectById.on(42))                          // 80%
        pool.transact(tx -> {                                    // 20%
            tx.execute(selectById.on(42));
            tx.execute(insertRow.on("Widget", price, true, now, "Cat"));
        });
        """.trimIndent()
    )
    report.section("5 Sequential Reads", "$TX_TASK_COUNT concurrent tasks, 5 reads per task, $TX_POOL_SIZE connections.", multiReadResults, "ops/sec",
        """
        for (int i = 0; i < 5; i++)
            pool.execute(selectById.on(i));
        """.trimIndent()
    )
    report.section("ReadonlyTransact vs Transact",
        "$TX_TASK_COUNT concurrent single-read tasks, $TX_POOL_SIZE connections. Compares PgPipe readonly path (no BEGIN/COMMIT) vs mutable path (with tx).",
        readonlyResults, "ops/sec",
        """
        // transactRead: no BEGIN/COMMIT, any connection
        pool.transactRead(conn ->
            conn.queryFirst(sql, codec)
        );

        // transact: BEGIN + query + COMMIT on reserved connection
        pool.transact(conn ->
            conn.execute(selectById.on(42))
        );
        """.trimIndent()
    )
    report.section("Readonly combine() vs sequential (5 queries, local)",
        "$TX_TASK_COUNT concurrent tasks, each running 5 queries. $TX_POOL_SIZE connections.",
        readonlyCombineResults, "ops/sec",
        """
        // readonly + combine: 5 queries fan out across all connections
        pool.transactRead(conn -> conn.execute(
            q1.combine(q2).combine(q3).combine(q4).combine(q5)
        ));

        // readonly + sequential: 5 queries, one at a time, any connection
        pool.transactRead(conn -> {
            conn.queryFirst(sql1, codec);
            conn.queryFirst(sql2, codec);
            // ...
        });

        // mutable + combine: 5 queries combined, on reserved connection
        pool.transact(conn -> conn.execute(
            q1.combine(q2).combine(q3).combine(q4).combine(q5)
        ));
        """.trimIndent()
    )
    report.section("Readonly combine() vs sequential (5 queries, 10ms RTT)",
        "$TX_TASK_COUNT concurrent tasks, each running 5 queries through 10ms RTT proxy. $TX_POOL_SIZE connections.",
        readonlyCombineLatencyResults, "ops/sec",
        """
        // readonly + combine @ 10ms RTT: 5 queries across all connections, ~1 RTT
        pool.transactRead(conn -> conn.execute(
            q1.combine(q2).combine(q3).combine(q4).combine(q5)
        ));

        // readonly + sequential @ 10ms RTT: 5 queries, 5 RTTs
        pool.transactRead(conn -> {
            conn.queryFirst(sql1, codec);  // 10ms
            conn.queryFirst(sql2, codec);  // 10ms
            // ... total ~50ms
        });
        """.trimIndent()
    )
    report.writeTo(REPORT_DIR, "transactions.md")
}

// ==================== Pool warmup helpers ====================

private fun warmupPool(pool: PgPipelinePool, tpl: dev.typr.foundations.Template.Query1<Int, java.util.Optional<BenchRow>>) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..TX_WARMUP_OPS).map { i -> exec.submit<Any?> { pool.execute(tpl.on((i % ROW_COUNT) + 1)) } }
            .forEach(Future<*>::get)
    }
}

private fun warmupRawVertx(pool: PgPool, sql: String) {
    for (i in 1..TX_POOL_SIZE) {
        pool.query("SELECT 1").execute().toCompletionStage().toCompletableFuture().join()
    }
    val warmupFutures = (1..TX_WARMUP_OPS).map { i ->
        pool.preparedQuery(sql).execute(VxTuple.of((i % ROW_COUNT) + 1))
            .toCompletionStage().toCompletableFuture()
    }
    CompletableFuture.allOf(*warmupFutures.toTypedArray()).join()
}

private fun warmupHikari(tx: Transactor, tpl: dev.typr.foundations.Template.Query1<Int, java.util.Optional<BenchRow>>) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..TX_WARMUP_OPS).map { i -> exec.submit<Any?> { tx.execute(tpl.on((i % ROW_COUNT) + 1)) } }
            .forEach(Future<*>::get)
    }
}

private fun warmupHibernateTx(sf: org.hibernate.SessionFactory) {
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..TX_WARMUP_OPS).map { i -> exec.submit<Any?> {
            sf.openSession().use { session ->
                session.find(BenchRowEntity::class.java, (i % ROW_COUNT) + 1)
            }
        }}.forEach(Future<*>::get)
    }
}

// ==================== Database setup ====================

fun setupTransactionBenchmarkDatabase() {
    val pgConfig = PgConfig.builder(PG_HOST, PG_PORT, PG_DB, PG_USER, PG_PASS).build()
    val ds = HikariDataSourceFactory.create(pgConfig, PoolConfig.builder().maximumPoolSize(2).minimumIdle(2).build())

    ds.connection.use { conn ->
        conn.createStatement().use { stmt ->
            val rs = conn.prepareStatement("SELECT count(*) FROM information_schema.tables WHERE table_name = 'bench_read'").executeQuery()
            rs.next()
            val exists = rs.getInt(1) > 0
            rs.close()

            if (!exists) {
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
            } else {
                println("bench_read already exists, skipping seed")
            }

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

    ds.close()
}

// ==================== Benchmark harness ====================

private fun runTimedTxBenchmark(name: String, taskCount: Int, op: (Int) -> Any?): BenchResult {
    return runBenchmark(name, taskCount, TX_WARMUP_OPS, TX_MEASURED_ITERATIONS, op)
}
