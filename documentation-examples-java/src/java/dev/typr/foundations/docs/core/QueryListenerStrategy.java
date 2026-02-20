package dev.typr.foundations.docs.core;

import dev.typr.foundations.QueryListener;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class QueryListenerStrategy {
    QueryListener logger = QueryListener.NOOP; // placeholder
    //start
    Transactor.Strategy strategy =
        Transactor.defaultStrategy()
            .replaceListener(logger);
    //stop
}
