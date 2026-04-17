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

    // For DuckDB composites, the normalize path below strips everything after the first '('
    // — so every STRUCT / MAP / UNION always normalizes to "struct"/"map"/"union" and passes
    // the string check regardless of field names or types. Run the structural parser first so
    // field-level mismatches get caught.
    String vendorText = returned.vendorTypeName();
    if (declared.typename() instanceof DuckDbTypename<?> declaredTn
        && vendorText != null
        && (vendorText.contains("STRUCT(")
            || vendorText.contains("MAP(")
            || vendorText.contains("UNION("))) {
      try {
        DuckDbTypename<Object> parsed = DuckDbTypenameParser.parse(vendorText);
        if (duckDbTypenamesMatch(declaredTn, parsed)) {
          checkColumnNullability(declared, returned, errors, nullabilityReliable, opts);
          return;
        }
        errors.add(
            new AlignmentError.ColumnTypeMismatch(
                pos,
                returned.displayName(),
                declared,
                returned,
                Set.of(declared.typename().sqlType().toLowerCase()),
                "The declared composite type does not match the returned vendor type \""
                    + vendorText
                    + "\""));
        checkColumnNullability(declared, returned, errors, nullabilityReliable, opts);
        return;
      } catch (RuntimeException parseFail) {
        // fall through to string-based path
      }
    }

    String metaName = normalizeVendorTypeName(vendorText);
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

    checkColumnNullability(declared, returned, errors, nullabilityReliable, opts);
  }

  private void checkColumnNullability(
      DbType<?> declared,
      JdbcMeta.ColumnMeta returned,
      List<AlignmentError> errors,
      boolean nullabilityReliable,
      AnalysisOptions opts) {
    if (!opts.nullableOk()
        && nullabilityReliable
        && returned.isNullabilityKnown()
        && returned.nullable() == ResultSetMetaData.columnNullable
        && !declared.isNullable()) {
      errors.add(new AlignmentError.NullabilityMismatch(returned.position(), returned.displayName(), declared));
    }
  }

  /**
   * Structural compare of two DuckDB typenames. Walks STRUCT fields / UNION members / MAP
   * key+value / LIST+ARRAY element recursively, comparing field names and types. {@link
   * DuckDbTypename.StructOf#name} and {@link DuckDbTypename.UnionOf#name} are ignored — the
   * driver never surfaces the user-given CREATE TYPE name through {@code getColumnTypeName}.
   */
  static boolean duckDbTypenamesMatch(DuckDbTypename<?> a, DuckDbTypename<?> b) {
    if (a instanceof DuckDbTypename.Opt<?> oa) return duckDbTypenamesMatch(oa.of(), b);
    if (b instanceof DuckDbTypename.Opt<?> ob) return duckDbTypenamesMatch(a, ob.of());
    if (a instanceof DuckDbTypename.ListOf<?> la && b instanceof DuckDbTypename.ListOf<?> lb) {
      return duckDbTypenamesMatch(la.elementType(), lb.elementType());
    }
    if (a instanceof DuckDbTypename.ArrayOf<?> aa && b instanceof DuckDbTypename.ArrayOf<?> ab) {
      return aa.size() == ab.size() && duckDbTypenamesMatch(aa.elementType(), ab.elementType());
    }
    if (a instanceof DuckDbTypename.MapOf<?, ?> ma && b instanceof DuckDbTypename.MapOf<?, ?> mb) {
      return duckDbTypenamesMatch(ma.keyType(), mb.keyType())
          && duckDbTypenamesMatch(ma.valueType(), mb.valueType());
    }
    if (a instanceof DuckDbTypename.StructOf<?> sa && b instanceof DuckDbTypename.StructOf<?> sb) {
      if (sa.fields().size() != sb.fields().size()) return false;
      for (int i = 0; i < sa.fields().size(); i++) {
        var fa = sa.fields().get(i);
        var fb = sb.fields().get(i);
        if (!fa.name().equals(fb.name())) return false;
        if (!duckDbTypenamesMatch(fa.type(), fb.type())) return false;
      }
      return true;
    }
    if (a instanceof DuckDbTypename.UnionOf<?> ua && b instanceof DuckDbTypename.UnionOf<?> ub) {
      if (ua.members().size() != ub.members().size()) return false;
      for (int i = 0; i < ua.members().size(); i++) {
        var ma = ua.members().get(i);
        var mb = ub.members().get(i);
        if (!ma.tag().equals(mb.tag())) return false;
        if (!duckDbTypenamesMatch(ma.type(), mb.type())) return false;
      }
      return true;
    }
    if (a instanceof DuckDbTypename.Base<?> ba && b instanceof DuckDbTypename.Base<?> bb) {
      return duckDbCanonicalBase(ba.sqlType()).equals(duckDbCanonicalBase(bb.sqlType()));
    }
    return false;
  }

  /**
   * Canonicalize a DuckDB base type name so aliases that resolve to the same storage form match
   * structurally. Mirrors the aliases set up in {@link DuckDbTypes} (e.g. {@code text},
   * {@code string}, {@code bpchar}, {@code char} all resolve to {@code varchar}).
   */
  private static String duckDbCanonicalBase(String sqlType) {
    String head = normalizeVendorTypeName(sqlType);
    return switch (head) {
      case "text", "string", "char_", "bpchar", "char" -> "varchar";
      case "float", "float4", "real" -> "float";
      case "float8", "double" -> "double";
      case "int", "int4" -> "integer";
      case "int2" -> "smallint";
      case "int8" -> "bigint";
      case "int1" -> "tinyint";
      case "bool" -> "boolean";
      case "numeric" -> "decimal";
      default -> head;
    };
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

    // Peel trailing LIST/ARRAY suffixes — `[]`, `[]`-chains, and DuckDB fixed-size `[N]` — from
    // the right so we can strip precision on the scalar head and re-append suffixes unchanged.
    StringBuilder suffix = new StringBuilder();
    while (true) {
      int len = lower.length();
      if (len >= 2 && lower.endsWith("[]")) {
        suffix.insert(0, "[]");
        lower = lower.substring(0, len - 2);
        continue;
      }
      int close = lower.lastIndexOf(']');
      if (close == len - 1) {
        int open = lower.lastIndexOf('[');
        if (open > 0 && lower.substring(open + 1, close).matches("\\d+")) {
          suffix.insert(0, lower.substring(open));
          lower = lower.substring(0, open);
          continue;
        }
      }
      break;
    }

    // Strip precision specifier like "varchar(255)" -> "varchar", "decimal(10,2)" -> "decimal"
    int parenIdx = lower.indexOf('(');
    if (parenIdx > 0) {
      lower = lower.substring(0, parenIdx);
    }

    // Handle PG array prefix: "_int4" -> "int4[]"
    if (suffix.length() == 0 && lower.startsWith("_") && !lower.contains(" ")) {
      lower = lower.substring(1);
      suffix.append("[]");
    }

    // PG stores all N-dim arrays under a single array type ({@code _T}, reported as
    // {@code T[]}). Our declared typename can include an explicit {@code [][]…} chain to
    // surface intent, but for analysis we must collapse pure {@code []} chains to a single
    // {@code []} so declared multi-dim types match the returned vendor type. Fixed-size
    // suffixes like {@code [N]} (DuckDB ARRAY) are preserved because they convey a real
    // distinction at the storage layer.
    String suffixStr = suffix.toString();
    if (!suffixStr.isEmpty() && suffixStr.chars().allMatch(c -> c == '[' || c == ']')) {
      suffixStr = "[]";
    }

    return lower + suffixStr;
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
