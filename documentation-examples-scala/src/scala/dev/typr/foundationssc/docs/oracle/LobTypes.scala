package dev.typr.foundationssc.docs.oracle
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object LobTypes:
  //start
  val clobType: OracleType[String] = OracleTypes.clob
  val nclobType: OracleType[String] = OracleTypes.nclob
  val blobType: OracleType[Array[Byte]] = OracleTypes.blob

  // Non-empty variants
  val clobNonEmpty: OracleType[NonEmptyString] = OracleTypes.clobNonEmpty
  val blobNonEmpty: OracleType[NonEmptyBlob] = OracleTypes.blobNonEmpty
  //stop
