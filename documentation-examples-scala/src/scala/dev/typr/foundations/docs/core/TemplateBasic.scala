package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object TemplateBasic:
  case class User(id: Int, name: String, email: String)

  val userCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  var tx: Transactor = null // placeholder

  //start
  // Reusable template - SQL is fixed, values come later.
  // sql"..." cannot be used with unbound parameters;
  // the builder API provides type-safe Template construction.
  val findByEmail: Template[String, Option[User]] =
    Fragment.of(
      "SELECT id, name, email FROM users WHERE email = "
    ).param(PgTypes.text)
      .query(userCodec.maxOne())

  // Fill the template to get a concrete operation
  def findAlice(): Option[User] =
    findByEmail.on("alice@example.com").transact(tx)

  // Reuse with different values
  def findBob(): Option[User] =
    findByEmail.on("bob@example.com").transact(tx)
  //stop
