package dev.typr.foundations.docs.core;

import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.*;

@SuppressWarnings("unused")
public class TransactorSetup {
  // start
  // PostgreSQL
  Transactor pgTx =
      Transactor.create(
          PgConfig.builder("localhost", 5432, "mydb", "user", "pass")
              .sslmode(PgSslMode.REQUIRE)
              .build());

  // DuckDB (in-memory)
  Transactor duckTx = Transactor.create(DuckDbConfig.inMemory().build());

  // DuckDB (file-based)
  Transactor duckFileTx =
      Transactor.create(
          DuckDbConfig.builder("/tmp/analytics.db").threads(4).memoryLimit("2GB").build());

  // MariaDB / MySQL
  Transactor mariaTx =
      Transactor.create(MariaConfig.builder("localhost", 3306, "mydb", "user", "pass").build());

  // Oracle
  Transactor oracleTx =
      Transactor.create(
          OracleConfig.builder("localhost", 1521, "xe", "user", "pass")
              .serviceName("XEPDB1")
              .build());

  // SQL Server — .encrypt(FALSE) is correct for local dev against the default container image
  // (self-signed cert, no trust chain). Production should use .encrypt(TRUE) with a trusted
  // certificate; the handshake failure against localhost is otherwise inscrutable.
  Transactor mssqlTx =
      Transactor.create(
          SqlServerConfig.builder("localhost", 1433, "mydb", "user", "pass")
              .encrypt(SqlServerEncrypt.FALSE)
              .build());

  // DB2
  Transactor db2Tx =
      Transactor.create(Db2Config.builder("localhost", 50000, "mydb", "user", "pass").build());
  // stop
}
