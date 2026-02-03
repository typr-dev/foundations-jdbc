package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.{MariaType, MariaTypes}
import dev.typr.foundations.data.{Uint1, Uint4, Uint8}

@SuppressWarnings(Array("unused"))
object IntegerTypesUnsigned:
  //start
  val unsignedTiny: MariaType[Uint1] = MariaTypes.tinyintUnsigned
  val unsignedInt: MariaType[Uint4] = MariaTypes.intUnsigned
  val unsignedBig: MariaType[Uint8] = MariaTypes.bigintUnsigned
  //stop
