package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object DomainType:
  //start
  // Wrapper type
  case class Email(value: String)

  // Create PgType from text
  val emailType: PgType[Email] = PgTypes.text.transform(Email.apply, _.value)
  //stop
