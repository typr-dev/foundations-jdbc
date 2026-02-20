package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object OptionalQueryBasic:
  case class User(id: Int, name: String, email: String)

  val userCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  var tx: Transactor = null // placeholder

  //start
  // A search with an optional name filter.
  // When present, the filter is applied; when absent, it's skipped.
  val searchUsers: Template[Option[String], List[User]] =
    Fragment.of(
      "SELECT id, name, email FROM users WHERE 1=1"
    ).optionally(
        Fragment.of(" AND name ILIKE ").param(PgTypes.text))
      .query(userCodec.all())

  // Apply filter
  def filtered(): List[User] =
    searchUsers.on(Some("%alice%")).transact(tx)

  // Skip filter - returns all users
  def all(): List[User] =
    searchUsers.on(None).transact(tx)
  //stop
