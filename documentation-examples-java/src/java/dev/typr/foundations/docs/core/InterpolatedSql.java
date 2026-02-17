package dev.typr.foundations.docs.core;

import dev.typr.foundations.QueryEvent;
import dev.typr.foundations.QueryListener;

@SuppressWarnings("unused")
public class InterpolatedSql {
    //start
    QueryListener debugListener = new QueryListener() {
        @Override
        public void beforeQuery(String sql, String name) {}

        @Override
        public void afterQuery(QueryEvent event) {
            // SQL with values inlined for debugging:
            // SELECT * FROM users WHERE id = 42
            System.out.println(event.interpolatedSql());
        }

        @Override
        public void failedQuery(QueryEvent event) {
            System.err.println("Failed: "
                + event.interpolatedSql());
        }
    };
    //stop
}
