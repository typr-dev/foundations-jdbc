package dev.typr.foundations.benchmark

import dev.typr.foundations.connect.DuckDbConfig
import dev.typr.foundations.connect.ConnectionSource
import java.io.File
import java.math.BigDecimal
import java.sql.Connection
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

const val COLUMNS = "id, name, description, category, tags, quantity, price, weight, active, rating, created_at, updated_at, release_date, sku, notes"
const val SELECT_SQL = "SELECT $COLUMNS FROM items"

private val SIZES = listOf(1, 10, 100, 1_000, 10_000, 100_000)
private const val WARMUP_RUNS = 5
private const val MEASURED_RUNS = 11

private val CATEGORIES = listOf("Electronics", "Books", "Clothing", "Home & Garden", "Sports", "Toys", "Food", "Automotive")
private val BASE_DATE = LocalDateTime.of(2024, 1, 1, 0, 0)
private val BASE_LOCAL_DATE = LocalDate.of(2024, 1, 1)

fun main() {
    val ds = ConnectionSource.of(DuckDbConfig.inMemory().build())

    val benchmarks = listOf(
        RawJdbcBenchmark(ds),
        FoundationsBenchmark(ds),
        HibernateEntityBenchmark(ds),
        HibernateNativeBenchmark(ds),
        JdbiBenchmark(ds),
        JdbcTemplateBenchmark(ds),
    )

    val results = mutableMapOf<String, MutableMap<Int, Long>>()
    for (b in benchmarks) {
        results[b.name] = mutableMapOf()
    }

    println("Benchmark: SELECT all rows from 'items' table (15 columns, varied types)")
    println("Warmup: $WARMUP_RUNS runs, Measured: $MEASURED_RUNS runs (median)")
    println("=" .repeat(70))

    for (n in SIZES) {
        println("\n--- N = %,d ---".format(n))
        setupTable(ds.getConnection(), n)

        for (benchmark in benchmarks) {
            // Warmup
            repeat(WARMUP_RUNS) {
                val items = benchmark.selectAll()
                check(items.size == n) { "${benchmark.name}: expected $n rows, got ${items.size}" }
            }

            // Measured runs
            val times = mutableListOf<Long>()
            repeat(MEASURED_RUNS) {
                val start = System.nanoTime()
                val items = benchmark.selectAll()
                val elapsed = System.nanoTime() - start
                check(items.size == n) { "${benchmark.name}: expected $n rows, got ${items.size}" }
                times.add(elapsed)
            }

            val median = times.sorted()[MEASURED_RUNS / 2]
            results[benchmark.name]!![n] = median
            println("  %-20s %s".format(benchmark.name, formatNanos(median)))
        }
    }

    println("\n" + "=".repeat(70))
    println("Done. Writing results to site/docs/benchmarks.md")
    writeMarkdown(benchmarks.map { it.name }, results)
}

private fun setupTable(conn: Connection, n: Int) {
    conn.createStatement().use { stmt ->
        stmt.execute("DROP TABLE IF EXISTS items")
        stmt.execute("""
            CREATE TABLE items (
                id INTEGER,
                name VARCHAR,
                description VARCHAR,
                category VARCHAR,
                tags VARCHAR,
                quantity INTEGER,
                price DECIMAL(12,2),
                weight DOUBLE,
                active BOOLEAN,
                rating DOUBLE,
                created_at TIMESTAMP,
                updated_at TIMESTAMP,
                release_date DATE,
                sku VARCHAR,
                notes VARCHAR
            )
        """.trimIndent())
    }
    conn.prepareStatement("INSERT INTO items VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)").use { ps ->
        for (i in 1..n) {
            ps.setInt(1, i)
            ps.setString(2, "Item #$i — ${CATEGORIES[i % CATEGORIES.size]} Product")
            ps.setString(3, "Detailed description for item $i. This product features high-quality materials, " +
                "precision engineering, and comes with a comprehensive warranty. Suitable for both professional " +
                "and personal use. Model: MDL-${i * 7 % 9999}. Batch: ${i / 100 + 1}.")
            ps.setString(4, CATEGORIES[i % CATEGORIES.size])
            ps.setString(5, "tag-${i % 20},tag-${i % 7},tag-${i % 13},featured,sale-${i % 5}")
            ps.setInt(6, i * 10 + (i % 97))
            ps.setBigDecimal(7, BigDecimal("${i % 10000}.${i % 100}"))
            ps.setDouble(8, (i % 500) * 0.1 + 0.05)
            ps.setBoolean(9, i % 3 != 0)
            ps.setDouble(10, 1.0 + (i % 50) * 0.08)
            ps.setObject(11, BASE_DATE.plusMinutes(i.toLong()))
            ps.setObject(12, BASE_DATE.plusMinutes(i.toLong() * 2))
            ps.setObject(13, BASE_LOCAL_DATE.plusDays(i.toLong() % 365))
            ps.setString(14, UUID(i.toLong(), (i.toLong() * 31) xor 0x123456789ABCDEFL).toString())
            ps.setString(15, "Note for item $i: shipped from warehouse W-${i % 12}, shelf S-${i % 200}, " +
                "bin B-${i % 50}. Last audit: ${2024 + i % 3}-Q${i % 4 + 1}.")
            ps.addBatch()
            if (i % 10_000 == 0) ps.executeBatch()
        }
        ps.executeBatch()
    }
}

