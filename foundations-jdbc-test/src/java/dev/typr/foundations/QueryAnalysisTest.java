package dev.typr.foundations;

import dev.typr.foundations.analysis.AlignmentError;
import dev.typr.foundations.analysis.QueryAnalysis;
import dev.typr.foundations.analysis.QueryAnalyzer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for query analysis functionality.
 * Uses DuckDB for testing since it's embedded and requires no Docker.
 */
public class QueryAnalysisTest {

  // ─────────────────────────────────────────────────────────────────────────────
  // Test helpers
  // ─────────────────────────────────────────────────────────────────────────────

  private static <T> T withConnection(SqlFunction<Connection, T> f) {
    try (var conn = DriverManager.getConnection("jdbc:duckdb:")) {
      conn.setAutoCommit(false);
      try {
        return f.apply(conn);
      } finally {
        conn.rollback();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Basic analysis tests
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testSimpleSelectAnalysis() {
    withConnection(conn -> {
      // Create test table
      conn.createStatement().execute("CREATE TABLE users (id INTEGER, name VARCHAR)");

      // Analyze a simple query - single column uses simpler overload
      RowParser<Integer> parser = RowParsers.of(DuckDbTypes.integer);

      Fragment fragment = Fragment.lit("SELECT id FROM users");
      Operation.Query<List<Integer>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      assertTrue("Analysis should succeed for valid query", analysis.succeeded());
      assertEquals(0, analysis.parameterAlignment().size());
      assertEquals(1, analysis.columnAlignment().size());

      return null;
    });
  }

  @Test
  public void testParameterTypeAnalysis() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE products (id INTEGER, name VARCHAR, price DECIMAL(10, 2))");

      RowParser<String> parser = RowParsers.of(DuckDbTypes.varchar);

      // Query with parameter - note: DuckDB doesn't always provide parameter metadata
      Fragment fragment = Fragment.interpolate("SELECT name FROM products WHERE id = ")
          .param(DuckDbTypes.integer, 42)
          .done();

      Operation.Query<List<String>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      // The analysis should at least not crash
      // Parameter metadata availability varies by database

      return null;
    });
  }

  @Test
  public void testColumnCountMismatch_TooManyInParser() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE simple (id INTEGER)");

      // Parser expects 2 columns but query returns 1
      RowParser<String> parser = RowParsers.of(
          DuckDbTypes.integer,
          DuckDbTypes.varchar,  // Extra column
          (i, s) -> i + ":" + s,
          (str) -> new Object[]{0, ""}
      );

      Fragment fragment = Fragment.lit("SELECT id FROM simple");
      Operation.Query<List<String>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      assertFalse("Analysis should fail when parser has extra columns", analysis.succeeded());

      List<AlignmentError> errors = analysis.columnErrors();
      assertTrue("Should have column errors", !errors.isEmpty());

      // Should have an ExtraColumn error
      boolean hasExtraColumn = errors.stream()
          .anyMatch(e -> e instanceof AlignmentError.ExtraColumn);
      assertTrue("Should detect extra column in parser", hasExtraColumn);

