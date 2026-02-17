package dev.typr.foundations;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A thin wrapper around a source of database connections and a strategy for managing transactions.
 *
 * <p>Inspired by doobie's Transactor, this class provides a clean way to manage database
 * connections with configurable lifecycle hooks for transaction management.
 *
 * <p>Typically obtained via {@link dev.typr.foundations.connect.ConnectionSource#transactor}:
 *
 * <pre>{@code
 * var ds = SimpleDataSource.create(config, settings);
 * var tx = ds.transactor(Transactor.testStrategy());
 * tx.execute(conn -> repo.selectAll(conn));
 * }</pre>
 */
public record Transactor(SqlSupplier<Connection> connect, Strategy strategy) {

  /**
   * Execute an operation with full strategy lifecycle.
   *
   * @param <T> the result type
   * @param operation the operation to execute with a connection
   * @return the operation result
   * @throws SQLException if a database error occurs
   */
  public <T> T execute(SqlFunction<Connection, T> operation) throws SQLException {
    Connection raw = connect.get();
    Connection conn =
        strategy.listener() != QueryListener.NOOP
            ? new InstrumentedConnection(raw, strategy.listener(), null, null)
            : raw;
    try {
      strategy.before().apply(conn);
      T result = operation.apply(conn);
      strategy.after().apply(conn);
      return result;
    } catch (SQLException | RuntimeException e) {
      strategy.oops().apply(conn, e);
      throw e;
    } finally {
      strategy.always().apply(conn);
    }
  }

  /**
   * Execute an Operation with full strategy lifecycle.
   *
   * @param <T> the result type
   * @param op the Operation to execute
   * @return the operation result
   * @throws SQLException if a database error occurs
   */
  public <T> T execute(Operation<T> op) throws SQLException {
    return execute(op::runChecked);
  }

  /**
   * Execute a void operation with full strategy lifecycle.
   *
   * @param operation the operation to execute with a connection
   * @throws SQLException if a database error occurs
   */
  public void executeVoid(SqlConsumer<Connection> operation) throws SQLException {
    execute(
        conn -> {
          operation.apply(conn);
          return null;
        });
  }

  /**
   * Returns a new Transactor with the given strategy merged on top of this one.
   *
   * @param override the strategy to merge
   * @return a new Transactor with the merged strategy
   */
  public Transactor withStrategy(Strategy override) {
    return new Transactor(connect, strategy.merge(override));
  }

  /**
   * Execute an operation with a one-shot strategy override merged on top of the base strategy.
   *
   * @param <T> the result type
   * @param override the strategy to merge for this execution
   * @param operation the operation to execute
   * @return the operation result
   * @throws SQLException if a database error occurs
   */
  public <T> T execute(Strategy override, SqlFunction<Connection, T> operation)
      throws SQLException {
    return withStrategy(override).execute(operation);
  }

  /**
   * Default strategy: manual transactions with commit on success, close always.
   *
   * <p>Behavior:
   *
   * <ul>
   *   <li>before: setAutoCommit(false)
   *   <li>after: commit()
   *   <li>oops: no-op (caller handles exceptions)
   *   <li>always: close()
   * </ul>
   *
   * @return a strategy for manual transaction management
   */
  public static Strategy defaultStrategy() {
    return new Strategy(
        conn -> conn.setAutoCommit(false),
        Connection::commit,
        (conn, t) -> {},
        Connection::close,
        QueryListener.NOOP);
  }

  /**
   * Strategy for auto-commit mode (no manual transactions).
   *
   * <p>Behavior:
   *
   * <ul>
   *   <li>before: no-op
   *   <li>after: no-op
   *   <li>oops: no-op
   *   <li>always: close()
   * </ul>
   *
   * @return a strategy for auto-commit mode
   */
  public static Strategy autoCommitStrategy() {
    return new Strategy(
        conn -> {}, conn -> {}, (conn, t) -> {}, Connection::close, QueryListener.NOOP);
  }

  /**
   * Strategy with rollback on error.
   *
   * <p>Behavior:
   *
   * <ul>
   *   <li>before: setAutoCommit(false)
   *   <li>after: commit()
   *   <li>oops: rollback() (silently ignores rollback failures)
   *   <li>always: close()
   * </ul>
   *
   * @return a strategy that rolls back on error
   */
  public static Strategy rollbackOnErrorStrategy() {
    return new Strategy(
        conn -> conn.setAutoCommit(false),
        Connection::commit,
        (conn, t) -> {
          try {
            if (!conn.getAutoCommit() && !conn.isClosed()) {
              conn.rollback();
            }
          } catch (SQLException ignored) {
          }
        },
        Connection::close,
        QueryListener.NOOP);
  }

  /**
   * Strategy for testing: always rollback instead of commit.
   *
   * <p>Behavior:
   *
   * <ul>
   *   <li>before: setAutoCommit(false)
   *   <li>after: rollback() (instead of commit, to keep test data isolated)
   *   <li>oops: no-op (caller handles exceptions)
   *   <li>always: close()
   * </ul>
   *
   * @return a strategy for testing that always rolls back
   */
  public static Strategy testStrategy() {
    return new Strategy(
        conn -> conn.setAutoCommit(false),
        Connection::rollback,
        (conn, t) -> {},
        Connection::close,
        QueryListener.NOOP);
  }

  /**
   * Data type representing the common setup, error-handling, and cleanup strategy associated with
   * an SQL transaction. A `Transactor` uses a `Strategy` to wrap programs prior to execution.
   *
   * @param before a program to prepare the connection for use
   * @param after a program to run on success
   * @param oops a program to run on failure (catch), receives the connection and the throwable
   * @param always a program to run in all cases (finally)
   * @param listener a query listener for observability (use QueryListener.NOOP for none)
   */
  public record Strategy(
      SqlConsumer<Connection> before,
      SqlConsumer<Connection> after,
      SqlBiConsumer<Connection, Throwable> oops,
      SqlConsumer<Connection> always,
      QueryListener listener) {

    public Strategy withListener(QueryListener listener) {
      return new Strategy(before, after, oops, always, listener);
    }

    public Strategy merge(Strategy other) {
      return new Strategy(
          conn -> {
            before.apply(conn);
            other.before.apply(conn);
          },
          conn -> {
            after.apply(conn);
            other.after.apply(conn);
          },
          (conn, t) -> {
            oops.apply(conn, t);
            other.oops.apply(conn, t);
          },
          conn -> {
            always.apply(conn);
            other.always.apply(conn);
          },
          QueryListener.compose(listener, other.listener));
    }
  }
}
