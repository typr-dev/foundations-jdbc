package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object TemplateMixed:
  case class User(id: Int, name: String, status: String)

  val userCodec: RowCodec[User] = RowCodec
    .builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.status)
    .build(User.apply)

  var tx: Transactor = null // placeholder

  // start
  // Mix bound and unbound parameters in the same template.
  // Status is fixed at "active"; name filter and limit vary per call.
  val activeUsersByName: Template.Query2[String, Int, List[User]] =
    sql"SELECT id, name, status FROM users WHERE status = "
      .value(PgTypes.text, "active")
      .append(" AND name ILIKE ")
      .param(PgTypes.text)
      .append(" ORDER BY name LIMIT ")
      .param(PgTypes.int4)
      .query(userCodec.all())

  def example(): List[User] =
    activeUsersByName.on("%alice%", 10).transact(tx)
  // stop
