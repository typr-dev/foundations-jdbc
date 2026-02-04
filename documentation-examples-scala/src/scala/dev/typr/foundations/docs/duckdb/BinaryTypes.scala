package dev.typr.foundations.docs.duckdb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val blobType: DuckDbType[Array[Byte]] = DuckDbTypes.blob
  //stop
