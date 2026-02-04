package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryAnalysis:
  case class User(id: Int, name: String, createdAt: Int, email: String)
  val connection: Connection = null // placeholder

  //start
  // Your query looks fine at compile time...
  val query: Operation.Query[List[User]] = Fragment.interpolate("SELECT id, name, created_at, email FROM users WHERE active = ")
    .param(PgTypes.bool, true)
    .done()
    .query(RowParser.builder[User]()
      .field(PgTypes.int4, _.id)           // id: correct
      .field(PgTypes.text, _.name)         // name: correct
      .field(PgTypes.int4, _.createdAt)    // created_at: WRONG! Should be timestamptz
      .field(PgTypes.text, _.email)        // email: nullable but not Optional!
      .build(User.apply)
      .all().underlying)

  // But Query Analysis catches the bugs in your tests
  def check(): Unit =
    val result: QueryAnalysis = QueryAnalyzer.analyze(query, connection)
    if !result.succeeded() then
      throw new AssertionError(result.report())  // Fails with the detailed report
  //stop
