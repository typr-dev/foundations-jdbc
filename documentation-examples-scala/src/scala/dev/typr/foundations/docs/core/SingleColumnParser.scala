package dev.typr.foundations.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object SingleColumnParser:
  //start
  val idParser: RowCodec[Int] = RowCodec.of(PgTypes.int4)
  //stop
