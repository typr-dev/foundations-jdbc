package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object OptionalQueryMulti:
  case class User(id: Int, name: String, email: String)

  val userParser: RowParser[User] = RowParser.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  var tx: Transactor = null // placeholder
  var checker: QueryChecker = null // placeholder

  //start
  // Multiple optional filters - each independently present or absent
  val search: SqlTemplate.Query3[Option[String], Option[String], Boolean, List[User]] =
    Fragment.of(
      "SELECT id, name, email FROM users WHERE 1=1"
    ).optionally(
        Fragment.of(" AND name ILIKE ").param(PgTypes.text))
      .optionally(
        Fragment.of(" AND email ILIKE ").param(PgTypes.text))
      .optionally(
        Fragment.of(" AND active = TRUE"))
      .append(" ORDER BY name")
      .query(userParser.all())

  // Each combination is type-safe
  def example(): List[User] =
    search.on(Some("%alice%"), None, true).transact(tx)

  // Query analysis expands all 2^3 = 8 combinations automatically
  def verifyAllVariants(): Unit =
    checker.check(search)
  //stop
