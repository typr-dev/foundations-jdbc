package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import java.time.Instant

@SuppressWarnings(Array("unused"))
object RowParserBasic:
  //start
  case class Person(id: Int, name: String, createdAt: Instant)

  val personParser: RowParser[Person] = RowParser.builder[Person]()
    .field(PgTypes.int4, _.id)
    .field(PgTypes.text, _.name)
    .field(PgTypes.timestamptz, _.createdAt)
    .build(Person.apply)
  //stop
