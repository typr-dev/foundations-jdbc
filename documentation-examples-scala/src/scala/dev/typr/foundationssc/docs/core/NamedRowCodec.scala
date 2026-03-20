package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

import java.time.Instant

@SuppressWarnings(Array("unused"))
object NamedRowCodec:
  // start
  case class Product(
      id: Int,
      name: String,
      price: BigDecimal,
      createdAt: Instant
  )

  val productCodec: RowCodecNamed[Product] =
    RowCodec
      .namedBuilder[Product]()
      .field("id", PgTypes.int4)(_.id)
      .field("name", PgTypes.text)(_.name)
      .field("price", PgTypes.numeric)(_.price)
      .field("created_at", PgTypes.timestamptz)(_.createdAt)
      .build(Product.apply)

  // Column list — no hand-written strings to keep in sync
  val allProducts =
    sql"SELECT ${productCodec.columnList} FROM product"
  // stop
