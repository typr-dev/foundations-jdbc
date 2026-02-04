package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object IntegerTypesUnsigned:
  //start
  val unsignedTiny: MariaType[Uint1] = MariaTypes.tinyintUnsigned
  val unsignedInt: MariaType[Uint4] = MariaTypes.intUnsigned
  val unsignedBig: MariaType[Uint8] = MariaTypes.bigintUnsigned
  //stop
