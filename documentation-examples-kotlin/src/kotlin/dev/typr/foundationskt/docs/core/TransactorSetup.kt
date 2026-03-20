package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.Transactor
import dev.typr.foundationskt.connect.*

@Suppress("unused")
class TransactorSetup {
    //start
    // PostgreSQL
    val pgTx =
        Transactor.create(
            PostgresConfig.builder(
                    "localhost", 5432, "mydb", "user", "pass")
                .sslmode(PgSslMode.REQUIRE)
                .build())

    // DuckDB (in-memory)
    val duckTx =
        Transactor.create(DuckDbConfig.inMemory().build())

    // DuckDB (file-based)
    val duckFileTx =
        Transactor.create(
            DuckDbConfig.builder("/tmp/analytics.db")
                .threads(4)
                .memoryLimit("2GB")
                .build())

    // MariaDB / MySQL
    val mariaTx =
        Transactor.create(
            MariaDbConfig.builder(
                    "localhost", 3306, "mydb", "user", "pass")
                .build())

    // Oracle
    val oracleTx =
        Transactor.create(
            OracleConfig.builder(
                    "localhost", 1521, "xe", "user", "pass")
                .serviceName("XEPDB1")
                .build())

    // SQL Server
    val mssqlTx =
        Transactor.create(
            SqlServerConfig.builder(
                    "localhost", 1433, "mydb", "user", "pass")
                .encrypt(SqlServerEncrypt.TRUE)
                .build())

    // DB2
    val db2Tx =
        Transactor.create(
            Db2Config.builder(
                    "localhost", 50000, "mydb", "user", "pass")
                .build())
    //stop
}
