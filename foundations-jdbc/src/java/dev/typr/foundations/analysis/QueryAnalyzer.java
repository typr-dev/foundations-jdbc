package dev.typr.foundations.analysis;

import dev.typr.foundations.DbType;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.ResultSetParser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public final class QueryAnalyzer {

  private QueryAnalyzer() {}

  public static <T> QueryAnalysis analyze(Operation.Query<T> query, Connection conn)
      throws SQLException {
    return analyzeFragmentAndParser(null, query.query(), query.parser(), conn);
  }

  public static <T> QueryAnalysis analyze(String name, Operation.Query<T> query, Connection conn)
      throws SQLException {
    return analyzeFragmentAndParser(name, query.query(), query.parser(), conn);
  }

  public static <T> QueryAnalysis analyze(Operation.UpdateReturning<T> op, Connection conn)
      throws SQLException {
    return analyzeFragmentAndParser(null, op.query(), op.parser(), conn);
  }

  public static <T> QueryAnalysis analyze(String name, Operation.UpdateReturning<T> op, Connection conn)
      throws SQLException {
    return analyzeFragmentAndParser(name, op.query(), op.parser(), conn);
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

  public static QueryAnalysis analyze(Operation.Update update, Connection conn)
      throws SQLException {
    return analyze((String) null, update, conn);
  }

  public static QueryAnalysis analyze(String name, Operation.Update update, Connection conn)
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

  private static List<DbType<?>> extractColumnTypes(ResultSetParser<?> parser) {
    if (parser instanceof ResultSetParser.First<?> f) {
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
