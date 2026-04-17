package dev.typr.foundations;

import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import dev.typr.foundations.data.Uint1;
import dev.typr.foundations.data.Uint2;
import dev.typr.foundations.data.Uint4;
import dev.typr.foundations.data.Uint8;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 * Tests for DuckDB type codecs. Tests all types defined in DuckDbTypes including: - Primitive types
 * (integers, floats, strings, dates, etc.) - Composite types (LIST, MAP, STRUCT, UNION, ARRAY)
 * DuckDB is an embedded database, so tests run in-process using an in-memory database.
 */
public class DuckDbTypeTest {

  private static final AtomicInteger tableCounter = new AtomicInteger(0);

  private static String uniqueTableName(String prefix) {
    return prefix + "_" + tableCounter.incrementAndGet();
  }

  // ==================== Wrapper Type Examples for transform testing ====================
  record UserId(int value) {}

  record ProductCode(String value) {}

  // A complex wrapper that contains a map internally
  record Config(java.util.Map<String, Integer> settings) {}

  // Bimapped types for testing MAP with wrapper keys/values
  static DuckDbType<UserId> userIdType = DuckDbTypes.integer.transform(UserId::new, UserId::value);

  static DuckDbType<ProductCode> productCodeType =
      DuckDbTypes.varchar.transform(ProductCode::new, ProductCode::value);

  // A type that wraps a map - transformped from MAP(VARCHAR, INTEGER)
  static DuckDbType<Config> configType =
      DuckDbTypes.varchar.mapTo(DuckDbTypes.integer).transform(Config::new, Config::settings);

  // ==================== STRUCT Example ====================
  record Person(String name, int age) {}

  // Type-safe builder: field types are tracked, no casts needed in build()
  DuckDbType<Person> personType =
      DuckDbTypes.compositeOf(
          "Person",
          RowCodec.<Person>namedBuilder()
              .field("name", DuckDbTypes.varchar, Person::name)
              .field("age", DuckDbTypes.integer, Person::age)
              .build(Person::new));

  // ==================== Nested STRUCT Examples ====================

  /** Struct with an optional field — exercises null-wrapping in struct attributes. */
  record WithOpt(String name, Optional<String> nickname) {}

  DuckDbType<WithOpt> withOptType =
      DuckDbTypes.compositeOf(
          "with_opt",
          RowCodec.<WithOpt>namedBuilder()
              .field("name", DuckDbTypes.varchar, WithOpt::name)
              .field("nickname", DuckDbTypes.varchar.opt(), WithOpt::nickname)
              .build(WithOpt::new));

  /** Struct with a list-of-scalar field. */
  record PersonWithHobbies(String name, List<String> hobbies) {}

  DuckDbType<PersonWithHobbies> personWithHobbiesType =
      DuckDbTypes.compositeOf(
          "person_hobbies",
          RowCodec.<PersonWithHobbies>namedBuilder()
              .field("name", DuckDbTypes.varchar, PersonWithHobbies::name)
              .field("hobbies", DuckDbTypes.varchar.list(), PersonWithHobbies::hobbies)
              .build(PersonWithHobbies::new));

  /** Struct-containing-struct (2-level nesting). */
  record Employee(String role, Person person) {}

  DuckDbType<Employee> employeeType =
      DuckDbTypes.compositeOf(
          "employee",
          RowCodec.<Employee>namedBuilder()
              .field("role", DuckDbTypes.varchar, Employee::role)
              .field("person", personType, Employee::person)
              .build(Employee::new));

  /** Struct with list-of-struct field (deep nesting — the headline broken case). */
  record Team(String name, List<Person> members) {}

  DuckDbType<Team> teamType =
      DuckDbTypes.compositeOf(
          "team",
          RowCodec.<Team>namedBuilder()
              .field("name", DuckDbTypes.varchar, Team::name)
              .field("members", personType.list(), Team::members)
              .build(Team::new));

  /** Struct with fixed-size ARRAY of structs (DuckDB's ARRAY(T, N), distinct from LIST). */
  record TeamArr(String name, List<Person> members) {}

  DuckDbType<TeamArr> teamArrType =
      DuckDbTypes.compositeOf(
          "team_arr",
          RowCodec.<TeamArr>namedBuilder()
              .field("name", DuckDbTypes.varchar, TeamArr::name)
              .field("members", personType.array(2), TeamArr::members)
              .build(TeamArr::new));

  /** Struct with list-of-struct-containing-list-of-struct (3-level deep). */
  record Company(String name, List<Team> teams) {}

  DuckDbType<Company> companyType =
      DuckDbTypes.compositeOf(
          "company",
          RowCodec.<Company>namedBuilder()
              .field("name", DuckDbTypes.varchar, Company::name)
              .field("teams", teamType.list(), Company::teams)
              .build(Company::new));

  // Parsers for JSON-encoded row type testing
  static RowCodec<Person> personCodec =
      RowCodec.<Person>builder()
          .field(DuckDbTypes.varchar, Person::name)
          .field(DuckDbTypes.integer, Person::age)
          .build(Person::new);

  static RowCodecNamed<Person> namedPersonCodec =
      RowCodec.<Person>namedBuilder()
          .field("name", DuckDbTypes.varchar, Person::name)
          .field("age", DuckDbTypes.integer, Person::age)
          .build(Person::new);

  // ==================== UNION Example ====================
  sealed interface IntOrString {
    record Num(int value) implements IntOrString {}

    record Str(String value) implements IntOrString {}
  }

  // New simplified API: just provide wrapper/unwrapper functions, everything else is auto-derived
  DuckDbUnion<IntOrString> intOrStringUnion =
      DuckDbUnion.<IntOrString>builder("IntOrString")
          .member(
              "num",
              DuckDbTypes.integer,
              Integer.class,
              IntOrString.Num::new, // wrapper: Integer -> IntOrString
              ios -> ios instanceof IntOrString.Num n ? n.value() : null) // unwrapper
          .member(
              "str",
              DuckDbTypes.varchar,
              String.class,
              IntOrString.Str::new, // wrapper: String -> IntOrString
              ios -> ios instanceof IntOrString.Str s ? s.value() : null) // unwrapper
          .build(); // auto-derives reader, writer, and JSON codec

