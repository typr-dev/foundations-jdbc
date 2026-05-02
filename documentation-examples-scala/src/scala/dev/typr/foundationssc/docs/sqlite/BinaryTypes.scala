package dev.typr.foundationssc.docs.sqlite
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object BinaryTypes:
  // start
  val blobType: SqliteType[Array[Byte]] = SqliteTypes.blob
  // stop
