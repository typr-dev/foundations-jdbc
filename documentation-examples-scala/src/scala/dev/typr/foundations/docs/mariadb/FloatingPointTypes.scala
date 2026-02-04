package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  //start
  val floatType: MariaType[Float] = MariaTypes.float_
  val doubleType: MariaType[Double] = MariaTypes.double_
  //stop
