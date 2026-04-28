package dev.typr.foundations;

import dev.typr.foundations.internal.Str;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Result of analyzing a query against the database. Contains alignment information for parameters
 * and columns, and provides methods to extract errors and generate reports.
 *
 * <p>This is the main output of {@link QueryAnalyzer#analyze}.
 */
public record QueryAnalysis(
    String sql,
    Optional<String> queryName,
    List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> parameterAlignment,
    List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> columnAlignment,
    boolean parameterMetadataAvailable,
    Optional<AlignmentError.PrepareFailure> prepareFailure,
    int variantCount) {

  public QueryAnalysis(
      String sql,
      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> parameterAlignment,
      List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> columnAlignment,
      boolean parameterMetadataAvailable) {
    this(
        sql,
        Optional.empty(),
        parameterAlignment,
        columnAlignment,
        parameterMetadataAvailable,
        Optional.empty(),
        1);
  }

  public QueryAnalysis(
      String sql,
      Optional<String> queryName,
      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> parameterAlignment,
      List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> columnAlignment,
      boolean parameterMetadataAvailable) {
    this(
        sql,
        queryName,
        parameterAlignment,
        columnAlignment,
        parameterMetadataAvailable,
        Optional.empty(),
        1);
  }

  public QueryAnalysis(
      String sql,
      Optional<String> queryName,
      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> parameterAlignment,
      List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> columnAlignment,
      boolean parameterMetadataAvailable,
      Optional<AlignmentError.PrepareFailure> prepareFailure) {
    this(
        sql,
        queryName,
        parameterAlignment,
        columnAlignment,
        parameterMetadataAvailable,
        prepareFailure,
        1);
  }

  public QueryAnalysis withVariantCount(int variantCount) {
    return new QueryAnalysis(
        sql,
        queryName,
        parameterAlignment,
        columnAlignment,
        parameterMetadataAvailable,
        prepareFailure,
        variantCount);
  }

  /** Construct a prepare-failure analysis (metadata couldn't be read — driver rejected the SQL). */
  public static QueryAnalysis prepareFailed(
      String sql,
      Optional<String> queryName,
      List<DbType<?>> paramTypes,
      AlignmentError.PrepareFailure failure) {
    List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> declared = new ArrayList<>();
    for (DbType<?> t : paramTypes) declared.add(new Alignment.LeftOnly<>(t));
    return new QueryAnalysis(sql, queryName, declared, List.of(), false, Optional.of(failure), 1);
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
    prepareFailure.ifPresent(all::add);
    all.addAll(parameterErrors());
    all.addAll(columnErrors());
    return all;
  }

  public boolean succeeded() {
    return allErrors().isEmpty();
  }

  public String displayName() {
    if (queryName.isPresent()) return queryName.get();
    String oneLine = sql.replace('\n', ' ').replaceAll("\\s+", " ").trim();
    return oneLine.length() <= 60 ? oneLine : oneLine.substring(0, 57) + "...";
  }

  /**
   * Compact error format for use inside {@link CheckReport}: query name, SQL, and errors
   * without the header box. Returns empty for successful queries.
   */
  public Str styledErrors() {
    if (succeeded()) return Str.empty();
    var b = Str.builder();
    b.red("  ✗ ").bold(displayName()).newline();
    b.gray("    SQL: ").gray(truncateSql(sql, 68)).newline();
    for (var error : allErrors()) {
      b.plain("    ").add(error.styledMessage()).newline();
    }
    return b.build();
  }

  /** Full report with header box. Used for standalone single-query output. */
  public Str styledReport() {
    if (succeeded()) {
      var ok = Str.builder();
      ok.boldGreen("✓ ").plain(displayName());
      if (variantCount > 1) {
        ok.gray(" (" + variantCount + " variants checked)");
      }
      ok.newline();
      return ok.build();
    }

    var b = Str.builder();
    b.newline();
    b.cyan("╔══════════════════════════════════════════════════════════════════════════════╗").newline();
    b.cyan("║").bold("  Query Analysis Report                                                       ").cyan("║").newline();
    b.cyan("╚══════════════════════════════════════════════════════════════════════════════╝").newline();
    b.newline();
    b.add(styledReportBody());
    return b.build();
  }

  /** Report body without the header box. Used by {@link CheckReport} which adds its own header. */
  public Str styledReportBody() {
    var b = Str.builder();

    // SQL with failure indicator
    List<AlignmentError> errors = allErrors();
    if (!errors.isEmpty()) {
      b.red("✗ ");
    }
    if (queryName.isPresent()) {
      b.bold("SQL (").cyan(queryName.get()).bold("):").newline();
    } else {
      b.bold("SQL:").newline();
    }
    b.plain("  ").gray(truncateSql(sql, 72)).newline().newline();

    java.util.Set<Integer> paramErrorPositions = new java.util.HashSet<>();
    for (AlignmentError e : parameterErrors()) paramErrorPositions.add(e.position());
    java.util.Set<Integer> columnErrorPositions = new java.util.HashSet<>();
    for (AlignmentError e : columnErrors()) columnErrorPositions.add(e.position());

    // Parameters section
    if (!parameterAlignment.isEmpty() || !parameterMetadataAvailable) {
      b.add(formatTable("Parameters", "declared (code)", "expected (database)",
          parameterAlignment, paramErrorPositions, true, !parameterMetadataAvailable));
    }

    b.add(formatTable("Columns", "declared (code)", "returned (database)",
        columnAlignment, columnErrorPositions, false, false));


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

    String vendorText = expected.vendorTypeName();
    if (vendorText == null || vendorText.isEmpty() || "unknown".equalsIgnoreCase(vendorText)) {
      return;
    }

    // DuckDB: structural traversal. Parse the vendor typename into a DuckDbTypename tree and
    // compare against the declared tree field-by-field / element-by-element.
    if (declared.typename() instanceof DuckDbTypename<?>) {
      DuckDbTypename<Object> parsed;
      try {
        parsed = DuckDbTypenameParser.parse(vendorText);
      } catch (RuntimeException parseFail) {
        return; // Don't block on vendor text we can't parse.
      }
      if (!duckDbMatchesTopLevel(declared, parsed)) {
        errors.add(
            new AlignmentError.ParameterTypeMismatch(
                pos,
                declared,
                expected,
                Set.of(declared.typename().sqlType().toLowerCase()),
                "Declared " + declared.typename() + " does not match parameter type " + parsed));
      }
      return;
    }

    // Non-DuckDB DBs: string-normalize path (no tree parsers written yet for those).
    String metaName = normalizeVendorTypeName(vendorText);
    if (metaName.isEmpty()) return;
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

    String vendorText = returned.vendorTypeName();

    if (declared.typename() instanceof DuckDbTypename<?>
        && vendorText != null
        && !vendorText.isEmpty()
        && !"unknown".equalsIgnoreCase(vendorText)) {
      DuckDbTypename<Object> parsed;
      try {
        parsed = DuckDbTypenameParser.parse(vendorText);
      } catch (RuntimeException parseFail) {
        checkColumnNullability(declared, returned, errors, nullabilityReliable, opts);
        return;
      }
      if (!duckDbMatchesTopLevel(declared, parsed)) {
        errors.add(
            new AlignmentError.ColumnTypeMismatch(
                pos,
                returned.displayName(),
                declared,
                returned,
                Set.of(declared.typename().sqlType().toLowerCase()),
                "Declared " + declared.typename() + " does not match column type " + parsed));
      }
      checkColumnNullability(declared, returned, errors, nullabilityReliable, opts);
      return;
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
      errors.add(
          new AlignmentError.NullabilityMismatch(
              returned.position(), returned.displayName(), declared));
    }
  }

  /**
   * Top-level match between a declared {@link DbType} and a parsed {@link DuckDbTypename}. Differs
   * from {@link #duckDbTypenamesMatch} in that the TOP-LEVEL {@link DuckDbTypename.Base} case
   * consults the declared type's {@link DbType#vendorTypeNames()} alias set — so user-named ENUM
   * types ({@code color_enum} aliased to {@code enum}) and other declared aliases match the
   * vendor-reported form. Nested Base nodes inside composites fall back to structural
   * canonicalization since aliases only live on the outer {@code DbType}.
   */
  private static boolean duckDbMatchesTopLevel(DbType<?> declared, DuckDbTypename<?> parsed) {
    DuckDbTypename<?> declaredTn = (DuckDbTypename<?>) declared.typename();
    // Peel Opt wrappers — nullability is tracked separately.
    DuckDbTypename<?> unwrappedDeclared =
        (declaredTn instanceof DuckDbTypename.Opt<?> od) ? od.of() : declaredTn;
    DuckDbTypename<?> unwrappedParsed =
        (parsed instanceof DuckDbTypename.Opt<?> op) ? op.of() : parsed;
    if (unwrappedDeclared instanceof DuckDbTypename.Base<?>
        && unwrappedParsed instanceof DuckDbTypename.Base<?> pb) {
      Set<String> declaredAliases = normalizeVendorTypeNames(declared.vendorTypeNames());
      String parsedName = duckDbCanonicalBase(pb.sqlType());
      if (declaredAliases.contains(parsedName)) return true;
      for (String alias : declaredAliases) {
        if (duckDbCanonicalBase(alias).equals(parsedName)) return true;
      }
      return false;
    }
    return duckDbTypenamesMatch(unwrappedDeclared, unwrappedParsed);
  }

  /**
   * Structural compare of two DuckDB typenames. Walks STRUCT fields / UNION members / MAP key+value
   * / LIST+ARRAY element recursively, comparing field names and types. {@link
   * DuckDbTypename.StructOf#name} and {@link DuckDbTypename.UnionOf#name} are ignored — the driver
   * never surfaces the user-given CREATE TYPE name through {@code getColumnTypeName}.
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
   * structurally. Mirrors the aliases set up in {@link DuckDbTypes} (e.g. {@code text}, {@code
   * string}, {@code bpchar}, {@code char} all resolve to {@code varchar}).
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

  public static String normalizeVendorTypeName(String name) {
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
    if (suffix.isEmpty() && lower.startsWith("_") && !lower.contains(" ")) {
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

  private static final int COL_LEFT = 30;
  private static final int COL_RIGHT = 38;

  private Str formatTable(
      String title, String leftHeader, String rightHeader,
      List<? extends Alignment<DbType<?>, ?>> alignments,
      java.util.Set<Integer> errorPositions,
      boolean isParameter, boolean metadataUnavailable) {

    var b = Str.builder();
    // Top border: ┌─────...─────┬─────...─────┐ (COL_LEFT + 1 + COL_RIGHT + 1 total)
    b.gray("┌" + "─".repeat(COL_LEFT) + "┬" + "─".repeat(COL_RIGHT) + "┐").newline();
    // Header row
    b.gray("│").gray(pad(" " + leftHeader, COL_LEFT)).gray("│").gray(pad(" " + rightHeader, COL_RIGHT)).gray("│").newline();
    // Separator
    b.gray("├" + "─".repeat(COL_LEFT) + "┼" + "─".repeat(COL_RIGHT) + "┤").newline();

    if (metadataUnavailable) {
      b.gray("│").yellow(pad(" (metadata not available)", COL_LEFT)).gray("│").gray(pad("", COL_RIGHT)).gray("│").newline();
    } else if (alignments.isEmpty()) {
      b.gray("│").gray(pad(" (none)", COL_LEFT)).gray("│").gray(pad("", COL_RIGHT)).gray("│").newline();
    } else {
      String label = isParameter ? "param" : "col";
      for (int i = 0; i < alignments.size(); i++) {
        b.add(formatRow(i + 1, alignments.get(i), label, errorPositions));
      }
    }

    // Bottom border
    b.gray("└" + "─".repeat(COL_LEFT) + "┴" + "─".repeat(COL_RIGHT) + "┘").newline().newline();
    return b.build();
  }

  private Str formatRow(
      int pos, Alignment<DbType<?>, ?> align, String label,
      java.util.Set<Integer> errorPositions) {

    boolean hasError = errorPositions.contains(pos);
    String declared;
    String actual;

    switch (align) {
      case Alignment.Both(var d, var a) -> {
        declared = d.typename().sqlType();
        if (a instanceof JdbcMeta.ParameterMeta pm) {
          String vendor = pm.vendorTypeName();
          actual = (vendor == null || vendor.isEmpty()) ? "(driver does not report)" : vendor;
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
        if (a instanceof JdbcMeta.ParameterMeta pm) {
          String vendor = pm.vendorTypeName();
          actual = (vendor == null || vendor.isEmpty()) ? "(driver does not report)" : vendor;
        } else {
          JdbcMeta.ColumnMeta cm = (JdbcMeta.ColumnMeta) a;
          actual = cm.displayName() + " : " + cm.vendorTypeName();
        }
      }
    }

    // Build left cell as Str, measure its plain length, pad with spaces
    Str icon = hasError ? Str.red("✗") : Str.green("✓");
    Str declStr = hasError ? Str.green(declared) : Str.cyan(declared);
    Str leftCell = Str.plain(" ").add(icon).add(" " + label + "[" + pos + "]: ").add(declStr);
    Str rightCell = hasError ? Str.red(" " + actual) : Str.plain(" " + actual);

    var b = Str.builder();
    b.gray("│");
    b.add(leftCell);
    b.plain(spaces(COL_LEFT - leftCell.plainText().length()));
    b.gray("│");
    b.add(rightCell);
    b.plain(spaces(COL_RIGHT - rightCell.plainText().length()));
    b.gray("│").newline();
    return b.build();
  }

  private static String pad(String text, int width) {
    if (text.length() >= width) return text.substring(0, width);
    return text + " ".repeat(width - text.length());
  }

  private static String spaces(int n) {
    return n > 0 ? " ".repeat(n) : "";
  }
}
