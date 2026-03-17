package dev.typr.foundations.docs.core;

import dev.typr.foundations.QueryListener;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class StrategyMerge {
  QueryListener logger = QueryListener.NOOP; // placeholder
  // start
  Transactor.Strategy base = Transactor.defaultStrategy();
  Transactor.Strategy withLogging = base.mergeListener(logger);
  // stop
}
