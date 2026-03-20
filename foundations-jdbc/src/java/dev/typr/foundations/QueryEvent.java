package dev.typr.foundations;

import java.time.Duration;
import org.jetbrains.annotations.Nullable;

public record QueryEvent(
    @Nullable String name,
    String sql,
    Fragment fragment,
    Duration elapsed,
    @Nullable Throwable error) {

  public String interpolatedSql() {
    return fragment.renderInterpolated();
  }
}
