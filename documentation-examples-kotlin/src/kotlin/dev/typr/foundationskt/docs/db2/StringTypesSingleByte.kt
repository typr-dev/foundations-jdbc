package dev.typr.foundationskt.docs.db2

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class StringTypesSingleByte {
    //start
    val charType: Db2Type<String> = Db2Types.char_
    val char10: Db2Type<String> = Db2Types.char_Of(10)
    val varcharType: Db2Type<String> = Db2Types.varchar
    val varchar255: Db2Type<String> = Db2Types.varcharOf(255)
    val clobType: Db2Type<String> = Db2Types.clob
    //stop
}
