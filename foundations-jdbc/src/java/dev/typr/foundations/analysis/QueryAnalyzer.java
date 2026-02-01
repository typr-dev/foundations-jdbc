package dev.typr.foundations.analysis;

import dev.typr.foundations.DbType;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.ResultSetParser;
import dev.typr.foundations.RowParser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Analyzes queries against the database to detect type mismatches.
 *
 * <p>This is the core of foundations-jdbc's query checking feature, inspired by doobie's
 * type checking. It works by:
 * <ol>
 *   <li>Extracting parameter types from the {@link Fragment}</li>
 *   <li>Extracting column types from the {@link RowParser}</li>
 *   <li>Preparing the statement on a real connection</li>
 *   <li>Getting JDBC metadata about what the database expects/returns</li>
 *   <li>Comparing our types against JDBC metadata</li>
 * </ol>
 *
 * <p>Example usage:
 * <pre>{@code
 * var query = Fragment.interpolate("SELECT id, name FROM users WHERE id = ")
 *     .param(PgTypes.int4, 1)
 *     .done()
 *     .query(userParser.all());
 *
 * QueryAnalysis analysis = QueryAnalyzer.analyze(query, connection);
 * if (!analysis.succeeded()) {
 *     fail(analysis.report());
 * }
 * }</pre>
 */
public final class QueryAnalyzer {

  private QueryAnalyzer() {}

  /**
   * Analyze a query operation against the database.
   *
   * @param query the query operation to analyze
   * @param conn a connection to use for metadata extraction
   * @return analysis results
   * @throws SQLException if preparing the statement fails
   */
  public static <T> QueryAnalysis analyze(Operation.Query<T> query, Connection conn)
      throws SQLException {
    return analyzeFragmentAndParser(query.query(), query.parser(), conn);
  }

  /**
   * Analyze an update-returning operation against the database.
   *
   * @param op the update-returning operation to analyze
   * @param conn a connection to use for metadata extraction
   * @return analysis results
   * @throws SQLException if preparing the statement fails
   */
  public static <T> QueryAnalysis analyze(Operation.UpdateReturning<T> op, Connection conn)
      throws SQLException {
    return analyzeFragmentAndParser(op.query(), op.parser(), conn);
  }

  /**
   * Analyze a fragment with a row parser against the database.
   *
   * @param fragment the SQL fragment with parameters
   * @param parser the result set parser
   * @param conn a connection to use for metadata extraction
   * @return analysis results
   * @throws SQLException if preparing the statement fails
   */
  public static QueryAnalysis analyzeFragmentAndParser(
      Fragment fragment,
      ResultSetParser<?> parser,
      Connection conn) throws SQLException {

    String sql = fragment.render();
    List<DbType<?>> paramTypes = fragment.parameterTypes();
    List<DbType<?>> columnTypes = extractColumnTypes(parser);

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      // Extract JDBC metadata
      List<JdbcMeta.ParameterMeta> paramMeta = JdbcMeta.extractParameters(ps);
      List<JdbcMeta.ColumnMeta> colMeta = JdbcMeta.extractColumns(ps);

      // Determine if parameter metadata is available
      // Some databases (MySQL/MariaDB) don't provide reliable parameter metadata
      boolean paramMetaAvailable = !paramMeta.isEmpty() || paramTypes.isEmpty();

      // Align our types with JDBC metadata
      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> paramAlignment =
          Alignment.align(paramTypes, paramMeta);
      List<Alignment<DbType<?>, JdbcMeta.ColumnMeta>> colAlignment =
          Alignment.align(columnTypes, colMeta);

      return new QueryAnalysis(sql, paramAlignment, colAlignment, paramMetaAvailable);
    }
  }

  /**
   * Analyze just the parameters of an update operation (no result columns).
   *
   * @param update the update operation to analyze
   * @param conn a connection to use for metadata extraction
   * @return analysis results
   * @throws SQLException if preparing the statement fails
   */
  public static QueryAnalysis analyze(Operation.Update update, Connection conn)
      throws SQLException {
    Fragment fragment = update.query();
    String sql = fragment.render();
    List<DbType<?>> paramTypes = fragment.parameterTypes();

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      List<JdbcMeta.ParameterMeta> paramMeta = JdbcMeta.extractParameters(ps);
      boolean paramMetaAvailable = !paramMeta.isEmpty() || paramTypes.isEmpty();

      List<Alignment<DbType<?>, JdbcMeta.ParameterMeta>> paramAlignment =
          Alignment.align(paramTypes, paramMeta);

      // No columns for UPDATE
      return new QueryAnalysis(sql, paramAlignment, List.of(), paramMetaAvailable);
    }
  }

  /**
   * Extract column types from a ResultSetParser.
   * Works with the standard ResultSetParser wrappers (First, MaxOne, ExactlyOne, All, Foreach).
   */
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
