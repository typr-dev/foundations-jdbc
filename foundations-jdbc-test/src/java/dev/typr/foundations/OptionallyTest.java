package dev.typr.foundations;

import static org.junit.Assert.*;

import dev.typr.foundations.internal.ConnectionJdbc;
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class OptionallyTest {

  private static final AtomicInteger tableCounter = new AtomicInteger(0);

  private static String uniqueTableName() {
    return "opt_test_" + tableCounter.incrementAndGet();
  }

  record Product(String name, BigDecimal price, boolean active) {}

  static RowCodecNamed<Product> productCodec =
      RowCodec.<Product>namedBuilder()
          .field("name", DuckDbTypes.varchar, Product::name)
          .field("price", DuckDbTypes.decimalOf(10, 2), Product::price)
          .field("active", DuckDbTypes.boolean_, Product::active)
          .build(Product::new);

  static OperationRead<List<Product>> findProductsByActiveFlag(String table, boolean activeOnly) {
    return Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
        .optionally(activeOnly).append(" AND active = true")
        .append(" ORDER BY name")
        .query(productCodec.all());
  }

  static OperationRead<List<Product>> findProductsByName(String table, Optional<String> name) {
    return Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
        .optionally(name).append(" AND name = ", DuckDbTypes.varchar)
        .append(" ORDER BY name")
        .query(productCodec.all());
  }

  static OperationRead<List<Product>> findProductsMulti(
      String table,
      Optional<String> name,
      Optional<BigDecimal> minPrice,
      boolean activeOnly) {
    return Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
        .optionally(name).append(" AND name = ", DuckDbTypes.varchar)
        .optionally(minPrice).append(" AND price >= ", DuckDbTypes.decimalOf(10, 2))
        .optionally(activeOnly).append(" AND active = true")
        .append(" ORDER BY name")
        .query(productCodec.all());
  }

  static OperationRead<List<Product>> findProductsByActiveAndName(
      String table, boolean active, Optional<String> name) {
    return Fragment.of("SELECT name, price, active FROM " + table + " WHERE active = ")
        .value(DuckDbTypes.boolean_, active)
        .optionally(name).append(" AND name = ", DuckDbTypes.varchar)
        .append(" ORDER BY name")
        .query(productCodec.all());
  }

  static OperationRead<List<Product>> findProductsByPriceRange(
      String table, Optional<BigDecimal> minPrice, Optional<BigDecimal> maxPrice) {
    var decType = DuckDbTypes.decimalOf(10, 2);
    return Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
        .optionally(minPrice).append(" AND price >= ", decType)
        .optionally(maxPrice).append(" AND price <= ", decType)
        .append(" ORDER BY name")
        .query(productCodec.all());
  }

  @Test
  public void testBooleanOptionally() throws SQLException {
    try (java.sql.Connection jdbcConn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      jdbcConn
          .createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      jdbcConn
          .createStatement()
          .execute(
              "INSERT INTO " + table + " VALUES ('Widget', 9.99, true), ('Gadget', 19.99, false)");

      var conn = new ConnectionJdbc(jdbcConn);

      List<Product> result1 = findProductsByActiveFlag(table, true).run(conn);
      assertEquals(1, result1.size());
      assertEquals("Widget", result1.getFirst().name());

      List<Product> result2 = findProductsByActiveFlag(table, false).run(conn);
      assertEquals(2, result2.size());
    }
  }

  @Test
  public void testSingleParamOptionally() throws SQLException {
    try (java.sql.Connection jdbcConn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      jdbcConn
          .createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      jdbcConn
          .createStatement()
          .execute(
              "INSERT INTO "
                  + table
                  + " VALUES ('Widget', 9.99, true), ('Gadget', 19.99, false), ('Wand', 5.00,"
                  + " true)");

      var conn = new ConnectionJdbc(jdbcConn);

      List<Product> result1 = findProductsByName(table, Optional.of("Widget")).run(conn);
      assertEquals(1, result1.size());
      assertEquals("Widget", result1.getFirst().name());

      List<Product> result2 = findProductsByName(table, Optional.empty()).run(conn);
      assertEquals(3, result2.size());
    }
  }

  @Test
  public void testMultipleOptionally() throws SQLException {
    try (java.sql.Connection jdbcConn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      jdbcConn
          .createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      jdbcConn
          .createStatement()
          .execute(
              "INSERT INTO "
                  + table
                  + " VALUES "
                  + "('Widget', 9.99, true), ('Gadget', 19.99, false), ('Wand', 5.00, true)");

      var conn = new ConnectionJdbc(jdbcConn);

      List<Product> result1 =
          findProductsMulti(table, Optional.of("Widget"), Optional.empty(), false).run(conn);
      assertEquals(1, result1.size());
      assertEquals("Widget", result1.getFirst().name());

      List<Product> result2 =
          findProductsMulti(table, Optional.empty(), Optional.of(new BigDecimal("6.00")), true)
              .run(conn);
      assertEquals(1, result2.size());
      assertEquals("Widget", result2.getFirst().name());

      List<Product> result3 =
          findProductsMulti(table, Optional.empty(), Optional.empty(), false).run(conn);
      assertEquals(3, result3.size());
    }
  }

  @Test
  public void testMixedParamAndOptionally() throws SQLException {
    try (java.sql.Connection jdbcConn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      jdbcConn
          .createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      jdbcConn
          .createStatement()
          .execute(
              "INSERT INTO "
                  + table
                  + " VALUES "
                  + "('Widget', 9.99, true), ('Gadget', 19.99, false), ('Wand', 5.00, true)");

      var conn = new ConnectionJdbc(jdbcConn);

      List<Product> result1 =
          findProductsByActiveAndName(table, true, Optional.empty()).run(conn);
      assertEquals(2, result1.size());

      List<Product> result2 =
          findProductsByActiveAndName(table, true, Optional.of("Widget")).run(conn);
      assertEquals(1, result2.size());
      assertEquals("Widget", result2.getFirst().name());
    }
  }

  @Test
  public void testTwoOptionalsForRange() throws SQLException {
    try (java.sql.Connection jdbcConn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      jdbcConn
          .createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      jdbcConn
          .createStatement()
          .execute(
              "INSERT INTO "
                  + table
                  + " VALUES "
                  + "('Widget', 9.99, true), ('Gadget', 19.99, false), ('Wand', 5.00, true)");

      var conn = new ConnectionJdbc(jdbcConn);

      List<Product> result1 =
          findProductsByPriceRange(
                  table,
                  Optional.of(new BigDecimal("5.00")),
                  Optional.of(new BigDecimal("10.00")))
              .run(conn);
      assertEquals(2, result1.size());
      assertEquals("Wand", result1.get(0).name());
      assertEquals("Widget", result1.get(1).name());

      List<Product> result2 =
          findProductsByPriceRange(table, Optional.empty(), Optional.empty()).run(conn);
      assertEquals(3, result2.size());
    }
  }

  @Test
  public void testAnalysisVariants() throws SQLException {
    try (java.sql.Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      conn.createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");

      var op = findProductsMulti(table, Optional.empty(), Optional.empty(), false);

      List<QueryAnalysis> analyses = QueryAnalyzer.analyze(op, conn);
      assertEquals(8, analyses.size());
      for (QueryAnalysis analysis : analyses) {
        assertTrue("Analysis should succeed: " + analysis.report(), analysis.succeeded());
      }
    }
  }

  @Test
  public void testAnalysisWithOptionallyProducesCorrectVariantCount() throws SQLException {
    try (java.sql.Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      conn.createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");

      var op = findProductsByName(table, Optional.empty());

      List<QueryAnalysis> analyses = QueryAnalyzer.analyze(op, conn);
      assertEquals(2, analyses.size());
      assertTrue(analyses.get(0).succeeded());
      assertTrue(analyses.get(1).succeeded());
    }
  }

  @Test
  public void testOptionallyRendering() {
    var fragment =
        Fragment.of("SELECT * FROM t WHERE 1=1")
            .optionally(true).append(" AND active = true")
            .append(" ORDER BY name");

    String rendered = fragment.render();
    assertTrue(rendered.contains("AND active = true"));
  }

  @Test
  public void testCountParams() {
    assertEquals(0, Fragment.countParams(Fragment.of("hello")));
    assertEquals(1, Fragment.countParams(Fragment.of(" x = ").param(DuckDbTypes.integer).done()));
    assertEquals(
        2,
        Fragment.countParams(
            Fragment.of(" BETWEEN ")
                .param(DuckDbTypes.integer)
                .append(" AND ")
                .param(DuckDbTypes.integer)
                .done()));
  }
}