  DuckDbType<IntOrString> intOrStringType = intOrStringUnion.asType();

  record DuckDbTypeAndExample<A>(DuckDbType<A> type, A example, boolean hasIdentity) {
    public DuckDbTypeAndExample(DuckDbType<A> type, A example) {
      this(type, example, true);
    }

    public DuckDbTypeAndExample<A> noIdentity() {
      return new DuckDbTypeAndExample<>(type, example, false);
    }

    boolean supportsList() {
      return type.listCodec().isPresent()
          && !(type.typename() instanceof DuckDbTypename.ListOf)
          && !(type.typename() instanceof DuckDbTypename.ArrayOf);
    }

    // Nested LIST/ARRAY wraps each inner collection as a DuckDBUserArray whose elements are the
    // scalar's Java representation. DuckDB's driver only understands a subset of Java types in
    // that path — types that require string-based parsing (INTERVAL, UUID, JSON) or have no
    // native parameter-binding (HUGEINT/BigInteger, TIME/LocalTime) don't round-trip here.
    boolean supportsNested() {
      if (!supportsList()) return false;
      String sql = type.typename().sqlType();
      return switch (sql) {
        case "INTERVAL", "UUID", "JSON", "HUGEINT", "UHUGEINT", "TIME", "TIMETZ" -> false;
        default -> !sql.startsWith("DECIMAL"); // precision normalization differs nested
      };
    }

    // STRUCT/LIST/ARRAY map entries bind natively (DuckDBUserStruct / DuckDBUserArray) so
    // nested VARCHAR[] fields round-trip without DuckDB re-parsing our SQL literal. DECIMAL is
    // still excluded because of the BigDecimal precision-padding mismatch (12345 vs 12345.000
    // are unequal by .equals()).
    boolean supportsMap() {
      if (!supportsList()) return false;
      return !type.typename().sqlType().startsWith("DECIMAL");
    }
  }

  // Sample enum for ENUM type testing
  enum Color {
    RED,
    GREEN,
    BLUE
  }

  // HUGEINT range: -170141183460469231731687303715884105728 to
  // 170141183460469231731687303715884105727
  BigInteger HUGEINT_MAX = new BigInteger("170141183460469231731687303715884105727");
  BigInteger HUGEINT_MIN = new BigInteger("-170141183460469231731687303715884105728");
  // UHUGEINT range: 0 to 340282366920938463463374607431768211455
  BigInteger UHUGEINT_MAX = new BigInteger("340282366920938463463374607431768211455");
  // UBIGINT range: 0 to 18446744073709551615
  BigInteger UBIGINT_MAX = new BigInteger("18446744073709551615");

