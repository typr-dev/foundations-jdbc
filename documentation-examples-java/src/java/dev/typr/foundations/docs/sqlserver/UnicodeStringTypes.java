package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;

@SuppressWarnings("unused")
public class UnicodeStringTypes {
  // start
  SqlServerType<String> ncharType = SqlServerTypes.ncharOf(10);
  SqlServerType<String> nvarcharType = SqlServerTypes.nvarcharOf(255);
  SqlServerType<String> nvarcharMax = SqlServerTypes.nvarcharMax;
  // stop
}
