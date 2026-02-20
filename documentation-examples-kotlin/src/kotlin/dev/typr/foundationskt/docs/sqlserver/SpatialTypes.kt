package dev.typr.foundationskt.docs.sqlserver

import com.microsoft.sqlserver.jdbc.Geography
import com.microsoft.sqlserver.jdbc.Geometry
import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SpatialTypes {
    //start
    val geoType: SqlServerType<Geography> = SqlServerTypes.geography
    val geomType: SqlServerType<Geometry> = SqlServerTypes.geometry
    //stop
}
