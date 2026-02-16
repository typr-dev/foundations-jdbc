package dev.typr.foundationskt

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.junit.Assert.assertEquals
import org.junit.Test
import java.sql.DriverManager
import java.util.concurrent.ConcurrentLinkedQueue
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

    @Test
    fun columnListEmbedding() {
        data class Row(val id: Int, val name: String)

        val parser: RowParserNamed<Row> = RowParser.namedBuilder<Row>()
            .field("id", DuckDbTypes.integer, Row::id)
            .field("name", DuckDbTypes.varchar, Row::name)
            .build(::Row)

        val frag = Sql { "SELECT ${parser.columnList} FROM users WHERE id = ${DuckDbTypes.integer(1)}" }
        assertEquals("SELECT id, name FROM users WHERE id = ?::INTEGER", frag.render())
    }

    @Test
    fun columnListEmbeddingRuntime() {
        data class Row(val id: Int, val name: String)

        val parser: RowParserNamed<Row> = RowParser.namedBuilder<Row>()
            .field("id", DuckDbTypes.integer, Row::id)
            .field("name", DuckDbTypes.varchar, Row::name)
            .build(::Row)

        DriverManager.getConnection("jdbc:duckdb:").use { conn ->
            conn.createStatement().execute("CREATE TABLE test_users (id INTEGER, name VARCHAR)")
            conn.createStatement().execute("INSERT INTO test_users VALUES (1, 'Alice'), (2, 'Bob')")

            val frag = Sql { "SELECT ${parser.columnList} FROM test_users WHERE id = ${DuckDbTypes.integer(1)}" }
            val result = frag.query(parser.exactlyOne()).runChecked(conn)
            assertEquals(1, result.id)
            assertEquals("Alice", result.name)
        }
    }

    @Test
    fun virtualThreadSafety() {
        val results = ConcurrentLinkedQueue<Pair<Int, String>>()

        val threads = (0 until 1000).map { i ->
            Thread.ofVirtual().start {
                val frag = Sql { "SELECT ${DuckDbTypes.integer(i)} AS val" }
                results.add(i to frag.render())
            }
        }

        threads.forEach { it.join() }

        assertEquals(1000, results.size)
        val byIndex = results.associateBy({ it.first }, { it.second })
        for (i in 0 until 1000) {
            assertEquals("SELECT ?::INTEGER AS val", byIndex[i])
        }
    }

    @Test
    fun coroutinesSafety() {
        val results = runBlocking {
            (0 until 1000).map { i ->
                async(Dispatchers.Default) {
                    val frag = Sql { "SELECT ${DuckDbTypes.integer(i)} AS val" }
                    i to frag.render()
                }
            }.awaitAll()
        }

        assertEquals(1000, results.size)
        val byIndex = results.associateBy({ it.first }, { it.second })
        for (i in 0 until 1000) {
            assertEquals("SELECT ?::INTEGER AS val", byIndex[i])
        }
    }

    @Test
    fun coroutinesWithContextSwitch() {
        runBlocking {
            val frag1 = withContext(Dispatchers.Default) {
                Sql { "SELECT ${DuckDbTypes.integer(1)} AS a" }
            }
            val frag2 = withContext(Dispatchers.IO) {
                Sql { "SELECT ${DuckDbTypes.varchar("hello")} AS b" }
            }

            assertEquals("SELECT ?::INTEGER AS a", frag1.render())
            assertEquals("SELECT ?::VARCHAR AS b", frag2.render())
        }
    }

    @Test
    fun virtualThreadsWithDuckDbExecution() {
        DriverManager.getConnection("jdbc:duckdb:").use { conn ->
            val results = ConcurrentLinkedQueue<Pair<Int, Int>>()

            val threads = (0 until 1000).map { i ->
                Thread.ofVirtual().start {
                    val frag = Sql { "SELECT ${DuckDbTypes.integer(i)}" }
                    val result = synchronized(conn) {
                        frag.query(RowParser.of(DuckDbTypes.integer).exactlyOne()).runChecked(conn)
                    }
                    results.add(i to result)
                }
            }

            threads.forEach { it.join() }

            assertEquals(1000, results.size)
            val byIndex = results.associateBy({ it.first }, { it.second })
            for (i in 0 until 1000) {
                assertEquals(i, byIndex[i])
            }
        }
    }

    @Test
    fun coroutinesWithDuckDbExecution() {
        DriverManager.getConnection("jdbc:duckdb:").use { conn ->
            val mutex = Mutex()

            val results = runBlocking {
                (0 until 1000).map { i ->
                    async(Dispatchers.Default) {
                        val frag = Sql { "SELECT ${DuckDbTypes.integer(i)}" }
                        val result = mutex.withLock {
                            frag.query(RowParser.of(DuckDbTypes.integer).exactlyOne()).runChecked(conn)
                        }
                        i to result
                    }
                }.awaitAll()
            }

            assertEquals(1000, results.size)
            val byIndex = results.associateBy({ it.first }, { it.second })
            for (i in 0 until 1000) {
                assertEquals(i, byIndex[i])
            }
        }
    }
}
