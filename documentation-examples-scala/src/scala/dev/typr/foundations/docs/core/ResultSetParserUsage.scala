package dev.typr.foundations.docs.core

import dev.typr.foundations.PgTypes
import dev.typr.foundations.scala.{ResultSetParser, RowParser}
import java.sql.{ResultSet, SQLException}
import java.time.Instant

@SuppressWarnings(Array("unused"))
object ResultSetParserUsage:
  case class Person(id: Int, name: String, createdAt: Instant)

  val personParser: RowParser[Person] = RowParser.builder[Person]()
    .field(PgTypes.int4, _.id)
    .field(PgTypes.text, _.name)
    .field(PgTypes.timestamptz, _.createdAt)
    .build(Person.apply)

  var resultSet: ResultSet = null // placeholder

  //start
  // Parse at most one result - returns Option[Person]
  val singleParser: ResultSetParser[Option[Person]] = personParser.maxOne()

  // Parse all results as a list
  val listParser: ResultSetParser[List[Person]] = personParser.all()

  // Execute with ResultSet
  @throws[SQLException]
  def parse(): Option[Person] =
    singleParser.apply(resultSet)
  //stop
