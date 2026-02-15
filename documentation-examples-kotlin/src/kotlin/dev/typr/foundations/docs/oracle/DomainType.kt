package dev.typr.foundations.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class EmployeeId(val value: Long)

    // Create OracleType from NUMBER
    val empIdType: OracleType<EmployeeId> =
        OracleTypes.numberLong.transform(::EmployeeId, EmployeeId::value)
    //stop
}
