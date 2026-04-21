package dev.typr.foundations;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * Internal decorator that adds cross-cutting concerns (listener, rollback-only) to any {@link
 * Transactor}.
 */
final class TransactorDecorator implements Transactor {

  private final Transactor underlying;
  private final boolean rollbackOnly;
  private final QueryListener listener;

  TransactorDecorator(Transactor underlying, boolean rollbackOnly, QueryListener listener) {
    this.underlying = underlying;
    this.rollbackOnly = rollbackOnly;
    this.listener = listener;
  }

  // ========== Transactor ==========

  @Override
  public <T> T execute(Operation<T> op) {
    if (listener != QueryListener.NOOP) {
      return underlying.execute(op.withListener(listener));
    }
    return underlying.execute(op);
  }

  @Override
  public <T> T transact(SqlFunction<Connection, T> fn) {
    SqlFunction<Connection, T> wrapped = instrumentTransactFn(fn);
    long start = System.nanoTime();
    try {
      T result =
          rollbackOnly ? doTransactRollback(underlying, wrapped) : underlying.transact(wrapped);
      fireAfterTransaction(start);
      return result;
    } catch (RuntimeException e) {
      fireFailedTransaction(start, e);
      throw e;
    } catch (Exception e) {
      fireFailedTransaction(start, e);
      throw new RuntimeException(e);
    }
  }

  private static <T> T doTransactRollback(Transactor tx, SqlFunction<Connection, T> fn) {
    if (tx instanceof TransactorDecorator d) return doTransactRollback(d.underlying, fn);
    if (tx instanceof RollbackCapable rc) return rc.transactRollback(fn);
    throw new UnsupportedOperationException(
        "rollbackOnly() requires a Transactor that supports rollback (JdbcSqlExecutor or"
            + " SpringTransactor)");
  }

  @Override
  public <T> T transactRead(SqlFunction<ConnectionRead, T> fn) {
    SqlFunction<ConnectionRead, T> wrapped =
        listener != QueryListener.NOOP ? rc -> fn.apply(instrumentedRead(rc)) : fn;
    return underlying.transactRead(wrapped);
  }

  // ========== Decoration — compose with existing ==========

  @Override
  public Transactor rollbackOnly() {
    return new TransactorDecorator(underlying, true, listener);
  }

  @Override
  public Transactor withListener(QueryListener newListener) {
    QueryListener composed =
        listener == QueryListener.NOOP ? newListener : listener.compose(newListener);
    return new TransactorDecorator(underlying, rollbackOnly, composed);
  }

  @Override
  public Transactor mergeListener(QueryListener other) {
    return new TransactorDecorator(underlying, rollbackOnly, listener.compose(other));
  }

  // ========== Lifecycle ==========

  @Override
  public void close() throws Exception {
    underlying.close();
  }

  // ========== Listener wiring ==========

  private <T> SqlFunction<Connection, T> instrumentTransactFn(SqlFunction<Connection, T> fn) {
    if (listener == QueryListener.NOOP) return fn;
    return mc -> fn.apply(instrumentedReadWrite(mc));
  }

  private Connection instrumentedReadWrite(Connection inner) {
    return new Connection() {
      @Override
      public <R> R execute(OperationRead<R> op) {
        return inner.execute(op.withListener(listener));
      }

      @Override
      public <R> R execute(Operation<R> op) {
        return inner.execute(op.withListener(listener));
      }

      @Override
      public <R> List<R> query(Fragment sql, RowCodec<R> codec) {
        return execute(sql.query(codec.all()));
      }

      @Override
      public <R> Optional<R> queryFirst(Fragment sql, RowCodec<R> codec) {
        return execute(sql.query(codec.first()));
      }

      @Override
      public int update(Fragment sql) {
        return execute(sql.update());
      }

      @Override
      public java.sql.Connection unwrap() {
        return inner.unwrap();
      }
    };
  }

  private ConnectionRead instrumentedRead(ConnectionRead inner) {
    return new ConnectionRead() {
      @Override
      public <R> R execute(OperationRead<R> op) {
        return inner.execute(op.withListener(listener));
      }

      @Override
      public <R> List<R> query(Fragment sql, RowCodec<R> codec) {
        return execute(sql.query(codec.all()));
      }

      @Override
      public <R> Optional<R> queryFirst(Fragment sql, RowCodec<R> codec) {
        return execute(sql.query(codec.first()));
      }
    };
  }

  private void fireAfterTransaction(long startNanos) {
    if (listener != QueryListener.NOOP) {
      listener.afterTransaction(
          new TransactionEvent(
              Duration.ofNanos(System.nanoTime() - startNanos), java.util.Optional.empty()));
    }
  }

  private void fireFailedTransaction(long startNanos, Throwable error) {
    if (listener != QueryListener.NOOP) {
      listener.failedTransaction(
          new TransactionEvent(
              Duration.ofNanos(System.nanoTime() - startNanos), java.util.Optional.of(error)));
    }
  }
}
