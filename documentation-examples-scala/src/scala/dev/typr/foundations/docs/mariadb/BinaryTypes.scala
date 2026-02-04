package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val binaryType: MariaType[Array[Byte]] = MariaTypes.binary(16)
  val varbinaryType: MariaType[Array[Byte]] = MariaTypes.varbinary(255)
  val blobType: MariaType[Array[Byte]] = MariaTypes.blob
  //stop
