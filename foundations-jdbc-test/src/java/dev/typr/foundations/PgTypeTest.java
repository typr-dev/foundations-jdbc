package dev.typr.foundations;

import dev.typr.foundations.data.*;
import dev.typr.foundations.data.JsonValue;
import dev.typr.foundations.data.Vector;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import org.postgresql.geometric.*;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.util.PGInterval;

public class PgTypeTest {

  private static final AtomicInteger tableCounter = new AtomicInteger(0);

  private static String uniqueTableName(String prefix) {
    return prefix + "_" + tableCounter.incrementAndGet();
  }

  // PostgreSQL only supports microsecond precision (6 digits), but Java's now() methods
  // return nanosecond precision (9 digits). Truncate to ensure roundtrip equality.
  private static LocalTime nowTime() {
    return LocalTime.now().truncatedTo(ChronoUnit.MICROS);
  }

  private static LocalDateTime nowDateTime() {
    return LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
  }

  private static Instant nowInstant() {
    return Instant.now().truncatedTo(ChronoUnit.MICROS);
  }

  private static OffsetTime nowOffsetTime() {
    return OffsetTime.now().truncatedTo(ChronoUnit.MICROS);
  }

  record TestPair<A>(A t0, Optional<A> t1) {}

  // Simple record and parsers for JSON-encoded row type testing
  record Item(String name, int quantity) {}

  static RowCodec<Item> itemCodec =
      RowCodec.<Item>builder()
          .field(PgTypes.text, Item::name)
          .field(PgTypes.int4, Item::quantity)
          .build(Item::new);

  static RowCodecNamed<Item> namedItemCodec =
      RowCodec.<Item>namedBuilder()
          .field("name", PgTypes.text, Item::name)
          .field("quantity", PgTypes.int4, Item::quantity)
          .build(Item::new);

  record PgTypeAndExample<A>(
      PgType<A> type,
      A example,
      boolean hasIdentity,
      boolean streamingWorks,
      boolean compositeTextWorks) {
    public PgTypeAndExample(PgType<A> type, A example) {
      this(type, example, true, true, true);
    }

    public PgTypeAndExample<A> noStreaming() {
      return new PgTypeAndExample<>(type, example, hasIdentity, false, compositeTextWorks);
    }

    public PgTypeAndExample<A> noIdentity() {
      return new PgTypeAndExample<>(type, example, false, streamingWorks, compositeTextWorks);
    }

    public PgTypeAndExample<A> noCompositeText() {
      return new PgTypeAndExample<>(type, example, hasIdentity, streamingWorks, false);
    }
  }

  /** Auto-generate a singleton list test entry for one element entry. */
  static <A> PgTypeAndExample<List<A>> singletonListEntry(PgTypeAndExample<A> elem) {
    return new PgTypeAndExample<>(
        elem.type().array(),
        List.of(elem.example()),
        elem.hasIdentity(),
        elem.streamingWorks(),
        elem.compositeTextWorks());
  }

  /** Auto-generate an empty list test entry for a type (once per type). */
  static <A> PgTypeAndExample<List<A>> emptyListEntry(PgTypeAndExample<A> elem) {
    return new PgTypeAndExample<>(
        elem.type().array(),
        List.of(),
        elem.hasIdentity(),
        elem.streamingWorks(),
        elem.compositeTextWorks());
  }

  /** Auto-generate a multi-element list test entry combining all examples for a type. */
  static <A> PgTypeAndExample<List<A>> multiListEntry(List<PgTypeAndExample<A>> sameTypeEntries) {
    var first = sameTypeEntries.get(0);
    List<A> values = sameTypeEntries.stream().map(PgTypeAndExample::example).toList();
    return new PgTypeAndExample<>(
        first.type().array(),
        values,
        first.hasIdentity(),
        first.streamingWorks(),
        first.compositeTextWorks());
  }

  /**
   * Auto-generate a nested list test entry (PG multi-dim array): List of single-element lists.
   * Composite-text round-trip is skipped — SQL literal encoding of nested arrays is type-specific
   * and covered by dedicated tests; the {@code .array().array()} combinator is verified via the
   * native {@code createArrayOf} path instead.
   */
  static <A> PgTypeAndExample<List<List<A>>> nestedListEntry(PgTypeAndExample<A> elem) {
    return new PgTypeAndExample<>(
            elem.type().array().array(),
            List.of(List.of(elem.example())),
            elem.hasIdentity(),
            elem.streamingWorks(),
            elem.compositeTextWorks())
        .noCompositeText();
  }

  /** Should we auto-generate list test entries for this scalar entry? */
  static boolean hasListSupport(PgTypeAndExample<?> elem) {
    return elem.type().pgArrayCodec().isPresent()
        && !elem.type().typename().sqlType().contains("[]");
  }


