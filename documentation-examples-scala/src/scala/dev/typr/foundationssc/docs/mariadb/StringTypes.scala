package dev.typr.foundationssc.docs.mariadb
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object StringTypes:
  // start
  val charType: MariaType[String] = MariaTypes.char_Of(10)
  val varcharType: MariaType[String] = MariaTypes.varcharOf(255)
  val textType: MariaType[String] = MariaTypes.text
  val longType: MariaType[String] = MariaTypes.longtext
  // stop
