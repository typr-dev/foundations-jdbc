package dev.typr.foundations.docs.db2

import dev.typr.foundations.Db2Type
import dev.typr.foundations.Db2Types
import dev.typr.foundations.data.Xml

@Suppress("unused")
class SpecialTypes {
    //start
    val xmlType: Db2Type<Xml> = Db2Types.xml
    val rowidType: Db2Type<ByteArray> = Db2Types.rowid
    //stop
}
