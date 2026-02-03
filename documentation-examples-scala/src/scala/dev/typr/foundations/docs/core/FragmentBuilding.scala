package dev.typr.foundations.docs.core

import dev.typr.foundations.{Fragment, PgTypes}
import dev.typr.foundations.scala.RowParser
import java.sql.Connection
import java.time.Instant

@SuppressWarnings(Array("unused"))
object FragmentBuilding:
  case class User(id: Int, name: String, status: String, createdAt: Instant)

  val userParser: RowParser[User] = RowParser.builder[User]()
    .field(PgTypes.int4, _.id)
    .field(PgTypes.text, _.name)
    .field(PgTypes.text, _.status)
    .field(PgTypes.timestamptz, _.createdAt)
    .build(User.apply)

  var connection: Connection = null // placeholder
  val userId: Integer = 1
  val cutoffDate: Instant = Instant.now()

  //start
  val query: Fragment = Fragment.interpolate("SELECT * FROM users WHERE id = ")
    .param(PgTypes.int4, userId)
    .sql(" AND status = ")
    .param(PgTypes.text, "active")
    .sql(" AND created_at > ")
    .param(PgTypes.timestamptz, cutoffDate)
    .done()

  // Execute safely - parameters are bound, not interpolated
  val users: List[User] = query.query(userParser.all().underlying).runUnchecked(connection)
  //stop
