package dev.typr.foundationssc.docs.core

import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object ReadonlyTransactionMulti:
  case class User(name: String, email: String)

  val userCodec: RowCodecNamed[User] = RowCodec
    .namedBuilder[User]()
    .field("name", PgTypes.text)(_.name)
    .field("email", PgTypes.text)(_.email)
    .build(User.apply)

  private val tx: Transactor = null // placeholder

  val findAll: OperationRead[List[User]] =
    Fragment.of("SELECT name, email FROM users").query(userCodec.all())

  val countUsers: OperationRead[Long] =
    Fragment.of("SELECT count(*) FROM users").queryExactlyOne(PgTypes.int8)

  val findRecent: OperationRead[List[User]] =
    Fragment.of("SELECT name, email FROM users ORDER BY created_at DESC LIMIT 10").query(userCodec.all())

  // start
  // Multiple reads in one session — same connection, auto-commit mode
  case class Dashboard(users: List[User], count: Long, recent: List[User])

  def dashboard(): Dashboard =
    tx.transactRead { conn =>
      val users = findAll.run(conn)
      val count = countUsers.run(conn)
      val recent = findRecent.run(conn)
      Dashboard(users, count, recent)
    }
  // stop
