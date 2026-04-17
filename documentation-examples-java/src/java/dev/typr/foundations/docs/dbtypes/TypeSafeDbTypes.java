package dev.typr.foundations.docs.dbtypes;

import dev.typr.foundations.*;
import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.Range;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.postgresql.geometric.PGpoint;

@SuppressWarnings("unused")
public class TypeSafeDbTypes {
  // start
  // PostgreSQL types
  PgType<List<Integer>> intArray = PgTypes.int4.array();
  PgType<Range<LocalDate>> dateRange = PgTypes.daterange;
  PgType<PGpoint> point = PgTypes.point;

  // MariaDB types
  MariaType<Json> json = MariaTypes.json;

  // DuckDB types
  DuckDbType<Map<String, Integer>> map = DuckDbTypes.varchar.mapTo(DuckDbTypes.integer);
  // stop
}
