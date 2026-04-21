package dev.typr.foundations.docs.core;

import dev.typr.foundations.QueryListener;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class TransactorListener {
  Transactor tx = null; // placeholder
  QueryListener logger = QueryListener.NOOP; // placeholder
  QueryListener metrics = QueryListener.NOOP; // placeholder
  // start
  Transactor withLogging = tx.withListener(logger);
  Transactor withBoth = tx.withListener(logger).mergeListener(metrics);
  // stop
}
