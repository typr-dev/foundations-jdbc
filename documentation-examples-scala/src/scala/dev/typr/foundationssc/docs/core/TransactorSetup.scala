package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.Transactor
import dev.typr.foundationssc.connect.*

@SuppressWarnings(Array("unused"))
object TransactorSetup:
  // start
  // PostgreSQL
  val pgTx =
    Transactor.create(
      PgConfig
        .builder("localhost", 5432, "mydb", "user", "pass")
        .sslmode(PgSslMode.REQUIRE)
        .build()
    )

  // DuckDB (in-memory)
  val duckTx =
    Transactor.create(DuckDbConfig.inMemory().build())

  // DuckDB (file-based)
  val duckFileTx =
    Transactor.create(
      DuckDbConfig
        .builder("/tmp/analytics.db")
        .threads(4)
        .memoryLimit("2GB")
        .build()
    )

  // MariaDB / MySQL
  val mariaTx =
    Transactor.create(
      MariaConfig
        .builder("localhost", 3306, "mydb", "user", "pass")
        .build()
    )

  // Oracle
  val oracleTx =
    Transactor.create(
      OracleConfig
        .builder("localhost", 1521, "xe", "user", "pass")
        .serviceName("XEPDB1")
        .build()
    )

  // SQL Server — .encrypt(FALSE) for local dev (self-signed cert); use TRUE + trusted cert in prod
  val mssqlTx =
    Transactor.create(
      SqlServerConfig
        .builder("localhost", 1433, "mydb", "user", "pass")
        .encrypt(SqlServerEncrypt.FALSE)
        .build()
    )

  // DB2
  val db2Tx =
    Transactor.create(
      Db2Config
        .builder("localhost", 50000, "mydb", "user", "pass")
        .build()
    )
  // stop
