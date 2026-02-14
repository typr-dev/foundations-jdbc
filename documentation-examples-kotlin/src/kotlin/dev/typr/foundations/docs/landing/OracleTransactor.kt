package dev.typr.foundations.docs.landing

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.connect.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class OracleTransactor {
    //start
    // Oracle - typed config, no JDBC URL to remember
    val tx = SimpleDataSource.create(OracleConfig.builder("localhost", 1521, "xe", "app", "secret")
        .serviceName("XEPDB1")
        .build()).transactor()

    // Everything inside runs in one transaction
    fun getGreeting(): String = Fragment.of("SELECT 'Hello from Oracle' FROM dual")
        .query(RowParser.of(OracleTypes.varchar2).exactlyOne())
        .transact(tx)

    //stop
}
