package dev.typr.foundations.docs.postgresql
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object DomainType:
  //start
  // Wrapper type
  case class Email(value: String)

  // Create PgType from text
  val emailType: PgType[Email] = PgTypes.text.bimap(Email.apply, _.value)
  //stop
