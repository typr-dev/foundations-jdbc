package dev.typr.foundations;

import static org.junit.jupiter.api.Assertions.*;

import dev.typr.foundations.connect.DuckDbConfig;
import dev.typr.foundations.hikari.HikariDataSourceFactory;
import dev.typr.foundations.hikari.PoolConfig;
import dev.typr.foundations.spring.SpringTransactor;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for SpringTransactor with Spring Boot auto-configuration.
 */
@SpringBootTest
class SpringTransactorTest {

  @Configuration
  @EnableAutoConfiguration
  static class TestConfig {
    @Bean
    DataSource dataSource() {
      return HikariDataSourceFactory.create(
          DuckDbConfig.inMemory().build(),
          PoolConfig.builder().maximumPoolSize(3).build()
      ).asDataSource();
    }

    @Bean
    TransactionalService transactionalService(Transactor tx) {
      return new TransactionalService(tx);
    }
  }

  static class TransactionalService {
    private final Transactor tx;

    TransactionalService(Transactor tx) {
      this.tx = tx;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void insertRequired(int id, String name) throws SQLException {
      Fragment.of("INSERT INTO spring_test VALUES (")
          .value(DuckDbTypes.integer, id).append(", ")
          .value(DuckDbTypes.varchar, name).append(")")
          .update()
          .transact(tx);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertRequiresNew(int id, String name) throws SQLException {
      Fragment.of("INSERT INTO spring_test VALUES (")
          .value(DuckDbTypes.integer, id).append(", ")
          .value(DuckDbTypes.varchar, name).append(")")
          .update()
          .transact(tx);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void insertMandatory(int id, String name) throws SQLException {
      Fragment.of("INSERT INTO spring_test VALUES (")
          .value(DuckDbTypes.integer, id).append(", ")
          .value(DuckDbTypes.varchar, name).append(")")
          .update()
          .transact(tx);
    }

    @Transactional
    public void insertThenFail(int id, String name) throws SQLException {
      Fragment.of("INSERT INTO spring_test VALUES (")
          .value(DuckDbTypes.integer, id).append(", ")
          .value(DuckDbTypes.varchar, name).append(")")
          .update()
          .transact(tx);
      throw new RuntimeException("Simulated failure");
    }
  }

  @Autowired
  Transactor tx;

  @Autowired
  DataSource dataSource;

  @Autowired
  TransactionalService service;

  @BeforeEach
  void setUp() throws SQLException {
    Fragment.of("DROP TABLE IF EXISTS spring_test").update().transact(tx);
    Fragment.of("CREATE TABLE spring_test (id INTEGER PRIMARY KEY, name VARCHAR(100))")
        .update()
        .transact(tx);
  }

  @Test
  void transactorIsAutoConfigured() {
    assertNotNull(tx, "Transactor should be auto-configured when DataSource exists");
  }

  @Test
  void standaloneTransactionCommits() throws SQLException {
    // Insert data - should auto-commit since we're not in @Transactional
    Fragment.of("INSERT INTO spring_test VALUES (1, 'standalone')")
        .update()
        .transact(tx);

    // Verify it was committed
    String name = Fragment.of("SELECT name FROM spring_test WHERE id = 1")
        .query(RowCodec.of(DuckDbTypes.varchar).exactlyOne())
        .transact(tx);

    assertEquals("standalone", name);
  }

  @Test
  @Transactional
  void joinsExistingSpringTransaction() throws SQLException {
    // Multiple operations in same Spring transaction
    Fragment.of("INSERT INTO spring_test VALUES (1, 'first')")
        .update()
        .transact(tx);

    Fragment.of("INSERT INTO spring_test VALUES (2, 'second')")
        .update()
        .transact(tx);

    // Both should be visible within the same transaction
    List<String> names = Fragment.of("SELECT name FROM spring_test ORDER BY id")
        .query(RowCodec.of(DuckDbTypes.varchar).all())
        .transact(tx);

    assertEquals(List.of("first", "second"), names);
  }

  @Test
  void rollsBackOnException() throws SQLException {
    // Insert one row successfully
    Fragment.of("INSERT INTO spring_test VALUES (1, 'committed')")
        .update()
        .transact(tx);

    // Try a transaction that will fail
    try {
      tx.execute(conn -> {
        Fragment.of("INSERT INTO spring_test VALUES (2, 'will rollback')")
            .update()
            .runChecked(conn);
        throw new SQLException("Simulated failure");
      });
      fail("Should have thrown SQLException");
    } catch (SQLException e) {
      assertEquals("Simulated failure", e.getMessage());
    }

    // Verify only the first insert is present
    Integer count = Fragment.of("SELECT COUNT(*) FROM spring_test")
        .query(RowCodec.of(DuckDbTypes.integer).exactlyOne())
        .transact(tx);

    assertEquals(1, count, "Failed transaction should have rolled back");
  }

  @Test
  void canCreateTransactorManually() throws SQLException {
    Transactor manualTx = SpringTransactor.create(dataSource);

    Fragment.of("INSERT INTO spring_test VALUES (99, 'manual')")
        .update()
        .transact(manualTx);

    String name = Fragment.of("SELECT name FROM spring_test WHERE id = 99")
        .query(RowCodec.of(DuckDbTypes.varchar).exactlyOne())
        .transact(manualTx);

    assertEquals("manual", name);
  }

  @Test
  void propagationRequired() throws SQLException {
    // REQUIRED creates a new transaction when none exists
    service.insertRequired(1, "required");

    String name = Fragment.of("SELECT name FROM spring_test WHERE id = 1")
        .query(RowCodec.of(DuckDbTypes.varchar).exactlyOne())
        .transact(tx);

    assertEquals("required", name);
  }

  @Test
  @Transactional
  void propagationRequiredJoinsExisting() throws SQLException {
    // REQUIRED joins existing transaction
    service.insertRequired(1, "first");
    service.insertRequired(2, "second");

    List<String> names = Fragment.of("SELECT name FROM spring_test ORDER BY id")
        .query(RowCodec.of(DuckDbTypes.varchar).all())
        .transact(tx);

    assertEquals(List.of("first", "second"), names);
  }

  @Test
  void propagationRequiresNew() throws SQLException {
    // REQUIRES_NEW always creates a new transaction
    service.insertRequiresNew(1, "new-tx");

    String name = Fragment.of("SELECT name FROM spring_test WHERE id = 1")
        .query(RowCodec.of(DuckDbTypes.varchar).exactlyOne())
        .transact(tx);

    assertEquals("new-tx", name);
  }

  @Test
  void propagationMandatoryFailsWithoutTransaction() {
    // MANDATORY throws when no transaction exists
    assertThrows(Exception.class, () -> service.insertMandatory(1, "should-fail"));
  }

  @Test
  @Transactional
  void propagationMandatorySucceedsWithTransaction() throws SQLException {
    // MANDATORY works when transaction exists
    service.insertMandatory(1, "mandatory");

    String name = Fragment.of("SELECT name FROM spring_test WHERE id = 1")
        .query(RowCodec.of(DuckDbTypes.varchar).exactlyOne())
        .transact(tx);

    assertEquals("mandatory", name);
  }

  @Test
  void transactionRollsBackOnRuntimeException() throws SQLException {
    // Insert should be rolled back when RuntimeException is thrown
    try {
      service.insertThenFail(1, "will-rollback");
      fail("Should have thrown RuntimeException");
    } catch (RuntimeException e) {
      assertEquals("Simulated failure", e.getMessage());
    }

    Integer count = Fragment.of("SELECT COUNT(*) FROM spring_test")
        .query(RowCodec.of(DuckDbTypes.integer).exactlyOne())
        .transact(tx);

    assertEquals(0, count, "Transaction should have rolled back");
  }
}
