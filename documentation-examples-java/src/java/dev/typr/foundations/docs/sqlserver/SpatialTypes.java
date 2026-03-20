package dev.typr.foundations.docs.sqlserver;

import com.microsoft.sqlserver.jdbc.Geography;
import com.microsoft.sqlserver.jdbc.Geometry;
import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;

@SuppressWarnings("unused")
public class SpatialTypes {
  // start
  SqlServerType<Geography> geoType = SqlServerTypes.geography;
  SqlServerType<Geometry> geomType = SqlServerTypes.geometry;
  // stop
}
