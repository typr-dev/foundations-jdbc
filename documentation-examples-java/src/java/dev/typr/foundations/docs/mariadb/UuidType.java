package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;
import java.util.UUID;

@SuppressWarnings("unused")
public class UuidType {
  // start
  MariaType<UUID> uuidType = MariaTypes.uuid;
  // stop
}
