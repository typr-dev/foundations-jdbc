package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object TemplateThenFrom:
  case class NewUser(id: Int, name: String)

  val newUserCodec: RowCodec[NewUser] = RowCodec.builder[NewUser]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(NewUser.apply)

  var tx: Transactor = null // placeholder

  //start
  // 1-param template: insert user, return id and name
  val insertUser: Template[String, NewUser] =
    sql"INSERT INTO users(name) VALUES("
      .param(PgTypes.text)
      .append(") RETURNING id, name")
      .query(newUserCodec.exactlyOne())

  // 2-param template: log the creation with both fields
  val logCreation: Template.Update2[Int, String] =
    sql"INSERT INTO audit_log(user_id, username) VALUES("
      .param(PgTypes.int4)
      .append(", ")
      .param(PgTypes.text)
      .append(")")
      .update()

  // Chain: .from() adapts the 2-param template to accept NewUser
  def insertAndLog(): Int =
    insertUser.on("Alice")
      .andThen(logCreation.from(_.id, _.name))
      .transact(tx)
  //stop
