package dev.typr.foundations.docs.oracle

import dev.typr.foundations.NonEmptyString
import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes

@Suppress("unused")
class NonEmptyStringTypes {
    //start
    val nonEmpty: OracleType<NonEmptyString> = OracleTypes.varchar2NonEmpty(100)
    val nvarNonEmpty: OracleType<NonEmptyString> = OracleTypes.nvarchar2NonEmpty(100)
    //stop
}
