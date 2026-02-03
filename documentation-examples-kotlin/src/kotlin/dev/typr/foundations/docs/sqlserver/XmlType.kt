package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes
import dev.typr.foundations.data.Xml

@Suppress("unused")
class XmlType {
    //start
    val xmlType: SqlServerType<Xml> = SqlServerTypes.xml
    //stop
}
