package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.time.Instant

@SuppressWarnings(Array("unused"))
object BatchOperations:
  case class Product(id: Int, name: String, price: BigDecimal, createdAt: Instant)

  val productCodec: RowCodecNamed[Product] = RowCodec
    .namedBuilder[Product]()
    .field("id", PgTypes.int4)(_.id)
    .field("name", PgTypes.text)(_.name)
    .field("price", PgTypes.numeric)(_.price)
    .field("created_at", PgTypes.timestamptz)(_.createdAt)
    .build(Product.apply)

  // start
  // Batch insert — all columns as parameters
  // Connection is provided implicitly by the caller (e.g. inside transact { })
  def insertProducts(products: List[Product])(using Connection): Option[Array[Int]] =
    Fragment.insertMany("product", productCodec, products.iterator).run

  // Batch insert — skip auto-generated ID column
  def insertProductsAutoId(products: List[Product])(using Connection): Option[Array[Int]] =
    Fragment.insertMany("product", productCodec, products.iterator, "id").run
  // stop
