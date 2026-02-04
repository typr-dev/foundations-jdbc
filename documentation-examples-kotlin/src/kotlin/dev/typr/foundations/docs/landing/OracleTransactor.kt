package dev.typr.foundations.docs.landing

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.connect.oracle.OracleConfig
import java.sql.Connection

@Suppress("unused")
class OracleTransactor {
    //start
    // Oracle - typed config, no JDBC URL to remember
    val tx: Transactor = OracleConfig.builder("localhost", 1521, "xe", "app", "secret")
        .serviceName("XEPDB1")
        .build()
        .transactor()

    // Everything inside runs in one transaction
    fun getGreeting(): String = tx.execute(SqlFunction { conn: Connection ->
        Fragment.lit("SELECT 'Hello from Oracle' FROM dual")
            .query(RowParser.of(OracleTypes.varchar2).exactlyOne())
            .run(conn)
    })

    // Built-in strategies for common patterns
    val defaultStrategy: Strategy = Transactor.defaultStrategy()         // begin -> commit -> close
    val autoCommit: Strategy = Transactor.autoCommitStrategy()           // no transaction, just close
    val rollbackOnError: Strategy = Transactor.rollbackOnErrorStrategy() // begin -> commit, rollback on error -> close
    val test: Strategy = Transactor.testStrategy()                       // begin -> rollback -> close (for tests)
    //stop
}
