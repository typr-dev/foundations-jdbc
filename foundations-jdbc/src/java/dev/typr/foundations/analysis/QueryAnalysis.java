package dev.typr.foundations.analysis;

import dev.typr.foundations.DbType;
import dev.typr.foundations.Str;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Result of analyzing a query against the database. Contains alignment information
 * for parameters and columns, and provides methods to extract errors and generate reports.
 *
 * <p>This is the main output of {@link QueryAnalyzer#analyze}.</p>
 *
 * <p>Example usage:
 * <pre>{@code
 * QueryAnalysis analysis = QueryAnalyzer.analyze(query, connection);
 * if (!analysis.succeeded()) {
 *     System.err.println(analysis.report());
 *     throw new AssertionError("Query type check failed");
 * }
 * }</pre>
 */
public record QueryAnalysis(
    String sql,
    List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> parameterAlignment,
    List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> columnAlignment,
    boolean parameterMetadataAvailable
) {

  /**
   * Check all parameter alignments and return any errors found.
   */
  public List<AlignmentError> parameterErrors() {
    if (!parameterMetadataAvailable) {
      return List.of();
    }

    List<AlignmentError> errors = new ArrayList<>();
    int pos = 1;
    for (Alignment<DbType<?>, JdbcMeta.ParameterMeta> alignment : parameterAlignment) {
      switch (alignment) {
        case Alignment.Both<DbType<?>, JdbcMeta.ParameterMeta>(var declared, var expected) -> {
          checkParameterTypes(pos, declared, expected, errors);
        }
        case Alignment.LeftOnly<DbType<?>, JdbcMeta.ParameterMeta>(var declared) -> {
          errors.add(new AlignmentError.ExtraParameter(pos, declared));
        }
        case Alignment.RightOnly<DbType<?>, JdbcMeta.ParameterMeta>(var expected) -> {
          errors.add(new AlignmentError.MissingParameter(pos, expected));
        }
      }
      pos++;
    }
    return errors;
  }

  /**
   * Check all column alignments and return any errors found.
   */
  public List<AlignmentError> columnErrors() {
    List<AlignmentError> errors = new ArrayList<>();

    // Check if nullability metadata is reliable.
    // Some databases (like DuckDB) report ALL columns as nullable regardless of NOT NULL constraints.
    // If every column with metadata reports as nullable, we assume nullability info is unreliable.
    boolean nullabilityReliable = isNullabilityReliable();

    int pos = 1;
    for (Alignment<DbType<?>, JdbcMeta.ColumnMeta> alignment : columnAlignment) {
      switch (alignment) {
        case Alignment.Both<DbType<?>, JdbcMeta.ColumnMeta>(var declared, var returned) -> {
          checkColumnTypes(pos, declared, returned, errors, nullabilityReliable);
        }
        case Alignment.LeftOnly<DbType<?>, JdbcMeta.ColumnMeta>(var declared) -> {
          errors.add(new AlignmentError.ExtraColumn(pos, declared));
        }
        case Alignment.RightOnly<DbType<?>, JdbcMeta.ColumnMeta>(var returned) -> {
          errors.add(new AlignmentError.MissingColumn(pos, returned));
        }
      }
      pos++;
    }
    return errors;
  }

  /**
   * Check if nullability metadata from the database is reliable.
   * If all columns report as nullable, it's likely the driver doesn't track NOT NULL constraints.
   */
  private boolean isNullabilityReliable() {
    if (columnAlignment.isEmpty()) return true;

    int nullableCount = 0;
    int totalWithMeta = 0;

    for (var alignment : columnAlignment) {
      if (alignment instanceof Alignment.Both(var declared, var meta)) {
        JdbcMeta.ColumnMeta cm = (JdbcMeta.ColumnMeta) meta;
        if (cm.isNullabilityKnown()) {
          totalWithMeta++;
          if (cm.nullable() == ResultSetMetaData.columnNullable) {
            nullableCount++;
          }
        }
      }
    }

    // If we have metadata and at least one column is NOT nullable, metadata is reliable
    // If ALL columns are nullable, the driver probably doesn't track NOT NULL constraints
    return totalWithMeta == 0 || nullableCount < totalWithMeta;
  }

  /**
   * Get all errors from both parameters and columns.
   */
  public List<AlignmentError> allErrors() {
    List<AlignmentError> all = new ArrayList<>();
    all.addAll(parameterErrors());
    all.addAll(columnErrors());
    return all;
  }

  /**
   * Returns true if no errors were found.
   */
  public boolean succeeded() {
    return allErrors().isEmpty();
  }

  /**
   * Generate a styled report of the analysis.
   * Use {@link #reportColored()} for ANSI colors or {@link #report()} for plain text.
   */
  public Str styledReport() {
    var b = Str.builder();

    // Header
    b.newline();
    b.cyan("╔══════════════════════════════════════════════════════════════════════════════╗").newline();
    b.cyan("║").bold("  Query Analysis Report                                                       ").cyan("║").newline();
    b.cyan("╚══════════════════════════════════════════════════════════════════════════════╝").newline();
    b.newline();

    // SQL
    b.bold("SQL:").newline();
    b.plain("  ").gray(truncateSql(sql, 72)).newline().newline();

    // Parameters section
    b.gray("┌─ ").bold("Parameters ");
    if (!parameterMetadataAvailable) {
      b.yellow("(metadata not available) ");
    }
    b.gray("─".repeat(Math.max(1, 65 - (parameterMetadataAvailable ? 0 : 26))) + "┐").newline();
    if (parameterAlignment.isEmpty()) {
      b.gray("│  (none)" + " ".repeat(70) + "│").newline();
    } else {
      for (int i = 0; i < parameterAlignment.size(); i++) {
        var align = parameterAlignment.get(i);
        b.add(formatStyledAlignment(i + 1, align, true));
      }
    }
    b.gray("└" + "─".repeat(78) + "┘").newline().newline();

    // Columns section
    b.gray("┌─ ").bold("Columns ").gray("─".repeat(68) + "┐").newline();
    if (columnAlignment.isEmpty()) {
      b.gray("│  (none)" + " ".repeat(70) + "│").newline();
    } else {
      for (int i = 0; i < columnAlignment.size(); i++) {
        var align = columnAlignment.get(i);
        b.add(formatStyledAlignment(i + 1, align, false));
      }
    }
    b.gray("└" + "─".repeat(78) + "┘").newline().newline();

    // Errors section
    List<AlignmentError> errors = allErrors();
    if (errors.isEmpty()) {
      b.boldGreen("✓ No errors found").newline();
    } else {
      b.boldRed("✗ " + errors.size() + " error(s) found:").newline().newline();
      for (int i = 0; i < errors.size(); i++) {
        b.plain("  ").yellow(String.valueOf(i + 1)).plain(". ").add(errors.get(i).styledMessage()).newline().newline();
      }
    }

    return b.build();
  }

  /**
   * Generate a human-readable report of the analysis (plain text).
   */
  public String report() {
    return styledReport().plainText();
  }

  /**
   * Generate a human-readable report with ANSI colors.
   */
  public String reportColored() {
    return styledReport().render();
  }

  private void checkParameterTypes(int pos, DbType<?> declared, JdbcMeta.ParameterMeta expected,
      List<AlignmentError> errors) {
    Set<Integer> targets = declared.jdbcTargets();

    // Check if our type can write to the expected JDBC type
    if (!targets.isEmpty() && !targets.contains(expected.jdbcType())) {
      // Special case: if we're writing OTHER, most types work
      if (expected.jdbcType() != java.sql.Types.OTHER) {
        errors.add(new AlignmentError.ParameterTypeMismatch(
            pos, declared, expected, targets,
            "The declared type cannot write to " + JdbcMeta.jdbcTypeName(expected.jdbcType())
        ));
      }
    }
  }

  private void checkColumnTypes(int pos, DbType<?> declared, JdbcMeta.ColumnMeta returned,
      List<AlignmentError> errors, boolean nullabilityReliable) {
    Set<Integer> sources = declared.jdbcSources();

    // Check if our type can read from the returned JDBC type
    if (!sources.isEmpty() && !sources.contains(returned.jdbcType())) {
      // Special case: OTHER is often used for custom types
      if (returned.jdbcType() != java.sql.Types.OTHER) {
        errors.add(new AlignmentError.ColumnTypeMismatch(
            pos,
            returned.displayName(),
            declared,
            returned,
            sources,
            "The declared type cannot read from " + JdbcMeta.jdbcTypeName(returned.jdbcType())
        ));
      }
    }

    // Check nullability: if column is nullable but type is not optional
    // Only check if the database's nullability metadata is reliable
    if (nullabilityReliable
        && returned.isNullabilityKnown()
        && returned.nullable() == ResultSetMetaData.columnNullable
        && !declared.isNullable()) {
      errors.add(new AlignmentError.NullabilityMismatch(
          pos, returned.displayName(), declared
      ));
    }
  }

  private String truncateSql(String sql, int maxLen) {
    String oneLine = sql.replace('\n', ' ').replaceAll("\\s+", " ").trim();
    if (oneLine.length() <= maxLen) {
      return oneLine;
    }
    return oneLine.substring(0, maxLen - 3) + "...";
  }

  private Str formatStyledAlignment(int pos, Alignment<DbType<?>, ?> align, boolean isParameter) {
    boolean ok;
    String declared;
    String actual;

    switch (align) {
      case Alignment.Both(var d, var a) -> {
        ok = true;
        declared = d.typename().sqlType();
        if (isParameter) {
          JdbcMeta.ParameterMeta pm = (JdbcMeta.ParameterMeta) a;
          actual = pm.vendorTypeName();
        } else {
          JdbcMeta.ColumnMeta cm = (JdbcMeta.ColumnMeta) a;
          actual = cm.displayName() + " : " + cm.vendorTypeName();
        }
      }
      case Alignment.LeftOnly(var d) -> {
        ok = false;
        declared = d.typename().sqlType();
        actual = "(missing)";
      }
      case Alignment.RightOnly(var a) -> {
        ok = false;
        declared = "(missing)";
        if (isParameter) {
          JdbcMeta.ParameterMeta pm = (JdbcMeta.ParameterMeta) a;
          actual = pm.vendorTypeName();
        } else {
          JdbcMeta.ColumnMeta cm = (JdbcMeta.ColumnMeta) a;
          actual = cm.displayName() + " : " + cm.vendorTypeName();
        }
      }
    }

    String label = isParameter ? "param" : "col";
    var b = Str.builder();
    b.gray("│  ");
    if (ok) {
      b.green("✓");
    } else {
      b.red("✗");
    }
    b.plain(" " + label + "[").yellow(String.valueOf(pos)).plain("]: ");

    // Format declared type
    String declaredPadded = String.format("%-20s", declared);
    if (ok || !declared.equals("(missing)")) {
      b.cyan(declaredPadded);
    } else {
      b.red(declaredPadded);
    }

    b.gray(" → ");

    // Format actual type
    String actualPadded = String.format("%-30s", actual);
    if (ok || !actual.equals("(missing)")) {
      b.plain(actualPadded);
    } else {
      b.red(actualPadded);
    }

    // Pad to fixed width and close
    String line = b.build().plainText();
    int padding = 78 - line.length();
    if (padding > 0) {
      b.plain(" ".repeat(padding));
    }
    b.gray("│").newline();

    return b.build();
  }
}
