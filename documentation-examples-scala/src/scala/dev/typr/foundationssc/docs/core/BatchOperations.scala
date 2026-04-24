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
  val insertAll: RowTemplate.Update[Product] =
    Fragment.insertInto("product", productCodec)

  // Connection is provided implicitly by the caller (e.g. inside transact { })
  def insertProducts(products: List[Product])(using Connection): Option[Array[Int]] =
    insertAll.onMany(products.iterator).run

  // Batch insert — skip auto-generated ID column
  val insertAutoId: RowTemplate.Update[Product] =
    Fragment.insertInto("product", productCodec, "id")

  def insertProductsAutoId(products: List[Product])(using Connection): Option[Array[Int]] =
    insertAutoId.onMany(products.iterator).run
  // stop
