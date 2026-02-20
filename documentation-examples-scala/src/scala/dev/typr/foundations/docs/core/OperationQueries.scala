package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object OperationQueries:
  case class User(id: Int, name: String)

  val userCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(User.apply)

  val fragment: Fragment = Fragment.of("SELECT id, name FROM users")

  //start
  // Multi-column: pass a RowCodec with a result mode
  val allUsers: Operation[List[User]] = fragment.query(userCodec.all())
  val maybeUser: Operation[Option[User]] = fragment.query(userCodec.maxOne())
  val oneUser: Operation[User] = fragment.query(userCodec.exactlyOne())

  // Single-column: shorthand methods skip the codec
  val allIds: Operation[List[Int]] =
    Fragment.of("SELECT id FROM users").queryAll(PgTypes.int4)
  val maybeName: Operation[Option[String]] =
    Fragment.of("SELECT name FROM users LIMIT 1").queryMaxOne(PgTypes.text)
  val count: Operation[Int] =
    Fragment.of("SELECT count(*) FROM users").queryExactlyOne(PgTypes.int4)
  //stop
