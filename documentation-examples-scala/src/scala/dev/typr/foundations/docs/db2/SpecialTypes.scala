package dev.typr.foundations.docs.db2

import dev.typr.foundations.{Db2Type, Db2Types}
import dev.typr.foundations.data.Xml

@SuppressWarnings(Array("unused"))
object SpecialTypes:
  //start
  val xmlType: Db2Type[Xml] = Db2Types.xml
  val rowidType: Db2Type[Array[Byte]] = Db2Types.rowid
  //stop
