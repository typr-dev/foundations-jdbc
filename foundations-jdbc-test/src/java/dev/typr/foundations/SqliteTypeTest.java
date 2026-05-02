package dev.typr.foundations;

import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.junit.Test;

/**
 * Tests for SQLite type codecs. SQLite is embedded, so the tests run in-process against {@code
 * jdbc:sqlite::memory:}.
 *
 * <p>Each {@code DriverManager.getConnection("jdbc:sqlite::memory:")} call opens an independent
 * database, so {@link #withConnection(SqlFunction)} creates one per test case and tears it down
 * via close.
 */
public class SqliteTypeTest {

  private static final AtomicInteger tableCounter = new AtomicInteger(0);

  private static String uniqueTableName(String prefix) {
    return prefix + "_" + tableCounter.incrementAndGet();
  }

  // Wrapper-type examples for transform()
  record UserId(long value) {}

  record ProductCode(String value) {}

  static SqliteType<UserId> userIdType =
      SqliteTypes.integer.transform(UserId::new, UserId::value);

  static SqliteType<ProductCode> productCodeType =
      SqliteTypes.text.transform(ProductCode::new, ProductCode::value);

  enum Color {
    RED,
    GREEN,
    BLUE
  }

  record SqliteTypeAndExample<A>(SqliteType<A> type, A example, boolean hasIdentity) {
    public SqliteTypeAndExample(SqliteType<A> type, A example) {
      this(type, example, true);
    }

    public SqliteTypeAndExample<A> noIdentity() {
      return new SqliteTypeAndExample<>(type, example, false);
    }
  }

