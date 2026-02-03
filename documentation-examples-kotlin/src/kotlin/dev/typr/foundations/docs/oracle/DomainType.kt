package dev.typr.foundations.docs.oracle

import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class EmployeeId(val value: Long)

    // Create OracleType from NUMBER
    val empIdType: OracleType<EmployeeId> =
        OracleTypes.numberLong.bimap(::EmployeeId, EmployeeId::value)
    //stop
}
