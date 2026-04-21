package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ExecuteTransact:
  case class City(name: String, population: Int)

  val cityCodec: RowCodec[City] = RowCodec
    .builder[City]()
    .field(PgTypes.text)(_.name)
    .field(PgTypes.int4)(_.population)
    .build(City.apply)

  var tx: Transactor = null // placeholder

  val findCities: OperationRead[List[City]] =
    sql"SELECT name, population FROM city ORDER BY population DESC"
      .query(cityCodec.all())

  val countCities: OperationRead[Long] =
    sql"SELECT count(*) FROM city".queryExactlyOne(PgTypes.int8)

  // start
  // Single-operation form: .transact(tx) handles commit/rollback/close.
  def cities(): List[City] = findCities.transact(tx)

  // Multiple operations in one transaction: compose with combineWith.
  def citiesWithCount(): List[City] =
    findCities
      .combineWith(countCities) { (list, count) =>
        println(s"rows: $count")
        list
      }
      .transact(tx)
  // stop
