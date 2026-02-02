package dev.typr.foundations;

import dev.typr.foundations.analysis.AlignmentError;
import dev.typr.foundations.analysis.QueryAnalysis;
import dev.typr.foundations.analysis.QueryAnalyzer;
import dev.typr.foundations.data.Uint1;
import dev.typr.foundations.data.Uint2;
import dev.typr.foundations.data.Uint4;
import dev.typr.foundations.data.Uint8;
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
  // Row types for tests
  // ─────────────────────────────────────────────────────────────────────────────

  record IntStr(Integer i, String s) {}
  record IntOptStr(Integer i, Optional<String> s) {}
  record IntInt(Integer i1, Integer i2) {}
  record IntStrDouble(Integer i, String s, Double d) {}
  record IntStrBool(Integer i, String s, Boolean b) {}
  record StrStrStr(String a, String b, String c) {}
  record IntTypes(Byte t, Short s, Integer m, Long b, BigInteger h) {}
  record UIntTypes(Uint1 t, Uint2 s, Uint4 m, Uint8 b, BigInteger h) {}
  record FloatTypes(Float f, Double d, BigDecimal dec) {}
  record DateTimeTypes(LocalDate d, LocalTime t, LocalDateTime ts, OffsetDateTime tstz) {}
  record ArrayTypes(Integer[] ints, String[] strs) {}
  record DecDoubleInt(BigDecimal sum, Double avg, Long cnt) {}

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
      conn.createStatement().execute("CREATE TABLE users (id INTEGER, name VARCHAR)");

      RowParser<Integer> parser = RowParser.of(DuckDbTypes.integer);

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

      RowParser<String> parser = RowParser.of(DuckDbTypes.varchar);

      Fragment fragment = Fragment.interpolate("SELECT name FROM products WHERE id = ")
          .param(DuckDbTypes.integer, 42)
          .done();

      Operation.Query<List<String>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      return null;
    });
  }

  @Test
  public void testColumnCountMismatch_TooManyInParser() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE simple (id INTEGER)");

      RowParser<IntStr> parser = RowParser.<IntStr>builder()
          .field(DuckDbTypes.integer, IntStr::i)
          .field(DuckDbTypes.varchar, IntStr::s)
          .build(IntStr::new);

      Fragment fragment = Fragment.lit("SELECT id FROM simple");
      Operation.Query<List<IntStr>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      assertFalse("Analysis should fail when parser has extra columns", analysis.succeeded());

      List<AlignmentError> errors = analysis.columnErrors();
      assertTrue("Should have column errors", !errors.isEmpty());

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

      RowParser<IntStr> parser = RowParser.<IntStr>builder()
          .field(DuckDbTypes.integer, IntStr::i)
          .field(DuckDbTypes.varchar, IntStr::s)
          .build(IntStr::new);

      Fragment fragment = Fragment.lit("SELECT id, name, active FROM multi");
      Operation.Query<List<IntStr>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

      assertFalse("Analysis should fail when parser has too few columns", analysis.succeeded());

      List<AlignmentError> errors = analysis.columnErrors();

      boolean hasMissingColumn = errors.stream()
          .anyMatch(e -> e instanceof AlignmentError.MissingColumn);
      assertTrue("Should detect missing column in parser", hasMissingColumn);

      return null;
    });
  }

  @Test
  public void testNullabilityMismatch() {
    withConnection(conn -> {
      conn.createStatement().execute(
          "CREATE TABLE nullable_test (id INTEGER NOT NULL, name VARCHAR)"
      );

      RowParser<IntStr> parser = RowParser.<IntStr>builder()
          .field(DuckDbTypes.integer, IntStr::i)
          .field(DuckDbTypes.varchar, IntStr::s)
          .build(IntStr::new);

      Fragment fragment = Fragment.lit("SELECT id, name FROM nullable_test");
      Operation.Query<List<IntStr>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

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

      RowParser<IntOptStr> parser = RowParser.<IntOptStr>builder()
          .field(DuckDbTypes.integer, IntOptStr::i)
          .field(DuckDbTypes.varchar.opt(), IntOptStr::s)
          .build(IntOptStr::new);

      Fragment fragment = Fragment.lit("SELECT id, name FROM nullable_correct");
      Operation.Query<List<IntOptStr>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      System.out.println(analysis.report());

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

      RowParser<IntInt> parser = RowParser.<IntInt>builder()
          .field(DuckDbTypes.integer, IntInt::i1)
          .field(DuckDbTypes.integer, IntInt::i2)
          .build(IntInt::new);

      Fragment fragment = Fragment.lit("SELECT id, ts FROM typed");
      Operation.Query<List<IntInt>> query = fragment.query(parser.all());

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

      // Use a record with wrong types to test type mismatch detection
      record IntIntDouble(Integer a, Integer b, Double c) {}
      RowParser<IntIntDouble> parser = RowParser.<IntIntDouble>builder()
          .field(DuckDbTypes.integer, IntIntDouble::a)
          .field(DuckDbTypes.integer, IntIntDouble::b)  // wrong - should be varchar
          .field(DuckDbTypes.double_, IntIntDouble::c)
          // missing d column
          .build(IntIntDouble::new);

      Fragment fragment = Fragment.lit("SELECT a, b, c, d FROM report_test");
      Operation.Query<List<IntIntDouble>> query = fragment.query(parser.all());

      QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn);

      String report = analysis.report();
      System.out.println(report);

      assertTrue("Report should contain SQL section", report.contains("SQL:"));
      assertTrue("Report should contain Parameters section", report.contains("Parameters"));
      assertTrue("Report should contain Columns section", report.contains("Columns"));
      assertTrue("Report should contain error indicator", report.contains("error"));

      return null;
    });
  }

  @Test
  public void testFragmentParameterTypes() {
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

      assertEquals(0, analysis.columnAlignment().size());

      return null;
    });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Comprehensive type tests
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

      RowParser<IntTypes> parser = RowParser.<IntTypes>builder()
          .field(DuckDbTypes.tinyint, IntTypes::t)
          .field(DuckDbTypes.smallint, IntTypes::s)
          .field(DuckDbTypes.integer, IntTypes::m)
          .field(DuckDbTypes.bigint, IntTypes::b)
          .field(DuckDbTypes.hugeint, IntTypes::h)
          .build(IntTypes::new);

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

      RowParser<UIntTypes> parser = RowParser.<UIntTypes>builder()
          .field(DuckDbTypes.utinyint, UIntTypes::t)
          .field(DuckDbTypes.usmallint, UIntTypes::s)
          .field(DuckDbTypes.uinteger, UIntTypes::m)
          .field(DuckDbTypes.ubigint, UIntTypes::b)
          .field(DuckDbTypes.uhugeint, UIntTypes::h)
          .build(UIntTypes::new);

      Fragment fragment = Fragment.lit("SELECT utiny, usmall, umed, ubig, uhuge FROM uint_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

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

      RowParser<FloatTypes> parser = RowParser.<FloatTypes>builder()
          .field(DuckDbTypes.float_, FloatTypes::f)
          .field(DuckDbTypes.double_, FloatTypes::d)
          .field(DuckDbTypes.decimal, FloatTypes::dec)
          .build(FloatTypes::new);

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

      RowParser<StrStrStr> parser = RowParser.<StrStrStr>builder()
          .field(DuckDbTypes.varchar, StrStrStr::a)
          .field(DuckDbTypes.text, StrStrStr::b)
          .field(DuckDbTypes.char_, StrStrStr::c)
          .build(StrStrStr::new);

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

      RowParser<Boolean> parser = RowParser.of(DuckDbTypes.boolean_);

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

      RowParser<DateTimeTypes> parser = RowParser.<DateTimeTypes>builder()
          .field(DuckDbTypes.date, DateTimeTypes::d)
          .field(DuckDbTypes.time, DateTimeTypes::t)
          .field(DuckDbTypes.timestamp, DateTimeTypes::ts)
          .field(DuckDbTypes.timestamptz, DateTimeTypes::tstz)
          .build(DateTimeTypes::new);

      Fragment fragment = Fragment.lit("SELECT d, t, ts, tstz FROM datetime_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      return null;
    });
  }

  @Test
  public void testUuidType() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE uuid_types (u UUID)");

      RowParser<UUID> parser = RowParser.of(DuckDbTypes.uuid);

      Fragment fragment = Fragment.lit("SELECT u FROM uuid_types");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      return null;
    });
  }

  @Test
  public void testBlobType() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE blob_types (b BLOB)");

      RowParser<byte[]> parser = RowParser.of(DuckDbTypes.blob);

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

      RowParser<ArrayTypes> parser = RowParser.<ArrayTypes>builder()
          .field(DuckDbTypes.integerArray, ArrayTypes::ints)
          .field(DuckDbTypes.varcharArray, ArrayTypes::strs)
          .build(ArrayTypes::new);

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

      RowParser<Integer> parser = RowParser.of(DuckDbTypes.integer);

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

      RowParser<String> parser = RowParser.of(DuckDbTypes.varchar);

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

      RowParser<LocalDateTime> parser = RowParser.of(DuckDbTypes.timestamp);

      Fragment fragment = Fragment.lit("SELECT d FROM mismatch3");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

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

      RowParser<IntStrBool> parser = RowParser.<IntStrBool>builder()
          .field(DuckDbTypes.integer, IntStrBool::i)
          .field(DuckDbTypes.varchar, IntStrBool::s)
          .field(DuckDbTypes.boolean_, IntStrBool::b)
          .build(IntStrBool::new);

      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

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

      record JoinRow(Integer id, String name, BigDecimal total) {}
      RowParser<JoinRow> parser = RowParser.<JoinRow>builder()
          .field(DuckDbTypes.integer, JoinRow::id)
          .field(DuckDbTypes.varchar, JoinRow::name)
          .field(DuckDbTypes.decimal, JoinRow::total)
          .build(JoinRow::new);

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

      RowParser<DecDoubleInt> parser = RowParser.<DecDoubleInt>builder()
          .field(DuckDbTypes.decimal, DecDoubleInt::sum)
          .field(DuckDbTypes.double_, DecDoubleInt::avg)
          .field(DuckDbTypes.bigint, DecDoubleInt::cnt)
          .build(DecDoubleInt::new);

      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      return null;
    });
  }

  @Test
  public void testSubqueryWithCast() {
    withConnection(conn -> {
      conn.createStatement().execute("CREATE TABLE items (price INTEGER)");

      Fragment fragment = Fragment.lit("SELECT CAST(price AS DOUBLE) FROM items");

      RowParser<Double> parser = RowParser.of(DuckDbTypes.double_);

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

      RowParser<dev.typr.foundations.data.Json> parser = RowParser.of(DuckDbTypes.json);

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

      RowParser<Duration> parser = RowParser.of(DuckDbTypes.interval);

      Fragment fragment = Fragment.lit("SELECT dur FROM interval_test");
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn);

      System.out.println(analysis.report());

      return null;
    });
  }
}
