package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*


import java.sql.Connection
import java.time.Instant

@SuppressWarnings(Array("unused"))
object FragmentBuilding:
  case class User(id: Int, name: String, status: String, createdAt: Instant)

  val userCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.status)
    .field(PgTypes.timestamptz)(_.createdAt)
    .build(User.apply)

  var connection: Connection = null // placeholder
  val userId: Integer = 1
  val cutoffDate: Instant = Instant.now()

  //start
  val query: Fragment =
    sql"""SELECT * FROM users
          WHERE id = ${PgTypes.int4(userId)}
          AND status = ${PgTypes.text("active")}
          AND created_at > ${PgTypes.timestamptz(cutoffDate)}"""

  // Parameters are bound, not interpolated
  val users: List[User] =
    query.query(userCodec.all()).run(connection)
  //stop
