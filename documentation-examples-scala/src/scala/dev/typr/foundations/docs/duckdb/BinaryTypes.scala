package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.{DuckDbType, DuckDbTypes}

@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val blobType: DuckDbType[Array[Byte]] = DuckDbTypes.blob
  //stop
