package dev.typr.foundations;
import java.util.ArrayList;
import java.util.List;

public interface QueryChecker {

  Transactor transactor();

  static QueryChecker create(Transactor transactor) {
    return () -> transactor;
  }

  default void check(Analyzable analyzable) {
    List<QueryAnalysis> analyses =
        transactor().execute(conn -> QueryAnalyzer.analyze(analyzable, conn));
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
    QueryAnalysis analysis =
        transactor().execute(conn -> QueryAnalyzer.analyzeFragmentAndParser(fragment, parser, conn));
    if (!analysis.succeeded()) {
      throw new AssertionError("Query type check failed:\n" + analysis.report());
    }
  }

  default <T> void check(Fragment fragment, RowCodec<T> codec) {
    check(fragment, codec.all());
  }

  default CheckReport checkAll(List<? extends Analyzable> analyzables) {
    List<QueryAnalysis> all = new ArrayList<>();
    for (Analyzable a : analyzables) {
      List<QueryAnalysis> analyses =
          transactor().execute(conn -> QueryAnalyzer.analyze(a, conn));
      all.addAll(analyses);
    }
    return new CheckReport(List.copyOf(all));
  }

  default CheckReport checkAll(Analyzable... analyzables) {
    return checkAll(List.of(analyzables));
  }

  default void checkRoutine(Procedure<?> procedure) {
    RoutineAnalysis analysis =
        transactor().execute(conn -> RoutineAnalyzer.analyzeProcedure(procedure, conn));
    if (!analysis.succeeded()) {
      throw new AssertionError("Routine analysis failed:\n" + analysis.report());
    }
  }
}
