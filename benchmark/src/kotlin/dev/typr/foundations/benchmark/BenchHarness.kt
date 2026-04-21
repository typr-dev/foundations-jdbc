package dev.typr.foundations.benchmark

import org.HdrHistogram.ConcurrentHistogram
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

// ==================== Configuration ====================

const val WARMUP_SECONDS = 3L
const val MEASURE_SECONDS = 10L
const val CONCURRENCY = 200

// ==================== Result ====================

data class BenchResult(
    val name: String,
    val opsPerIteration: Long,
    val wallTimeMs: Long,
    val histogram: ConcurrentHistogram,
) {
    val opsPerSec: Double get() = opsPerIteration * 1000.0 / wallTimeMs
    val p50us: Long get() = histogram.getValueAtPercentile(50.0) / 1000
    val p95us: Long get() = histogram.getValueAtPercentile(95.0) / 1000
    val p99us: Long get() = histogram.getValueAtPercentile(99.0) / 1000
    val p999us: Long get() = histogram.getValueAtPercentile(99.9) / 1000
    val maxUs: Long get() = histogram.maxValue / 1000

    val library: String get() = when {
        name.startsWith("Foundations+PgPipe") -> "Foundations+PgPipe"
        name.startsWith("Foundations+Hikari") -> "Foundations+Hikari"
        name.startsWith("Raw JDBC") -> "Raw JDBC"
        name.startsWith("Hibernate") -> "Hibernate"
        name.startsWith("Vert.x") -> "Vert.x"
        else -> name
    }
}

// ==================== Time-Budgeted Measurement ====================

fun runBenchmark(name: String, taskCount: Int, warmupOps: Int, iterations: Int, op: (Int) -> Any?): BenchResult {
    return runTimedBenchmark(name, CONCURRENCY, WARMUP_SECONDS, MEASURE_SECONDS, op)
}

fun runBenchmarkNoWarmup(name: String, taskCount: Int, iterations: Int, op: (Int) -> Any?): BenchResult {
    return runTimedBenchmark(name, CONCURRENCY, 0, MEASURE_SECONDS, op)
}

fun runTimedBenchmark(
    name: String,
    concurrency: Int,
    warmupSeconds: Long,
    measureSeconds: Long,
    op: (Int) -> Any?,
): BenchResult {
    val counter = AtomicInteger(0)
    val running = AtomicBoolean(true)

    if (warmupSeconds > 0) {
        running.set(true)
        counter.set(0)
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val futures = (1..concurrency).map {
                exec.submit<Any?> {
                    while (running.get()) {
                        val i = counter.incrementAndGet()
                        try { op(i) } catch (_: Exception) {}
                    }
                }
            }
            Thread.sleep(warmupSeconds * 1000)
            running.set(false)
            futures.forEach { try { it.get() } catch (_: Exception) {} }
        }
    }

    val histogram = ConcurrentHistogram(1, 600_000_000_000L, 3)
    running.set(true)
    counter.set(0)

    val wallStart = System.nanoTime()
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        val futures = (1..concurrency).map {
            exec.submit<Any?> {
                while (running.get()) {
                    val i = counter.incrementAndGet()
                    val start = System.nanoTime()
                    try {
                        op(i)
                        histogram.recordValue(System.nanoTime() - start)
                    } catch (_: Exception) {}
                }
            }
        }
        Thread.sleep(measureSeconds * 1000)
        running.set(false)
        futures.forEach { try { it.get(5, java.util.concurrent.TimeUnit.SECONDS) } catch (_: Exception) {} }
    }
    val wallMs = (System.nanoTime() - wallStart) / 1_000_000

    val totalOps = histogram.totalCount
    return BenchResult(name, totalOps, wallMs, histogram)
}

fun runBatchFanOutBenchmark(
    name: String,
    taskCount: Int,
    batchSize: Int,
    warmupOps: Int,
    iterations: Int,
    op: (Int, Int) -> Any?,
): BenchResult {
    return runTimedBenchmark(name, CONCURRENCY, WARMUP_SECONDS, MEASURE_SECONDS) { i ->
        Executors.newVirtualThreadPerTaskExecutor().use { exec ->
            val subFutures = (0 until batchSize).map { j ->
                exec.submit<Any?> { op(i, j) }
            }
            subFutures.forEach(Future<*>::get)
        }
    }
}

