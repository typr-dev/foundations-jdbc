package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;

@SuppressWarnings("unused")
public class UnicodeStringTypes {
  // start
  SqlServerType<String> ncharType = SqlServerTypes.nchar(10);
  SqlServerType<String> nvarcharType = SqlServerTypes.nvarchar(255);
  SqlServerType<String> nvarcharMax = SqlServerTypes.nvarcharMax;
  // stop
}
