package dev.typr.foundations.docs.core

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import java.math.BigDecimal
import java.sql.Connection
import java.time.Instant

@Suppress("unused")
class FragmentRow {
    data class Product(val id: Int, val name: String, val price: BigDecimal, val createdAt: Instant)

    val productParser: RowParserNamed<Product> = RowParser.namedBuilder<Product>()
        .field("id", PgTypes.int4, Product::id)
        .field("name", PgTypes.text, Product::name)
        .field("price", PgTypes.numeric, Product::price)
        .field("created_at", PgTypes.timestamptz, Product::createdAt)
        .build(::Product)

    lateinit var conn: Connection

    //start
    // All columns as parameters — great for app-generated IDs
    val insertTemplate = Fragment.of("INSERT INTO product (")
        .append(productParser.columnList).append(") VALUES (")
        .paramRow(productParser)
        .append(") RETURNING ").append(productParser.columnList)
        .query(productParser.exactlyOne())

    fun insert(product: Product): Product =
        insertTemplate.on(product).run(conn)

    // Skip columns handled by the database — e.g. sequences or defaults
    val insertWithSequenceTemplate = Fragment.of("INSERT INTO product (")
        .append(productParser.columnList).append(") VALUES (nextval('product_id_seq'), ")
        .paramRow(productParser, "id")
        .append(") RETURNING ").append(productParser.columnList)
        .query(productParser.exactlyOne())

    fun insertWithSequence(product: Product): Product =
        insertWithSequenceTemplate.on(product).run(conn)
    //stop
}
