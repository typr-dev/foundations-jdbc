package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import org.postgresql.geometric.PGcircle
import org.postgresql.geometric.PGpoint
import org.postgresql.geometric.PGpolygon

@Suppress("unused")
class GeometricTypes {
    //start
    val pointType: PgType<PGpoint> = PgTypes.point
    val circleType: PgType<PGcircle> = PgTypes.circle
    val polygonType: PgType<PGpolygon> = PgTypes.polygon

    // Create geometric objects
    val point: PGpoint = PGpoint(1.0, 2.0)
    val circle: PGcircle = PGcircle(point, 5.0)
    //stop
}
