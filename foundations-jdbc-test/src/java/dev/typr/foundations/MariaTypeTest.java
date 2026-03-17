package dev.typr.foundations;

import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import dev.typr.foundations.data.maria.Inet4;
import dev.typr.foundations.data.maria.Inet6;
import dev.typr.foundations.data.maria.MariaSet;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.Test;

/** Tests for MariaDB type codecs. Tests all types defined in MariaTypes. */
public class MariaTypeTest {

  private static final AtomicInteger tableCounter = new AtomicInteger(0);

  private static String uniqueTableName(String prefix) {
    return prefix + "_" + tableCounter.incrementAndGet();
  }

  record TestPair<A>(A t0, Optional<A> t1) {}

  record Item(String name, int quantity) {}

  static RowCodec<Item> itemCodec =
      RowCodec.<Item>builder()
          .field(MariaTypes.varchar, Item::name)
          .field(MariaTypes.int_, Item::quantity)
          .build(Item::new);

  static RowCodecNamed<Item> namedItemCodec =
      RowCodec.<Item>namedBuilder()
          .field("name", MariaTypes.varchar, Item::name)
          .field("quantity", MariaTypes.int_, Item::quantity)
          .build(Item::new);

  record MariaTypeAndExample<A>(
      MariaType<A> type,
      A example,
      boolean hasIdentity,
      boolean streamingWorks,
      boolean jsonRoundtripWorks) {
    public MariaTypeAndExample(MariaType<A> type, A example) {
      this(type, example, true, true, true);
    }

    public MariaTypeAndExample<A> noStreaming() {
      return new MariaTypeAndExample<>(type, example, hasIdentity, false, jsonRoundtripWorks);
    }

    public MariaTypeAndExample<A> noIdentity() {
      return new MariaTypeAndExample<>(type, example, false, streamingWorks, jsonRoundtripWorks);
    }

    // MariaDB's JSON encoding of binary data is lossy - bytes > 127 get corrupted
    public MariaTypeAndExample<A> noJsonRoundtrip() {
      return new MariaTypeAndExample<>(type, example, hasIdentity, streamingWorks, false);
    }
  }

  // Sample enum for ENUM type testing
  enum Color {
    RED,
    GREEN,
    BLUE
  }

