package dev.typr.foundationssc.docs.db2
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object StringTypesSingleByte:
  //start
  val charType: Db2Type[String] = Db2Types.char_
  val char10: Db2Type[String] = Db2Types.char_(10)
  val varcharType: Db2Type[String] = Db2Types.varchar
  val varchar255: Db2Type[String] = Db2Types.varchar(255)
  val clobType: Db2Type[String] = Db2Types.clob
  //stop
