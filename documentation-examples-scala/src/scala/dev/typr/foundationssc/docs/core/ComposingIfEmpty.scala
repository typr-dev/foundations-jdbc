package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ComposingIfEmpty:
  case class User(id: Int, name: String, email: String)

  val userCodec: RowCodec[User] = RowCodec
    .builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  var tx: Transactor = null // placeholder
  val email: String = "alice@example.com"
  val name: String = "Alice"

  // start
  // Find-or-create pattern
  def findUser(email: String): OperationRead[Option[User]] =
    Fragment
      .of("SELECT id, name, email FROM users WHERE email = ")
      .value(PgTypes.text, email)
      .query(userCodec.maxOne())

  def createUser(name: String, email: String): OperationRead[User] =
    Fragment
      .of("INSERT INTO users(name, email) VALUES(")
      .value(PgTypes.text, name)
      .append(", ")
      .value(PgTypes.text, email)
      .append(") RETURNING *")
      .query(userCodec.exactlyOne())

  def findOrCreate(): User =
    OperationRead
      .ifEmpty(findUser(email), createUser(name, email))
      .transact(tx)
  // stop
