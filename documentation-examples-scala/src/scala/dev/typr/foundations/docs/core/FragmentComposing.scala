package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object FragmentComposing:
  case class ProductRow(id: Int, name: String, price: BigDecimal)

  val rowParser: RowParser[ProductRow] = RowParser.builder[ProductRow]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.numeric)(_.price)
    .build(ProductRow.apply)

  var tx: Transactor = null // placeholder
  val maxPrice: Option[BigDecimal] = Some(BigDecimal("100"))

  //start
  // Build small reusable filters
  def byName(name: String): Fragment =
    sql"name ILIKE ${Fragment.encode(PgTypes.text, name)}"

  def cheaperThan(max: BigDecimal): Fragment =
    sql"price < ${Fragment.encode(PgTypes.numeric, max)}"

  // Compose dynamically - only include the filters that are present
  @throws[SQLException]
  def query(): List[ProductRow] =
    val filters: List[Fragment] = List(
      Some(byName("%widget%")),
      maxPrice.map(cheaperThan)
    ).flatten

    sql"SELECT * FROM product ${Fragment.whereAnd(filters)}"
      .query(rowParser.all())
      .transact(tx)
  //stop
