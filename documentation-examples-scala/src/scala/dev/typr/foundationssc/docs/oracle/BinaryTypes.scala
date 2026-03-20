package dev.typr.foundationssc.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object BinaryTypes:
  // start
  val rawType: OracleType[Array[Byte]] = OracleTypes.raw
  val raw100: OracleType[Array[Byte]] = OracleTypes.raw(100) // RAW(100)

  // Non-empty variant
  val rawNonEmpty: OracleType[NonEmptyBlob] = OracleTypes.rawNonEmpty(100)
  // stop
