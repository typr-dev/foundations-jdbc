package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.Jsonb;

@SuppressWarnings("unused")
public class JsonTypes {
  // start
  PgType<Json> jsonType = PgTypes.json;
  PgType<Jsonb> jsonbType = PgTypes.jsonb;

  // Parse and use JSON
  Json data = new Json("{\"name\": \"John\"}");
  // stop
}
