package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object SingleColumnCodec:
  // start
  val idCodec: RowCodec[Int] = RowCodec.of(PgTypes.int4)
  // stop
