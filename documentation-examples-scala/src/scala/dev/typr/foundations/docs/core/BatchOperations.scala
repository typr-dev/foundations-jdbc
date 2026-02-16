package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.sql.Connection
import java.time.Instant

@SuppressWarnings(Array("unused"))
object BatchOperations:
  case class Product(id: Int, name: String, price: BigDecimal, createdAt: Instant)

  val productParser: RowParserNamed[Product] = RowParser.namedBuilder[Product]()
    .field("id", PgTypes.int4)(_.id)
    .field("name", PgTypes.text)(_.name)
    .field("price", PgTypes.numeric)(_.price)
    .field("created_at", PgTypes.timestamptz)(_.createdAt)
    .build(Product.apply)

  val conn: Connection = null // placeholder

  //start
  // Batch insert — all columns as parameters
  val insertAll: RowSqlTemplate.Update[Product] =
    Fragment.of("INSERT INTO product (")
      .append(productParser.columnList)
      .append(") VALUES (")
      .paramRow(productParser)
      .append(")")
      .update()

  def insertProducts(products: List[Product]): Array[Int] =
    insertAll.onMany(products.iterator).run(conn)

  // Batch insert — skip auto-generated ID column
  val insertAutoId: RowSqlTemplate.Update[Product] =
    Fragment.of(
      "INSERT INTO product (name, price, created_at) VALUES ("
    ).paramRow(productParser, "id")
      .append(")")
      .update()

  def insertProductsAutoId(products: List[Product]): Array[Int] =
    insertAutoId.onMany(products.iterator).run(conn)
  //stop
