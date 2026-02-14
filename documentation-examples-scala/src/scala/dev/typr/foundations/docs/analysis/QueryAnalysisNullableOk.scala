package dev.typr.foundations.docs.analysis
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryAnalysisNullableOk:
  private val connection: Connection = null // placeholder

  //start
  case class OrderRow(userId: Int, userName: String, orderTotal: BigDecimal)

  // The LEFT JOIN makes o.total nullable in the result set,
  // but .nullableOk() tells analysis we'll handle it
  val orderParser: RowParser[OrderRow] = RowParser.builder[OrderRow]()
    .field(PgTypes.int4)(_.userId)
    .field(PgTypes.text)(_.userName)
    .field(PgTypes.numeric.nullableOk())(_.orderTotal)
    .build(OrderRow.apply)

  def analyzeLeftJoin(): Unit =
    val query = sql"""SELECT u.id, u.name, o.total
      FROM users u
      LEFT JOIN orders o ON u.id = o.user_id"""
      .query(orderParser.all())

    val analysis: QueryAnalysis = QueryAnalyzer.analyze(query, connection).head
    if !analysis.succeeded() then
      throw AssertionError(analysis.report())
  //stop
