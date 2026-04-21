package dev.typr.foundations;

/** Internal interface for transactors that support rollback-only transactions. */
public interface RollbackCapable {
  <T> T transactRollback(SqlFunction<Connection, T> fn);
}
