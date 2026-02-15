package dev.typr.foundations.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


import java.time.Instant

@SuppressWarnings(Array("unused"))
object ProductRowParser:
  case class Product(id: ProductId, name: String, price: BigDecimal, tags: Option[Array[String]],
                     dimensions: Option[Dim], metadata: Option[Jsonb], createdAt: Option[Instant])
  case class ProductId(value: Int)
  case class Dim(width: Double, height: Double, depth: Double, unit: String)
  case class Category(id: Int, name: String)

  val productIdType: PgType[ProductId] = PgTypes.int4.bimap(ProductId.apply, _.value)
  val dimensionsType: PgType[Dim] = null // placeholder
  val categoryRowParser: RowParser[Category] = null // placeholder

  //start
  val rowParser: RowParser[Product] = RowParser.builder[Product]()
    .field(productIdType)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.numeric)(_.price)
    .field(PgTypes.textArray.opt)(_.tags)
    .field(dimensionsType.opt)(_.dimensions)
    .field(PgTypes.jsonb.opt)(_.metadata)
    .field(PgTypes.timestamptz.opt)(_.createdAt)
    .build(Product.apply)

  // Compose parsers for joins
  val joined: RowParser[(Product, Option[Category])] =
    rowParser.leftJoined(categoryRowParser)
  //stop
