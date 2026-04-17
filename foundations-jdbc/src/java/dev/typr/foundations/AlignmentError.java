package dev.typr.foundations;

import java.util.Set;

/**
 * Errors detected during query analysis. Each error describes a mismatch between what the code
 * declares and what the database expects/provides.
 */
public sealed interface AlignmentError {

  /** 1-indexed position of the error (parameter or column number). */
  int position();

  /** Styled error message with optional ANSI colors. */
  Str styledMessage();

  /** Human-readable error message (plain text). */
  default String message() {
    return styledMessage().plainText();
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Parameter errors
  // ─────────────────────────────────────────────────────────────────────────────

  record ExtraParameter(int position, DbType<?> type) implements AlignmentError {
    @Override
    public Str styledMessage() {
      return Str.plain("Parameter ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" is declared in code (")
          .add(Str.cyan(type.typename().sqlType()))
          .add(") but not expected by the query");
    }
  }

  record MissingParameter(int position, JdbcMeta.ParameterMeta meta) implements AlignmentError {
    @Override
    public Str styledMessage() {
      return Str.plain("Parameter ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" is expected by the query (")
          .add(Str.red(meta.vendorTypeName()))
          .add(") but not provided in code");
    }
  }

  record ParameterTypeMismatch(
      int position,
      DbType<?> declared,
      JdbcMeta.ParameterMeta expected,
      Set<String> declaredTypeNames,
      String reason)
      implements AlignmentError {
    @Override
    public Str styledMessage() {
      return Str.plain("Parameter ")
          .add(Str.yellow(String.valueOf(position)))
          .add(": type mismatch\n")
          .add(Str.gray("   │ "))
          .add("Declared: ")
          .add(Str.green(declared.typename().sqlType()))
          .add(Str.gray(" (accepts: " + formatTypeNames(declaredTypeNames) + ")"))
          .add("\n")
          .add(Str.gray("   │ "))
          .add("Expected: ")
          .add(Str.red(expected.vendorTypeName()))
          .add("\n")
          .add(Str.gray("   └ "))
          .add(reason);
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Column errors
  // ─────────────────────────────────────────────────────────────────────────────

  record ExtraColumn(int position, DbType<?> type) implements AlignmentError {
    @Override
    public Str styledMessage() {
      return Str.plain("Column ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" is declared in RowCodec (")
          .add(Str.cyan(type.typename().sqlType()))
          .add(") but not returned by query");
    }
  }

  record MissingColumn(int position, JdbcMeta.ColumnMeta meta) implements AlignmentError {
    @Override
    public Str styledMessage() {
      return Str.plain("Column ")
          .add(Str.yellow(String.valueOf(position)))
          .add(" '")
          .add(Str.cyan(meta.displayName()))
          .add("' is returned by query (")
          .add(Str.red(meta.vendorTypeName()))
          .add(") but not declared in RowCodec");
    }
  }

  record ColumnTypeMismatch(
      int position,
      String columnName,
      DbType<?> declared,
      JdbcMeta.ColumnMeta returned,
      Set<String> declaredTypeNames,
      String reason)
      implements AlignmentError {
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
          .add(Str.gray(" (accepts: " + formatTypeNames(declaredTypeNames) + ")"))
          .add("\n")
          .add(Str.gray("   │ "))
          .add("Returned: ")
          .add(Str.red(returned.vendorTypeName()))
          .add("\n")
          .add(Str.gray("   └ "))
          .add(reason);
    }
  }

  /**
   * The prepared-statement call itself failed — the driver rejected the SQL before we could query
   * its parameter/column metadata. Typical on PostgreSQL when the declared parameter type is
   * incompatible with the column's type (e.g., binding {@code text} to an {@code int4} column),
   * producing {@code operator does not exist: integer = text} at prepare time.
   *
   * <p>{@code position} is 0 since we don't know which parameter the driver objected to.
   */
  record PrepareFailure(String sqlState, String driverMessage, String parsedHint)
      implements AlignmentError {
    @Override
    public int position() {
      return 0;
    }

    @Override
    public Str styledMessage() {
      var b = Str.builder();
      b.plain("Prepare failed: the database rejected the statement before parameter metadata")
          .newline()
          .gray("   │ ")
          .plain("could be read. This usually means a declared parameter type is")
          .newline()
          .gray("   │ ")
          .plain("incompatible with the column being compared.")
          .newline()
          .gray("   │ ")
          .plain("SQLSTATE: ")
          .yellow(sqlState == null ? "(unknown)" : sqlState)
          .newline()
          .gray("   │ ")
          .plain("Driver:   ")
          .red(driverMessage == null ? "(no message)" : driverMessage);
      if (parsedHint != null && !parsedHint.isEmpty()) {
        b.newline().gray("   └ ").plain(parsedHint);
      }
      return b.build();
    }
  }

  record NullabilityMismatch(int position, String columnName, DbType<?> type)
      implements AlignmentError {
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
          .add(" to make the type nullable, or ")
          .add(Str.cyan(".nullableOk()"))
          .add(" to suppress this check");
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Helpers
  // ─────────────────────────────────────────────────────────────────────────────

  private static String formatTypeNames(Set<String> names) {
    if (names.isEmpty()) {
      return "none";
    }
    return String.join(", ", names.stream().sorted().toList());
  }
}
