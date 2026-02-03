package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.{MariaType, MariaTypes}

@SuppressWarnings(Array("unused"))
object FloatingPointTypes:
  //start
  val floatType: MariaType[java.lang.Float] = MariaTypes.float_
  val doubleType: MariaType[java.lang.Double] = MariaTypes.double_
  //stop
