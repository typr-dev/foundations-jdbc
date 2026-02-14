package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

@SuppressWarnings(Array("unused"))
object OracleTransactor:
  //start
  // Oracle - typed config, no JDBC URL to remember
  val tx = SimpleDataSource.create(OracleConfig.builder("localhost", 1521, "xe", "app", "secret")
    .serviceName("XEPDB1")
    .build()).transactor()

  // Everything inside runs in one transaction
  def getGreeting(): String =
    sql"SELECT 'Hello from Oracle' FROM dual"
      .query(RowParser.of(OracleTypes.varchar2).exactlyOne())
      .transact(tx)

  //stop
