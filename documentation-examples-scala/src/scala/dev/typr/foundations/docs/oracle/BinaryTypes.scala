package dev.typr.foundations.docs.oracle
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val rawType: OracleType[Array[Byte]] = OracleTypes.raw
  val raw100: OracleType[Array[Byte]] = OracleTypes.raw(100)  // RAW(100)

  // Non-empty variant
  val rawNonEmpty: OracleType[NonEmptyBlob] = OracleTypes.rawNonEmpty(100)
  //stop
