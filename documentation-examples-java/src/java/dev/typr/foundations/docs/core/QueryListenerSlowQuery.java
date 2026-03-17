package dev.typr.foundations.docs.core;

import dev.typr.foundations.QueryEvent;
import dev.typr.foundations.QueryListener;
import java.time.Duration;

@SuppressWarnings("unused")
public class QueryListenerSlowQuery {
  // start
  QueryListener slowQueryDetector(Duration threshold) {
    return new QueryListener() {
      @Override
      public void beforeQuery(String sql, String name) {}

      @Override
      public void afterQuery(QueryEvent event) {
        if (event.elapsed().compareTo(threshold) > 0) {
          System.err.println(
              "SLOW QUERY [" + event.elapsed().toMillis() + "ms]: " + event.interpolatedSql());
        }
      }

      @Override
      public void failedQuery(QueryEvent event) {}
    };
  }
  // stop
}
