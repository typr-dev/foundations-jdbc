package dev.typr.foundations;
import java.sql.SQLException;
import java.util.List;

public interface QueryChecker {

  Transactor transactor();

  default void check(Operation<?> op) {
    check(null, op);
  }

  default void check(String name, Operation<?> op) {
    List<QueryAnalysis> analyses;
    try {
      analyses = transactor().execute(conn -> QueryAnalyzer.analyze(name, op, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze operation", e);
    }
    StringBuilder errors = new StringBuilder();
    int errorCount = 0;
    for (QueryAnalysis analysis : analyses) {
      if (!analysis.succeeded()) {
        errorCount++;
        errors.append("\n\n").append(analysis.report());
      }
    }
    if (errorCount > 0) {
      throw new AssertionError("Query type check failed:" + errors);
    }
  }

  default void check(Fragment fragment, ResultSetParser<?> parser) {
    QueryAnalysis analysis;
    try {
      analysis = transactor().execute(conn -> QueryAnalyzer.analyzeFragmentAndParser(fragment, parser, conn));
    } catch (SQLException e) {
      throw new RuntimeException("Failed to analyze query", e);
    }
    if (!analysis.succeeded()) {
      throw new AssertionError("Query type check failed:\n" + analysis.report());
    }
  }

  default <T> void check(Fragment fragment, RowParser<T> parser) {
    check(fragment, parser.all());
  }

  default void checkAll(Operation<?>... operations) {
    StringBuilder errors = new StringBuilder();
    int errorCount = 0;

    for (int i = 0; i < operations.length; i++) {
      Operation<?> op = operations[i];
      List<QueryAnalysis> analyses;
      try {
        analyses = transactor().execute(conn -> QueryAnalyzer.analyze(op, conn));
      } catch (SQLException e) {
        throw new RuntimeException("Failed to analyze operation " + (i + 1), e);
      }
      for (QueryAnalysis analysis : analyses) {
        if (!analysis.succeeded()) {
          errorCount++;
          errors.append("\n\n--- Operation ").append(i + 1).append(" ---\n");
          errors.append(analysis.report());
        }
      }
    }

    if (errorCount > 0) {
      throw new AssertionError(
          errorCount + " queries failed type checking:" + errors
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
}
