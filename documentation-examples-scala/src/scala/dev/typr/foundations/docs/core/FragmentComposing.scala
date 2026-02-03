package dev.typr.foundations.docs.core

import dev.typr.foundations.{Fragment, PgTypes, Transactor}
import dev.typr.foundations.scala.RowParser
import java.math.BigDecimal
import java.sql.SQLException
import java.util.{List as JList, Optional}
import scala.jdk.OptionConverters.*
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
  val maxPrice: Optional[BigDecimal] = Optional.of(new BigDecimal("100"))

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
      maxPrice.toScala.map(cheaperThan)
    ).flatten.asJava

    Fragment.lit("SELECT * FROM product ")
      .append(Fragment.whereAnd(filters))
      .query(rowParser.all().underlying)
      .transact(tx)
  //stop
