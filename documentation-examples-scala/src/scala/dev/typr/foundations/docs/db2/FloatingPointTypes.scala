package dev.typr.foundations.docs.db2
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  //start
  val realType: Db2Type[Float] = Db2Types.real
  val doubleType: Db2Type[Double] = Db2Types.double_
  //stop
