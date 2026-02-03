package dev.typr.foundations.docs.landing

import dev.typr.foundations.{And, PgType, PgTypes}
import dev.typr.foundations.data.Jsonb
import dev.typr.foundations.scala.{DbTypeOps, RowParser}
import java.math.BigDecimal
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
    .field(productIdType, _.id)
    .field(PgTypes.text, _.name)
    .field(PgTypes.numeric, _.price)
    .field(PgTypes.textArray.nullable, _.tags)
    .field(dimensionsType.nullable, _.dimensions)
    .field(PgTypes.jsonb.nullable, _.metadata)
    .field(PgTypes.timestamptz.nullable, _.createdAt)
    .build(Product.apply)

  // Compose parsers for joins
  val joined: RowParser[And[Product, Option[Category]]] =
    rowParser.leftJoined(categoryRowParser)
  //stop
