package dev.typr.foundationssc.docs.analysis
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object QueryAnalysisBasic:
  case class User(id: Int, name: String, email: String)

  private val transactor: Transactor = null // placeholder

  private val userRowCodec: RowCodec[User] = RowCodec
    .builder[User]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.text)(_.email)
    .build(User.apply)

  // start
  def checkQueryManually(): Unit =
    val query =
      sql"""SELECT id, name, email
            FROM users
            WHERE id = ${PgTypes.int4(1)}"""
        .query(userRowCodec.all())

    val checker = QueryChecker.create(transactor)
    checker.check(query)
  // stop
