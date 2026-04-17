package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;

@SuppressWarnings("unused")
public class BinaryTypes {
  // start
  SqlServerType<byte[]> binaryType = SqlServerTypes.binaryOf(16);
  SqlServerType<byte[]> varbinaryType = SqlServerTypes.varbinaryOf(255);
  SqlServerType<byte[]> varbinaryMax = SqlServerTypes.varbinaryMax;
  // stop
}
