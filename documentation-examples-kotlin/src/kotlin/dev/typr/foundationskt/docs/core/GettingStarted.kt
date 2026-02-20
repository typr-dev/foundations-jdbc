package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class GettingStarted {
    //start
    val tx: Transactor =
        SingleConnectionDataSource.create(DuckDbConfig.inMemory().build())
            .transactor()

    val result: Int = sql { "SELECT 42" }
        .query(RowCodec.of(DuckDbTypes.integer).exactlyOne())
        .transact(tx)
    //stop
}
