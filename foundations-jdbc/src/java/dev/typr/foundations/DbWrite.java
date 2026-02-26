package dev.typr.foundations;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Common interface for writing values to PreparedStatement. Implemented by both PgWrite
 * (PostgreSQL) and MariaWrite (MariaDB).
 */
public interface DbWrite<A> {
  /** Set a value in a PreparedStatement at the given index. */
  void set(PreparedStatement ps, int idx, A value) throws SQLException;

  /**
   * Return inline SQL for a value, bypassing parameter binding. When present, the SQL expression
   * is rendered directly into the query text and no JDBC parameter is consumed.
   *
   * <p>Used for types like DuckDB UNION lists where the value cannot be expressed as a JDBC
   * parameter (requires SQL expression syntax like {@code union_value(tag := val)}).
   */
  default java.util.Optional<String> inlineSql(A value) {
    return java.util.Optional.empty();
  }
}
