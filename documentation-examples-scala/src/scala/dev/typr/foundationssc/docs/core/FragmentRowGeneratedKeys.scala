package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*

import java.time.Instant

@SuppressWarnings(Array("unused"))
object FragmentRowGeneratedKeys:
  case class Product(id: Int, name: String, price: BigDecimal, createdAt: Instant)

  val productCodec: RowCodecNamed[Product] = RowCodec
    .namedBuilder[Product]()
    .field("id", PgTypes.int4)(_.id)
    .field("name", PgTypes.text)(_.name)
    .field("price", PgTypes.numeric)(_.price)
    .field("created_at", PgTypes.timestamptz)(_.createdAt)
    .build(Product.apply)

  // start
  // For databases without RETURNING (DB2, Oracle, SQL Server, MariaDB):
  def insertGeneratedKey(product: Product)(using Connection): Int =
    Fragment
      .insertInto("product", productCodec, "id")
      .updateOneGenerated(product, Array("id"), RowCodec.of(PgTypes.int4).exactlyOne())
      .run
  // stop
