package dev.typr.foundationskt.docs.oracle

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StringTypes {
    //start
    val varcharType: OracleType<String> = OracleTypes.varchar2
    val varchar100: OracleType<String> = OracleTypes.varchar2Of(100)
    val charType: OracleType<String> = OracleTypes.char_Of(10)
    val nvarcharType: OracleType<String> = OracleTypes.nvarchar2Of(100)
    //stop
}
