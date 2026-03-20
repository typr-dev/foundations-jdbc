package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.data.Vector;
import java.util.Map;

@SuppressWarnings("unused")
public class SpecialTypes {
  // start
  PgType<Map<String, String>> hstoreType = PgTypes.hstore;
  PgType<Vector> vectorType = PgTypes.vector;
  // stop
}
