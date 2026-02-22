package dev.typr.foundationssc.docs.duckdb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val blobType: DuckDbType[Array[Byte]] = DuckDbTypes.blob
  //stop