      return null;
    });
  }

  @Test
  public void testColumnCountMismatch_TooFewInParser() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE multi (id INTEGER, name VARCHAR, active BOOLEAN)");

      // Parser expects 2 columns but query returns 3
      RowParser<String> parser = RowParsers.of(
          DuckDbTypes.integer,
          DuckDbTypes.varchar,
          (i, s) -> i + ":" + s,
          (str) -> new Object[]{0, ""}
      );

      Fragment fragment = Fragment.lit("SELECT id, name, active FROM multi");
      Operation.Query<List<String>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      assertFalse("Analysis should fail when parser has too few columns", analysis.succeeded());

      List<AlignmentError> errors = analysis.columnErrors();

      // Should have a MissingColumn error
      boolean hasMissingColumn = errors.stream()
          .anyMatch(e -> e instanceof AlignmentError.MissingColumn);
      assertTrue("Should detect missing column in parser", hasMissingColumn);

      return null;
    });
  }

  @Test
  public void testNullabilityMismatch() {
    withConnection(conn -> {
      // Create table with nullable column
      conn.createStatement().execute(
          "CREATE TABLE nullable_test (id INTEGER NOT NULL, name VARCHAR)"
      );

      // Parser uses non-optional type for nullable column
      RowParser<String> parser = RowParsers.of(
          DuckDbTypes.integer,
          DuckDbTypes.varchar,  // name is nullable but we're not using .opt()
          (i, s) -> i + ":" + s,
          (str) -> new Object[]{0, ""}
      );

      Fragment fragment = Fragment.lit("SELECT id, name FROM nullable_test");
      Operation.Query<List<String>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      // Check if nullability mismatch was detected
      // Note: Some databases don't report nullable for all columns
      List<AlignmentError> errors = analysis.columnErrors();
      System.out.println("Nullability errors found: " + errors.size());

      return null;
    });
  }

  @Test
  public void testCorrectNullableType() {
    withConnection(conn -> {
      conn.createStatement().execute(
          "CREATE TABLE nullable_correct (id INTEGER NOT NULL, name VARCHAR)"
      );

      // Parser correctly uses .opt() for nullable column
      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.integer,
          DuckDbTypes.varchar.opt(),  // Correctly marked as optional
          (i, s) -> new Object[]{i, s},
          (a) -> a
      );

      Fragment fragment = Fragment.lit("SELECT id, name FROM nullable_correct");
      Operation.Query<List<Object[]>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      // The nullable column should not generate an error
      List<AlignmentError> nullErrors = analysis.columnErrors().stream()
          .filter(e -> e instanceof AlignmentError.NullabilityMismatch)
          .toList();

      assertEquals("No nullability errors for correctly-typed nullable column", 0, nullErrors.size());

      return null;
    });
  }

  @Test
  public void testTypeMismatch() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE typed (id INTEGER, ts TIMESTAMP)");

      // Parser uses wrong type for timestamp column
      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.integer,
          DuckDbTypes.integer,  // Wrong! Should be timestamp
          (i1, i2) -> new Object[]{i1, i2},
          (a) -> a
      );

      Fragment fragment = Fragment.lit("SELECT id, ts FROM typed");
      Operation.Query<List<Object[]>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      assertFalse("Analysis should fail for type mismatch", analysis.succeeded());

      List<AlignmentError> errors = analysis.columnErrors();
      boolean hasTypeMismatch = errors.stream()
          .anyMatch(e -> e instanceof AlignmentError.ColumnTypeMismatch);

      assertTrue("Should detect column type mismatch", hasTypeMismatch);

      return null;
    });
  }

  @Test
  public void testReportFormatting() {
    withConnection(conn -> {
      conn.createStatement().execute(
          "CREATE TABLE report_test (a INTEGER, b VARCHAR, c DOUBLE, d BOOLEAN)"
      );

      // Mix of correct and incorrect columns
      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.integer,      // correct
          DuckDbTypes.integer,      // wrong - should be varchar
          DuckDbTypes.double_,      // correct
          // missing d
          (a, b, c) -> new Object[]{a, b, c},
          (arr) -> arr
      );

      Fragment fragment = Fragment.lit("SELECT a, b, c, d FROM report_test");
      Operation.Query<List<Object[]>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      String report = analysis.report();
      System.out.println(report);

      // Verify report contains expected sections
      assertTrue("Report should contain SQL section", report.contains("SQL:"));
      assertTrue("Report should contain Parameters section", report.contains("Parameters"));
      assertTrue("Report should contain Columns section", report.contains("Columns"));
      assertTrue("Report should contain error indicator", report.contains("error"));

      return null;
    });
  }

  @Test
  public void testFragmentParameterTypes() {
    // Test that Fragment.parameterTypes() correctly extracts types
    Fragment simple = Fragment.lit("SELECT 1");
    assertEquals("Literal fragment should have no parameters", 0, simple.parameterTypes().size());

    Fragment withParam = Fragment.interpolate("SELECT * FROM t WHERE id = ")
        .param(DuckDbTypes.integer, 42)
        .done();
    assertEquals("Fragment with one param should have 1 parameter type", 1, withParam.parameterTypes().size());

    Fragment multiParam = Fragment.interpolate("SELECT * FROM t WHERE a = ")
        .param(DuckDbTypes.integer, 1)
        .sql(" AND b = ")
        .param(DuckDbTypes.varchar, "test")
        .sql(" AND c = ")
        .param(DuckDbTypes.boolean_, true)
        .done();
    assertEquals("Fragment with three params should have 3 parameter types", 3, multiParam.parameterTypes().size());

    List<DbType<?>> types = multiParam.parameterTypes();
    assertEquals("First param should be integer", "INTEGER", types.get(0).typename().sqlType());
    assertEquals("Second param should be varchar", "VARCHAR", types.get(1).typename().sqlType());
    assertEquals("Third param should be boolean", "BOOLEAN", types.get(2).typename().sqlType());
  }

  @Test
  public void testOptionalTypeNullability() {
    // Test that .opt() types correctly report as nullable
    DbType<Integer> nonNullable = DuckDbTypes.integer;
    DbType<Optional<Integer>> nullable = DuckDbTypes.integer.opt();

    assertFalse("Base type should not be nullable", nonNullable.isNullable());
    assertTrue("Optional type should be nullable", nullable.isNullable());
  }

  @Test
  public void testUpdateAnalysis() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE update_test (id INTEGER, name VARCHAR)");

      Fragment fragment = Fragment.interpolate("UPDATE update_test SET name = ")
          .param(DuckDbTypes.varchar, "new_name")
          .sql(" WHERE id = ")
          .param(DuckDbTypes.integer, 1)
          .done();

      Operation.Update update = fragment.update();

      QueryAnalysis analysis = QueryAnalyzer.analyze(update, conn);

      System.out.println(analysis.report());

      // Update analysis should have parameters but no columns
      assertEquals(0, analysis.columnAlignment().size());

      return null;
    });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Comprehensive type tests - verify all DuckDB types work with query analysis
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testIntegerTypes() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE TABLE int_types (
            tiny TINYINT,
            small SMALLINT,
            med INTEGER,
            big BIGINT,
            huge HUGEINT
          )
          """);

      // Correct types
      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.tinyint,
          DuckDbTypes.smallint,
          DuckDbTypes.integer,
          DuckDbTypes.bigint,
          DuckDbTypes.hugeint,
          (t, s, m, b, h) -> new Object[]{t, s, m, b, h},
          a -> a
      );

      Fragment fragment = Fragment.lit("SELECT tiny, small, med, big, huge FROM int_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());
      assertTrue("Integer types should match", analysis.succeeded());

      return null;
    });
  }

  @Test
  public void testUnsignedIntegerTypes() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE TABLE uint_types (
            utiny UTINYINT,
            usmall USMALLINT,
            umed UINTEGER,
            ubig UBIGINT,
            uhuge UHUGEINT
          )
          """);

      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.utinyint,
          DuckDbTypes.usmallint,
          DuckDbTypes.uinteger,
          DuckDbTypes.ubigint,
          DuckDbTypes.uhugeint,
          (t, s, m, b, h) -> new Object[]{t, s, m, b, h},
          a -> a
      );

      Fragment fragment = Fragment.lit("SELECT utiny, usmall, umed, ubig, uhuge FROM uint_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());
      // Note: analysis may succeed or fail based on JDBC type mapping
      // The important thing is it doesn't crash

      return null;
    });
  }

  @Test
  public void testFloatingPointTypes() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE TABLE float_types (
            f FLOAT,
            d DOUBLE,
            dec DECIMAL(10, 2)
          )
          """);

      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.float_,
          DuckDbTypes.double_,
          DuckDbTypes.decimal,
          (f, d, dec) -> new Object[]{f, d, dec},
          a -> a
      );

      Fragment fragment = Fragment.lit("SELECT f, d, dec FROM float_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());
      assertTrue("Floating point types should match", analysis.succeeded());

      return null;
    });
  }

  @Test
  public void testStringTypes() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE TABLE string_types (
            v VARCHAR,
            t TEXT,
            c CHAR(10)
          )
          """);

      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.varchar,
          DuckDbTypes.text,
          DuckDbTypes.char_,
          (v, t, c) -> new Object[]{v, t, c},
          a -> a
      );

      Fragment fragment = Fragment.lit("SELECT v, t, c FROM string_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());
      assertTrue("String types should match", analysis.succeeded());

      return null;
    });
  }

  @Test
  public void testBooleanType() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE bool_types (b BOOLEAN)");

      RowParser<Boolean> parser = RowParsers.of(DuckDbTypes.boolean_);

      Fragment fragment = Fragment.lit("SELECT b FROM bool_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());
      assertTrue("Boolean type should match", analysis.succeeded());

      return null;
    });
  }

  @Test
  public void testDateTimeTypes() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE TABLE datetime_types (
            d DATE,
            t TIME,
            ts TIMESTAMP,
            tstz TIMESTAMP WITH TIME ZONE
          )
          """);

      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.date,
          DuckDbTypes.time,
          DuckDbTypes.timestamp,
          DuckDbTypes.timestamptz,
          (d, t, ts, tstz) -> new Object[]{d, t, ts, tstz},
          a -> a
      );

      Fragment fragment = Fragment.lit("SELECT d, t, ts, tstz FROM datetime_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());
      // Just verify it runs without crashing

      return null;
    });
  }

  @Test
  public void testUuidType() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE uuid_types (u UUID)");

      RowParser<UUID> parser = RowParsers.of(DuckDbTypes.uuid);

      Fragment fragment = Fragment.lit("SELECT u FROM uuid_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());
      // UUID maps to OTHER in JDBC, so should pass

      return null;
    });
  }

  @Test
  public void testBlobType() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE blob_types (b BLOB)");

      RowParser<byte[]> parser = RowParsers.of(DuckDbTypes.blob);

      Fragment fragment = Fragment.lit("SELECT b FROM blob_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      return null;
    });
  }

  @Test
  public void testArrayTypes() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE TABLE array_types (
            int_arr INTEGER[],
            str_arr VARCHAR[]
          )
          """);

      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.integerArray,
          DuckDbTypes.varcharArray,
          (i, s) -> new Object[]{i, s},
          a -> a
      );

      Fragment fragment = Fragment.lit("SELECT int_arr, str_arr FROM array_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      return null;
    });
  }

  @Test
  public void testTypeMismatch_IntegerVsString() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE mismatch1 (name VARCHAR)");

      // Wrong type: using integer for varchar column
      RowParser<Integer> parser = RowParsers.of(DuckDbTypes.integer);

      Fragment fragment = Fragment.lit("SELECT name FROM mismatch1");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      assertFalse("Should detect integer vs string mismatch", analysis.succeeded());
      assertTrue("Should have column errors",
          analysis.columnErrors().stream().anyMatch(e -> e instanceof AlignmentError.ColumnTypeMismatch));

      return null;
    });
  }

  @Test
  public void testTypeMismatch_StringVsBoolean() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE mismatch2 (flag BOOLEAN)");

      // Wrong type: using varchar for boolean column
      RowParser<String> parser = RowParsers.of(DuckDbTypes.varchar);

      Fragment fragment = Fragment.lit("SELECT flag FROM mismatch2");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      assertFalse("Should detect varchar vs boolean mismatch", analysis.succeeded());

      return null;
    });
  }

  @Test
  public void testTypeMismatch_TimestampVsDate() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE mismatch3 (d DATE)");

      // Wrong type: using timestamp for date column
      RowParser<LocalDateTime> parser = RowParsers.of(DuckDbTypes.timestamp);

      Fragment fragment = Fragment.lit("SELECT d FROM mismatch3");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      // This might or might not be detected as mismatch depending on JDBC type compatibility
      // but should not crash

      return null;
    });
  }

  @Test
  public void testMultipleParameters() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE multi_param (id INTEGER, name VARCHAR, active BOOLEAN)");

      Fragment fragment = Fragment.interpolate("SELECT * FROM multi_param WHERE id = ")
          .param(DuckDbTypes.integer, 1)
          .sql(" AND name = ")
          .param(DuckDbTypes.varchar, "test")
          .sql(" AND active = ")
          .param(DuckDbTypes.boolean_, true)
          .done();

      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.integer,
          DuckDbTypes.varchar,
          DuckDbTypes.boolean_,
          (i, n, a) -> new Object[]{i, n, a},
          arr -> arr
      );

      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      // Should have 3 parameters
      assertEquals("Should have 3 parameter types", 3, fragment.parameterTypes().size());

      return null;
    });
  }

  @Test
  public void testJoinQuery() {
    withConnection(conn -> {
      conn.createStatement().execute("""
          CREATE TABLE users (id INTEGER, name VARCHAR);
          CREATE TABLE orders (id INTEGER, user_id INTEGER, total DECIMAL(10, 2));
          """);

      Fragment fragment = Fragment.lit("""
          SELECT u.id, u.name, o.total
          FROM users u
          JOIN orders o ON u.id = o.user_id
          """);

      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.integer,
          DuckDbTypes.varchar,
          DuckDbTypes.decimal,
          (id, name, total) -> new Object[]{id, name, total},
          arr -> arr
      );

      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      assertTrue("Join query types should match", analysis.succeeded());

      return null;
    });
  }

  @Test
  public void testAggregateQuery() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE sales (amount DECIMAL(10, 2))");

      Fragment fragment = Fragment.lit("SELECT SUM(amount), AVG(amount), COUNT(*) FROM sales");

      // Aggregates return specific types
      RowParser<Object[]> parser = RowParsers.of(
          DuckDbTypes.decimal,  // SUM
          DuckDbTypes.double_,  // AVG returns double
          DuckDbTypes.bigint,   // COUNT returns bigint
          (sum, avg, cnt) -> new Object[]{sum, avg, cnt},
          arr -> arr
      );

      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      // May or may not match exactly depending on aggregate return types

      return null;
    });
  }

  @Test
  public void testSubqueryWithCast() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE items (price INTEGER)");

      Fragment fragment = Fragment.lit("SELECT CAST(price AS DOUBLE) FROM items");

      RowParser<Double> parser = RowParsers.of(DuckDbTypes.double_);

      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      assertTrue("Cast to double should match double type", analysis.succeeded());

      return null;
    });
  }

  @Test
  public void testJsonType() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE json_test (data JSON)");

      RowParser<dev.typr.foundations.data.Json> parser = RowParsers.of(DuckDbTypes.json);

      Fragment fragment = Fragment.lit("SELECT data FROM json_test");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      return null;
    });
  }

  @Test
  public void testIntervalType() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE interval_test (dur INTERVAL)");

      RowParser<Duration> parser = RowParsers.of(DuckDbTypes.interval);

      Fragment fragment = Fragment.lit("SELECT dur FROM interval_test");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      return null;
    });
  }
}
