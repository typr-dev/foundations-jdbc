package dev.typr.foundationssc.docs.dynamic
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ListBased:
  case class ProductRow(id: Int, name: String, price: BigDecimal)

  val codec: RowCodec[ProductRow] = RowCodec
    .builder[ProductRow]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.numeric)(_.price)
    .build(ProductRow.apply)

  // start
  // Build a list at runtime, then join with `Fragment.whereAnd`. Query Analysis
  // sees only the SQL shape constructed at scan time — runtime variants that
  // the test never built are not checked.
  def search(
      namePattern: Option[String],
      maxPrice: Option[BigDecimal]
  ): OperationRead.Query[List[ProductRow]] =
    val filters: List[Fragment] = List(
      namePattern.map(p => sql"name ILIKE ${PgTypes.text(p)}"),
      maxPrice.map(p => sql"price < ${PgTypes.numeric(p)}")
    ).flatten

    sql"SELECT id, name, price FROM product ${Fragment.whereAnd(filters)}"
      .query(codec.all())
  // stop
