package dev.typr.foundations.docs.sqlserver

import com.microsoft.sqlserver.jdbc.{Geography, Geometry}
import dev.typr.foundations.{SqlServerType, SqlServerTypes}

@SuppressWarnings(Array("unused"))
object SpatialTypes:
  //start
  val geoType: SqlServerType[Geography] = SqlServerTypes.geography
  val geomType: SqlServerType[Geometry] = SqlServerTypes.geometry
  //stop
