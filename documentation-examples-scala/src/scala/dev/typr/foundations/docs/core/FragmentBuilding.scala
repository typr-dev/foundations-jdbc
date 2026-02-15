package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*


import java.sql.Connection
import java.time.Instant

@SuppressWarnings(Array("unused"))
object FragmentBuilding:
  case class User(id: Int, name: String, status: String, createdAt: Instant)

  val userParser: RowParser[User] = RowParser.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.status)
    .field(PgTypes.timestamptz)(_.createdAt)
    .build(User.apply)

  var connection: Connection = null // placeholder
  val userId: Integer = 1
  val cutoffDate: Instant = Instant.now()

  //start
  val id = Fragment.encode(PgTypes.int4, userId)
  val status = Fragment.encode(PgTypes.text, "active")
  val cutoff = Fragment.encode(PgTypes.timestamptz, cutoffDate)

  val query: Fragment =
    sql"SELECT * FROM users WHERE id = $id AND status = $status AND created_at > $cutoff"

  // Execute safely - parameters are bound, not interpolated
  val users: List[User] = query.query(userParser.all()).run(connection)
  //stop
