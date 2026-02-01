package dev.typr.foundations.analysis;

import dev.typr.foundations.DbType;
import java.util.Set;

/**
 * Errors detected during query analysis. Each error describes a mismatch between
 * what the code declares and what the database expects/provides.
 */
public sealed interface AlignmentError {

  /**
   * 1-indexed position of the error (parameter or column number).
   */
  int position();

  /**
   * Human-readable error message.
   */
  String message();

  // ─────────────────────────────────────────────────────────────────────────────
  // Parameter errors
  // ─────────────────────────────────────────────────────────────────────────────

  /**
   * Parameter declared in code but not expected by SQL.
   * The Fragment has more parameters than the query needs.
   */
  record ExtraParameter(
      int position,
      DbType<?> type
  ) implements AlignmentError {
    @Override
    public String message() {
      return "Parameter " + position + " is declared in code (" + type.typename().sqlType() +
          ") but not expected by the query";
    }
  }

  /**
   * Parameter expected by SQL but not declared in code.
   * The Fragment has fewer parameters than the query needs.
   */
  record MissingParameter(
      int position,
      JdbcMeta.ParameterMeta meta
  ) implements AlignmentError {
    @Override
    public String message() {
      return "Parameter " + position + " is expected by the query (" +
          meta.vendorTypeName() + " / " + JdbcMeta.jdbcTypeName(meta.jdbcType()) +
          ") but not provided in code";
    }
  }

  /**
   * Parameter type in code doesn't match what the database expects.
   */
  record ParameterTypeMismatch(
      int position,
      DbType<?> declared,
      JdbcMeta.ParameterMeta expected,
      Set<Integer> declaredJdbcTypes,
      String reason
  ) implements AlignmentError {
    @Override
    public String message() {
      String declaredStr = declared.typename().sqlType() +
          " (JDBC: " + formatJdbcTypes(declaredJdbcTypes) + ")";
      String expectedStr = expected.vendorTypeName() +
          " (JDBC: " + JdbcMeta.jdbcTypeName(expected.jdbcType()) + ")";
      return "Parameter " + position + ": type mismatch\n" +
          "  │ Declared: " + declaredStr + "\n" +
          "  │ Expected: " + expectedStr + "\n" +
          "  └ " + reason;
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Column errors
  // ─────────────────────────────────────────────────────────────────────────────

  /**
   * Column declared in RowParser but not returned by query.
   */
  record ExtraColumn(
      int position,
      DbType<?> type
  ) implements AlignmentError {
    @Override
    public String message() {
      return "Column " + position + " is declared in RowParser (" + type.typename().sqlType() +
          ") but not returned by query";
    }
  }

  /**
   * Column returned by query but not declared in RowParser.
   */
  record MissingColumn(
      int position,
      JdbcMeta.ColumnMeta meta
  ) implements AlignmentError {
    @Override
    public String message() {
      return "Column " + position + " '" + meta.displayName() + "' is returned by query (" +
          meta.vendorTypeName() + " / " + JdbcMeta.jdbcTypeName(meta.jdbcType()) +
          ") but not declared in RowParser";
    }
  }

  /**
   * Column type in RowParser doesn't match what the database returns.
   */
  record ColumnTypeMismatch(
      int position,
      String columnName,
      DbType<?> declared,
      JdbcMeta.ColumnMeta returned,
      Set<Integer> declaredJdbcTypes,
      String reason
  ) implements AlignmentError {
    @Override
    public String message() {
      String declaredStr = declared.typename().sqlType() +
          " (JDBC: " + formatJdbcTypes(declaredJdbcTypes) + ")";
      String returnedStr = returned.vendorTypeName() +
          " (JDBC: " + JdbcMeta.jdbcTypeName(returned.jdbcType()) + ")";
      return "Column " + position + " '" + columnName + "': type mismatch\n" +
          "  │ Declared: " + declaredStr + "\n" +
          "  │ Returned: " + returnedStr + "\n" +
          "  └ " + reason;
    }
  }

  /**
   * Column is nullable in database but declared as non-optional in code.
   * This can cause NullPointerException at runtime.
   */
  record NullabilityMismatch(
      int position,
      String columnName,
      DbType<?> type
  ) implements AlignmentError {
    @Override
    public String message() {
      return "Column " + position + " '" + columnName + "': nullability mismatch\n" +
          "  │ The database says this column is nullable\n" +
          "  │ But the type " + type.typename().sqlType() + " is not Optional\n" +
          "  └ Use .opt() to make the type nullable, or ensure the column is NOT NULL";
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Helpers
  // ─────────────────────────────────────────────────────────────────────────────

  private static String formatJdbcTypes(Set<Integer> types) {
    if (types.isEmpty()) {
      return "none";
    }
    return String.join(", ", types.stream()
        .map(JdbcMeta::jdbcTypeName)
        .sorted()
        .toList());
  }
}
