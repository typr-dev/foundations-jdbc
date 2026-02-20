package dev.typr.foundationssc.docs.analysis
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*


import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryAnalysisBasic:
  case class User(id: Int, name: String, email: String)

  private val connection: Connection = null // placeholder
  private val userId = 1

  private val userRowCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  //start
  def analyzeQuery(): Unit =
    // Build your query as normal
    val query =
      sql"""SELECT id, name, email
            FROM users
            WHERE id = ${PgTypes.int4(userId)}"""
        .query(userRowCodec.all())

    // Analyze it against the database
    val analysis: QueryAnalysis =
      QueryAnalyzer.analyze(query, connection).head

    // Check the results
    if !analysis.succeeded() then
      throw AssertionError(analysis.report())
  //stop
