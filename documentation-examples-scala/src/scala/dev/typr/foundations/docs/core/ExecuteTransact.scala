package dev.typr.foundations.docs.core
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object ExecuteTransact:
  case class City(name: String, population: Int)

  val cityParser: RowParser[City] = RowParser.builder[City]()
    .field(PgTypes.text)(_.name)
    .field(PgTypes.int4)(_.population)
    .build(City.apply)

  var tx: Transactor = null // placeholder

  val findCities: Operation[List[City]] =
    sql"SELECT name, population FROM city ORDER BY population DESC"
      .query(cityParser.all())

  //start
  @throws[SQLException]
  def cities(): List[City] = findCities.transact(tx)
  //stop
