package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

import java.time.Instant

@SuppressWarnings(Array("unused"))
object NamedRowParser:
  //start
  case class Product(id: Int, name: String, price: BigDecimal, createdAt: Instant)

  val productParser: RowParserNamed[Product] = RowParser.namedBuilder[Product]()
    .field("id", PgTypes.int4)(_.id)
    .field("name", PgTypes.text)(_.name)
    .field("price", PgTypes.numeric)(_.price)
    .field("created_at", PgTypes.timestamptz)(_.createdAt)
    .build(Product.apply)

  // Column list for SQL — no hand-written strings to keep in sync
  val allProducts = sql"SELECT ${productParser.columnList} FROM product"
  //stop
