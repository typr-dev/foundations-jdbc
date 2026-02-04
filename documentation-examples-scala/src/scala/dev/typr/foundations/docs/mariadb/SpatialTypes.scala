package dev.typr.foundations.docs.mariadb
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import org.mariadb.jdbc.`type`.{GeometryCollection, Point, Polygon}

@SuppressWarnings(Array("unused"))
object SpatialTypes:
  //start
  val pointType: MariaType[Point] = MariaTypes.point
  val polygonType: MariaType[Polygon] = MariaTypes.polygon
  val gcType: MariaType[GeometryCollection] = MariaTypes.geometrycollection

  // Create a point
  val p: Point = new Point(1.0, 2.0)
  //stop
