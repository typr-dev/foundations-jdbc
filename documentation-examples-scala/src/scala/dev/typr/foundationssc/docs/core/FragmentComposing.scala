package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FragmentComposing:
  case class ProductRow(id: Int, name: String, price: BigDecimal)

  val rowCodec: RowCodec[ProductRow] = RowCodec
    .builder[ProductRow]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.numeric)(_.price)
    .build(ProductRow.apply)

  var tx: Transactor = null // placeholder
  val namePattern: Option[String] = Some("%widget%")
  val maxPrice: Option[BigDecimal] = Some(BigDecimal("100"))

  // start
  // Compose dynamic filters with the `optionally` DSL. Each `.optionally().append(...)`
  // is a branch point Query Analysis verifies against the schema, even when
  // the runtime never takes that branch at this call site.
  def query(): List[ProductRow] =
    sql"SELECT id, name, price FROM product WHERE 1 = 1"
      .optionally(namePattern).append(" AND name ILIKE ", PgTypes.text)
      .optionally(maxPrice).append(" AND price < ", PgTypes.numeric)
      .query(rowCodec.all())
      .transact(tx)
  // stop
