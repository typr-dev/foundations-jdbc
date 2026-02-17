package dev.typr.foundations.docs.core;

import dev.typr.foundations.QueryListener;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class StrategyOverride {
    Transactor tx = null; // placeholder
    QueryListener logger = QueryListener.NOOP; // placeholder
    //start
    Transactor txWithLogging = tx.withStrategy(
        Transactor.defaultStrategy().withListener(logger));
    //stop
}
