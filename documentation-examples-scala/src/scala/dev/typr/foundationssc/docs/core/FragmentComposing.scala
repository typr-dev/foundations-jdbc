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
  val maxPrice: Option[BigDecimal] = Some(BigDecimal("100"))

  // start
  // Build small reusable filters
  def byName(name: String): Fragment =
    sql"name ILIKE ${PgTypes.text(name)}"

  def cheaperThan(max: BigDecimal): Fragment =
    sql"price < ${PgTypes.numeric(max)}"

  // Compose dynamically
  def query(): List[ProductRow] =
    val filters: List[Fragment] =
      List(
        Some(byName("%widget%")),
        maxPrice.map(cheaperThan)
      ).flatten

    tx.transact { conn =>
      sql"SELECT * FROM product ${Fragment.whereAnd(filters)}"
        .query(rowCodec.all())
        .run(conn)
    }
  // stop
