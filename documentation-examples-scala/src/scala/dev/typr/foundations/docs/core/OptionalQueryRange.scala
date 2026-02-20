package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object OptionalQueryRange:
  case class Product(id: Int, name: String, price: BigDecimal)

  val productCodec: RowCodec[Product] = RowCodec.builder[Product]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.numeric)(_.price)
    .build(Product.apply)

  var tx: Transactor = null // placeholder

  //start
  // When an optional clause needs multiple parameters,
  // pass a multi-parameter builder.
  // The grouped parameters are provided or omitted together.
  val byPriceRange: Template[Option[(BigDecimal, BigDecimal)], List[Product]] =
    Fragment.of(
      "SELECT id, name, price FROM products WHERE 1=1"
    ).optionally(
        Fragment.of(" AND price BETWEEN ")
          .param(PgTypes.numeric)
          .append(" AND ")
          .param(PgTypes.numeric))
      .query(productCodec.all())

  // With range
  def inRange(): List[Product] =
    byPriceRange
      .on(Some((BigDecimal("10"), BigDecimal("50"))))
      .transact(tx)

  // Without range - returns all products
  def all(): List[Product] =
    byPriceRange.on(None).transact(tx)
  //stop
