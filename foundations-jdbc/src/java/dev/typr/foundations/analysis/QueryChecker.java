package dev.typr.foundations.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.Procedure;
import dev.typr.foundations.ResultSetParser;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;
import java.sql.SQLException;

public interface QueryChecker {

  Transactor transactor();

  default <T> void check(Operation.Query<T> query) {
    assertAnalysisSucceeded(analyzeInternal(null, query));
  }

  default <T> void check(String name, Operation.Query<T> query) {
    assertAnalysisSucceeded(analyzeInternal(name, query));
  }

  default void check(Operation.Update update) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyze(update, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze update", e);
    }
    assertAnalysisSucceeded(analysis);
  }

  default void check(String name, Operation.Update update) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyze(name, update, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze update", e);
    }
    assertAnalysisSucceeded(analysis);
  }

  default <T> void check(Operation.UpdateReturning<T> op) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyze(op, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze update returning", e);
    }
    assertAnalysisSucceeded(analysis);
  }

  default <T> void check(String name, Operation.UpdateReturning<T> op) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyze(name, op, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze update returning", e);
    }
    assertAnalysisSucceeded(analysis);
  }

  default void check(Fragment fragment, ResultSetParser<?> parser) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyzeFragmentAndParser(fragment, parser, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze query", e);
    }
    assertAnalysisSucceeded(analysis);
  }

  default <T> void check(Fragment fragment, RowParser<T> parser) {
    check(fragment, parser.all());
  }

  default <T> QueryAnalysis analyze(Operation.Query<T> query) {
    return analyzeInternal(null, query);
  }

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

  default void checkRoutine(Procedure<?> procedure) {
    RoutineAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> RoutineAnalyzer.analyzeProcedure(procedure, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze routine", e);
    }
    if (!analysis.succeeded()) {
      throw new AssertionError("Routine analysis failed:\n" + analysis.report());
    }
  }

  private <T> QueryAnalysis analyzeInternal(String name, Operation.Query<T> query) {
    try {
      return transactor().execute(conn -> QueryAnalyzer.analyze(name, query, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze query", e);
    }
  }

  private static void assertAnalysisSucceeded(QueryAnalysis analysis) {
    if (!analysis.succeeded()) {
      throw new AssertionError("Query type check failed:\n" + analysis.report());
    }
  }
}