  List<PgTypeAndExample<?>> Elements =
      List.<PgTypeAndExample<?>>of(
          // ==================== ACL Item Types ====================
          new PgTypeAndExample<>(PgTypes.aclitem, new AclItem("postgres=r*w/postgres")),

          // ==================== Boolean Types ====================
          new PgTypeAndExample<>(PgTypes.bool, true),
          new PgTypeAndExample<>(PgTypes.bool, false),

          // ==================== Bit String Types ====================
          new PgTypeAndExample<>(PgTypes.bit, new Bit("1")),
          new PgTypeAndExample<>(PgTypes.bit, new Bit("0")),
          new PgTypeAndExample<>(PgTypes.bit(8), new Bit("10110011")),
          new PgTypeAndExample<>(PgTypes.bit(8), new Bit("00000000")),
          new PgTypeAndExample<>(PgTypes.varbit, new Varbit("1")),
          new PgTypeAndExample<>(PgTypes.varbit, new Varbit("101")),
          new PgTypeAndExample<>(PgTypes.varbit, new Varbit("00000000")),

          // ==================== Geometric Types ====================
          new PgTypeAndExample<>(PgTypes.box, new PGbox(42, 42, 42, 42)).noIdentity(),
          new PgTypeAndExample<>(PgTypes.box, new PGbox(-100, -50, 100, 50)).noIdentity(),
          new PgTypeAndExample<>(PgTypes.circle, new PGcircle(new PGpoint(0.01, 42.34), 101.2))
              .noIdentity(),
          new PgTypeAndExample<>(PgTypes.circle, new PGcircle(new PGpoint(0, 0), 0)).noIdentity(),
          new PgTypeAndExample<>(PgTypes.line, new PGline(1.1, 2.2, 3.3)).noIdentity(),
          new PgTypeAndExample<>(PgTypes.lseg, new PGlseg(1.1, 2.2, 3.3, 4.4)).noIdentity(),
          new PgTypeAndExample<>(
                  PgTypes.path,
                  new PGpath(new PGpoint[] {new PGpoint(1.1, 2.2), new PGpoint(3.3, 4.4)}, true))
              .noIdentity(),
          new PgTypeAndExample<>(PgTypes.point, new PGpoint(1.1, 2.2)).noIdentity(),
          new PgTypeAndExample<>(PgTypes.point, new PGpoint(0, 0)).noIdentity(),
          new PgTypeAndExample<>(
                  PgTypes.polygon,
                  new PGpolygon(new PGpoint[] {new PGpoint(1.1, 2.2), new PGpoint(3.3, 4.4)}))
              .noIdentity(),

          // ==================== Character Types ====================
          new PgTypeAndExample<>(PgTypes.bpchar(5), "377  "),
          new PgTypeAndExample<>(PgTypes.bpchar, "377"),
          new PgTypeAndExample<>(PgTypes.bpchar, ""),
          new PgTypeAndExample<>(PgTypes.text, ",.;{}[]-//#®✅"),
          new PgTypeAndExample<>(PgTypes.text, ""),
          new PgTypeAndExample<>(PgTypes.text, "Line1\nLine2\tTabbed"),
          new PgTypeAndExample<>(PgTypes.text, "Quote\"Test'Single"),
          new PgTypeAndExample<>(PgTypes.text, "Emoji: 😀🎉🚀"),

          // ==================== Binary Types ====================
          new PgTypeAndExample<>(PgTypes.bytea, new byte[] {-1, 1, 127}),
          new PgTypeAndExample<>(PgTypes.bytea, new byte[] {}),
          new PgTypeAndExample<>(PgTypes.bytea, new byte[] {0, 0, 0}),
          new PgTypeAndExample<>(PgTypes.bytea, new byte[] {(byte) 0xFF, (byte) 0xFE, (byte) 0xFD}),

          // ==================== Date/Time Types ====================
          new PgTypeAndExample<>(PgTypes.date, LocalDate.now()),
          new PgTypeAndExample<>(PgTypes.date, LocalDate.of(1970, 1, 1)),
          new PgTypeAndExample<>(PgTypes.date, LocalDate.of(2099, 12, 31)),
          new PgTypeAndExample<>(PgTypes.time, nowTime()),
          new PgTypeAndExample<>(PgTypes.time, LocalTime.of(0, 0, 0)),
          new PgTypeAndExample<>(PgTypes.time, LocalTime.of(23, 59, 59, 999999000)),
          new PgTypeAndExample<>(PgTypes.timestamp, nowDateTime()),
          new PgTypeAndExample<>(PgTypes.timestamp, LocalDateTime.of(1970, 1, 1, 0, 0, 0)),
          new PgTypeAndExample<>(PgTypes.timestamptz, nowInstant()),
          new PgTypeAndExample<>(PgTypes.timestamptz, Instant.EPOCH),
          new PgTypeAndExample<>(PgTypes.timetz, nowOffsetTime()),
          new PgTypeAndExample<>(PgTypes.interval, new PGInterval(1, 2, 3, 4, 5, 6.666)),
          new PgTypeAndExample<>(PgTypes.interval, new PGInterval(0, 0, 0, 0, 0, 0)).noIdentity(),

          // ==================== Numeric Types ====================
          new PgTypeAndExample<>(PgTypes.int2, (short) 42),
          new PgTypeAndExample<>(PgTypes.int2, Short.MIN_VALUE),
          new PgTypeAndExample<>(PgTypes.int2, Short.MAX_VALUE),
          new PgTypeAndExample<>(PgTypes.int2, (short) 0),
          new PgTypeAndExample<>(PgTypes.int4, 42),
          new PgTypeAndExample<>(PgTypes.int4, Integer.MIN_VALUE),
          new PgTypeAndExample<>(PgTypes.int4, Integer.MAX_VALUE),
          new PgTypeAndExample<>(PgTypes.int4, 0),
          new PgTypeAndExample<>(PgTypes.int8, 42L),
          new PgTypeAndExample<>(PgTypes.int8, Long.MIN_VALUE),
          new PgTypeAndExample<>(PgTypes.int8, Long.MAX_VALUE),
          new PgTypeAndExample<>(PgTypes.int8, 0L),
          new PgTypeAndExample<>(PgTypes.float4, 42.42f),
          new PgTypeAndExample<>(PgTypes.float4, 0.0f),
          new PgTypeAndExample<>(PgTypes.float4, 1.0E-38f),
          new PgTypeAndExample<>(PgTypes.float8, 42.42),
          new PgTypeAndExample<>(PgTypes.float8, 0.0),
          new PgTypeAndExample<>(PgTypes.float8, Double.MAX_VALUE),
          new PgTypeAndExample<>(PgTypes.numeric, new BigDecimal("0.002")),
          new PgTypeAndExample<>(PgTypes.numeric, BigDecimal.ZERO),
          new PgTypeAndExample<>(PgTypes.numeric, new BigDecimal("-99999999999999.999999999999")),
          new PgTypeAndExample<>(PgTypes.numeric, new BigDecimal("99999999999999.999999999999")),
          new PgTypeAndExample<>(PgTypes.smallint, (short) 42),
          new PgTypeAndExample<>(PgTypes.money, new Money("42.22")),
          new PgTypeAndExample<>(PgTypes.money, new Money("0.00")),
          new PgTypeAndExample<>(PgTypes.money, new Money("-999.99")),

          // ==================== Vector Types ====================
          new PgTypeAndExample<>(PgTypes.int2vector, new Int2Vector(new short[] {1, 2, 3})),
          new PgTypeAndExample<>(PgTypes.oidvector, new OidVector(new int[] {1, 2, 3})),
          new PgTypeAndExample<>(PgTypes.vector, new Vector(new float[] {1.0f, 2.0f, 3.0f})),
          new PgTypeAndExample<>(PgTypes.vector, new Vector(new float[] {0.0f, 0.0f, 0.0f})),

          // ==================== Identifier Types ====================
          new PgTypeAndExample<>(PgTypes.name, "my_table_name"),
          new PgTypeAndExample<>(PgTypes.name, "a"),
          new PgTypeAndExample<>(
              PgTypes.name, "this_is_a_very_long_identifier_name_close_to_63_chars_limit"),

          // ==================== Network Types ====================
          new PgTypeAndExample<>(PgTypes.inet, new Inet("10.1.0.0")),
          new PgTypeAndExample<>(PgTypes.inet, new Inet("192.168.1.1")),
          new PgTypeAndExample<>(PgTypes.inet, new Inet("255.255.255.255")),
          new PgTypeAndExample<>(PgTypes.inet, new Inet("0.0.0.0")),
          new PgTypeAndExample<>(PgTypes.cidr, new Cidr("192.168.1.0/24")),
          new PgTypeAndExample<>(PgTypes.cidr, new Cidr("10.0.0.0/8")),
          new PgTypeAndExample<>(PgTypes.cidr, new Cidr("172.16.0.0/12")),
          new PgTypeAndExample<>(PgTypes.macaddr, new MacAddr("08:00:2b:01:02:03")),
          new PgTypeAndExample<>(PgTypes.macaddr, new MacAddr("00:00:00:00:00:00")),
          new PgTypeAndExample<>(PgTypes.macaddr, new MacAddr("ff:ff:ff:ff:ff:ff")),
          new PgTypeAndExample<>(PgTypes.macaddr8, new MacAddr8("08:00:2b:01:02:03:04:05")),
          new PgTypeAndExample<>(PgTypes.macaddr8, new MacAddr8("00:00:00:00:00:00:00:00")),
          new PgTypeAndExample<>(PgTypes.macaddr8, new MacAddr8("ff:ff:ff:ff:ff:ff:ff:ff")),

          // ==================== Key-Value Types ====================
          new PgTypeAndExample<>(PgTypes.hstore, Map.of(",.;{}[]-//#®✅", ",.;{}[]-//#®✅")),
          new PgTypeAndExample<>(PgTypes.hstore, Map.of()),
          new PgTypeAndExample<>(PgTypes.hstore, Map.of("key1", "value1", "key2", "value2")),

          // ==================== JSON Types ====================
          new PgTypeAndExample<>(PgTypes.json, new Json("{\"A\": 42}")).noIdentity(),
          new PgTypeAndExample<>(PgTypes.json, new Json("{}")).noIdentity(),
          new PgTypeAndExample<>(PgTypes.json, new Json("[]")).noIdentity(),
          new PgTypeAndExample<>(PgTypes.json, new Json("null")).noIdentity(),
          new PgTypeAndExample<>(PgTypes.json, new Json("\"string\"")).noIdentity(),
          new PgTypeAndExample<>(PgTypes.jsonb, new Jsonb("{\"A\": 42}")).noIdentity(),
          new PgTypeAndExample<>(PgTypes.jsonb, new Jsonb("{}")).noIdentity(),

          // ==================== JSON-Encoded Row Types ====================
          new PgTypeAndExample<>(PgTypes.jsonArrayEncoded(itemCodec), new Item("Widget", 5))
              .noIdentity(),
          new PgTypeAndExample<>(
                  PgTypes.jsonArrayEncodedList(itemCodec), List.of(new Item("Widget", 5)))
              .noIdentity(),
          new PgTypeAndExample<>(PgTypes.jsonObjectEncoded(namedItemCodec), new Item("Widget", 5))
              .noIdentity(),
          new PgTypeAndExample<>(
                  PgTypes.jsonObjectEncodedList(namedItemCodec), List.of(new Item("Widget", 5)))
              .noIdentity(),
          new PgTypeAndExample<>(PgTypes.jsonbArrayEncoded(itemCodec), new Item("Widget", 5))
              .noIdentity(),
          new PgTypeAndExample<>(
                  PgTypes.jsonbArrayEncodedList(itemCodec), List.of(new Item("Widget", 5)))
              .noIdentity(),
          new PgTypeAndExample<>(PgTypes.jsonbObjectEncoded(namedItemCodec), new Item("Widget", 5))
              .noIdentity(),
          new PgTypeAndExample<>(
                  PgTypes.jsonbObjectEncodedList(namedItemCodec), List.of(new Item("Widget", 5)))
              .noIdentity(),

          // ==================== Reg* Types ====================
          new PgTypeAndExample<>(PgTypes.regconfig, new Regconfig("danish")),
          new PgTypeAndExample<>(PgTypes.regconfig, new Regconfig("english")),
          new PgTypeAndExample<>(PgTypes.regdictionary, new Regdictionary("english_stem")),
          new PgTypeAndExample<>(PgTypes.regnamespace, new Regnamespace("public")),
          new PgTypeAndExample<>(PgTypes.regnamespace, new Regnamespace("pg_catalog")),
          new PgTypeAndExample<>(PgTypes.regoperator, new Regoperator("-(bigint,bigint)")),
          new PgTypeAndExample<>(PgTypes.regprocedure, new Regprocedure("sum(integer)")),
          new PgTypeAndExample<>(PgTypes.regrole, new Regrole("pg_monitor")),
          new PgTypeAndExample<>(PgTypes.regtype, new Regtype("integer")),
          new PgTypeAndExample<>(PgTypes.regtype, new Regtype("text")),

          // ==================== Misc Types ====================
          new PgTypeAndExample<>(PgTypes.oid, new Oid(42)),
          new PgTypeAndExample<>(PgTypes.xid, new Xid("1")),
          new PgTypeAndExample<>(PgTypes.uuid, UUID.randomUUID()),
          new PgTypeAndExample<>(PgTypes.uuid, new UUID(0, 0)),
          new PgTypeAndExample<>(PgTypes.uuid, new UUID(-1, -1)),
          new PgTypeAndExample<>(PgTypes.xml, new Xml("<a>42</a>")).noIdentity(),
          new PgTypeAndExample<>(
                  PgTypes.xml, new Xml("<root><child attr=\"value\">text</child></root>"))
              .noIdentity(),

          // ==================== Range Types ====================
          new PgTypeAndExample<>(
              PgTypes.int4range, Range.int4(new RangeBound.Closed<>(1), new RangeBound.Open<>(10))),
          new PgTypeAndExample<>(
              PgTypes.int4range,
              Range.int4(new RangeBound.Closed<>(0), new RangeBound.Closed<>(100))),
          new PgTypeAndExample<>(
              PgTypes.int4range, Range.int4(RangeBound.infinite(), new RangeBound.Open<>(10))),
          new PgTypeAndExample<>(
              PgTypes.int4range, Range.int4(new RangeBound.Closed<>(1), RangeBound.infinite())),
          new PgTypeAndExample<>(
              PgTypes.int4range, Range.int4(RangeBound.infinite(), RangeBound.infinite())),
          new PgTypeAndExample<>(PgTypes.int4range, Range.empty()),
          new PgTypeAndExample<>(
              PgTypes.int8range,
              Range.int8(new RangeBound.Closed<>(1L), new RangeBound.Open<>(1000000L))),
          new PgTypeAndExample<>(
              PgTypes.int8range,
              Range.int8(
                  new RangeBound.Closed<>(Long.MIN_VALUE + 1),
                  new RangeBound.Open<>(Long.MAX_VALUE))),
          new PgTypeAndExample<>(PgTypes.int8range, Range.empty()),
          new PgTypeAndExample<>(
              PgTypes.numrange,
              Range.numeric(
                  new RangeBound.Closed<>(new BigDecimal("0.5")),
                  new RangeBound.Open<>(new BigDecimal("10.5")))),
          new PgTypeAndExample<>(
              PgTypes.numrange,
              Range.numeric(
                  new RangeBound.Open<>(BigDecimal.ZERO),
                  new RangeBound.Closed<>(new BigDecimal("99.99")))),
          new PgTypeAndExample<>(PgTypes.numrange, Range.empty()),
          new PgTypeAndExample<>(
              PgTypes.daterange,
              Range.date(
                  new RangeBound.Closed<>(LocalDate.of(2024, 1, 1)),
                  new RangeBound.Open<>(LocalDate.of(2024, 12, 31)))),
          new PgTypeAndExample<>(
              PgTypes.daterange,
              Range.date(RangeBound.infinite(), new RangeBound.Closed<>(LocalDate.now()))),
          new PgTypeAndExample<>(PgTypes.daterange, Range.empty()),
          new PgTypeAndExample<>(
              PgTypes.tsrange,
              Range.timestamp(
                  new RangeBound.Closed<>(LocalDateTime.of(2024, 1, 1, 0, 0)),
                  new RangeBound.Open<>(LocalDateTime.of(2024, 12, 31, 23, 59, 59)))),
          new PgTypeAndExample<>(PgTypes.tsrange, Range.empty()),
          new PgTypeAndExample<>(
              PgTypes.tstzrange,
              Range.timestamptz(
                  new RangeBound.Closed<>(Instant.parse("2024-01-01T00:00:00Z")),
                  new RangeBound.Open<>(Instant.parse("2024-12-31T23:59:59Z")))),
          new PgTypeAndExample<>(PgTypes.tstzrange, Range.empty()));

