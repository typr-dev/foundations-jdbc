package dev.typr.foundations.docs.sqlserver

import com.microsoft.sqlserver.jdbc.Geography
import com.microsoft.sqlserver.jdbc.Geometry
import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes

@Suppress("unused")
class SpatialTypes {
    //start
    val geoType: SqlServerType<Geography> = SqlServerTypes.geography
    val geomType: SqlServerType<Geometry> = SqlServerTypes.geometry
    //stop
}
