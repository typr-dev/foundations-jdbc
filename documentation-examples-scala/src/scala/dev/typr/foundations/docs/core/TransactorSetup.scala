package dev.typr.foundations.docs.core

import dev.typr.foundations.{Fragment, PgTypes, SqlFunction, Transactor}
import dev.typr.foundations.scala.RowParser
import dev.typr.foundations.connect.ConnectionSource
import java.math.BigDecimal
import java.sql.{Connection, SQLException}

@SuppressWarnings(Array("unused"))
object TransactorSetup:
  case class ProductRow(id: Int, name: String, price: BigDecimal)

  val rowParser: RowParser[ProductRow] = RowParser.builder[ProductRow]()
    .field(PgTypes.int4, _.id)
    .field(PgTypes.text, _.name)
    .field(PgTypes.numeric, _.price)
    .build(ProductRow.apply)

  var connectionSource: ConnectionSource = null // placeholder
  val minPrice: BigDecimal = new BigDecimal("10")

  //start
  // The Transactor manages connections and transactions
  // You choose the strategy - it handles the lifecycle
  @throws[SQLException]
  def query(): List[ProductRow] =
    val tx: Transactor = connectionSource.transactor(Transactor.defaultStrategy())

    // Everything inside runs in one transaction: begin, commit, close
    val op: SqlFunction[Connection, List[ProductRow]] = conn =>
      Fragment.interpolate("SELECT * FROM product WHERE price > ")
        .param(PgTypes.numeric, minPrice)
        .done()
        .query(rowParser.all().underlying)
        .runUnchecked(conn)
    tx.execute(op)
  //stop
