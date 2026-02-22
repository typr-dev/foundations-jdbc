package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


import java.sql.ResultSet
import java.time.Instant

@SuppressWarnings(Array("unused"))
object ResultSetParserUsage:
  case class Person(id: Int, name: String, createdAt: Instant)

  val personCodec: RowCodec[Person] = RowCodec.builder[Person]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .field(PgTypes.timestamptz)(_.createdAt)
    .build(Person.apply)

  var resultSet: ResultSet = null // placeholder

  //start
  // Parse at most one result - returns Option[Person]
  val singleParser: ResultSetParser[Option[Person]] = personCodec.maxOne()

  // Parse all results as a list
  val listParser: ResultSetParser[List[Person]] = personCodec.all()

  // Execute with ResultSet
  def parse(): Option[Person] =
    singleParser.apply(resultSet)
  //stop
