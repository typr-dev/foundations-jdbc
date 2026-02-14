package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryAnalysis:
  case class User(id: Int, name: String, createdAt: Int, email: String)
  object User:
    val rowParser: RowParser[User] = RowParser.builder[User]()
      .field(PgTypes.int4)(_.id)
      .field(PgTypes.text)(_.name)
      .field(PgTypes.int4)(_.createdAt)    // WRONG! Should be timestamptz
      .field(PgTypes.text)(_.email)        // nullable but not Optional!
      .build(User.apply)

  val connection: Connection = null // placeholder

  //start
  // Your query looks fine at compile time...
  val active = Fragment.encode(PgTypes.bool, true)
  val query: Operation.Query[List[User]] =
    sql"SELECT id, name, created_at, email FROM users WHERE active = $active"
      .query(User.rowParser.all())

  // But Query Analysis catches the bugs in your tests
  def check(): Unit =
    val result: QueryAnalysis = QueryAnalyzer.analyze(query, connection).head
    if !result.succeeded() then
      throw new AssertionError(result.report())
  //stop
