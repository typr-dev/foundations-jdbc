package dev.typr.foundations.docs.db2

import dev.typr.foundations.Db2Type
import dev.typr.foundations.Db2Types

@Suppress("unused")
class StringTypesSingleByte {
    //start
    val charType: Db2Type<String> = Db2Types.char_
    val char10: Db2Type<String> = Db2Types.char_(10)
    val varcharType: Db2Type<String> = Db2Types.varchar
    val varchar255: Db2Type<String> = Db2Types.varchar(255)
    val clobType: Db2Type<String> = Db2Types.clob
    //stop
}