fun runAsyncBenchmark(name: String, taskCount: Int, iterations: Int, op: (Int) -> CompletableFuture<*>): BenchResult {
    val histogram = ConcurrentHistogram(1, 600_000_000_000L, 3)
    val running = AtomicBoolean(true)
    val counter = AtomicInteger(0)

    // Warmup
    val warmupRunning = AtomicBoolean(true)
    val warmupFutures = mutableListOf<CompletableFuture<*>>()
    val warmupEnd = System.nanoTime() + WARMUP_SECONDS * 1_000_000_000
    while (System.nanoTime() < warmupEnd) {
        val i = counter.incrementAndGet()
        warmupFutures.add(op(i))
        if (warmupFutures.size > 1000) {
            CompletableFuture.allOf(*warmupFutures.toTypedArray()).join()
            warmupFutures.clear()
        }
    }
    if (warmupFutures.isNotEmpty()) {
        CompletableFuture.allOf(*warmupFutures.toTypedArray()).join()
    }

    // Measure
    counter.set(0)
    val wallStart = System.nanoTime()
    val measureEnd = wallStart + MEASURE_SECONDS * 1_000_000_000
    val pendingFutures = mutableListOf<CompletableFuture<*>>()

    while (System.nanoTime() < measureEnd) {
        val i = counter.incrementAndGet()
        val start = System.nanoTime()
        val future = op(i).thenApply { histogram.recordValue(System.nanoTime() - start) }
        pendingFutures.add(future)
        if (pendingFutures.size > 500) {
            CompletableFuture.allOf(*pendingFutures.toTypedArray()).join()
            pendingFutures.clear()
        }
    }
    if (pendingFutures.isNotEmpty()) {
        try {
            CompletableFuture.allOf(*pendingFutures.toTypedArray())
                .get(10, java.util.concurrent.TimeUnit.SECONDS)
        } catch (_: Exception) {}
    }

    val wallMs = (System.nanoTime() - wallStart) / 1_000_000
    val totalOps = histogram.totalCount
    return BenchResult(name, totalOps, wallMs, histogram)
}

fun runAsyncBatchBenchmark(
    name: String,
    taskCount: Int,
    batchSize: Int,
    iterations: Int,
    op: (Int, Int) -> CompletableFuture<*>,
): BenchResult {
    return runAsyncBenchmark(name, taskCount, iterations) { i ->
        val subFutures = (0 until batchSize).map { j -> op(i, j) }
        CompletableFuture.allOf(*subFutures.toTypedArray())
    }
}

// ==================== Resource Cleanup ====================

fun closeQuietly(name: String, action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
        println("  Warning: failed to close $name: ${e.message}")
    }
}

// ==================== Warmup ====================

fun warmup(count: Int, op: (Int) -> Any?) {
    if (count <= 0) return
    Executors.newVirtualThreadPerTaskExecutor().use { exec ->
        (1..count).map { i ->
            exec.submit<Any?> { op(i) }
        }.forEach(Future<*>::get)
    }
}

// ==================== Reporting ====================

fun printResults(title: String, results: List<BenchResult>, metricLabel: String) {
    println()
    println("=== $title ===")
    println("%-22s %12s %10s %10s %10s %10s %10s".format(
        "", metricLabel, "p50", "p95", "p99", "p99.9", "wall"
    ))
    for (r in results) {
        println("%-22s %,12.0f %8dus %8dus %8dus %8dus %7dms".format(
            r.name,
            r.opsPerSec,
            r.p50us,
            r.p95us,
            r.p99us,
            r.p999us,
            r.wallTimeMs,
        ))
    }
    if (results.size >= 2) {
        val fastest = results.maxBy { it.opsPerSec }
        val slowest = results.minBy { it.opsPerSec }
        val speedup = fastest.opsPerSec / slowest.opsPerSec
        println("  → ${fastest.name} is %.1fx faster than ${slowest.name}".format(speedup))
    }
}

fun printResultsCompact(title: String, results: List<BenchResult>, metricLabel: String) {
    println()
    println("=== $title ===")
    println("%-22s %12s %10s %10s".format("", metricLabel, "p50", "wall"))
    for (r in results) {
        println("%-22s %,12.0f %8dus %7dms".format(
            r.name,
            r.opsPerSec,
            r.p50us,
            r.wallTimeMs,
        ))
    }
    if (results.size >= 2) {
        val fastest = results.maxBy { it.opsPerSec }
        val slowest = results.minBy { it.opsPerSec }
        val speedup = fastest.opsPerSec / slowest.opsPerSec
        println("  → ${fastest.name} is %.1fx faster than ${slowest.name}".format(speedup))
    }
}

// ==================== LatencyProxy Helper ====================

class ProxiedPort(val port: Int, private val proxy: LatencyProxy?) : AutoCloseable {
    override fun close() {
        proxy?.close()
    }
}

