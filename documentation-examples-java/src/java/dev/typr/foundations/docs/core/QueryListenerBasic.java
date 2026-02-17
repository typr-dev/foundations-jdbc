package dev.typr.foundations.docs.core;

import dev.typr.foundations.QueryEvent;
import dev.typr.foundations.QueryListener;

@SuppressWarnings("unused")
public class QueryListenerBasic {
    //start
    QueryListener logger = new QueryListener() {
        @Override
        public void beforeQuery(String sql, String name) {
            System.out.println("Executing: " + sql);
        }

        @Override
        public void afterQuery(QueryEvent event) {
            System.out.println(event.name()
                + " completed in " + event.elapsed().toMillis() + "ms");
        }

        @Override
        public void failedQuery(QueryEvent event) {
            System.err.println(event.name()
                + " failed after " + event.elapsed().toMillis() + "ms: "
                + event.error().getMessage());
        }
    };
    //stop
}