private fun formatNanos(nanos: Long): String {
    return when {
        nanos < 1_000 -> "${nanos} ns"
        nanos < 1_000_000 -> "%.1f us".format(nanos / 1_000.0)
        nanos < 1_000_000_000 -> "%.2f ms".format(nanos / 1_000_000.0)
        else -> "%.3f s".format(nanos / 1_000_000_000.0)
    }
}

private fun writeMarkdown(libraryNames: List<String>, results: Map<String, Map<Int, Long>>) {
    val sb = StringBuilder()
    sb.appendLine("---")
    sb.appendLine("sidebar_position: 9")
    sb.appendLine("---")
    sb.appendLine()
    sb.appendLine("# Benchmarks")
    sb.appendLine()
    sb.appendLine("## Methodology")
    sb.appendLine()
    sb.appendLine("All benchmarks run against an **in-memory DuckDB** database on a single shared connection.")
    sb.appendLine("Each library executes a SELECT returning **15 columns** of varied types (VARCHAR, INTEGER, DECIMAL, DOUBLE, BOOLEAN, TIMESTAMP, DATE) and maps every row into a Kotlin data class.")
    sb.appendLine()
    sb.appendLine("- **$WARMUP_RUNS** warmup runs (discarded)")
    sb.appendLine("- **$MEASURED_RUNS** measured runs, **median** reported")
    sb.appendLine("- Row counts verified on every run")
    sb.appendLine("- All libraries share the same `javax.sql.DataSource` (single connection, no pooling overhead)")
    sb.appendLine("- VARCHAR columns contain realistic-length strings (50-200 chars)")
    sb.appendLine()
    sb.appendLine("## Results")
    sb.appendLine()

    // Header row
    sb.append("| Rows |")
    for (name in libraryNames) sb.append(" $name |")
    sb.appendLine()

    // Separator
    sb.append("| ---: |")
    for (name in libraryNames) sb.append(" ---: |")
    sb.appendLine()

    // Data rows
    for (n in SIZES) {
        sb.append("| %,d |".format(n))
        for (name in libraryNames) {
            val nanos = results[name]!![n]!!
            sb.append(" %s |".format(formatNanos(nanos)))
        }
        sb.appendLine()
    }

    sb.appendLine()
    sb.appendLine("## Libraries")
    sb.appendLine()
    sb.appendLine("| Library | Approach |")
    sb.appendLine("| :--- | :--- |")
    sb.appendLine("| **Raw JDBC** | `PreparedStatement` + positional `getInt`/`getString` — theoretical minimum |")
    sb.appendLine("| **Foundations JDBC** | `RowCodec` + `Fragment.query()` + `Transactor` |")
    sb.appendLine("| **Hibernate (entity)** | `@Entity` + `SessionFactory` + native SQL mapped to managed entity — full ORM machinery |")
    sb.appendLine("| **Hibernate (native)** | `SessionFactory` + native SQL + manual `Object[]` tuple mapping — lightest Hibernate usage |")
    sb.appendLine("| **JDBI** | `Jdbi.create(ds)` + `RowMapper` + `withHandle` |")
    sb.appendLine("| **JdbcTemplate** | `JdbcTemplate(ds)` + `RowMapper` |")
    sb.appendLine()
    sb.appendLine("## Notes")
    sb.appendLine()
    sb.appendLine("- This benchmark measures **mapping overhead**, not database query execution time. All libraries run the same SQL against the same in-memory DuckDB instance.")
    sb.appendLine("- **Hibernate (entity)** uses `@Entity` annotation with full ORM machinery: bytecode-enhanced entity hydration, persistence context management, and dirty-checking state snapshots. This is how Hibernate is typically used in production.")
    sb.appendLine("- **Hibernate (native)** uses `createNativeQuery` with `Object[]` tuple result and manual column mapping — the absolute lightest-weight Hibernate usage, bypassing the entire entity lifecycle.")
    sb.appendLine("- DuckDB is an embedded analytical database — there is no network latency. Differences between libraries are pure framework overhead.")

    val siteDocsDir = File("site/docs")
    siteDocsDir.mkdirs()
    File(siteDocsDir, "benchmarks.md").writeText(sb.toString())
    println("Wrote site/docs/benchmarks.md")
}
