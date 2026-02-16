package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

import java.sql.Connection
import java.time.Instant

@SuppressWarnings(Array("unused"))
object FragmentRow:
  case class Product(id: Int, name: String, price: BigDecimal, createdAt: Instant)

  val productParser: RowParserNamed[Product] = RowParser.namedBuilder[Product]()
    .field("id", PgTypes.int4)(_.id)
    .field("name", PgTypes.text)(_.name)
    .field("price", PgTypes.numeric)(_.price)
    .field("created_at", PgTypes.timestamptz)(_.createdAt)
    .build(Product.apply)

  val conn: Connection = null // placeholder

  //start
  // All columns as parameters — great for app-generated IDs
  val insertTemplate: RowSqlTemplate.Query[Product, Product] = Fragment.of("INSERT INTO product (")
    .append(productParser.columnList).append(") VALUES (")
    .paramRow(productParser)
    .append(") RETURNING ").append(productParser.columnList)
    .query(productParser.exactlyOne())

  def insert(product: Product): Product =
    insertTemplate.on(product).run(conn)

  // Skip columns handled by the database — e.g. sequences or defaults
  val insertWithSequenceTemplate: RowSqlTemplate.Query[Product, Product] = Fragment.of("INSERT INTO product (")
    .append(productParser.columnList).append(") VALUES (nextval('product_id_seq'), ")
    .paramRow(productParser, "id")
    .append(") RETURNING ").append(productParser.columnList)
    .query(productParser.exactlyOne())

  def insertWithSequence(product: Product): Product =
    insertWithSequenceTemplate.on(product).run(conn)
  //stop
