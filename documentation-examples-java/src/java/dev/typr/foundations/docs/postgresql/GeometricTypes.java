package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import org.postgresql.geometric.PGcircle;
import org.postgresql.geometric.PGpoint;
import org.postgresql.geometric.PGpolygon;

@SuppressWarnings("unused")
public class GeometricTypes {
  // start
  PgType<PGpoint> pointType = PgTypes.point;
  PgType<PGcircle> circleType = PgTypes.circle;
  PgType<PGpolygon> polygonType = PgTypes.polygon;

  // Create geometric objects
  PGpoint point = new PGpoint(1.0, 2.0);
  PGcircle circle = new PGcircle(point, 5.0);
  // stop
}
