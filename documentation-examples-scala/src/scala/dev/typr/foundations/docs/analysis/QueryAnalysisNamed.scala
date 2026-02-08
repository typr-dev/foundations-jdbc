package dev.typr.foundations.docs.analysis
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryAnalysisNamed:
  case class User(id: Int, name: String, email: String)

  private val connection: Connection = null // placeholder
  private val userId = 1

  private val userRowParser: RowParser[User] = RowParser.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  //start
  def analyzeNamedQuery(): Unit =
    val query = sql"SELECT id, name, email FROM users WHERE id = ${Fragment.encode(PgTypes.int4, userId)}"
      .query(userRowParser.all())

    // Give your query a name - it shows up in the error report
    val analysis = QueryAnalyzer.analyze("findUserById", query, connection)

    if !analysis.succeeded() then
      throw AssertionError(analysis.report())
  //stop
