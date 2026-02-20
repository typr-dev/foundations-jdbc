package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object TemplateFrom:
  var tx: Transactor = null // placeholder

  //start
  // A case class gives names to each template parameter
  case class InsertUser(name: String, email: String)

  // .from() maps case class fields to template params
  val insertUser: Template.From[InsertUser, Int] =
    Fragment.of("INSERT INTO users(name, email) VALUES(")
      .param(PgTypes.text)
      .append(", ")
      .param(PgTypes.text)
      .append(")")
      .update()
      .from(_.name, _.email)

  // Callers pass the case class
  def createUser(): Int =
    insertUser.on(InsertUser("Alice", "alice@example.com"))
      .transact(tx)
  //stop
