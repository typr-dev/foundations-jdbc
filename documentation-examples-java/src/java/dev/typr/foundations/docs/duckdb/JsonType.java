package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import dev.typr.foundations.data.Json;

@SuppressWarnings("unused")
public class JsonType {
  // start
  DuckDbType<Json> jsonType = DuckDbTypes.json;

  Json data = new Json("{\"name\": \"DuckDB\"}");
  // stop
}
