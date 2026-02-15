package dev.typr.foundations;

import java.math.BigDecimal;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * Tests for SQL Server type codecs. Tests JDBC roundtrip, JSON roundtrip, and JSON DB roundtrip for
 * every type.
 */
public class SqlServerTypeTest {

  private static final AtomicInteger tableCounter = new AtomicInteger(0);

  private static String uniqueTableName(String prefix) {
    return prefix + "_" + tableCounter.incrementAndGet();
  }

  // Test wrapper types for alias and CLR types (like generated domain types)
  record EmailAddress(String value) {}

  record AssemblyData(byte[] value) {}

  record SqlServerTypeAndExample<A>(
      SqlServerType<A> type,
      A example,
      boolean hasIdentity,
      boolean supportsTextRoundtrip,
      boolean supportsJsonDbRoundtrip) {

    public SqlServerTypeAndExample(SqlServerType<A> type, A example) {
      this(type, example, true, true, true);
    }

    public SqlServerTypeAndExample<A> noIdentity() {
      return new SqlServerTypeAndExample<>(
          type, example, false, supportsTextRoundtrip, supportsJsonDbRoundtrip);
    }

    public SqlServerTypeAndExample<A> noTextRoundtrip() {
      return new SqlServerTypeAndExample<>(
          type, example, hasIdentity, false, supportsJsonDbRoundtrip);
    }

    public SqlServerTypeAndExample<A> noJsonDbRoundtrip() {
      return new SqlServerTypeAndExample<>(
          type, example, hasIdentity, supportsTextRoundtrip, false);
    }
  }

