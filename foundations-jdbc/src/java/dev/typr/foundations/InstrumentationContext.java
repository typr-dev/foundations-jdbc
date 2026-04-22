package dev.typr.foundations;

import java.time.Duration;
import java.util.Optional;

/**
 * Immutable context for query instrumentation: name, timeout, listener. Threaded through the
 * operation tree by {@link OperationRunner}.
 */
public record InstrumentationContext(
    Optional<String> name, Optional<Duration> timeout, QueryListener listener) {

  public static final InstrumentationContext EMPTY =
      new InstrumentationContext(Optional.empty(), Optional.empty(), QueryListener.NOOP);

  public InstrumentationContext withName(String name) {
    return new InstrumentationContext(Optional.of(name), this.timeout, this.listener);
  }

  public InstrumentationContext withTimeout(Duration timeout) {
    return new InstrumentationContext(this.name, Optional.of(timeout), this.listener);
  }

  public InstrumentationContext withListener(QueryListener listener) {
    QueryListener composed =
        this.listener == QueryListener.NOOP
            ? listener
            : QueryListener.compose(this.listener, listener);
    return new InstrumentationContext(this.name, this.timeout, composed);
  }
}
