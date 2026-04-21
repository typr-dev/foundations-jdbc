package dev.typr.foundations;

import java.util.Optional;

public interface QueryListener {
  void beforeQuery(String sql, Optional<String> name);

  void afterQuery(QueryEvent event);

  void failedQuery(QueryEvent event);

  default void afterTransaction(TransactionEvent event) {}

  default void failedTransaction(TransactionEvent event) {}

  QueryListener NOOP =
      new QueryListener() {
        @Override
        public void beforeQuery(String sql, Optional<String> name) {}

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
      public void beforeQuery(String sql, Optional<String> name) {
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

      @Override
      public void afterTransaction(TransactionEvent event) {
        first.afterTransaction(event);
        second.afterTransaction(event);
      }

      @Override
      public void failedTransaction(TransactionEvent event) {
        first.failedTransaction(event);
        second.failedTransaction(event);
      }
    };
  }
}
