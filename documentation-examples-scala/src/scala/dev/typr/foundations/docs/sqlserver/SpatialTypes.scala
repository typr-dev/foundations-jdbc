package dev.typr.foundations.docs.sqlserver
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*


import com.microsoft.sqlserver.jdbc.{Geography, Geometry}

@SuppressWarnings(Array("unused"))
object SpatialTypes:
  //start
  val geoType: SqlServerType[Geography] = SqlServerTypes.geography
  val geomType: SqlServerType[Geometry] = SqlServerTypes.geometry
  //stop
