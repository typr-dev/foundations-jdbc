package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object ComposingIfEmpty:
  case class User(id: Int, name: String, email: String)

  val userCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  var tx: Transactor = null // placeholder
  val email: String = "alice@example.com"
  val name: String = "Alice"

  //start
  // Find-or-create pattern
  val findUser: Template[String, Option[User]] =
    sql"SELECT id, name, email FROM users WHERE email = "
      .param(PgTypes.text)
      .query(userCodec.maxOne())

  val createUser: Template.Query2[String, String, User] =
    sql"INSERT INTO users(name, email) VALUES("
      .param(PgTypes.text).append(", ")
      .param(PgTypes.text)
      .append(") RETURNING *")
      .query(userCodec.exactlyOne())

  def findOrCreate(): User =
    Operation.ifEmpty(
      findUser.on(email),
      createUser.on(name, email)
    ).transact(tx)
  //stop
