package dev.typr.foundationskt.docs.db2

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SpecialTypes {
    //start
    val xmlType: Db2Type<Xml> = Db2Types.xml
    val rowidType: Db2Type<ByteArray> = Db2Types.rowid
    //stop
}
