package dev.typr.foundations.docs.oracle

import dev.typr.foundations.{NonEmptyBlob, NonEmptyString, OracleType, OracleTypes}

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