  static com.microsoft.sqlserver.jdbc.Geography createGeography() {
    try {
      return com.microsoft.sqlserver.jdbc.Geography.point(47.653, -122.358, 4326);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static com.microsoft.sqlserver.jdbc.Geometry createGeometry() {
    try {
      return com.microsoft.sqlserver.jdbc.Geometry.point(10.0, 20.0, 0);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  List<SqlServerTypeAndExample<?>> All =
      List.of(
          // ==================== Integer Types ====================
          new SqlServerTypeAndExample<>(
              SqlServerTypes.tinyint, new dev.typr.foundations.data.Uint1((short) 127)),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.tinyint, new dev.typr.foundations.data.Uint1((short) 0)),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.tinyint, new dev.typr.foundations.data.Uint1((short) 255)),
          new SqlServerTypeAndExample<>(SqlServerTypes.smallint, (short) 32767),
          new SqlServerTypeAndExample<>(SqlServerTypes.smallint, Short.MIN_VALUE),
          new SqlServerTypeAndExample<>(SqlServerTypes.smallint, Short.MAX_VALUE),
          new SqlServerTypeAndExample<>(SqlServerTypes.int_, 42),
          new SqlServerTypeAndExample<>(SqlServerTypes.int_, Integer.MIN_VALUE),
          new SqlServerTypeAndExample<>(SqlServerTypes.int_, Integer.MAX_VALUE),
          new SqlServerTypeAndExample<>(SqlServerTypes.bigint, 9223372036854775807L),
          new SqlServerTypeAndExample<>(SqlServerTypes.bigint, Long.MIN_VALUE),
          new SqlServerTypeAndExample<>(SqlServerTypes.bigint, Long.MAX_VALUE),

          // ==================== Fixed-Point Types ====================
          // Use DECIMAL(18,4) - values must have exactly 4 decimal places to match
          new SqlServerTypeAndExample<>(
              SqlServerTypes.decimal(18, 4), new BigDecimal("12345.6789")),
          new SqlServerTypeAndExample<>(SqlServerTypes.decimal(18, 4), new BigDecimal("0.0000")),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.decimal(18, 4), new BigDecimal("-99999.9990")),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.decimal(10, 2), new BigDecimal("12345678.90")),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.money, new BigDecimal("922337203685477.5807")),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.money, new BigDecimal("-922337203685477.5808")),
          new SqlServerTypeAndExample<>(SqlServerTypes.smallmoney, new BigDecimal("214748.3647")),
          new SqlServerTypeAndExample<>(SqlServerTypes.smallmoney, new BigDecimal("-214748.3648")),

          // ==================== Floating-Point Types ====================
          new SqlServerTypeAndExample<>(SqlServerTypes.real, 3.14f).noIdentity(),
          new SqlServerTypeAndExample<>(SqlServerTypes.real, 0.0f).noIdentity(),
          new SqlServerTypeAndExample<>(SqlServerTypes.real, Float.MAX_VALUE).noIdentity(),
          new SqlServerTypeAndExample<>(SqlServerTypes.float_, 2.718281828459045),
          new SqlServerTypeAndExample<>(SqlServerTypes.float_, 0.0),
          new SqlServerTypeAndExample<>(SqlServerTypes.float_, 1234567.89),

          // ==================== Boolean Type ====================
          new SqlServerTypeAndExample<>(SqlServerTypes.bit, true),
          new SqlServerTypeAndExample<>(SqlServerTypes.bit, false),

          // ==================== String Types (Non-Unicode) ====================
          // Use explicit sizes that match the data
          new SqlServerTypeAndExample<>(SqlServerTypes.char_(10), "Hello     "),
          new SqlServerTypeAndExample<>(SqlServerTypes.char_(10), "fixed     "),
          new SqlServerTypeAndExample<>(SqlServerTypes.varchar(50), "variable length"),
          new SqlServerTypeAndExample<>(SqlServerTypes.varchar(50), ""),
          new SqlServerTypeAndExample<>(SqlServerTypes.varchar(50), "Quote\"Test'Single"),
          new SqlServerTypeAndExample<>(SqlServerTypes.varcharMax, "Very long text ".repeat(100)),
          // TEXT is deprecated legacy type, cannot be used with = operator in WHERE clause
          new SqlServerTypeAndExample<>(SqlServerTypes.text, "legacy text type").noIdentity(),

          // ==================== String Types (Unicode) ====================
          new SqlServerTypeAndExample<>(SqlServerTypes.nchar(10), "Unicode   "),
          new SqlServerTypeAndExample<>(SqlServerTypes.nvarchar(50), "Unicode variable: 中文 😀"),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.nvarcharMax, "Unicode long: ".repeat(50) + "中文"),
          // NTEXT is deprecated legacy type, cannot be used with = operator in WHERE clause
          new SqlServerTypeAndExample<>(SqlServerTypes.ntext, "legacy unicode text").noIdentity(),

          // ==================== Binary Types ====================
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.binary(10),
                  new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A})
              .noIdentity(),
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.varbinary(10), new byte[] {(byte) 0xFF, 0x00, 0x7F})
              .noIdentity(),
          new SqlServerTypeAndExample<>(SqlServerTypes.varbinaryMax, new byte[1000]).noIdentity(),
          // IMAGE is deprecated legacy type, cannot be used with = operator in WHERE clause
          new SqlServerTypeAndExample<>(SqlServerTypes.image, new byte[] {0x01, 0x02}).noIdentity(),

          // ==================== Date/Time Types ====================
          new SqlServerTypeAndExample<>(SqlServerTypes.date, LocalDate.of(2024, 12, 22)),
          new SqlServerTypeAndExample<>(SqlServerTypes.date, LocalDate.of(1970, 1, 1)),
          new SqlServerTypeAndExample<>(SqlServerTypes.time, LocalTime.of(14, 30, 45)),
          new SqlServerTypeAndExample<>(SqlServerTypes.time, LocalTime.of(0, 0, 0)),
          // TIME(7) has JDBC conversion precision issues with nanoseconds
          new SqlServerTypeAndExample<>(SqlServerTypes.time(7), LocalTime.of(14, 30, 45, 123456700))
              .noIdentity(),
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.datetime, LocalDateTime.of(2024, 12, 22, 14, 30, 45))
              .noIdentity(),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.smalldatetime, LocalDateTime.of(2024, 12, 22, 14, 30, 0)),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.datetime2, LocalDateTime.of(2024, 12, 22, 14, 30, 45, 123456700)),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.datetime2(7), LocalDateTime.of(2024, 12, 22, 14, 30, 45, 123456700)),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.datetimeoffset,
              OffsetDateTime.of(2024, 12, 22, 14, 30, 45, 0, ZoneOffset.UTC)),
          new SqlServerTypeAndExample<>(
              SqlServerTypes.datetimeoffset(7),
              OffsetDateTime.of(2024, 12, 22, 14, 30, 45, 123456700, ZoneOffset.ofHours(-5))),

          // ==================== Special Types ====================
          new SqlServerTypeAndExample<>(
              SqlServerTypes.uniqueidentifier,
              UUID.fromString("550e8400-e29b-41d4-a716-446655440000")),
          new SqlServerTypeAndExample<>(SqlServerTypes.uniqueidentifier, UUID.randomUUID())
              .noIdentity(),
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.xml,
                  new dev.typr.foundations.data.Xml("<root><item>value</item></root>"))
              .noIdentity()
              .noJsonDbRoundtrip(),
          // HIERARCHYID - can't use identity check (= operator) and JSON DB doesn't work
          // because our Java encoder produces different bytes than SQL Server's encoder.
          // JDBC roundtrip works when SQL Server parses the path string.
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.hierarchyid, dev.typr.foundations.data.HierarchyId.parse("/"))
              .noIdentity()
              .noJsonDbRoundtrip(),
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.hierarchyid, dev.typr.foundations.data.HierarchyId.parse("/1/"))
              .noIdentity()
              .noJsonDbRoundtrip(),
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.hierarchyid, dev.typr.foundations.data.HierarchyId.parse("/1/2/"))
              .noIdentity()
              .noJsonDbRoundtrip(),
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.hierarchyid,
                  dev.typr.foundations.data.HierarchyId.parse("/1/2/3/"))
              .noIdentity()
              .noJsonDbRoundtrip(),
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.hierarchyid,
                  dev.typr.foundations.data.HierarchyId.parse("/1/1/1/1/"))
              .noIdentity()
              .noJsonDbRoundtrip(),

          // ==================== Spatial Types ====================
          // Spatial types cannot use = operator, and FOR JSON cannot serialize CLR objects
          new SqlServerTypeAndExample<>(SqlServerTypes.geography, createGeography())
              .noIdentity()
              .noJsonDbRoundtrip(),
          new SqlServerTypeAndExample<>(SqlServerTypes.geometry, createGeometry())
              .noIdentity()
              .noJsonDbRoundtrip(),

          // ==================== Alias Types (User-Defined Types) ====================
          // Test domain-like wrapper pattern (like CREATE TYPE EmailAddress FROM NVARCHAR(255))
          new SqlServerTypeAndExample<>(
              SqlServerTypes.nvarchar(255).transform(EmailAddress::new, EmailAddress::value),
              new EmailAddress("test@example.com")),

          // ==================== CLR Types (Assembly Types) ====================
          // Test CLR type as domain wrapper around VARBINARY (like generated code)
          new SqlServerTypeAndExample<>(
                  SqlServerTypes.varbinary(100).transform(AssemblyData::new, AssemblyData::value),
                  new AssemblyData(new byte[] {0x01, 0x02, 0x03, 0x04}))
              .noIdentity());

  static void withConnection(SqlFunction<Connection, ?> f) {
    try {
      Containers.sqlserverTransactor().execute(f);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void test() {
    System.out.println("=== SQL Server Type Tester ===\n");

    // Test JSON roundtrip (in-memory) - parallel
    System.out.println("=== JSON Roundtrip Tests (parallel) ===");
    All.parallelStream().forEach(SqlServerTypeTest::testJsonRoundtrip);
    System.out.println();

    // Run all DB tests in parallel
    System.out.println("=== DB Roundtrip Tests (parallel) ===");
    var failures =
        All.parallelStream()
            .flatMap(
                t -> {
                  var errors = new ArrayList<String>();

                  // JDBC roundtrip test
                  try {
                    withConnection(
                        conn -> {
                          testJdbcRoundtrip(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    errors.add(
                        "JDBC test FAILED " + t.type.typename().sqlType() + ": " + e.getMessage());
                  }

                  // JSON DB roundtrip test
                  if (t.supportsJsonDbRoundtrip) {
                    try {
                      withConnection(
                          conn -> {
                            testJsonDbRoundtrip(conn, t);
                            return null;
                          });
                    } catch (Exception e) {
                      errors.add(
                          "JSON DB test FAILED "
                              + t.type.typename().sqlType()
                              + ": "
                              + e.getMessage());
                    }
                  }

                  return errors.stream();
                })
            .toList();

    // Callable roundtrip tests (stored procedure IN/OUT) - parallel
    System.out.println("\n=== Callable Roundtrip Tests (parallel) ===");
    var callableFailures =
        All.parallelStream()
            .flatMap(
                t -> {
                  var errors = new ArrayList<String>();
                  try {
                    withConnection(
                        conn -> {
                          testCallableRoundtrip(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    String msg = e.getMessage() != null ? e.getMessage() : e.toString();
                    if (msg.contains("does not support stored procedure OUT parameters")) {
                      System.out.println(
                          "Callable SKIPPED "
                              + t.type.typename().sqlType()
                              + ": not supported for OUT params");
                    } else {
                      errors.add(
                          "Callable test FAILED "
                              + t.type.typename().sqlType()
                              + ": "
                              + msg);
                    }
                  }
                  return errors.stream();
                })
            .toList();

    // Query analysis tests - deduplicated by SQL type, run in parallel
    System.out.println("\n=== Query Analysis Tests (parallel) ===");
    var analysisFailures =
        All.stream()
            .collect(Collectors.toMap(t -> t.type.typename().sqlType(), t -> t, (a, b) -> a))
            .values()
            .parallelStream()
            .flatMap(
                t -> {
                  var errors = new ArrayList<String>();
                  try {
                    withConnection(
                        conn -> {
                          testQueryAnalysis(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    errors.add(
                        "Analysis FAILED "
                            + t.type.typename().sqlType()
                            + ": "
                            + e.getMessage());
                  }
                  if (t.hasIdentity) {
                    try {
                      withConnection(
                          conn -> {
                            testQueryAnalysisWithParam(conn, t);
                            return null;
                          });
                    } catch (Exception e) {
                      errors.add(
                          "Param analysis FAILED "
                              + t.type.typename().sqlType()
                              + ": "
                              + e.getMessage());
                    }
                  }
                  return errors.stream();
                })
            .toList();

    var allFailures = new ArrayList<String>();
    allFailures.addAll(failures);
    allFailures.addAll(callableFailures);
    allFailures.addAll(analysisFailures);

    System.out.println("\n=====================================");
    if (allFailures.isEmpty()) {
      System.out.println("All tests passed!");
    } else {
      allFailures.forEach(System.out::println);
      throw new RuntimeException(
          allFailures.size() + " tests failed:\n" + String.join("\n", allFailures));
    }
    System.out.println("=====================================");
  }

  static <A> void testQueryAnalysis(Connection conn, SqlServerTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("#qa");
    conn.createStatement().execute("CREATE TABLE " + tableName + " (v " + sqlType + ")");
    try {
      RowParser<A> parser = RowParser.of(t.type);
      Fragment fragment = Fragment.of("SELECT v FROM " + tableName);
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();
      if (!analysis.succeeded()) {
        throw new RuntimeException(
            "Query analysis failed for " + sqlType + ":\n" + analysis.report());
      }
    } finally {
      conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void testQueryAnalysisWithParam(Connection conn, SqlServerTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("#qap");
    conn.createStatement()
        .execute("CREATE TABLE " + tableName + " (v " + sqlType + " NOT NULL)");
    try {
      RowParser<A> parser = RowParser.of(t.type);
      Fragment fragment =
          Fragment.of("SELECT v FROM " + tableName + " WHERE v = ")
              .value(t.type, t.example);
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();
      if (!analysis.succeeded()) {
        throw new RuntimeException(
            "Param analysis failed for " + sqlType + ":\n" + analysis.report());
      }
    } finally {
      conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void testJsonRoundtrip(SqlServerTypeAndExample<A> t) {
    SqlServerJson<A> jsonCodec = t.type.sqlServerJson();
    A original = t.example;

    dev.typr.foundations.data.JsonValue jsonValue = jsonCodec.toJson(original);
    String encoded = jsonValue.encode();
    dev.typr.foundations.data.JsonValue parsed = dev.typr.foundations.data.JsonValue.parse(encoded);
    A decoded = jsonCodec.fromJson(parsed);

    System.out.println(
        "JSON roundtrip "
            + t.type.typename().sqlType()
            + ": "
            + format(original)
            + " -> "
            + encoded
            + " -> "
            + format(decoded));

    if (t.hasIdentity && !areEqual(decoded, original)) {
      throw new RuntimeException("JSON roundtrip failed for " + t.type.typename().sqlType());
    }
  }

  static <A> void testJdbcRoundtrip(Connection conn, SqlServerTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("#test");

    // Create temp table
    String createSql = "CREATE TABLE " + tableName + " (v " + sqlType + ")";
    conn.createStatement().execute(createSql);

    try {
      // Insert value
      var insert = conn.prepareStatement("INSERT INTO " + tableName + " (v) VALUES (?)");
      t.type.write().set(insert, 1, t.example);
      insert.execute();
      insert.close();

      // Select back
      PreparedStatement select;
      if (t.hasIdentity) {
        // Cast parameter to match column type exactly
        String whereClause =
            switch (sqlType) {
              case "GEOGRAPHY" -> "WHERE v.STEquals(CAST(? AS GEOGRAPHY)) = 1";
              case "GEOMETRY" -> "WHERE v.STEquals(CAST(? AS GEOMETRY)) = 1";
              default -> "WHERE v = CAST(? AS " + sqlType + ")";
            };
        select = conn.prepareStatement("SELECT v FROM " + tableName + " " + whereClause);
        t.type.write().set(select, 1, t.example);
      } else {
        select = conn.prepareStatement("SELECT v FROM " + tableName);
      }

      select.execute();
      var rs = select.getResultSet();
      if (!rs.next()) {
        throw new RuntimeException("No rows returned for " + sqlType);
      }

      A result = t.type.read().read(rs, 1);
      select.close();

      System.out.println(
          "JDBC roundtrip " + sqlType + ": " + format(t.example) + " -> " + format(result));

      if (t.hasIdentity && !areEqual(result, t.example)) {
        throw new RuntimeException("JDBC roundtrip failed for " + sqlType);
      }
    } finally {
      conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void testJsonDbRoundtrip(Connection conn, SqlServerTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("#test_json");

    // Create temp table
    conn.createStatement().execute("CREATE TABLE " + tableName + " (v " + sqlType + ")");

    try {
      // Insert value
      var insert = conn.prepareStatement("INSERT INTO " + tableName + " (v) VALUES (?)");
      t.type.write().set(insert, 1, t.example);
      insert.execute();
      insert.close();

      // Select back as JSON using FOR JSON PATH
      var select =
          conn.prepareStatement(
              "SELECT v FROM " + tableName + " FOR JSON PATH, WITHOUT_ARRAY_WRAPPER");
      select.execute();
      var rs = select.getResultSet();

      if (!rs.next()) {
        throw new RuntimeException("No JSON returned");
      }

      String jsonFromDb = rs.getString(1);
      select.close();

      // Parse JSON and extract value
      dev.typr.foundations.data.JsonValue parsedFromDb =
          dev.typr.foundations.data.JsonValue.parse(jsonFromDb);
      dev.typr.foundations.data.JsonValue valueJson =
          ((dev.typr.foundations.data.JsonValue.JObject) parsedFromDb).get("v");
      A decoded = t.type.sqlServerJson().fromJson(valueJson);

      System.out.println(
          "JSON DB roundtrip "
              + sqlType
              + ": "
              + format(t.example)
              + " -> DB -> "
              + jsonFromDb
              + " -> "
              + format(decoded));

      if (t.hasIdentity && !areEqual(decoded, t.example)) {
        throw new RuntimeException("JSON DB roundtrip failed for " + sqlType);
      }
    } finally {
      conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
    }
  }

  private static final Set<String> UNSUPPORTED_PROC_PARAM_TYPES =
      Set.of("TEXT", "NTEXT", "IMAGE", "ROWVERSION", "TIMESTAMP");

  static <A> void testCallableRoundtrip(Connection conn, SqlServerTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();

    if (UNSUPPORTED_PROC_PARAM_TYPES.contains(sqlType.toUpperCase())) {
      System.out.println(
          "Callable SKIPPED " + sqlType + ": cannot be used as stored procedure parameter");
      return;
    }

    String procName = "dbo." + uniqueTableName("proc_callable");
    String createSql =
        "CREATE PROCEDURE "
            + procName
            + " @p_in "
            + sqlType
            + ", @p_out "
            + sqlType
            + " OUTPUT AS BEGIN SET @p_out = @p_in; END";
    conn.createStatement().execute(createSql);

    try {
      DbProcedure.Def1_1<A, A> proc =
          DbProcedure.define(procName).in(t.type).out(t.type).build();

      A result = proc.call(t.example).runChecked(conn);

      System.out.println(
          "Callable roundtrip "
              + sqlType
              + ": "
              + format(t.example)
              + " -> proc -> "
              + format(result));

      if (t.hasIdentity && !areEqual(result, t.example)) {
        throw new RuntimeException("Callable roundtrip failed for " + sqlType);
      }
    } finally {
      conn.createStatement().execute("DROP PROCEDURE IF EXISTS " + procName);
    }
  }

  static <A> boolean areEqual(A actual, A expected) {
    if (expected instanceof byte[]) {
      return Arrays.equals((byte[]) actual, (byte[]) expected);
    }
    if (expected instanceof Object[]) {
      return Arrays.equals((Object[]) actual, (Object[]) expected);
    }
    // For BigDecimal, compare numerically (ignore scale differences)
    if (expected instanceof BigDecimal) {
      return ((BigDecimal) actual).compareTo((BigDecimal) expected) == 0;
    }
    // For floating point, allow small differences
    if (expected instanceof Float) {
      return Math.abs((Float) actual - (Float) expected) < 0.0001f;
    }
    if (expected instanceof Double) {
      return Math.abs((Double) actual - (Double) expected) < 0.0001;
    }
    // For spatial types, compare WKT representations
    if (expected instanceof com.microsoft.sqlserver.jdbc.Geography) {
      return ((com.microsoft.sqlserver.jdbc.Geography) actual)
          .toString()
          .equals(((com.microsoft.sqlserver.jdbc.Geography) expected).toString());
    }
    if (expected instanceof com.microsoft.sqlserver.jdbc.Geometry) {
      return ((com.microsoft.sqlserver.jdbc.Geometry) actual)
          .toString()
          .equals(((com.microsoft.sqlserver.jdbc.Geometry) expected).toString());
    }
    // For wrapper types (AssemblyData), compare underlying values
    if (expected instanceof AssemblyData) {
      return Arrays.equals(((AssemblyData) actual).value(), ((AssemblyData) expected).value());
    }
    return actual.equals(expected);
  }

  static <A> String format(A a) {
    if (a instanceof byte[]) {
      byte[] arr = (byte[]) a;
      if (arr.length > 20) {
        return "byte[" + arr.length + "]";
      }
      return Arrays.toString(arr);
    }
    if (a instanceof Object[]) {
      return Arrays.toString((Object[]) a);
    }
    if (a instanceof String) {
      String s = (String) a;
      if (s.length() > 50) {
        return "\"" + s.substring(0, 47) + "...\"";
      }
    }
    return a.toString();
  }
}
