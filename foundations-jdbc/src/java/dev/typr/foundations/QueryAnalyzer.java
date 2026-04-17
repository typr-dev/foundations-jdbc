package dev.typr.foundations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class QueryAnalyzer {

  private QueryAnalyzer() {}

  public static List<QueryAnalysis> analyze(Analyzable analyzable, Connection conn) {
    try {
      return switch (analyzable) {
        case Analyzable.Named(var defaultName, var inner) ->
            applyDefaultName(defaultName, analyze(inner, conn));
        case Operation<?> op -> analyzeChecked(op, conn);
        case Template<?, ?> t -> analyzeChecked(t, conn);
      };
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  private static List<QueryAnalysis> applyDefaultName(
      String defaultName, List<QueryAnalysis> results) {
    return results.stream()
        .map(
            r ->
                r.queryName() == null
                    ? new QueryAnalysis(
                        r.sql(),
                        defaultName,
                        r.parameterAlignment(),
                        r.columnAlignment(),
                        r.parameterMetadataAvailable())
                    : r)
        .toList();
  }

  public static List<QueryAnalysis> analyze(Template<?, ?> template, Connection conn) {
    try {
      return analyzeChecked(template, conn);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  private static List<QueryAnalysis> analyzeChecked(Template<?, ?> template, Connection conn)
      throws SQLException {
    Fragment fragment = template.fragment();
    List<Fragment> variants = OptionallyResolver.analysisVariants(fragment);
    ResultSetParser<?> parser = extractResultSetParser(template);
    List<QueryAnalysis> results = new ArrayList<>();
    for (Fragment variant : variants) {
      if (parser != null) {
        results.add(analyzeFragmentAndParserChecked(variant, parser, conn));
      } else {
        results.add(analyzeUpdate(new Operation.Update(variant), conn));
      }
    }
    return results;
  }

  public static List<QueryAnalysis> analyze(Operation<?> op, Connection conn) {
    try {
      return analyzeChecked(op, conn);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  private static List<QueryAnalysis> analyzeChecked(Operation<?> op, Connection conn)
      throws SQLException {
    return analyzeNamed(null, op, conn);
  }

  private static List<QueryAnalysis> analyzeNamed(String name, Operation<?> op, Connection conn)
      throws SQLException {
    return switch (op) {
      case Operation.Query<?> q ->
          List.of(analyzeFragmentAndParserChecked(name, q.query(), q.parser(), conn));
      case Operation.UpdateReturning<?> ur ->
          List.of(analyzeFragmentAndParserChecked(name, ur.query(), ur.parser(), conn));
      case Operation.Update u -> List.of(analyzeUpdate(name, u, conn));
      case Operation.Configured<?> c ->
          analyzeNamed(name != null ? name : c.name(), c.inner(), conn);
      case Operation.Mapped<?, ?> m -> analyzeNamed(name, m.source(), conn);
      case Operation.Combine<?, ?> w -> {
        var r = new ArrayList<>(analyzeNamed(null, w.first(), conn));
        r.addAll(analyzeNamed(null, w.second(), conn));
        yield r;
      }
      case Operation.IfEmpty<?> ie -> {
        var r = new ArrayList<>(analyzeNamed(null, ie.check(), conn));
        r.addAll(analyzeNamed(null, ie.fallback(), conn));
        yield r;
      }
      case Operation.Then<?, ?, ?> t -> {
        var r = new ArrayList<>(analyzeNamed(null, t.source(), conn));
        Fragment templateFragment = t.continuation().fragment();
        List<DbType<?>> paramTypes = templateFragment.parameterTypes();
        String sql = templateFragment.render();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
          List<JdbcMeta.ParameterMeta> paramMeta = JdbcMeta.extractParameters(ps);
          List<JdbcMeta.ColumnMeta> colMeta = JdbcMeta.extractColumns(ps);
          boolean paramMetaAvailable = !paramMeta.isEmpty() || paramTypes.isEmpty();
          List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> paramAlignment =
              Alignment.align(paramTypes, paramMeta);
          ResultSetParser<?> templateParser = extractResultSetParser(t.continuation());
          List<DbType<?>> columnTypes =
              templateParser != null ? extractColumnTypes(templateParser) : List.of();
          List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> colAlignment =
              Alignment.align(columnTypes, colMeta);
          r.add(new QueryAnalysis(sql, null, paramAlignment, colAlignment, paramMetaAvailable));
        }
        yield r;
      }
      case Operation.Streaming<?> s ->
          List.of(analyzeFragmentAndParserChecked(name, s.query(), s.codec().all(), conn));
      case Operation.Execute e ->
          List.of(analyzeUpdate(name, new Operation.Update(e.query()), conn));
      case Operation.Pure<?> ignored -> List.of();
      default -> List.of();
    };
  }

  public static QueryAnalysis analyzeFragmentAndParser(
      Fragment fragment, ResultSetParser<?> parser, Connection conn) {
    try {
      return analyzeFragmentAndParserChecked(null, fragment, parser, conn);
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  private static QueryAnalysis analyzeFragmentAndParserChecked(
      Fragment fragment, ResultSetParser<?> parser, Connection conn) throws SQLException {
    return analyzeFragmentAndParserChecked(null, fragment, parser, conn);
  }

  private static QueryAnalysis analyzeFragmentAndParserChecked(
      String name, Fragment fragment, ResultSetParser<?> parser, Connection conn)
      throws SQLException {

    String sql = fragment.render();
    List<DbType<?>> paramTypes = fragment.parameterTypes();
    List<DbType<?>> columnTypes = extractColumnTypes(parser);

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      List<JdbcMeta.ParameterMeta> paramMeta = JdbcMeta.extractParameters(ps);
      List<JdbcMeta.ColumnMeta> colMeta = JdbcMeta.extractColumns(ps);

      boolean paramMetaAvailable = !paramMeta.isEmpty() || paramTypes.isEmpty();

      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> paramAlignment =
          Alignment.align(paramTypes, paramMeta);
      List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> colAlignment =
          Alignment.align(columnTypes, colMeta);

      return new QueryAnalysis(sql, name, paramAlignment, colAlignment, paramMetaAvailable);
    }
  }

  private static QueryAnalysis analyzeUpdate(Operation.Update update, Connection conn)
      throws SQLException {
    return analyzeUpdate(null, update, conn);
  }

  private static QueryAnalysis analyzeUpdate(String name, Operation.Update update, Connection conn)
      throws SQLException {
    Fragment fragment = update.query();
    String sql = fragment.render();
    List<DbType<?>> paramTypes = fragment.parameterTypes();

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      List<JdbcMeta.ParameterMeta> paramMeta = JdbcMeta.extractParameters(ps);

      // Target-table-schema fallback: when the driver reports no USABLE parameter metadata but
      // we have declared parameters AND the SQL is an INSERT with an explicit column list,
      // pull the table schema via DatabaseMetaData.getColumns and synthesize parameter
      // metadata from the matching columns. Catches STRUCT-field mismatches on INSERT
      // parameters that would otherwise be invisible on DuckDB, and in principle supports
      // reporting extra/missing codec columns.
      //
      // "Not usable" means: the driver returned entries whose vendor type name is null/empty
      // (DuckDB) OR returned zero entries while we have declared params.
      if (!paramTypes.isEmpty() && !paramMetaHasVendorNames(paramMeta)) {
        Optional<List<JdbcMeta.ParameterMeta>> synthesized =
            insertParameterMetaFromTableSchema(sql, paramTypes.size(), conn);
        if (synthesized.isPresent()) {
          paramMeta = synthesized.get();
        }
      }

      boolean paramMetaAvailable = !paramMeta.isEmpty() || paramTypes.isEmpty();

      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> paramAlignment =
          Alignment.align(paramTypes, paramMeta);

      return new QueryAnalysis(sql, name, paramAlignment, List.of(), paramMetaAvailable);
    }
  }

  private static boolean paramMetaHasVendorNames(List<JdbcMeta.ParameterMeta> paramMeta) {
    if (paramMeta.isEmpty()) return false;
    for (JdbcMeta.ParameterMeta pm : paramMeta) {
      String v = pm.vendorTypeName();
      if (v != null && !v.isEmpty()) return true;
    }
    return false;
  }

  private static final java.util.regex.Pattern INSERT_COLUMN_LIST =
      java.util.regex.Pattern.compile(
          "^\\s*INSERT\\s+INTO\\s+(\"[^\"]+\"|[a-zA-Z_][\\w.]*)\\s*\\(([^)]+)\\)\\s+VALUES\\b",
          java.util.regex.Pattern.CASE_INSENSITIVE);

  /**
   * If {@code sql} is an {@code INSERT INTO <table> (<cols>) VALUES ...} statement, pull the
   * target table's schema via {@link java.sql.DatabaseMetaData#getColumns} and synthesize one
   * ParameterMeta per codec column by looking each codec column up by name. Returns
   * {@code Optional.empty()} when the SQL isn't a shape we can handle, the parameter count
   * doesn't match the column list, the table has no usable schema, or any codec column name
   * doesn't appear in the table — in those cases the caller falls back to the driver's own
   * (usually empty) parameter metadata rather than report bogus errors.
   *
   * <p>Prefers a proper JDBC metadata source over re-parsing SQL or running a phantom SELECT:
   * {@code getColumns} works even when the target column names collide with SQL keywords, and
   * it surfaces auto-generated / default columns so we can tell legitimate partial inserts
   * from codec-with-missing-fields.
   */
  private static Optional<List<JdbcMeta.ParameterMeta>> insertParameterMetaFromTableSchema(
      String sql, int expectedParamCount, Connection conn) {
    var m = INSERT_COLUMN_LIST.matcher(sql);
    if (!m.find()) return Optional.empty();
    String table = stripQuotes(m.group(1));
    String[] rawCols = m.group(2).split(",");
    if (rawCols.length != expectedParamCount) return Optional.empty();

    java.util.Map<String, JdbcMeta.ColumnMeta> tableColumns;
    try {
      tableColumns = loadTableColumns(conn, table);
    } catch (SQLException ignored) {
      return Optional.empty();
    }
    if (tableColumns.isEmpty()) return Optional.empty();

    List<JdbcMeta.ParameterMeta> synthetic = new java.util.ArrayList<>(expectedParamCount);
    for (int i = 0; i < rawCols.length; i++) {
      String codecCol = stripQuotes(rawCols[i].trim()).toLowerCase();
      JdbcMeta.ColumnMeta tableCol = tableColumns.get(codecCol);
      if (tableCol == null) return Optional.empty();
      synthetic.add(
          new JdbcMeta.ParameterMeta(
              i + 1, tableCol.jdbcType(), tableCol.vendorTypeName(), tableCol.nullable()));
    }
    return Optional.of(synthetic);
  }

  private static java.util.Map<String, JdbcMeta.ColumnMeta> loadTableColumns(
      Connection conn, String tableName) throws SQLException {
    var result = new java.util.LinkedHashMap<String, JdbcMeta.ColumnMeta>();
    try (java.sql.ResultSet rs =
        conn.getMetaData().getColumns(null, null, tableName, "%")) {
      while (rs.next()) {
        String name = rs.getString("COLUMN_NAME");
        if (name == null) continue;
        int jdbcType = rs.getInt("DATA_TYPE");
        String typeName = rs.getString("TYPE_NAME");
        int nullable = rs.getInt("NULLABLE");
        int ordinal = rs.getInt("ORDINAL_POSITION");
        result.put(
            name.toLowerCase(),
            new JdbcMeta.ColumnMeta(ordinal, jdbcType, typeName, nullable, name, name));
      }
    }
    return result;
  }

  private static String stripQuotes(String ident) {
    if (ident.length() >= 2 && ident.charAt(0) == '"' && ident.charAt(ident.length() - 1) == '"') {
      return ident.substring(1, ident.length() - 1);
    }
    return ident;
  }

  @SuppressWarnings("rawtypes")
  private static ResultSetParser<?> extractResultSetParser(Template<?, ?> template) {
    return switch (template) {
      case Template.Query1 q -> q.parser();
      case Template.Query2 q -> q.parser();
      case Template.Query3 q -> q.parser();
      case Template.Query4 q -> q.parser();
      case Template.Query5 q -> q.parser();
      case Template.Query6 q -> q.parser();
      case Template.Query7 q -> q.parser();
      case Template.Query8 q -> q.parser();
      case Template.Query9 q -> q.parser();
      case Template.Query10 q -> q.parser();
      case Template.From f -> extractResultSetParser(f.inner());
      case RowTemplate.Query<?, ?> q -> q.resultParser();
      default -> null;
    };
  }

  private static List<DbType<?>> extractColumnTypes(ResultSetParser<?> parser) {
    if (parser instanceof ResultSetParser.Mapped<?, ?> m) {
      return extractColumnTypes(m.inner());
    } else if (parser instanceof ResultSetParser.First<?> f) {
      return f.rowCodec().columns();
    } else if (parser instanceof ResultSetParser.MaxOne<?> m) {
      return m.rowCodec().columns();
    } else if (parser instanceof ResultSetParser.ExactlyOne<?> e) {
      return e.rowCodec().columns();
    } else if (parser instanceof ResultSetParser.All<?> a) {
      return a.rowCodec().columns();
    } else if (parser instanceof ResultSetParser.Foreach<?> f) {
      return f.rowCodec().columns();
    } else {
      return List.of();
    }
  }
}
