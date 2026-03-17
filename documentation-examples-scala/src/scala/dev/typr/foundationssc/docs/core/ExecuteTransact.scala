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

  val findCities: Operation[List[City]] =
    sql"SELECT name, population FROM city ORDER BY population DESC"
      .query(cityCodec.all())

  // start
  def cities(): List[City] = tx.transact { conn =>
    findCities.run(conn)
  }
  // stop
