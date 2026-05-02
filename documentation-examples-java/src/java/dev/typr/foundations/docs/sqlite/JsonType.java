package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;
import dev.typr.foundations.data.Json;

@SuppressWarnings("unused")
public class JsonType {
  // start
  // Backed by TEXT — use SQLite's built-in JSON1 functions (json(), json_extract(), ->, ->>).
  SqliteType<Json> jsonType = SqliteTypes.json;

  Json data = new Json("{\"name\": \"SQLite\"}");
  // stop
}
