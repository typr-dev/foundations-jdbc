package dev.typr.foundations;

/**
 * Thrown by {@link QueryChecker#checkRoutine} when a stored procedure or function fails analysis
 * against the live database. Carries the failing {@link RoutineAnalysis} for programmatic
 * inspection.
 *
 * <p>Extends {@link RuntimeException} (not {@link AssertionError}) so it is caught by {@code catch
 * (Exception e)}.
 */
public final class RoutineCheckFailedException extends RuntimeException {

  private final RoutineAnalysis analysis;

  public RoutineCheckFailedException(RoutineAnalysis analysis, String message) {
    super(message);
    this.analysis = analysis;
  }

  public RoutineAnalysis analysis() {
    return analysis;
  }
}
