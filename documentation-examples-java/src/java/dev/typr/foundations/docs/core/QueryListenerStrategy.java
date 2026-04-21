package dev.typr.foundations.docs.core;

import dev.typr.foundations.QueryListener;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class QueryListenerStrategy {
  Transactor tx = null; // placeholder
  QueryListener logger = QueryListener.NOOP; // placeholder
  // start
  Transactor txWithListener = tx.withListener(logger);
  // stop
}
