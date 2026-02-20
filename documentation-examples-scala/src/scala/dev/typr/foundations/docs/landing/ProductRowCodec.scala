package dev.typr.foundations.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


import java.time.Instant

@SuppressWarnings(Array("unused"))
object ProductRowCodec:
  case class Product(id: ProductId, name: String, price: BigDecimal, tags: Option[Array[String]],
                     dimensions: Option[Dim], metadata: Option[Jsonb], createdAt: Option[Instant])
  case class ProductId(value: Int)
  case class Dim(width: Double, height: Double, depth: Double, unit: String)
  case class Category(id: Int, name: String)

  val productIdType: PgType[ProductId] = PgTypes.int4.transform(ProductId.apply, _.value)
  val dimensionsType: PgType[Dim] =
    PgStruct.builder[Dim]("dimensions")
      .field("width", PgTypes.float8, _.width)
      .field("height", PgTypes.float8, _.height)
      .field("depth", PgTypes.float8, _.depth)
      .field("unit", PgTypes.text, _.unit)
      .build(Dim.apply)
      .asType()

  val categoryRowCodec: RowCodec[Category] =
    RowCodec.builder[Category]()
      .field(PgTypes.int4)(_.id)
      .field(PgTypes.text)(_.name)
      .build(Category.apply)

  //start
  val rowCodec: RowCodec[Product] = RowCodec.builder[Product]()
    .field(productIdType)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.numeric)(_.price)
    .field(PgTypes.textArray.opt)(_.tags)
    .field(dimensionsType.opt)(_.dimensions)
    .field(PgTypes.jsonb.opt)(_.metadata)
    .field(PgTypes.timestamptz.opt)(_.createdAt)
    .build(Product.apply)

  // Compose codecs for joins
  val joined: RowCodec[(Product, Option[Category])] =
    rowCodec.leftJoined(categoryRowCodec)
  //stop
