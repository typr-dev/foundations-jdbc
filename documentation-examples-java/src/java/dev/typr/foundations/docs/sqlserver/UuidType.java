package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;
import java.util.UUID;

@SuppressWarnings("unused")
public class UuidType {
  // start
  SqlServerType<UUID> uuidType = SqlServerTypes.uniqueidentifier;
  // stop
}
