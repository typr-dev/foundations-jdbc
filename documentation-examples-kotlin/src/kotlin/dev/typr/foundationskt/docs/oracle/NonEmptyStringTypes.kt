package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class NonEmptyStringTypes {
    //start
    val nonEmpty: OracleType<NonEmptyString> = OracleTypes.varchar2NonEmpty(100)
    val nvarNonEmpty: OracleType<NonEmptyString> = OracleTypes.nvarchar2NonEmpty(100)
    //stop
}
