package dev.typr.foundationssc.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object Dimensions:
  // start
  case class Dim(
      width: Double,
      height: Double,
      depth: Double,
      unit: String
  )

  val dimCodec: RowCodecNamed[Dim] =
    RowCodec
      .namedBuilder[Dim]()
      .field("width", PgTypes.float8)(_.width)
      .field("height", PgTypes.float8)(_.height)
      .field("depth", PgTypes.float8)(_.depth)
      .field("unit", PgTypes.text)(_.unit)
      .build(Dim.apply)

  // Named composite — reads and writes via CREATE TYPE dimensions
  val pgType: PgType[Dim] = PgTypes.compositeOf("dimensions", dimCodec)
  // stop
