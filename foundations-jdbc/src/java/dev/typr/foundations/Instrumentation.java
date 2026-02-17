package dev.typr.foundations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import org.jetbrains.annotations.Nullable;

final class Instrumentation {
  private Instrumentation() {}

  static String applyName(String sql, Connection conn) {
    if (conn instanceof InstrumentedConnection ic) {
      String name = ic.nextQueryName();
      if (name != null) {
        return "/* " + name + " */ " + sql;
      }
    }
    return sql;
  }

  static void applyTimeout(PreparedStatement stmt, Connection conn) throws SQLException {
    if (conn instanceof InstrumentedConnection ic) {
      Duration timeout = ic.queryTimeout();
      if (timeout != null) {
        int seconds = (int) Math.max(1, timeout.toSeconds());
        stmt.setQueryTimeout(seconds);
      }
    }
  }

  interface InstrumentedAction<T> {
    T run() throws SQLException;
  }

  static <T> T instrumented(
      Connection conn, Fragment fragment, String sql, InstrumentedAction<T> action)
      throws SQLException {
    if (!(conn instanceof InstrumentedConnection ic)) {
      return action.run();
    }
    QueryListener listener = ic.queryListener();
    if (listener == QueryListener.NOOP) {
      return action.run();
    }
    @Nullable String name = ic.currentQueryName();
    listener.beforeQuery(sql, name);
    long start = System.nanoTime();
    try {
      T result = action.run();
      long elapsed = System.nanoTime() - start;
      listener.afterQuery(
          new QueryEvent(name, sql, fragment, Duration.ofNanos(elapsed), null));
      return result;
    } catch (SQLException | RuntimeException e) {
      long elapsed = System.nanoTime() - start;
      listener.failedQuery(
          new QueryEvent(name, sql, fragment, Duration.ofNanos(elapsed), e));
      throw e;
    }
  }
}
