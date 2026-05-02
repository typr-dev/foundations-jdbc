package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;
import java.util.UUID;

@SuppressWarnings("unused")
public class UuidType {
  // start
  // SQLite has no UUID storage class — values are stored as canonical 36-char TEXT.
  SqliteType<UUID> uuidType = SqliteTypes.uuid;
  // stop
}
