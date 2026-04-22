package dev.typr.foundations;

import java.time.Duration;
import java.util.Optional;

public record QueryEvent(
    Optional<String> name,
    String sql,
    Fragment fragment,
    Duration elapsed,
    Optional<Throwable> error) {

  public String interpolatedSql() {
    return fragment.renderInterpolated();
  }
}
