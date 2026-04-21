package dev.typr.foundations;

/**
 * A connection that can execute any operation — reads and writes. Extends {@link ConnectionRead},
 * so a {@code Connection} can be used anywhere a {@code ConnectionRead} is expected.
 *
 * <p>Provides {@link #unwrap()} to access the underlying JDBC {@link java.sql.Connection} for
 * database-specific operations (LISTEN/NOTIFY, advisory locks, temp tables, etc.).
 *
 * @see ConnectionRead
 * @see Transactor#transact
 */
public interface Connection extends ConnectionRead {

  /** Execute any read-write operation (update, insert, DDL, composed read-write operations). */
  <T> T execute(Operation<T> op);

  /** Execute an update/insert/delete and return the number of affected rows. */
  int update(Fragment sql);

  /** Access the underlying JDBC connection for database-specific operations. */
  java.sql.Connection unwrap();
}
