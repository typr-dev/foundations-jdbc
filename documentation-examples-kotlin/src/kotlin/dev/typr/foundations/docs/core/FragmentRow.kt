package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.math.BigDecimal
import java.sql.Connection
import java.time.Instant

@Suppress("unused")
class FragmentRow {
    data class Product(val id: Int, val name: String, val price: BigDecimal, val createdAt: Instant)

    val productParser: RowParserNamed<Product> =
        RowParser.namedBuilder<Product>()
            .field("id", PgTypes.int4, Product::id)
            .field("name", PgTypes.text, Product::name)
            .field("price", PgTypes.numeric, Product::price)
            .field("created_at", PgTypes.timestamptz, Product::createdAt)
            .build(::Product)

    lateinit var conn: Connection

    //start
    fun insert(product: Product): Product =
        Fragment.of("INSERT INTO product (")
            .append(productParser.columnList)
            .append(") VALUES (")
            .row(productParser, product)
            .append(") RETURNING ")
            .append(productParser.columnList)
            .query(productParser.exactlyOne())
            .run(conn)

    // Skip columns with database defaults — pass column names to except
    fun insertWithDefault(product: Product): Product =
        Fragment.of("INSERT INTO product (")
            .append(productParser.columnList)
            .append(") VALUES (DEFAULT, ")
            .row(productParser, product, "id")
            .append(") RETURNING ")
            .append(productParser.columnList)
            .query(productParser.exactlyOne())
            .run(conn)
    //stop
}