  /**
   * All test entries: element types + auto-generated array entries.
   *
   * <p>For each scalar entry with array support, we generate a singleton array test (per entry) so
   * every edge-case value flows through the array codec. For each unique scalar type, we also
   * generate one multi-element array (combining all the type's edge-case examples, exercising
   * element separators) and one empty array.
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  List<PgTypeAndExample<?>> All = buildAll();

  @SuppressWarnings({"unchecked", "rawtypes"})
  private List<PgTypeAndExample<?>> buildAll() {
    var out = new java.util.ArrayList<PgTypeAndExample<?>>(Elements);

    // Per-entry singleton list tests (edge-case values through the element codec)
    for (var e : Elements) {
      if (hasListSupport(e)) {
        out.add(singletonListEntry((PgTypeAndExample) e));
      }
    }

    // Group entries for per-type list tests (multi + empty + nested).
    // Key by (sqlType, example class) so transformed types (e.g. jsonArrayEncoded<Item>)
    // don't collide with their base type (json<Json>) even though both have sqlType="json".
    var byType = new java.util.LinkedHashMap<String, List<PgTypeAndExample<?>>>();
    for (var e : Elements) {
      if (hasListSupport(e)) {
        String key = e.type().typename().sqlType() + "#" + e.example().getClass().getName();
        byType.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(e);
      }
    }
    for (var group : byType.values()) {
      var first = (PgTypeAndExample) group.get(0);
      char delim = ((PgType<?>) first.type()).arrayDelimiter();
      if (delim == ',' && group.size() > 1) {
        out.add(multiListEntry((List) group));
      }
      out.add(emptyListEntry(first));
      if (delim == ',') {
        out.add(nestedListEntry(first));
      }
    }

    return List.copyOf(out);
  }

  static <T> void withConnection(SqlFunction<Connection, T> f) {
    Containers.postgresTransactor().execute(f);
  }

  @Test
  public void test() {
    System.out.println(Arr.of(0, 1, 2, 3).reshape(2, 2));
    System.out.println(Arr.of("a", "b", "c", "d \",d").reshape(2, 2));
    System.out.println(ArrParser.parse(Arr.of(1, 2, 3, 4).encode(Object::toString)));
    System.out.println(ArrParser.parse("{{\"a\",\"b\"},{\"c\",\"d \\\",d\"}}"));

    // Test JSON roundtrip (no DB connection needed) - parallel
    System.out.println("\n=== JSON Roundtrip Tests (parallel) ===");
    All.parallelStream().forEach(PgTypeTest::testJsonRoundtrip);

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
                          conn.unwrap(PgConnection.class).setPrepareThreshold(0);
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

                  return errors.stream();
                })
            .toList();

    // Composite type tests - deduplicated by SQL type, run in parallel
    System.out.println("\n=== Composite Type DB Roundtrip Tests (parallel) ===");
    var compositeFailures =
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
                          testCompositeDbRoundtrip(conn, t);
                          return null;
                        });
                    return java.util.stream.Stream.<String>empty();
                  } catch (Exception e) {
                    return java.util.stream.Stream.of(
                        "Composite test FAILED "
                            + t.type.typename().sqlType()
                            + ": "
                            + e.getMessage());
                  }
                })
            .toList();

    // Test comprehensive composite with all supported types
    System.out.println("\n=== Comprehensive Composite Type Test ===");
    withConnection(
        conn -> {
          testComprehensiveComposite(conn);
          return null;
        });

    // Stored procedure roundtrip tests - deduplicate by SQL type, run in parallel
    // Tests function return, OUT param, and INOUT param paths.
    System.out.println("\n=== Call Roundtrip Tests (parallel) ===");
    var callFailures =
        All.stream()
            .collect(
                java.util.stream.Collectors.toMap(
                    t -> t.type.typename().sqlType(), t -> t, (a, b) -> a))
            .values()
            .parallelStream()
            .flatMap(
                t -> {
                  var errors = new ArrayList<String>();
                  try {
                    withConnection(
                        conn -> {
                          testCallRoundtrip(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    errors.add(
                        "Call test FAILED " + t.type.typename().sqlType() + ": " + e.getMessage());
                  }
                  try {
                    withConnection(
                        conn -> {
                          testCallOutParam(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    errors.add(
                        "Call OUT test FAILED "
                            + t.type.typename().sqlType()
                            + ": "
                            + e.getMessage());
                  }
                  try {
                    withConnection(
                        conn -> {
                          testCallInOutParam(conn, t);
                          return null;
                        });
                  } catch (Exception e) {
                    errors.add(
                        "Call INOUT test FAILED "
                            + t.type.typename().sqlType()
                            + ": "
                            + e.getMessage());
                  }
                  return errors.stream();
                })
            .toList();

    // Query analysis tests - deduplicated by SQL type, run in parallel
    System.out.println("\n=== Query Analysis Tests (parallel) ===");
    var analysisFailures =
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
                          testQueryAnalysis(conn, t);
                          return null;
                        });
                    return java.util.stream.Stream.<String>empty();
                  } catch (Exception e) {
                    return java.util.stream.Stream.of(
                        "Analysis FAILED " + t.type.typename().sqlType() + ": " + e.getMessage());
                  }
                })
            .toList();

    // Report results
    var allFailures = new ArrayList<String>();
    allFailures.addAll(failures);
    allFailures.addAll(compositeFailures);
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

  // Test type wrapped in a composite, roundtripped through the database
  static <A> void testCompositeDbRoundtrip(Connection conn, PgTypeAndExample<A> t)
      throws SQLException {
    // Skip types that don't support composite text encoding
    if (!t.compositeTextWorks) {
      return;
    }

    // Check if the type's PgCompositeText implementation works
    try {
      t.type.pgCompositeText().encode(t.example);
    } catch (UnsupportedOperationException e) {
      return;
    }

    String sqlType = t.type.typename().sqlType();
    int uniqueId = tableCounter.incrementAndGet();

    String compositeTypeName =
        "test_wrapper_"
            + uniqueId
            + "_"
            + sqlType
                .replace("(", "_")
                .replace(")", "_")
                .replace(",", "_")
                .replace(" ", "_")
                .replace("[", "_")
                .replace("]", "_");

    // Create composite type with single field
    try {
      conn.createStatement().execute("DROP TYPE IF EXISTS " + compositeTypeName + " CASCADE");
      conn.createStatement()
          .execute("CREATE TYPE " + compositeTypeName + " AS (wrapped_value " + sqlType + ")");

      // Build composite PgType for this wrapper
      PgType<SingleFieldWrapper<A>> wrapperType =
          PgTypes.compositeOf(
              compositeTypeName,
              RowCodec.<SingleFieldWrapper<A>>namedBuilder()
                  .field("wrapped_value", t.type, SingleFieldWrapper::value)
                  .build(SingleFieldWrapper::new));
      String tableName = "test_composite_rt_" + uniqueId;

      // Create temp table
      conn.createStatement()
          .execute("CREATE TEMP TABLE " + tableName + " (v " + compositeTypeName + ")");

      try {
        // Insert value
        SingleFieldWrapper<A> original = new SingleFieldWrapper<>(t.example);
        var insert = conn.prepareStatement("INSERT INTO " + tableName + " (v) VALUES (?)");
        wrapperType.write().set(insert, 1, original);
        insert.execute();
        insert.close();

        // Select back
        var select = conn.prepareStatement("SELECT v FROM " + tableName);
        select.execute();
        var rs = select.getResultSet();

        if (!rs.next()) {
          throw new RuntimeException("No rows returned");
        }

        SingleFieldWrapper<A> decoded = wrapperType.read().read(rs, 1);
        select.close();

        if (t.hasIdentity && !areEqual(decoded.value, t.example)) {
          throw new RuntimeException(
              "Composite DB roundtrip failed for "
                  + sqlType
                  + ": expected '"
                  + format(t.example)
                  + "' but got '"
                  + format(decoded.value)
                  + "'");
        }
      } finally {
        conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
      }
    } finally {
      conn.createStatement().execute("DROP TYPE IF EXISTS " + compositeTypeName + " CASCADE");
    }
  }

  record SingleFieldWrapper<A>(A value) {}

  // Test a comprehensive composite type with all commonly-used field types
  record ComprehensiveComposite(
      String textField,
      Integer int4Field,
      Long int8Field,
      Short int2Field,
      Double float8Field,
      Float float4Field,
      Boolean boolField,
      BigDecimal numericField,
      UUID uuidField,
      LocalDate dateField,
      LocalTime timeField,
      LocalDateTime timestampField) {}

  static void testComprehensiveComposite(Connection conn) throws SQLException {
    String typeName = "test_comprehensive_composite";

    conn.createStatement().execute("DROP TYPE IF EXISTS " + typeName + " CASCADE");
    conn.createStatement()
        .execute(
            "CREATE TYPE "
                + typeName
                + " AS ("
                + "text_field TEXT, "
                + "int4_field INT4, "
                + "int8_field INT8, "
                + "int2_field INT2, "
                + "float8_field FLOAT8, "
                + "float4_field FLOAT4, "
                + "bool_field BOOL, "
                + "numeric_field NUMERIC, "
                + "uuid_field UUID, "
                + "date_field DATE, "
                + "time_field TIME, "
                + "timestamp_field TIMESTAMP"
                + ")");

    try {
      PgType<ComprehensiveComposite> compositeType =
          PgTypes.compositeOf(
              typeName,
              RowCodec.<ComprehensiveComposite>namedBuilder()
                  .field("text_field", PgTypes.text, ComprehensiveComposite::textField)
                  .field("int4_field", PgTypes.int4, ComprehensiveComposite::int4Field)
                  .field("int8_field", PgTypes.int8, ComprehensiveComposite::int8Field)
                  .field("int2_field", PgTypes.int2, ComprehensiveComposite::int2Field)
                  .field("float8_field", PgTypes.float8, ComprehensiveComposite::float8Field)
                  .field("float4_field", PgTypes.float4, ComprehensiveComposite::float4Field)
                  .field("bool_field", PgTypes.bool, ComprehensiveComposite::boolField)
                  .field("numeric_field", PgTypes.numeric, ComprehensiveComposite::numericField)
                  .field("uuid_field", PgTypes.uuid, ComprehensiveComposite::uuidField)
                  .field("date_field", PgTypes.date, ComprehensiveComposite::dateField)
                  .field("time_field", PgTypes.time, ComprehensiveComposite::timeField)
                  .field(
                      "timestamp_field", PgTypes.timestamp, ComprehensiveComposite::timestampField)
                  .build(ComprehensiveComposite::new));

      conn.createStatement().execute("CREATE TEMP TABLE test_comp (v " + typeName + ")");

      try {
        // Create test value with special characters
        ComprehensiveComposite original =
            new ComprehensiveComposite(
                "Hello, \"World\"! (with special chars: \n\t\\)",
                Integer.MAX_VALUE,
                Long.MIN_VALUE,
                (short) 42,
                3.14159265359,
                2.71828f,
                true,
                new BigDecimal("12345.67890"),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                LocalDate.of(2024, 12, 25),
                LocalTime.of(14, 30, 45).truncatedTo(ChronoUnit.MICROS),
                LocalDateTime.of(2024, 12, 25, 14, 30, 45).truncatedTo(ChronoUnit.MICROS));

        // Test in-memory PgCompositeText roundtrip
        PgCompositeText<ComprehensiveComposite> compositeText = compositeType.pgCompositeText();
        String encoded = compositeText.encode(original).orElseThrow();
        ComprehensiveComposite decodedInMemory = compositeText.decode(encoded);

        System.out.println("Comprehensive composite in-memory roundtrip:");
        System.out.println("  Original: " + original);
        System.out.println("  Encoded: " + encoded);
        System.out.println("  Decoded: " + decodedInMemory);

        if (!original.equals(decodedInMemory)) {
          throw new RuntimeException(
              "In-memory roundtrip failed: expected " + original + " but got " + decodedInMemory);
        }

        // Insert into database
        var insert = conn.prepareStatement("INSERT INTO test_comp (v) VALUES (?)");
        compositeType.write().set(insert, 1, original);
        insert.execute();
        insert.close();

        // Read back
        var select = conn.prepareStatement("SELECT v FROM test_comp");
        select.execute();
        var rs = select.getResultSet();
        rs.next();
        ComprehensiveComposite decoded = compositeType.read().read(rs, 1);
        select.close();

        System.out.println("Comprehensive composite DB roundtrip:");
        System.out.println("  Original: " + original);
        System.out.println("  Decoded:  " + decoded);

        if (!original.equals(decoded)) {
          throw new RuntimeException(
              "DB roundtrip failed: expected " + original + " but got " + decoded);
        }

        System.out.println("Comprehensive composite tests PASSED!");
      } finally {
        conn.createStatement().execute("DROP TABLE IF EXISTS test_comp");
      }
    } finally {
      conn.createStatement().execute("DROP TYPE IF EXISTS " + typeName + " CASCADE");
    }
  }

  static <A> void testJsonRoundtrip(PgTypeAndExample<A> t) {
    try {
      PgJson<A> jsonCodec = t.type.pgJson();
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
  // Insert value into native column, read back as JSON using to_json(), parse back to value
  static <A> void testJsonDbRoundtrip(Connection conn, PgTypeAndExample<A> t) throws SQLException {
    PgJson<A> jsonCodec = t.type.pgJson();
    A original = t.example;
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("test_json_rt");

    // Create temp table with the native type column
    conn.createStatement().execute("CREATE TEMP TABLE " + tableName + " (v " + sqlType + ")");

    try {
      // Insert value using native type
      var insert = conn.prepareStatement("INSERT INTO " + tableName + " (v) VALUES (?)");
      t.type.write().set(insert, 1, original);
      insert.execute();
      insert.close();

      // Select back as JSON using to_json - this is what MULTISET does
      var select = conn.prepareStatement("SELECT to_json(v) FROM " + tableName);
      select.execute();
      var rs = select.getResultSet();

      if (!rs.next()) {
        throw new RuntimeException("No rows returned");
      }

      // Read the JSON string back from the database
      String jsonFromDb = rs.getString(1);
      select.close();

      // Parse the JSON and convert back to value
      JsonValue parsedFromDb = JsonValue.parse(jsonFromDb);
      A decoded = jsonCodec.fromJson(parsedFromDb);

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
      conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void testQueryAnalysis(Connection conn, PgTypeAndExample<A> t) throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String tableName = uniqueTableName("qa");
    conn.createStatement().execute("CREATE TEMP TABLE " + tableName + " (v " + sqlType + ")");
    try {
      RowCodec<A> parser = RowCodec.of(t.type);
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

  static <A> void testCase(Connection conn, PgTypeAndExample<A> t) throws SQLException {
    String tableName = uniqueTableName("test");
    conn.createStatement()
        .execute("create temp table " + tableName + " (v " + t.type.typename().sqlType() + ")");
    A expected = t.example;
    batchInsert(conn, t.type, tableName, expected);
    if (t.streamingWorks) {
      StreamingInsert.insert(
          "COPY " + tableName + "(v) FROM STDIN",
          100,
          Arrays.asList(t.example).iterator(),
          conn,
          t.type.pgText());
    }

    final PreparedStatement select;
    if (t.hasIdentity) {
      select = conn.prepareStatement("select v, null from " + tableName + " where v = ?");
      t.type.write().set(select, 1, expected);
    } else {
      select = conn.prepareStatement("select v, null from " + tableName);
    }

    select.execute();
    var rs = select.getResultSet();
    List<TestPair<A>> rows =
        RowCodec.<TestPair<A>>builder()
            .field(t.type, TestPair::t0)
            .field(t.type.opt(), TestPair::t1)
            .build(TestPair::new)
            .all()
            .apply(rs);
    select.close();
    conn.createStatement().execute("drop table " + tableName + ";");
    assertEquals(rows.get(0).t0(), expected);
    if (t.streamingWorks) {
      assertEquals(rows.get(1).t0(), expected);
    }
  }

  // ==================== Stored Procedure Roundtrip ====================
  // For each type, create a PL/pgSQL identity function, call it via Call.function(),
  // and verify the value roundtrips through CallableStatement.

  static <A> void testCallRoundtrip(Connection conn, PgTypeAndExample<A> t) throws SQLException {
    String sqlType = t.type.typename().sqlType();
    String safeName =
        "identity_"
            + sqlType
                .replace("(", "_")
                .replace(")", "_")
                .replace(",", "_")
                .replace(" ", "_")
                .replace("[", "_arr_")
                .replace("]", "")
                .replace("\"", "");
    int uniqueId = tableCounter.incrementAndGet();
    String funcName = safeName + "_" + uniqueId;

    conn.createStatement()
        .execute(
            "CREATE OR REPLACE FUNCTION "
                + funcName
                + "(p "
                + sqlType
                + ") RETURNS "
                + sqlType
                + " AS $$ BEGIN RETURN p; END; $$ LANGUAGE plpgsql");

    try {
      Procedure<A> proc =
          Procedure.buildFunction(funcName, java.util.List.of(ParamDef.input(t.type)), t.type);

      A result = proc.call(t.example).run(conn);

      if (!areEqual(result, t.example)) {
        throw new RuntimeException(
            "Call roundtrip failed for "
                + sqlType
                + ": expected '"
                + format(t.example)
                + "' but got '"
                + format(result)
                + "'");
      }
    } finally {
      conn.createStatement().execute("DROP FUNCTION IF EXISTS " + funcName);
    }
  }

  private static String safeName(String sqlType) {
    return sqlType
        .replace("(", "_")
        .replace(")", "_")
        .replace(",", "_")
        .replace(" ", "_")
        .replace("[", "_arr_")
        .replace("]", "")
        .replace("\"", "");
  }

  /**
   * Test type as a procedure OUT parameter. Creates {@code CREATE PROCEDURE foo(IN i T, OUT o T) AS
   * $$ BEGIN o := i; END; $$} and verifies the OUT value matches input.
   */
  @SuppressWarnings("unchecked")
  static <A> void testCallOutParam(Connection conn, PgTypeAndExample<A> t) throws SQLException {
    String sqlType = t.type.typename().sqlType();
    int uniqueId = tableCounter.incrementAndGet();
    String procName = "out_" + safeName(sqlType) + "_" + uniqueId;

    conn.createStatement()
        .execute(
            "CREATE OR REPLACE PROCEDURE "
                + procName
                + "(IN i "
                + sqlType
                + ", OUT o "
                + sqlType
                + ") AS $$ BEGIN o := i; END; $$ LANGUAGE plpgsql");

    try {
      Procedure<A> proc =
          Procedure.buildSingleOut(
              procName,
              java.util.List.of(ParamDef.input(t.type), ParamDef.of(t.type, ParamDef.Mode.OUT)));

      A result = proc.call(t.example).run(conn);

      if (!areEqual(result, t.example)) {
        throw new RuntimeException(
            "OUT param roundtrip failed for "
                + sqlType
                + ": expected '"
                + format(t.example)
                + "' but got '"
                + format(result)
                + "'");
      }
    } finally {
      conn.createStatement()
          .execute("DROP PROCEDURE IF EXISTS " + procName + "(" + sqlType + "," + sqlType + ")");
    }
  }

