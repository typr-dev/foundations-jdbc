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
    List<QueryAnalysis> failed = new ArrayList<>();
    for (QueryAnalysis analysis : analyses) {
      if (!analysis.succeeded()) {
        failed.add(analysis);
        errors.append("\n\n").append(analysis.report());
      }
    }
    if (!failed.isEmpty()) {
      throw new QueryCheckFailedException(failed, "Query type check failed:" + errors);
    }
  }

  default void check(Fragment fragment, ResultSetParser<?> parser) {
    QueryAnalysis analysis =
        transactor()
            .execute(conn -> QueryAnalyzer.analyzeFragmentAndParser(fragment, parser, conn));
    if (!analysis.succeeded()) {
      throw new QueryCheckFailedException(
          List.of(analysis), "Query type check failed:\n" + analysis.report());
    }
  }

  default <T> void check(Fragment fragment, RowCodec<T> codec) {
    check(fragment, codec.all());
  }

  default CheckReport analyzeAll(List<? extends Analyzable> analyzables) {
    List<QueryAnalysis> all = new ArrayList<>();
    for (Analyzable a : analyzables) {
      List<QueryAnalysis> analyses = transactor().execute(conn -> QueryAnalyzer.analyze(a, conn));
      all.addAll(analyses);
    }
    return new CheckReport(List.copyOf(all));
  }

  default CheckReport analyzeAll(Analyzable... analyzables) {
    return analyzeAll(List.of(analyzables));
  }

  default CheckReport checkAll(List<? extends Analyzable> analyzables) {
    CheckReport report = analyzeAll(analyzables);
    report.assertAllSucceeded();
    return report;
  }

  default CheckReport checkAll(Analyzable... analyzables) {
    return checkAll(List.of(analyzables));
  }

  default void checkRoutine(RoutineDef def) {
    checkRoutine(def.procedure());
  }

  default void checkRoutine(Procedure<?> procedure) {
    RoutineAnalysis analysis =
        transactor().execute(conn -> RoutineAnalyzer.analyzeProcedure(procedure, conn));
    if (!analysis.succeeded()) {
      throw new RoutineCheckFailedException(
          analysis, "Routine analysis failed:\n" + analysis.report());
    }
  }
}
