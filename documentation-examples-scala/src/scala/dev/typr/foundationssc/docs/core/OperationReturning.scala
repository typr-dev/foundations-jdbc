package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql

@SuppressWarnings(Array("unused"))
object OperationReturning:
  case class User(id: Int, name: String)

  val userCodec: RowCodec[User] = RowCodec
    .builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(User.apply)

  // start
  // INSERT ... RETURNING id, name
  val insertedUsers: Operation[List[User]] =
    sql"INSERT INTO users (name) VALUES ('alice') RETURNING id, name"
      .updateReturning(userCodec.all())

  val insertedUser: Operation[User] =
    sql"INSERT INTO users (name) VALUES ('alice') RETURNING id, name"
      .updateReturning(userCodec.exactlyOne())

  // For databases that use generated keys instead of RETURNING (SQL Server, MariaDB)
  val generatedId: Operation[Int] =
    sql"INSERT INTO users (name) VALUES ('alice')"
      .updateReturningGeneratedKeys(Array("id"), RowCodec.of(PgTypes.int4).exactlyOne())
  // stop
