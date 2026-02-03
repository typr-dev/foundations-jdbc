package dev.typr.foundations.docs.landing

import dev.typr.foundations.Fragment
import dev.typr.foundations.OracleTypes
import dev.typr.foundations.kotlin.RowParser
import dev.typr.foundations.kotlin.query
import dev.typr.foundations.SqlFunction
import dev.typr.foundations.Transactor
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
    val defaultStrategy = Transactor.defaultStrategy()         // begin -> commit -> close
    val autoCommit = Transactor.autoCommitStrategy()           // no transaction, just close
    val rollbackOnError = Transactor.rollbackOnErrorStrategy() // begin -> commit, rollback on error -> close
    val test = Transactor.testStrategy()                       // begin -> rollback -> close (for tests)
    //stop
}
