package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*


@SuppressWarnings(Array("unused"))
object OptionalQueryBooleanFlags:
  case class User(id: Int, name: String, email: String)

  val userCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  var tx: Transactor = null // placeholder

  //start
  val activeUsers: Template[Boolean, List[User]] =
    sql"SELECT id, name, email FROM users WHERE 1=1"
      .optionally(sql" AND active = TRUE")
      .query(userCodec.all())

  // Include the filter
  def onlyActive(): List[User] =
    activeUsers.on(true).transact(tx)

  // Skip the filter - returns all users
  def all(): List[User] =
    activeUsers.on(false).transact(tx)
  //stop
