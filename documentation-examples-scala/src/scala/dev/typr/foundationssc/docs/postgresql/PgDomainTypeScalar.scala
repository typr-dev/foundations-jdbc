package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object PgDomainTypeScalar:
  // start
  // PG schema:  CREATE DOMAIN person_name AS varchar(100);
  case class Name(value: String)
  object Name:
    val pgType: PgType[Name] =
      PgTypes.text.asDomain("person_name", Name.apply, _.value)
  // stop
