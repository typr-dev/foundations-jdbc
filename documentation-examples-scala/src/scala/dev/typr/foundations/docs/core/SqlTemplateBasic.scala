package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object SqlTemplateBasic:
  case class User(id: Int, name: String, email: String)

  val userParser: RowParser[User] = RowParser.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  var tx: Transactor = null // placeholder

  //start
  // Define a reusable template - SQL structure is fixed, values come later.
  // The sql"..." interpolator cannot be used here because unbound parameters
  // require the builder API for type-safe SqlTemplate construction.
  val findByEmail: SqlTemplate[String, Option[User]] =
    Fragment.of("SELECT id, name, email FROM users WHERE email = ")
      .param(PgTypes.text)
      .query(userParser.maxOne())

  // Fill the template with a value to get a concrete operation
  @throws[SQLException]
  def findAlice(): Option[User] = findByEmail.on("alice@example.com").transact(tx)

  // Reuse the same template with different values
  @throws[SQLException]
  def findBob(): Option[User] = findByEmail.on("bob@example.com").transact(tx)
  //stop
