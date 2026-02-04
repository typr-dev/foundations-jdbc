package dev.typr.foundations.docs.analysis
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryAnalysisBasic:
  case class User(id: Int, name: String, email: String)

  private val connection: Connection = null // placeholder
  private val userId = 1

  private val userRowParser: RowParser[User] = RowParser.builder[User]()
    .field(PgTypes.int4, _.id)
    .field(PgTypes.text, _.name)
    .field(PgTypes.text, _.email)
    .build(User.apply)

  //start
  def analyzeQuery(): Unit =
    // Build your query as normal
    val query = Fragment.interpolate("SELECT id, name, email FROM users WHERE id = ")
      .param(PgTypes.int4, userId)
      .done()
      .query(userRowParser.all().underlying)

    // Analyze it against the database
    val analysis: QueryAnalysis = QueryAnalyzer.analyze(query, connection)

    // Check the results
    if !analysis.succeeded() then
      throw AssertionError(analysis.report())
  //stop