  /**
   * Test type as a procedure INOUT parameter. Creates {@code CREATE PROCEDURE foo(INOUT p T) AS $$
   * BEGIN END; $$} which passes the value through unchanged, and verifies roundtrip.
   */
  @SuppressWarnings("unchecked")
  static <A> void testCallInOutParam(Connection conn, PgTypeAndExample<A> t) throws SQLException {
    String sqlType = t.type.typename().sqlType();
    int uniqueId = tableCounter.incrementAndGet();
    String procName = "inout_" + safeName(sqlType) + "_" + uniqueId;

    conn.createStatement()
        .execute(
            "CREATE OR REPLACE PROCEDURE "
                + procName
                + "(INOUT p "
                + sqlType
                + ") AS $$ BEGIN END; $$ LANGUAGE plpgsql");

    try {
      Procedure<A> proc =
          Procedure.buildSingleOut(
              procName, java.util.List.of(ParamDef.of(t.type, ParamDef.Mode.INOUT)));

      A result = proc.call(t.example).run(conn);

      if (!areEqual(result, t.example)) {
        throw new RuntimeException(
            "INOUT param roundtrip failed for "
                + sqlType
                + ": expected '"
                + format(t.example)
                + "' but got '"
                + format(result)
                + "'");
      }
    } finally {
      conn.createStatement().execute("DROP PROCEDURE IF EXISTS " + procName + "(" + sqlType + ")");
    }
  }

