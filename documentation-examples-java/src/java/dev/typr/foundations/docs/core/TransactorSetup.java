package dev.typr.foundations.docs.core;

import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.*;

@SuppressWarnings("unused")
public class TransactorSetup {
    //start
    // PostgreSQL
    Transactor pgTx =
        PostgresConfig.builder(
                "localhost", 5432, "mydb", "user", "pass")
            .sslmode(PgSslMode.REQUIRE)
            .transactor();

    // DuckDB (in-memory)
    Transactor duckTx =
        DuckDbConfig.inMemory().transactor();

    // DuckDB (file-based)
    Transactor duckFileTx =
        DuckDbConfig.builder("/tmp/analytics.db")
            .threads(4)
            .memoryLimit("2GB")
            .transactor();

    // MariaDB / MySQL
    Transactor mariaTx =
        MariaDbConfig.builder(
                "localhost", 3306, "mydb", "user", "pass")
            .transactor();

    // Oracle
    Transactor oracleTx =
        OracleConfig.builder(
                "localhost", 1521, "xe", "user", "pass")
            .serviceName("XEPDB1")
            .transactor();

    // SQL Server
    Transactor mssqlTx =
        SqlServerConfig.builder(
                "localhost", 1433, "mydb", "user", "pass")
            .encrypt(SqlServerEncrypt.TRUE)
            .transactor();

    // DB2
    Transactor db2Tx =
        Db2Config.builder(
                "localhost", 50000, "mydb", "user", "pass")
            .transactor();
    //stop
}
