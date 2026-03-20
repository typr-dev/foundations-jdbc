package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object IntegerTypesUnsigned:
  // start
  val unsignedTiny: MariaType[Uint1] = MariaTypes.tinyintUnsigned
  val unsignedInt: MariaType[Uint4] = MariaTypes.intUnsigned
  val unsignedBig: MariaType[Uint8] = MariaTypes.bigintUnsigned
  // stop
