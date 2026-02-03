package dev.typr.foundations.docs.landing

import dev.typr.foundations.{PgStruct, PgType, PgTypes}

@SuppressWarnings(Array("unused"))
object Dimensions:
  //start
  case class Dim(
    width: Double, height: Double,
    depth: Double, unit: String
  )

  // PgStruct handles PostgreSQL's composite wire format
  val pgStruct: PgStruct[Dim] = PgStruct.builder[Dim]("dimensions")
    .field("width", PgTypes.float8, _.width)
    .field("height", PgTypes.float8, _.height)
    .field("depth", PgTypes.float8, _.depth)
    .field("unit", PgTypes.text, _.unit)
    .build(Dim.apply)

  val pgType: PgType[Dim] = pgStruct.asType()
  //stop
