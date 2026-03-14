package dev.typr.foundations.docs.core;

import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.*;

@SuppressWarnings("unused")
public class TransactorSetup {
    //start
    // PostgreSQL
    Transactor pgTx =
        Transactor.create(
            PostgresConfig.builder(
                    "localhost", 5432, "mydb", "user", "pass")
                .sslmode(PgSslMode.REQUIRE)
                .build());

    // DuckDB (in-memory)
    Transactor duckTx =
        Transactor.create(DuckDbConfig.inMemory().build());

    // DuckDB (file-based)
    Transactor duckFileTx =
        Transactor.create(
            DuckDbConfig.builder("/tmp/analytics.db")
                .threads(4)
                .memoryLimit("2GB")
                .build());

    // MariaDB / MySQL
    Transactor mariaTx =
        Transactor.create(
            MariaDbConfig.builder(
                    "localhost", 3306, "mydb", "user", "pass")
                .build());

    // Oracle
    Transactor oracleTx =
        Transactor.create(
            OracleConfig.builder(
                    "localhost", 1521, "xe", "user", "pass")
                .serviceName("XEPDB1")
                .build());

    // SQL Server
    Transactor mssqlTx =
        Transactor.create(
            SqlServerConfig.builder(
                    "localhost", 1433, "mydb", "user", "pass")
                .encrypt(SqlServerEncrypt.TRUE)
                .build());

    // DB2
    Transactor db2Tx =
        Transactor.create(
            Db2Config.builder(
                    "localhost", 50000, "mydb", "user", "pass")
                .build());
    //stop
}
