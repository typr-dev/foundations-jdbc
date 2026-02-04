package dev.typr.foundations.docs.postgresql
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object BinaryTypes:
  //start
  val bytesType: PgType[Array[Byte]] = PgTypes.bytea
  //stop
