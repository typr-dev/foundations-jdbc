package dev.typr.foundations;

import dev.typr.foundations.connect.ConnectionSettings;
import dev.typr.foundations.connect.DatabaseConfig;
import dev.typr.foundations.connect.SimpleDataSource;
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
   * @throws DatabaseException if a database error occurs
   */
  public <T> T execute(SqlFunction<Connection, T> operation) {
    try {
      Connection raw = connect.get();
      Connection conn =
          strategy.listener() != QueryListener.NOOP
              ? new InstrumentedConnection(raw, strategy.listener(), null, null)
              : raw;
      try {
        strategy.onBegin().apply(conn);
        T result = operation.apply(conn);
        strategy.onSuccess().apply(conn);
        return result;
      } catch (SQLException | RuntimeException e) {
        strategy.onFailure().apply(conn, e);
        throw e;
      } finally {
        strategy.onComplete().apply(conn);
      }
    } catch (SQLException e) {
      throw new DatabaseException(e);
    }
  }

  /**
   * Execute an Operation with full strategy lifecycle.
   *
   * @param <T> the result type
   * @param op the Operation to execute
   * @return the operation result
   * @throws DatabaseException if a database error occurs
   */
  public <T> T execute(Operation<T> op) {
    return execute(conn -> op.run(conn));
  }

  /**
   * Execute a void operation with full strategy lifecycle.
   *
   * @param operation the operation to execute with a connection
   * @throws DatabaseException if a database error occurs
   */
  public void executeVoid(SqlConsumer<Connection> operation) {
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
   * Returns a new Transactor with the given listener merged into the existing strategy.
   *
   * @param listener the listener to merge
   * @return a new Transactor with the merged listener
   */
  public Transactor mergeListener(QueryListener listener) {
    return new Transactor(connect, strategy.mergeListener(listener));
  }

  /**
   * Execute an operation with a one-shot strategy override merged on top of the base strategy.
   *
   * @param <T> the result type
   * @param override the strategy to merge for this execution
   * @param operation the operation to execute
   * @return the operation result
   * @throws DatabaseException if a database error occurs
   */
  public <T> T execute(Strategy override, SqlFunction<Connection, T> operation) {
    return withStrategy(override).execute(operation);
  }

  public static Transactor create(DatabaseConfig config) {
    return SimpleDataSource.create(config).transactor();
  }

  public static Transactor create(DatabaseConfig config, Strategy strategy) {
    return SimpleDataSource.create(config).transactor(strategy);
  }

  public static Transactor create(DatabaseConfig config, ConnectionSettings settings) {
    return SimpleDataSource.create(config, settings).transactor();
  }

  public static Transactor create(
      DatabaseConfig config, ConnectionSettings settings, Strategy strategy) {
    return SimpleDataSource.create(config, settings).transactor(strategy);
  }

  /**
   * Default strategy: manual transactions with commit on success, close always.
   *
   * <p>Behavior:
   *
   * <ul>
   *   <li>onBegin: setAutoCommit(false)
   *   <li>onSuccess: commit()
   *   <li>onFailure: no-op (caller handles exceptions)
   *   <li>onComplete: close()
   * </ul>
   *
   * @return a strategy for manual transaction management
   */
  public static Strategy defaultStrategy() {
    return Strategy.empty()
        .replaceOnBegin(conn -> conn.setAutoCommit(false))
        .replaceOnSuccess(Connection::commit)
        .replaceOnComplete(Connection::close);
  }

  /**
   * Strategy for auto-commit mode (no manual transactions).
   *
   * <p>Behavior:
   *
   * <ul>
   *   <li>onBegin: no-op
   *   <li>onSuccess: no-op
   *   <li>onFailure: no-op
   *   <li>onComplete: close()
   * </ul>
   *
   * @return a strategy for auto-commit mode
   */
  public static Strategy autoCommitStrategy() {
    return Strategy.empty().replaceOnComplete(Connection::close);
  }

  /**
   * Strategy with rollback on error.
   *
   * <p>Behavior:
   *
   * <ul>
   *   <li>onBegin: setAutoCommit(false)
   *   <li>onSuccess: commit()
   *   <li>onFailure: rollback() (silently ignores rollback failures)
   *   <li>onComplete: close()
   * </ul>
   *
   * @return a strategy that rolls back on error
   */
  public static Strategy rollbackOnErrorStrategy() {
    return Strategy.empty()
        .replaceOnBegin(conn -> conn.setAutoCommit(false))
        .replaceOnSuccess(Connection::commit)
        .replaceOnFailure(
            (conn, t) -> {
              try {
                if (!conn.getAutoCommit() && !conn.isClosed()) {
                  conn.rollback();
                }
              } catch (SQLException ignored) {
              }
            })
        .replaceOnComplete(Connection::close);
  }

  /**
   * Strategy for testing: always rollback instead of commit.
   *
   * <p>Behavior:
   *
   * <ul>
   *   <li>onBegin: setAutoCommit(false)
   *   <li>onSuccess: rollback() (instead of commit, to keep test data isolated)
   *   <li>onFailure: no-op (caller handles exceptions)
   *   <li>onComplete: close()
   * </ul>
   *
   * @return a strategy for testing that always rolls back
   */
  public static Strategy testStrategy() {
    return Strategy.empty()
        .replaceOnBegin(conn -> conn.setAutoCommit(false))
        .replaceOnSuccess(Connection::rollback)
        .replaceOnComplete(Connection::close);
  }

  /**
   * Data type representing the common setup, error-handling, and cleanup strategy associated with
   * an SQL transaction. A `Transactor` uses a `Strategy` to wrap programs prior to execution.
   *
   * @param onBegin a program to prepare the connection for use
   * @param onSuccess a program to run on success
   * @param onFailure a program to run on failure (catch), receives the connection and the throwable
   * @param onComplete a program to run in all cases (finally)
   * @param listener a query listener for observability (use QueryListener.NOOP for none)
   */
  public record Strategy(
      SqlConsumer<Connection> onBegin,
      SqlConsumer<Connection> onSuccess,
      SqlBiConsumer<Connection, Throwable> onFailure,
      SqlConsumer<Connection> onComplete,
      QueryListener listener) {

    public static Strategy empty() {
      return new Strategy(conn -> {}, conn -> {}, (conn, t) -> {}, conn -> {}, QueryListener.NOOP);
    }

    public Strategy replaceOnBegin(SqlConsumer<Connection> onBegin) {
      return new Strategy(onBegin, onSuccess, onFailure, onComplete, listener);
    }

    public Strategy replaceOnSuccess(SqlConsumer<Connection> onSuccess) {
      return new Strategy(onBegin, onSuccess, onFailure, onComplete, listener);
    }

    public Strategy replaceOnFailure(SqlBiConsumer<Connection, Throwable> onFailure) {
      return new Strategy(onBegin, onSuccess, onFailure, onComplete, listener);
    }

    public Strategy replaceOnComplete(SqlConsumer<Connection> onComplete) {
      return new Strategy(onBegin, onSuccess, onFailure, onComplete, listener);
    }

    public Strategy replaceListener(QueryListener listener) {
      return new Strategy(onBegin, onSuccess, onFailure, onComplete, listener);
    }

    public Strategy mergeOnBegin(SqlConsumer<Connection> other) {
      SqlConsumer<Connection> existing = this.onBegin;
      return new Strategy(
          conn -> {
            existing.apply(conn);
            other.apply(conn);
          },
          onSuccess,
          onFailure,
          onComplete,
          listener);
    }

    public Strategy mergeOnSuccess(SqlConsumer<Connection> other) {
      SqlConsumer<Connection> existing = this.onSuccess;
      return new Strategy(
          onBegin,
          conn -> {
            existing.apply(conn);
            other.apply(conn);
          },
          onFailure,
          onComplete,
          listener);
    }

    public Strategy mergeOnFailure(SqlBiConsumer<Connection, Throwable> other) {
      SqlBiConsumer<Connection, Throwable> existing = this.onFailure;
      return new Strategy(
          onBegin,
          onSuccess,
          (conn, t) -> {
            existing.apply(conn, t);
            other.apply(conn, t);
          },
          onComplete,
          listener);
    }

    public Strategy mergeOnComplete(SqlConsumer<Connection> other) {
      SqlConsumer<Connection> existing = this.onComplete;
      return new Strategy(
          onBegin,
          onSuccess,
          onFailure,
          conn -> {
            existing.apply(conn);
            other.apply(conn);
          },
          listener);
    }

    public Strategy mergeListener(QueryListener other) {
      return new Strategy(onBegin, onSuccess, onFailure, onComplete, listener.compose(other));
    }

    public Strategy merge(Strategy other) {
      return new Strategy(
          conn -> {
            onBegin.apply(conn);
            other.onBegin.apply(conn);
          },
          conn -> {
            onSuccess.apply(conn);
            other.onSuccess.apply(conn);
          },
          (conn, t) -> {
            onFailure.apply(conn, t);
            other.onFailure.apply(conn, t);
          },
          conn -> {
            onComplete.apply(conn);
            other.onComplete.apply(conn);
          },
          QueryListener.compose(listener, other.listener));
    }
  }
}
