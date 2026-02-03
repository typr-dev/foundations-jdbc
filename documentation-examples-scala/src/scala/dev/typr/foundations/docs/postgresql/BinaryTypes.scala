package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}

@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val bytesType: PgType[Array[Byte]] = PgTypes.bytea
  //stop
