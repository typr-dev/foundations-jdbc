package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object OperationThenRecord:
  case class NewUser(id: Int, name: String)

  val newUserCodec: RowCodec[NewUser] = RowCodec
    .builder[NewUser]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(NewUser.apply)

  var tx: Transactor = null // placeholder

  // start
  // Insert and return the new user (id + name).
  def insertUser(name: String): OperationRead[NewUser] =
    Fragment
      .of("INSERT INTO users(name) VALUES(")
      .value(PgTypes.text, name)
      .append(") RETURNING id, name")
      .query(newUserCodec.exactlyOne())

  // Log the creation, taking the new user as input.
  def logCreation(user: NewUser): Operation[Int] =
    Fragment
      .of("INSERT INTO audit_log(user_id, username) VALUES(")
      .value(PgTypes.int4, user.id)
      .append(", ")
      .value(PgTypes.text, user.name)
      .append(")")
      .update()

  // Chain: insertUser → returned NewUser → logCreation.
  def insertAndLog(): Int =
    insertUser("Alice").andThen(user => logCreation(user)).transact(tx)
  // stop
