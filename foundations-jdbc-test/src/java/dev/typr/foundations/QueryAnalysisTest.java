package dev.typr.foundations;

import static org.junit.Assert.*;

import dev.typr.foundations.data.Uint1;
import dev.typr.foundations.data.Uint2;
import dev.typr.foundations.data.Uint4;
import dev.typr.foundations.data.Uint8;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ParameterMetaData;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;

/**
 * Tests for query analysis functionality. Uses DuckDB for testing since it's embedded and requires
 * no Docker.
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

  record ArrayTypes(List<Integer> ints, List<String> strs) {}

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
      throw new DatabaseException(e);
    }
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Basic analysis tests
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testSimpleSelectAnalysis() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE users (id INTEGER, name VARCHAR)");

          RowCodec<Integer> parser = RowCodec.of(DuckDbTypes.integer);

          Fragment fragment = Fragment.of("SELECT id FROM users");
          Operation.Query<List<Integer>> query = fragment.query(parser.all());

          QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn).getFirst();

          System.out.println(analysis.report());

          assertTrue("Analysis should succeed for valid query", analysis.succeeded());
          assertEquals(0, analysis.parameterAlignment().size());
          assertEquals(1, analysis.columnAlignment().size());

          return null;
        });
  }

  @Test
  public void testParameterTypeAnalysis() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TABLE products (id INTEGER, name VARCHAR, price DECIMAL(10, 2))");

          RowCodec<String> parser = RowCodec.of(DuckDbTypes.varchar);

          Fragment fragment =
              Fragment.of("SELECT name FROM products WHERE id = ").value(DuckDbTypes.integer, 42);

          Operation.Query<List<String>> query = fragment.query(parser.all());

          QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testColumnCountMismatch_TooManyInParser() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE simple (id INTEGER)");

          RowCodec<IntStr> parser =
              RowCodec.<IntStr>builder()
                  .field(DuckDbTypes.integer, IntStr::i)
                  .field(DuckDbTypes.varchar, IntStr::s)
                  .build(IntStr::new);

          Fragment fragment = Fragment.of("SELECT id FROM simple");
          Operation.Query<List<IntStr>> query = fragment.query(parser.all());

          QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn).getFirst();

          System.out.println(analysis.report());

          assertFalse("Analysis should fail when parser has extra columns", analysis.succeeded());

          List<AlignmentError> errors = analysis.columnErrors();
          assertTrue("Should have column errors", !errors.isEmpty());

          boolean hasExtraColumn =
              errors.stream().anyMatch(e -> e instanceof AlignmentError.ExtraColumn);
          assertTrue("Should detect extra column in parser", hasExtraColumn);

          return null;
        });
  }

  @Test
  public void testColumnCountMismatch_TooFewInParser() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TABLE multi (id INTEGER, name VARCHAR, active BOOLEAN)");

          RowCodec<IntStr> parser =
              RowCodec.<IntStr>builder()
                  .field(DuckDbTypes.integer, IntStr::i)
                  .field(DuckDbTypes.varchar, IntStr::s)
                  .build(IntStr::new);

          Fragment fragment = Fragment.of("SELECT id, name, active FROM multi");
          Operation.Query<List<IntStr>> query = fragment.query(parser.all());

          QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn).getFirst();

          System.out.println(analysis.report());

          assertFalse("Analysis should fail when parser has too few columns", analysis.succeeded());

          List<AlignmentError> errors = analysis.columnErrors();

          boolean hasMissingColumn =
              errors.stream().anyMatch(e -> e instanceof AlignmentError.MissingColumn);
          assertTrue("Should detect missing column in parser", hasMissingColumn);

          return null;
        });
  }

  @Test
  public void testNullabilityMismatch() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TABLE nullable_test (id INTEGER NOT NULL, name VARCHAR)");

          RowCodec<IntStr> parser =
              RowCodec.<IntStr>builder()
                  .field(DuckDbTypes.integer, IntStr::i)
                  .field(DuckDbTypes.varchar, IntStr::s)
                  .build(IntStr::new);

          Fragment fragment = Fragment.of("SELECT id, name FROM nullable_test");
          Operation.Query<List<IntStr>> query = fragment.query(parser.all());

          QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn).getFirst();

          System.out.println(analysis.report());

          List<AlignmentError> errors = analysis.columnErrors();
          System.out.println("Nullability errors found: " + errors.size());

          return null;
        });
  }

  @Test
  public void testCorrectNullableType() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TABLE nullable_correct (id INTEGER NOT NULL, name VARCHAR)");

          RowCodec<IntOptStr> parser =
              RowCodec.<IntOptStr>builder()
                  .field(DuckDbTypes.integer, IntOptStr::i)
                  .field(DuckDbTypes.varchar.opt(), IntOptStr::s)
                  .build(IntOptStr::new);

          Fragment fragment = Fragment.of("SELECT id, name FROM nullable_correct");
          Operation.Query<List<IntOptStr>> query = fragment.query(parser.all());

          QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn).getFirst();

          System.out.println(analysis.report());

          List<AlignmentError> nullErrors =
              analysis.columnErrors().stream()
                  .filter(e -> e instanceof AlignmentError.NullabilityMismatch)
                  .toList();

          assertEquals(
              "No nullability errors for correctly-typed nullable column", 0, nullErrors.size());

          return null;
        });
  }

  @Test
  public void testTypeMismatch() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE typed (id INTEGER, ts TIMESTAMP)");

          RowCodec<IntInt> parser =
              RowCodec.<IntInt>builder()
                  .field(DuckDbTypes.integer, IntInt::i1)
                  .field(DuckDbTypes.integer, IntInt::i2)
                  .build(IntInt::new);

          Fragment fragment = Fragment.of("SELECT id, ts FROM typed");
          Operation.Query<List<IntInt>> query = fragment.query(parser.all());

          QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn).getFirst();

          System.out.println(analysis.report());

          assertFalse("Analysis should fail for type mismatch", analysis.succeeded());

          List<AlignmentError> errors = analysis.columnErrors();
          boolean hasTypeMismatch =
              errors.stream().anyMatch(e -> e instanceof AlignmentError.ColumnTypeMismatch);

          assertTrue("Should detect column type mismatch", hasTypeMismatch);

          return null;
        });
  }

  @Test
  public void testReportFormatting() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TABLE report_test (a INTEGER, b VARCHAR, c DOUBLE, d BOOLEAN)");

          // Use a record with wrong types to test type mismatch detection
          record IntIntDouble(Integer a, Integer b, Double c) {}
          RowCodec<IntIntDouble> parser =
              RowCodec.<IntIntDouble>builder()
                  .field(DuckDbTypes.integer, IntIntDouble::a)
                  .field(DuckDbTypes.integer, IntIntDouble::b) // wrong - should be varchar
                  .field(DuckDbTypes.double_, IntIntDouble::c)
                  // missing d column
                  .build(IntIntDouble::new);

          Fragment fragment = Fragment.of("SELECT a, b, c, d FROM report_test");
          Operation.Query<List<IntIntDouble>> query = fragment.query(parser.all());

          QueryAnalysis analysis = QueryAnalyzer.analyze(query, conn).getFirst();

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
    Fragment simple = Fragment.of("SELECT 1");
    assertEquals("Literal fragment should have no parameters", 0, simple.parameterTypes().size());

    Fragment withParam = Fragment.of("SELECT * FROM t WHERE id = ").value(DuckDbTypes.integer, 42);
    assertEquals(
        "Fragment with one param should have 1 parameter type",
        1,
        withParam.parameterTypes().size());

    Fragment multiParam =
        Fragment.of("SELECT * FROM t WHERE a = ")
            .value(DuckDbTypes.integer, 1)
            .append(" AND b = ")
            .value(DuckDbTypes.varchar, "test")
            .append(" AND c = ")
            .value(DuckDbTypes.boolean_, true);
    assertEquals(
        "Fragment with three params should have 3 parameter types",
        3,
        multiParam.parameterTypes().size());

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
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE update_test (id INTEGER, name VARCHAR)");

          Fragment fragment =
              Fragment.of("UPDATE update_test SET name = ")
                  .value(DuckDbTypes.varchar, "new_name")
                  .append(" WHERE id = ")
                  .value(DuckDbTypes.integer, 1);

          Operation.Update update = fragment.update();

          QueryAnalysis analysis = QueryAnalyzer.analyze(update, conn).getFirst();

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
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE int_types (
                    tiny TINYINT,
                    small SMALLINT,
                    med INTEGER,
                    big BIGINT,
                    huge HUGEINT
                  )
                  """);

          RowCodec<IntTypes> parser =
              RowCodec.<IntTypes>builder()
                  .field(DuckDbTypes.tinyint, IntTypes::t)
                  .field(DuckDbTypes.smallint, IntTypes::s)
                  .field(DuckDbTypes.integer, IntTypes::m)
                  .field(DuckDbTypes.bigint, IntTypes::b)
                  .field(DuckDbTypes.hugeint, IntTypes::h)
                  .build(IntTypes::new);

          Fragment fragment = Fragment.of("SELECT tiny, small, med, big, huge FROM int_types");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());
          assertTrue("Integer types should match", analysis.succeeded());

          return null;
        });
  }

  @Test
  public void testUnsignedIntegerTypes() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE uint_types (
                    utiny UTINYINT,
                    usmall USMALLINT,
                    umed UINTEGER,
                    ubig UBIGINT,
                    uhuge UHUGEINT
                  )
                  """);

          RowCodec<UIntTypes> parser =
              RowCodec.<UIntTypes>builder()
                  .field(DuckDbTypes.utinyint, UIntTypes::t)
                  .field(DuckDbTypes.usmallint, UIntTypes::s)
                  .field(DuckDbTypes.uinteger, UIntTypes::m)
                  .field(DuckDbTypes.ubigint, UIntTypes::b)
                  .field(DuckDbTypes.uhugeint, UIntTypes::h)
                  .build(UIntTypes::new);

          Fragment fragment =
              Fragment.of("SELECT utiny, usmall, umed, ubig, uhuge FROM uint_types");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testFloatingPointTypes() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE float_types (
                    f FLOAT,
                    d DOUBLE,
                    dec DECIMAL(10, 2)
                  )
                  """);

          RowCodec<FloatTypes> parser =
              RowCodec.<FloatTypes>builder()
                  .field(DuckDbTypes.float_, FloatTypes::f)
                  .field(DuckDbTypes.double_, FloatTypes::d)
                  .field(DuckDbTypes.decimal, FloatTypes::dec)
                  .build(FloatTypes::new);

          Fragment fragment = Fragment.of("SELECT f, d, dec FROM float_types");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());
          assertTrue("Floating point types should match", analysis.succeeded());

          return null;
        });
  }

  @Test
  public void testStringTypes() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE string_types (
                    v VARCHAR,
                    t TEXT,
                    c CHAR(10)
                  )
                  """);

          RowCodec<StrStrStr> parser =
              RowCodec.<StrStrStr>builder()
                  .field(DuckDbTypes.varchar, StrStrStr::a)
                  .field(DuckDbTypes.text, StrStrStr::b)
                  .field(DuckDbTypes.char_, StrStrStr::c)
                  .build(StrStrStr::new);

          Fragment fragment = Fragment.of("SELECT v, t, c FROM string_types");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());
          assertTrue("String types should match", analysis.succeeded());

          return null;
        });
  }

  @Test
  public void testBooleanType() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE bool_types (b BOOLEAN)");

          RowCodec<Boolean> parser = RowCodec.of(DuckDbTypes.boolean_);

          Fragment fragment = Fragment.of("SELECT b FROM bool_types");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());
          assertTrue("Boolean type should match", analysis.succeeded());

          return null;
        });
  }

  @Test
  public void testDateTimeTypes() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE datetime_types (
                    d DATE,
                    t TIME,
                    ts TIMESTAMP,
                    tstz TIMESTAMP WITH TIME ZONE
                  )
                  """);

          RowCodec<DateTimeTypes> parser =
              RowCodec.<DateTimeTypes>builder()
                  .field(DuckDbTypes.date, DateTimeTypes::d)
                  .field(DuckDbTypes.time, DateTimeTypes::t)
                  .field(DuckDbTypes.timestamp, DateTimeTypes::ts)
                  .field(DuckDbTypes.timestamptz, DateTimeTypes::tstz)
                  .build(DateTimeTypes::new);

          Fragment fragment = Fragment.of("SELECT d, t, ts, tstz FROM datetime_types");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testUuidType() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE uuid_types (u UUID)");

          RowCodec<UUID> parser = RowCodec.of(DuckDbTypes.uuid);

          Fragment fragment = Fragment.of("SELECT u FROM uuid_types");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testBlobType() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE blob_types (b BLOB)");

          RowCodec<byte[]> parser = RowCodec.of(DuckDbTypes.blob);

          Fragment fragment = Fragment.of("SELECT b FROM blob_types");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testArrayTypes() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE array_types (
                    int_arr INTEGER[],
                    str_arr VARCHAR[]
                  )
                  """);

          RowCodec<ArrayTypes> parser =
              RowCodec.<ArrayTypes>builder()
                  .field(DuckDbTypes.integer.list(), ArrayTypes::ints)
                  .field(DuckDbTypes.varchar.list(), ArrayTypes::strs)
                  .build(ArrayTypes::new);

          Fragment fragment = Fragment.of("SELECT int_arr, str_arr FROM array_types");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testTypeMismatch_IntegerVsString() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE mismatch1 (name VARCHAR)");

          RowCodec<Integer> parser = RowCodec.of(DuckDbTypes.integer);

          Fragment fragment = Fragment.of("SELECT name FROM mismatch1");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          assertFalse("Should detect integer vs string mismatch", analysis.succeeded());
          assertTrue(
              "Should have column errors",
              analysis.columnErrors().stream()
                  .anyMatch(e -> e instanceof AlignmentError.ColumnTypeMismatch));

          return null;
        });
  }

  @Test
  public void testTypeMismatch_StringVsBoolean() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE mismatch2 (flag BOOLEAN)");

          RowCodec<String> parser = RowCodec.of(DuckDbTypes.varchar);

          Fragment fragment = Fragment.of("SELECT flag FROM mismatch2");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          assertFalse("Should detect varchar vs boolean mismatch", analysis.succeeded());

          return null;
        });
  }

  @Test
  public void testTypeMismatch_TimestampVsDate() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE mismatch3 (d DATE)");

          RowCodec<LocalDateTime> parser = RowCodec.of(DuckDbTypes.timestamp);

          Fragment fragment = Fragment.of("SELECT d FROM mismatch3");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testMultipleParameters() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TABLE multi_param (id INTEGER, name VARCHAR, active BOOLEAN)");

          Fragment fragment =
              Fragment.of("SELECT * FROM multi_param WHERE id = ")
                  .value(DuckDbTypes.integer, 1)
                  .append(" AND name = ")
                  .value(DuckDbTypes.varchar, "test")
                  .append(" AND active = ")
                  .value(DuckDbTypes.boolean_, true);

          RowCodec<IntStrBool> parser =
              RowCodec.<IntStrBool>builder()
                  .field(DuckDbTypes.integer, IntStrBool::i)
                  .field(DuckDbTypes.varchar, IntStrBool::s)
                  .field(DuckDbTypes.boolean_, IntStrBool::b)
                  .build(IntStrBool::new);

          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          assertEquals("Should have 3 parameter types", 3, fragment.parameterTypes().size());

          return null;
        });
  }

  @Test
  public void testJoinQuery() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE users (id INTEGER, name VARCHAR);
                  CREATE TABLE orders (id INTEGER, user_id INTEGER, total DECIMAL(10, 2));
                  """);

          Fragment fragment =
              Fragment.of(
                  """
                  SELECT u.id, u.name, o.total
                  FROM users u
                  JOIN orders o ON u.id = o.user_id
                  """);

          record JoinRow(Integer id, String name, BigDecimal total) {}
          RowCodec<JoinRow> parser =
              RowCodec.<JoinRow>builder()
                  .field(DuckDbTypes.integer, JoinRow::id)
                  .field(DuckDbTypes.varchar, JoinRow::name)
                  .field(DuckDbTypes.decimal, JoinRow::total)
                  .build(JoinRow::new);

          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          assertTrue("Join query types should match", analysis.succeeded());

          return null;
        });
  }

  @Test
  public void testAggregateQuery() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE sales (amount DECIMAL(10, 2))");

          Fragment fragment = Fragment.of("SELECT SUM(amount), AVG(amount), COUNT(*) FROM sales");

          RowCodec<DecDoubleInt> parser =
              RowCodec.<DecDoubleInt>builder()
                  .field(DuckDbTypes.decimal, DecDoubleInt::sum)
                  .field(DuckDbTypes.double_, DecDoubleInt::avg)
                  .field(DuckDbTypes.bigint, DecDoubleInt::cnt)
                  .build(DecDoubleInt::new);

          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testSubqueryWithCast() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE items (price INTEGER)");

          Fragment fragment = Fragment.of("SELECT CAST(price AS DOUBLE) FROM items");

          RowCodec<Double> parser = RowCodec.of(DuckDbTypes.double_);

          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          assertTrue("Cast to double should match double type", analysis.succeeded());

          return null;
        });
  }

  @Test
  public void testJsonType() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE json_test (data JSON)");

          RowCodec<dev.typr.foundations.data.Json> parser = RowCodec.of(DuckDbTypes.json);

          Fragment fragment = Fragment.of("SELECT data FROM json_test");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testIntervalType() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE interval_test (dur INTERVAL)");

          RowCodec<Duration> parser = RowCodec.of(DuckDbTypes.interval);

          Fragment fragment = Fragment.of("SELECT dur FROM interval_test");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  @Test
  public void testReportColoredVsPlain() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE color_test (id INTEGER, name VARCHAR)");

          // Intentionally use wrong type to generate errors
          RowCodec<IntInt> parser =
              RowCodec.<IntInt>builder()
                  .field(DuckDbTypes.integer, IntInt::i1)
                  .field(DuckDbTypes.integer, IntInt::i2) // Wrong type
                  .build(IntInt::new);

          Fragment fragment = Fragment.of("SELECT id, name FROM color_test");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          // Get both versions
          String plain = analysis.report();
          String colored = analysis.reportColored();

          System.out.println("=== Plain report ===");
          System.out.println(plain);
          System.out.println("=== Colored report ===");
          System.out.println(colored);

          // Colored version should contain ANSI escape codes
          assertTrue(
              "Colored report should contain ANSI escape codes", colored.contains("\u001b["));
          assertFalse(
              "Plain report should NOT contain ANSI escape codes", plain.contains("\u001b["));

          // Both should contain the same structural elements
          assertTrue("Plain should contain error count", plain.contains("error"));
          assertTrue("Colored should contain error count", colored.contains("error"));

          // Error messages should also have colored versions
          for (var error : analysis.allErrors()) {
            String plainMsg = error.message();
            String coloredMsg = error.styledMessage().render();

            assertTrue("Colored message should contain ANSI codes", coloredMsg.contains("\u001b["));
            assertFalse(
                "Plain message should NOT contain ANSI codes", plainMsg.contains("\u001b["));
          }

          return null;
        });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // LEFT JOIN nullability tests
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testLeftJoinNullability() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE lj_users (id INTEGER NOT NULL, name VARCHAR NOT NULL);
                  CREATE TABLE lj_orders (id INTEGER NOT NULL, user_id INTEGER NOT NULL, total DECIMAL(10, 2) NOT NULL);
                  """);

          record JoinRow(Integer userId, String name, Optional<BigDecimal> total) {}
          RowCodec<JoinRow> parser =
              RowCodec.<JoinRow>builder()
                  .field(DuckDbTypes.integer, JoinRow::userId)
                  .field(DuckDbTypes.varchar, JoinRow::name)
                  .field(DuckDbTypes.decimal.opt(), JoinRow::total)
                  .build(JoinRow::new);

          Fragment fragment =
              Fragment.of(
                  """
                  SELECT u.id, u.name, o.total
                  FROM lj_users u
                  LEFT JOIN lj_orders o ON u.id = o.user_id
                  """);

          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());
          assertTrue("LEFT JOIN with opt() should succeed", analysis.succeeded());

          return null;
        });
  }

  @Test
  public void testLeftJoinNullabilityMismatch() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE ljm_users (id INTEGER NOT NULL, name VARCHAR NOT NULL);
                  CREATE TABLE ljm_orders (id INTEGER NOT NULL, user_id INTEGER NOT NULL, total DECIMAL(10, 2) NOT NULL);
                  """);

          record JoinRow(Integer userId, String name, BigDecimal total) {}
          RowCodec<JoinRow> parser =
              RowCodec.<JoinRow>builder()
                  .field(DuckDbTypes.integer, JoinRow::userId)
                  .field(DuckDbTypes.varchar, JoinRow::name)
                  .field(DuckDbTypes.decimal, JoinRow::total)
                  .build(JoinRow::new);

          Fragment fragment =
              Fragment.of(
                  """
                  SELECT u.id, u.name, o.total
                  FROM ljm_users u
                  LEFT JOIN ljm_orders o ON u.id = o.user_id
                  """);

          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          return null;
        });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // unchecked() escape hatch
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testUncheckedSkipsAnalysis() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE uc_test (name VARCHAR)");

          RowCodec<Integer> parser = RowCodec.of(DuckDbTypes.integer.unchecked());

          Fragment fragment = Fragment.of("SELECT name FROM uc_test");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          assertTrue("unchecked() should skip type checking", analysis.succeeded());

          return null;
        });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // nullableOk() escape hatch
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testNullableOkSkipsNullabilityCheck() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TABLE nok_test (id INTEGER NOT NULL, name VARCHAR)");

          record Row(Integer id, String name) {}
          RowCodec<Row> parser =
              RowCodec.<Row>builder()
                  .field(DuckDbTypes.integer, Row::id)
                  .field(DuckDbTypes.varchar.nullableOk(), Row::name)
                  .build(Row::new);

          Fragment fragment = Fragment.of("SELECT id, name FROM nok_test");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          List<AlignmentError> nullErrors =
              analysis.columnErrors().stream()
                  .filter(e -> e instanceof AlignmentError.NullabilityMismatch)
                  .toList();

          assertEquals("nullableOk() should suppress nullability errors", 0, nullErrors.size());

          return null;
        });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Named queries
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testNamedQuery() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE nq_test (id INTEGER, name VARCHAR)");

          RowCodec<IntStr> parser =
              RowCodec.<IntStr>builder()
                  .field(DuckDbTypes.integer, IntStr::i)
                  .field(DuckDbTypes.varchar, IntStr::s)
                  .build(IntStr::new);

          Fragment fragment = Fragment.of("SELECT id, name FROM nq_test");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()).named("findUsers"), conn)
                  .getFirst();

          System.out.println(analysis.report());

          assertTrue("Named query should succeed", analysis.succeeded());
          String report = analysis.report();
          assertTrue("Report should contain query name", report.contains("findUsers"));

          return null;
        });
  }

  @Test
  public void testNamedQueryWithError() {
    withConnection(
        conn -> {
          conn.createStatement().execute("CREATE TABLE nqe_test (name VARCHAR)");

          RowCodec<Integer> parser = RowCodec.of(DuckDbTypes.integer);

          Fragment fragment = Fragment.of("SELECT name FROM nqe_test");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()).named("getUserId"), conn)
                  .getFirst();

          System.out.println(analysis.report());

          assertFalse("Named query with type mismatch should fail", analysis.succeeded());
          String report = analysis.report();
          assertTrue("Error report should contain query name", report.contains("getUserId"));

          return null;
        });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // VIEW analysis
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testViewAnalysis() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute(
                  """
                  CREATE TABLE va_users (id INTEGER NOT NULL, name VARCHAR NOT NULL);
                  CREATE VIEW va_users_view AS SELECT id, name FROM va_users;
                  """);

          RowCodec<IntStr> parser =
              RowCodec.<IntStr>builder()
                  .field(DuckDbTypes.integer, IntStr::i)
                  .field(DuckDbTypes.varchar, IntStr::s)
                  .build(IntStr::new);

          Fragment fragment = Fragment.of("SELECT id, name FROM va_users_view");
          QueryAnalysis analysis =
              QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();

          System.out.println(analysis.report());

          assertTrue("View analysis should succeed", analysis.succeeded());

          return null;
        });
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Direct QueryAnalysis construction tests (no database connection needed)
  // Verifies how the analyzer handles unknown, null, and missing metadata.
  // ─────────────────────────────────────────────────────────────────────────────

  private static JdbcMeta.ParameterMeta paramMeta(int pos, String vendorTypeName) {
    return new JdbcMeta.ParameterMeta(
        pos, Types.OTHER, vendorTypeName, ParameterMetaData.parameterNullableUnknown);
  }

  private static JdbcMeta.ColumnMeta colMeta(
      int pos, String vendorTypeName, String name, int nullable) {
    return new JdbcMeta.ColumnMeta(pos, Types.OTHER, vendorTypeName, nullable, name, name);
  }

  @Test
  public void testParamNullVendorTypeName_NoError() {
    var analysis =
        new QueryAnalysis(
            "SELECT 1 WHERE ? IS NOT NULL",
            List.of(new Alignment.Both<>(DuckDbTypes.integer, paramMeta(1, null))),
            List.of(),
            true);
    assertTrue("Null vendor type name should be skipped (no error)", analysis.succeeded());
    assertEquals(0, analysis.parameterErrors().size());
  }

  @Test
  public void testParamEmptyVendorTypeName_NoError() {
    var analysis =
        new QueryAnalysis(
            "SELECT 1 WHERE ? IS NOT NULL",
            List.of(new Alignment.Both<>(DuckDbTypes.integer, paramMeta(1, ""))),
            List.of(),
            true);
    assertTrue("Empty vendor type name should be skipped (no error)", analysis.succeeded());
    assertEquals(0, analysis.parameterErrors().size());
  }

  @Test
  public void testParamUnknownVendorTypeName_NoError() {
    var analysis =
        new QueryAnalysis(
            "SELECT 1 WHERE ? IS NOT NULL",
            List.of(new Alignment.Both<>(DuckDbTypes.integer, paramMeta(1, "unknown"))),
            List.of(),
            true);
    assertTrue("'unknown' vendor type name should be skipped (no error)", analysis.succeeded());
    assertEquals(0, analysis.parameterErrors().size());
  }

  @Test
  public void testParamMatchingType_NoError() {
    var analysis =
        new QueryAnalysis(
            "SELECT 1 WHERE id = ?",
            List.of(new Alignment.Both<>(DuckDbTypes.integer, paramMeta(1, "INTEGER"))),
            List.of(),
            true);
    assertTrue("Matching type should produce no error", analysis.succeeded());
    assertEquals(0, analysis.parameterErrors().size());
  }

  @Test
  public void testParamMismatchingType_Error() {
    var analysis =
        new QueryAnalysis(
            "SELECT 1 WHERE id = ?",
            List.of(new Alignment.Both<>(DuckDbTypes.integer, paramMeta(1, "varchar"))),
            List.of(),
            true);
    assertFalse("Mismatching type should produce an error", analysis.succeeded());
    assertEquals(1, analysis.parameterErrors().size());
    assertTrue(
        "Should be ParameterTypeMismatch",
        analysis.parameterErrors().getFirst() instanceof AlignmentError.ParameterTypeMismatch);
  }

  @Test
  public void testParamUnchecked_NoErrorDespiteMismatch() {
    var analysis =
        new QueryAnalysis(
            "SELECT 1 WHERE id = ?",
            List.of(new Alignment.Both<>(DuckDbTypes.integer.unchecked(), paramMeta(1, "varchar"))),
            List.of(),
            true);
    assertTrue(
        "unchecked() should skip type checking even when types mismatch", analysis.succeeded());
    assertEquals(0, analysis.parameterErrors().size());
  }

  @Test
  public void testParamMetadataNotAvailable_AllErrorsSuppressed() {
    var analysis =
        new QueryAnalysis(
            "SELECT 1 WHERE id = ?",
            List.of(new Alignment.Both<>(DuckDbTypes.integer, paramMeta(1, "varchar"))),
            List.of(),
            false);
    assertTrue("All param errors suppressed when metadata not available", analysis.succeeded());
    assertEquals(0, analysis.parameterErrors().size());
  }

  @Test
  public void testExtraParameter_Error() {
    var analysis =
        new QueryAnalysis(
            "SELECT 1", List.of(new Alignment.LeftOnly<>(DuckDbTypes.integer)), List.of(), true);
    assertFalse("Extra parameter should produce an error", analysis.succeeded());
    assertEquals(1, analysis.parameterErrors().size());
    assertTrue(
        "Should be ExtraParameter",
        analysis.parameterErrors().getFirst() instanceof AlignmentError.ExtraParameter);
  }

  @Test
  public void testMissingParameter_Error() {
    var analysis =
        new QueryAnalysis(
            "SELECT 1 WHERE id = ?",
            List.of(new Alignment.RightOnly<>(paramMeta(1, "INTEGER"))),
            List.of(),
            true);
    assertFalse("Missing parameter should produce an error", analysis.succeeded());
    assertEquals(1, analysis.parameterErrors().size());
    assertTrue(
        "Should be MissingParameter",
        analysis.parameterErrors().getFirst() instanceof AlignmentError.MissingParameter);
  }

  @Test
  public void testColumnNullVendorTypeName_NoError() {
    var analysis =
        new QueryAnalysis(
            "SELECT x",
            List.of(),
            List.of(
                new Alignment.Both<>(
                    DuckDbTypes.integer,
                    colMeta(1, null, "x", ResultSetMetaData.columnNullableUnknown))),
            true);
    assertTrue("Null column vendor type name should be skipped", analysis.succeeded());
    assertEquals(0, analysis.columnErrors().size());
  }

  @Test
  public void testColumnEmptyVendorTypeName_NoError() {
    var analysis =
        new QueryAnalysis(
            "SELECT x",
            List.of(),
            List.of(
                new Alignment.Both<>(
                    DuckDbTypes.integer,
                    colMeta(1, "", "x", ResultSetMetaData.columnNullableUnknown))),
            true);
    assertTrue("Empty column vendor type name should be skipped", analysis.succeeded());
    assertEquals(0, analysis.columnErrors().size());
  }

  @Test
  public void testColumnUnknownVendorTypeName_NoError() {
    var analysis =
        new QueryAnalysis(
            "SELECT x",
            List.of(),
            List.of(
                new Alignment.Both<>(
                    DuckDbTypes.integer,
                    colMeta(1, "unknown", "x", ResultSetMetaData.columnNullableUnknown))),
            true);
    assertTrue("'unknown' column vendor type name should be skipped", analysis.succeeded());
    assertEquals(0, analysis.columnErrors().size());
  }

  @Test
  public void testColumnMismatchingType_Error() {
    var analysis =
        new QueryAnalysis(
            "SELECT x",
            List.of(),
            List.of(
                new Alignment.Both<>(
                    DuckDbTypes.integer,
                    colMeta(1, "varchar", "x", ResultSetMetaData.columnNullableUnknown))),
            true);
    assertFalse("Mismatching column type should produce an error", analysis.succeeded());
    assertEquals(1, analysis.columnErrors().size());
    assertTrue(
        "Should be ColumnTypeMismatch",
        analysis.columnErrors().getFirst() instanceof AlignmentError.ColumnTypeMismatch);
  }

  @Test
  public void testNullabilityMismatch_NullableColumnWithNonOptionalType() {
    var analysis =
        new QueryAnalysis(
            "SELECT x, y",
            List.of(),
            List.of(
                new Alignment.Both<>(
                    DuckDbTypes.integer,
                    colMeta(1, "INTEGER", "x", ResultSetMetaData.columnNoNulls)),
                new Alignment.Both<>(
                    DuckDbTypes.integer,
                    colMeta(2, "INTEGER", "y", ResultSetMetaData.columnNullable))),
            true);
    assertFalse("Nullable column with non-optional type should error", analysis.succeeded());
    var nullErrors =
        analysis.columnErrors().stream()
            .filter(e -> e instanceof AlignmentError.NullabilityMismatch)
            .toList();
    assertEquals(1, nullErrors.size());
    assertEquals(2, nullErrors.getFirst().position());
  }

  @Test
  public void testNullabilityUnknown_NoError() {
    var analysis =
        new QueryAnalysis(
            "SELECT x",
            List.of(),
            List.of(
                new Alignment.Both<>(
                    DuckDbTypes.integer,
                    colMeta(1, "INTEGER", "x", ResultSetMetaData.columnNullableUnknown))),
            true);
    assertTrue("Unknown nullability should not produce an error", analysis.succeeded());
  }

  @Test
  public void testAllColumnsNullable_NullabilitySkipped() {
    var analysis =
        new QueryAnalysis(
            "SELECT x, y",
            List.of(),
            List.of(
                new Alignment.Both<>(
                    DuckDbTypes.integer,
                    colMeta(1, "INTEGER", "x", ResultSetMetaData.columnNullable)),
                new Alignment.Both<>(
                    DuckDbTypes.varchar,
                    colMeta(2, "VARCHAR", "y", ResultSetMetaData.columnNullable))),
            true);
    assertTrue(
        "When ALL columns report nullable, nullability is unreliable and should be skipped",
        analysis.succeeded());
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // normalizeVendorTypeName tests
  // ─────────────────────────────────────────────────────────────────────────────

  @Test
  public void testNormalizeVendorTypeName_null() {
    assertEquals("", QueryAnalysis.normalizeVendorTypeName(null));
  }

  @Test
  public void testNormalizeVendorTypeName_empty() {
    assertEquals("", QueryAnalysis.normalizeVendorTypeName(""));
  }

  @Test
  public void testNormalizeVendorTypeName_lowercase() {
    assertEquals("varchar", QueryAnalysis.normalizeVendorTypeName("VARCHAR"));
    assertEquals("int4", QueryAnalysis.normalizeVendorTypeName("INT4"));
  }

  @Test
  public void testNormalizeVendorTypeName_stripPrecision() {
    assertEquals("varchar", QueryAnalysis.normalizeVendorTypeName("VARCHAR(255)"));
    assertEquals("decimal", QueryAnalysis.normalizeVendorTypeName("DECIMAL(10,2)"));
    assertEquals("number", QueryAnalysis.normalizeVendorTypeName("NUMBER(10)"));
  }

  @Test
  public void testNormalizeVendorTypeName_preserveArraySuffix() {
    assertEquals("integer[]", QueryAnalysis.normalizeVendorTypeName("INTEGER[]"));
    assertEquals("decimal[]", QueryAnalysis.normalizeVendorTypeName("DECIMAL(18,3)[]"));
  }

  @Test
  public void testNormalizeVendorTypeName_pgArrayPrefix() {
    assertEquals("int4[]", QueryAnalysis.normalizeVendorTypeName("_int4"));
    assertEquals("varchar[]", QueryAnalysis.normalizeVendorTypeName("_varchar"));
  }

  @Test
  public void testNormalizeVendorTypeName_stripSchemaPrefix() {
    assertEquals("address_t", QueryAnalysis.normalizeVendorTypeName("typr.address_t"));
    assertEquals("my_type", QueryAnalysis.normalizeVendorTypeName("schema.my_type"));
  }

  @Test
  public void testNormalizeVendorTypeName_trimWhitespace() {
    assertEquals("integer", QueryAnalysis.normalizeVendorTypeName("  INTEGER  "));
  }

  // ─────────────────────────────────────────────────────────────────────────────
  // Composite (STRUCT) structural matching — DuckDbTypenameParser end-to-end
  // ─────────────────────────────────────────────────────────────────────────────

  record Person(String name, Integer age) {}

  static final DuckDbType<Person> personType =
      DuckDbTypes.compositeOf(
          "person",
          RowCodec.<Person>namedBuilder()
              .field("name", DuckDbTypes.varchar, Person::name)
              .field("age", DuckDbTypes.integer, Person::age)
              .build(Person::new));

  @Test
  public void testStructAnalysis_matchingFields() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TEMP TABLE t (p STRUCT(name VARCHAR, age INTEGER))");
          RowCodec<Person> parser = RowCodec.of(personType);
          var analysis =
              QueryAnalyzer.analyze(Fragment.of("SELECT p FROM t").query(parser.all()), conn)
                  .getFirst();
          assertTrue(
              "expected structural match, got:\n" + analysis.report(), analysis.succeeded());
          return null;
        });
  }

  @Test
  public void testStructAnalysis_wrongFieldName() {
    // Declared STRUCT field is "age" but the DB column has "years". The parser-based path
    // catches this — string-normalize alone would not (both normalize to "struct").
    record PersonWrong(String name, Integer years) {}
    DuckDbType<PersonWrong> wrongType =
        DuckDbTypes.compositeOf(
            "person_wrong",
            RowCodec.<PersonWrong>namedBuilder()
                .field("name", DuckDbTypes.varchar, PersonWrong::name)
                .field("years", DuckDbTypes.integer, PersonWrong::years)
                .build(PersonWrong::new));
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TEMP TABLE t (p STRUCT(name VARCHAR, age INTEGER))");
          RowCodec<PersonWrong> parser = RowCodec.of(wrongType);
          var analysis =
              QueryAnalyzer.analyze(Fragment.of("SELECT p FROM t").query(parser.all()), conn)
                  .getFirst();
          assertFalse(
              "expected struct-field mismatch to fail analysis, got:\n" + analysis.report(),
              analysis.succeeded());
          return null;
        });
  }

  @Test
  public void testStructAnalysis_wrongFieldType() {
    // Declared STRUCT field "age" is INTEGER but DB column has BIGINT.
    record PersonBig(String name, Long age) {}
    DuckDbType<PersonBig> bigType =
        DuckDbTypes.compositeOf(
            "person_big",
            RowCodec.<PersonBig>namedBuilder()
                .field("name", DuckDbTypes.varchar, PersonBig::name)
                .field("age", DuckDbTypes.bigint, PersonBig::age)
                .build(PersonBig::new));
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TEMP TABLE t (p STRUCT(name VARCHAR, age INTEGER))");
          RowCodec<PersonBig> parser = RowCodec.of(bigType);
          var analysis =
              QueryAnalyzer.analyze(Fragment.of("SELECT p FROM t").query(parser.all()), conn)
                  .getFirst();
          assertFalse(
              "expected struct-field-type mismatch to fail analysis, got:\n" + analysis.report(),
              analysis.succeeded());
          return null;
        });
  }

  @Test
  public void testStructAnalysis_listOfStructMatches() {
    withConnection(
        conn -> {
          conn.createStatement()
              .execute("CREATE TEMP TABLE t (ps STRUCT(name VARCHAR, age INTEGER)[])");
          RowCodec<List<Person>> parser = RowCodec.of(personType.list());
          var analysis =
              QueryAnalyzer.analyze(Fragment.of("SELECT ps FROM t").query(parser.all()), conn)
                  .getFirst();
          assertTrue(
              "expected structural match on struct[], got:\n" + analysis.report(),
              analysis.succeeded());
          return null;
        });
  }
}
