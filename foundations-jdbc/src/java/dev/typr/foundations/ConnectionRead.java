package dev.typr.foundations;

import java.util.List;
import java.util.Optional;

/**
 * A connection that can only execute read operations. Cannot update, cannot access the underlying
 * JDBC connection. This restriction enables optimizations: the execution engine can skip
 * transactions, fan out queries across multiple connections, and route to read replicas.
 *
 * <p>{@link Connection} extends this interface, so any method accepting {@code ConnectionRead} also
 * accepts a {@code Connection}.
 *
 * @see Connection
 * @see Transactor#transactRead
 */
public interface ConnectionRead {

  /** Execute a read-only operation (query, combine, map, etc.). */
  <T> T execute(OperationRead<T> op);

  /** Execute a query and return all rows. */
  <T> List<T> query(Fragment sql, RowCodec<T> codec);

  /** Execute a query and return the first row, or empty if no results. */
  <T> Optional<T> queryFirst(Fragment sql, RowCodec<T> codec);
}
