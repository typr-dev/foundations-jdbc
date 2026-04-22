package dev.typr.foundations;

import java.util.ArrayList;
import java.util.List;

public interface QueryChecker {

  /** The transactor used for query checking. */
  Transactor transactor();

  /**
   * Create a QueryChecker backed by the given transactor. The transactor must be JDBC-backed —
   * query checking requires a raw JDBC connection for {@code DatabaseMetaData} access. Create a
   * separate transactor via {@code Transactor.create(config)} if your main transactor is not
   * JDBC-backed.
   */
  static QueryChecker create(Transactor transactor) {
    return () -> transactor;
  }

  /** Analyze an analyzable. Override to provide non-JDBC analysis (e.g. wire-protocol based). */
  default List<QueryAnalysis> doAnalyze(Analyzable analyzable) {
    return transactJdbc(
        mc -> {
          StatementAnalyzer analyzer =
              new dev.typr.foundations.internal.JdbcStatementAnalyzer(mc.unwrap());
          return AnalysisRunner.analyze(analyzable, analyzer);
        });
  }

  /** Analyze a fragment and parser pair. Override to provide non-JDBC analysis. */
  default QueryAnalysis doAnalyzeFragmentAndParser(Fragment fragment, ResultSetParser<?> parser) {
    return transactJdbc(
        mc -> {
          StatementAnalyzer analyzer =
              new dev.typr.foundations.internal.JdbcStatementAnalyzer(mc.unwrap());
          return AnalysisRunner.analyzeFragmentAndParser(
              java.util.Optional.empty(), fragment, parser, analyzer);
        });
  }

  /** Analyze a routine (procedure/function). Override to provide non-JDBC analysis. */
  default RoutineAnalysis doAnalyzeRoutine(Procedure<?> procedure) {
    return transactJdbc(mc -> RoutineAnalyzer.analyzeProcedure(procedure, mc.unwrap()));
  }

  default void check(Analyzable analyzable) {
    List<QueryAnalysis> analyses = doAnalyze(analyzable);
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
    QueryAnalysis analysis = doAnalyzeFragmentAndParser(fragment, parser);
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
      List<QueryAnalysis> analyses = doAnalyze(a);
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
    RoutineAnalysis analysis = doAnalyzeRoutine(procedure);
    if (!analysis.succeeded()) {
      throw new RoutineCheckFailedException(
          analysis, "Routine analysis failed:\n" + analysis.report());
    }
  }

  private <T> T transactJdbc(SqlFunction<Connection, T> fn) {
    try {
      return transactor().transact(fn);
    } catch (DatabaseException e) {
      throw e;
    } catch (RuntimeException e) {
      throw new IllegalStateException(
          "QueryChecker requires a JDBC-backed Transactor. "
              + "Create a separate Transactor via Transactor.create(config) for query checking.",
          e);
    }
  }

  /**
   * Thrown by {@link QueryChecker} when one or more queries fail type/nullability checking against
   * the live database. Carries the failed {@link QueryAnalysis}es so callers can inspect individual
   * failures programmatically — useful for IDE reporters, aggregated CI output, or selective retry.
   *
   * <p>Extends {@link RuntimeException} (not {@link AssertionError}) so it is caught by a plain
   * {@code catch (Exception e)} and does not imply assertions-enabled semantics.
   */
  final class QueryCheckFailedException extends RuntimeException {

    private final List<QueryAnalysis> failed;

    public QueryCheckFailedException(List<QueryAnalysis> failed, String message) {
      super(message);
      this.failed = List.copyOf(failed);
    }

    /** The failing analyses (non-empty by construction). */
    public List<QueryAnalysis> failed() {
      return failed;
    }
  }

  /**
   * Thrown by {@link QueryChecker#checkRoutine} when a stored procedure or function fails analysis
   * against the live database. Carries the failing {@link RoutineAnalysis} for programmatic
   * inspection.
   *
   * <p>Extends {@link RuntimeException} (not {@link AssertionError}) so it is caught by {@code
   * catch (Exception e)}.
   */
  final class RoutineCheckFailedException extends RuntimeException {

    private final RoutineAnalysis analysis;

    public RoutineCheckFailedException(RoutineAnalysis analysis, String message) {
      super(message);
      this.analysis = analysis;
    }

    public RoutineAnalysis analysis() {
      return analysis;
    }
  }
}
