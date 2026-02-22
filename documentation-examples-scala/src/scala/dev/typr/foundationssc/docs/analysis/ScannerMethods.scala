package dev.typr.foundationssc.docs.analysis

import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object ScannerMethods:
  case class City(id: Int, name: String)

  val cityCodec: RowCodec[City] = RowCodec.builder[City]()
    .field(PgTypes.int4)(_.id)
    .field(PgTypes.text)(_.name)
    .build(City.apply)

  //start
  // Fields — discovered automatically
  val allCities: Operation[List[City]] =
    Fragment.of("SELECT id, name FROM cities")
      .query(cityCodec.all())

  // No-arg methods — discovered automatically
  def activeCities(): Operation[List[City]] =
    Fragment.of("SELECT id, name FROM cities WHERE active")
      .query(cityCodec.all())

  // Methods with parameters — dummy arguments constructed automatically
  def findByName(name: String): Operation[Option[City]] =
    Fragment.of("SELECT id, name FROM cities WHERE name = ")
      .value(PgTypes.text, name)
      .query(cityCodec.maxOne())

  // Templates — also discovered
  val findById: Template[Int, Option[City]] =
    Fragment.of("SELECT id, name FROM cities WHERE id = ")
      .param(PgTypes.int4)
      .query(cityCodec.maxOne())
  //stop
