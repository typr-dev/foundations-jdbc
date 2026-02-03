package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}
import dev.typr.foundations.data.Xml

@SuppressWarnings(Array("unused"))
object XmlType:
  //start
  val xmlType: SqlServerType[Xml] = SqlServerTypes.xml
  //stop
