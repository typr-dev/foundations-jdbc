package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object BooleanType:
  //start
  val boolType: MariaType[Boolean] = MariaTypes.bool
  val bitBool: MariaType[Boolean] = MariaTypes.bit1
  //stop
