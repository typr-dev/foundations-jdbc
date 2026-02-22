package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object FirstQuery:
  //start
  case class City(name: String, population: Int)

  val cityCodec: RowCodecNamed[City] = RowCodec.namedBuilder[City]()
    .field("name", DuckDbTypes.varchar)(_.name)
    .field("population", DuckDbTypes.integer)(_.population)
    .build(City.apply)

  val findCities: Operation[List[City]] =
    sql"SELECT ${cityCodec.columnList} FROM city ORDER BY population DESC"
      .query(cityCodec.all())
  //stop
