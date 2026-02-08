package dev.typr.foundations;

/**
 * Common interface for text encoding of values. Used for bulk loading via PostgreSQL's COPY
 * protocol. Implemented by PgText.
 */
public interface DbText<A> {
  void unsafeEncode(A a, StringBuilder sb);
}
