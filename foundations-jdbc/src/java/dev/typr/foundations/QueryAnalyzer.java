package dev.typr.foundations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class QueryAnalyzer {

  private QueryAnalyzer() {}

  public static List<QueryAnalysis> analyze(Operation<?> op, Connection conn)
      throws SQLException {
    return analyze(null, op, conn);
  }

  public static List<QueryAnalysis> analyze(SqlTemplate<?, ?> template, Connection conn)
      throws SQLException {
    return analyze(null, template, conn);
  }

  public static List<QueryAnalysis> analyze(String name, SqlTemplate<?, ?> template, Connection conn)
      throws SQLException {
    ResultSetParser<?> parser = extractResultSetParser(template);
    if (parser != null) {
      return List.of(analyzeFragmentAndParser(name, template.fragment(), parser, conn));
    } else {
      return List.of(analyzeUpdate(name, new Operation.Update(template.fragment()), conn));
    }
  }

  public static List<QueryAnalysis> analyze(RowSqlTemplate<?, ?> template, Connection conn)
      throws SQLException {
    return analyze(null, template, conn);
  }

  public static List<QueryAnalysis> analyze(String name, RowSqlTemplate<?, ?> template, Connection conn)
      throws SQLException {
    return switch (template) {
      case RowSqlTemplate.Query<?, ?> q ->
          List.of(analyzeFragmentAndParser(name, q.fragment(), q.resultParser(), conn));
      case RowSqlTemplate.Update<?> u ->
          List.of(analyzeUpdate(name, new Operation.Update(u.fragment()), conn));
    };
  }

  public static List<QueryAnalysis> analyze(String name, Operation<?> op, Connection conn)
      throws SQLException {
    return switch (op) {
      case Operation.Query<?> q -> List.of(analyzeFragmentAndParser(name, q.query(), q.parser(), conn));
      case Operation.UpdateReturning<?> ur -> List.of(analyzeFragmentAndParser(name, ur.query(), ur.parser(), conn));
      case Operation.Update u -> List.of(analyzeUpdate(name, u, conn));
      case Operation.Mapped<?, ?> m -> analyze(name, m.source(), conn);
      case Operation.With<?, ?> w -> {
        var r = new ArrayList<>(analyze(w.first(), conn));
        r.addAll(analyze(w.second(), conn));
        yield r;
      }
      case Operation.IfEmpty<?> ie -> {
        var r = new ArrayList<>(analyze(ie.check(), conn));
        r.addAll(analyze(ie.fallback(), conn));
        yield r;
      }
      case Operation.Then<?, ?, ?> t -> {
        var r = new ArrayList<>(analyze(t.source(), conn));
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
          List<DbType<?>> columnTypes = templateParser != null ? extractColumnTypes(templateParser) : List.of();
          List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> colAlignment =
              Alignment.align(columnTypes, colMeta);
          r.add(new QueryAnalysis(sql, null, paramAlignment, colAlignment, paramMetaAvailable));
        }
        yield r;
      }
      case Operation.Pure<?> ignored -> List.of();
      default -> List.of();
    };
  }

  public static QueryAnalysis analyzeFragmentAndParser(
      Fragment fragment,
      ResultSetParser<?> parser,
      Connection conn) throws SQLException {
    return analyzeFragmentAndParser(null, fragment, parser, conn);
  }

  public static QueryAnalysis analyzeFragmentAndParser(
      String name,
      Fragment fragment,
      ResultSetParser<?> parser,
      Connection conn) throws SQLException {

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

  private static QueryAnalysis analyzeUpdate(String name, Operation.Update update, Connection conn)
      throws SQLException {
    Fragment fragment = update.query();
    String sql = fragment.render();
    List<DbType<?>> paramTypes = fragment.parameterTypes();

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      List<JdbcMeta.ParameterMeta> paramMeta = JdbcMeta.extractParameters(ps);
      boolean paramMetaAvailable = !paramMeta.isEmpty() || paramTypes.isEmpty();

      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> paramAlignment =
          Alignment.align(paramTypes, paramMeta);

      return new QueryAnalysis(sql, name, paramAlignment, List.of(), paramMetaAvailable);
    }
  }

  @SuppressWarnings("rawtypes")
  private static ResultSetParser<?> extractResultSetParser(SqlTemplate<?, ?> template) {
    return switch (template) {
      case SqlTemplate.Query1 q -> q.parser();
      case SqlTemplate.Query2 q -> q.parser();
      case SqlTemplate.Query3 q -> q.parser();
      case SqlTemplate.Query4 q -> q.parser();
      case SqlTemplate.Query5 q -> q.parser();
      case SqlTemplate.Query6 q -> q.parser();
      case SqlTemplate.Query7 q -> q.parser();
      case SqlTemplate.Query8 q -> q.parser();
      case SqlTemplate.Query9 q -> q.parser();
      case SqlTemplate.Query10 q -> q.parser();
      default -> null;
    };
  }

  private static List<DbType<?>> extractColumnTypes(ResultSetParser<?> parser) {
    if (parser instanceof ResultSetParser.Mapped<?, ?> m) {
      return extractColumnTypes(m.inner());
    } else if (parser instanceof ResultSetParser.First<?> f) {
      return f.rowParser().columns();
    } else if (parser instanceof ResultSetParser.MaxOne<?> m) {
      return m.rowParser().columns();
    } else if (parser instanceof ResultSetParser.ExactlyOne<?> e) {
      return e.rowParser().columns();
    } else if (parser instanceof ResultSetParser.All<?> a) {
      return a.rowParser().columns();
    } else if (parser instanceof ResultSetParser.Foreach<?> f) {
      return f.rowParser().columns();
    } else {
      return List.of();
    }
  }
}
