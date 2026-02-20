package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql

@SuppressWarnings(Array("unused"))
object OperationQueries:
  case class User(id: Int, name: String)

  val userCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(User.apply)

  val fragment: Fragment = sql"SELECT id, name FROM users"

  //start
  // Multi-column: pass a RowCodec with a result mode
  val allUsers: Operation[List[User]] = fragment.query(userCodec.all())
  val maybeUser: Operation[Option[User]] = fragment.query(userCodec.maxOne())
  val oneUser: Operation[User] = fragment.query(userCodec.exactlyOne())

  // Single-column: shorthand methods skip the codec
  val allIds: Operation[List[Int]] =
    sql"SELECT id FROM users".queryAll(PgTypes.int4)
  val maybeName: Operation[Option[String]] =
    sql"SELECT name FROM users LIMIT 1".queryMaxOne(PgTypes.text)
  val count: Operation[Int] =
    sql"SELECT count(*) FROM users".queryExactlyOne(PgTypes.int4)
  //stop
