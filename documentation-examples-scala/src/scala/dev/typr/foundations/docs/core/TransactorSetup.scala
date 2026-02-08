package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object TransactorSetup:
  case class ProductRow(id: Int, name: String, price: BigDecimal)

  val rowParser: RowParser[ProductRow] = RowParser.builder[ProductRow]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.numeric)(_.price)
    .build(ProductRow.apply)

  var connectionSource: ConnectionSource = null // placeholder
  val minPrice: BigDecimal = BigDecimal("10")

  //start
  // The Transactor manages connections and transactions
  // You choose the strategy - it handles the lifecycle
  @throws[SQLException]
  def query(): List[ProductRow] =
    val tx: Transactor = connectionSource.transactor(Transactor.defaultStrategy())

    // Everything inside runs in one transaction: begin, commit, close
    sql"SELECT * FROM product WHERE price > ${Fragment.encode(PgTypes.numeric, minPrice)}"
      .query(rowParser.all())
      .transact(tx)
  //stop
