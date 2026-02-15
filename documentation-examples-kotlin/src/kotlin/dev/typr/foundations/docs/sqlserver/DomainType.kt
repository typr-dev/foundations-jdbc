package dev.typr.foundations.docs.sqlserver

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class OrderId(val value: Int)

    // Create SqlServerType from INT
    val orderIdType: SqlServerType<OrderId> =
        SqlServerTypes.int_.bimap(::OrderId, OrderId::value)
    //stop
}
