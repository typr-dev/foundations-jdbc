package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;

@SuppressWarnings("unused")
public class StringTypes {
  // start
  SqlServerType<String> charType = SqlServerTypes.char_Of(10);
  SqlServerType<String> varcharType = SqlServerTypes.varcharOf(255);
  SqlServerType<String> varcharMax = SqlServerTypes.varcharMax;
  // stop
}