fun proxiedPort(latencyMs: Long, baseProxyPort: Int): ProxiedPort {
    if (latencyMs == 0L) return ProxiedPort(PG_PORT, null)
    val port = baseProxyPort + latencyMs.toInt()
    val proxy = LatencyProxy(port, PG_HOST, PG_PORT, latencyMs)
    proxy.start()
    return ProxiedPort(port, proxy)
}

// ==================== JSON Report ====================

class MarkdownReport(private val title: String) {
    private val sections = mutableListOf<Map<String, Any>>()

    fun section(heading: String, description: String, results: List<BenchResult>, metricLabel: String) {
        section(heading, description, results, metricLabel, "")
    }

    fun section(heading: String, description: String, results: List<BenchResult>, metricLabel: String, code: String) {
        sections.add(mapOf(
            "title" to heading,
            "description" to description,
            "metricLabel" to metricLabel,
            "code" to code.trimIndent().trim(),
            "results" to results.map { r -> resultToMap(r) },
        ))
    }

    fun sectionCompact(heading: String, description: String, results: List<BenchResult>, metricLabel: String) {
        section(heading, description, results, metricLabel, "")
    }

    fun rawSection(markdown: String) {
        sections.add(mapOf(
            "title" to "",
            "description" to markdown,
            "metricLabel" to "",
            "results" to emptyList<Any>(),
        ))
    }

    fun writeTo(dir: String, filename: String) {
        val jsonFilename = filename.replace(".md", ".json")
        val file = File(dir, jsonFilename)
        file.parentFile.mkdirs()
        file.writeText(toJson())
        println("  Wrote report: ${file.absolutePath}")
        System.exit(0)
    }

    private fun toJson(): String {
        val sb = StringBuilder()
        sb.appendLine("{")
        sb.appendLine("  \"title\": ${jsonStr(title)},")
        sb.appendLine("  \"timestamp\": ${jsonStr(java.time.Instant.now().toString())},")
        sb.appendLine("  \"sections\": [")
        for ((idx, section) in sections.withIndex()) {
            sb.appendLine("    {")
            sb.appendLine("      \"title\": ${jsonStr(section["title"] as String)},")
            sb.appendLine("      \"description\": ${jsonStr(section["description"] as String)},")
            sb.appendLine("      \"metricLabel\": ${jsonStr(section["metricLabel"] as String)},")
            val code = section["code"] as String
            if (code.isNotEmpty()) {
                sb.appendLine("      \"code\": ${jsonStr(code)},")
            }
            @Suppress("UNCHECKED_CAST")
            val results = section["results"] as List<Map<String, Any>>
            sb.appendLine("      \"results\": [")
            for ((ridx, r) in results.withIndex()) {
                sb.append("        {")
                sb.append("\"name\":${jsonStr(r["name"] as String)},")
                sb.append("\"library\":${jsonStr(r["library"] as String)},")
                sb.append("\"opsPerSec\":${String.format(java.util.Locale.US, "%.1f", r["opsPerSec"] as Double)},")
                sb.append("\"p50us\":${r["p50us"]},")
                sb.append("\"p95us\":${r["p95us"]},")
                sb.append("\"p99us\":${r["p99us"]},")
                sb.append("\"p999us\":${r["p999us"]},")
                sb.append("\"wallTimeMs\":${r["wallTimeMs"]}")
                sb.appendLine("}${if (ridx < results.size - 1) "," else ""}")
            }
            sb.appendLine("      ]")
            sb.appendLine("    }${if (idx < sections.size - 1) "," else ""}")
        }
        sb.appendLine("  ]")
        sb.appendLine("}")
        return sb.toString()
    }
}

private fun resultToMap(r: BenchResult): Map<String, Any> = mapOf(
    "name" to r.name,
    "library" to r.library,
    "opsPerSec" to r.opsPerSec,
    "p50us" to r.p50us,
    "p95us" to r.p95us,
    "p99us" to r.p99us,
    "p999us" to r.p999us,
    "wallTimeMs" to r.wallTimeMs,
)

private fun jsonStr(s: String): String {
    val escaped = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
    return "\"$escaped\""
}

val REPORT_DIR: String by lazy {
    val cwd = File(System.getProperty("user.dir"))
    var dir = cwd
    while (dir.parentFile != null) {
        if (File(dir, "site/static/benchmark-results").isDirectory) {
            return@lazy File(dir, "site/static/benchmark-results").absolutePath
        }
        if (File(dir, "site/static").isDirectory) {
            return@lazy File(dir, "site/static/benchmark-results").absolutePath
        }
        dir = dir.parentFile
    }
    File(cwd, "benchmark-results").absolutePath
}
