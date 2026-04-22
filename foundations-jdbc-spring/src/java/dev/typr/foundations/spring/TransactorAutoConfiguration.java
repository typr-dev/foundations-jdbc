package dev.typr.foundations.spring;

import dev.typr.foundations.Transactor;
import dev.typr.foundations.TransactorJdbc;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for {@link Transactor}.
 *
 * <p>Automatically creates a Spring-aware {@link Transactor} bean when a {@link DataSource} is
 * available. The transactor integrates with Spring's transaction management:
 *
 * <ul>
 *   <li>Inside {@code @Transactional}: joins the existing transaction
 *   <li>Outside {@code @Transactional}: manages its own transaction
 * </ul>
 *
 * <p>Simply inject the transactor in your services:
 *
 * <pre>{@code
 * @Service
 * public class ProductService {
 *     private final Transactor tx;
 *
 *     public ProductService(Transactor tx) {
 *         this.tx = tx;
 *     }
 *
 *     public List<Product> findAll() throws SQLException {
 *         return Fragment.of("SELECT * FROM product")
 *             .query(Product.rowCodec.all())
 *             .transactRead(tx);
 *     }
 * }
 * }</pre>
 */
@AutoConfiguration
@ConditionalOnClass({DataSource.class, Transactor.class})
public class TransactorAutoConfiguration {

  @Bean
  @ConditionalOnBean(DataSource.class)
  @ConditionalOnMissingBean(Transactor.class)
  public TransactorJdbc transactor(DataSource dataSource) {
    return SpringTransactor.create(dataSource);
  }
}