  List<DuckDbTypeAndExample<?>> All =
      List.of(
          // ==================== Integer Types (Signed) ====================
          new DuckDbTypeAndExample<>(DuckDbTypes.tinyint, (byte) 42),
          new DuckDbTypeAndExample<>(DuckDbTypes.tinyint, Byte.MIN_VALUE),
          new DuckDbTypeAndExample<>(DuckDbTypes.tinyint, Byte.MAX_VALUE),
          new DuckDbTypeAndExample<>(DuckDbTypes.tinyint, (byte) 0),
          new DuckDbTypeAndExample<>(DuckDbTypes.smallint, (short) 4242),
          new DuckDbTypeAndExample<>(DuckDbTypes.smallint, Short.MIN_VALUE),
          new DuckDbTypeAndExample<>(DuckDbTypes.smallint, Short.MAX_VALUE),
          new DuckDbTypeAndExample<>(DuckDbTypes.smallint, (short) 0),
          new DuckDbTypeAndExample<>(DuckDbTypes.integer, 42424242),
          new DuckDbTypeAndExample<>(DuckDbTypes.integer, Integer.MIN_VALUE),
          new DuckDbTypeAndExample<>(DuckDbTypes.integer, Integer.MAX_VALUE),
          new DuckDbTypeAndExample<>(DuckDbTypes.integer, 0),
          new DuckDbTypeAndExample<>(DuckDbTypes.bigint, 4242424242424242L),
          new DuckDbTypeAndExample<>(DuckDbTypes.bigint, Long.MIN_VALUE),
          new DuckDbTypeAndExample<>(DuckDbTypes.bigint, Long.MAX_VALUE),
          new DuckDbTypeAndExample<>(DuckDbTypes.bigint, 0L),
          // HUGEINT - 128-bit signed integer
          new DuckDbTypeAndExample<>(DuckDbTypes.hugeint, BigInteger.valueOf(12345678901234567L)),
          new DuckDbTypeAndExample<>(DuckDbTypes.hugeint, HUGEINT_MAX),
          new DuckDbTypeAndExample<>(DuckDbTypes.hugeint, HUGEINT_MIN),
          new DuckDbTypeAndExample<>(DuckDbTypes.hugeint, BigInteger.ZERO),

          // ==================== Integer Types (Unsigned) ====================
          new DuckDbTypeAndExample<>(DuckDbTypes.utinyint, new Uint1((short) 255)),
          new DuckDbTypeAndExample<>(DuckDbTypes.utinyint, new Uint1((short) 0)),
          new DuckDbTypeAndExample<>(DuckDbTypes.usmallint, new Uint2(65535)),
          new DuckDbTypeAndExample<>(DuckDbTypes.usmallint, new Uint2(0)),
          new DuckDbTypeAndExample<>(DuckDbTypes.uinteger, new Uint4(4294967295L)),
          new DuckDbTypeAndExample<>(DuckDbTypes.uinteger, new Uint4(0L)),
          // UBIGINT - 64-bit unsigned integer
          new DuckDbTypeAndExample<>(DuckDbTypes.ubigint, new Uint8(UBIGINT_MAX)),
          new DuckDbTypeAndExample<>(DuckDbTypes.ubigint, new Uint8(BigInteger.ZERO)),
          // UHUGEINT - 128-bit unsigned integer
          new DuckDbTypeAndExample<>(DuckDbTypes.uhugeint, UHUGEINT_MAX),
          new DuckDbTypeAndExample<>(DuckDbTypes.uhugeint, BigInteger.ZERO),

          // ==================== Floating-Point Types ====================
          new DuckDbTypeAndExample<>(DuckDbTypes.float_, 3.14159f).noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.float_, 0.0f).noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.float_, Float.MAX_VALUE).noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.float_, Float.MIN_VALUE).noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.double_, 3.141592653589793),
          new DuckDbTypeAndExample<>(DuckDbTypes.double_, 0.0),
          new DuckDbTypeAndExample<>(DuckDbTypes.double_, -3.141592653589793),
          new DuckDbTypeAndExample<>(DuckDbTypes.double_, Double.MAX_VALUE),

          // ==================== Fixed-Point Types ====================
          new DuckDbTypeAndExample<>(DuckDbTypes.decimal, new BigDecimal("12345")),
          new DuckDbTypeAndExample<>(DuckDbTypes.decimal, BigDecimal.ZERO),
          new DuckDbTypeAndExample<>(DuckDbTypes.decimal, new BigDecimal("-9999999999")),
          new DuckDbTypeAndExample<>(DuckDbTypes.decimal(10, 2), new BigDecimal("12345678.90")),
          new DuckDbTypeAndExample<>(DuckDbTypes.decimal(10, 2), new BigDecimal("0.00")),
          new DuckDbTypeAndExample<>(DuckDbTypes.decimal(10, 2), new BigDecimal("-99999999.99")),
          new DuckDbTypeAndExample<>(DuckDbTypes.decimal(10, 5), new BigDecimal("12345.67890")),

          // ==================== Boolean Type ====================
          new DuckDbTypeAndExample<>(DuckDbTypes.boolean_, true),
          new DuckDbTypeAndExample<>(DuckDbTypes.boolean_, false),

          // ==================== String Types ====================
          new DuckDbTypeAndExample<>(DuckDbTypes.varchar, "Hello, DuckDB!"),
          new DuckDbTypeAndExample<>(DuckDbTypes.varchar, ""),
          new DuckDbTypeAndExample<>(
              DuckDbTypes.varchar, "Unicode: \u00e9\u00e8\u00ea \u4e2d\u6587"),
          new DuckDbTypeAndExample<>(DuckDbTypes.varchar, "Line1\nLine2\tTabbed"),
          new DuckDbTypeAndExample<>(DuckDbTypes.varchar, "Quote\"Test'Single\\Back"),
          new DuckDbTypeAndExample<>(
              DuckDbTypes.varchar, "Emoji: \uD83D\uDE00\uD83C\uDF89\uD83D\uDE80"),
          new DuckDbTypeAndExample<>(DuckDbTypes.varchar(100), "Fixed length varchar"),
          new DuckDbTypeAndExample<>(DuckDbTypes.text, "Text type content"),
          new DuckDbTypeAndExample<>(DuckDbTypes.char_(10), "hello"),

          // ==================== Binary Types ====================
          // BLOB[] not supported: DuckDBUserArray can't serialize binary data
          new DuckDbTypeAndExample<>(DuckDbTypes.blob, new byte[] {0x01, 0x02, 0x03, 0x04, 0x05}),
          new DuckDbTypeAndExample<>(DuckDbTypes.blob, new byte[] {}),
          new DuckDbTypeAndExample<>(
              DuckDbTypes.blob, new byte[] {(byte) 0xFF, 0x00, 0x7F, (byte) 0x80}),
          new DuckDbTypeAndExample<>(DuckDbTypes.blob, new byte[] {0x00}),

          // ==================== Date/Time Types ====================
          new DuckDbTypeAndExample<>(DuckDbTypes.date, LocalDate.of(2024, 6, 15)),
          new DuckDbTypeAndExample<>(DuckDbTypes.date, LocalDate.of(1970, 1, 1)),
          new DuckDbTypeAndExample<>(DuckDbTypes.date, LocalDate.of(2099, 12, 31)),
          new DuckDbTypeAndExample<>(DuckDbTypes.time, LocalTime.of(14, 30, 45)),
          new DuckDbTypeAndExample<>(DuckDbTypes.time, LocalTime.of(0, 0, 0)),
          new DuckDbTypeAndExample<>(DuckDbTypes.time, LocalTime.of(23, 59, 59)),
          new DuckDbTypeAndExample<>(
              DuckDbTypes.timestamp, LocalDateTime.of(2024, 6, 15, 14, 30, 45)),
          new DuckDbTypeAndExample<>(DuckDbTypes.timestamp, LocalDateTime.of(1970, 1, 1, 0, 0, 0)),
          new DuckDbTypeAndExample<>(
              DuckDbTypes.timestamp, LocalDateTime.of(2024, 6, 15, 14, 30, 45, 123456000)),
          // Timestamp with timezone
          new DuckDbTypeAndExample<>(
              DuckDbTypes.timestamptz,
              OffsetDateTime.of(2024, 6, 15, 14, 30, 45, 0, ZoneOffset.UTC)),

          // ==================== Interval Type ====================
          new DuckDbTypeAndExample<>(DuckDbTypes.interval, Duration.ofHours(2).plusMinutes(30)),
          new DuckDbTypeAndExample<>(DuckDbTypes.interval, Duration.ofDays(5)),

          // ==================== UUID Type ====================
          new DuckDbTypeAndExample<>(
              DuckDbTypes.uuid, UUID.fromString("550e8400-e29b-41d4-a716-446655440000")),
          new DuckDbTypeAndExample<>(
              DuckDbTypes.uuid, UUID.fromString("00000000-0000-0000-0000-000000000000")),

          // ==================== JSON Type ====================
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.json, new Json("{\"name\": \"DuckDB\", \"version\": 1.0}"))
              .noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.json, new Json("[1, 2, 3, \"four\"]"))
              .noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.json, new Json("{}")).noIdentity(),

          // ==================== JSON-Encoded Row Types ====================
          // These types store structured rows as JSON — the codec is derived from the RowCodec
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.jsonArrayEncoded(personCodec), new Person("Alice", 30))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.jsonArrayEncodedList(personCodec),
                  List.of(new Person("Alice", 30), new Person("Bob", 25)))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.jsonObjectEncoded(namedPersonCodec), new Person("Alice", 30))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.jsonObjectEncodedList(namedPersonCodec),
                  List.of(new Person("Alice", 30), new Person("Bob", 25)))
              .noIdentity(),

          // ==================== ENUM Type ====================
          new DuckDbTypeAndExample<>(DuckDbTypes.ofEnum("color_enum", Color::valueOf), Color.GREEN),
          new DuckDbTypeAndExample<>(DuckDbTypes.ofEnum("color_enum", Color::valueOf), Color.RED),

          // ==================== LIST Types ====================
          // LIST types don't support direct equality in WHERE clauses, so we mark noIdentity()
          // Native JNI types (best performance)
          new DuckDbTypeAndExample<>(DuckDbTypes.boolean_.list(), List.of(true, false, true))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.tinyint.list(), List.of((byte) 1, (byte) 2, (byte) -1))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.smallint.list(), List.of((short) 100, (short) -200))
              .noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.integer.list(), List.of(1, 2, 3, 4, 5))
              .noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.integer.list(), List.of()).noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.integer.list(), List.of(-100, 0, 100))
              .noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.bigint.list(), List.of(1L, 2L, 9999999999L))
              .noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.float_.list(), List.of(1.5f, 2.5f, 3.14f))
              .noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.double_.list(), List.of(1.5, 2.5, 3.14159))
              .noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.varchar.list(), List.of("hello", "world"))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.list(), List.of("quote'test", "back\\slash"))
              .noIdentity(),

          // ==================== MAP Types ====================
          // MAP types use the mapTo() combinator. They don't support direct equality in WHERE
          // clauses. MAP[] not supported: DuckDBUserArray string format rejected by DuckDB.
          // Empty MAP — exercises the empty-stringifier branch and zero-entry roundtrip.
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.integer), java.util.Map.<String, Integer>of())
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.integer), java.util.Map.of("a", 1, "b", 2))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.varchar),
                  java.util.Map.of("key1", "value1", "key2", "value2"))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.integer.mapTo(DuckDbTypes.varchar),
                  java.util.Map.of(1, "one", 2, "two"))
              .noIdentity(),
          // MAP with UUID keys
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.uuid.mapTo(DuckDbTypes.varchar),
                  java.util.Map.of(
                      UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "value1",
                      UUID.fromString("123e4567-e89b-12d3-a456-426614174000"), "value2"))
              .noIdentity(),
          // MAP with TIME values
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.time),
                  java.util.Map.of(
                      "morning", LocalTime.of(8, 15, 0),
                      "afternoon", LocalTime.of(14, 30, 45)))
              .noIdentity(),
          // MAP with UUID keys and TIME values
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.uuid.mapTo(DuckDbTypes.time),
                  java.util.Map.of(
                      UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                      LocalTime.of(14, 30, 45),
                      UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                      LocalTime.of(8, 15, 0)))
              .noIdentity(),
          // More MAP type combinations
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.bigint),
                  java.util.Map.of("count", 100L, "total", 999999999999L))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.double_),
                  java.util.Map.of("pi", 3.14159, "e", 2.71828))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.boolean_),
                  java.util.Map.of("active", true, "verified", false))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.integer.mapTo(DuckDbTypes.integer),
                  java.util.Map.of(1, 100, 2, 200, 3, 300))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.date),
                  java.util.Map.of(
                      "start", LocalDate.of(2024, 1, 1), "end", LocalDate.of(2024, 12, 31)))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.uuid),
                  java.util.Map.of(
                      "user1", UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                      "user2", UUID.fromString("123e4567-e89b-12d3-a456-426614174000")))
              .noIdentity(),
          // MAP with transformped key type (UserId wrapping Integer)
          new DuckDbTypeAndExample<>(
                  userIdType.mapTo(DuckDbTypes.varchar),
                  java.util.Map.of(new UserId(1), "admin", new UserId(2), "user"))
              .noIdentity(),
          // MAP with transformped value type (ProductCode wrapping String)
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.integer.mapTo(productCodeType),
                  java.util.Map.of(1, new ProductCode("PROD-001"), 2, new ProductCode("PROD-002")))
              .noIdentity(),
          // MAP with transformped key AND value types
          new DuckDbTypeAndExample<>(
                  userIdType.mapTo(productCodeType),
                  java.util.Map.of(
                      new UserId(100), new ProductCode("SKU-A"),
                      new UserId(200), new ProductCode("SKU-B")))
              .noIdentity(),
          // MAP with Long keys
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.bigint.mapTo(DuckDbTypes.varchar),
                  java.util.Map.of(9999999999L, "large-key", 1L, "small-key"))
              .noIdentity(),
          // MAP with Double keys
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.double_.mapTo(DuckDbTypes.varchar),
                  java.util.Map.of(3.14, "pi", 2.71, "e"))
              .noIdentity(),
          // Config type directly (transformped from MAP(VARCHAR, INTEGER))
          // This tests that transform works correctly with map types
          new DuckDbTypeAndExample<>(
                  configType, new Config(java.util.Map.of("max_conn", 100, "min_conn", 5)))
              .noIdentity(),
          // Optional MAP — exercises .opt() on a transformed map type and the read.fromJdbcValue
          // path through DuckDbRead.Nullable.
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.varchar.mapTo(DuckDbTypes.integer).opt(),
                  java.util.Optional.of(java.util.Map.of("present", 1)))
              .noIdentity(),

          // ==================== LIST Types with complex element types ====================
          // String-converted types (~33% overhead at 100k rows, but required for correctness)
          // LIST<UUID> - UUID requires String conversion to avoid byte-ordering bug
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.uuid.list(),
                  List.of(
                      UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
                      UUID.fromString("123e4567-e89b-12d3-a456-426614174000")))
              .noIdentity(),
          // LIST<TIME> - LocalTime requires String conversion (JNI doesn't recognize
          // java.time.LocalTime)
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.time.list(),
                  List.of(LocalTime.of(14, 30, 45), LocalTime.of(8, 15, 0)))
              .noIdentity(),
          // LIST<DATE> - LocalDate requires String conversion
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.date.list(),
                  List.of(LocalDate.of(2024, 6, 15), LocalDate.of(1970, 1, 1)))
              .noIdentity(),
          // LIST<TIMESTAMP> - LocalDateTime requires String conversion
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.timestamp.list(),
                  List.of(
                      LocalDateTime.of(2024, 6, 15, 14, 30, 45),
                      LocalDateTime.of(1970, 1, 1, 0, 0, 0)))
              .noIdentity(),
          // LIST<DECIMAL> - BigDecimal requires String conversion
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.decimal.list(),
                  List.of(
                      new java.math.BigDecimal("123.456"), new java.math.BigDecimal("-99999.99")))
              .noIdentity(),
          // LIST<HUGEINT> - BigInteger requires String conversion
          new DuckDbTypeAndExample<>(
                  DuckDbTypes.hugeint.list(),
                  List.of(
                      new java.math.BigInteger("170141183460469231731687303715884105727"),
                      java.math.BigInteger.ZERO))
              .noIdentity(),

          // ==================== STRUCT Types ====================
          new DuckDbTypeAndExample<>(personType, new Person("Alice", 30)).noIdentity(),
          new DuckDbTypeAndExample<>(personType, new Person("Bob", 25)).noIdentity(),

          // ==================== LIST of STRUCT Types ====================
          new DuckDbTypeAndExample<>(
                  personType.list(), List.of(new Person("Alice", 30), new Person("Bob", 25)))
              .noIdentity(),
          new DuckDbTypeAndExample<>(personType.list(), List.of()).noIdentity(),

          // ==================== STRUCT with nested field types ====================
          // Struct with Optional field (nullable) — null must arrive as Optional.empty()
          new DuckDbTypeAndExample<>(withOptType, new WithOpt("Alice", Optional.of("Ali")))
              .noIdentity(),
          new DuckDbTypeAndExample<>(withOptType, new WithOpt("Bob", Optional.empty()))
              .noIdentity(),
          // Struct with List<String> field
          new DuckDbTypeAndExample<>(
                  personWithHobbiesType,
                  new PersonWithHobbies("Alice", List.of("reading", "hiking")))
              .noIdentity(),
          new DuckDbTypeAndExample<>(personWithHobbiesType, new PersonWithHobbies("Bob", List.of()))
              .noIdentity(),
          // Struct-in-struct (2-level)
          new DuckDbTypeAndExample<>(
                  employeeType, new Employee("Engineer", new Person("Alice", 30)))
              .noIdentity(),
          // Struct with list-of-struct field (deep nesting — previously broken)
          new DuckDbTypeAndExample<>(
                  teamType,
                  new Team("Platform", List.of(new Person("Alice", 30), new Person("Bob", 25))))
              .noIdentity(),
          new DuckDbTypeAndExample<>(teamType, new Team("Empty", List.of())).noIdentity(),
          // Struct with fixed-size ARRAY-of-struct field
          new DuckDbTypeAndExample<>(
                  teamArrType,
                  new TeamArr("Platform", List.of(new Person("Alice", 30), new Person("Bob", 25))))
              .noIdentity(),
          // 3-level deep: struct → list of struct with list-of-struct field
          new DuckDbTypeAndExample<>(
                  companyType,
                  new Company(
                      "Acme",
                      List.of(
                          new Team("Platform", List.of(new Person("Alice", 30))),
                          new Team(
                              "Design", List.of(new Person("Bob", 25), new Person("Carol", 28))))))
              .noIdentity(),

          // ==================== UNION Types ====================
          // UNION[] not supported: array elements returned as String, tag inference fails
          new DuckDbTypeAndExample<>(intOrStringType, new IntOrString.Num(42)).noIdentity(),
          new DuckDbTypeAndExample<>(intOrStringType, new IntOrString.Str("hello")).noIdentity(),

          // ==================== LIST of UNION Types ====================
          new DuckDbTypeAndExample<>(
                  intOrStringType.list(),
                  List.of(new IntOrString.Num(42), new IntOrString.Str("hello")))
              .noIdentity(),
          new DuckDbTypeAndExample<>(
                  intOrStringType.list(),
                  List.of(new IntOrString.Num(1), new IntOrString.Num(2), new IntOrString.Num(3)))
              .noIdentity(),
          new DuckDbTypeAndExample<>(intOrStringType.list(), List.of()).noIdentity(),

          // ==================== LIST Variable Length Cases ====================
          // Prove that LIST (unlike ARRAY) accepts variable lengths
          new DuckDbTypeAndExample<>(DuckDbTypes.integer.list(), List.of()).noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.integer.list(), List.of(1, 2)).noIdentity(),
          new DuckDbTypeAndExample<>(DuckDbTypes.integer.list(), List.of(1, 2, 3, 4, 5))
              .noIdentity());

  // Connection helper for DuckDB - uses in-memory database
  static <T> T withConnection(SqlFunction<Connection, T> f) {
    try (var conn = java.sql.DriverManager.getConnection("jdbc:duckdb:")) {
      conn.setAutoCommit(false);
      // Create the enum type for testing
      conn.createStatement().execute("CREATE TYPE color_enum AS ENUM ('RED', 'GREEN', 'BLUE')");
      try {
        return f.apply(conn);
      } finally {
        conn.rollback();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  static <A> DuckDbTypeAndExample<java.util.List<A>> toArrayExample(
      DuckDbTypeAndExample<A> scalar) {
    DuckDbType<java.util.List<A>> arrayType = scalar.type.array(1);
    return new DuckDbTypeAndExample<>(arrayType, java.util.List.of(scalar.example)).noIdentity();
  }

  static <A> void tryDerive(
      DuckDbTypeAndExample<A> scalar,
      java.util.function.Function<DuckDbTypeAndExample<A>, DuckDbTypeAndExample<?>> derive,
      java.util.List<DuckDbTypeAndExample<?>> out) {
    try {
      out.add(derive.apply(scalar));
    } catch (Exception e) {
      System.out.println(
          "  Skipping derived test for "
              + scalar.type.typename().sqlType()
              + ": "
              + e.getMessage());
    }
  }

  static <A> DuckDbTypeAndExample<java.util.List<A>> toListExample(DuckDbTypeAndExample<A> scalar) {
    DuckDbType<java.util.List<A>> listType = scalar.type.list();
    return new DuckDbTypeAndExample<>(listType, java.util.List.of(scalar.example)).noIdentity();
  }

  static <A> DuckDbTypeAndExample<java.util.List<java.util.List<A>>> toNestedListExample(
      DuckDbTypeAndExample<A> scalar) {
    DuckDbType<java.util.List<java.util.List<A>>> nestedType = scalar.type.list().list();
    return new DuckDbTypeAndExample<>(
            nestedType, java.util.List.of(java.util.List.of(scalar.example)))
        .noIdentity();
  }

  static <A> DuckDbTypeAndExample<java.util.List<java.util.List<A>>> toNestedArrayExample(
      DuckDbTypeAndExample<A> scalar) {
    DuckDbType<java.util.List<java.util.List<A>>> nestedType = scalar.type.array(1).array(1);
    return new DuckDbTypeAndExample<>(
            nestedType, java.util.List.of(java.util.List.of(scalar.example)))
        .noIdentity();
  }

  static <A> DuckDbTypeAndExample<java.util.List<java.util.List<A>>> toArrayOfListExample(
      DuckDbTypeAndExample<A> scalar) {
    DuckDbType<java.util.List<java.util.List<A>>> nestedType = scalar.type.list().array(1);
    return new DuckDbTypeAndExample<>(
            nestedType, java.util.List.of(java.util.List.of(scalar.example)))
        .noIdentity();
  }

  /** A second example (different key value) so multi-entry maps stress duplicate-key handling. */
  @SuppressWarnings("unchecked")
  private static <A> A altKey(DuckDbTypeAndExample<A> scalar) {
    A example = scalar.example;
    return switch (example) {
      case Byte b -> (A) Byte.valueOf((byte) (b == 0 ? 1 : b + 1));
      case Short s -> (A) Short.valueOf((short) (s == 0 ? 1 : s + 1));
      case Integer i -> (A) Integer.valueOf(i == 0 ? 1 : i + 1);
      case Long l -> (A) Long.valueOf(l == 0 ? 1 : l + 1);
      case Float f -> (A) Float.valueOf(f == 0f ? 1f : f + 1f);
      case Double d -> (A) Double.valueOf(d == 0d ? 1d : d + 1d);
      case Boolean b -> (A) Boolean.valueOf(!b);
      case String s -> (A) (s + "_alt");
      case java.math.BigInteger bi -> (A) bi.add(java.math.BigInteger.ONE);
      case java.math.BigDecimal bd -> (A) bd.add(java.math.BigDecimal.ONE);
      case java.util.UUID u -> (A) java.util.UUID.fromString("00000000-0000-0000-0000-000000000001");
      case java.time.LocalDate d -> (A) d.plusDays(1);
      case java.time.LocalTime t -> (A) t.plusSeconds(1);
      case java.time.LocalDateTime dt -> (A) dt.plusSeconds(1);
      case java.time.Instant i -> (A) i.plusSeconds(1);
      case java.time.OffsetTime ot -> (A) ot.plusSeconds(1);
      case java.time.Duration d -> (A) d.plusSeconds(1);
      default -> example; // fall back to single-entry map for types we can't perturb
    };
  }

  /** Derive a map test using {@code scalar} as the KEY and varchar as the value. */
  static <A> DuckDbTypeAndExample<java.util.Map<A, String>> toMapAsKeyExample(
      DuckDbTypeAndExample<A> scalar) {
    DuckDbType<java.util.Map<A, String>> mapType =
        scalar.type.mapTo(DuckDbTypes.varchar);
    A k1 = scalar.example;
    A k2 = altKey(scalar);
    java.util.Map<A, String> example;
    if (java.util.Objects.equals(k1, k2)) {
      example = java.util.Map.of(k1, "v1");
    } else {
      var m = new java.util.LinkedHashMap<A, String>();
      m.put(k1, "v1");
      m.put(k2, "v2");
      example = m;
    }
    return new DuckDbTypeAndExample<>(mapType, example).noIdentity();
  }

  /** Derive a map test using varchar as key and {@code scalar} as the VALUE. */
  static <A> DuckDbTypeAndExample<java.util.Map<String, A>> toMapAsValueExample(
      DuckDbTypeAndExample<A> scalar) {
    DuckDbType<java.util.Map<String, A>> mapType =
        DuckDbTypes.varchar.mapTo(scalar.type);
    return new DuckDbTypeAndExample<>(mapType, java.util.Map.of("k1", scalar.example))
        .noIdentity();
  }

  @Test
  public void test() {
    System.out.println("Testing DuckDB type codecs...\n");

    // Deduplicate eligible scalars by SQL type — single example per scalar type.
    var scalars =
        All.stream()
            .filter(t -> t.supportsList())
            .collect(Collectors.toMap(t -> t.type.typename().sqlType(), t -> t, (a, b) -> a))
            .values();

    var listExamples = new ArrayList<DuckDbTypeAndExample<?>>();
    var arrayExamples = new ArrayList<DuckDbTypeAndExample<?>>();
    var nestedListExamples = new ArrayList<DuckDbTypeAndExample<?>>();
    var nestedArrayExamples = new ArrayList<DuckDbTypeAndExample<?>>();
    var arrayOfListExamples = new ArrayList<DuckDbTypeAndExample<?>>();
    var mapKeyExamples = new ArrayList<DuckDbTypeAndExample<?>>();
    var mapValueExamples = new ArrayList<DuckDbTypeAndExample<?>>();
    for (var s : scalars) {
      tryDerive(s, DuckDbTypeTest::toListExample, listExamples);
      tryDerive(s, DuckDbTypeTest::toArrayExample, arrayExamples);
      if (s.supportsNested()) {
        tryDerive(s, DuckDbTypeTest::toNestedListExample, nestedListExamples);
        tryDerive(s, DuckDbTypeTest::toNestedArrayExample, nestedArrayExamples);
        tryDerive(s, DuckDbTypeTest::toArrayOfListExample, arrayOfListExamples);
      }
      if (s.supportsMap()) {
        tryDerive(s, DuckDbTypeTest::toMapAsKeyExample, mapKeyExamples);
        tryDerive(s, DuckDbTypeTest::toMapAsValueExample, mapValueExamples);
      }
    }

    System.out.println("Generated " + listExamples.size() + " LIST type tests");
    System.out.println("Generated " + arrayExamples.size() + " ARRAY(size) type tests");
    System.out.println("Generated " + nestedListExamples.size() + " LIST-of-LIST tests");
    System.out.println("Generated " + nestedArrayExamples.size() + " ARRAY-of-ARRAY tests");
    System.out.println("Generated " + arrayOfListExamples.size() + " ARRAY-of-LIST tests");
    System.out.println("Generated " + mapKeyExamples.size() + " MAP-as-key tests");
    System.out.println("Generated " + mapValueExamples.size() + " MAP-as-value tests\n");

    var allWithArrays = new ArrayList<DuckDbTypeAndExample<?>>();
    allWithArrays.addAll(All);
    allWithArrays.addAll(listExamples);
    allWithArrays.addAll(arrayExamples);
    allWithArrays.addAll(nestedListExamples);
    allWithArrays.addAll(nestedArrayExamples);
    allWithArrays.addAll(arrayOfListExamples);
    allWithArrays.addAll(mapKeyExamples);
    allWithArrays.addAll(mapValueExamples);

    // Test JSON roundtrip first (no database connection needed) - parallel
    System.out.println("=== JSON Roundtrip Tests (parallel) ===");
    allWithArrays.parallelStream().forEach(DuckDbTypeTest::testJsonRoundtrip);
    System.out.println();

    // Run all DB tests in parallel - each test gets its own connection
    System.out.println("=== DB Roundtrip Tests (parallel) ===");
    var failures =
        allWithArrays.parallelStream()
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

                  return errors.stream();
                })
            .toList();

    // ==================== Query Analysis Tests ====================
    System.out.println("\n=== Query Analysis Tests (parallel) ===");
    var analysisFailures =
        allWithArrays.stream()
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

    var allFailures = new ArrayList<>(failures);
    allFailures.addAll(analysisFailures);

    // ==================== Edge Case Tests ====================
    System.out.println("\n=== Negative Test: ARRAY Size Validation ===\n");

    // Test ARRAY size validation - DuckDB should reject wrong-sized arrays
    // Expected error: "Conversion Error: Cannot cast array of size 4 to array of size 5"
    System.out.println("ARRAY[5] with 4 values (expected: size mismatch error)");
    try (Connection negConn = java.sql.DriverManager.getConnection("jdbc:duckdb:");
        Statement stmt = negConn.createStatement()) {
      stmt.execute("CREATE TABLE array_size_test (vec INTEGER[5])");
      stmt.execute("INSERT INTO array_size_test VALUES (array_value(1, 2, 3, 4)::INTEGER[5])");
      allFailures.add("ARRAY size validation should have thrown error");
    } catch (SQLException e) {
      String errMsg = e.getMessage();
      if (errMsg.contains("Conversion Error") || errMsg.contains("Cannot cast array")) {
        System.out.println(
            "  PASSED - got expected error: "
                + errMsg.substring(0, Math.min(100, errMsg.length()))
                + "...\n");
      } else {
        allFailures.add("ARRAY size validation: unexpected error: " + errMsg);
      }
    }

    System.out.println("\n=====================================");
    if (allFailures.isEmpty()) {
      System.out.println("All tests passed!");
    } else {
      allFailures.forEach(f -> System.err.println("FAILURE: " + f));
      throw new RuntimeException(
          allFailures.size() + " tests failed:\n" + String.join("\n", allFailures));
    }
    System.out.println("=====================================");
  }

  static <A> void testJsonRoundtrip(DuckDbTypeAndExample<A> t) {
    try {
      DuckDbJson<A> jsonCodec = t.type.duckDbJson();
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

  static <A> void testQueryAnalysis(Connection conn, DuckDbTypeAndExample<A> t)
      throws SQLException {
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
      conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void testQueryAnalysisWithParam(Connection conn, DuckDbTypeAndExample<A> t)
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
      conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
    }
  }

  static <A> void batchInsert(Connection conn, DbType<A> type, String tableName, A value)
      throws SQLException {
    if (type.write().inlineSql(value).isPresent()) {
      Fragment.of("INSERT INTO " + tableName + " (v) VALUES (")
          .value(type, value)
          .append(")")
          .update()
          .run(conn);
    } else {
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
  }

  static <A> void testCase(Connection conn, DuckDbTypeAndExample<A> t) throws SQLException {
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
      conn.createStatement().execute("DROP TABLE IF EXISTS " + tableName);
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
    if (expected instanceof Object[] expArr && actual instanceof Object[] actArr) {
      if (expArr.length != actArr.length) return false;
      for (int i = 0; i < expArr.length; i++) {
        if (!areEqual(actArr[i], expArr[i])) return false;
      }
      return true;
    }
    // BigDecimal: compare by value, not scale
    if (expected instanceof BigDecimal && actual instanceof BigDecimal) {
      return ((BigDecimal) actual).compareTo((BigDecimal) expected) == 0;
    }
    // List: compare element by element
    if (expected instanceof List<?> && actual instanceof List<?>) {
      List<?> expectedList = (List<?>) expected;
      List<?> actualList = (List<?>) actual;
      if (expectedList.size() != actualList.size()) return false;
      for (int i = 0; i < expectedList.size(); i++) {
        if (!areEqual(actualList.get(i), expectedList.get(i))) return false;
      }
      return true;
    }
    // Map: compare entries
    if (expected instanceof java.util.Map<?, ?> && actual instanceof java.util.Map<?, ?>) {
      java.util.Map<?, ?> expectedMap = (java.util.Map<?, ?>) expected;
      java.util.Map<?, ?> actualMap = (java.util.Map<?, ?>) actual;
      if (expectedMap.size() != actualMap.size()) return false;
      for (var entry : expectedMap.entrySet()) {
        Object actualValue = actualMap.get(entry.getKey());
        if (!areEqual(actualValue, entry.getValue())) return false;
      }
      return true;
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

  @Test
  public void testListOfUnion() {
    DuckDbType<List<IntOrString>> listOfUnionType = intOrStringType.list();

    withConnection(
        conn -> {
          var stmt = conn.createStatement();
          String tableName = uniqueTableName("union_list_test");
          stmt.execute(
              "CREATE TEMPORARY TABLE " + tableName + " (v UNION(num INTEGER, str VARCHAR)[])");

          List<IntOrString> mixed = List.of(new IntOrString.Num(42), new IntOrString.Str("hello"));
          List<IntOrString> allNums =
              List.of(new IntOrString.Num(1), new IntOrString.Num(2), new IntOrString.Num(3));
          List<IntOrString> allStrs = List.of(new IntOrString.Str("a"), new IntOrString.Str("b"));
          List<IntOrString> empty = List.of();

          batchInsert(conn, listOfUnionType, tableName, mixed);
          batchInsert(conn, listOfUnionType, tableName, allNums);
          batchInsert(conn, listOfUnionType, tableName, allStrs);
          batchInsert(conn, listOfUnionType, tableName, empty);

          var rs = stmt.executeQuery("SELECT v FROM " + tableName + " ORDER BY rowid");

          // Row 1: mixed [Num(42), Str("hello")]
          if (!rs.next()) throw new RuntimeException("Expected row 1");
          List<IntOrString> row1 = listOfUnionType.read().read(rs, 1);
          assertEquals(row1.size(), 2, "row1 size");
          assertEquals(row1.get(0), new IntOrString.Num(42), "row1[0]");
          assertEquals(row1.get(1), new IntOrString.Str("hello"), "row1[1]");

          // Row 2: all nums [Num(1), Num(2), Num(3)]
          if (!rs.next()) throw new RuntimeException("Expected row 2");
          List<IntOrString> row2 = listOfUnionType.read().read(rs, 1);
          assertEquals(row2.size(), 3, "row2 size");
          assertEquals(row2.get(0), new IntOrString.Num(1), "row2[0]");
          assertEquals(row2.get(1), new IntOrString.Num(2), "row2[1]");
          assertEquals(row2.get(2), new IntOrString.Num(3), "row2[2]");

          // Row 3: all strings [Str("a"), Str("b")]
          if (!rs.next()) throw new RuntimeException("Expected row 3");
          List<IntOrString> row3 = listOfUnionType.read().read(rs, 1);
          assertEquals(row3.size(), 2, "row3 size");
          assertEquals(row3.get(0), new IntOrString.Str("a"), "row3[0]");
          assertEquals(row3.get(1), new IntOrString.Str("b"), "row3[1]");

          // Row 4: empty list
          if (!rs.next()) throw new RuntimeException("Expected row 4");
          List<IntOrString> row4 = listOfUnionType.read().read(rs, 1);
          assertEquals(row4.size(), 0, "row4 size");

          System.out.println(
              "List of UNION write+read test passed: mixed, all-num, all-str, empty");
          return null;
        });
  }

  @Test
  public void testListOfUnionJsonRoundtrip() {
    DuckDbType<List<IntOrString>> listOfUnionType = intOrStringType.list();
    DuckDbJson<List<IntOrString>> jsonCodec = listOfUnionType.duckDbJson();

    // Mixed types
    List<IntOrString> mixed =
        List.of(new IntOrString.Num(42), new IntOrString.Str("hello"), new IntOrString.Num(7));
    JsonValue jsonMixed = jsonCodec.toJson(mixed);
    List<IntOrString> decodedMixed = jsonCodec.fromJson(JsonValue.parse(jsonMixed.encode()));
    assertEquals(decodedMixed, mixed, "mixed JSON roundtrip");

    // All nums
    List<IntOrString> allNums =
        List.of(new IntOrString.Num(1), new IntOrString.Num(2), new IntOrString.Num(3));
    JsonValue jsonNums = jsonCodec.toJson(allNums);
    List<IntOrString> decodedNums = jsonCodec.fromJson(JsonValue.parse(jsonNums.encode()));
    assertEquals(decodedNums, allNums, "all-nums JSON roundtrip");

    // All strings
    List<IntOrString> allStrs = List.of(new IntOrString.Str("a"), new IntOrString.Str("b"));
    JsonValue jsonStrs = jsonCodec.toJson(allStrs);
    List<IntOrString> decodedStrs = jsonCodec.fromJson(JsonValue.parse(jsonStrs.encode()));
    assertEquals(decodedStrs, allStrs, "all-strs JSON roundtrip");

    // Empty
    List<IntOrString> empty = List.of();
    JsonValue jsonEmpty = jsonCodec.toJson(empty);
    List<IntOrString> decodedEmpty = jsonCodec.fromJson(JsonValue.parse(jsonEmpty.encode()));
    assertEquals(decodedEmpty, empty, "empty JSON roundtrip");

    System.out.println("List of UNION JSON roundtrip test passed: mixed, all-num, all-str, empty");
  }
}
