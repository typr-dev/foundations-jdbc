package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object BinaryTypes:
  // start
  val binaryType: MariaType[Array[Byte]] = MariaTypes.binary(16)
  val varbinaryType: MariaType[Array[Byte]] = MariaTypes.varbinary(255)
  val blobType: MariaType[Array[Byte]] = MariaTypes.blob
  // stop
