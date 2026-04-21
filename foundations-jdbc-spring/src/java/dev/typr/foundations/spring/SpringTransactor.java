package dev.typr.foundations.spring;

import dev.typr.foundations.*;
import dev.typr.foundations.internal.ConnectionJdbc;
import dev.typr.foundations.internal.ConnectionReadJdbc;
import java.sql.SQLException;
import java.util.function.Function;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Factory for creating Spring-aware {@link dev.typr.foundations.Transactor} instances.
 *
 * <p>The transactor automatically adapts to the current Spring transaction context:
 *
 * <ul>
 *   <li>Inside {@code @Transactional}: joins the existing transaction (no commit/rollback)
 *   <li>Outside {@code @Transactional}: manages its own transaction (begin/commit/rollback)
 * </ul>
 *
 * <p>Usage as a Spring bean:
 *
 * <pre>{@code
 * @Configuration
 * public class AppConfig {
 *     @Bean
 *     public Transactor transactor(DataSource dataSource) {
 *         return SpringTransactor.create(dataSource);
 *     }
 * }
 * }</pre>
 */
public final class SpringTransactor {

  private SpringTransactor() {}

  /**
   * Create a Spring-aware transactor with default exception mapping.
   *
   * <p>For structured database-specific errors (e.g. PostgreSQL {@link
   * DatabaseException.Postgres}), use {@link #create(DataSource, Function)} with the appropriate
   * mapper (e.g. {@code config::mapException}).
   *
   * @param dataSource the Spring-managed DataSource
   * @return a Spring-aware transactor
   */
  public static TransactorJdbc create(DataSource dataSource) {
    return new SpringSqlExecutor(dataSource, DatabaseException.Jdbc::new);
  }

  /**
   * Create a Spring-aware transactor with a custom exception mapper.
   *
   * <p>Use this overload to get structured database-specific errors:
   *
   * <pre>{@code
   * var config = PgConfig.builder("localhost", 5432, "mydb", "user", "pass").build();
   * var tx = SpringTransactor.create(dataSource, config::mapException);
   * }</pre>
   *
   * @param dataSource the Spring-managed DataSource
   * @param exceptionMapper maps {@link SQLException} to {@link DatabaseException}
   * @return a Spring-aware transactor
   */
  public static TransactorJdbc create(
      DataSource dataSource, Function<SQLException, DatabaseException> exceptionMapper) {
    return new SpringSqlExecutor(dataSource, exceptionMapper);
  }

  private static final class SpringSqlExecutor
      implements TransactorJdbc, dev.typr.foundations.RollbackCapable {
    private final DataSource dataSource;
    private final Function<SQLException, DatabaseException> exceptionMapper;

    SpringSqlExecutor(
        DataSource dataSource, Function<SQLException, DatabaseException> exceptionMapper) {
      this.dataSource = dataSource;
      this.exceptionMapper = exceptionMapper;
    }

    @Override
    public <T> T executeJdbc(SqlFunction<java.sql.Connection, T> operation) {
      return withConnection(operation);
    }

    @Override
    public <T> T execute(Operation<T> op) {
      return withConnection(conn -> new ConnectionJdbc(conn, exceptionMapper).execute(op));
    }

    @Override
    public <T> T transact(SqlFunction<dev.typr.foundations.Connection, T> fn) {
      return doTransact(fn, false);
    }

    @Override
    public <T> T transactRollback(SqlFunction<dev.typr.foundations.Connection, T> fn) {
      return doTransact(fn, true);
    }

    @Override
    public <T> T transactRead(SqlFunction<ConnectionRead, T> fn) {
      return withConnection(conn -> fn.apply(new ConnectionReadJdbc(conn, exceptionMapper)));
    }

    private <T> T doTransact(
        SqlFunction<dev.typr.foundations.Connection, T> fn, boolean rollbackOnly) {
      return withConnection(
          conn -> {
            boolean springManaged = TransactionSynchronizationManager.isActualTransactionActive();
            if (!springManaged) {
              conn.setAutoCommit(false);
            }
            T result = fn.apply(new ConnectionJdbc(conn, exceptionMapper));
            if (!springManaged) {
              if (rollbackOnly) {
                conn.rollback();
              } else {
                conn.commit();
              }
            }
            return result;
          });
    }

    private <T> T withConnection(SqlFunction<java.sql.Connection, T> operation) {
      try {
        java.sql.Connection conn = DataSourceUtils.getConnection(dataSource);
        try {
          return operation.apply(conn);
        } catch (SQLException | RuntimeException e) {
          boolean springManaged = TransactionSynchronizationManager.isActualTransactionActive();
          if (!springManaged) {
            try {
              if (!conn.isClosed() && !conn.getAutoCommit()) {
                conn.rollback();
              }
            } catch (SQLException ignored) {
            }
          }
          throw e;
        } finally {
          DataSourceUtils.releaseConnection(conn, dataSource);
        }
      } catch (SQLException e) {
        throw exceptionMapper.apply(e);
      }
    }
  }
}
