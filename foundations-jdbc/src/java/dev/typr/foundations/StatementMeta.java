package dev.typr.foundations;

import java.util.List;

/**
 * Metadata extracted from a prepared statement: parameter types and column types. Backend-agnostic
 * — both JDBC (via {@code PreparedStatement}) and PgPipe (via Parse+Describe) produce this same
 * structure.
 */
public record StatementMeta(
    List<JdbcMeta.ParameterMeta> parameters, List<JdbcMeta.ColumnMeta> columns) {

  public static final StatementMeta EMPTY = new StatementMeta(List.of(), List.of());
}
