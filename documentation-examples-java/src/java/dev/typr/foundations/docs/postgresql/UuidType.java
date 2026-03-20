package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import java.util.UUID;

@SuppressWarnings("unused")
public class UuidType {
  // start
  PgType<UUID> uuidType = PgTypes.uuid;
  // stop
}
