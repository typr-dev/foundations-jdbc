package dev.typr.foundations.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.ResultSetParser;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Test mixin for checking query types against the database.
 *
 * <p>This interface provides a simple way to verify that your queries and row parsers
 * are type-correct at test time. It catches type mismatches that would otherwise
 * only appear at runtime.
 *
 * <p>Example usage in JUnit:
 * <pre>{@code
 * class UserQueriesTest implements QueryChecker {
 *     private static final Transactor TX = PostgresConfig.builder()
 *         .host("localhost").database("test").build().transactor();
 *
 *     @Override
 *     public Transactor transactor() { return TX; }
 *
 *     @Test
 *     void findByEmail_typesAlign() {
 *         check(
 *             Fragment.interpolate("SELECT id, name, email FROM users WHERE email = ")
 *                 .param(PgTypes.text, "test@example.com")
 *                 .done()
 *                 .query(UserRow.rowParser.all())
 *         );
 *     }
 * }
 * }</pre>
 *
 * <p>If there's a type mismatch, the test fails with a detailed report showing:
 * <ul>
 *   <li>Which parameters/columns have issues</li>
 *   <li>What types were declared vs what the database expects</li>
 *   <li>Nullability mismatches</li>
 * </ul>
 */
public interface QueryChecker {

  /**
   * Get the transactor used to connect to the database.
   * Override this to provide your test database connection.
   */
  Transactor transactor();

  /**
   * Check a query operation and fail the test if there are type errors.
   *
   * @param query the query to check
   * @throws AssertionError if there are type mismatches
   */
  default <T> void check(Operation.Query<T> query) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyze(query, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze query", e);
    }

    assertAnalysisSucceeded(analysis);
  }

  /**
   * Check an update operation and fail the test if there are type errors.
   *
   * @param update the update to check
   * @throws AssertionError if there are type mismatches
   */
  default void check(Operation.Update update) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyze(update, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze update", e);
    }

    assertAnalysisSucceeded(analysis);
  }

  /**
   * Check an update-returning operation and fail the test if there are type errors.
   *
   * @param op the update-returning to check
   * @throws AssertionError if there are type mismatches
   */
  default <T> void check(Operation.UpdateReturning<T> op) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyze(op, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze update returning", e);
    }

    assertAnalysisSucceeded(analysis);
  }

  /**
   * Check a fragment with a row parser and fail the test if there are type errors.
   *
   * @param fragment the SQL fragment
   * @param parser the result set parser
   * @throws AssertionError if there are type mismatches
   */
  default void check(Fragment fragment, ResultSetParser<?> parser) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyzeFragmentAndParser(fragment, parser, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze query", e);
    }

    assertAnalysisSucceeded(analysis);
  }

  /**
   * Check a fragment with a row parser and fail the test if there are type errors.
   * Convenience overload that wraps the RowParser in .all().
   *
   * @param fragment the SQL fragment
   * @param parser the row parser
   * @throws AssertionError if there are type mismatches
   */
  default <T> void check(Fragment fragment, RowParser<T> parser) {
    check(fragment, parser.all());
  }

  /**
   * Analyze a query and return the analysis result without failing.
   * Use this if you want to inspect the analysis programmatically.
   *
   * @param query the query to analyze
   * @return the analysis result
   */
  default <T> QueryAnalysis analyze(Operation.Query<T> query) {
    try {
      return transactor().execute(conn -> QueryAnalyzer.analyze(query, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze query", e);
    }
  }

  /**
   * Check multiple queries at once. Collects all errors and reports them together.
   *
   * @param queries the queries to check
   * @throws AssertionError if any query has type mismatches
   */
  default void checkAll(Operation.Query<?>... queries) {
    StringBuilder errors = new StringBuilder();
    int errorCount = 0;

    for (int i = 0; i < queries.length; i++) {
      Operation.Query<?> query = queries[i];
      QueryAnalysis analysis = analyze(query);
      if (!analysis.succeeded()) {
        errorCount++;
        errors.append("\n\n--- Query ").append(i + 1).append(" ---\n");
        errors.append(analysis.report());
      }
    }

    if (errorCount > 0) {
      throw new AssertionError(
          errorCount + " of " + queries.length + " queries failed type checking:" + errors
      );
    }
  }

  private static void assertAnalysisSucceeded(QueryAnalysis analysis) {
    if (!analysis.succeeded()) {
      throw new AssertionError("Query type check failed:\n" + analysis.report());
    }
  }
}
