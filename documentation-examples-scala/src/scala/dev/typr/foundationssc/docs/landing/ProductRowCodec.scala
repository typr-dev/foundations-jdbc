package dev.typr.foundationssc.docs.landing
import dev.typr.foundationssc.*

import java.time.Instant

@SuppressWarnings(Array("unused"))
object ProductRowCodec:
  case class Product(id: Int, name: String, price: BigDecimal, createdAt: Option[Instant])
  case class Category(id: Int, name: String)

  val categoryRowCodec: RowCodec[Category] =
    RowCodec
      .builder[Category]()
      .field(PgTypes.int4)(_.id)
      .field(PgTypes.text)(_.name)
      .build(Category.apply)

  // start
  val productCodec: RowCodecNamed[Product] = RowCodec
    .namedBuilder[Product]()
    .field("id", PgTypes.int4)(_.id)
    .field("name", PgTypes.text)(_.name)
    .field("price", PgTypes.numeric)(_.price)
    .field("created_at", PgTypes.timestamptz.opt)(_.createdAt)
    .build(Product.apply)

  // Compose codecs for joins
  val joined: RowCodec[(Product, Option[Category])] =
    productCodec.leftJoined(categoryRowCodec)
  // stop
