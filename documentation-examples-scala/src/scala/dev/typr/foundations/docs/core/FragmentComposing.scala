package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

import java.sql.SQLException
import java.util.{List as JList}
import scala.jdk.CollectionConverters.*

@SuppressWarnings(Array("unused"))
object FragmentComposing:
  case class ProductRow(id: Int, name: String, price: BigDecimal)

  val rowParser: RowParser[ProductRow] = RowParser.builder[ProductRow]()
    .field(PgTypes.int4, _.id)
    .field(PgTypes.text, _.name)
    .field(PgTypes.numeric, _.price)
    .build(ProductRow.apply)

  var tx: Transactor = null // placeholder
  val maxPrice: Option[BigDecimal] = Some(BigDecimal("100"))

  //start
  // Build small reusable filters
  def byName(name: String): Fragment =
    Fragment.interpolate("name ILIKE ").param(PgTypes.text, name).done()

  def cheaperThan(max: BigDecimal): Fragment =
    Fragment.interpolate("price < ").param(PgTypes.numeric, max).done()

  // Compose dynamically - only include the filters that are present
  @throws[SQLException]
  def query(): List[ProductRow] =
    val filters: JList[Fragment] = Seq(
      Some(byName("%widget%")),
      maxPrice.map(cheaperThan)
    ).flatten.asJava

    Fragment.lit("SELECT * FROM product ")
      .append(Fragment.whereAnd(filters))
      .query(rowParser.all().underlying)
      .transact(tx)
  //stop
