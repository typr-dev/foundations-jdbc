package dev.typr.foundations;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Result of analyzing a query against the database. Contains alignment information for parameters
 * and columns, and provides methods to extract errors and generate reports.
 *
 * <p>This is the main output of {@link QueryAnalyzer#analyze}.
 */
public record QueryAnalysis(
    String sql,
    String queryName,
    List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> parameterAlignment,
    List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> columnAlignment,
    boolean parameterMetadataAvailable) {

  public QueryAnalysis(
      String sql,
      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> parameterAlignment,
      List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> columnAlignment,
      boolean parameterMetadataAvailable) {
    this(sql, null, parameterAlignment, columnAlignment, parameterMetadataAvailable);
  }

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

  public List<AlignmentError> columnErrors() {
    List<AlignmentError> errors = new ArrayList<>();
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

    return totalWithMeta == 0 || nullableCount < totalWithMeta;
  }

  public List<AlignmentError> allErrors() {
    List<AlignmentError> all = new ArrayList<>();
    all.addAll(parameterErrors());
    all.addAll(columnErrors());
    return all;
  }

  public boolean succeeded() {
    return allErrors().isEmpty();
  }

  public Str styledReport() {
    var b = Str.builder();

    // Header
    b.newline();
    b.cyan("╔══════════════════════════════════════════════════════════════════════════════╗")
        .newline();
    b.cyan("║")
        .bold("  Query Analysis Report                                                       ")
        .cyan("║")
        .newline();
    b.cyan("╚══════════════════════════════════════════════════════════════════════════════╝")
        .newline();
    b.newline();

    // SQL
    if (queryName != null) {
      b.bold("SQL (").cyan(queryName).bold("):").newline();
    } else {
      b.bold("SQL:").newline();
    }
    b.plain("  ").gray(truncateSql(sql, 72)).newline().newline();

    java.util.Set<Integer> paramErrorPositions = new java.util.HashSet<>();
    for (AlignmentError e : parameterErrors()) paramErrorPositions.add(e.position());
    java.util.Set<Integer> columnErrorPositions = new java.util.HashSet<>();
    for (AlignmentError e : columnErrors()) columnErrorPositions.add(e.position());

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
        b.add(formatStyledAlignment(i + 1, align, true, paramErrorPositions));
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
        b.add(formatStyledAlignment(i + 1, align, false, columnErrorPositions));
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
        b.plain("  ")
            .yellow(String.valueOf(i + 1))
            .plain(". ")
            .add(errors.get(i).styledMessage())
            .newline()
            .newline();
      }
    }

    return b.build();
  }

  public String report() {
    return styledReport().plainText();
  }

  public String reportColored() {
    return styledReport().render();
  }

  private void checkParameterTypes(
      int pos, DbType<?> declared, JdbcMeta.ParameterMeta expected, List<AlignmentError> errors) {
    AnalysisOptions opts = declared.analysisOptions();
    if (opts.unchecked()) return;

    String metaName = normalizeVendorTypeName(expected.vendorTypeName());
    if (metaName.isEmpty() || "unknown".equals(metaName)) return;

    Set<String> ours = normalizeVendorTypeNames(declared.vendorTypeNames());
    if (!ours.isEmpty() && !ours.contains(metaName)) {
      errors.add(
          new AlignmentError.ParameterTypeMismatch(
              pos,
              declared,
              expected,
              ours,
              "The declared type does not match the expected vendor type \"" + metaName + "\""));
    }
  }

  private void checkColumnTypes(
      int pos,
      DbType<?> declared,
      JdbcMeta.ColumnMeta returned,
      List<AlignmentError> errors,
      boolean nullabilityReliable) {
    AnalysisOptions opts = declared.analysisOptions();
    if (opts.unchecked()) return;

    String metaName = normalizeVendorTypeName(returned.vendorTypeName());
    if (!metaName.isEmpty() && !"unknown".equals(metaName)) {
      Set<String> ours = normalizeVendorTypeNames(declared.vendorTypeNames());
      if (!ours.isEmpty() && !ours.contains(metaName)) {
        errors.add(
            new AlignmentError.ColumnTypeMismatch(
                pos,
                returned.displayName(),
                declared,
                returned,
                ours,
                "The declared type does not match the returned vendor type \"" + metaName + "\""));
      }
    }

    // Check nullability
    if (!opts.nullableOk()
        && nullabilityReliable
        && returned.isNullabilityKnown()
        && returned.nullable() == ResultSetMetaData.columnNullable
        && !declared.isNullable()) {
      errors.add(new AlignmentError.NullabilityMismatch(pos, returned.displayName(), declared));
    }
  }

  static Set<String> normalizeVendorTypeNames(Set<String> names) {
    var normalized = new java.util.HashSet<String>(names.size());
    for (String name : names) {
      normalized.add(normalizeVendorTypeName(name));
    }
    return Set.copyOf(normalized);
  }

  static String normalizeVendorTypeName(String name) {
    if (name == null || name.isEmpty()) return "";
    String lower = name.toLowerCase().trim();

    // Strip SQL identifier quotes: "\"mood\"" -> "mood"
    lower = lower.replace("\"", "");

    // Strip schema prefix: "typr.address_t" -> "address_t" (Oracle reports UDTs schema-qualified)
    int dotIdx = lower.lastIndexOf('.');
    if (dotIdx >= 0) {
      lower = lower.substring(dotIdx + 1);
    }

    // Preserve array suffix before stripping precision
    String suffix = "";
    if (lower.endsWith("[]")) {
      suffix = "[]";
      lower = lower.substring(0, lower.length() - 2);
    }

    // Strip precision specifier like "varchar(255)" -> "varchar", "decimal(10,2)" -> "decimal"
    int parenIdx = lower.indexOf('(');
    if (parenIdx > 0) {
      lower = lower.substring(0, parenIdx);
    }

    // Handle PG array prefix: "_int4" -> "int4[]"
    if (suffix.isEmpty() && lower.startsWith("_") && !lower.contains(" ")) {
      lower = lower.substring(1);
      suffix = "[]";
    }

    return lower + suffix;
  }

  private String truncateSql(String sql, int maxLen) {
    String oneLine = sql.replace('\n', ' ').replaceAll("\\s+", " ").trim();
    if (oneLine.length() <= maxLen) {
      return oneLine;
    }
    return oneLine.substring(0, maxLen - 3) + "...";
  }

  private Str formatStyledAlignment(
      int pos,
      Alignment<DbType<?>, ?> align,
      boolean isParameter,
      java.util.Set<Integer> errorPositions) {
    boolean hasError = errorPositions.contains(pos);
    boolean vendorMissing = false;
    String declared;
    String actual;

    switch (align) {
      case Alignment.Both(var d, var a) -> {
        declared = d.typename().sqlType();
        if (isParameter) {
          JdbcMeta.ParameterMeta pm = (JdbcMeta.ParameterMeta) a;
          String vendor = pm.vendorTypeName();
          if (vendor == null || vendor.isEmpty()) {
            vendorMissing = true;
            actual = "(driver does not report)";
          } else {
            actual = vendor;
          }
        } else {
          JdbcMeta.ColumnMeta cm = (JdbcMeta.ColumnMeta) a;
          actual = cm.displayName() + " : " + cm.vendorTypeName();
        }
      }
      case Alignment.LeftOnly(var d) -> {
        declared = d.typename().sqlType();
        actual = "(missing)";
      }
      case Alignment.RightOnly(var a) -> {
        declared = "(missing)";
        if (isParameter) {
          JdbcMeta.ParameterMeta pm = (JdbcMeta.ParameterMeta) a;
          String vendor = pm.vendorTypeName();
          if (vendor == null || vendor.isEmpty()) {
            vendorMissing = true;
            actual = "(driver does not report)";
          } else {
            actual = vendor;
          }
        } else {
          JdbcMeta.ColumnMeta cm = (JdbcMeta.ColumnMeta) a;
          actual = cm.displayName() + " : " + cm.vendorTypeName();
        }
      }
    }

    boolean skipped = (isParameter && !parameterMetadataAvailable) || vendorMissing;

    String label = isParameter ? "param" : "col";
    var b = Str.builder();
    b.gray("│  ");
    if (skipped) {
      b.gray("·");
    } else if (hasError) {
      b.red("✗");
    } else {
      b.green("✓");
    }
    b.plain(" " + label + "[").yellow(String.valueOf(pos)).plain("]: ");

    String declaredPadded = String.format("%-20s", declared);
    if (!hasError || !declared.equals("(missing)")) {
      b.cyan(declaredPadded);
    } else {
      b.red(declaredPadded);
    }

    b.gray(" → ");

    String actualPadded = String.format("%-30s", actual);
    if (!hasError || !actual.equals("(missing)")) {
      b.plain(actualPadded);
    } else {
      b.red(actualPadded);
    }

    String line = b.build().plainText();
    int padding = 78 - line.length();
    if (padding > 0) {
      b.plain(" ".repeat(padding));
    }
    b.gray("│").newline();

    return b.build();
  }
}
