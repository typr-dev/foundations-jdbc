package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class ArrayTypes {
  // start
  // Any scalar type can be made into a PostgreSQL array via `.array()`.
  PgType<List<Integer>> intArray = PgTypes.int4.array();
  PgType<List<String>> textArray = PgTypes.text.array();
  PgType<List<UUID>> uuidArray = PgTypes.uuid.array();

  // Multi-dimensional arrays compose: `int4[][]` in SQL.
  PgType<List<List<Integer>>> intMatrix = PgTypes.int4.array().array();
  // stop
}
