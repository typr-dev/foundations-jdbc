package dev.typr.foundations.benchmark

// ==================== PostgreSQL Connection ====================

const val PG_HOST = "localhost"
const val PG_PORT = 6432
const val PG_DB = "world"
const val PG_USER = "postgres"
const val PG_PASS = "password"

const val ROW_COUNT = 10_000

// ==================== Benchmark Profiles ====================

data class BenchParams(
    val poolSize: Int,
    val taskCount: Int,
    val warmupOps: Int,
    val measuredIterations: Int,
)

val THROUGHPUT_PARAMS = BenchParams(
    poolSize = 50,
    taskCount = 10_000,
    warmupOps = 5_000,
    measuredIterations = 5,
)

val TX_PARAMS = BenchParams(
    poolSize = 10,
    taskCount = 5_000,
    warmupOps = 1_000,
    measuredIterations = 5,
)

val LATENCY_PARAMS = BenchParams(
    poolSize = 10,
    taskCount = 1_000,
    warmupOps = 300,
    measuredIterations = 3,
)

val BATCH_INSERT_PARAMS = BenchParams(
    poolSize = 10,
    taskCount = 10_000,
    warmupOps = 3,
    measuredIterations = 5,
)

val STREAMING_PARAMS = BenchParams(
    poolSize = 10,
    taskCount = 10_000,
    warmupOps = 1,
    measuredIterations = 3,
)
