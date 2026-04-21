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

    @Test
    fun booleanOptionallySameInKotlinAndJava() {
        val template = Fragment.of("SELECT name FROM products WHERE 1=1")
            .optionally(Fragment.of(" AND active = true"))
            .query(parser.all())

        val withActive = template.on(true).run(conn)
        assertEquals(listOf("Widget", "Gadget"), withActive)

        val withoutActive = template.on(false).run(conn)
        assertEquals(listOf("Widget", "Gadget", "Doohickey"), withoutActive)
    }

    @Test
    fun singleParamOptionallyUsesNullable() {
        val template = Fragment.of("SELECT name FROM products WHERE 1=1")
            .optionally(Fragment.of(" AND name = ").param(DuckDbTypes.varchar))
            .append(" ORDER BY name")
            .query(parser.all())

        val withName = template.on("Widget").run(conn)
        assertEquals(listOf("Widget"), withName)

        val withoutName = template.on(null).run(conn)
        assertEquals(listOf("Doohickey", "Gadget", "Widget"), withoutName)
    }

    @Test
    fun mixedParamAndOptionally() {
        val template = Fragment.of("SELECT name FROM products WHERE active = ")
            .param(DuckDbTypes.boolean_)
            .optionally(Fragment.of(" AND name = ").param(DuckDbTypes.varchar))
            .append(" ORDER BY name")
            .query(parser.all())

        val activeWithName = template.on(true, "Widget").run(conn)
        assertEquals(listOf("Widget"), activeWithName)

        val activeWithoutName = template.on(true, null).run(conn)
        assertEquals(listOf("Gadget", "Widget"), activeWithoutName)
    }

    @Test
    fun multipleOptionally() {
        val template = Fragment.of("SELECT name FROM products WHERE 1=1")
            .optionally(Fragment.of(" AND name = ").param(DuckDbTypes.varchar))
            .optionally(Fragment.of(" AND active = true"))
            .append(" ORDER BY name")
            .query(parser.all())

        val nameAndActive = template.on("Widget", true).run(conn)
        assertEquals(listOf("Widget"), nameAndActive)

        val noNameButActive = template.on(null, true).run(conn)
        assertEquals(listOf("Gadget", "Widget"), noNameButActive)

        val nameNoActive = template.on("Doohickey", false).run(conn)
        assertEquals(listOf("Doohickey"), nameNoActive)

        val neitherFilter = template.on(null, false).run(conn)
        assertEquals(listOf("Doohickey", "Gadget", "Widget"), neitherFilter)
    }

    @Test
    fun updateWithOptionally() {
        val template = Fragment.of("UPDATE products SET active = ")
            .param(DuckDbTypes.boolean_)
            .optionally(Fragment.of(" WHERE name = ").param(DuckDbTypes.varchar))
            .update()

        val renderedFragment = template.fragment().render()
        assert(renderedFragment.contains("UPDATE products SET active = "))
    }
}