  List<MariaTypeAndExample<?>> All =
      List.of(
          // ==================== Integer Types (Signed) ====================
          new MariaTypeAndExample<>(MariaTypes.tinyint, (byte) 42),
          new MariaTypeAndExample<>(MariaTypes.tinyint, Byte.MIN_VALUE), // Edge case: min value
          new MariaTypeAndExample<>(MariaTypes.tinyint, Byte.MAX_VALUE), // Edge case: max value
          new MariaTypeAndExample<>(MariaTypes.tinyint, (byte) 0), // Edge case: zero
          new MariaTypeAndExample<>(MariaTypes.smallint, (short) 4242),
          new MariaTypeAndExample<>(MariaTypes.smallint, Short.MIN_VALUE), // Edge case: min value
          new MariaTypeAndExample<>(MariaTypes.smallint, Short.MAX_VALUE), // Edge case: max value
          new MariaTypeAndExample<>(MariaTypes.smallint, (short) 0), // Edge case: zero
          new MariaTypeAndExample<>(MariaTypes.mediumint, 424242),
          new MariaTypeAndExample<>(MariaTypes.mediumint, -8388608), // Edge case: min MEDIUMINT
          new MariaTypeAndExample<>(MariaTypes.mediumint, 8388607), // Edge case: max MEDIUMINT
          new MariaTypeAndExample<>(MariaTypes.mediumint, 0), // Edge case: zero
          new MariaTypeAndExample<>(MariaTypes.int_, 42424242),
          new MariaTypeAndExample<>(MariaTypes.int_, Integer.MIN_VALUE), // Edge case: min value
          new MariaTypeAndExample<>(MariaTypes.int_, Integer.MAX_VALUE), // Edge case: max value
          new MariaTypeAndExample<>(MariaTypes.int_, 0), // Edge case: zero
          new MariaTypeAndExample<>(MariaTypes.bigint, 4242424242424242L),
          new MariaTypeAndExample<>(MariaTypes.bigint, Long.MIN_VALUE), // Edge case: min value
          new MariaTypeAndExample<>(MariaTypes.bigint, Long.MAX_VALUE), // Edge case: max value
          new MariaTypeAndExample<>(MariaTypes.bigint, 0L), // Edge case: zero

          // ==================== Integer Types (Unsigned) ====================
          new MariaTypeAndExample<>(
              MariaTypes.tinyintUnsigned,
              new dev.typr.foundations.data.Uint1((short) 255)), // Max TINYINT UNSIGNED
          new MariaTypeAndExample<>(
              MariaTypes.tinyintUnsigned,
              new dev.typr.foundations.data.Uint1((short) 0)), // Edge case: min unsigned
          new MariaTypeAndExample<>(
              MariaTypes.smallintUnsigned,
              new dev.typr.foundations.data.Uint2(65535)), // Max SMALLINT UNSIGNED
          new MariaTypeAndExample<>(
              MariaTypes.smallintUnsigned,
              new dev.typr.foundations.data.Uint2(0)), // Edge case: min unsigned
          new MariaTypeAndExample<>(
              MariaTypes.mediumintUnsigned,
              new dev.typr.foundations.data.Uint4(16777215)), // Max MEDIUMINT UNSIGNED
          new MariaTypeAndExample<>(
              MariaTypes.mediumintUnsigned,
              new dev.typr.foundations.data.Uint4(0)), // Edge case: min unsigned
          new MariaTypeAndExample<>(
              MariaTypes.intUnsigned,
              new dev.typr.foundations.data.Uint4(4294967295L)), // Max INT UNSIGNED
          new MariaTypeAndExample<>(
              MariaTypes.intUnsigned,
              new dev.typr.foundations.data.Uint4(0L)), // Edge case: min unsigned
          new MariaTypeAndExample<>(
              MariaTypes.bigintUnsigned,
              new dev.typr.foundations.data.Uint8(
                  new BigInteger("18446744073709551615"))), // Max BIGINT UNSIGNED
          new MariaTypeAndExample<>(
              MariaTypes.bigintUnsigned,
              new dev.typr.foundations.data.Uint8(BigInteger.ZERO)), // Edge case: min unsigned

          // ==================== Fixed-Point Types ====================
          new MariaTypeAndExample<>(MariaTypes.decimal, new BigDecimal("12345")),
          new MariaTypeAndExample<>(MariaTypes.decimal, BigDecimal.ZERO), // Edge case: zero
          new MariaTypeAndExample<>(
              MariaTypes.decimal, new BigDecimal("-9999999999")), // Edge case: negative
          new MariaTypeAndExample<>(MariaTypes.numeric, new BigDecimal("99999")),
          new MariaTypeAndExample<>(MariaTypes.decimal(10, 2), new BigDecimal("12345678.90")),
          new MariaTypeAndExample<>(
              MariaTypes.decimal(10, 2), new BigDecimal("0.00")), // Edge case: zero with decimals
          new MariaTypeAndExample<>(
              MariaTypes.decimal(10, 2),
              new BigDecimal("-99999999.99")), // Edge case: large negative
          new MariaTypeAndExample<>(MariaTypes.decimal(10, 5), new BigDecimal("12345.67890")),
          new MariaTypeAndExample<>(
              MariaTypes.decimal(10, 5), new BigDecimal("0.00001")), // Edge case: small value

          // ==================== Floating-Point Types ====================
          new MariaTypeAndExample<>(MariaTypes.float_, 3.14159f).noIdentity(),
          new MariaTypeAndExample<>(MariaTypes.float_, 0.0f).noIdentity(), // Edge case: zero
          new MariaTypeAndExample<>(MariaTypes.float_, 1.0E-38f)
              .noIdentity(), // Edge case: small positive
          new MariaTypeAndExample<>(MariaTypes.double_, 3.141592653589793),
          new MariaTypeAndExample<>(MariaTypes.double_, 0.0), // Edge case: zero
          new MariaTypeAndExample<>(MariaTypes.double_, -3.141592653589793), // Edge case: negative

          // ==================== Boolean Type ====================
          new MariaTypeAndExample<>(MariaTypes.bool, true),
          new MariaTypeAndExample<>(MariaTypes.bool, false),

          // ==================== Bit Types ====================
          // BIT types also have JSON encoding issues
          new MariaTypeAndExample<>(MariaTypes.bit1, true).noJsonRoundtrip(),
          new MariaTypeAndExample<>(MariaTypes.bit1, false)
              .noJsonRoundtrip(), // Edge case: false bit

          // ==================== String Types ====================
          new MariaTypeAndExample<>(MariaTypes.char_(10), "hello"),
          new MariaTypeAndExample<>(MariaTypes.char_(10), ""), // Edge case: empty string
          new MariaTypeAndExample<>(MariaTypes.char_(10), "a"), // Edge case: single char
          new MariaTypeAndExample<>(
              MariaTypes.varchar(255), "Hello, MariaDB! Unicode: \u00e9\u00e8\u00ea \u4e2d\u6587"),
          new MariaTypeAndExample<>(MariaTypes.varchar(255), ""), // Edge case: empty string
          new MariaTypeAndExample<>(
              MariaTypes.varchar(255), "Line1\nLine2\tTabbed"), // Edge case: whitespace
          new MariaTypeAndExample<>(
              MariaTypes.varchar(255), "Quote\"Test'Single\\Back"), // Edge case: special chars
          new MariaTypeAndExample<>(
              MariaTypes.varchar(255),
              "Emoji: \uD83D\uDE00\uD83C\uDF89\uD83D\uDE80"), // Edge case: emoji
          new MariaTypeAndExample<>(MariaTypes.tinytext, "tiny text content"),
          new MariaTypeAndExample<>(MariaTypes.tinytext, ""), // Edge case: empty
          new MariaTypeAndExample<>(
              MariaTypes.text, "Regular text content with special chars: ,.;{}[]-//#\u00ae\u2705"),
          new MariaTypeAndExample<>(MariaTypes.text, ""), // Edge case: empty
          new MariaTypeAndExample<>(MariaTypes.mediumtext, "Medium text can hold up to 16MB"),
          new MariaTypeAndExample<>(MariaTypes.longtext, "Long text can hold up to 4GB"),

          // ==================== Binary Types ====================
          // Note: MariaDB's JSON encoding of binary is lossy - bytes > 127 get corrupted
          // because JSON is UTF-8 and MariaDB outputs raw bytes without proper encoding
          new MariaTypeAndExample<>(MariaTypes.binary(5), new byte[] {0x01, 0x02, 0x03, 0x00, 0x00})
              .noJsonRoundtrip(),
          new MariaTypeAndExample<>(MariaTypes.binary(5), new byte[] {0x00, 0x00, 0x00, 0x00, 0x00})
              .noJsonRoundtrip(), // Edge case: all zeros
          new MariaTypeAndExample<>(
                  MariaTypes.varbinary(255), new byte[] {(byte) 0xFF, 0x00, 0x7F, (byte) 0x80})
              .noJsonRoundtrip(),
          new MariaTypeAndExample<>(MariaTypes.varbinary(255), new byte[] {})
              .noJsonRoundtrip(), // Edge case: empty
          new MariaTypeAndExample<>(MariaTypes.varbinary(255), new byte[] {0x00})
              .noJsonRoundtrip(), // Edge case: single zero byte
          new MariaTypeAndExample<>(MariaTypes.tinyblob, new byte[] {0x01, 0x02, 0x03})
              .noJsonRoundtrip(),
          new MariaTypeAndExample<>(MariaTypes.tinyblob, new byte[] {})
              .noJsonRoundtrip(), // Edge case: empty
          new MariaTypeAndExample<>(
                  MariaTypes.blob, new byte[] {(byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF})
              .noJsonRoundtrip(),
          new MariaTypeAndExample<>(MariaTypes.blob, new byte[] {})
              .noJsonRoundtrip(), // Edge case: empty
          new MariaTypeAndExample<>(
                  MariaTypes.mediumblob, new byte[] {0x00, 0x11, 0x22, 0x33, 0x44, 0x55})
              .noJsonRoundtrip(),
          new MariaTypeAndExample<>(
                  MariaTypes.longblob,
                  new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE})
              .noJsonRoundtrip(),

          // ==================== Date/Time Types ====================
          new MariaTypeAndExample<>(MariaTypes.date, LocalDate.of(2024, 6, 15)),
          new MariaTypeAndExample<>(MariaTypes.date, LocalDate.of(1970, 1, 1)), // Edge case: epoch
          new MariaTypeAndExample<>(
              MariaTypes.date, LocalDate.of(2099, 12, 31)), // Edge case: far future
          new MariaTypeAndExample<>(
              MariaTypes.date, LocalDate.of(1000, 1, 1)), // Edge case: old date
          new MariaTypeAndExample<>(MariaTypes.time, LocalTime.of(14, 30, 45)),
          new MariaTypeAndExample<>(MariaTypes.time, LocalTime.of(0, 0, 0)), // Edge case: midnight
          new MariaTypeAndExample<>(
              MariaTypes.time, LocalTime.of(23, 59, 59)), // Edge case: end of day
          new MariaTypeAndExample<>(MariaTypes.time(3), LocalTime.of(14, 30, 45, 123000000)),
          new MariaTypeAndExample<>(
              MariaTypes.time(6), LocalTime.of(14, 30, 45, 123456000)), // Edge case: microseconds
          new MariaTypeAndExample<>(MariaTypes.datetime, LocalDateTime.of(2024, 6, 15, 14, 30, 45)),
          new MariaTypeAndExample<>(
              MariaTypes.datetime, LocalDateTime.of(1970, 1, 1, 0, 0, 0)), // Edge case: epoch
          new MariaTypeAndExample<>(
              MariaTypes.datetime(6), LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123456000)),
          new MariaTypeAndExample<>(
              MariaTypes.timestamp, LocalDateTime.of(2024, 6, 15, 14, 30, 45)),
          new MariaTypeAndExample<>(
              MariaTypes.timestamp,
              LocalDateTime.of(
                  1971, 1, 1, 0, 0,
                  1)), // Edge case: near epoch (timestamp starts at 1970-01-01 00:00:01)
          new MariaTypeAndExample<>(
              MariaTypes.timestamp(6), LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123456000)),
          new MariaTypeAndExample<>(MariaTypes.year, Year.of(2024)),
          new MariaTypeAndExample<>(MariaTypes.year, Year.of(1901)), // Edge case: min YEAR
          new MariaTypeAndExample<>(MariaTypes.year, Year.of(2155)), // Edge case: max YEAR

          // ==================== ENUM Type ====================
          new MariaTypeAndExample<>(
              MariaTypes.ofEnum("ENUM('RED','GREEN','BLUE')", Color::valueOf), Color.GREEN),
          new MariaTypeAndExample<>(
              MariaTypes.ofEnum("ENUM('RED','GREEN','BLUE')", Color::valueOf),
              Color.RED), // Edge case: first value
          new MariaTypeAndExample<>(
              MariaTypes.ofEnum("ENUM('RED','GREEN','BLUE')", Color::valueOf),
              Color.BLUE), // Edge case: last value

          // ==================== SET Type ====================
          new MariaTypeAndExample<>(
              MariaTypes.set.withTypename("SET('email','sms','push')"),
              MariaSet.of("email", "push")),
          new MariaTypeAndExample<>(
              MariaTypes.set.withTypename("SET('a','b','c')"),
              MariaSet.empty()), // Edge case: empty set
          new MariaTypeAndExample<>(
              MariaTypes.set.withTypename("SET('x','y','z')"),
              MariaSet.of("x", "y", "z")), // Edge case: all values
          new MariaTypeAndExample<>(
              MariaTypes.set.withTypename("SET('only')"),
              MariaSet.of("only")), // Edge case: single value

          // ==================== JSON Type ====================
          new MariaTypeAndExample<>(
                  MariaTypes.json, new Json("{\"name\": \"MariaDB\", \"version\": 10.11}"))
              .noIdentity(),
          new MariaTypeAndExample<>(MariaTypes.json, new Json("[1, 2, 3, \"four\"]")).noIdentity(),
          new MariaTypeAndExample<>(MariaTypes.json, new Json("{}"))
              .noIdentity(), // Edge case: empty object
          new MariaTypeAndExample<>(MariaTypes.json, new Json("[]"))
              .noIdentity(), // Edge case: empty array
          new MariaTypeAndExample<>(MariaTypes.json, new Json("null"))
              .noIdentity(), // Edge case: null
          new MariaTypeAndExample<>(MariaTypes.json, new Json("\"string\""))
              .noIdentity(), // Edge case: string value
          new MariaTypeAndExample<>(MariaTypes.json, new Json("42"))
              .noIdentity(), // Edge case: number value
          new MariaTypeAndExample<>(MariaTypes.json, new Json("true"))
              .noIdentity(), // Edge case: boolean value

          // ==================== JSON-Encoded Row Types ====================
          new MariaTypeAndExample<>(MariaTypes.jsonArrayEncoded(itemCodec), new Item("Widget", 5))
              .noIdentity(),
          new MariaTypeAndExample<>(
                  MariaTypes.jsonArrayEncodedList(itemCodec), List.of(new Item("Widget", 5)))
              .noIdentity(),
          new MariaTypeAndExample<>(
                  MariaTypes.jsonObjectEncoded(namedItemCodec), new Item("Widget", 5))
              .noIdentity(),
          new MariaTypeAndExample<>(
                  MariaTypes.jsonObjectEncodedList(namedItemCodec), List.of(new Item("Widget", 5)))
              .noIdentity(),

          // ==================== Network Types (MariaDB 10.10+) ====================
          new MariaTypeAndExample<>(MariaTypes.inet4, Inet4.parse("192.168.1.100")),
          new MariaTypeAndExample<>(MariaTypes.inet4, Inet4.parse("10.0.0.1")),
          new MariaTypeAndExample<>(
              MariaTypes.inet4, Inet4.parse("0.0.0.0")), // Edge case: any address
          new MariaTypeAndExample<>(
              MariaTypes.inet4, Inet4.parse("255.255.255.255")), // Edge case: broadcast
          new MariaTypeAndExample<>(
              MariaTypes.inet4, Inet4.parse("127.0.0.1")), // Edge case: localhost
          new MariaTypeAndExample<>(MariaTypes.inet6, Inet6.parse("2001:db8::1")),
          new MariaTypeAndExample<>(
              MariaTypes.inet6, Inet6.parse("::ffff:192.168.1.1")), // IPv4-mapped
          new MariaTypeAndExample<>(MariaTypes.inet6, Inet6.parse("::")), // Edge case: any address
          new MariaTypeAndExample<>(MariaTypes.inet6, Inet6.parse("::1")), // Edge case: localhost
          new MariaTypeAndExample<>(
              MariaTypes.inet6, Inet6.parse("fe80::1")) // Edge case: link-local
          );

  static <T> T withConnection(SqlFunction<Connection, T> f) {
    return Containers.mariadbTransactor().execute(f);
  }

  @Test
  public void test() {
    System.out.println("Testing MariaDB type codecs...\n");

    // Test JSON roundtrip first (no database connection needed) - parallel
    System.out.println("=== JSON Roundtrip Tests (parallel) ===");
    All.parallelStream().forEach(MariaTypeTest::testJsonRoundtrip);
    System.out.println();

    // Run native type and JSON DB tests in parallel
    System.out.println("=== Native Type + JSON DB Roundtrip Tests (parallel) ===");
    var failures =
        All.parallelStream()
            .flatMap(
                t -> {
                  var errors = new java.util.ArrayList<String>();

                  // Native type roundtrip test
                  try {
                    withConnection(
                        conn -> {
                          testCase(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    errors.add(
                        "Native test FAILED "
                            + t.type.typename().sqlType()
                            + ": "
                            + e.getMessage());
                  }

                  // JSON DB roundtrip test
                  if (t.jsonRoundtripWorks()) {
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

    // Callable roundtrip tests - test stored procedure IN/OUT parameter roundtrip
    System.out.println("\n=== Callable Roundtrip Tests (parallel) ===");
    var callFailures =
        All.stream()
            .collect(
                java.util.stream.Collectors.toMap(
                    t -> t.type.typename().sqlType(), t -> t, (a, b) -> a))
            .values()
            .parallelStream()
            .flatMap(
                t -> {
                  try {
                    withConnection(
                        conn -> {
                          testCallableRoundtrip(conn, t);
                          return null;
                        });
                    return java.util.stream.Stream.<String>empty();
                  } catch (Exception e) {
                    if (e.getMessage() != null
                        && e.getMessage()
                            .contains("does not support stored procedure OUT parameters")) {
                      return java.util.stream.Stream.empty();
                    }
                    return java.util.stream.Stream.of(
                        "Callable test FAILED "
                            + t.type.typename().sqlType()
                            + ": "
                            + e.getMessage());
                  }
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
                  var errors = new java.util.ArrayList<String>();
                  try {
                    withConnection(
                        conn -> {
                          testQueryAnalysis(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    errors.add(
                        "Analysis FAILED " + t.type.typename().sqlType() + ": " + e.getMessage());
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

    var allFailures = new java.util.ArrayList<String>();
    allFailures.addAll(failures);
    allFailures.addAll(callFailures);
    allFailures.addAll(analysisFailures);

    System.out.println("\n=====================================");
    if (allFailures.isEmpty()) {
      System.out.println("All tests passed!");
    } else {
      allFailures.forEach(System.out::println);
      throw new RuntimeException(allFailures.size() + " tests failed");
    }
    System.out.println("=====================================");
  }

  static <A> void testQueryAnalysis(Connection conn, MariaTypeAndExample<A> t) throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("qa");
    conn.createStatement().execute("CREATE TEMPORARY TABLE " + tableName + " (v " + sqlType + ")");
    try {
      RowCodec<A> parser = RowCodec.of(t.type);
      Fragment fragment = Fragment.of("SELECT v FROM " + tableName);
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();
      if (!analysis.succeeded()) {
        throw new RuntimeException(
            "Query analysis failed for " + sqlType + ":\n" + analysis.report());
      }
    } finally {
      conn.createStatement().execute("DROP TEMPORARY TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void testQueryAnalysisWithParam(Connection conn, MariaTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("qap");
    conn.createStatement()
        .execute("CREATE TEMPORARY TABLE " + tableName + " (v " + sqlType + " NOT NULL)");
    try {
      RowCodec<A> parser = RowCodec.of(t.type);
      Fragment fragment =
          Fragment.of("SELECT v FROM " + tableName + " WHERE v = ").value(t.type, t.example);
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();
      if (!analysis.succeeded()) {
        throw new RuntimeException(
            "Param analysis failed for " + sqlType + ":\n" + analysis.report());
      }
    } finally {
      conn.createStatement().execute("DROP TEMPORARY TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void testJsonRoundtrip(MariaTypeAndExample<A> t) {
    try {
      MariaJson<A> jsonCodec = t.type.mariaJson();
      A original = t.example;

      // Test toJson -> encode -> parse -> fromJson roundtrip (in-memory)
      JsonValue jsonValue = jsonCodec.toJson(original);
      String encoded = jsonValue.encode();
      JsonValue parsed = JsonValue.parse(encoded);
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

  // Test JSON roundtrip through the database - simulates MULTISET behavior
  // Insert value into native column, read back as JSON, parse back to value
  static <A> void testJsonDbRoundtrip(Connection conn, MariaTypeAndExample<A> t)
      throws SQLException {
    MariaJson<A> jsonCodec = t.type.mariaJson();
    A original = t.example;
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("test_json_rt");

    // Create temp table with the native type column
    conn.createStatement().execute("CREATE TEMPORARY TABLE " + tableName + " (v " + sqlType + ")");

    try {
      // Insert value using native type
      var insert = conn.prepareStatement("INSERT INTO " + tableName + " (v) VALUES (?)");
      t.type.write().set(insert, 1, original);
      insert.execute();
      insert.close();

      // Select back as JSON using JSON_OBJECT - this is what MULTISET does
      var select = conn.prepareStatement("SELECT JSON_OBJECT('v', v) FROM " + tableName);
      select.execute();
      var rs = select.getResultSet();

      if (!rs.next()) {
        throw new RuntimeException("No rows returned");
      }

      // Read the JSON string back from the database
      String jsonFromDb = rs.getString(1);
      select.close();

      // Parse the JSON object and extract 'v' field
      JsonValue parsedFromDb = JsonValue.parse(jsonFromDb);
      JsonValue fieldValue = ((JsonValue.JObject) parsedFromDb).get("v");
      A decoded = jsonCodec.fromJson(fieldValue);

      if (t.hasIdentity && !areEqual(decoded, original)) {
        throw new RuntimeException(
            "JSON DB roundtrip failed for "
                + sqlType
                + ": expected '"
                + format(original)
                + "' but got '"
                + format(decoded)
                + "'");
      }
    } finally {
      conn.createStatement().execute("DROP TEMPORARY TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void batchInsert(Connection conn, DbType<A> type, String tableName, A value)
      throws SQLException {
    RowCodecNamed<A> parser =
        RowCodec.<A>namedBuilder()
            .field("v", type, java.util.function.Function.identity())
            .build(java.util.function.Function.identity());
    Fragment.of("INSERT INTO " + tableName + " (v) VALUES (")
        .paramRow(parser)
        .append(")")
        .update()
        .onMany(List.of(value).iterator())
        .run(conn);
  }

  static <A> void testCase(Connection conn, MariaTypeAndExample<A> t) throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("test_table");

    // Create temp table
    conn.createStatement().execute("CREATE TEMPORARY TABLE " + tableName + " (v " + sqlType + ")");

    try {
      A expected = t.example;
      batchInsert(conn, t.type, tableName, expected);

      // Select and verify
      final PreparedStatement select;
      if (t.hasIdentity) {
        select = conn.prepareStatement("SELECT v, NULL FROM " + tableName + " WHERE v = ?");
        t.type.write().set(select, 1, expected);
      } else {
        select = conn.prepareStatement("SELECT v, NULL FROM " + tableName);
      }

      select.execute();
      var rs = select.getResultSet();

      if (!rs.next()) {
        throw new RuntimeException("No rows returned");
      }

      // Read the value
      A actual = t.type.read().read(rs, 1);
      // Read the null value using opt()
      Optional<A> actualNull = t.type.opt().read().read(rs, 2);

      select.close();

      assertEquals(actual, expected, "value mismatch");
      assertEquals(actualNull, Optional.empty(), "null value mismatch");

    } finally {
      // Drop temp table
      conn.createStatement().execute("DROP TEMPORARY TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void testCallableRoundtrip(Connection conn, MariaTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String procName = uniqueTableName("test_proc");

    var proc = DbProcedure.define(procName).input(t.type).out(t.type).build();

    conn.createStatement()
        .execute(
            "CREATE PROCEDURE "
                + procName
                + "(IN p_in "
                + sqlType
                + ", OUT p_out "
                + sqlType
                + ") BEGIN SET p_out = p_in; END");

    try {
      A result = proc.call(t.example).run(conn);

      System.out.println(
          "Callable roundtrip " + sqlType + ": " + format(t.example) + " -> " + format(result));

      if (t.hasIdentity() && !areEqual(result, t.example)) {
        throw new RuntimeException(
            "Callable roundtrip failed for "
                + sqlType
                + ": expected '"
                + format(t.example)
                + "' but got '"
                + format(result)
                + "'");
      }
    } finally {
      conn.createStatement().execute("DROP PROCEDURE IF EXISTS " + procName);
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
    if (expected instanceof Object[]) {
      return Arrays.deepEquals((Object[]) actual, (Object[]) expected);
    }

    // For spatial types, compare string representations
    if (expected instanceof org.mariadb.jdbc.type.Geometry) {
      return actual.toString().equals(expected.toString());
    }

    return actual.equals(expected);
  }

  static <A> String format(A a) {
    if (a == null) return "null";
    if (a instanceof byte[]) {
      return bytesToHex((byte[]) a);
    }
    if (a instanceof Object[]) {
      return Arrays.deepToString((Object[]) a);
    }
    return a.toString();
  }

  static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    for (int i = 0; i < bytes.length; i++) {
      if (i > 0) sb.append(", ");
      sb.append(String.format("0x%02X", bytes[i]));
    }
    sb.append("]");
    return sb.toString();
  }
}