  List<SqliteTypeAndExample<?>> All =
      List.of(
          // ==================== INTEGER affinity ====================
          new SqliteTypeAndExample<>(SqliteTypes.integer, 42424242424242L),
          new SqliteTypeAndExample<>(SqliteTypes.integer, Long.MIN_VALUE),
          new SqliteTypeAndExample<>(SqliteTypes.integer, Long.MAX_VALUE),
          new SqliteTypeAndExample<>(SqliteTypes.integer, 0L),
          new SqliteTypeAndExample<>(SqliteTypes.bigint, 9876543210L),
          new SqliteTypeAndExample<>(SqliteTypes.int_, 42424242),
          new SqliteTypeAndExample<>(SqliteTypes.int_, Integer.MIN_VALUE),
          new SqliteTypeAndExample<>(SqliteTypes.int_, Integer.MAX_VALUE),
          new SqliteTypeAndExample<>(SqliteTypes.smallint, (short) 4242),
          new SqliteTypeAndExample<>(SqliteTypes.smallint, Short.MIN_VALUE),
          new SqliteTypeAndExample<>(SqliteTypes.smallint, Short.MAX_VALUE),
          new SqliteTypeAndExample<>(SqliteTypes.tinyint, (byte) 42),
          new SqliteTypeAndExample<>(SqliteTypes.tinyint, Byte.MIN_VALUE),
          new SqliteTypeAndExample<>(SqliteTypes.tinyint, Byte.MAX_VALUE),

          // ==================== Boolean (NUMERIC affinity, stored as 0/1) ====================
          new SqliteTypeAndExample<>(SqliteTypes.boolean_, true),
          new SqliteTypeAndExample<>(SqliteTypes.boolean_, false),

          // ==================== REAL affinity ====================
          new SqliteTypeAndExample<>(SqliteTypes.real, 3.141592653589793),
          new SqliteTypeAndExample<>(SqliteTypes.real, 0.0),
          new SqliteTypeAndExample<>(SqliteTypes.real, -3.141592653589793),
          new SqliteTypeAndExample<>(SqliteTypes.real, Double.MAX_VALUE),
          new SqliteTypeAndExample<>(SqliteTypes.float_, 3.14159f).noIdentity(),
          new SqliteTypeAndExample<>(SqliteTypes.float_, 0.0f).noIdentity(),

          // ==================== NUMERIC affinity (BigDecimal) ====================
          new SqliteTypeAndExample<>(SqliteTypes.numeric, new BigDecimal("12345")),
          new SqliteTypeAndExample<>(SqliteTypes.numeric, BigDecimal.ZERO),
          new SqliteTypeAndExample<>(SqliteTypes.numeric, new BigDecimal("-9999999999")),
          new SqliteTypeAndExample<>(SqliteTypes.decimalOf(10, 2), new BigDecimal("12345678.90")),
          new SqliteTypeAndExample<>(SqliteTypes.decimalOf(10, 2), new BigDecimal("0.00")),

          // ==================== TEXT affinity ====================
          new SqliteTypeAndExample<>(SqliteTypes.text, "Hello, SQLite!"),
          new SqliteTypeAndExample<>(SqliteTypes.text, ""),
          new SqliteTypeAndExample<>(
              SqliteTypes.text, "Unicode: éèê 中文"),
          new SqliteTypeAndExample<>(SqliteTypes.text, "Line1\nLine2\tTabbed"),
          new SqliteTypeAndExample<>(SqliteTypes.text, "Quote\"Test'Single\\Back"),
          new SqliteTypeAndExample<>(
              SqliteTypes.text, "Emoji: 😀🎉🚀"),
          new SqliteTypeAndExample<>(SqliteTypes.varcharOf(100), "Fixed length varchar"),
          new SqliteTypeAndExample<>(SqliteTypes.varchar, "varchar content"),
          new SqliteTypeAndExample<>(SqliteTypes.charOf(10), "hello"),
          new SqliteTypeAndExample<>(SqliteTypes.clob, "CLOB content here"),

          // ==================== BLOB affinity ====================
          new SqliteTypeAndExample<>(
              SqliteTypes.blob, new byte[] {0x01, 0x02, 0x03, 0x04, 0x05}),
          new SqliteTypeAndExample<>(SqliteTypes.blob, new byte[] {}),
          new SqliteTypeAndExample<>(
              SqliteTypes.blob, new byte[] {(byte) 0xFF, 0x00, 0x7F, (byte) 0x80}),
          new SqliteTypeAndExample<>(SqliteTypes.blob, new byte[] {0x00}),

          // ==================== Date/Time (TEXT) ====================
          new SqliteTypeAndExample<>(SqliteTypes.date, LocalDate.of(2024, 6, 15)),
          new SqliteTypeAndExample<>(SqliteTypes.date, LocalDate.of(1970, 1, 1)),
          new SqliteTypeAndExample<>(SqliteTypes.date, LocalDate.of(2099, 12, 31)),
          new SqliteTypeAndExample<>(SqliteTypes.time, LocalTime.of(14, 30, 45)),
          new SqliteTypeAndExample<>(SqliteTypes.time, LocalTime.of(0, 0, 0)),
          new SqliteTypeAndExample<>(SqliteTypes.time, LocalTime.of(23, 59, 59)),
          new SqliteTypeAndExample<>(
              SqliteTypes.datetime, LocalDateTime.of(2024, 6, 15, 14, 30, 45)),
          new SqliteTypeAndExample<>(
              SqliteTypes.datetime, LocalDateTime.of(1970, 1, 1, 0, 0, 0)),
          // Millis precision survives the default `yyyy-MM-dd HH:mm:ss.SSS` format. Higher
          // sub-second precision is silently truncated on write.
          new SqliteTypeAndExample<>(
              SqliteTypes.datetime, LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123_000_000)),
          new SqliteTypeAndExample<>(SqliteTypes.instant, Instant.parse("2024-06-15T14:30:45Z")),
          new SqliteTypeAndExample<>(
              SqliteTypes.instant, Instant.parse("2024-06-15T14:30:45.123Z")),

          // ==================== UUID (TEXT) ====================
          new SqliteTypeAndExample<>(
              SqliteTypes.uuid, UUID.fromString("550e8400-e29b-41d4-a716-446655440000")),
          new SqliteTypeAndExample<>(
              SqliteTypes.uuid, UUID.fromString("00000000-0000-0000-0000-000000000000")),

          // ==================== JSON (TEXT) ====================
          new SqliteTypeAndExample<>(SqliteTypes.json, new Json("{\"name\":\"SQLite\"}")),
          new SqliteTypeAndExample<>(SqliteTypes.json, new Json("[1,2,3]")),
          new SqliteTypeAndExample<>(SqliteTypes.json, new Json("{}")),

          // ==================== Enum (TEXT) ====================
          new SqliteTypeAndExample<>(SqliteTypes.ofEnum(Color::valueOf), Color.GREEN),
          new SqliteTypeAndExample<>(SqliteTypes.ofEnum(Color::valueOf), Color.RED),

