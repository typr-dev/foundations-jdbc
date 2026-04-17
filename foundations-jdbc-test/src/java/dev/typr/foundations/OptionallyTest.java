package dev.typr.foundations;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.sql.Connection;
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

  @Test
  public void testBooleanOptionally() throws SQLException {
    try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      conn.createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      conn.createStatement()
          .execute(
              "INSERT INTO " + table + " VALUES ('Widget', 9.99, true), ('Gadget', 19.99, false)");

      var template =
          Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
              .optionally(Fragment.of(" AND active = true"))
              .append(" ORDER BY name")
              .query(productCodec.all());

      List<Product> result1 = template.on(true).run(conn);
      assertEquals(1, result1.size());
      assertEquals("Widget", result1.getFirst().name());

      List<Product> result2 = template.on(false).run(conn);
      assertEquals(2, result2.size());
    }
  }

  @Test
  public void testSingleParamOptionally() throws SQLException {
    try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      conn.createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      conn.createStatement()
          .execute(
              "INSERT INTO "
                  + table
                  + " VALUES ('Widget', 9.99, true), ('Gadget', 19.99, false), ('Wand', 5.00,"
                  + " true)");

      var template =
          Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
              .optionally(Fragment.of(" AND name = ").param(DuckDbTypes.varchar))
              .append(" ORDER BY name")
              .query(productCodec.all());

      List<Product> result1 = template.on(Optional.of("Widget")).run(conn);
      assertEquals(1, result1.size());
      assertEquals("Widget", result1.getFirst().name());

      List<Product> result2 = template.on(Optional.empty()).run(conn);
      assertEquals(3, result2.size());
    }
  }

  @Test
  public void testMultipleOptionally() throws SQLException {
    try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      conn.createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      conn.createStatement()
          .execute(
              "INSERT INTO "
                  + table
                  + " VALUES "
                  + "('Widget', 9.99, true), ('Gadget', 19.99, false), ('Wand', 5.00, true)");

      var template =
          Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
              .optionally(Fragment.of(" AND name = ").param(DuckDbTypes.varchar))
              .optionally(Fragment.of(" AND price >= ").param(DuckDbTypes.decimalOf(10, 2)))
              .optionally(Fragment.of(" AND active = true"))
              .append(" ORDER BY name")
              .query(productCodec.all());

      List<Product> result1 = template.on(Optional.of("Widget"), Optional.empty(), false).run(conn);
      assertEquals(1, result1.size());
      assertEquals("Widget", result1.getFirst().name());

      List<Product> result2 =
          template.on(Optional.empty(), Optional.of(new BigDecimal("6.00")), true).run(conn);
      assertEquals(1, result2.size());
      assertEquals("Widget", result2.getFirst().name());

      List<Product> result3 = template.on(Optional.empty(), Optional.empty(), false).run(conn);
      assertEquals(3, result3.size());
    }
  }

  @Test
  public void testMixedParamAndOptionally() throws SQLException {
    try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      conn.createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      conn.createStatement()
          .execute(
              "INSERT INTO "
                  + table
                  + " VALUES "
                  + "('Widget', 9.99, true), ('Gadget', 19.99, false), ('Wand', 5.00, true)");

      var template =
          Fragment.of("SELECT name, price, active FROM " + table + " WHERE active = ")
              .param(DuckDbTypes.boolean_)
              .optionally(Fragment.of(" AND name = ").param(DuckDbTypes.varchar))
              .append(" ORDER BY name")
              .query(productCodec.all());

      List<Product> result1 = template.on(true, Optional.empty()).run(conn);
      assertEquals(2, result1.size());

      List<Product> result2 = template.on(true, Optional.of("Widget")).run(conn);
      assertEquals(1, result2.size());
      assertEquals("Widget", result2.getFirst().name());
    }
  }

  @Test
  public void testTwoParamOptionally() throws SQLException {
    try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      conn.createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");
      conn.createStatement()
          .execute(
              "INSERT INTO "
                  + table
                  + " VALUES "
                  + "('Widget', 9.99, true), ('Gadget', 19.99, false), ('Wand', 5.00, true)");

      var decType = DuckDbTypes.decimalOf(10, 2);
      var template =
          Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
              .optionally(
                  Fragment.of(" AND price BETWEEN ").param(decType).append(" AND ").param(decType))
              .append(" ORDER BY name")
              .query(productCodec.all());

      List<Product> result1 =
          template
              .on(Optional.of(Tuple.of(new BigDecimal("5.00"), new BigDecimal("10.00"))))
              .run(conn);
      assertEquals(2, result1.size());
      assertEquals("Wand", result1.get(0).name());
      assertEquals("Widget", result1.get(1).name());

      List<Product> result2 = template.on(Optional.empty()).run(conn);
      assertEquals(3, result2.size());
    }
  }

  @Test
  public void testAnalysisVariants() throws SQLException {
    try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      conn.createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");

      var template =
          Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
              .optionally(Fragment.of(" AND name = ").param(DuckDbTypes.varchar))
              .optionally(Fragment.of(" AND price >= ").param(DuckDbTypes.decimalOf(10, 2)))
              .optionally(Fragment.of(" AND active = true"))
              .append(" ORDER BY name")
              .query(productCodec.all());

      List<Fragment> variants = OptionallyResolver.analysisVariants(template.fragment());
      assertEquals(8, variants.size());

      List<QueryAnalysis> analyses = QueryAnalyzer.analyze(template, conn);
      assertEquals(8, analyses.size());
      for (QueryAnalysis analysis : analyses) {
        assertTrue("Analysis should succeed: " + analysis.report(), analysis.succeeded());
      }
    }
  }

  @Test
  public void testAnalysisWithOptionallyProducesCorrectVariantCount() throws SQLException {
    try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      String table = uniqueTableName();
      conn.createStatement()
          .execute(
              "CREATE TABLE " + table + " (name VARCHAR, price DECIMAL(10,2), active BOOLEAN)");

      var template =
          Fragment.of("SELECT name, price, active FROM " + table + " WHERE 1=1")
              .optionally(Fragment.of(" AND name = ").param(DuckDbTypes.varchar))
              .append(" ORDER BY name")
              .query(productCodec.all());

      List<QueryAnalysis> analyses = QueryAnalyzer.analyze(template, conn);
      assertEquals(2, analyses.size());
      assertTrue(analyses.get(0).succeeded());
      assertTrue(analyses.get(1).succeeded());
    }
  }

  @Test
  public void testOptionallyRendering() {
    var fragment =
        Fragment.of("SELECT * FROM t WHERE 1=1")
            .optionally(Fragment.of(" AND active = true"))
            .done();

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
