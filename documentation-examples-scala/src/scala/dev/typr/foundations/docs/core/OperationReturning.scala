package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object OperationReturning:
  case class User(id: Int, name: String)

  val userCodec: RowCodec[User] = RowCodec.builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(User.apply)

  //start
  // INSERT ... RETURNING id, name
  val insertedUsers: Operation[List[User]] =
    Fragment.of("INSERT INTO users (name) VALUES ('alice') RETURNING id, name")
      .updateReturning(userCodec.all())

  val insertedUser: Operation[User] =
    Fragment.of("INSERT INTO users (name) VALUES ('alice') RETURNING id, name")
      .updateReturning(userCodec.exactlyOne())
  //stop
