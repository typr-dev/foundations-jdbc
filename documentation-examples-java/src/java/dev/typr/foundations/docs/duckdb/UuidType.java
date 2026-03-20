package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import java.util.UUID;

@SuppressWarnings("unused")
public class UuidType {
  // start
  DuckDbType<UUID> uuidType = DuckDbTypes.uuid;
  // stop
}
