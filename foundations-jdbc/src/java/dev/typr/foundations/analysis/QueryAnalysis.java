package dev.typr.foundations.analysis;

import dev.typr.foundations.DbType;
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
   * Generate a human-readable report of the analysis.
   */
  public String report() {
    StringBuilder sb = new StringBuilder();

    // Header
    sb.append("\n");
    sb.append("╔══════════════════════════════════════════════════════════════════════════════╗\n");
    sb.append("║  Query Analysis Report                                                       ║\n");
    sb.append("╚══════════════════════════════════════════════════════════════════════════════╝\n");
    sb.append("\n");

    // SQL
    sb.append("SQL:\n");
    sb.append("  ").append(truncateSql(sql, 72)).append("\n\n");

    // Parameters section
    sb.append("┌─ Parameters ");
    if (!parameterMetadataAvailable) {
      sb.append("(metadata not available) ");
    }
    sb.append("─".repeat(Math.max(1, 65 - (parameterMetadataAvailable ? 0 : 26)))).append("┐\n");
    if (parameterAlignment.isEmpty()) {
      sb.append("│  (none)").append(" ".repeat(70)).append("│\n");
    } else {
      for (int i = 0; i < parameterAlignment.size(); i++) {
        var align = parameterAlignment.get(i);
        sb.append(formatAlignment(i + 1, align, true));
      }
    }
    sb.append("└").append("─".repeat(78)).append("┘\n\n");

    // Columns section
    sb.append("┌─ Columns ").append("─".repeat(68)).append("┐\n");
    if (columnAlignment.isEmpty()) {
      sb.append("│  (none)").append(" ".repeat(70)).append("│\n");
    } else {
      for (int i = 0; i < columnAlignment.size(); i++) {
        var align = columnAlignment.get(i);
        sb.append(formatAlignment(i + 1, align, false));
      }
    }
    sb.append("└").append("─".repeat(78)).append("┘\n\n");

    // Errors section
    List<AlignmentError> errors = allErrors();
    if (errors.isEmpty()) {
      sb.append("✓ No errors found\n");
    } else {
      sb.append("✗ ").append(errors.size()).append(" error(s) found:\n\n");
      for (int i = 0; i < errors.size(); i++) {
        sb.append("  ").append(i + 1).append(". ").append(errors.get(i).message()).append("\n\n");
      }
    }

    return sb.toString();
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

  private String formatAlignment(int pos, Alignment<DbType<?>, ?> align, boolean isParameter) {
    String status;
    String declared = "";
    String actual = "";

    switch (align) {
      case Alignment.Both(var d, var a) -> {
        status = "✓";
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
        status = "✗";
        declared = d.typename().sqlType();
        actual = "(missing)";
      }
      case Alignment.RightOnly(var a) -> {
        status = "✗";
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
    String line = String.format("│  %s %s[%d]: %-20s → %-30s", status, label, pos, declared, actual);
    // Pad to fixed width
    if (line.length() < 79) {
      line = line + " ".repeat(79 - line.length());
    } else if (line.length() > 78) {
      line = line.substring(0, 75) + "...";
    }
    return line + "│\n";
  }
}
