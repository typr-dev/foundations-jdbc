package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class ListTypes {
  // start
  // Any type can be made into a list with .list()
  DuckDbType<List<Integer>> listInt = DuckDbTypes.integer.list();
  DuckDbType<List<String>> listStr = DuckDbTypes.varchar.list();
  DuckDbType<List<Double>> listDouble = DuckDbTypes.double_.list();
  DuckDbType<List<UUID>> listUuid = DuckDbTypes.uuid.list();
  DuckDbType<List<LocalDate>> listDate = DuckDbTypes.date.list();
  DuckDbType<List<BigDecimal>> listDecimal = DuckDbTypes.decimal.list();
  // stop
}
