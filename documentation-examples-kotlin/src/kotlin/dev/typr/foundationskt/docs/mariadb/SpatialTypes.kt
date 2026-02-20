package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import org.mariadb.jdbc.type.GeometryCollection
import org.mariadb.jdbc.type.Point
import org.mariadb.jdbc.type.Polygon

@Suppress("unused")
class SpatialTypes {
    //start
    val pointType: MariaType<Point> = MariaTypes.point
    val polygonType: MariaType<Polygon> = MariaTypes.polygon
    val gcType: MariaType<GeometryCollection> = MariaTypes.geometrycollection

    // Create a point
    val p: Point = Point(1.0, 2.0)
    //stop
}
