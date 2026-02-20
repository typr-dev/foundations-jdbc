package dev.typr.foundations;

import org.jetbrains.annotations.Nullable;

public interface QueryListener {
  void beforeQuery(String sql, @Nullable String name);

  void afterQuery(QueryEvent event);

  void failedQuery(QueryEvent event);

  QueryListener NOOP =
      new QueryListener() {
        @Override
        public void beforeQuery(String sql, @Nullable String name) {}

        @Override
        public void afterQuery(QueryEvent event) {}

        @Override
        public void failedQuery(QueryEvent event) {}
      };

  default QueryListener compose(QueryListener other) {
    return compose(this, other);
  }

  static QueryListener compose(QueryListener first, QueryListener second) {
    if (first == NOOP) return second;
    if (second == NOOP) return first;
    return new QueryListener() {
      @Override
      public void beforeQuery(String sql, @Nullable String name) {
        first.beforeQuery(sql, name);
        second.beforeQuery(sql, name);
      }

      @Override
      public void afterQuery(QueryEvent event) {
        first.afterQuery(event);
        second.afterQuery(event);
      }

      @Override
      public void failedQuery(QueryEvent event) {
        first.failedQuery(event);
        second.failedQuery(event);
      }
    };
  }
}
