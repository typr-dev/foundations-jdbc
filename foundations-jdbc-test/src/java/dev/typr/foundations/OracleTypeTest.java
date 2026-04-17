package dev.typr.foundations;

import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import dev.typr.foundations.data.NonEmptyString;
import dev.typr.foundations.data.OracleIntervalDS;
import dev.typr.foundations.data.OracleIntervalYM;
import dev.typr.foundations.data.PaddedString;
import dev.typr.foundations.hikari.PooledDataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

/** Tests for Oracle type codecs. Tests all types defined in OracleTypes. */
public class OracleTypeTest {

  private static final AtomicInteger tableCounter = new AtomicInteger(0);

  // Connection pool from Testcontainers
  private static PooledDataSource pool() {
    return Containers.oraclePool();
  }

  static {
    java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("GMT+03:00"));
  }

  private static String uniqueTableName(String prefix) {
    return prefix + "_" + tableCounter.incrementAndGet();
  }

  // ═══════════════════════════════════════════════════════════════════════════
  // Test Data Types for Object-Relational Types
  // ═══════════════════════════════════════════════════════════════════════════

  /** Address - corresponds to Oracle type address_t */
  record Coordinates(BigDecimal latitude, BigDecimal longitude) {}

  record Address(String street, String city, Coordinates location) {}

  // Helper to build COORDINATES_T type
  static OracleType<Coordinates> coordinatesType() {
    return OracleTypes.compositeOf(
        "COORDINATES_T",
        RowCodec.<Coordinates>namedBuilder()
            .field("LATITUDE", OracleTypes.numberOf(9, 6), Coordinates::latitude)
            .field("LONGITUDE", OracleTypes.numberOf(9, 6), Coordinates::longitude)
            .build(Coordinates::new));
  }

  // Helper to build ADDRESS_T type (with nested COORDINATES_T)
  static OracleType<Address> addressType() {
    return OracleTypes.compositeOf(
        "ADDRESS_T",
        RowCodec.<Address>namedBuilder()
            .field("STREET", OracleTypes.varchar2Of(100), Address::street)
            .field("CITY", OracleTypes.varchar2Of(50), Address::city)
            .field("LOCATION", coordinatesType(), Address::location)
            .build(Address::new));
  }

  // Example coordinates
  static Coordinates coords(String lat, String lon) {
    return new Coordinates(new BigDecimal(lat), new BigDecimal(lon));
  }

  /** OrderItem - corresponds to Oracle type order_item_t */
  record OrderItem(Long productId, Integer quantity) {}

  record OracleItem(String name, int quantity) {}

  static RowCodec<OracleItem> oracleItemCodec =
      RowCodec.<OracleItem>builder()
          .field(OracleTypes.varchar2Of(100), OracleItem::name)
          .field(OracleTypes.numberInt, OracleItem::quantity)
          .build(OracleItem::new);

  static RowCodecNamed<OracleItem> namedOracleItemCodec =
      RowCodec.<OracleItem>namedBuilder()
          .field("name", OracleTypes.varchar2Of(100), OracleItem::name)
          .field("quantity", OracleTypes.numberInt, OracleItem::quantity)
          .build(OracleItem::new);

  /** AllTypesStruct - comprehensive struct containing all Oracle types (NOT NULL fields) */
  record AllTypesStruct(
      String varcharField,
      String nvarcharField,
      String charField,
      String ncharField,
      BigDecimal numberField,
      Integer numberIntField,
      Long numberLongField,
      Float binaryFloatField,
      Double binaryDoubleField,
      LocalDateTime dateField,
      LocalDateTime timestampField,
      ZonedDateTime timestampTzField,
      Instant timestampLtzField,
      OracleIntervalYM intervalYmField,
      OracleIntervalDS intervalDsField,
      Address nestedObjectField,
      List<String> varrayField) {}

  /** AllTypesStructOptional - comprehensive struct containing all Oracle types (all nullable) */
  record AllTypesStructOptional(
      Optional<String> varcharField,
      Optional<String> nvarcharField,
      Optional<String> charField,
      Optional<String> ncharField,
      Optional<BigDecimal> numberField,
      Optional<Integer> numberIntField,
      Optional<Long> numberLongField,
      Optional<Float> binaryFloatField,
      Optional<Double> binaryDoubleField,
      Optional<LocalDateTime> dateField,
      Optional<LocalDateTime> timestampField,
      Optional<ZonedDateTime> timestampTzField,
      Optional<Instant> timestampLtzField,
      Optional<OracleIntervalYM> intervalYmField,
      Optional<OracleIntervalDS> intervalDsField,
      Optional<Address> nestedObjectField,
      Optional<List<String>> varrayField) {}

  /**
   * AllTypesStructNoLobs - Oracle types without LOBs (for VARRAY compatibility) Oracle restriction:
   * VARRAYs cannot contain structs with embedded LOB types or nested tables
   */
  record AllTypesStructNoLobs(
      String varcharField,
      String nvarcharField,
      String charField,
      String ncharField,
      BigDecimal numberField,
      Integer numberIntField,
      Long numberLongField,
      Float binaryFloatField,
      Double binaryDoubleField,
      LocalDateTime dateField,
      LocalDateTime timestampField,
      ZonedDateTime timestampTzField,
      Instant timestampLtzField,
      OracleIntervalYM intervalYmField,
      OracleIntervalDS intervalDsField,
      Address nestedObjectField,
      List<String> varrayField) {}

  /** AllTypesStructNoLobsOptional - Optional variant without LOBs (for VARRAY compatibility) */
  record AllTypesStructNoLobsOptional(
      Optional<String> varcharField,
      Optional<String> nvarcharField,
      Optional<String> charField,
      Optional<String> ncharField,
      Optional<BigDecimal> numberField,
      Optional<Integer> numberIntField,
      Optional<Long> numberLongField,
      Optional<Float> binaryFloatField,
      Optional<Double> binaryDoubleField,
      Optional<LocalDateTime> dateField,
      Optional<LocalDateTime> timestampField,
      Optional<ZonedDateTime> timestampTzField,
      Optional<Instant> timestampLtzField,
      Optional<OracleIntervalYM> intervalYmField,
      Optional<OracleIntervalDS> intervalDsField,
      Optional<Address> nestedObjectField,
      Optional<List<String>> varrayField) {}

  // ═══════════════════════════════════════════════════════════════════════════

  record OracleTypeAndExample<A>(
      OracleType<A> type,
      A example,
      A expectedRoundtrip, // Nullable - the expected value after roundtrip (may be null for SQL
      // NULL)
      boolean useExpectedRoundtrip, // If true, use expectedRoundtrip; if false, use example
      boolean hasIdentity,
      boolean streamingWorks,
      boolean jsonRoundtripWorks,
      boolean supportsComposite, // If true, auto-derive OBJECT/VARRAY/NESTED TABLE wrappers
      List<String>
          setupSql // Optional SQL statements to run before test (for type definitions, etc.)
      ) {
    public OracleTypeAndExample(OracleType<A> type, A example) {
      this(type, example, null, false, true, true, true, true, List.of());
    }

    public OracleTypeAndExample(OracleType<A> type, A example, A expectedRoundtrip) {
      this(type, example, expectedRoundtrip, true, true, true, true, true, List.of());
    }

    public OracleTypeAndExample(OracleType<A> type, A example, List<String> setupSql) {
      this(type, example, null, false, true, true, true, true, setupSql);
    }

    public OracleTypeAndExample<A> noStreaming() {
      return new OracleTypeAndExample<>(
          type,
          example,
          expectedRoundtrip,
          useExpectedRoundtrip,
          hasIdentity,
          false,
          jsonRoundtripWorks,
          supportsComposite,
          setupSql);
    }

    public OracleTypeAndExample<A> noIdentity() {
      return new OracleTypeAndExample<>(
          type,
          example,
          expectedRoundtrip,
          useExpectedRoundtrip,
          false,
          streamingWorks,
          jsonRoundtripWorks,
          supportsComposite,
          setupSql);
    }

    public OracleTypeAndExample<A> noJsonRoundtrip() {
      return new OracleTypeAndExample<>(
          type,
          example,
          expectedRoundtrip,
          useExpectedRoundtrip,
          hasIdentity,
          streamingWorks,
          false,
          supportsComposite,
          setupSql);
    }

    public OracleTypeAndExample<A> noComposite() {
      return new OracleTypeAndExample<>(
          type,
          example,
          expectedRoundtrip,
          useExpectedRoundtrip,
          hasIdentity,
          streamingWorks,
          jsonRoundtripWorks,
          false,
          setupSql);
    }

    public A expected() {
      return useExpectedRoundtrip ? expectedRoundtrip : example;
    }
  }

  List<OracleTypeAndExample<?>> All =
      List.<OracleTypeAndExample<?>>of(
          // ═══════════════════════════════════════════════════════════════════════════
          // Numeric Types
          // ═══════════════════════════════════════════════════════════════════════════

          // NUMBER - universal numeric type
          new OracleTypeAndExample<>(OracleTypes.number, new BigDecimal("12345.6789")),
          new OracleTypeAndExample<>(OracleTypes.number, BigDecimal.ZERO), // Edge case: zero
          new OracleTypeAndExample<>(
              OracleTypes.number, new BigDecimal("-9999999999.999999")), // Edge case: negative
          new OracleTypeAndExample<>(
              OracleTypes.number, new BigDecimal("0.00000001")), // Edge case: small value

          // NUMBER as Integer
          new OracleTypeAndExample<>(OracleTypes.numberInt, 42),
          new OracleTypeAndExample<>(OracleTypes.numberInt, Integer.MIN_VALUE),
          new OracleTypeAndExample<>(OracleTypes.numberInt, Integer.MAX_VALUE),
          new OracleTypeAndExample<>(OracleTypes.numberInt, 0),

          // NUMBER as Long
          new OracleTypeAndExample<>(OracleTypes.numberLong, 424242424242L),
          new OracleTypeAndExample<>(OracleTypes.numberLong, Long.MIN_VALUE),
          new OracleTypeAndExample<>(OracleTypes.numberLong, Long.MAX_VALUE),
          new OracleTypeAndExample<>(OracleTypes.numberLong, 0L),

          // NUMBER with precision and scale
          new OracleTypeAndExample<>(OracleTypes.numberOf(10, 2), new BigDecimal("12345678.90")),
          new OracleTypeAndExample<>(OracleTypes.numberOf(10, 2), new BigDecimal("-99999999.99")),
          new OracleTypeAndExample<>(
              OracleTypes.numberOf(38, 10),
              new BigDecimal("1234567890123456789012345678.1234567890")),

          // BINARY_FLOAT - 32-bit IEEE 754
          new OracleTypeAndExample<>(OracleTypes.binaryFloat, 3.14159f),
          new OracleTypeAndExample<>(OracleTypes.binaryFloat, 0.0f),
          new OracleTypeAndExample<>(OracleTypes.binaryFloat, Float.MIN_VALUE),
          new OracleTypeAndExample<>(OracleTypes.binaryFloat, Float.MAX_VALUE),
          new OracleTypeAndExample<>(OracleTypes.binaryFloat, -1.5E10f),

          // BINARY_DOUBLE - 64-bit IEEE 754
          // Oracle supports range: 1.0E-130 to 1.0E126 (excluding zero)
          new OracleTypeAndExample<>(OracleTypes.binaryDouble, 3.141592653589793),
          new OracleTypeAndExample<>(OracleTypes.binaryDouble, 0.0),
          new OracleTypeAndExample<>(
              OracleTypes.binaryDouble, 1.0E-129), // Near Oracle's min positive value
          new OracleTypeAndExample<>(OracleTypes.binaryDouble, -2.5E100),

          // FLOAT (ANSI type mapped to NUMBER)
          new OracleTypeAndExample<>(OracleTypes.float_, 42.42),
          new OracleTypeAndExample<>(OracleTypes.float_Of(63), 123.456), // REAL equivalent

          // ═══════════════════════════════════════════════════════════════════════════
          // Character Types
          // ═══════════════════════════════════════════════════════════════════════════

          // VARCHAR2
          new OracleTypeAndExample<>(OracleTypes.varchar2Of(100), "Hello, Oracle!"),
          new OracleTypeAndExample<>(OracleTypes.varchar2Of(100), "", (String) null)
              .noJsonRoundtrip(), // Oracle quirk: empty string → NULL
          new OracleTypeAndExample<>(
              OracleTypes.varchar2Of(100), "Unicode: \u00e9\u00e8\u00ea \u4e2d\u6587"),
          new OracleTypeAndExample<>(OracleTypes.varchar2Of(100), "Line1\nLine2\tTabbed"),
          new OracleTypeAndExample<>(OracleTypes.varchar2Of(100), "Quote\"Test'Single"),
          new OracleTypeAndExample<>(OracleTypes.varchar2Of(100), "Special chars: ,.;{}[]-//#"),

          // VARCHAR2 with NonEmptyString (for NOT NULL columns)
          new OracleTypeAndExample<>(
              OracleTypes.varchar2NonEmpty(100), NonEmptyString.force("NonEmpty VARCHAR2")),
          new OracleTypeAndExample<>(
              OracleTypes.varchar2NonEmpty(100), NonEmptyString.force("Test \u4e2d\u6587")),

          // CHAR (fixed-length, blank-padded)
          new OracleTypeAndExample<>(
              OracleTypes.char_Of(10), "hello     "), // Note: CHAR pads with spaces
          new OracleTypeAndExample<>(
              OracleTypes.char_Of(5), "abc  "), // May be trimmed on comparison

          // CHAR with PaddedString (for NOT NULL columns)
          new OracleTypeAndExample<>(OracleTypes.charPadded(10), PaddedString.force("hello", 10)),
          new OracleTypeAndExample<>(
              OracleTypes.charPadded(20), PaddedString.force("padded test", 20)),

          // NVARCHAR2 (National character set)
          new OracleTypeAndExample<>(
              OracleTypes.nvarchar2Of(100), "Unicode text: \u0391\u0392\u0393"),
          new OracleTypeAndExample<>(
              OracleTypes.nvarchar2Of(100), "Emoji: \uD83D\uDE00\uD83C\uDF89"),

          // NVARCHAR2 with NonEmptyString (for NOT NULL columns)
          new OracleTypeAndExample<>(
              OracleTypes.nvarchar2NonEmpty(100), NonEmptyString.force("NonEmpty NVARCHAR2")),

          // NCHAR
          new OracleTypeAndExample<>(OracleTypes.ncharOf(10), "test      "),

          // NCHAR with PaddedString (for NOT NULL columns)
          new OracleTypeAndExample<>(
              OracleTypes.ncharPadded(15), PaddedString.force("nchar test", 15)),

          // CLOB - Character Large Object (cannot be used as comparison key)
          new OracleTypeAndExample<>(
                  OracleTypes.clob, "This is a CLOB text that could be very large.")
              .noStreaming()
              .noIdentity(),
          new OracleTypeAndExample<>(OracleTypes.clob, "Short CLOB").noStreaming().noIdentity(),

          // CLOB with NonEmptyString (for NOT NULL columns - cannot be used as comparison key)
          new OracleTypeAndExample<>(
                  OracleTypes.clobNonEmpty, NonEmptyString.force("NonEmpty CLOB text"))
              .noStreaming()
              .noIdentity(),

          // NCLOB - National CLOB (cannot be used as comparison key)
          new OracleTypeAndExample<>(OracleTypes.nclob, "National CLOB with \u4e2d\u6587")
              .noStreaming()
              .noIdentity(),

          // NCLOB with NonEmptyString (for NOT NULL columns - cannot be used as comparison key)
          new OracleTypeAndExample<>(
                  OracleTypes.nclobNonEmpty, NonEmptyString.force("NonEmpty NCLOB \u4e2d\u6587"))
              .noStreaming()
              .noIdentity(),

          // ═══════════════════════════════════════════════════════════════════════════
          // Binary Types
          // ═══════════════════════════════════════════════════════════════════════════

          // RAW
          new OracleTypeAndExample<>(
              OracleTypes.rawOf(100), new byte[] {0x01, 0x02, 0x03, (byte) 0xFF}),
          new OracleTypeAndExample<>(OracleTypes.rawOf(100), new byte[] {}, (byte[]) null)
              .noJsonRoundtrip(), // Oracle quirk: empty byte array → NULL
          new OracleTypeAndExample<>(
              OracleTypes.rawOf(100), new byte[] {0x00, 0x00, 0x00}), // Edge case: zeros
          new OracleTypeAndExample<>(
              OracleTypes.rawOf(100),
              new byte[] {(byte) 0xDE, (byte) 0xAD, (byte) 0xBE, (byte) 0xEF}),

          // BLOB - Binary Large Object (cannot be used as comparison key)
          new OracleTypeAndExample<>(
                  OracleTypes.blob, new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE})
              .noStreaming()
              .noIdentity(),
          new OracleTypeAndExample<>(OracleTypes.blob, new byte[] {})
              .noStreaming()
              .noIdentity(), // Edge case: empty
          new OracleTypeAndExample<>(
                  OracleTypes.blob, new byte[] {0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77})
              .noStreaming()
              .noIdentity(),

          // ═══════════════════════════════════════════════════════════════════════════
          // Date/Time Types
          // ═══════════════════════════════════════════════════════════════════════════

          // DATE (includes time in Oracle!)
          new OracleTypeAndExample<>(OracleTypes.date, LocalDateTime.of(2024, 6, 15, 14, 30, 45)),
          new OracleTypeAndExample<>(
              OracleTypes.date, LocalDateTime.of(1970, 1, 1, 0, 0, 0)), // Edge case: epoch
          new OracleTypeAndExample<>(
              OracleTypes.date,
              LocalDateTime.of(2099, 12, 31, 23, 59, 59)), // Edge case: far future
          new OracleTypeAndExample<>(
              OracleTypes.date, LocalDateTime.of(1, 1, 1, 0, 0, 0)), // Edge case: very old

          // TIMESTAMP
          new OracleTypeAndExample<>(
              OracleTypes.timestamp, LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123456000)),
          new OracleTypeAndExample<>(
              OracleTypes.timestampOf(6), LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123456000)),
          new OracleTypeAndExample<>(
              OracleTypes.timestampOf(9), LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123456789)),
          new OracleTypeAndExample<>(
              OracleTypes.timestamp, LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0)), // Edge case: epoch

          // TIMESTAMP WITH TIME ZONE — fixed offsets
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithTimeZone,
              ZonedDateTime.of(2024, 6, 15, 14, 30, 45, 0, ZoneOffset.ofHours(2))),
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithTimeZone,
              ZonedDateTime.of(2024, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)),
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithTimeZone,
              ZonedDateTime.of(
                  2024, 6, 15, 14, 30, 45, 123456000, ZoneOffset.ofHoursMinutes(-5, -30))),

          // TIMESTAMP WITH TIME ZONE — named zone regions (the whole point of ZonedDateTime
          // vs OffsetDateTime — these would lose their region identity under OffsetDateTime
          // and freeze to whatever offset was in effect at the moment).
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithTimeZone,
              ZonedDateTime.of(
                  2024, 1, 15, 10, 30, 0, 0, java.time.ZoneId.of("America/Los_Angeles"))), // PST
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithTimeZone,
              ZonedDateTime.of(
                  2024, 7, 15, 10, 30, 0, 0, java.time.ZoneId.of("America/Los_Angeles"))), // PDT
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithTimeZone,
              ZonedDateTime.of(2024, 6, 15, 9, 0, 0, 0, java.time.ZoneId.of("Europe/Berlin"))),
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithTimeZone,
              ZonedDateTime.of(2024, 3, 10, 15, 0, 0, 0, java.time.ZoneId.of("Asia/Tokyo"))),
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithTimeZone,
              ZonedDateTime.of(2024, 12, 31, 23, 59, 59, 999000000, java.time.ZoneId.of("UTC"))),
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithTimeZone,
              ZonedDateTime.of(2024, 10, 20, 6, 15, 0, 0, java.time.ZoneId.of("Australia/Sydney"))),

          // TIMESTAMP WITH LOCAL TIME ZONE
          new OracleTypeAndExample<>(
              OracleTypes.timestampWithLocalTimeZone, Instant.parse("2024-06-15T11:30:45Z")),

          // INTERVAL YEAR TO MONTH - Now using OracleIntervalYM class (parses both Oracle and
          // ISO-8601 formats)
          new OracleTypeAndExample<>(
              OracleTypes.intervalYearToMonth, new OracleIntervalYM(2, 5)), // 2 years, 5 months
          new OracleTypeAndExample<>(
              OracleTypes.intervalYearToMonth,
              new OracleIntervalYM(-1, -6)), // negative: -1 year -6 months
          new OracleTypeAndExample<>(
              OracleTypes.intervalYearToMonth, new OracleIntervalYM(0, 0)), // zero

          // INTERVAL DAY TO SECOND - Now using OracleIntervalDS class (parses both Oracle and
          // ISO-8601 formats)
          new OracleTypeAndExample<>(
              OracleTypes.intervalDayToSecond,
              new OracleIntervalDS(3, 14, 30, 45, 123456000)), // 3 days 14:30:45.123456
          new OracleTypeAndExample<>(
              OracleTypes.intervalDayToSecond,
              new OracleIntervalDS(-1, 0, 0, 0, 0)), // negative: -1 day
          new OracleTypeAndExample<>(
              OracleTypes.intervalDayToSecond, new OracleIntervalDS(0, 0, 0, 0, 0)), // zero

          // ═══════════════════════════════════════════════════════════════════════════
          // ROWID Types
          // ═══════════════════════════════════════════════════════════════════════════

          // Note: ROWID values are generated by Oracle and cannot be inserted directly
          // We test with mock values that follow the format but may not represent real rows

          // ═══════════════════════════════════════════════════════════════════════════
          // JSON Type (Oracle 21c+)
          // Note: JSON type doesn't support equality comparisons in WHERE clauses
          // ═══════════════════════════════════════════════════════════════════════════

          new OracleTypeAndExample<>(
                  OracleTypes.json, new Json("{\"name\": \"Oracle\", \"version\": 23}"))
              .noIdentity(),
          new OracleTypeAndExample<>(OracleTypes.json, new Json("[1, 2, 3, \"four\"]"))
              .noIdentity(),
          new OracleTypeAndExample<>(OracleTypes.json, new Json("{}"))
              .noIdentity(), // Edge case: empty object
          new OracleTypeAndExample<>(OracleTypes.json, new Json("[]"))
              .noIdentity(), // Edge case: empty array
          new OracleTypeAndExample<>(OracleTypes.json, new Json("null"))
              .noIdentity(), // Edge case: null
          new OracleTypeAndExample<>(OracleTypes.json, new Json("\"string\""))
              .noIdentity(), // Edge case: string
          new OracleTypeAndExample<>(OracleTypes.json, new Json("42"))
              .noIdentity(), // Edge case: number
          new OracleTypeAndExample<>(OracleTypes.json, new Json("true"))
              .noIdentity(), // Edge case: boolean

          // ═══════════════════════════════════════════════════════════════════════════
          // JSON-Encoded Row Types
          // ═══════════════════════════════════════════════════════════════════════════

          new OracleTypeAndExample<>(
                  OracleTypes.jsonArrayEncoded(oracleItemCodec), new OracleItem("Widget", 5))
              .noIdentity(),
          new OracleTypeAndExample<>(
                  OracleTypes.jsonArrayEncodedList(oracleItemCodec),
                  List.of(new OracleItem("Widget", 5)))
              .noIdentity(),
          new OracleTypeAndExample<>(
                  OracleTypes.jsonObjectEncoded(namedOracleItemCodec), new OracleItem("Widget", 5))
              .noIdentity(),
          new OracleTypeAndExample<>(
                  OracleTypes.jsonObjectEncodedList(namedOracleItemCodec),
                  List.of(new OracleItem("Widget", 5)))
              .noIdentity(),

          // ═══════════════════════════════════════════════════════════════════════════
          // Boolean Type (Oracle 23c+ native, or NUMBER(1) convention)
          // ═══════════════════════════════════════════════════════════════════════════

          // Native BOOLEAN (23c+) - comment out if using older Oracle
          // new OracleTypeAndExample<>(OracleTypes.boolean_, true),
          // new OracleTypeAndExample<>(OracleTypes.boolean_, false),

          // NUMBER(1) as Boolean (traditional approach)
          new OracleTypeAndExample<>(OracleTypes.numberAsBoolean, true),
          new OracleTypeAndExample<>(OracleTypes.numberAsBoolean, false),

          // ═══════════════════════════════════════════════════════════════════════════
          // Object-Relational Types (User-Defined Types)
          // ═══════════════════════════════════════════════════════════════════════════

          // OBJECT TYPE example - address_t (with nested coordinates_t)
          new OracleTypeAndExample<>(
              addressType(),
              new Address("123 Main St", "San Francisco", coords("37.774929", "-122.419418")),
              List.of(
                  """
                  CREATE OR REPLACE TYPE COORDINATES_T AS OBJECT (
                    LATITUDE NUMBER(9,6),
                    LONGITUDE NUMBER(9,6)
                  )
                  """,
                  """
                  CREATE OR REPLACE TYPE ADDRESS_T AS OBJECT (
                    STREET VARCHAR2(100),
                    CITY VARCHAR2(50),
                    LOCATION COORDINATES_T
                  )
                  """)),

          // VARRAY example - phone_list (max 5 elements) - cannot be used as comparison key
          new OracleTypeAndExample<>(
              OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2Of(20)),
              List.of("555-1234", "555-5678", "555-9999"),
              null,
              false,
              false,
              true,
              true,
              false, // supportsComposite — already composite
              List.of("CREATE OR REPLACE TYPE PHONE_LIST AS VARRAY(5) OF VARCHAR2(20)")),

          // VARRAY edge case - single element
          new OracleTypeAndExample<>(
              OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2Of(20)),
              List.of("555-0000"),
              null,
              false,
              false,
              true,
              true,
              false, // supportsComposite — already composite
              List.of("CREATE OR REPLACE TYPE PHONE_LIST AS VARRAY(5) OF VARCHAR2(20)")),

          // VARRAY edge case - max size (5 elements)
          new OracleTypeAndExample<>(
              OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2Of(20)),
              List.of("555-1111", "555-2222", "555-3333", "555-4444", "555-5555"),
              null,
              false,
              false,
              true,
              true,
              false, // supportsComposite — already composite
              List.of("CREATE OR REPLACE TYPE PHONE_LIST AS VARRAY(5) OF VARCHAR2(20)")),

          // NESTED TABLE example - order_items_t with nested OBJECT type
          new OracleTypeAndExample<>(
                  OracleNestedTable.of(
                      "ORDER_ITEMS_T",
                      OracleTypes.compositeOf(
                          "ORDER_ITEM_T",
                          RowCodec.<OrderItem>namedBuilder()
                              .field("PRODUCT_ID", OracleTypes.numberLong, OrderItem::productId)
                              .field("QUANTITY", OracleTypes.numberInt, OrderItem::quantity)
                              .build(OrderItem::new))),
                  List.of(new OrderItem(101L, 2), new OrderItem(202L, 5), new OrderItem(303L, 1)),
                  List.of(
                      """
                      CREATE OR REPLACE TYPE ORDER_ITEM_T AS OBJECT (
                        PRODUCT_ID NUMBER,
                        QUANTITY NUMBER
                      )
                      """,
                      "CREATE OR REPLACE TYPE ORDER_ITEMS_T AS TABLE OF ORDER_ITEM_T"))
              .noIdentity(), // Collections can't be used as comparison keys

          // NESTED TABLE edge case - single item
          new OracleTypeAndExample<>(
                  OracleNestedTable.of(
                      "ORDER_ITEMS_T",
                      OracleTypes.compositeOf(
                          "ORDER_ITEM_T",
                          RowCodec.<OrderItem>namedBuilder()
                              .field("PRODUCT_ID", OracleTypes.numberLong, OrderItem::productId)
                              .field("QUANTITY", OracleTypes.numberInt, OrderItem::quantity)
                              .build(OrderItem::new))),
                  List.of(new OrderItem(999L, 42)),
                  List.of(
                      """
                      CREATE OR REPLACE TYPE ORDER_ITEM_T AS OBJECT (
                        PRODUCT_ID NUMBER,
                        QUANTITY NUMBER
                      )
                      """,
                      "CREATE OR REPLACE TYPE ORDER_ITEMS_T AS TABLE OF ORDER_ITEM_T"))
              .noIdentity(), // Collections can't be used as comparison keys

          // NESTED TABLE edge case - empty list
          new OracleTypeAndExample<>(
                  OracleNestedTable.of(
                      "ORDER_ITEMS_T",
                      OracleTypes.compositeOf(
                          "ORDER_ITEM_T",
                          RowCodec.<OrderItem>namedBuilder()
                              .field("PRODUCT_ID", OracleTypes.numberLong, OrderItem::productId)
                              .field("QUANTITY", OracleTypes.numberInt, OrderItem::quantity)
                              .build(OrderItem::new))),
                  List.of(),
                  List.of(
                      """
                      CREATE OR REPLACE TYPE ORDER_ITEM_T AS OBJECT (
                        PRODUCT_ID NUMBER,
                        QUANTITY NUMBER
                      )
                      """,
                      "CREATE OR REPLACE TYPE ORDER_ITEMS_T AS TABLE OF ORDER_ITEM_T"))
              .noIdentity(),

          // ═══════════════════════════════════════════════════════════════════════════
          // Comprehensive STRUCT Tests - All Oracle Types
          // ═══════════════════════════════════════════════════════════════════════════

          // TEST_ALLTYPES - struct with all Oracle types (NOT NULL fields)
          new OracleTypeAndExample<AllTypesStruct>(
                  OracleTypes.compositeOf(
                      "TEST_ALLTYPES",
                      RowCodec.<AllTypesStruct>namedBuilder()
                          .field("VARCHAR_FIELD", OracleTypes.varchar2Of(100), s -> s.varcharField)
                          .field(
                              "NVARCHAR_FIELD", OracleTypes.nvarchar2Of(100), s -> s.nvarcharField)
                          .field("CHAR_FIELD", OracleTypes.char_Of(10), s -> s.charField)
                          .field("NCHAR_FIELD", OracleTypes.ncharOf(10), s -> s.ncharField)
                          .field("NUMBER_FIELD", OracleTypes.number, s -> s.numberField)
                          .field("NUMBER_INT_FIELD", OracleTypes.numberInt, s -> s.numberIntField)
                          .field(
                              "NUMBER_LONG_FIELD", OracleTypes.numberLong, s -> s.numberLongField)
                          .field(
                              "BINARY_FLOAT_FIELD",
                              OracleTypes.binaryFloat,
                              s -> s.binaryFloatField)
                          .field(
                              "BINARY_DOUBLE_FIELD",
                              OracleTypes.binaryDouble,
                              s -> s.binaryDoubleField)
                          .field("DATE_FIELD", OracleTypes.date, s -> s.dateField)
                          .field("TIMESTAMP_FIELD", OracleTypes.timestamp, s -> s.timestampField)
                          .field(
                              "TIMESTAMP_TZ_FIELD",
                              OracleTypes.timestampWithTimeZone,
                              s -> s.timestampTzField)
                          .field(
                              "TIMESTAMP_LTZ_FIELD",
                              OracleTypes.timestampWithLocalTimeZone,
                              s -> s.timestampLtzField)
                          .field(
                              "INTERVAL_YM_FIELD",
                              OracleTypes.intervalYearToMonth,
                              s -> s.intervalYmField)
                          .field(
                              "INTERVAL_DS_FIELD",
                              OracleTypes.intervalDayToSecond,
                              s -> s.intervalDsField)
                          .field("NESTED_OBJECT_FIELD", addressType(), s -> s.nestedObjectField)
                          .field(
                              "VARRAY_FIELD",
                              OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2Of(20)),
                              s -> s.varrayField)
                          .build(AllTypesStruct::new)),
                  new AllTypesStruct(
                      "test varchar",
                      "test nvarchar",
                      "char10    ",
                      "nchar10   ",
                      new BigDecimal("123.45"),
                      42,
                      12345678L,
                      3.14f,
                      2.718,
                      LocalDateTime.of(2024, 6, 15, 14, 30, 45),
                      LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123456000),
                      ZonedDateTime.of(2024, 6, 15, 14, 30, 45, 0, ZoneOffset.ofHours(2)),
                      Instant.parse("2024-06-15T11:30:45Z"),
                      new OracleIntervalYM(2, 5),
                      new OracleIntervalDS(3, 14, 30, 45, 123456000),
                      new Address("123 Main St", "San Francisco", coords("37.7749", "-122.4194")),
                      List.of("555-1234", "555-5678")),
                  List.of(
                      "BEGIN EXECUTE IMMEDIATE 'DROP TYPE TEST_ALLTYPES FORCE'; EXCEPTION WHEN"
                          + " OTHERS THEN NULL; END;",
                      """
                      CREATE OR REPLACE TYPE TEST_ALLTYPES AS OBJECT (
                        VARCHAR_FIELD VARCHAR2(100),
                        NVARCHAR_FIELD NVARCHAR2(100),
                        CHAR_FIELD CHAR(10),
                        NCHAR_FIELD NCHAR(10),
                        NUMBER_FIELD NUMBER,
                        NUMBER_INT_FIELD NUMBER(10),
                        NUMBER_LONG_FIELD NUMBER(19),
                        BINARY_FLOAT_FIELD BINARY_FLOAT,
                        BINARY_DOUBLE_FIELD BINARY_DOUBLE,
                        DATE_FIELD DATE,
                        TIMESTAMP_FIELD TIMESTAMP,
                        TIMESTAMP_TZ_FIELD TIMESTAMP WITH TIME ZONE,
                        TIMESTAMP_LTZ_FIELD TIMESTAMP WITH LOCAL TIME ZONE,
                        INTERVAL_YM_FIELD INTERVAL YEAR TO MONTH,
                        INTERVAL_DS_FIELD INTERVAL DAY TO SECOND,
                        NESTED_OBJECT_FIELD ADDRESS_T,
                        VARRAY_FIELD PHONE_LIST
                      )
                      """))
              .noIdentity()
              .noJsonRoundtrip(), // Complex struct - skip identity and JSON tests for now

          // TEST_ALLTYPES_OPT - comprehensive struct with all nullable fields
          new OracleTypeAndExample<AllTypesStructOptional>(
                  OracleTypes.compositeOf(
                      "TEST_ALLTYPES_OPT",
                      RowCodec.<AllTypesStructOptional>namedBuilder()
                          .field(
                              "VARCHAR_FIELD",
                              OracleTypes.varchar2Of(100).opt(),
                              s -> s.varcharField)
                          .field(
                              "NVARCHAR_FIELD",
                              OracleTypes.nvarchar2Of(100).opt(),
                              s -> s.nvarcharField)
                          .field("CHAR_FIELD", OracleTypes.char_Of(10).opt(), s -> s.charField)
                          .field("NCHAR_FIELD", OracleTypes.ncharOf(10).opt(), s -> s.ncharField)
                          .field("NUMBER_FIELD", OracleTypes.number.opt(), s -> s.numberField)
                          .field(
                              "NUMBER_INT_FIELD",
                              OracleTypes.numberInt.opt(),
                              s -> s.numberIntField)
                          .field(
                              "NUMBER_LONG_FIELD",
                              OracleTypes.numberLong.opt(),
                              s -> s.numberLongField)
                          .field(
                              "BINARY_FLOAT_FIELD",
                              OracleTypes.binaryFloat.opt(),
                              s -> s.binaryFloatField)
                          .field(
                              "BINARY_DOUBLE_FIELD",
                              OracleTypes.binaryDouble.opt(),
                              s -> s.binaryDoubleField)
                          .field("DATE_FIELD", OracleTypes.date.opt(), s -> s.dateField)
                          .field(
                              "TIMESTAMP_FIELD", OracleTypes.timestamp.opt(), s -> s.timestampField)
                          .field(
                              "TIMESTAMP_TZ_FIELD",
                              OracleTypes.timestampWithTimeZone.opt(),
                              s -> s.timestampTzField)
                          .field(
                              "TIMESTAMP_LTZ_FIELD",
                              OracleTypes.timestampWithLocalTimeZone.opt(),
                              s -> s.timestampLtzField)
                          .field(
                              "INTERVAL_YM_FIELD",
                              OracleTypes.intervalYearToMonth.opt(),
                              s -> s.intervalYmField)
                          .field(
                              "INTERVAL_DS_FIELD",
                              OracleTypes.intervalDayToSecond.opt(),
                              s -> s.intervalDsField)
                          .field(
                              "NESTED_OBJECT_FIELD", addressType().opt(), s -> s.nestedObjectField)
                          .field(
                              "VARRAY_FIELD",
                              OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2Of(20)).opt(),
                              s -> s.varrayField)
                          .build(AllTypesStructOptional::new)),
                  new AllTypesStructOptional(
                      Optional.of("test varchar"),
                      Optional.empty(), // Test null nvarcharField
                      Optional.of("char10    "),
                      Optional.empty(), // Test null ncharField
                      Optional.of(new BigDecimal("123.45")),
                      Optional.of(42),
                      Optional.empty(), // Test null numberLongField
                      Optional.of(3.14f),
                      Optional.of(2.718),
                      Optional.of(LocalDateTime.of(2024, 6, 15, 14, 30, 45)),
                      Optional.empty(), // Test null timestampField
                      Optional.of(
                          ZonedDateTime.of(2024, 6, 15, 14, 30, 45, 0, ZoneOffset.ofHours(2))),
                      Optional.of(Instant.parse("2024-06-15T11:30:45Z")),
                      Optional.of(new OracleIntervalYM(2, 5)),
                      Optional.empty(), // Test null intervalDsField
                      Optional.of(
                          new Address(
                              "123 Main St", "San Francisco", coords("37.7749", "-122.4194"))),
                      Optional.of(List.of("555-1234", "555-5678"))),
                  List.of(
                      "BEGIN EXECUTE IMMEDIATE 'DROP TYPE TEST_ALLTYPES_OPT FORCE';"
                          + " EXCEPTION WHEN OTHERS THEN NULL; END;",
                      """
                      CREATE OR REPLACE TYPE TEST_ALLTYPES_OPT AS OBJECT (
                        VARCHAR_FIELD VARCHAR2(100),
                        NVARCHAR_FIELD NVARCHAR2(100),
                        CHAR_FIELD CHAR(10),
                        NCHAR_FIELD NCHAR(10),
                        NUMBER_FIELD NUMBER,
                        NUMBER_INT_FIELD NUMBER(10),
                        NUMBER_LONG_FIELD NUMBER(19),
                        BINARY_FLOAT_FIELD BINARY_FLOAT,
                        BINARY_DOUBLE_FIELD BINARY_DOUBLE,
                        DATE_FIELD DATE,
                        TIMESTAMP_FIELD TIMESTAMP,
                        TIMESTAMP_TZ_FIELD TIMESTAMP WITH TIME ZONE,
                        TIMESTAMP_LTZ_FIELD TIMESTAMP WITH LOCAL TIME ZONE,
                        INTERVAL_YM_FIELD INTERVAL YEAR TO MONTH,
                        INTERVAL_DS_FIELD INTERVAL DAY TO SECOND,
                        NESTED_OBJECT_FIELD ADDRESS_T,
                        VARRAY_FIELD PHONE_LIST
                      )
                      """))
              .noIdentity()
              .noJsonRoundtrip(), // Complex struct - skip identity and JSON tests for now

          // ═══════════════════════════════════════════════════════════════════════════
          // Structs Without LOBs - For VARRAY Compatibility
          // Oracle restriction: VARRAYs cannot contain structs with embedded LOBs
          // ═══════════════════════════════════════════════════════════════════════════

          // TEST_ALLTYPES_NOLOBS - standalone struct without LOBs
          new OracleTypeAndExample<>(
                  OracleTypes.compositeOf(
                      "TEST_ALLTYPES_NOLOBS",
                      RowCodec.<AllTypesStructNoLobs>namedBuilder()
                          .field("VARCHAR_FIELD", OracleTypes.varchar2Of(100), s -> s.varcharField)
                          .field(
                              "NVARCHAR_FIELD", OracleTypes.nvarchar2Of(100), s -> s.nvarcharField)
                          .field("CHAR_FIELD", OracleTypes.char_Of(10), s -> s.charField)
                          .field("NCHAR_FIELD", OracleTypes.ncharOf(10), s -> s.ncharField)
                          .field("NUMBER_FIELD", OracleTypes.number, s -> s.numberField)
                          .field("NUMBER_INT_FIELD", OracleTypes.numberInt, s -> s.numberIntField)
                          .field(
                              "NUMBER_LONG_FIELD", OracleTypes.numberLong, s -> s.numberLongField)
                          .field(
                              "BINARY_FLOAT_FIELD",
                              OracleTypes.binaryFloat,
                              s -> s.binaryFloatField)
                          .field(
                              "BINARY_DOUBLE_FIELD",
                              OracleTypes.binaryDouble,
                              s -> s.binaryDoubleField)
                          .field("DATE_FIELD", OracleTypes.date, s -> s.dateField)
                          .field("TIMESTAMP_FIELD", OracleTypes.timestamp, s -> s.timestampField)
                          .field(
                              "TIMESTAMP_TZ_FIELD",
                              OracleTypes.timestampWithTimeZone,
                              s -> s.timestampTzField)
                          .field(
                              "TIMESTAMP_LTZ_FIELD",
                              OracleTypes.timestampWithLocalTimeZone,
                              s -> s.timestampLtzField)
                          .field(
                              "INTERVAL_YM_FIELD",
                              OracleTypes.intervalYearToMonth,
                              s -> s.intervalYmField)
                          .field(
                              "INTERVAL_DS_FIELD",
                              OracleTypes.intervalDayToSecond,
                              s -> s.intervalDsField)
                          .field("NESTED_OBJECT_FIELD", addressType(), s -> s.nestedObjectField)
                          .field(
                              "VARRAY_FIELD",
                              OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2Of(20)),
                              s -> s.varrayField)
                          .build(AllTypesStructNoLobs::new)),
                  new AllTypesStructNoLobs(
                      "varchar_val",
                      "nvarchar_val",
                      "char_val  ",
                      "nchar_val ",
                      new BigDecimal("123.45"),
                      42,
                      9876543210L,
                      3.14f,
                      2.718281828,
                      LocalDateTime.of(2024, 3, 15, 14, 30),
                      LocalDateTime.of(2024, 3, 15, 14, 30, 45, 123456789),
                      ZonedDateTime.of(2024, 3, 15, 14, 30, 45, 0, ZoneOffset.ofHours(2)),
                      Instant.parse("2024-03-15T11:30:45Z"),
                      new OracleIntervalYM(2, 6),
                      new OracleIntervalDS(5, 12, 30, 45, 123456000),
                      new Address("456 Oak Ave", "Portland", coords("45.5152", "-122.6784")),
                      List.of("555-1234", "555-5678")),
                  List.of(
                      """
                      CREATE OR REPLACE TYPE TEST_ALLTYPES_NOLOBS AS OBJECT (
                        VARCHAR_FIELD VARCHAR2(100),
                        NVARCHAR_FIELD NVARCHAR2(100),
                        CHAR_FIELD CHAR(10),
                        NCHAR_FIELD NCHAR(10),
                        NUMBER_FIELD NUMBER,
                        NUMBER_INT_FIELD NUMBER(10),
                        NUMBER_LONG_FIELD NUMBER(19),
                        BINARY_FLOAT_FIELD BINARY_FLOAT,
                        BINARY_DOUBLE_FIELD BINARY_DOUBLE,
                        DATE_FIELD DATE,
                        TIMESTAMP_FIELD TIMESTAMP,
                        TIMESTAMP_TZ_FIELD TIMESTAMP WITH TIME ZONE,
                        TIMESTAMP_LTZ_FIELD TIMESTAMP WITH LOCAL TIME ZONE,
                        INTERVAL_YM_FIELD INTERVAL YEAR TO MONTH,
                        INTERVAL_DS_FIELD INTERVAL DAY TO SECOND,
                        NESTED_OBJECT_FIELD ADDRESS_T,
                        VARRAY_FIELD PHONE_LIST
                      )
                      """))
              .noJsonRoundtrip()
              .noIdentity(), // Oracle ORA-22901: cannot compare types with VARRAY attributes

          // TEST_ALLTYPES_NOLOBS_OPT - standalone struct without LOBs, optional fields
          new OracleTypeAndExample<>(
                  OracleTypes.compositeOf(
                      "TEST_ALLTYPES_NOLOBS_OPT",
                      RowCodec.<AllTypesStructNoLobsOptional>namedBuilder()
                          .field(
                              "VARCHAR_FIELD",
                              OracleTypes.varchar2Of(100).opt(),
                              s -> s.varcharField)
                          .field(
                              "NVARCHAR_FIELD",
                              OracleTypes.nvarchar2Of(100).opt(),
                              s -> s.nvarcharField)
                          .field("CHAR_FIELD", OracleTypes.char_Of(10).opt(), s -> s.charField)
                          .field("NCHAR_FIELD", OracleTypes.ncharOf(10).opt(), s -> s.ncharField)
                          .field("NUMBER_FIELD", OracleTypes.number.opt(), s -> s.numberField)
                          .field(
                              "NUMBER_INT_FIELD",
                              OracleTypes.numberInt.opt(),
                              s -> s.numberIntField)
                          .field(
                              "NUMBER_LONG_FIELD",
                              OracleTypes.numberLong.opt(),
                              s -> s.numberLongField)
                          .field(
                              "BINARY_FLOAT_FIELD",
                              OracleTypes.binaryFloat.opt(),
                              s -> s.binaryFloatField)
                          .field(
                              "BINARY_DOUBLE_FIELD",
                              OracleTypes.binaryDouble.opt(),
                              s -> s.binaryDoubleField)
                          .field("DATE_FIELD", OracleTypes.date.opt(), s -> s.dateField)
                          .field(
                              "TIMESTAMP_FIELD", OracleTypes.timestamp.opt(), s -> s.timestampField)
                          .field(
                              "TIMESTAMP_TZ_FIELD",
                              OracleTypes.timestampWithTimeZone.opt(),
                              s -> s.timestampTzField)
                          .field(
                              "TIMESTAMP_LTZ_FIELD",
                              OracleTypes.timestampWithLocalTimeZone.opt(),
                              s -> s.timestampLtzField)
                          .field(
                              "INTERVAL_YM_FIELD",
                              OracleTypes.intervalYearToMonth.opt(),
                              s -> s.intervalYmField)
                          .field(
                              "INTERVAL_DS_FIELD",
                              OracleTypes.intervalDayToSecond.opt(),
                              s -> s.intervalDsField)
                          .field(
                              "NESTED_OBJECT_FIELD", addressType().opt(), s -> s.nestedObjectField)
                          .field(
                              "VARRAY_FIELD",
                              OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2Of(20)).opt(),
                              s -> s.varrayField)
                          .build(AllTypesStructNoLobsOptional::new)),
                  new AllTypesStructNoLobsOptional(
                      Optional.of("varchar_val"),
                      Optional.empty(),
                      Optional.of("char_val  "),
                      Optional.empty(),
                      Optional.of(new BigDecimal("123.45")),
                      Optional.of(42),
                      Optional.empty(),
                      Optional.of(3.14f),
                      Optional.of(2.718281828),
                      Optional.of(LocalDateTime.of(2024, 3, 15, 14, 30)),
                      Optional.empty(),
                      Optional.of(
                          ZonedDateTime.of(2024, 3, 15, 14, 30, 45, 0, ZoneOffset.ofHours(2))),
                      Optional.of(Instant.parse("2024-03-15T11:30:45Z")),
                      Optional.of(new OracleIntervalYM(2, 6)),
                      Optional.empty(),
                      Optional.of(
                          new Address("456 Oak Ave", "Portland", coords("45.5152", "-122.6784"))),
                      Optional.of(List.of("555-1234"))),
                  List.of(
                      """
                      CREATE OR REPLACE TYPE TEST_ALLTYPES_NOLOBS_OPT AS OBJECT (
                        VARCHAR_FIELD VARCHAR2(100),
                        NVARCHAR_FIELD NVARCHAR2(100),
                        CHAR_FIELD CHAR(10),
                        NCHAR_FIELD NCHAR(10),
                        NUMBER_FIELD NUMBER,
                        NUMBER_INT_FIELD NUMBER(10),
                        NUMBER_LONG_FIELD NUMBER(19),
                        BINARY_FLOAT_FIELD BINARY_FLOAT,
                        BINARY_DOUBLE_FIELD BINARY_DOUBLE,
                        DATE_FIELD DATE,
                        TIMESTAMP_FIELD TIMESTAMP,
                        TIMESTAMP_TZ_FIELD TIMESTAMP WITH TIME ZONE,
                        TIMESTAMP_LTZ_FIELD TIMESTAMP WITH LOCAL TIME ZONE,
                        INTERVAL_YM_FIELD INTERVAL YEAR TO MONTH,
                        INTERVAL_DS_FIELD INTERVAL DAY TO SECOND,
                        NESTED_OBJECT_FIELD ADDRESS_T,
                        VARRAY_FIELD PHONE_LIST
                      )
                      """))
              .noJsonRoundtrip()
              .noIdentity(), // Oracle ORA-22901: cannot compare types with VARRAY attributes

          // VARRAY of TEST_ALLTYPES_NOLOBS - array of structs without LOBs
          new OracleTypeAndExample<>(
                  OracleVArray.of(
                      "TEST_ALLTYPES_NOLOBS_ARR",
                      10,
                      OracleTypes.compositeOf(
                          "TEST_ALLTYPES_NOLOBS",
                          RowCodec.<AllTypesStructNoLobs>namedBuilder()
                              .field(
                                  "VARCHAR_FIELD", OracleTypes.varchar2Of(100), s -> s.varcharField)
                              .field(
                                  "NVARCHAR_FIELD",
                                  OracleTypes.nvarchar2Of(100),
                                  s -> s.nvarcharField)
                              .field("CHAR_FIELD", OracleTypes.char_Of(10), s -> s.charField)
                              .field("NCHAR_FIELD", OracleTypes.ncharOf(10), s -> s.ncharField)
                              .field("NUMBER_FIELD", OracleTypes.number, s -> s.numberField)
                              .field(
                                  "NUMBER_INT_FIELD", OracleTypes.numberInt, s -> s.numberIntField)
                              .field(
                                  "NUMBER_LONG_FIELD",
                                  OracleTypes.numberLong,
                                  s -> s.numberLongField)
                              .field(
                                  "BINARY_FLOAT_FIELD",
                                  OracleTypes.binaryFloat,
                                  s -> s.binaryFloatField)
                              .field(
                                  "BINARY_DOUBLE_FIELD",
                                  OracleTypes.binaryDouble,
                                  s -> s.binaryDoubleField)
                              .field("DATE_FIELD", OracleTypes.date, s -> s.dateField)
                              .field(
                                  "TIMESTAMP_FIELD", OracleTypes.timestamp, s -> s.timestampField)
                              .field(
                                  "TIMESTAMP_TZ_FIELD",
                                  OracleTypes.timestampWithTimeZone,
                                  s -> s.timestampTzField)
                              .field(
                                  "TIMESTAMP_LTZ_FIELD",
                                  OracleTypes.timestampWithLocalTimeZone,
                                  s -> s.timestampLtzField)
                              .field(
                                  "INTERVAL_YM_FIELD",
                                  OracleTypes.intervalYearToMonth,
                                  s -> s.intervalYmField)
                              .field(
                                  "INTERVAL_DS_FIELD",
                                  OracleTypes.intervalDayToSecond,
                                  s -> s.intervalDsField)
                              .field("NESTED_OBJECT_FIELD", addressType(), s -> s.nestedObjectField)
                              .field(
                                  "VARRAY_FIELD",
                                  OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2Of(20)),
                                  s -> s.varrayField)
                              .build(AllTypesStructNoLobs::new))),
                  List.of(
                      new AllTypesStructNoLobs(
                          "varchar1",
                          "nvarchar1",
                          "char1     ",
                          "nchar1    ",
                          new BigDecimal("111.11"),
                          11,
                          1111L,
                          1.1f,
                          1.11,
                          LocalDateTime.of(2024, 1, 1, 10, 0),
                          LocalDateTime.of(2024, 1, 1, 10, 0, 0, 111000000),
                          ZonedDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC),
                          Instant.parse("2024-01-01T07:00:00Z"),
                          new OracleIntervalYM(1, 1),
                          new OracleIntervalDS(1, 1, 1, 1, 111000000),
                          new Address("111 First St", "City1", coords("40.7128", "-74.006")),
                          List.of("111-1111")),
                      new AllTypesStructNoLobs(
                          "varchar2",
                          "nvarchar2",
                          "char2     ",
                          "nchar2    ",
                          new BigDecimal("222.22"),
                          22,
                          2222L,
                          2.2f,
                          2.22,
                          LocalDateTime.of(2024, 2, 2, 20, 0),
                          LocalDateTime.of(2024, 2, 2, 20, 0, 0, 222000000),
                          ZonedDateTime.of(2024, 2, 2, 20, 0, 0, 0, ZoneOffset.ofHours(-5)),
                          Instant.parse("2024-02-02T17:00:00Z"),
                          new OracleIntervalYM(2, 2),
                          new OracleIntervalDS(2, 2, 2, 2, 222000000),
                          new Address("222 Second St", "City2", coords("34.0522", "-118.2437")),
                          List.of("222-2222", "222-3333"))),
                  List.of(
                      "BEGIN EXECUTE IMMEDIATE 'DROP TYPE TEST_ALLTYPES_NOLOBS_ARR';"
                          + " EXCEPTION WHEN OTHERS THEN NULL; END;",
                      "BEGIN EXECUTE IMMEDIATE 'DROP TYPE TEST_ALLTYPES_NOLOBS FORCE';"
                          + " EXCEPTION WHEN OTHERS THEN NULL; END;",
                      """
                      CREATE OR REPLACE TYPE TEST_ALLTYPES_NOLOBS AS OBJECT (
                        VARCHAR_FIELD VARCHAR2(100),
                        NVARCHAR_FIELD NVARCHAR2(100),
                        CHAR_FIELD CHAR(10),
                        NCHAR_FIELD NCHAR(10),
                        NUMBER_FIELD NUMBER,
                        NUMBER_INT_FIELD NUMBER(10),
                        NUMBER_LONG_FIELD NUMBER(19),
                        BINARY_FLOAT_FIELD BINARY_FLOAT,
                        BINARY_DOUBLE_FIELD BINARY_DOUBLE,
                        DATE_FIELD DATE,
                        TIMESTAMP_FIELD TIMESTAMP,
                        TIMESTAMP_TZ_FIELD TIMESTAMP WITH TIME ZONE,
                        TIMESTAMP_LTZ_FIELD TIMESTAMP WITH LOCAL TIME ZONE,
                        INTERVAL_YM_FIELD INTERVAL YEAR TO MONTH,
                        INTERVAL_DS_FIELD INTERVAL DAY TO SECOND,
                        NESTED_OBJECT_FIELD ADDRESS_T,
                        VARRAY_FIELD PHONE_LIST
                      )
                      """,
                      "CREATE OR REPLACE TYPE TEST_ALLTYPES_NOLOBS_ARR AS VARRAY(10) OF"
                          + " TEST_ALLTYPES_NOLOBS"))
              .noIdentity(), // Complex array of structs - skip identity test

          // VARRAY of TEST_ALLTYPES_NOLOBS_OPT - array of structs without LOBs, optional
          // fields
          new OracleTypeAndExample<>(
                  OracleVArray.of(
                      "TEST_ALLTYPES_NOLOBS_OPT_ARR",
                      10,
                      OracleTypes.compositeOf(
                          "TEST_ALLTYPES_NOLOBS_OPT",
                          RowCodec.<AllTypesStructNoLobsOptional>namedBuilder()
                              .field(
                                  "VARCHAR_FIELD",
                                  OracleTypes.varchar2Of(100).opt(),
                                  s -> s.varcharField)
                              .field(
                                  "NVARCHAR_FIELD",
                                  OracleTypes.nvarchar2Of(100).opt(),
                                  s -> s.nvarcharField)
                              .field("CHAR_FIELD", OracleTypes.char_Of(10).opt(), s -> s.charField)
                              .field(
                                  "NCHAR_FIELD", OracleTypes.ncharOf(10).opt(), s -> s.ncharField)
                              .field("NUMBER_FIELD", OracleTypes.number.opt(), s -> s.numberField)
                              .field(
                                  "NUMBER_INT_FIELD",
                                  OracleTypes.numberInt.opt(),
                                  s -> s.numberIntField)
                              .field(
                                  "NUMBER_LONG_FIELD",
                                  OracleTypes.numberLong.opt(),
                                  s -> s.numberLongField)
                              .field(
                                  "BINARY_FLOAT_FIELD",
                                  OracleTypes.binaryFloat.opt(),
                                  s -> s.binaryFloatField)
                              .field(
                                  "BINARY_DOUBLE_FIELD",
                                  OracleTypes.binaryDouble.opt(),
                                  s -> s.binaryDoubleField)
                              .field("DATE_FIELD", OracleTypes.date.opt(), s -> s.dateField)
                              .field(
                                  "TIMESTAMP_FIELD",
                                  OracleTypes.timestamp.opt(),
                                  s -> s.timestampField)
                              .field(
                                  "TIMESTAMP_TZ_FIELD",
                                  OracleTypes.timestampWithTimeZone.opt(),
                                  s -> s.timestampTzField)
                              .field(
                                  "TIMESTAMP_LTZ_FIELD",
                                  OracleTypes.timestampWithLocalTimeZone.opt(),
                                  s -> s.timestampLtzField)
                              .field(
                                  "INTERVAL_YM_FIELD",
                                  OracleTypes.intervalYearToMonth.opt(),
                                  s -> s.intervalYmField)
                              .field(
                                  "INTERVAL_DS_FIELD",
                                  OracleTypes.intervalDayToSecond.opt(),
                                  s -> s.intervalDsField)
                              .field(
                                  "NESTED_OBJECT_FIELD",
                                  addressType().opt(),
                                  s -> s.nestedObjectField)
                              .field(
                                  "VARRAY_FIELD",
                                  OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2Of(20))
                                      .opt(),
                                  s -> s.varrayField)
                              .build(AllTypesStructNoLobsOptional::new))),
                  List.of(
                      new AllTypesStructNoLobsOptional(
                          Optional.of("varchar1"),
                          Optional.empty(),
                          Optional.of("char1     "),
                          Optional.empty(),
                          Optional.of(new BigDecimal("111.11")),
                          Optional.of(11),
                          Optional.empty(),
                          Optional.of(1.1f),
                          Optional.of(1.11),
                          Optional.of(LocalDateTime.of(2024, 1, 1, 10, 0)),
                          Optional.empty(),
                          Optional.of(ZonedDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneOffset.UTC)),
                          Optional.of(Instant.parse("2024-01-01T07:00:00Z")),
                          Optional.of(new OracleIntervalYM(1, 1)),
                          Optional.empty(),
                          Optional.of(
                              new Address("111 First St", "City1", coords("40.7128", "-74.006"))),
                          Optional.of(List.of("111-1111"))),
                      new AllTypesStructNoLobsOptional(
                          Optional.of("varchar2"),
                          Optional.of("nvarchar2"),
                          Optional.of("char2     "),
                          Optional.of("nchar2    "),
                          Optional.of(new BigDecimal("222.22")),
                          Optional.of(22),
                          Optional.of(2222L),
                          Optional.of(2.2f),
                          Optional.of(2.22),
                          Optional.of(LocalDateTime.of(2024, 2, 2, 20, 0)),
                          Optional.of(LocalDateTime.of(2024, 2, 2, 20, 0, 0, 222000000)),
                          Optional.of(
                              ZonedDateTime.of(2024, 2, 2, 20, 0, 0, 0, ZoneOffset.ofHours(-5))),
                          Optional.of(Instant.parse("2024-02-02T17:00:00Z")),
                          Optional.of(new OracleIntervalYM(2, 2)),
                          Optional.of(new OracleIntervalDS(2, 2, 2, 2, 222000000)),
                          Optional.of(
                              new Address(
                                  "222 Second St", "City2", coords("34.0522", "-118.2437"))),
                          Optional.of(List.of("222-2222", "222-3333")))),
                  List.of(
                      "BEGIN EXECUTE IMMEDIATE 'DROP TYPE TEST_ALLTYPES_NOLOBS_OPT_ARR';"
                          + " EXCEPTION WHEN OTHERS THEN NULL; END;",
                      "BEGIN EXECUTE IMMEDIATE 'DROP TYPE TEST_ALLTYPES_NOLOBS_OPT FORCE';"
                          + " EXCEPTION WHEN OTHERS THEN NULL; END;",
                      """
                      CREATE OR REPLACE TYPE TEST_ALLTYPES_NOLOBS_OPT AS OBJECT (
                        VARCHAR_FIELD VARCHAR2(100),
                        NVARCHAR_FIELD NVARCHAR2(100),
                        CHAR_FIELD CHAR(10),
                        NCHAR_FIELD NCHAR(10),
                        NUMBER_FIELD NUMBER,
                        NUMBER_INT_FIELD NUMBER(10),
                        NUMBER_LONG_FIELD NUMBER(19),
                        BINARY_FLOAT_FIELD BINARY_FLOAT,
                        BINARY_DOUBLE_FIELD BINARY_DOUBLE,
                        DATE_FIELD DATE,
                        TIMESTAMP_FIELD TIMESTAMP,
                        TIMESTAMP_TZ_FIELD TIMESTAMP WITH TIME ZONE,
                        TIMESTAMP_LTZ_FIELD TIMESTAMP WITH LOCAL TIME ZONE,
                        INTERVAL_YM_FIELD INTERVAL YEAR TO MONTH,
                        INTERVAL_DS_FIELD INTERVAL DAY TO SECOND,
                        NESTED_OBJECT_FIELD ADDRESS_T,
                        VARRAY_FIELD PHONE_LIST
                      )
                      """,
                      "CREATE OR REPLACE TYPE TEST_ALLTYPES_NOLOBS_OPT_ARR AS VARRAY(10)"
                          + " OF TEST_ALLTYPES_NOLOBS_OPT"))
              .noIdentity() // Complex array of structs - skip identity test
          );

  // Connection helper for Oracle - uses HikariCP connection pool
  // Uses Oracle Free 23c on port 1521, connecting to FREEPDB1 pluggable database
  static <T> T withConnection(SqlFunction<Connection, T> f) {
    try (var pooledConn = pool().unwrap().getConnection()) {
      // Unwrap to get the underlying OracleConnection for STRUCT/ARRAY creation
      var conn = pooledConn.unwrap(oracle.jdbc.OracleConnection.class);
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

  @Test
  public void test() {
    System.out.println("Testing Oracle type codecs...\n");

    // Test JSON roundtrip first (no database connection needed) - parallel
    System.out.println("=== JSON Roundtrip Tests (parallel) ===");
    All.parallelStream()
        .filter(t -> t.jsonRoundtripWorks)
        .forEach(OracleTypeTest::testJsonRoundtrip);
    System.out.println();

    // Create all user-defined types upfront (must be sequential to avoid conflicts)
    withConnection(
        conn -> {
          System.out.println("=== Creating user-defined types ===");
          var executedSql = new HashSet<String>();
          for (OracleTypeAndExample<?> t : All) {
            if (!t.setupSql.isEmpty()) {
              try (var stmt = conn.createStatement()) {
                for (String sql : t.setupSql) {
                  if (executedSql.add(sql)) {
                    try {
                      stmt.execute(sql);
                    } catch (SQLException e) {
                      if (!e.getMessage().contains("ORA-00955")
                          && !e.getMessage().contains("ORA-02303")) {
                        throw e;
                      }
                    }
                  }
                }
              }
            }
          }
          conn.commit();
          return null;
        });

    // Run all DB tests in parallel
    System.out.println("\n=== DB Roundtrip Tests (parallel) ===");
    var failures =
        All.parallelStream()
            .flatMap(
                t -> {
                  var errors = new ArrayList<String>();

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

                  // getGeneratedKeys roundtrip test (skip user-defined types)
                  if (t.setupSql.isEmpty()) {
                    try {
                      withConnection(
                          conn -> {
                            testGeneratedKeysRoundtrip(conn, t);
                            return null;
                          });
                    } catch (Exception e) {
                      errors.add(
                          "getGeneratedKeys test FAILED "
                              + t.type.typename().sqlType()
                              + ": "
                              + e.getMessage());
                    }
                  }

                  return errors.stream();
                })
            .toList();

    // Stored procedure callable roundtrip tests - deduplicate by SQL type, skip composites
    System.out.println("\n=== Callable Roundtrip Tests (parallel) ===");
    var callFailures =
        All.stream()
            .filter(t -> t.setupSql.isEmpty())
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
                    return java.util.stream.Stream.of(
                        "Callable test FAILED "
                            + t.type.typename().sqlType()
                            + ": "
                            + e.getMessage());
                  }
                })
            .toList();

    // Query analysis tests - deduplicated by SQL type
    System.out.println("\n=== Query Analysis Tests (parallel) ===");
    var analysisFailures =
        All.stream()
            .collect(Collectors.toMap(t -> t.type.typename().sqlType(), t -> t, (a, b) -> a))
            .values()
            .parallelStream()
            .flatMap(
                t -> {
                  try {
                    withConnection(
                        conn -> {
                          testQueryAnalysis(conn, t);
                          return null;
                        });
                    return Stream.<String>empty();
                  } catch (Exception e) {
                    return Stream.of(
                        "Analysis FAILED " + t.type.typename().sqlType() + ": " + e.getMessage());
                  }
                })
            .toList();

    var allFailures = new ArrayList<String>();
    allFailures.addAll(failures);
    allFailures.addAll(callFailures);
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

  /**
   * Test getGeneratedKeys roundtrip - simulates INSERT RETURNING behavior. Creates a table with an
   * auto-generated ID column plus a column of the type under test, inserts a value, and reads back
   * the entire row via getGeneratedKeys().
   */
  static <A> void testGeneratedKeysRoundtrip(Connection conn, OracleTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();
    A original = t.example;
    A expected = t.expected(); // May differ from original due to Oracle quirks

    // Create table with auto-generated ID + test column
    String tableName = uniqueTableName("TEST_GENKEYS");
    try (var stmt = conn.createStatement()) {
      // Drop if exists from previous failed run
      try {
        stmt.execute("DROP TABLE " + tableName + " PURGE");
      } catch (SQLException ignored) {
      }
      stmt.execute(
          "CREATE TABLE "
              + tableName
              + " (id NUMBER GENERATED ALWAYS AS IDENTITY, v "
              + sqlType
              + ")");
    }

    try {
      // Insert using PreparedStatement with column names to get back via getGeneratedKeys
      String insertSql = "INSERT INTO " + tableName + " (v) VALUES (?)";
      var insert = conn.prepareStatement(insertSql, new String[] {"ID", "V"});
      t.type.write().set(insert, 1, original);
      insert.executeUpdate();

      // Read back via getGeneratedKeys
      var rs = insert.getGeneratedKeys();
      if (!rs.next()) {
        throw new RuntimeException("getGeneratedKeys returned no rows");
      }

      // Check metadata
      var meta = rs.getMetaData();
      System.out.println("getGeneratedKeys " + sqlType + ":");
      System.out.println("  Columns: " + meta.getColumnCount());
      for (int i = 1; i <= meta.getColumnCount(); i++) {
        System.out.println(
            "    " + i + ": " + meta.getColumnName(i) + " (" + meta.getColumnTypeName(i) + ")");
      }

      // Read ID (column 1)
      Long id = rs.getLong(1);
      System.out.println("  ID: " + id);

      // Read the value (column 2) - use optional reader if expecting NULL
      final A actual;
      if (expected == null) {
        Optional<A> actualOpt = t.type.opt().read().read(rs, 2);
        actual = actualOpt.orElse(null);
      } else {
        actual = t.type.read().read(rs, 2);
      }
      System.out.println("  Value: " + format(actual));

      rs.close();
      insert.close();

      assertEquals(actual, expected, "getGeneratedKeys value mismatch");
      System.out.println("  PASSED\n");

    } finally {
      // Drop table
      try (var stmt = conn.createStatement()) {
        stmt.execute("DROP TABLE " + tableName);
      }
    }
  }

  static <A> void testQueryAnalysis(Connection conn, OracleTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("QA");
    String createTableDDL = "CREATE TABLE " + tableName + " (v " + sqlType + ")";
    if (sqlType.contains("ORDER_ITEMS_T") || sqlType.contains("_NESTED_TABLE")) {
      createTableDDL += " NESTED TABLE v STORE AS " + tableName + "_STORAGE";
    }
    conn.createStatement().execute(createTableDDL);
    try {
      RowCodec<A> parser = RowCodec.of(t.type);
      Fragment fragment = Fragment.of("SELECT v FROM " + tableName);
      QueryAnalysis analysis = QueryAnalyzer.analyze(fragment.query(parser.all()), conn).getFirst();
      if (!analysis.succeeded()) {
        throw new RuntimeException(
            "Query analysis failed for " + sqlType + ":\n" + analysis.report());
      }
    } finally {
      conn.createStatement().execute("DROP TABLE " + tableName);
    }
  }

  static <A> void testJsonRoundtrip(OracleTypeAndExample<A> t) {
    try {
      OracleJson<A> jsonCodec = t.type.oracleJson();
      A original = t.example;
      A expected = t.expected(); // May differ from original due to Oracle quirks

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

      if (t.hasIdentity && !areEqual(decoded, expected)) {
        throw new RuntimeException(
            "JSON roundtrip failed for "
                + t.type.typename().sqlType()
                + ": expected '"
                + format(expected)
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
  static <A> void testJsonDbRoundtrip(Connection conn, OracleTypeAndExample<A> t)
      throws SQLException {
    OracleJson<A> jsonCodec = t.type.oracleJson();
    A original = t.example;
    A expected = t.expected(); // May differ from original due to Oracle quirks
    String sqlType = t.type.typename().sqlType();

    // Create temp table (Oracle uses Global Temporary Tables differently, using regular table +
    // cleanup)
    String tableName = uniqueTableName("TEST_JSON_RT");
    try (var stmt = conn.createStatement()) {
      // NESTED TABLE columns require STORE AS clause
      String createTableDDL = "CREATE TABLE " + tableName + " (v " + sqlType + ")";
      if (sqlType.contains("ORDER_ITEMS_T")) { // Nested table type
        createTableDDL += " NESTED TABLE v STORE AS " + tableName + "_STORAGE";
      }
      stmt.execute(createTableDDL);
    }

    try {
      // Insert value using native type
      var insert = conn.prepareStatement("INSERT INTO " + tableName + " (v) VALUES (?)");
      t.type.write().set(insert, 1, original);
      insert.execute();
      insert.close();

      // Select back as JSON using JSON_OBJECT - this is what MULTISET does
      var select = conn.prepareStatement("SELECT JSON_OBJECT('v' VALUE v) FROM " + tableName);
      select.execute();
      var rs = select.getResultSet();

      if (!rs.next()) {
        throw new RuntimeException("No rows returned");
      }

      // Read the JSON string back from the database
      String jsonFromDb = rs.getString(1);
      rs.close();
      select.close();

      // Parse the JSON object and extract 'v' field
      JsonValue parsedFromDb = JsonValue.parse(jsonFromDb);
      JsonValue fieldValue = ((JsonValue.JObject) parsedFromDb).get("v");
      A decoded = jsonCodec.fromJson(fieldValue);

      System.out.println(
          "JSON DB roundtrip "
              + sqlType
              + ": "
              + format(original)
              + " -> DB -> "
              + jsonFromDb
              + " -> "
              + format(decoded));

      if (t.hasIdentity && !areEqual(decoded, expected)) {
        throw new RuntimeException(
            "JSON DB roundtrip failed for "
                + sqlType
                + ": expected '"
                + format(expected)
                + "' but got '"
                + format(decoded)
                + "'");
      }
    } finally {
      try (var stmt = conn.createStatement()) {
        stmt.execute("DROP TABLE " + tableName);
      }
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

  static <A> void testCase(Connection conn, OracleTypeAndExample<A> t) throws SQLException {
    String sqlType = t.type.typename().sqlType();

    // Execute setup SQL (for type definitions, etc.)
    if (!t.setupSql.isEmpty()) {
      try (var stmt = conn.createStatement()) {
        for (String sql : t.setupSql) {
          try {
            stmt.execute(sql);
          } catch (SQLException e) {
            // Ignore common type creation errors:
            // ORA-00955: name is already used by an existing object
            // ORA-02303: cannot DROP or REPLACE a type with type or table dependents
            if (!e.getMessage().contains("ORA-00955")
                && !e.getMessage().contains("ORA-02303")
                && !e.getMessage().contains("ORA-00054")) {
              throw e;
            }
          }
        }
      }
    }

    // Create table (Oracle doesn't have CREATE TEMPORARY TABLE syntax in standard form)
    String tableName = uniqueTableName("TEST_TABLE");
    try (var stmt = conn.createStatement()) {
      // NESTED TABLE columns require STORE AS clause
      String createTableDDL = "CREATE TABLE " + tableName + " (v " + sqlType + ")";
      if (sqlType.contains("ORDER_ITEMS_T")
          || sqlType.contains("_NESTED_TABLE")
          || sqlType.endsWith("_NT")) { // Nested table type
        createTableDDL += " NESTED TABLE v STORE AS " + tableName + "_STORAGE";
      }
      stmt.execute(createTableDDL);
    }

    try {
      A original = t.example;
      A expected = t.expected(); // May differ from original due to Oracle quirks
      batchInsert(conn, t.type, tableName, original);

      // Select and verify
      final PreparedStatement select;
      if (t.hasIdentity) {
        // For NULL values, use IS NULL since WHERE v = NULL doesn't match (NULL = NULL is UNKNOWN)
        if (expected == null) {
          select = conn.prepareStatement("SELECT v, NULL FROM " + tableName + " WHERE v IS NULL");
        } else {
          select = conn.prepareStatement("SELECT v, NULL FROM " + tableName + " WHERE v = ?");
          t.type.write().set(select, 1, original);
        }
      } else {
        select = conn.prepareStatement("SELECT v, NULL FROM " + tableName);
      }

      select.execute();
      var rs = select.getResultSet();

      if (!rs.next()) {
        throw new RuntimeException("No rows returned");
      }

      // Read the value - use optional reader if expecting NULL
      final A actual;
      if (expected == null) {
        Optional<A> actualOpt = t.type.opt().read().read(rs, 1);
        actual = actualOpt.orElse(null);
      } else {
        actual = t.type.read().read(rs, 1);
      }

      // Read the null value using opt()
      Optional<A> actualNull = t.type.opt().read().read(rs, 2);

      rs.close();
      select.close();

      assertEquals(actual, expected, "value mismatch");
      assertEquals(actualNull, Optional.empty(), "null value mismatch");

    } finally {
      // Drop table
      try (var stmt = conn.createStatement()) {
        stmt.execute("DROP TABLE " + tableName);
      }
    }
  }

  // ==================== Stored Procedure Callable Roundtrip ====================
  // For each scalar type, create an Oracle identity procedure with IN and OUT params,
  // call it via DbProcedure.define().input().out().build(), and verify the value roundtrips.

  static <A> void testCallableRoundtrip(Connection conn, OracleTypeAndExample<A> t)
      throws SQLException {
    String sqlType = t.type.typename().sqlType();

    // Skip types that cannot be used as procedure parameters in Oracle
    if (sqlType.equals("LONG") || sqlType.equals("LONG RAW") || sqlType.equals("JSON")) {
      System.out.println("Callable roundtrip SKIPPED " + sqlType + " (cannot be procedure param)");
      return;
    }

    String safeName =
        "PROC_ID_"
            + sqlType
                .replace("(", "_")
                .replace(")", "_")
                .replace(",", "_")
                .replace(" ", "_")
                .replace("\"", "");
    int uniqueId = tableCounter.incrementAndGet();
    String procName = safeName + "_" + uniqueId;

    // Oracle PL/SQL doesn't allow size/precision constraints on procedure parameters
    String paramType = t.type.typename().sqlTypeNoPrecision();
    conn.createStatement()
        .execute(
            "CREATE OR REPLACE PROCEDURE "
                + procName
                + "(p_in IN "
                + paramType
                + ", p_out OUT "
                + paramType
                + ") IS BEGIN p_out := p_in; END;");
    conn.commit();

    try {
      A input = t.example;
      A expected = t.expected();

      DbProcedure.Def1_1<A, A> proc =
          DbProcedure.define(procName).input(t.type).out(t.type).build();

      A result = proc.call(input).run(conn);

      // Oracle PL/SQL uses unconstrained param types, so we need relaxed comparison:
      // - CHAR/NCHAR: unconstrained CHAR pads to max PL/SQL size, so compare trimmed
      // - TIMESTAMP(n>6): unconstrained TIMESTAMP defaults to precision 6
      boolean match;
      String baseType = paramType.toUpperCase();
      if ((baseType.equals("CHAR") || baseType.equals("NCHAR"))
          && result instanceof String resultStr
          && expected instanceof String expectedStr) {
        match = resultStr.stripTrailing().equals(expectedStr.stripTrailing());
      } else if (baseType.equals("TIMESTAMP")
          && result instanceof LocalDateTime resultLdt
          && expected instanceof LocalDateTime expectedLdt) {
        // Unconstrained TIMESTAMP defaults to precision 6; Oracle rounds (not truncates)
        match = java.time.Duration.between(resultLdt, expectedLdt).abs().toNanos() < 1000;
      } else {
        match = areEqual(result, expected);
      }

      if (!match) {
        throw new RuntimeException(
            "Callable roundtrip failed for "
                + sqlType
                + ": expected '"
                + format(expected)
                + "' but got '"
                + format(result)
                + "'");
      }
      System.out.println("Callable roundtrip " + sqlType + ": PASSED");
    } catch (DatabaseException e) {
      if (e.sqlException().getMessage() != null
          && e.sqlException()
              .getMessage()
              .contains("does not support stored procedure OUT parameters")) {
        System.out.println("Callable roundtrip SKIPPED " + sqlType + " (not supported)");
        return;
      }
      throw e;
    } finally {
      try {
        conn.createStatement().execute("DROP PROCEDURE " + procName);
        conn.commit();
      } catch (SQLException ignored) {
      }
    }
  }

  static <A> void assertEquals(A actual, A expected, String message) {
    if (!areEqual(actual, expected)) {
      throw new RuntimeException(
          message
              + ": actual='"
              + format(actual)
              + "' ("
              + (actual == null ? "null" : actual.getClass().getSimpleName())
              + ") expected='"
              + format(expected)
              + "' ("
              + (expected == null ? "null" : expected.getClass().getSimpleName())
              + ")");
    }
  }

  // ==================== Gap coverage tests ====================

  @Test
  public void testNullAndEmptyCollection() {
    // NULL collection at the column level (valid). Oracle rejects NULL OBJECT entries inside
    // VARRAY/NESTED TABLE with ORA-22805, so this test deliberately covers the cases that are
    // valid: NULL whole-collection and empty collection.
    record Item(String name, Integer qty) {}
    OracleType<Item> itemType =
        OracleTypes.compositeOf(
            "GAP_ITEM_T",
            RowCodec.<Item>namedBuilder()
                .field("NAME", OracleTypes.varchar2Of(50), Item::name)
                .field("QTY", OracleTypes.numberInt, Item::qty)
                .build(Item::new));
    OracleType<List<Item>> itemVArray = OracleVArray.of("GAP_ITEM_VA", 10, itemType);
    OracleType<List<Item>> itemNestedTable = OracleNestedTable.of("GAP_ITEM_NT", itemType);

    var pool = Containers.oraclePool();
    pool.transactor(Transactor.testStrategy())
        .execute(
            conn -> {
              var stmt = conn.createStatement();
              tryExec(stmt, "DROP TABLE gap_null_collection_t CASCADE CONSTRAINTS");
              tryExec(stmt, "DROP TYPE GAP_ITEM_VA FORCE");
              tryExec(stmt, "DROP TYPE GAP_ITEM_NT FORCE");
              tryExec(stmt, "DROP TYPE GAP_ITEM_T FORCE");
              stmt.execute("CREATE TYPE GAP_ITEM_T AS OBJECT (NAME VARCHAR2(50), QTY NUMBER)");
              stmt.execute("CREATE TYPE GAP_ITEM_VA AS VARRAY(10) OF GAP_ITEM_T");
              stmt.execute("CREATE TYPE GAP_ITEM_NT AS TABLE OF GAP_ITEM_T");
              stmt.execute(
                  "CREATE TABLE gap_null_collection_t (id NUMBER, va GAP_ITEM_VA, nt GAP_ITEM_NT)"
                      + " NESTED TABLE nt STORE AS gap_null_nt_store");

              var raw = conn.unwrap(oracle.jdbc.OracleConnection.class);

              // Row 1: NULL collections (whole column NULL)
              var ps1 = raw.prepareStatement("INSERT INTO gap_null_collection_t VALUES (?, ?, ?)");
              ps1.setInt(1, 1);
              itemVArray.opt().write().set(ps1, 2, java.util.Optional.empty());
              itemNestedTable.opt().write().set(ps1, 3, java.util.Optional.empty());
              ps1.executeUpdate();

              // Row 2: empty collections
              var ps2 = raw.prepareStatement("INSERT INTO gap_null_collection_t VALUES (?, ?, ?)");
              ps2.setInt(1, 2);
              itemVArray.write().set(ps2, 2, List.of());
              itemNestedTable.write().set(ps2, 3, List.of());
              ps2.executeUpdate();

              // Row 3: populated
              var ps3 = raw.prepareStatement("INSERT INTO gap_null_collection_t VALUES (?, ?, ?)");
              ps3.setInt(1, 3);
              List<Item> items = List.of(new Item("Keyboard", 1), new Item("Mouse", 2));
              itemVArray.write().set(ps3, 2, items);
              itemNestedTable.write().set(ps3, 3, items);
              ps3.executeUpdate();
              conn.commit();

              var rs =
                  conn.createStatement()
                      .executeQuery("SELECT id, va, nt FROM gap_null_collection_t ORDER BY id");
              // Row 1 - NULL collections
              if (!rs.next()) throw new AssertionError("no row 1");
              var va1 = itemVArray.opt().read().read(rs, 2);
              var nt1 = itemNestedTable.opt().read().read(rs, 3);
              if (va1.isPresent())
                throw new AssertionError("expected NULL VARRAY, got " + va1.get());
              if (nt1.isPresent()) throw new AssertionError("expected NULL NT, got " + nt1.get());

              // Row 2 - empty collections. Oracle returns NULL for empty VARRAY/NESTED TABLE
              // (this is a well-known Oracle quirk — an empty VARRAY and a NULL one are
              // indistinguishable through JDBC), so we test with the opt() read to tolerate both.
              if (!rs.next()) throw new AssertionError("no row 2");
              var va2 = itemVArray.opt().read().read(rs, 2);
              var nt2 = itemNestedTable.opt().read().read(rs, 3);
              if (va2.isPresent() && !va2.get().isEmpty())
                throw new AssertionError("row 2 VARRAY should be empty or null, got " + va2.get());
              if (nt2.isPresent() && !nt2.get().isEmpty())
                throw new AssertionError("row 2 NT should be empty or null, got " + nt2.get());

              // Row 3 - populated
              if (!rs.next()) throw new AssertionError("no row 3");
              var va3 = itemVArray.read().read(rs, 2);
              var nt3 = itemNestedTable.read().read(rs, 3);
              if (va3.size() != 2) throw new AssertionError("row 3 VARRAY size: " + va3.size());
              if (!va3.get(0).name().equals("Keyboard"))
                throw new AssertionError(va3.get(0).name());
              if (nt3.size() != 2) throw new AssertionError("row 3 NT size: " + nt3.size());
              System.out.println(
                  "NULL/empty VARRAY and NESTED TABLE roundtrip OK"
                      + " (Oracle: null elements inside collections are rejected as ORA-22805)");
              return null;
            });
  }

  @Test
  public void testFourLevelNesting() {
    // 4 levels: NESTED TABLE<Country> where Country has VARRAY<Region> where Region has
    // NESTED TABLE<City> where City has scalars. Exercises recursion through all collection
    // kinds at deeper than 3 levels.
    record City(String name, Integer population) {}
    record Region(String name, List<City> cities) {}
    record Country(String name, List<Region> regions) {}

    OracleType<City> cityType =
        OracleTypes.compositeOf(
            "GAP_CITY_T",
            RowCodec.<City>namedBuilder()
                .field("NAME", OracleTypes.varchar2Of(50), City::name)
                .field("POPULATION", OracleTypes.numberInt, City::population)
                .build(City::new));
    OracleType<List<City>> citiesType = OracleNestedTable.of("GAP_CITY_NT", cityType);
    OracleType<Region> regionType =
        OracleTypes.compositeOf(
            "GAP_REGION_T",
            RowCodec.<Region>namedBuilder()
                .field("NAME", OracleTypes.varchar2Of(50), Region::name)
                .field("CITIES", citiesType, Region::cities)
                .build(Region::new));
    OracleType<List<Region>> regionsType = OracleVArray.of("GAP_REGION_VA", 10, regionType);
    OracleType<Country> countryType =
        OracleTypes.compositeOf(
            "GAP_COUNTRY_T",
            RowCodec.<Country>namedBuilder()
                .field("NAME", OracleTypes.varchar2Of(50), Country::name)
                .field("REGIONS", regionsType, Country::regions)
                .build(Country::new));
    OracleType<List<Country>> countriesType = OracleNestedTable.of("GAP_COUNTRY_NT", countryType);

    var pool = Containers.oraclePool();
    pool.transactor(Transactor.testStrategy())
        .execute(
            conn -> {
              var stmt = conn.createStatement();
              tryExec(stmt, "DROP TABLE gap_four_level_t CASCADE CONSTRAINTS");
              tryExec(stmt, "DROP TYPE GAP_COUNTRY_NT FORCE");
              tryExec(stmt, "DROP TYPE GAP_COUNTRY_T FORCE");
              tryExec(stmt, "DROP TYPE GAP_REGION_VA FORCE");
              tryExec(stmt, "DROP TYPE GAP_REGION_T FORCE");
              tryExec(stmt, "DROP TYPE GAP_CITY_NT FORCE");
              tryExec(stmt, "DROP TYPE GAP_CITY_T FORCE");
              stmt.execute(
                  "CREATE TYPE GAP_CITY_T AS OBJECT (NAME VARCHAR2(50), POPULATION NUMBER)");
              stmt.execute("CREATE TYPE GAP_CITY_NT AS TABLE OF GAP_CITY_T");
              stmt.execute(
                  "CREATE TYPE GAP_REGION_T AS OBJECT (NAME VARCHAR2(50), CITIES GAP_CITY_NT)");
              stmt.execute("CREATE TYPE GAP_REGION_VA AS VARRAY(10) OF GAP_REGION_T");
              stmt.execute(
                  "CREATE TYPE GAP_COUNTRY_T AS OBJECT (NAME VARCHAR2(50), REGIONS GAP_REGION_VA)");
              stmt.execute("CREATE TYPE GAP_COUNTRY_NT AS TABLE OF GAP_COUNTRY_T");
              stmt.execute(
                  "CREATE TABLE gap_four_level_t (cs GAP_COUNTRY_NT)"
                      + " NESTED TABLE cs STORE AS gap_four_cs_store");

              var raw = conn.unwrap(oracle.jdbc.OracleConnection.class);
              List<Country> countries =
                  List.of(
                      new Country(
                          "Norway",
                          List.of(
                              new Region("Oslo", List.of(new City("Oslo", 700_000))),
                              new Region(
                                  "Vestland",
                                  List.of(
                                      new City("Bergen", 285_000),
                                      new City("Haugesund", 37_000))))));
              var ps = raw.prepareStatement("INSERT INTO gap_four_level_t VALUES (?)");
              countriesType.write().set(ps, 1, countries);
              ps.executeUpdate();
              conn.commit();

              var rs = conn.createStatement().executeQuery("SELECT cs FROM gap_four_level_t");
              if (!rs.next()) throw new AssertionError("no row");
              List<Country> decoded = countriesType.read().read(rs, 1);

              if (decoded.size() != 1) throw new AssertionError("countries: " + decoded.size());
              Country c = decoded.get(0);
              if (!c.name().equals("Norway")) throw new AssertionError(c.name());
              if (c.regions().size() != 2)
                throw new AssertionError("regions: " + c.regions().size());
              Region vestland = c.regions().get(1);
              if (vestland.cities().size() != 2)
                throw new AssertionError("cities: " + vestland.cities().size());
              if (!vestland.cities().get(0).name().equals("Bergen"))
                throw new AssertionError(vestland.cities().get(0).name());
              System.out.println(
                  "4-level nesting NESTED TABLE→OBJECT→VARRAY→OBJECT→NESTED TABLE OK");
              return null;
            });
  }

  // ==================== Auto-derived composite matrix ====================
  //
  // For every scalar in All that has supportsComposite=true, we auto-derive three shapes —
  // single-attribute OBJECT, VARRAY, NESTED TABLE — and run the usual testCase roundtrip.
  // The testCase harness already handles setupSql, nested-table STORE AS, etc., so each
  // derivation is just a new OracleTypeAndExample<?> with its own setupSql.
  //
  // Purpose: catch top-level-vs-composite asymmetry bugs (like the numberAsInt-in-STRUCT
  // bug: BigDecimal cast to Integer/Long worked at top level but blew up inside STRUCT
  // attribute reads).

  /** Wraps a scalar as the sole VAL attribute of an auto-generated OBJECT type. */
  static <A> OracleTypeAndExample<Tuple.Tuple1<A>> toObjectAttrExample(
      OracleTypeAndExample<A> scalar, String objectTypeName) {
    String elementSql = scalar.type.typename().sqlType();
    RowCodecNamed<Tuple.Tuple1<A>> codec =
        RowCodec.<Tuple.Tuple1<A>>namedBuilder()
            .field("VAL", scalar.type, Tuple.Tuple1::_1)
            .build(v -> new Tuple.Tuple1.Impl<>(v));
    OracleType<Tuple.Tuple1<A>> objType = OracleTypes.compositeOf(objectTypeName, codec);
    Tuple.Tuple1<A> example = new Tuple.Tuple1.Impl<>(scalar.example);

    List<String> setup = new ArrayList<>(scalar.setupSql);
    setup.add("DROP TYPE " + objectTypeName + " FORCE");
    setup.add("CREATE TYPE " + objectTypeName + " AS OBJECT (VAL " + elementSql + ")");
    return new OracleTypeAndExample<>(objType, example, setup).noIdentity();
  }

  /** Wraps a scalar as the element type of an auto-generated VARRAY. */
  static <A> OracleTypeAndExample<List<A>> toVArrayExample(
      OracleTypeAndExample<A> scalar, String varrayTypeName) {
    String elementSql = scalar.type.typename().sqlType();
    OracleType<List<A>> vaType = OracleVArray.of(varrayTypeName, 5, scalar.type);
    List<A> example = List.of(scalar.example);

    List<String> setup = new ArrayList<>(scalar.setupSql);
    setup.add("DROP TYPE " + varrayTypeName + " FORCE");
    setup.add("CREATE TYPE " + varrayTypeName + " AS VARRAY(5) OF " + elementSql);
    return new OracleTypeAndExample<>(vaType, example, setup).noIdentity();
  }

  /** Wraps a scalar as the element type of an auto-generated NESTED TABLE. */
  static <A> OracleTypeAndExample<List<A>> toNestedTableExample(
      OracleTypeAndExample<A> scalar, String ntTypeName) {
    String elementSql = scalar.type.typename().sqlType();
    OracleType<List<A>> ntType = OracleNestedTable.of(ntTypeName, scalar.type);
    List<A> example = List.of(scalar.example);

    List<String> setup = new ArrayList<>(scalar.setupSql);
    setup.add("DROP TYPE " + ntTypeName + " FORCE");
    setup.add("CREATE TYPE " + ntTypeName + " AS TABLE OF " + elementSql);
    return new OracleTypeAndExample<>(ntType, example, setup).noIdentity();
  }

  /**
   * Scalars Oracle accepts inside OBJECT/VARRAY/NESTED TABLE columns. Excludes:
   *
   * <ul>
   *   <li>Oracle limitations (genuinely unavailable as user-type attributes / VARRAY elements):
   *       LOBs, JSON, INTERVAL, TIME ZONE variants, BOOLEAN (native).
   *   <li>Types that are themselves composite (TEST_ALLTYPES, ADDRESS_T, PHONE_LIST, etc.).
   * </ul>
   */
  private static boolean scalarSupportsAutoComposite(String sqlType) {
    if (sqlType.contains("CLOB") || sqlType.contains("BLOB")) return false;
    if (sqlType.startsWith("RAW")) return false;
    if (sqlType.equals("JSON") || sqlType.contains("JSON")) return false;
    if (sqlType.contains("INTERVAL")) return false;
    if (sqlType.contains("TIME ZONE")) return false;
    if (sqlType.contains("BOOLEAN")) return false;
    if (sqlType.contains("_T") || sqlType.contains("PHONE_LIST")) return false;
    if (sqlType.startsWith("TEST_")) return false;
    return true;
  }

  @Test
  public void testScalarInsideComposites() {
    // For every supported scalar, auto-derive OBJECT / VARRAY / NESTED TABLE wrappers
    // and roundtrip each. Catches any bug where a scalar's top-level read differs from
    // its inside-composite read path (regression guard for the numberAsInt-in-STRUCT fix).
    var derived = new ArrayList<OracleTypeAndExample<?>>();
    var seenSqlTypes = new HashSet<String>();
    int idx = 0;
    for (OracleTypeAndExample<?> scalar : All) {
      if (!scalar.supportsComposite) continue;
      if (scalar.example == null) continue;
      if (scalar.useExpectedRoundtrip) continue;
      String sqlType = scalar.type.typename().sqlType();
      if (!scalarSupportsAutoComposite(sqlType)) continue;
      if (!seenSqlTypes.add(sqlType)) continue;

      String suffix = "AUTO" + (idx++);
      derived.add(toObjectAttrExample(scalar, "GAP_" + suffix + "_T"));
      derived.add(toVArrayExample(scalar, "GAP_" + suffix + "_VA"));
      derived.add(toNestedTableExample(scalar, "GAP_" + suffix + "_NT"));
    }

    System.out.println(
        "Auto-derived "
            + derived.size()
            + " composite tests from "
            + seenSqlTypes.size()
            + " unique scalar types");

    // Phase 1: create every needed OBJECT/VARRAY/NT type upfront (sequential to avoid races).
    withConnection(
        conn -> {
          var executed = new HashSet<String>();
          for (OracleTypeAndExample<?> t : derived) {
            try (var stmt = conn.createStatement()) {
              for (String sql : t.setupSql) {
                if (!executed.add(sql)) continue;
                try {
                  stmt.execute(sql);
                } catch (SQLException e) {
                  // Ignore 955 (name exists), 2303 (type has dependents), 4043 (type missing
                  // on DROP), 942 (table missing)
                  if (!e.getMessage().contains("ORA-00955")
                      && !e.getMessage().contains("ORA-02303")
                      && !e.getMessage().contains("ORA-04043")
                      && !e.getMessage().contains("ORA-00942")) {
                    throw e;
                  }
                }
              }
            }
          }
          conn.commit();
          return null;
        });

    // Phase 2: run each derived test in parallel against the pool.
    var failures =
        derived.parallelStream()
            .flatMap(
                t -> {
                  var errs = new ArrayList<String>();
                  try {
                    withConnection(
                        conn -> {
                          testCase(conn, t);
                          return null;
                        });
                  } catch (Throwable ex) {
                    errs.add(
                        "Composite FAILED " + t.type.typename().sqlType() + ": " + ex.getMessage());
                  }
                  return errs.stream();
                })
            .toList();

    if (!failures.isEmpty()) {
      throw new AssertionError(
          "Composite derivation failures ("
              + failures.size()
              + "):\n  "
              + String.join("\n  ", failures));
    }
  }

  @Test
  public void testNumberAsIntInsideStruct() {
    // numberAsInt(p) / numberAsLong(p) must read correctly as an attribute of an OBJECT.
    // Regression test: the prior implementation cast the JDBC value directly to Integer/Long,
    // which worked at top-level (driver unboxed eagerly) but threw
    // "class java.math.BigDecimal cannot be cast to class java.lang.Integer" inside STRUCT
    // attribute decoding, where the driver always hands BigDecimal regardless of precision.
    record Score(String name, int points, long total) {}
    OracleType<Score> scoreType =
        OracleTypes.compositeOf(
            "GAP_SCORE_T",
            RowCodec.<Score>namedBuilder()
                .field("NAME", OracleTypes.varchar2Of(50), Score::name)
                .field("POINTS", OracleTypes.numberAsInt(5), Score::points)
                .field("TOTAL", OracleTypes.numberAsLong(15), Score::total)
                .build(Score::new));

    // Write via Oracle's STRUCT constructor SQL (no PreparedStatement binding so we don't need
    // the Hikari pool's OracleConnection unwrap dance). The fix under test is on the READ path.
    var tx = Containers.oraclePool().transactor(Transactor.testStrategy());
    Score expected = new Score("alice", 42, 12_345_678_901L);
    Score decoded =
        tx.execute(
            conn -> {
              tryRun(conn, Fragment.of("DROP TABLE gap_score_holder CASCADE CONSTRAINTS"));
              tryRun(conn, Fragment.of("DROP TYPE GAP_SCORE_T FORCE"));
              Fragment.of(
                      "CREATE TYPE GAP_SCORE_T AS OBJECT ("
                          + "NAME VARCHAR2(50), POINTS NUMBER(5), TOTAL NUMBER(15))")
                  .execute()
                  .run(conn);
              Fragment.of("CREATE TABLE gap_score_holder (s GAP_SCORE_T)").execute().run(conn);
              Fragment.of(
                      "INSERT INTO gap_score_holder VALUES (GAP_SCORE_T('alice', 42, 12345678901))")
                  .execute()
                  .run(conn);
              return Fragment.of("SELECT s FROM gap_score_holder")
                  .queryExactlyOne(scoreType)
                  .run(conn);
            });
    if (!expected.equals(decoded)) {
      throw new AssertionError("mismatch: " + decoded + " vs " + expected);
    }
  }

  /**
   * TIMESTAMP WITH TIME ZONE must preserve *named zone regions*, not just their current offset.
   *
   * <p>This is the whole reason the library maps Oracle TSTZ to {@link ZonedDateTime} rather than
   * {@link OffsetDateTime}. The 13-byte on-disk TSTZ format holds either a fixed offset or a region
   * name; {@code ZonedDateTime} can represent both, while {@code OffsetDateTime} collapses every
   * region to its current offset — which loses DST-awareness on later reads.
   *
   * <p>Scenarios covered:
   *
   * <ol>
   *   <li>Named zone region ({@code America/Los_Angeles}) in winter (PST, UTC-8)
   *   <li>Same named region in summer (PDT, UTC-7) — verifies the zone ID itself is persisted, not
   *       the current offset
   *   <li>Fixed offset ({@code +05:30} — India) — verifies the offset path still works
   *   <li>Region round-trip then rendering: reloading the value into a different session TZ should
   *       yield the same instant AND the same zone ID
   * </ol>
   */
  @Test
  public void testTimestampWithTimeZonePreservesZoneRegion() {
    var tx = Containers.oraclePool().transactor(Transactor.testStrategy());
    String table = uniqueTableName("zdt_region");

    var winterLA =
        ZonedDateTime.of(2024, 1, 15, 10, 30, 0, 0, java.time.ZoneId.of("America/Los_Angeles"));
    var summerLA =
        ZonedDateTime.of(2024, 7, 15, 10, 30, 0, 0, java.time.ZoneId.of("America/Los_Angeles"));
    var berlin = ZonedDateTime.of(2024, 6, 15, 9, 0, 0, 0, java.time.ZoneId.of("Europe/Berlin"));
    var tokyo = ZonedDateTime.of(2024, 3, 10, 15, 0, 0, 0, java.time.ZoneId.of("Asia/Tokyo"));
    var fixedOffset = ZonedDateTime.of(2024, 6, 15, 14, 30, 0, 0, ZoneOffset.ofHoursMinutes(5, 30));

    List<ZonedDateTime> samples = List.of(winterLA, summerLA, berlin, tokyo, fixedOffset);

    // CREATE + INSERT + SELECT all in one tx — testStrategy rolls back on exit, so multi-tx
    // splits would lose the inserts before the read.
    List<ZonedDateTime> roundTripped =
        tx.execute(
            conn -> {
              Fragment.of(
                      "CREATE TABLE "
                          + table
                          + " (id NUMBER(5) PRIMARY KEY, ts TIMESTAMP WITH TIME ZONE)")
                  .execute()
                  .run(conn);
              for (int i = 0; i < samples.size(); i++) {
                Fragment.builder()
                    .append("INSERT INTO " + table + " (id, ts) VALUES (")
                    .value(OracleTypes.numberAsInt(5), i)
                    .append(", ")
                    .value(OracleTypes.timestampWithTimeZone, samples.get(i))
                    .append(")")
                    .execute()
                    .run(conn);
              }
              return Fragment.of("SELECT ts FROM " + table + " ORDER BY id")
                  .queryAll(OracleTypes.timestampWithTimeZone)
                  .run(conn);
            });

    for (int i = 0; i < samples.size(); i++) {
      ZonedDateTime expected = samples.get(i);
      ZonedDateTime actual = roundTripped.get(i);

      // Instant must match exactly — that's the basic "same moment in time" check.
      if (!expected.toInstant().equals(actual.toInstant())) {
        throw new AssertionError(
            "Instant mismatch for "
                + expected
                + " → "
                + actual
                + " (instants "
                + expected.toInstant()
                + " vs "
                + actual.toInstant()
                + ")");
      }

      // Zone identity must match — the whole point of using ZonedDateTime.
      if (!expected.getZone().equals(actual.getZone())) {
        throw new AssertionError(
            "Zone mismatch for "
                + expected
                + " → "
                + actual
                + " (zones "
                + expected.getZone()
                + " vs "
                + actual.getZone()
                + ")");
      }
    }

    // DST-awareness: winter and summer values at the same named region should have
    // *different* offsets at their respective instants, even though the zone ID is the same.
    // This is the specific behavior that OffsetDateTime can't express.
    if (winterLA.getOffset().equals(summerLA.getOffset())) {
      throw new AssertionError(
          "Sanity check failed — winter/summer LA should have different offsets");
    }
    int winterIdx = samples.indexOf(winterLA);
    int summerIdx = samples.indexOf(summerLA);
    if (!roundTripped.get(winterIdx).getOffset().equals(winterLA.getOffset())) {
      throw new AssertionError("Winter LA lost its DST offset: " + roundTripped.get(winterIdx));
    }
    if (!roundTripped.get(summerIdx).getOffset().equals(summerLA.getOffset())) {
      throw new AssertionError("Summer LA lost its DST offset: " + roundTripped.get(summerIdx));
    }
  }

  /**
   * Session-timezone independence: the same row should decode to the same {@link ZonedDateTime}
   * (same instant, same zone) regardless of which timezone the reading session is configured with.
   * Region names must survive unchanged; fixed offsets must survive unchanged.
   */
  @Test
  public void testTimestampWithTimeZoneIsSessionTzIndependent() {
    var tx = Containers.oraclePool().transactor(Transactor.testStrategy());
    String table = uniqueTableName("zdt_session");

    var value =
        ZonedDateTime.of(2024, 7, 15, 10, 30, 0, 0, java.time.ZoneId.of("America/Los_Angeles"));

    // Setup + all session-TZ reads in one tx block — testStrategy rolls back on exit so the
    // table doesn't outlive the test.
    tx.execute(
        conn -> {
          Fragment.of("CREATE TABLE " + table + " (ts TIMESTAMP WITH TIME ZONE)")
              .execute()
              .run(conn);
          Fragment.builder()
              .append("INSERT INTO " + table + " (ts) VALUES (")
              .value(OracleTypes.timestampWithTimeZone, value)
              .append(")")
              .execute()
              .run(conn);
          for (String sessionTz :
              List.of("UTC", "America/New_York", "Asia/Tokyo", "Europe/Berlin")) {
            Fragment.of("ALTER SESSION SET TIME_ZONE = '" + sessionTz + "'").execute().run(conn);
            ZonedDateTime decoded =
                Fragment.of("SELECT ts FROM " + table)
                    .queryExactlyOne(OracleTypes.timestampWithTimeZone)
                    .run(conn);
            if (!decoded.toInstant().equals(value.toInstant())) {
              throw new AssertionError(
                  "Session TZ "
                      + sessionTz
                      + ": instant mismatch: "
                      + decoded.toInstant()
                      + " vs "
                      + value.toInstant());
            }
            if (!decoded.getZone().equals(value.getZone())) {
              throw new AssertionError(
                  "Session TZ "
                      + sessionTz
                      + ": zone mismatch: "
                      + decoded.getZone()
                      + " vs "
                      + value.getZone());
            }
          }
          return null;
        });
  }

  /** Nullable column and null round-trip for TIMESTAMP WITH TIME ZONE → Optional<ZonedDateTime>. */
  @Test
  public void testTimestampWithTimeZoneNullable() {
    var tx = Containers.oraclePool().transactor(Transactor.testStrategy());
    String table = uniqueTableName("zdt_null");

    var value =
        ZonedDateTime.of(2024, 7, 15, 10, 30, 0, 0, java.time.ZoneId.of("America/Los_Angeles"));

    List<Optional<ZonedDateTime>> decoded =
        tx.execute(
            conn -> {
              Fragment.of(
                      "CREATE TABLE "
                          + table
                          + " (id NUMBER(5) PRIMARY KEY, ts TIMESTAMP WITH TIME ZONE)")
                  .execute()
                  .run(conn);
              Fragment.builder()
                  .append("INSERT INTO " + table + " (id, ts) VALUES (1, ")
                  .value(OracleTypes.timestampWithTimeZone, value)
                  .append(")")
                  .execute()
                  .run(conn);
              Fragment.of("INSERT INTO " + table + " (id, ts) VALUES (2, NULL)")
                  .execute()
                  .run(conn);
              return Fragment.of("SELECT ts FROM " + table + " ORDER BY id")
                  .queryAll(OracleTypes.timestampWithTimeZone.opt())
                  .run(conn);
            });

    if (decoded.size() != 2) {
      throw new AssertionError("Expected 2 rows, got " + decoded.size());
    }
    if (decoded.get(0).isEmpty()) {
      throw new AssertionError("Non-null row decoded as empty");
    }
    if (!decoded.get(0).get().toInstant().equals(value.toInstant())
        || !decoded.get(0).get().getZone().equals(value.getZone())) {
      throw new AssertionError("Non-null round-trip mismatch: " + decoded.get(0).get());
    }
    if (decoded.get(1).isPresent()) {
      throw new AssertionError("NULL row decoded as present: " + decoded.get(1));
    }
  }

  private static void tryRun(java.sql.Connection conn, Fragment fragment) {
    try {
      fragment.execute().run(conn);
    } catch (Exception ignored) {
      // best-effort cleanup
    }
  }

  private static void tryExec(java.sql.Statement stmt, String sql) {
    try {
      stmt.execute(sql);
    } catch (SQLException ignored) {
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

    // For BigDecimal, use compareTo to handle different scales
    if (expected instanceof BigDecimal && actual instanceof BigDecimal) {
      return ((BigDecimal) actual).compareTo((BigDecimal) expected) == 0;
    }

    // For OffsetDateTime, compare by instant (TIMESTAMP WITH LOCAL TIME ZONE converts to session
    // tz)
    if (expected instanceof OffsetDateTime && actual instanceof OffsetDateTime) {
      return ((OffsetDateTime) actual).toInstant().equals(((OffsetDateTime) expected).toInstant());
    }
    // For ZonedDateTime, compare by instant only in this shared helper — Oracle's JSON_OBJECT
    // renders TSTZ values in ISO_OFFSET_DATE_TIME format, stripping the zone region before the
    // library ever sees the JSON. The native (non-JSON) TSTZ round-trip is covered separately
    // in testTimestampWithTimeZonePreservesZoneRegion, which asserts zone identity explicitly.
    if (expected instanceof ZonedDateTime && actual instanceof ZonedDateTime) {
      return ((ZonedDateTime) actual).toInstant().equals(((ZonedDateTime) expected).toInstant());
    }

    // For Json, parse and compare structures (Oracle normalizes JSON formatting)
    if (expected instanceof Json && actual instanceof Json) {
      try {
        JsonValue v1 = JsonValue.parse(((Json) actual).value());
        JsonValue v2 = JsonValue.parse(((Json) expected).value());
        return v1.equals(v2);
      } catch (Exception e) {
        // If parsing fails, fall back to string comparison
        return ((Json) actual).value().equals(((Json) expected).value());
      }
    }

    // Drill into List (element-wise) and Tuple (component-wise) so auto-derived composite
    // wrappers get the same scalar-specific equality as top-level (BigDecimal.compareTo,
    // byte[] via Arrays.equals, etc.).
    if (expected instanceof List<?> expList && actual instanceof List<?> actList) {
      if (expList.size() != actList.size()) return false;
      for (int i = 0; i < expList.size(); i++) {
        if (!areEqual(actList.get(i), expList.get(i))) return false;
      }
      return true;
    }
    if (expected instanceof Tuple expTuple && actual instanceof Tuple actTuple) {
      Object[] expArr = expTuple.asArray();
      Object[] actArr = actTuple.asArray();
      if (expArr.length != actArr.length) return false;
      for (int i = 0; i < expArr.length; i++) {
        if (!areEqual(actArr[i], expArr[i])) return false;
      }
      return true;
    }

    return actual.equals(expected);
  }

  static <A> String format(A a) {
    return switch (a) {
      case null -> "null";
      case byte[] bytes -> bytesToHex(bytes);
      case Object[] objects -> Arrays.deepToString(objects);
      default -> a.toString();
    };
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
