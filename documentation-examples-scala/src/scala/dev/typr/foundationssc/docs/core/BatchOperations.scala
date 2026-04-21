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

  val conn: Connection = null // placeholder

  // start
  // Batch insert — all columns as parameters
  val insertAll: RowTemplate.Update[Product] =
    Fragment.insertInto("product", productCodec)

  def insertProducts(products: List[Product]): Option[Array[Int]] =
    insertAll.onMany(products.iterator).run(conn)

  // Batch insert — skip auto-generated ID column
  val insertAutoId: RowTemplate.Update[Product] =
    Fragment.insertInto("product", productCodec, "id")

  def insertProductsAutoId(products: List[Product]): Option[Array[Int]] =
    insertAutoId.onMany(products.iterator).run(conn)
  // stop
