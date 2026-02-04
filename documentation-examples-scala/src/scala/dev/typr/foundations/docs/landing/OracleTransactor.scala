package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.sql.Connection

@SuppressWarnings(Array("unused"))
object OracleTransactor:
  //start
  // Oracle - typed config, no JDBC URL to remember
  val tx: Transactor = OracleConfig.builder("localhost", 1521, "xe", "app", "secret")
    .serviceName("XEPDB1")
    .build()
    .transactor()

  // Everything inside runs in one transaction
  def getGreeting(): String =
    val op: SqlFunction[Connection, String] = conn =>
      Fragment.lit("SELECT 'Hello from Oracle' FROM dual")
        .query(RowParser.of(OracleTypes.varchar2).exactlyOne().underlying)
        .run(conn)
    tx.execute(op)

  // Built-in strategies for common patterns
  val defaultStrategy = Transactor.defaultStrategy()         // begin -> commit -> close
  val autoCommit = Transactor.autoCommitStrategy()           // no transaction, just close
  val rollbackOnError = Transactor.rollbackOnErrorStrategy() // begin -> commit, rollback on error -> close
  val test = Transactor.testStrategy()                       // begin -> rollback -> close (for tests)
  //stop
