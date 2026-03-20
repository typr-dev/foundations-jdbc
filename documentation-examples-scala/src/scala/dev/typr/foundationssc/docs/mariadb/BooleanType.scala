package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object BooleanType:
  // start
  val boolType: MariaType[Boolean] = MariaTypes.bool
  val bitBool: MariaType[Boolean] = MariaTypes.bit1
  // stop
