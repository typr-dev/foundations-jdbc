package dev.typr.foundationssc.docs.dynamic
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object WhenDsl:
  case class ProductRow(id: Int, name: String, price: BigDecimal)

  val codec: RowCodec[ProductRow] = RowCodec
    .builder[ProductRow]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.numeric)(_.price)
    .build(ProductRow.apply)

  // start
  // Three optional filters → 2^3 = 8 SQL shapes Query Analysis verifies.
  def search(
      namePattern: Option[String],
      maxPrice: Option[BigDecimal],
      onlyActive: Boolean
  ): OperationRead.Query[List[ProductRow]] =
    sql"SELECT id, name, price FROM product WHERE 1 = 1"
      .optionally(namePattern).append(" AND name ILIKE ", PgTypes.text)
      .optionally(maxPrice).append(" AND price < ", PgTypes.numeric)
      .optionally(onlyActive).append(" AND active = TRUE")
      .query(codec.all())
  // stop
