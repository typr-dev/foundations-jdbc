package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

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
  // Transactor manages connections and transactions
  @throws[SQLException]
  def query(): List[ProductRow] =
    val tx =
      connectionSource.transactor(Transactor.defaultStrategy())

    sql"SELECT * FROM product WHERE price > ${PgTypes.numeric(minPrice)}"
      .query(rowParser.all())
      .transact(tx)
  //stop
