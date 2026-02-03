package dev.typr.foundations.analysis;

import dev.typr.foundations.DbType;
import dev.typr.foundations.Str;
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
   * Styled error message with optional ANSI colors.
   */
  Str styledMessage();

  /**
   * Human-readable error message (plain text).
   */
  default String message() {
    return styledMessage().plainText();
  }

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
    public Str styledMessage() {
      return Str.plain("Parameter ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" is declared in code (")
          .add(Str.cyan(type.typename().sqlType()))
          .add(") but not expected by the query");
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
    public Str styledMessage() {
      return Str.plain("Parameter ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" is expected by the query (")
          .add(Str.red(meta.vendorTypeName()))
          .add(" / ")
          .add(Str.gray(JdbcMeta.jdbcTypeName(meta.jdbcType())))
          .add(") but not provided in code");
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
    public Str styledMessage() {
      return Str.plain("Parameter ")
          .add(Str.yellow(String.valueOf(position)))
          .add(": type mismatch\n")
          .add(Str.gray("   │ "))
          .add("Declared: ")
          .add(Str.green(declared.typename().sqlType()))
          .add(Str.gray(" (JDBC: " + formatJdbcTypes(declaredJdbcTypes) + ")"))
          .add("\n")
          .add(Str.gray("   │ "))
          .add("Expected: ")
          .add(Str.red(expected.vendorTypeName()))
          .add(Str.gray(" (JDBC: " + JdbcMeta.jdbcTypeName(expected.jdbcType()) + ")"))
          .add("\n")
          .add(Str.gray("   └ "))
          .add(reason);
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
    public Str styledMessage() {
      return Str.plain("Column ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" is declared in RowParser (")
          .add(Str.cyan(type.typename().sqlType()))
          .add(") but not returned by query");
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
    public Str styledMessage() {
      return Str.plain("Column ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" '")
          .add(Str.cyan(meta.displayName()))
          .add("' is returned by query (")
          .add(Str.red(meta.vendorTypeName()))
          .add(" / ")
          .add(Str.gray(JdbcMeta.jdbcTypeName(meta.jdbcType())))
          .add(") but not declared in RowParser");
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
    public Str styledMessage() {
      return Str.plain("Column ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" '")
          .add(Str.cyan(columnName))
          .add("': type mismatch\n")
          .add(Str.gray("   │ "))
          .add("Declared: ")
          .add(Str.green(declared.typename().sqlType()))
          .add(Str.gray(" (JDBC: " + formatJdbcTypes(declaredJdbcTypes) + ")"))
          .add("\n")
          .add(Str.gray("   │ "))
          .add("Returned: ")
          .add(Str.red(returned.vendorTypeName()))
          .add(Str.gray(" (JDBC: " + JdbcMeta.jdbcTypeName(returned.jdbcType()) + ")"))
          .add("\n")
          .add(Str.gray("   └ "))
          .add(reason);
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
    public Str styledMessage() {
      return Str.plain("Column ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" '")
          .add(Str.cyan(columnName))
          .add("': nullability mismatch\n")
          .add(Str.gray("   │ "))
          .add("The database says this column is nullable\n")
          .add(Str.gray("   │ "))
          .add("But the type ")
          .add(Str.green(type.typename().sqlType()))
          .add(" is not Optional\n")
          .add(Str.gray("   └ "))
          .add("Use ")
          .add(Str.cyan(".opt()"))
          .add(" to make the type nullable");
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
