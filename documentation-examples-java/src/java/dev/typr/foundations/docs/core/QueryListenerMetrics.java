package dev.typr.foundations.docs.core;

import dev.typr.foundations.QueryEvent;
import dev.typr.foundations.QueryListener;

@SuppressWarnings("unused")
public class QueryListenerMetrics {
  // start
  QueryListener metricsListener(/* MeterRegistry registry */ ) {
    return new QueryListener() {
      @Override
      public void beforeQuery(String sql, java.util.Optional<String> name) {}

      @Override
      public void afterQuery(QueryEvent event) {
        // registry.timer("db.query",
        //     "name", event.name(),
        //     "outcome", "success"
        // ).record(event.elapsed());
      }

      @Override
      public void failedQuery(QueryEvent event) {
        // registry.timer("db.query",
        //     "name", event.name(),
        //     "outcome", "failure"
        // ).record(event.elapsed());
      }
    };
  }
  // stop
}
