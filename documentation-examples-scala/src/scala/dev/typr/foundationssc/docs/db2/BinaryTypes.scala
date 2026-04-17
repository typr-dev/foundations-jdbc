package dev.typr.foundationssc.docs.db2
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object BinaryTypes:
  // start
  val binaryType: Db2Type[Array[Byte]] = Db2Types.binary
  val binary16: Db2Type[Array[Byte]] = Db2Types.binaryOf(16)
  val varbinaryType: Db2Type[Array[Byte]] = Db2Types.varbinary
  val blobType: Db2Type[Array[Byte]] = Db2Types.blob
  // stop
