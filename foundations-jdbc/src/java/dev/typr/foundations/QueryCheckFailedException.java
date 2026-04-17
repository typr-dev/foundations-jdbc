package dev.typr.foundations;

import java.util.List;

/**
 * Thrown by {@link QueryChecker} when one or more queries fail type/nullability checking against
 * the live database. Carries the failed {@link QueryAnalysis}es so callers can inspect
 * individual failures programmatically — useful for IDE reporters, aggregated CI output, or
 * selective retry.
 *
 * <p>Extends {@link RuntimeException} (not {@link AssertionError}) so it is caught by a plain
 * {@code catch (Exception e)} and does not imply assertions-enabled semantics.
 */
public final class QueryCheckFailedException extends RuntimeException {

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
