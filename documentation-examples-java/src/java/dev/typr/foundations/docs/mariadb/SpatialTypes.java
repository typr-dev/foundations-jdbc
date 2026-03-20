package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;
import org.mariadb.jdbc.type.GeometryCollection;
import org.mariadb.jdbc.type.Point;
import org.mariadb.jdbc.type.Polygon;

@SuppressWarnings("unused")
public class SpatialTypes {
  // start
  MariaType<Point> pointType = MariaTypes.point;
  MariaType<Polygon> polygonType = MariaTypes.polygon;
  MariaType<GeometryCollection> gcType = MariaTypes.geometrycollection;

  // Create a point
  Point p = new Point(1.0, 2.0);
  // stop
}
