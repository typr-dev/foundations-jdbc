package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ParameterizedQuery:
  case class City(name: String, population: Int)

  val cityCodec: RowCodecNamed[City] = RowCodec
    .namedBuilder[City]()
    .field("name", DuckDbTypes.varchar)(_.name)
    .field("population", DuckDbTypes.integer)(_.population)
    .build(City.apply)

  // start
  def findCityByName(name: String): OperationRead[Option[City]] =
    sql"SELECT ${cityCodec.columnList} FROM city WHERE name = ${DuckDbTypes.text(name)}"
      .query(cityCodec.maxOne())
  // stop
