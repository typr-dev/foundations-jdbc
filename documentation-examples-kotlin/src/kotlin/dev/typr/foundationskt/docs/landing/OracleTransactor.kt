package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class OracleTransactor {
    //start
    // Oracle - typed config, no JDBC URL to remember
    val tx =
        SimpleDataSource.create(
            OracleConfig.builder("localhost", 1521, "xe", "app", "secret")
                .serviceName("XEPDB1")
                .build()
        ).transactor()

    // Everything inside runs in one transaction
    fun getGreeting(): String =
        sql { "SELECT 'Hello from Oracle' FROM dual" }
            .query(RowCodec.of(OracleTypes.varchar2).exactlyOne())
            .transact(tx)

    //stop
}
