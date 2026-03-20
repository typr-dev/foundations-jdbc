package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;
import java.util.Optional;

@SuppressWarnings("unused")
public class NullableTypes {
  // start
  SqlServerType<Integer> notNull = SqlServerTypes.int_;
  SqlServerType<Optional<Integer>> nullable = SqlServerTypes.int_.opt();
  // stop
}
