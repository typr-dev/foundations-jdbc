package dev.typr.foundations.docs.core

import dev.typr.foundations.PgTypes
import dev.typr.foundations.scala.RowParser

@SuppressWarnings(Array("unused"))
object SingleColumnParser:
  //start
  val idParser: RowParser[Integer] = RowParser.of(PgTypes.int4)
  //stop
