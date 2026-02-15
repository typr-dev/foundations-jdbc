package dev.typr.foundationskt

import org.junit.Assert.assertEquals
import org.junit.Test
import java.sql.DriverManager
import java.util.concurrent.CyclicBarrier
import java.util.concurrent.atomic.AtomicReference

class SqlBuilderTest {

    @Test
    fun basicInterpolation() {
        val frag = Sql { "SELECT * FROM users WHERE id = ${DuckDbTypes.integer(42)}" }
        assertEquals("SELECT * FROM users WHERE id = ?::INTEGER", frag.render())
    }

    @Test
    fun multipleParams() {
        val frag = Sql { "SELECT * FROM t WHERE a = ${DuckDbTypes.integer(1)} AND b = ${DuckDbTypes.varchar("hello")}" }
        assertEquals("SELECT * FROM t WHERE a = ?::INTEGER AND b = ?::VARCHAR", frag.render())
    }

    @Test
    fun noParams() {
        val frag = Sql { "SELECT 1" }
        assertEquals("SELECT 1", frag.render())
    }

    @Test
    fun fragmentEmbedding() {
        val cols = Fragment.of("id, name")
        val frag = Sql { "SELECT $cols FROM users" }
        assertEquals("SELECT id, name FROM users", frag.render())
    }

    @Test
    fun mixedParamsAndFragments() {
        val table = Fragment.of("users")
        val frag = Sql { "SELECT * FROM $table WHERE id = ${DuckDbTypes.integer(1)} AND active = ${Fragment.of("true")}" }
        assertEquals("SELECT * FROM users WHERE id = ?::INTEGER AND active = true", frag.render())
    }

    @Test
    fun nestedSqlBlocks() {
        val inner = Sql { "id = ${DuckDbTypes.integer(1)}" }
        val outer = Sql { "SELECT * FROM t WHERE $inner AND name = ${DuckDbTypes.varchar("test")}" }
        assertEquals("SELECT * FROM t WHERE id = ?::INTEGER AND name = ?::VARCHAR", outer.render())
    }

    @Test
    fun toStringOutsideSql() {
        val frag = Fragment.of("SELECT 1")
        assertEquals("SELECT 1", frag.toString())
    }

    @Test
    fun emptyString() {
        val frag = Sql { "" }
        assertEquals("", frag.render())
    }

    @Test
    fun threadSafety() {
        val threadCount = 10
        val barrier = CyclicBarrier(threadCount)
        val errors = AtomicReference<Throwable?>(null)
        val results = Array<String?>(threadCount) { null }

        val threads = (0 until threadCount).map { i ->
            Thread {
                try {
                    barrier.await()
                    val frag = Sql { "SELECT ${DuckDbTypes.integer(i)} AS val" }
                    results[i] = frag.render()
                } catch (e: Throwable) {
                    errors.compareAndSet(null, e)
                }
            }
        }

        threads.forEach { it.start() }
        threads.forEach { it.join() }

        val error = errors.get()
        if (error != null) throw error

        for (i in 0 until threadCount) {
            assertEquals("SELECT ?::INTEGER AS val", results[i])
        }
    }

    @Test
    fun runtimeExecution() {
        DriverManager.getConnection("jdbc:duckdb:").use { conn ->
            val frag = Sql { "SELECT ${DuckDbTypes.integer(42)} AS answer" }
            val result = frag
                .query(RowParser.of(DuckDbTypes.integer).exactlyOne())
                .runChecked(conn)
            assertEquals(42, result)
        }
    }

    @Test
    fun runtimeExecutionMultipleParams() {
        DriverManager.getConnection("jdbc:duckdb:").use { conn ->
            val frag = Sql { "SELECT ${DuckDbTypes.integer(10)} + ${DuckDbTypes.integer(32)} AS answer" }
            val result = frag
                .query(RowParser.of(DuckDbTypes.integer).exactlyOne())
                .runChecked(conn)
            assertEquals(42, result)
        }
    }

    @Test
    fun paramAtStart() {
        val frag = Sql { "${DuckDbTypes.integer(1)} + 2" }
        assertEquals("?::INTEGER + 2", frag.render())
    }

    @Test
    fun paramAtEnd() {
        val frag = Sql { "SELECT ${DuckDbTypes.integer(1)}" }
        assertEquals("SELECT ?::INTEGER", frag.render())
    }

    @Test
    fun consecutiveParams() {
        val frag = Sql { "${DuckDbTypes.integer(1)}${DuckDbTypes.integer(2)}" }
        assertEquals("?::INTEGER?::INTEGER", frag.render())
    }
}
