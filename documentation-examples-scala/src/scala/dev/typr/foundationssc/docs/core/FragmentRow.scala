package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.time.Instant

@SuppressWarnings(Array("unused"))
object FragmentRow:
  case class Product(id: Int, name: String, price: BigDecimal, createdAt: Instant)

  val productCodec: RowCodecNamed[Product] = RowCodec
    .namedBuilder[Product]()
    .field("id", PgTypes.int4)(_.id)
    .field("name", PgTypes.text)(_.name)
    .field("price", PgTypes.numeric)(_.price)
    .field("created_at", PgTypes.timestamptz)(_.createdAt)
    .build(Product.apply)

  // start
  def insert(product: Product)(using Connection): Product =
    Fragment
      .insertIntoReturning("product", productCodec)
      .updateReturning(product)
      .run

  // Skip columns with database defaults — pass column names to except
  def insertWithDefault(product: Product)(using Connection): Product =
    Fragment
      .insertIntoReturning("product", productCodec, "id")
      .updateReturning(product)
      .run
  // stop
