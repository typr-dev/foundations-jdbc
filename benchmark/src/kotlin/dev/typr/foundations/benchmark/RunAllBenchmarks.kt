package dev.typr.foundations.benchmark

fun main() {
    val benchmarks = listOf(
        "Throughput" to "dev.typr.foundations.benchmark.ThroughputBenchmarkKt",
        "Pipelining Advantage" to "dev.typr.foundations.benchmark.PipeliningAdvantageBenchmarkKt",
        "Transactions" to "dev.typr.foundations.benchmark.TransactionBenchmarkKt",
        "Batch Insert" to "dev.typr.foundations.benchmark.BatchInsertBenchmarkKt",
        "Streaming" to "dev.typr.foundations.benchmark.StreamingBenchmarkKt",
        "Update" to "dev.typr.foundations.benchmark.UpdateBenchmarkKt",
        "Large Row" to "dev.typr.foundations.benchmark.LargeRowBenchmarkKt",
        "Cache Churn" to "dev.typr.foundations.benchmark.CacheChurnBenchmarkKt",
        "Tx Latency" to "dev.typr.foundations.benchmark.TxLatencyBenchmarkKt",
    )

    println("=== Running All Benchmarks (${benchmarks.size} suites) ===")
    println("Each suite runs in its own subprocess for clean connection isolation.")
    println()

    val timings = mutableListOf<Pair<String, Long>>()
    val classpath = System.getProperty("java.class.path")
    val javaHome = System.getProperty("java.home")
    val javaBin = "$javaHome/bin/java"

    for ((name, mainClass) in benchmarks) {
        println("=" .repeat(70))
        println("  Starting: $name")
        println("=" .repeat(70))

        val start = System.nanoTime()
        try {
            val pb = ProcessBuilder(
                javaBin,
                "--add-opens=java.base/sun.misc=ALL-UNNAMED",
                "-cp", classpath,
                mainClass
            )
            pb.inheritIO()
            pb.directory(java.io.File(System.getProperty("user.dir")))
            val process = pb.start()
            val exited = process.waitFor(600, java.util.concurrent.TimeUnit.SECONDS)
            if (!exited) {
                println("  TIMEOUT: $name — killing after 600s")
                process.destroyForcibly()
                process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS)
            } else if (process.exitValue() != 0) {
                println("  FAILED: $name — exit code ${process.exitValue()}")
            }
        } catch (e: Exception) {
            println("  FAILED: $name — ${e.message}")
        }

        val elapsed = (System.nanoTime() - start) / 1_000_000_000
        timings.add(name to elapsed)
        println()
        println("  Completed: $name (${elapsed}s)")
        println()
    }

    println("=" .repeat(70))
    println("=== All Benchmarks Complete ===")
    println("=" .repeat(70))
    for ((name, secs) in timings) {
        println("  %-30s %4ds".format(name, secs))
    }
    println("  %-30s %4ds".format("TOTAL", timings.sumOf { it.second }))
    println()
    println("Reports written to: $REPORT_DIR")
}
