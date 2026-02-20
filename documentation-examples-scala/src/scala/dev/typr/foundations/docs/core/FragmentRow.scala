package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

import java.sql.Connection
import java.time.Instant

@SuppressWarnings(Array("unused"))
object FragmentRow:
  case class Product(id: Int, name: String, price: BigDecimal, createdAt: Instant)

  val productCodec: RowCodecNamed[Product] = RowCodec.namedBuilder[Product]()
    .field("id", PgTypes.int4)(_.id)
    .field("name", PgTypes.text)(_.name)
    .field("price", PgTypes.numeric)(_.price)
    .field("created_at", PgTypes.timestamptz)(_.createdAt)
    .build(Product.apply)

  val conn: Connection = null // placeholder

  //start
  def insert(product: Product): Product =
    sql"INSERT INTO product ("
      .append(productCodec.columnList)
      .append(") VALUES (")
      .row(productCodec, product)
      .append(") RETURNING ")
      .append(productCodec.columnList)
      .query(productCodec.exactlyOne())
      .run(conn)

  // Skip columns with database defaults — pass column names to except
  def insertWithDefault(product: Product): Product =
    sql"INSERT INTO product ("
      .append(productCodec.columnList)
      .append(") VALUES (DEFAULT, ")
      .row(productCodec, product, "id")
      .append(") RETURNING ")
      .append(productCodec.columnList)
      .query(productCodec.exactlyOne())
      .run(conn)
  //stop
