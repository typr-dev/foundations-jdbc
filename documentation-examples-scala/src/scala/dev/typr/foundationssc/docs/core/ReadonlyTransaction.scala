package dev.typr.foundationssc.docs.core

import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object ReadonlyTransaction:
  case class User(name: String, email: String)

  val userCodec: RowCodecNamed[User] = RowCodec
    .namedBuilder[User]()
    .field("name", PgTypes.text)(_.name)
    .field("email", PgTypes.text)(_.email)
    .build(User.apply)

  private val tx: Transactor = null // placeholder

  val findAll: OperationRead[List[User]] =
    Fragment.of("SELECT name, email FROM users").query(userCodec.all())

  // start
  // Single read operation — no transaction overhead
  def allUsers(): List[User] =
    findAll.transactRead(tx)
  // stop
