package dev.typr.foundations.spring;

import dev.typr.foundations.SqlConsumer;
import dev.typr.foundations.Transactor;
import dev.typr.foundations.Transactor.Strategy;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Factory for creating Spring-aware {@link Transactor} instances.
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
 *
 * <p>Then inject and use:
 *
 * <pre>{@code
 * @Service
 * public class OrderService {
 *     private final Transactor tx;
 *
 *     public OrderService(Transactor tx) {
 *         this.tx = tx;
 *     }
 *
 *     @Transactional
 *     public List<Order> getOrders() throws SQLException {
 *         return new Operation.Query<>(sql, parser).transact(tx);
 *     }
 * }
 * }</pre>
 */
public final class SpringTransactor {

  private SpringTransactor() {}

  /**
   * Create a Spring-aware transactor for the given datasource.
   *
   * <p>The returned transactor automatically detects whether it's running inside a Spring
   * {@code @Transactional} context and adapts its behavior accordingly.
   *
   * @param dataSource the datasource to obtain connections from
   * @return a transactor that integrates with Spring transaction management
   */
  public static Transactor create(DataSource dataSource) {
    return new Transactor(() -> DataSourceUtils.getConnection(dataSource), strategy(dataSource));
  }

  private static Strategy strategy(DataSource dataSource) {
    return new Strategy(before(), after(), oops(), always(dataSource));
  }

  private static SqlConsumer<Connection> before() {
    return conn -> {
      if (!TransactionSynchronizationManager.isActualTransactionActive()) {
        conn.setAutoCommit(false);
      }
    };
  }

  private static SqlConsumer<Connection> after() {
    return conn -> {
      if (!TransactionSynchronizationManager.isActualTransactionActive()) {
        conn.commit();
      }
    };
  }

  private static java.util.function.Consumer<Throwable> oops() {
    return err -> {};
  }

  private static SqlConsumer<Connection> always(DataSource dataSource) {
    return conn -> {
      if (!TransactionSynchronizationManager.isActualTransactionActive()) {
        try {
          if (!conn.isClosed() && !conn.getAutoCommit()) {
            conn.rollback();
          }
        } catch (SQLException ignored) {
        }
      }
      DataSourceUtils.releaseConnection(conn, dataSource);
    };
  }
}
