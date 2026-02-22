package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
//start
object UserRepo:
  case class User(id: Int, name: String)

  val userCodec: RowCodecNamed[User] = RowCodec.namedBuilder[User]()
    .field("id", PgTypes.int4)(_.id)
    .field("name", PgTypes.text)(_.name)
    .build(User.apply)

  val selectAll: Operation[List[User]] =
    sql"SELECT ${userCodec.columnList} FROM users ORDER BY name"
      .query(userCodec.all())
      .named("UserRepo.selectAll")

  val selectById: Template[Int, Option[User]] =
    sql"SELECT ${userCodec.columnList} FROM users WHERE id = "
      .param(PgTypes.int4)
      .query(userCodec.maxOne())

//stop
