package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.connect.*

@SuppressWarnings(Array("unused"))
object TransactorSetup:
  //start
  // PostgreSQL
  val pgTx =
    PostgresConfig.builder(
        "localhost", 5432, "mydb", "user", "pass")
      .sslmode(PgSslMode.REQUIRE)
      .transactor()

  // DuckDB (in-memory)
  val duckTx =
    DuckDbConfig.inMemory().transactor()

  // DuckDB (file-based)
  val duckFileTx =
    DuckDbConfig.builder("/tmp/analytics.db")
      .threads(4)
      .memoryLimit("2GB")
      .transactor()

  // MariaDB / MySQL
  val mariaTx =
    MariaDbConfig.builder(
        "localhost", 3306, "mydb", "user", "pass")
      .transactor()

  // Oracle
  val oracleTx =
    OracleConfig.builder(
        "localhost", 1521, "xe", "user", "pass")
      .serviceName("XEPDB1")
      .transactor()

  // SQL Server
  val mssqlTx =
    SqlServerConfig.builder(
        "localhost", 1433, "mydb", "user", "pass")
      .encrypt(SqlServerEncrypt.TRUE)
      .transactor()

  // DB2
  val db2Tx =
    Db2Config.builder(
        "localhost", 50000, "mydb", "user", "pass")
      .transactor()
  //stop