          // ==================== Bimapped wrapper types ====================
          new SqliteTypeAndExample<>(userIdType, new UserId(42L)),
          new SqliteTypeAndExample<>(productCodeType, new ProductCode("PROD-001")));

  /**
   * Open a fresh in-memory SQLite database, run the test, and close. SQLite reuses no state
   * between connections to {@code :memory:}, so test isolation is automatic.
   */
  static <T> T withConnection(SqlFunction<Connection, T> f) {
    try (var jdbcConn = java.sql.DriverManager.getConnection("jdbc:sqlite::memory:")) {
      jdbcConn.setAutoCommit(false);
      try {
        return f.apply(new dev.typr.foundations.internal.ConnectionJdbc(jdbcConn));
      } finally {
        jdbcConn.rollback();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  // SQLite has only five storage classes (NULL, INTEGER, REAL, TEXT, BLOB) and no nested types
  // (no arrays, structs, maps, or unions). That makes the matrix of "positions" very small —
  // a value can appear as:
  //
  //   1. an INSERT/UPDATE column          — covered by Fragment.insertMany in {@link #testCase}
  //   2. a SELECT result column           — covered by the read in {@link #testCase}
  //   3. a WHERE-clause parameter         — covered by the bound `=?` in {@link #testCase}
  //   4. NULL via {@link DbType#opt()}    — covered by the "IS NULL" branch in {@link #testCase}
  //   5. a non-null value via opt()       — covered by {@link #testOptColumnRoundtrip}
  //   6. metadata for query analysis      — covered by {@link #testQueryAnalysis}
  //   7. metadata after parameter binding — covered by {@link #testQueryAnalysisWithParam}
  //
  // No further positions exist for SQLite. Every type in {@link #All} is exercised in all of them.
  @Test
  public void test() {
    System.out.println("Testing SQLite type codecs...\n");

    // JSON roundtrip — no DB connection needed
    System.out.println("=== JSON Roundtrip Tests ===");
    All.parallelStream().forEach(SqliteTypeTest::testJsonRoundtrip);
    System.out.println();

    // DB roundtrip — each test gets its own connection
    System.out.println("=== DB Roundtrip Tests ===");
    var failures =
        All.parallelStream()
            .flatMap(
                t -> {
                  var errors = new ArrayList<String>();
                  try {
                    withConnection(
                        conn -> {
                          testCase(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    errors.add(
                        "DB test FAILED " + t.type.typename().sqlType() + ": " + e.getMessage());
                  }
                  return errors.stream();
                })
            .toList();

    // opt() round-trip with a non-null value (column read/write through the Optional codec)
    System.out.println("\n=== Optional Column Round-trip Tests ===");
    var optFailures =
        All.parallelStream()
            .flatMap(
                t -> {
                  var errors = new ArrayList<String>();
                  try {
                    withConnection(
                        conn -> {
                          testOptColumnRoundtrip(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    errors.add(
                        "opt() round-trip FAILED "
                            + t.type.typename().sqlType()
                            + ": "
                            + e.getMessage());
                  }
                  return errors.stream();
                })
            .toList();

    // Query analysis (column-only and with-parameter)
    System.out.println("\n=== Query Analysis Tests ===");
    var analysisFailures =
        All.parallelStream()
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

    var allFailures = new ArrayList<>(failures);
    allFailures.addAll(optFailures);
    allFailures.addAll(analysisFailures);

    System.out.println("\n=====================================");
    if (allFailures.isEmpty()) {
      System.out.println("All SQLite tests passed!");
    } else {
      allFailures.forEach(f -> System.err.println("FAILURE: " + f));
      throw new RuntimeException(
          allFailures.size() + " tests failed:\n" + String.join("\n", allFailures));
    }
    System.out.println("=====================================");
  }

  static <A> void testJsonRoundtrip(SqliteTypeAndExample<A> t) {
    try {
      SqliteJson<A> jsonCodec = t.type.sqliteJson();
      A original = t.example;
      JsonValue jsonValue = jsonCodec.toJson(original);
      String encoded = jsonValue.encode();
      JsonValue parsed = JsonValue.parse(encoded);
      A decoded = jsonCodec.fromJson(parsed);
      System.out.println(
          "JSON "
              + t.type.typename().sqlType()
              + ": "
              + format(original)
              + " -> "
              + encoded
              + " -> "
              + format(decoded));
      if (t.hasIdentity && !areEqual(decoded, original)) {
        throw new RuntimeException(
            "JSON roundtrip failed for "
                + t.type.typename().sqlType()
                + ": expected '"
                + format(original)
                + "' but got '"
                + format(decoded)
                + "'");
      }
    } catch (Exception e) {
      throw new RuntimeException(
          "JSON roundtrip test failed for " + t.type.typename().sqlType(), e);
    }
  }

  static <A> void testQueryAnalysis(Connection conn, SqliteTypeAndExample<A> t) {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("qa");
    Fragment.of("CREATE TABLE " + tableName + " (v " + sqlType + ")").execute().run(conn);
    try {
      RowCodec<A> parser = RowCodec.of(t.type);
      Fragment fragment = Fragment.of("SELECT v FROM " + tableName);
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();
      if (!analysis.succeeded()) {
        throw new RuntimeException(
            "Query analysis failed for " + sqlType + ":\n" + analysis.report());
      }
    } finally {
      Fragment.of("DROP TABLE IF EXISTS " + tableName).execute().run(conn);
    }
  }

  static <A> void testQueryAnalysisWithParam(Connection conn, SqliteTypeAndExample<A> t) {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("qap");
    Fragment.of("CREATE TABLE " + tableName + " (v " + sqlType + " NOT NULL)")
        .execute()
        .run(conn);
    try {
      RowCodec<A> parser = RowCodec.of(t.type);
      Fragment fragment =
          Fragment.of("SELECT v FROM " + tableName + " WHERE v = ").value(t.type, t.example);
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();
      if (!analysis.succeeded()) {
        throw new RuntimeException(
            "Param query analysis failed for " + sqlType + ":\n" + analysis.report());
      }
    } finally {
      Fragment.of("DROP TABLE IF EXISTS " + tableName).execute().run(conn);
    }
  }

  /**
   * Round-trip a non-null value through {@code DbType.opt()} as the column type. Confirms that
   * the {@link Optional} wrapper writer and reader compose correctly — distinct from {@link
   * #testCase} which only exercises opt() on a NULL row.
   */
  static <A> void testOptColumnRoundtrip(Connection conn, SqliteTypeAndExample<A> t)
      throws SQLException {
    var jdbc = conn.unwrap();
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("opt");
    SqliteType<Optional<A>> optType = t.type.opt();

    try (var s = jdbc.createStatement()) {
      s.execute("CREATE TABLE " + tableName + " (v " + sqlType + ")");
    }
    try {
      Optional<A> some = Optional.of(t.example);

      RowCodecNamed<Optional<A>> parser =
          RowCodec.<Optional<A>>namedBuilder()
              .field("v", optType, Function.identity())
              .build(Function.identity());
      Fragment.insertMany(tableName, parser, List.of(some, Optional.<A>empty()).iterator())
          .run(conn);

      try (var s = jdbc.createStatement();
          var rs = s.executeQuery("SELECT v FROM " + tableName + " WHERE v IS NOT NULL")) {
        if (!rs.next()) throw new RuntimeException("Expected a non-null row for " + sqlType);
        Optional<A> readSome = optType.read().read(rs, 1);
        if (readSome.isEmpty()) {
          throw new RuntimeException("Expected Optional.of(...) for " + sqlType + ", got empty");
        }
        assertEquals(readSome.get(), t.example, "opt-some value mismatch for " + sqlType);
      }

      try (var s = jdbc.createStatement();
          var rs = s.executeQuery("SELECT v FROM " + tableName + " WHERE v IS NULL")) {
        if (!rs.next()) throw new RuntimeException("Expected NULL row for " + sqlType);
        Optional<A> readNone = optType.read().read(rs, 1);
        assertEquals(readNone, Optional.empty(), "opt-empty value mismatch for " + sqlType);
      }
    } finally {
      try (var s = jdbc.createStatement()) {
        s.execute("DROP TABLE IF EXISTS " + tableName);
      }
    }
  }

  static <A> void testCase(Connection conn, SqliteTypeAndExample<A> t) throws SQLException {
    var jdbc = conn.unwrap();
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("test_table");

    try (var s = jdbc.createStatement()) {
      s.execute("CREATE TABLE " + tableName + " (v " + sqlType + ")");
    }

    try {
      A expected = t.example;
      RowCodecNamed<A> parser =
          RowCodec.<A>namedBuilder()
              .field("v", t.type, Function.identity())
              .build(Function.identity());
      Fragment.insertMany(tableName, parser, List.of(expected).iterator()).run(conn);

      try (var s = jdbc.createStatement()) {
        s.execute("INSERT INTO " + tableName + " (v) VALUES (NULL)");
      }

      A actual;
      String selectSql =
          t.hasIdentity
              ? "SELECT v FROM " + tableName + " WHERE v = ?"
              : "SELECT v FROM " + tableName + " WHERE v IS NOT NULL";
      try (var select = jdbc.prepareStatement(selectSql)) {
        if (t.hasIdentity) {
          t.type.write().set(select, 1, expected);
        }
        try (var rs = select.executeQuery()) {
          if (!rs.next()) throw new RuntimeException("No rows returned for " + sqlType);
          actual = t.type.read().read(rs, 1);
        }
      }

      Optional<A> actualNull;
      try (var s = jdbc.createStatement();
          var rs = s.executeQuery("SELECT v FROM " + tableName + " WHERE v IS NULL")) {
        if (!rs.next()) throw new RuntimeException("Expected NULL row for " + sqlType);
        actualNull = t.type.opt().read().read(rs, 1);
      }

      assertEquals(actual, expected, "value mismatch for " + sqlType);
      assertEquals(actualNull, Optional.empty(), "null value mismatch for " + sqlType);
    } finally {
      try (var s = jdbc.createStatement()) {
        s.execute("DROP TABLE IF EXISTS " + tableName);
      }
    }
  }

  static <A> void assertEquals(A actual, A expected, String message) {
    if (!areEqual(actual, expected)) {
      throw new RuntimeException(
          message + ": actual='" + format(actual) + "' expected='" + format(expected) + "'");
    }
  }

  static <A> boolean areEqual(A actual, A expected) {
    if (expected == null && actual == null) return true;
    if (expected == null || actual == null) return false;
    if (expected instanceof byte[]) {
      return Arrays.equals((byte[]) actual, (byte[]) expected);
    }
    if (expected instanceof BigDecimal && actual instanceof BigDecimal) {
      return ((BigDecimal) actual).compareTo((BigDecimal) expected) == 0;
    }
    return actual.equals(expected);
  }

  /**
   * End-to-end smoke test: build a {@link dev.typr.foundations.connect.SqliteConfig}, hand it to
   * {@link dev.typr.foundations.connect.ConnectionSource}, get a {@link Transactor}, and run a
   * query through the full library API. {@link #test()} above bypasses the transactor and pokes
   * JDBC directly — this confirms the public happy path also works.
   */
  @org.junit.Test
  public void transactorEndToEnd() {
    var config =
        dev.typr.foundations.connect.SqliteConfig.inMemory().foreignKeys(true).build();
    var source = dev.typr.foundations.connect.ConnectionSource.of(config);
    var tx = source.transactor();

    Long answer =
        Fragment.of("SELECT 42").queryExactlyOne(SqliteTypes.integer).transact(tx);
    if (answer != 42L) {
      throw new RuntimeException("Expected 42, got " + answer);
    }

    // CREATE / INSERT / SELECT through the transactor
    Fragment.of("CREATE TABLE t (id INTEGER NOT NULL, name TEXT NOT NULL) STRICT")
        .execute()
        .transact(tx);
    Fragment.of("INSERT INTO t (id, name) VALUES (")
        .value(SqliteTypes.integer, 1L)
        .append(", ")
        .value(SqliteTypes.text, "alice")
        .append(")")
        .execute()
        .transact(tx);

    record Row(Long id, String name) {}
    var codec =
        RowCodec.<Row>namedBuilder()
            .field("id", SqliteTypes.integer, Row::id)
            .field("name", SqliteTypes.text, Row::name)
            .build(Row::new);
    Row row =
        Fragment.of("SELECT id, name FROM t").queryExactlyOne(codec).transact(tx);
    if (row.id() != 1L || !"alice".equals(row.name())) {
      throw new RuntimeException("Round-trip failed: " + row);
    }

    System.out.println("Transactor end-to-end: OK (foreign_keys=on, STRICT, " + row + ")");
  }

  static <A> String format(A a) {
    if (a == null) return "null";
    if (a instanceof byte[] bs) {
      StringBuilder sb = new StringBuilder("[");
      for (int i = 0; i < bs.length; i++) {
        if (i > 0) sb.append(", ");
        sb.append(String.format("0x%02X", bs[i]));
      }
      return sb.append("]").toString();
    }
    return a.toString();
  }
}
