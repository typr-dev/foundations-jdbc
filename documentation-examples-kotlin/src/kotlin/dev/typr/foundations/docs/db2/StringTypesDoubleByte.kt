package dev.typr.foundations.docs.db2

import dev.typr.foundations.Db2Type
import dev.typr.foundations.Db2Types

@Suppress("unused")
class StringTypesDoubleByte {
    //start
    val graphicType: Db2Type<String> = Db2Types.graphic
    val graphic10: Db2Type<String> = Db2Types.graphic(10)
    val vargraphicType: Db2Type<String> = Db2Types.vargraphic
    val dbclobType: Db2Type<String> = Db2Types.dbclob
    //stop
}