  static <A> void assertEquals(A actual, A expected) {
    if (!areEqual(actual, expected)) {
      throw new RuntimeException(
          "actual: '" + format(actual) + "' != expected '" + format(expected) + "'");
    }
  }

  static <A> boolean areEqual(A actual, A expected) {
    if (expected instanceof byte[]) {
      return Arrays.equals((byte[]) actual, (byte[]) expected);
    }
    if (expected instanceof boolean[]) {
      return Arrays.equals((boolean[]) actual, (boolean[]) expected);
    }
    if (expected instanceof short[]) {
      return Arrays.equals((short[]) actual, (short[]) expected);
    }
    if (expected instanceof int[]) {
      return Arrays.equals((int[]) actual, (int[]) expected);
    }
    if (expected instanceof long[]) {
      return Arrays.equals((long[]) actual, (long[]) expected);
    }
    if (expected instanceof float[]) {
      return Arrays.equals((float[]) actual, (float[]) expected);
    }
    if (expected instanceof double[]) {
      return Arrays.equals((double[]) actual, (double[]) expected);
    }
    if (expected instanceof Object[]) {
      return Arrays.equals((Object[]) actual, (Object[]) expected);
    }
    return actual.equals(expected);
  }

  static <A> String format(A a) {
    if (a instanceof byte[]) {
      return Arrays.toString((byte[]) a);
    }
    if (a instanceof boolean[]) {
      return Arrays.toString((boolean[]) a);
    }
    if (a instanceof short[]) {
      return Arrays.toString((short[]) a);
    }
    if (a instanceof int[]) {
      return Arrays.toString((int[]) a);
    }
    if (a instanceof long[]) {
      return Arrays.toString((long[]) a);
    }
    if (a instanceof float[]) {
      return Arrays.toString((float[]) a);
    }
    if (a instanceof double[]) {
      return Arrays.toString((double[]) a);
    }
    if (a instanceof Object[]) {
      return Arrays.toString((Object[]) a);
    }
    return a.toString();
  }
}
