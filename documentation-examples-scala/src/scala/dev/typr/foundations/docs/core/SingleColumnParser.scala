package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object SingleColumnParser:
  //start
  val idParser: RowParser[Int] = RowParser.of(PgTypes.int4)
  //stop
