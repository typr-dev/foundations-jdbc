package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object OptionalQueryFacade:
  case class User(id: Int, name: String, email: String)

  val userCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  var tx: Transactor = null // placeholder

  //start
  // Package filters into a case class so callers see a clean API
  case class UserSearch(
    name: Option[String],
    email: Option[String],
    activeOnly: Boolean
  )

  // .from() maps getters to template params
  private val searchTemplate: Template.From[UserSearch, List[User]] =
    sql"SELECT id, name, email FROM users WHERE 1=1"
      .optionally(
        sql" AND name ILIKE ".param(PgTypes.text))
      .optionally(
        sql" AND email ILIKE ".param(PgTypes.text))
      .optionally(
        sql" AND active = TRUE")
      .append(" ORDER BY name")
      .query(userCodec.all())
      .from(_.name, _.email, _.activeOnly)

  // Callers just pass the case class
  def searchUsers(search: UserSearch): List[User] =
    searchTemplate.on(search).transact(tx)

  def example(): List[User] =
    val search = UserSearch(
      name = Some("%alice%"), email = None, activeOnly = true)
    searchUsers(search)
  //stop
