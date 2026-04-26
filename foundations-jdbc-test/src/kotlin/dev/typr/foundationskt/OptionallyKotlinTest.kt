package dev.typr.foundationskt

import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import java.sql.DriverManager

class OptionallyKotlinTest {

    companion object {
        private lateinit var conn: dev.typr.foundationskt.Connection

        @JvmStatic
        @BeforeClass
        fun setup() {
            val jdbcConn = DriverManager.getConnection("jdbc:duckdb:")
            jdbcConn.createStatement().use { stmt ->
                stmt.execute("CREATE TABLE products (id INTEGER, name VARCHAR, price DECIMAL(10,2), active BOOLEAN)")
                stmt.execute("INSERT INTO products VALUES (1, 'Widget', 9.99, true)")
                stmt.execute("INSERT INTO products VALUES (2, 'Gadget', 19.99, true)")
                stmt.execute("INSERT INTO products VALUES (3, 'Doohickey', 5.99, false)")
            }
            conn = dev.typr.foundationskt.Connection(dev.typr.foundations.internal.ConnectionJdbc(jdbcConn))
        }
    }

    private val parser = RowCodec.builder<String>()
        .field(DuckDbTypes.varchar) { it }
        .build { it }

    private fun selectActive(activeOnly: Boolean): OperationRead<List<String>> =
        Fragment.of("SELECT name FROM products WHERE 1=1")
            .optionally(activeOnly).append(" AND active = true")
            .append(" ORDER BY name")
            .query(parser.all())

    private fun selectByName(name: String?): OperationRead<List<String>> =
        Fragment.of("SELECT name FROM products WHERE 1=1")
            .optionally(name).append(" AND name = ", DuckDbTypes.varchar)
            .append(" ORDER BY name")
            .query(parser.all())

    private fun selectByActiveAndName(active: Boolean, name: String?): OperationRead<List<String>> =
        Fragment.of("SELECT name FROM products WHERE active = ")
            .value(DuckDbTypes.boolean_, active)
            .optionally(name).append(" AND name = ", DuckDbTypes.varchar)
            .append(" ORDER BY name")
            .query(parser.all())

    private fun selectMulti(name: String?, activeOnly: Boolean): OperationRead<List<String>> =
        Fragment.of("SELECT name FROM products WHERE 1=1")
            .optionally(name).append(" AND name = ", DuckDbTypes.varchar)
            .optionally(activeOnly).append(" AND active = true")
            .append(" ORDER BY name")
            .query(parser.all())

    @Test
    fun booleanOptionallySameInKotlinAndJava() {
        val withActive = selectActive(true).run(conn)
        assertEquals(listOf("Gadget", "Widget"), withActive)

        val withoutActive = selectActive(false).run(conn)
        assertEquals(listOf("Doohickey", "Gadget", "Widget"), withoutActive)
    }

    @Test
    fun singleParamOptionallyUsesNullable() {
        val withName = selectByName("Widget").run(conn)
        assertEquals(listOf("Widget"), withName)

        val withoutName = selectByName(null).run(conn)
        assertEquals(listOf("Doohickey", "Gadget", "Widget"), withoutName)
    }

    @Test
    fun mixedParamAndOptionally() {
        val activeWithName = selectByActiveAndName(true, "Widget").run(conn)
        assertEquals(listOf("Widget"), activeWithName)

        val activeWithoutName = selectByActiveAndName(true, null).run(conn)
        assertEquals(listOf("Gadget", "Widget"), activeWithoutName)
    }

    @Test
    fun multipleOptionally() {
        val nameAndActive = selectMulti("Widget", true).run(conn)
        assertEquals(listOf("Widget"), nameAndActive)

        val noNameButActive = selectMulti(null, true).run(conn)
        assertEquals(listOf("Gadget", "Widget"), noNameButActive)

        val nameNoActive = selectMulti("Doohickey", false).run(conn)
        assertEquals(listOf("Doohickey"), nameNoActive)

        val neitherFilter = selectMulti(null, false).run(conn)
        assertEquals(listOf("Doohickey", "Gadget", "Widget"), neitherFilter)
    }

    @Test
    fun updateWithOptionally() {
        fun setActiveByName(active: Boolean, name: String?): Operation.Update =
            Fragment.of("UPDATE products SET active = ")
                .value(DuckDbTypes.boolean_, active)
                .optionally(name).append(" WHERE name = ", DuckDbTypes.varchar)
                .update()

        // Just verify the operations build without error
        setActiveByName(true, "Widget")
        setActiveByName(false, null)
    }
}
