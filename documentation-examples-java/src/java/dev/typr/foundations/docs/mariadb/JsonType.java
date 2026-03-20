package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;
import dev.typr.foundations.data.Json;

@SuppressWarnings("unused")
public class JsonType {
  // start
  MariaType<Json> jsonType = MariaTypes.json;

  Json data = new Json("{\"name\": \"John\", \"age\": 30}");
  // stop
}
