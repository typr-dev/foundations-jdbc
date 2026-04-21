package dev.typr.foundationskt

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.sql.DriverManager

class BatchOperationTest {

    data class Item(val name: String, val quantity: Int)

    val itemCodec: RowCodecNamed<Item> = RowCodec.namedBuilder<Item>()
        .field("name", DuckDbTypes.varchar, Item::name)
        .field("quantity", DuckDbTypes.integer, Item::quantity)
        .build(::Item)

    data class Product(val id: Int, val name: String, val quantity: Int)

    val productCodec: RowCodecNamed<Product> = RowCodec.namedBuilder<Product>()
        .field("id", DuckDbTypes.integer, Product::id)
        .field("name", DuckDbTypes.varchar, Product::name)
        .field("quantity", DuckDbTypes.integer, Product::quantity)
        .build(::Product)

    private fun withDuckDb(block: (Connection, java.sql.Connection) -> Unit) {
        DriverManager.getConnection("jdbc:duckdb:").use { jdbcConn ->
            val conn = Connection(dev.typr.foundations.internal.ConnectionJdbc(jdbcConn))
            block(conn, jdbcConn)
        }
    }

    @Test
    fun updateMany() {
        withDuckDb { conn, jdbcConn ->
            jdbcConn.createStatement().execute("CREATE TABLE items (name VARCHAR, quantity INTEGER)")

            val items = listOf(Item("apple", 10), Item("banana", 20), Item("cherry", 30))

            val insert = Fragment.of("INSERT INTO items (name, quantity) VALUES (?, ?)")
            val counts = insert.updateMany(itemCodec, items.iterator()).run(conn)
            assertNotNull(counts)
            assertEquals(3, counts!!.size)

            val result = Fragment.of("SELECT name, quantity FROM items ORDER BY name")
                .query(itemCodec.all())
                .run(conn)
            assertEquals(3, result.size)
            assertEquals("apple", result[0].name)
            assertEquals(10, result[0].quantity)
            assertEquals("banana", result[1].name)
            assertEquals(20, result[1].quantity)
            assertEquals("cherry", result[2].name)
            assertEquals(30, result[2].quantity)
        }
    }

    @Test
    fun updateReturningEach() {
        withDuckDb { conn, jdbcConn ->
            jdbcConn.createStatement().execute("CREATE TABLE items (name VARCHAR, quantity INTEGER)")

            val items = listOf(Item("apple", 10), Item("banana", 20))

            val insert = Fragment.of("INSERT INTO items (name, quantity) VALUES (?, ?) RETURNING name, quantity")
            val returned = insert.updateReturningEach(itemCodec, items.iterator()).run(conn)

            assertEquals(2, returned.size)
            assertEquals("apple", returned[0].name)
            assertEquals(10, returned[0].quantity)
            assertEquals("banana", returned[1].name)
            assertEquals(20, returned[1].quantity)
        }
    }

    @Test
    fun rowTemplateOnMany() {
        withDuckDb { conn, jdbcConn ->
            jdbcConn.createStatement().execute("CREATE TABLE products (id INTEGER, name VARCHAR, quantity INTEGER)")

            val insertTemplate = Fragment.of("INSERT INTO products (")
                .append(productCodec.columnList).append(") VALUES (")
                .paramRow(productCodec)
                .append(")")
                .update()

            val products = listOf(
                Product(1, "widget", 100),
                Product(2, "gadget", 200),
                Product(3, "doohickey", 300)
            )

            val counts = insertTemplate.onMany(products.iterator()).run(conn)
            assertNotNull(counts)
            assertEquals(3, counts!!.size)

            val result = Fragment.of("SELECT id, name, quantity FROM products ORDER BY id")
                .query(productCodec.all())
                .run(conn)
            assertEquals(3, result.size)
            assertEquals(1, result[0].id)
            assertEquals("widget", result[0].name)
            assertEquals(100, result[0].quantity)
            assertEquals(2, result[1].id)
            assertEquals("gadget", result[1].name)
            assertEquals(3, result[2].id)
            assertEquals("doohickey", result[2].name)
        }
    }

    @Test
    fun rowTemplateOnManySkippingColumns() {
        withDuckDb { conn, jdbcConn ->
            jdbcConn.createStatement().execute("CREATE SEQUENCE product_seq START 1")
            jdbcConn.createStatement().execute(
                "CREATE TABLE products2 (id INTEGER DEFAULT nextval('product_seq'), name VARCHAR, quantity INTEGER)"
            )

            val insertTemplate = Fragment.of("INSERT INTO products2 (name, quantity) VALUES (")
                .paramRow(productCodec, "id")
                .append(")")
                .update()

            val products = listOf(
                Product(0, "widget", 100),
                Product(0, "gadget", 200),
                Product(0, "doohickey", 300)
            )

            val counts = insertTemplate.onMany(products.iterator()).run(conn)
            assertNotNull(counts)
            assertEquals(3, counts!!.size)

            val result = Fragment.of("SELECT id, name, quantity FROM products2 ORDER BY id")
                .query(productCodec.all())
                .run(conn)
            assertEquals(3, result.size)
            assertEquals(1, result[0].id)
            assertEquals("widget", result[0].name)
            assertEquals(2, result[1].id)
            assertEquals("gadget", result[1].name)
            assertEquals(3, result[2].id)
            assertEquals("doohickey", result[2].name)
        }
    }

    @Test
    fun updateManyEmptyBatch() {
        withDuckDb { conn, jdbcConn ->
            jdbcConn.createStatement().execute("CREATE TABLE items (name VARCHAR, quantity INTEGER)")

            val insert = Fragment.of("INSERT INTO items (name, quantity) VALUES (?, ?)")
            val counts = insert.updateMany(itemCodec, emptyList<Item>().iterator()).run(conn)
            assertNotNull(counts)
            assertEquals(0, counts!!.size)
        }
    }

    @Test
    fun singleOnThenOnMany() {
        withDuckDb { conn, jdbcConn ->
            jdbcConn.createStatement().execute("CREATE TABLE products (id INTEGER, name VARCHAR, quantity INTEGER)")

            val insertTemplate = Fragment.of("INSERT INTO products (")
                .append(productCodec.columnList).append(") VALUES (")
                .paramRow(productCodec)
                .append(")")
                .update()

            // Single insert via .on()
            insertTemplate.on(Product(1, "first", 10)).run(conn)

            // Batch insert via .onMany()
            val batch = listOf(Product(2, "second", 20), Product(3, "third", 30))
            insertTemplate.onMany(batch.iterator()).run(conn)

            val result = Fragment.of("SELECT id, name, quantity FROM products ORDER BY id")
                .query(productCodec.all())
                .run(conn)
            assertEquals(3, result.size)
            assertEquals("first", result[0].name)
            assertEquals("second", result[1].name)
            assertEquals("third", result[2].name)
        }
    }
}
