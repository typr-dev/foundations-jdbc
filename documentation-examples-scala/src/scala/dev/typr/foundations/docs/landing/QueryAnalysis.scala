package dev.typr.foundations.docs.landing

import dev.typr.foundations.{Fragment, PgTypes}
import dev.typr.foundations.scala.RowParser
import dev.typr.foundations.analysis.{QueryAnalysis => QA, QueryAnalyzer}
import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryAnalysis:
  case class User(id: Int, name: String, createdAt: Int, email: String)
  val connection: Connection = null // placeholder

  //start
  // Your query looks fine at compile time...
  val query = Fragment.interpolate("SELECT id, name, created_at, email FROM users WHERE active = ")
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
    val analysis: QA = QueryAnalyzer.analyze(query, connection)
    if !analysis.succeeded() then
      throw new AssertionError(analysis.report())  // Fails with the detailed report
  //stop
