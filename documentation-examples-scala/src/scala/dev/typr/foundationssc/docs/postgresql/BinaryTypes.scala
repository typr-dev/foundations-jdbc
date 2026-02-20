package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val bytesType: PgType[Array[Byte]] = PgTypes.bytea
  //stop
