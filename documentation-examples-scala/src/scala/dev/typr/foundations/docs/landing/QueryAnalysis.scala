package dev.typr.foundations.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

import java.sql.Connection

@SuppressWarnings(Array("unused"))
object QueryAnalysisExample:
  case class User(id: Int, name: String, createdAt: Int, email: String)
  object User:
    val rowCodec: RowCodec[User] = RowCodec.builder[User]()
      .field(PgTypes.int4)(_.id)
      .field(PgTypes.text)(_.name)
      .field(PgTypes.int4)(_.createdAt)    // WRONG! Should be timestamptz
      .field(PgTypes.text)(_.email)        // nullable but not Optional!
      .build(User.apply)

  val connection: Connection = null // placeholder

  //start
  // Your query looks fine at compile time...
  val query: Operation.Query[List[User]] =
    sql"""SELECT id, name, created_at, email
          FROM users
          WHERE active = ${PgTypes.bool(true)}"""
      .query(User.rowCodec.all())

  // But Query Analysis catches the bugs in your tests
  def check(): Unit =
    val result: QueryAnalysis =
      QueryAnalyzer.analyze(query, connection).head
    if !result.succeeded() then
      throw new AssertionError(result.report())
  //stop
