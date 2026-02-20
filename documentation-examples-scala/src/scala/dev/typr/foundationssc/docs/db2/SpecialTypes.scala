package dev.typr.foundationssc.docs.db2
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object SpecialTypes:
  //start
  val xmlType: Db2Type[Xml] = Db2Types.xml
  val rowidType: Db2Type[Array[Byte]] = Db2Types.rowid
  //stop
