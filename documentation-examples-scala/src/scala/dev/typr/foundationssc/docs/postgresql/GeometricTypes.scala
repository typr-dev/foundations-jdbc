package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*


import org.postgresql.geometric.{PGcircle, PGpoint, PGpolygon}

@SuppressWarnings(Array("unused"))
object GeometricTypes:
  //start
  val pointType: PgType[PGpoint] = PgTypes.point
  val circleType: PgType[PGcircle] = PgTypes.circle
  val polygonType: PgType[PGpolygon] = PgTypes.polygon

  // Create geometric objects
  val point: PGpoint = new PGpoint(1.0, 2.0)
  val circle: PGcircle = new PGcircle(point, 5.0)
  //stop
