package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object PgDomainTypeArray:
  // start
  // Arrays of domains "just work" — `.array` composes after `.asDomain(...)`. Use
  // `.transform(...)` at the list level to map the container to a different wrapper type
  // without changing the schema.
  case class Name(value: String)
  object Name:
    val pgType: PgType[Name] =
      PgTypes.text.transform(Name.apply, _.value).asDomain("person_name")

  case class MiddleName(value: Name)

  val middleNames: PgType[List[MiddleName]] =
    Name.pgType.array.transform(
      ns => ns.map(MiddleName.apply),
      ms => ms.map(_.value)
    )
  // stop
