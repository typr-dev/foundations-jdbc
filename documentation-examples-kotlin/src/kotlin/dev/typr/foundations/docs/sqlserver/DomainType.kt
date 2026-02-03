package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes

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
