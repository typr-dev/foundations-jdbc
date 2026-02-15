package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object SqlTemplateMixed:
  case class User(id: Int, name: String, status: String)

  val userParser: RowParser[User] = RowParser.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.status)
    .build(User.apply)

  var tx: Transactor = null // placeholder

  //start
  // Mix bound and unbound parameters in the same template
  // The status is fixed at "active", but the limit varies per call
  val activeUsersWithLimit: SqlTemplate[Int, List[User]] =
    Fragment.of("SELECT id, name, status FROM users WHERE status = ")
      .value(PgTypes.text, "active")
      .append(" ORDER BY name LIMIT ")
      .param(PgTypes.int4)
      .query(userParser.all())

  @throws[SQLException]
  def topTen(): List[User] = activeUsersWithLimit.on(10).transact(tx)

  @throws[SQLException]
  def topFifty(): List[User] = activeUsersWithLimit.on(50).transact(tx)
  //stop
