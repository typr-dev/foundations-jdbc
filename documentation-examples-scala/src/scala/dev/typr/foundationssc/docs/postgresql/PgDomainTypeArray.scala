package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object PgDomainTypeArray:
  // start
  // Wrap once at the scalar level — the array codec carries the wrapper through
  // .array, so no list-level bijection is needed.
  case class Name(value: String)
  object Name:
    val pgType: PgType[Name] =
      PgTypes.text.asDomain("person_name", Name.apply, _.value)
    val pgArrayType: PgType[List[Name]] = pgType.array
  // stop
