package dev.typr.foundations.docs.db2

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.data.Xml

@Suppress("unused")
class SpecialTypes {
    //start
    val xmlType: Db2Type<Xml> = Db2Types.xml
    val rowidType: Db2Type<ByteArray> = Db2Types.rowid
    //stop
}
