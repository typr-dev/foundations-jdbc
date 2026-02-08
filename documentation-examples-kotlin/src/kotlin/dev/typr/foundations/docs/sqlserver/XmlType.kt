package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.kotlinfoundations.data.Xml

@Suppress("unused")
class XmlType {
    //start
    val xmlType: SqlServerType<Xml> = SqlServerTypes.xml
    //stop
}
