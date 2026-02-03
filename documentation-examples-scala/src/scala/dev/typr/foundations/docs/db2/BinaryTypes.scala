package dev.typr.foundations.docs.db2

import dev.typr.foundations.{Db2Type, Db2Types}

@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val binaryType: Db2Type[Array[Byte]] = Db2Types.binary
  val binary16: Db2Type[Array[Byte]] = Db2Types.binary(16)
  val varbinaryType: Db2Type[Array[Byte]] = Db2Types.varbinary
  val blobType: Db2Type[Array[Byte]] = Db2Types.blob
  //stop
