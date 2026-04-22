package dev.typr.foundations.internal;

import dev.typr.foundations.*;
import java.sql.SQLException;
import java.util.function.Function;
import javax.sql.DataSource;

public final class TransactorJdbcImpl implements TransactorJdbc {

  private final DataSource dataSource;
  private final Function<SQLException, DatabaseException> exceptionMapper;

  private TransactorJdbcImpl(
      DataSource dataSource, Function<SQLException, DatabaseException> exceptionMapper) {
    this.dataSource = dataSource;
    this.exceptionMapper = exceptionMapper;
  }

  public static TransactorJdbcImpl create(DataSource dataSource) {
    return new TransactorJdbcImpl(dataSource, DatabaseException.Jdbc::new);
  }

  public static TransactorJdbcImpl create(
      DataSource dataSource, Function<SQLException, DatabaseException> exceptionMapper) {
    return new TransactorJdbcImpl(dataSource, exceptionMapper);
  }

  @Override
  public <T> T executeJdbc(SqlFunction<java.sql.Connection, T> operation) {
    return doExecuteJdbc(operation);
  }

  private <T> T doExecuteJdbc(SqlFunction<java.sql.Connection, T> operation) {
    try {
      java.sql.Connection conn = dataSource.getConnection();
      try {
        conn.setAutoCommit(false);
        T result = operation.apply(conn);
        conn.commit();
        return result;
      } catch (Exception e) {
        try {
          if (!conn.isClosed() && !conn.getAutoCommit()) {
            conn.rollback();
          }
        } catch (SQLException ignored) {
          e.addSuppressed(ignored);
        }
        if (e instanceof SQLException se) throw exceptionMapper.apply(se);
        if (e instanceof RuntimeException re) throw re;
        throw new RuntimeException(e);
      } finally {
        conn.close();
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public <T> T execute(Operation<T> op) {
    return doExecuteJdbc(conn -> new ConnectionJdbc(conn, exceptionMapper).execute(op));
  }

  @Override
  public <T> T transact(SqlFunction<Connection, T> fn) {
    return doTransact(fn, false);
  }

  @Override
  public <T> T transactRollback(SqlFunction<Connection, T> fn) {
    return doTransact(fn, true);
  }

  @Override
  public <T> T transactRead(SqlFunction<ConnectionRead, T> fn) {
    try {
      java.sql.Connection conn = dataSource.getConnection();
      try {
        return fn.apply(new ConnectionReadJdbc(conn, exceptionMapper));
      } catch (Exception e) {
        if (e instanceof SQLException se) throw exceptionMapper.apply(se);
        if (e instanceof RuntimeException re) throw re;
        throw new RuntimeException(e);
      } finally {
        conn.close();
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  private <T> T doTransact(SqlFunction<Connection, T> fn, boolean rollbackOnly) {
    try {
      java.sql.Connection conn = dataSource.getConnection();
      try {
        conn.setAutoCommit(false);
        T result = fn.apply(new ConnectionJdbc(conn, exceptionMapper));
        if (rollbackOnly) {
          conn.rollback();
        } else {
          conn.commit();
        }
        return result;
      } catch (Exception e) {
        try {
          if (!conn.isClosed() && !conn.getAutoCommit()) {
            conn.rollback();
          }
        } catch (SQLException ignored) {
          e.addSuppressed(ignored);
        }
        if (e instanceof SQLException se) throw exceptionMapper.apply(se);
        if (e instanceof RuntimeException re) throw re;
        throw new RuntimeException(e);
      } finally {
        conn.close();
      }
    } catch (SQLException e) {
      throw exceptionMapper.apply(e);
    }
  }

  @Override
  public void close() {
    if (dataSource instanceof AutoCloseable ac) {
      try {
        ac.close();
      } catch (RuntimeException e) {
        throw e;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
