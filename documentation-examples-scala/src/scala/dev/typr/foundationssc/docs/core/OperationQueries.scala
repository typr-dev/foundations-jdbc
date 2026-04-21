package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql

@SuppressWarnings(Array("unused"))
object OperationQueries:
  case class User(id: Int, name: String)

  val userCodec: RowCodec[User] = RowCodec
    .builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(User.apply)

  val fragment: Fragment = sql"SELECT id, name FROM users"

  // start
  // Multi-column: pass a RowCodec with a result mode
  val allUsers: OperationRead[List[User]] = fragment.query(userCodec.all())
  val maybeUser: OperationRead[Option[User]] = fragment.query(userCodec.maxOne())
  val oneUser: OperationRead[User] = fragment.query(userCodec.exactlyOne())

  // Single-column: shorthand methods skip the codec
  val allIds: OperationRead[List[Int]] =
    sql"SELECT id FROM users".queryAll(PgTypes.int4)
  val maybeName: OperationRead[Option[String]] =
    sql"SELECT name FROM users LIMIT 1".queryMaxOne(PgTypes.text)
  val count: OperationRead[Int] =
    sql"SELECT count(*) FROM users".queryExactlyOne(PgTypes.int4)
  // stop
