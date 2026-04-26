package dev.typr.foundations;

import static org.junit.Assert.*;

import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

public class WhenDslTest {

  private <T> T withDuckDb(SqlFunction<Connection, T> fn) {
    try (var conn = DriverManager.getConnection("jdbc:duckdb:")) {
      conn.setAutoCommit(false);
      var mc = new dev.typr.foundations.internal.ConnectionJdbc(conn);
      Fragment.of("CREATE TABLE products (id INTEGER, name VARCHAR, price DOUBLE, active BOOLEAN)")
          .execute()
          .run(mc);
      Fragment.of(
              "INSERT INTO products VALUES (1,'Widget',9.99,true),(2,'Gadget',24.99,false),(3,'Gizmo',14.99,true)")
          .execute()
          .run(mc);
      return fn.apply(mc);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // ── Execution tests ─────────────────────────────────────────────

  @Test
  public void appendIfTrue() {
    withDuckDb(
        conn -> {
          var query =
              Fragment.of("SELECT id FROM products WHERE 1=1")
                  .optionally(true)
                  .append(" AND active = TRUE")
                  .query(RowCodec.of(DuckDbTypes.integer).all());
          List<Integer> ids = query.run(conn);
          assertEquals(List.of(1, 3), ids);
          return null;
        });
  }

  @Test
  public void appendIfFalse() {
    withDuckDb(
        conn -> {
          var query =
              Fragment.of("SELECT id FROM products WHERE 1=1")
                  .optionally(false)
                  .append(" AND active = TRUE")
                  .query(RowCodec.of(DuckDbTypes.integer).all());
          List<Integer> ids = query.run(conn);
          assertEquals(List.of(1, 2, 3), ids);
          return null;
        });
  }

  @Test
  public void appendIfPresentWithValue() {
    withDuckDb(
        conn -> {
          Optional<String> optName = Optional.of("Widget");
          var query =
              Fragment.of("SELECT id FROM products WHERE 1=1")
                  .optionally(optName)
                  .append(" AND name = ", DuckDbTypes.varchar)
                  .query(RowCodec.of(DuckDbTypes.integer).all());
          List<Integer> ids = query.run(conn);
          assertEquals(List.of(1), ids);
          return null;
        });
  }

  @Test
  public void appendIfPresentEmpty() {
    withDuckDb(
        conn -> {
          Optional<String> optName = Optional.empty();
          var query =
              Fragment.of("SELECT id FROM products WHERE 1=1")
                  .optionally(optName)
                  .append(" AND name = ", DuckDbTypes.varchar)
                  .query(RowCodec.of(DuckDbTypes.integer).all());
          List<Integer> ids = query.run(conn);
          assertEquals(List.of(1, 2, 3), ids);
          return null;
        });
  }

  @Test
  public void chooseTrue() {
    withDuckDb(
        conn -> {
          var query =
              Fragment.of("SELECT id FROM products")
                  .optionally(true)
                  .append(" ORDER BY id ASC", " ORDER BY id DESC")
                  .query(RowCodec.of(DuckDbTypes.integer).all());
          List<Integer> ids = query.run(conn);
          assertEquals(List.of(1, 2, 3), ids);
          return null;
        });
  }

  @Test
  public void chooseFalse() {
    withDuckDb(
        conn -> {
          var query =
              Fragment.of("SELECT id FROM products")
                  .optionally(false)
                  .append(" ORDER BY id ASC", " ORDER BY id DESC")
                  .query(RowCodec.of(DuckDbTypes.integer).all());
          List<Integer> ids = query.run(conn);
          assertEquals(List.of(3, 2, 1), ids);
          return null;
        });
  }

  @Test
  public void multipleWhens() {
    withDuckDb(
        conn -> {
          Optional<String> optName = Optional.of("Gizmo");
          var query =
              Fragment.of("SELECT id FROM products WHERE 1=1")
                  .optionally(optName)
                  .append(" AND name = ", DuckDbTypes.varchar)
                  .optionally(true)
                  .append(" AND active = TRUE")
                  .query(RowCodec.of(DuckDbTypes.integer).all());
          List<Integer> ids = query.run(conn);
          assertEquals(List.of(3), ids);
          return null;
        });
  }

  // ── QA variant expansion tests ──────────────────────────────────

  @Test
  public void analysisExpandsSingleBranch() {
    var query =
        Fragment.of("SELECT id FROM products WHERE 1=1")
            .optionally(true)
            .append(" AND active = TRUE")
            .query(RowCodec.of(DuckDbTypes.integer).all());
    List<Fragment> variants = OptionallyResolver.analysisVariants(query.query());
    assertEquals(2, variants.size());
  }

  @Test
  public void analysisExpandsMultipleBranches() {
    var query =
        Fragment.of("SELECT id FROM products WHERE 1=1")
            .optionally(Optional.of("x"))
            .append(" AND name = ", DuckDbTypes.varchar)
            .optionally(true)
            .append(" AND active = TRUE")
            .optionally(true)
            .append(" ORDER BY id ASC", " ORDER BY id DESC")
            .query(RowCodec.of(DuckDbTypes.integer).all());
    List<Fragment> variants = OptionallyResolver.analysisVariants(query.query());
    // 2 * 2 * 2 = 8 variants
    assertEquals(8, variants.size());
  }

  @Test
  public void analysisVariantHasCorrectParamTypes() {
    var fragment =
        Fragment.of("SELECT id FROM products WHERE 1=1")
            .optionally(Optional.<String>empty())
            .append(" AND name = ", DuckDbTypes.varchar);
    List<Fragment> variants = OptionallyResolver.analysisVariants(fragment);
    assertEquals(2, variants.size());

    // Variant 0: template with param — should have VARCHAR type
    assertEquals(1, variants.get(0).parameterTypes().size());
    assertEquals("VARCHAR", variants.get(0).parameterTypes().get(0).typename().sqlType());

    // Variant 1: EMPTY — no params
    assertEquals(0, variants.get(1).parameterTypes().size());
  }

  @Test
  public void qaChecksAllVariantsAgainstDb() {
    withDuckDb(
        conn -> {
          Optional<String> optName = Optional.of("Widget");
          var query =
              Fragment.of("SELECT id, name FROM products WHERE 1=1")
                  .optionally(optName)
                  .append(" AND name = ", DuckDbTypes.varchar)
                  .optionally(true)
                  .append(" AND active = TRUE")
                  .query(
                      RowCodec.of(DuckDbTypes.integer, DuckDbTypes.varchar)
                          .all());

          // All 4 variants should pass analysis
          List<QueryAnalysis> analyses = QueryAnalyzer.analyze(query, conn);
          assertEquals(4, analyses.size());
          for (QueryAnalysis a : analyses) {
            assertTrue("Failed: " + a.report(), a.succeeded());
          }
          return null;
        });
  }
}
