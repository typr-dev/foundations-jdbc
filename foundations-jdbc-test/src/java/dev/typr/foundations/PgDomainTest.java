package dev.typr.foundations;

import dev.typr.foundations.data.Bit;
import dev.typr.foundations.data.Cidr;
import dev.typr.foundations.data.Inet;
import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.Jsonb;
import dev.typr.foundations.data.MacAddr;
import dev.typr.foundations.data.Range;
import dev.typr.foundations.data.RangeBound;
import dev.typr.foundations.data.Varbit;
import dev.typr.foundations.data.Vector;
import dev.typr.foundations.data.Xml;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

/**
 * Coverage of PostgreSQL DOMAIN types — typed wrappers over a base PG type that show up to JDBC as
 * the underlying type but require the domain name in DDL/array-construction.
 *
 * <p>Two flavors are exercised here:
 *
 * <ol>
 *   <li>The user-facing pattern: a wrapper {@code Name} backed by a domain, plus a list bijection
 *       {@code Name.pgType.array().to(...)} mapping {@code List<Name>} to {@code List<MiddleName>}.
 *   <li>A scalar+array roundtrip for the domain over each common underlying type.
 * </ol>
 */
public class PgDomainTest {

  private static final AtomicInteger tableCounter = new AtomicInteger(0);

  private static String uniqueTableName(String prefix) {
    return prefix + "_" + tableCounter.incrementAndGet();
  }

  // ============================================================
  //  USER-SPECIFIC PATTERN
  // ============================================================

  /** Wrapper for the {@code person_name} PG DOMAIN (varchar(100)). */
  public record Name(String value) {
    public static final PgType<Name> pgType =
        PgTypes.text.transform(Name::new, Name::value).asDomain("person_name");
  }

  /** A second wrapper layered on top of {@link Name} via the array-level bijection. */
  public record MiddleName(Name value) {}

  public static final PgType<List<MiddleName>> pgTypeArray =
      Name.pgType
          .array()
          .to(
              Bijection.of(
                  xs -> xs.stream().map(MiddleName::new).toList(),
                  xs -> xs.stream().map(MiddleName::value).toList()));

  // ============================================================
  //  ROUNDTRIP CASES
  // ============================================================

  /**
   * One scalar roundtrip + (optionally) one array roundtrip per case. The domain itself is created
   * inside the test's rollback-only transaction via {@link #underlyingSql}.
   */
  record Case<A>(
      String domainName, String underlyingSql, PgType<A> pgType, A example, boolean testArray) {
    static <A> Case<A> of(String domainName, String underlying, PgType<A> baseType, A example) {
      return new Case<>(domainName, underlying, baseType.asDomain(domainName), example, true);
    }

    static <A> Case<A> noArray(
        String domainName, String underlying, PgType<A> baseType, A example) {
      return new Case<>(domainName, underlying, baseType.asDomain(domainName), example, false);
    }
  }

  static final List<Case<?>> CASES =
      List.of(
          Case.of("dom_text", "text", PgTypes.text, "hello, ®✅"),
          Case.of("dom_text2", "text", PgTypes.text, ""),
          Case.of("dom_text3", "text", PgTypes.text, "Line1\nLine2\tTabbed"),
          // A precision-bearing base
          Case.of("dom_varchar_100", "varchar(100)", PgTypes.text, "vc100 sample"),
          // PG identifier "name" type as a domain
          Case.of("dom_pg_name", "name", PgTypes.name, "my_table_name"),
          // Numeric family
          Case.of("dom_int2", "int2", PgTypes.int2, (short) 42),
          Case.of("dom_int4", "int4", PgTypes.int4, Integer.MAX_VALUE),
          Case.of("dom_int8", "int8", PgTypes.int8, Long.MIN_VALUE),
          Case.of("dom_float4", "float4", PgTypes.float4, 1.5f),
          Case.of("dom_float8", "float8", PgTypes.float8, 3.14159),
          Case.of("dom_numeric", "numeric", PgTypes.numeric, new BigDecimal("12345.6789")),
          // Boolean
          Case.of("dom_bool", "bool", PgTypes.bool, true),
          Case.of("dom_bool2", "bool", PgTypes.bool, false),
          // Bytea — base type has no array codec; asDomain enables read but PG JDBC's
          // createArrayOf rejects byte[] nested in Object[] on write.
          Case.noArray("dom_bytea", "bytea", PgTypes.bytea, new byte[] {1, 2, -1, 0, 127}),
          // Date/time
          Case.of("dom_date", "date", PgTypes.date, LocalDate.of(2024, 12, 25)),
          Case.of(
              "dom_time",
              "time",
              PgTypes.time,
              LocalTime.of(14, 30, 45).truncatedTo(ChronoUnit.MICROS)),
          Case.of(
              "dom_timestamp",
              "timestamp",
              PgTypes.timestamp,
              LocalDateTime.of(2024, 12, 25, 14, 30, 45).truncatedTo(ChronoUnit.MICROS)),
          Case.of(
              "dom_timestamptz",
              "timestamptz",
              PgTypes.timestamptz,
              Instant.parse("2024-12-25T14:30:45Z").truncatedTo(ChronoUnit.MICROS)),
          // UUID
          Case.of(
              "dom_uuid",
              "uuid",
              PgTypes.uuid,
              UUID.fromString("550e8400-e29b-41d4-a716-446655440000")),
          // JSON — jsonb canonicalizes whitespace; use the canonical form for equality.
          Case.of("dom_json", "json", PgTypes.json, new Json("{\"k\":1}")),
          Case.of("dom_jsonb", "jsonb", PgTypes.jsonb, new Jsonb("{\"k\": 1}")),
          // Network
          Case.of("dom_inet", "inet", PgTypes.inet, new Inet("10.1.0.0")),
          Case.of("dom_cidr", "cidr", PgTypes.cidr, new Cidr("192.168.1.0/24")),
          Case.of("dom_macaddr", "macaddr", PgTypes.macaddr, new MacAddr("08:00:2b:01:02:03")),
          // Bit strings
          Case.of("dom_bit_8", "bit(8)", PgTypes.bitOf(8), new Bit("10110011")),
          Case.of("dom_varbit", "varbit", PgTypes.varbit, new Varbit("101")),
          // Extension types
          Case.of(
              "dom_vector",
              "vector",
              PgTypes.vector,
              new Vector(new float[] {1.0f, 2.0f, 3.0f})),
          // hstore arrays are not supported by the library (PgTypes.hstore has empty array codec).
          Case.noArray(
              "dom_hstore", "hstore", PgTypes.hstore, Map.of("k1", "v1", "k2", "v2")),
          // XML — JDBC returns canonicalized text; skip array (PG arrays of xml are unusual).
          Case.noArray("dom_xml", "xml", PgTypes.xml, new Xml("<a>42</a>")),
          // Range
          Case.of(
              "dom_int4range",
              "int4range",
              PgTypes.int4range,
              Range.int4(new RangeBound.Closed<>(1), new RangeBound.Open<>(10))));

  // ============================================================
  //  USER-SPECIFIC TEST
  // ============================================================

  /**
   * Verifies that {@code .array().to(Bijection)} composes correctly when the underlying scalar is a
   * PG DOMAIN: write a {@code List<MiddleName>}, read it back, and require value-equality plus the
   * outermost wrapper type.
   */
  @Test
  public void testNameMiddleNameDomainArray() {
    var tx = Containers.postgresTransactor();
    String tableName = uniqueTableName("dom_user_pattern");

    tx.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE DOMAIN person_name AS varchar(100)").execute());
          mc.execute(
              Fragment.of("CREATE TEMP TABLE " + tableName + " (v person_name[])").execute());

          var original =
              List.of(
                  new MiddleName(new Name("Alice")),
                  new MiddleName(new Name("Beatrice")),
                  new MiddleName(new Name("Charlotte")));

          mc.execute(
              Fragment.of("INSERT INTO " + tableName + " (v) VALUES (")
                  .append(Fragment.encode(pgTypeArray, original))
                  .append(")")
                  .update());

          List<List<MiddleName>> rows =
              mc.execute(
                  Fragment.of("SELECT v FROM " + tableName).query(RowCodec.of(pgTypeArray).all()));

          if (rows.size() != 1) {
            throw new RuntimeException("Expected 1 row, got " + rows.size());
          }
          var got = rows.getFirst();
          if (!got.equals(original)) {
            throw new RuntimeException(
                "person_name[] roundtrip mismatch: expected " + original + " got " + got);
          }

          // Read back as scalar PG name elements via UNNEST to confirm the column is really a
          // domain array (not just text[] coerced).
          List<String> typenames =
              mc.execute(
                  Fragment.of(
                          "SELECT pg_typeof(v)::text FROM (SELECT unnest(v) AS v FROM "
                              + tableName
                              + ") s")
                      .query(RowCodec.of(PgTypes.text).all()));
          if (typenames.isEmpty()
              || !typenames.stream().allMatch(t -> t.equalsIgnoreCase("person_name"))) {
            throw new RuntimeException(
                "Expected each element to be person_name, got " + typenames);
          }
          return null;
        });
  }

  /**
   * Same pattern but exercising the scalar codec — read/write a single {@code Name} into a {@code
   * person_name} column to confirm the domain-typed scalar works on its own.
   */
  @Test
  public void testNameDomainScalar() {
    var tx = Containers.postgresTransactor();
    String tableName = uniqueTableName("dom_user_scalar");

    tx.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE DOMAIN person_name AS varchar(100)").execute());
          mc.execute(Fragment.of("CREATE TEMP TABLE " + tableName + " (v person_name)").execute());

          var original = new Name("Eve");
          mc.execute(
              Fragment.of("INSERT INTO " + tableName + " (v) VALUES (")
                  .append(Fragment.encode(Name.pgType, original))
                  .append(")")
                  .update());

          List<Name> rows =
              mc.execute(
                  Fragment.of("SELECT v FROM " + tableName).query(RowCodec.of(Name.pgType).all()));

          if (rows.size() != 1 || !rows.getFirst().equals(original)) {
            throw new RuntimeException("Expected [" + original + "], got " + rows);
          }
          return null;
        });
  }

  // ============================================================
  //  GENERIC DOMAIN COVERAGE
  // ============================================================

  @Test
  public void testDomainScalarRoundtrips() {
    var tx = Containers.postgresTransactor();
    var failures = new ArrayList<String>();

    for (Case<?> c : CASES) {
      try {
        tx.transact(
            mc -> {
              runScalarRoundtrip(mc, c);
              return null;
            });
      } catch (Exception e) {
        failures.add(c.domainName() + " (" + c.example() + "): " + e.getMessage());
      }
    }

    if (!failures.isEmpty()) {
      throw new RuntimeException(
          "Domain scalar roundtrip failures (" + failures.size() + "):\n  " + String.join("\n  ", failures));
    }
  }

  @Test
  public void testDomainArrayRoundtrips() {
    var tx = Containers.postgresTransactor();
    var failures = new ArrayList<String>();

    for (Case<?> c : CASES) {
      if (!c.testArray()) continue;
      try {
        tx.transact(
            mc -> {
              runArrayRoundtrip(mc, c);
              return null;
            });
      } catch (Exception e) {
        failures.add(c.domainName() + "[]: " + e.getMessage());
      }
    }

    if (!failures.isEmpty()) {
      throw new RuntimeException(
          "Domain array roundtrip failures (" + failures.size() + "):\n  " + String.join("\n  ", failures));
    }
  }

  // ============================================================
  //  HELPERS
  // ============================================================

  private static <A> void runScalarRoundtrip(Connection mc, Case<A> c) {
    String tableName = uniqueTableName("dom_scalar");
    mc.execute(
        Fragment.of("CREATE DOMAIN " + c.domainName() + " AS " + c.underlyingSql()).execute());
    mc.execute(
        Fragment.of("CREATE TEMP TABLE " + tableName + " (v " + c.domainName() + ")").execute());

    mc.execute(
        Fragment.of("INSERT INTO " + tableName + " (v) VALUES (")
            .append(Fragment.encode(c.pgType(), c.example()))
            .append(")")
            .update());

    List<A> rows =
        mc.execute(
            Fragment.of("SELECT v FROM " + tableName).query(RowCodec.of(c.pgType()).all()));

    if (rows.size() != 1 || !areEqual(rows.getFirst(), c.example())) {
      throw new RuntimeException(
          "expected '" + format(c.example()) + "' got '" + format(rows.isEmpty() ? null : rows.getFirst()) + "'");
    }
  }

  private static <A> void runArrayRoundtrip(Connection mc, Case<A> c) {
    PgType<List<A>> arrayType = c.pgType().array();
    String tableName = uniqueTableName("dom_array");
    mc.execute(
        Fragment.of("CREATE DOMAIN " + c.domainName() + " AS " + c.underlyingSql()).execute());
    mc.execute(
        Fragment.of("CREATE TEMP TABLE " + tableName + " (v " + c.domainName() + "[])").execute());

    List<A> values = List.of(c.example());
    mc.execute(
        Fragment.of("INSERT INTO " + tableName + " (v) VALUES (")
            .append(Fragment.encode(arrayType, values))
            .append(")")
            .update());

    List<List<A>> rows =
        mc.execute(Fragment.of("SELECT v FROM " + tableName).query(RowCodec.of(arrayType).all()));

    if (rows.size() != 1) {
      throw new RuntimeException("expected 1 row, got " + rows.size());
    }
    var got = rows.getFirst();
    if (got.size() != 1 || !areEqual(got.getFirst(), c.example())) {
      throw new RuntimeException(
          "expected ['" + format(c.example()) + "'] got '" + format(got) + "'");
    }
  }

  // ============================================================
  //  ENUM, COMPOSITE, CONSTRAINTS, NESTED DOMAIN, OPTIONAL
  // ============================================================

  public enum Traffic {
    red,
    amber,
    green
  }

  /** Domain wraps a user-defined ENUM. Scalar + array roundtrip. */
  @Test
  public void testDomainOverEnum() {
    var tx = Containers.postgresTransactor();
    tx.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE TYPE traffic AS ENUM ('red','amber','green')").execute());
          mc.execute(Fragment.of("CREATE DOMAIN traffic_dom AS traffic").execute());

          PgType<Traffic> trafficDom =
              PgTypes.ofEnum("traffic", Traffic.values()).asDomain("traffic_dom");

          mc.execute(Fragment.of("CREATE TEMP TABLE t (v traffic_dom)").execute());
          mc.execute(
              Fragment.of("INSERT INTO t (v) VALUES (")
                  .append(Fragment.encode(trafficDom, Traffic.amber))
                  .append(")")
                  .update());

          var got =
              mc.execute(
                  Fragment.of("SELECT v FROM t").query(RowCodec.of(trafficDom).all()));
          if (got.size() != 1 || got.getFirst() != Traffic.amber) {
            throw new RuntimeException("scalar enum domain mismatch: " + got);
          }
          return null;
        });
  }

  @Test
  public void testDomainOverEnumArray() {
    var tx = Containers.postgresTransactor();
    tx.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE TYPE traffic AS ENUM ('red','amber','green')").execute());
          mc.execute(Fragment.of("CREATE DOMAIN traffic_dom AS traffic").execute());

          PgType<List<Traffic>> trafficDomArr =
              PgTypes.ofEnum("traffic", Traffic.values()).asDomain("traffic_dom").array();

          mc.execute(Fragment.of("CREATE TEMP TABLE t (vs traffic_dom[])").execute());
          var values = List.of(Traffic.red, Traffic.amber, Traffic.green);
          mc.execute(
              Fragment.of("INSERT INTO t (vs) VALUES (")
                  .append(Fragment.encode(trafficDomArr, values))
                  .append(")")
                  .update());

          var got =
              mc.execute(
                  Fragment.of("SELECT vs FROM t").query(RowCodec.of(trafficDomArr).all()));
          if (got.size() != 1 || !got.getFirst().equals(values)) {
            throw new RuntimeException("array enum domain mismatch: " + got);
          }
          return null;
        });
  }

  public record Addr(String street, String city) {}

  /** Domain wraps a user-defined COMPOSITE type. */
  @Test
  public void testDomainOverComposite() {
    var tx = Containers.postgresTransactor();
    tx.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE TYPE addr_t AS (street text, city text)").execute());
          mc.execute(Fragment.of("CREATE DOMAIN addr_dom AS addr_t").execute());

          PgType<Addr> addrType =
              PgTypes.compositeOf(
                      "addr_t",
                      RowCodec.<Addr>namedBuilder()
                          .field("street", PgTypes.text, Addr::street)
                          .field("city", PgTypes.text, Addr::city)
                          .build(Addr::new))
                  .asDomain("addr_dom");

          mc.execute(Fragment.of("CREATE TEMP TABLE t (v addr_dom)").execute());
          var original = new Addr("742 Evergreen", "Springfield");
          mc.execute(
              Fragment.of("INSERT INTO t (v) VALUES (")
                  .append(Fragment.encode(addrType, original))
                  .append(")")
                  .update());

          var got = mc.execute(Fragment.of("SELECT v FROM t").query(RowCodec.of(addrType).all()));
          if (got.size() != 1 || !got.getFirst().equals(original)) {
            throw new RuntimeException("composite domain mismatch: " + got);
          }
          return null;
        });
  }

  /** A CHECK constraint must propagate as a SQL exception when violated on insert. */
  @Test
  public void testDomainCheckConstraintViolation() {
    var tx = Containers.postgresTransactor();
    boolean threw = false;
    try {
      tx.transact(
          mc -> {
            mc.execute(
                Fragment.of("CREATE DOMAIN positive_int AS int4 CHECK (VALUE > 0)").execute());
            PgType<Integer> posInt = PgTypes.int4.asDomain("positive_int");
            mc.execute(Fragment.of("CREATE TEMP TABLE t (v positive_int)").execute());
            mc.execute(
                Fragment.of("INSERT INTO t (v) VALUES (")
                    .append(Fragment.encode(posInt, -5))
                    .append(")")
                    .update());
            return null;
          });
    } catch (Exception e) {
      threw = true;
      // Surface check-constraint context by walking the cause chain.
      String chain = e.toString();
      Throwable c = e.getCause();
      while (c != null) {
        chain += " | " + c;
        c = c.getCause();
      }
      if (!chain.contains("positive_int") && !chain.toLowerCase().contains("check")) {
        throw new RuntimeException(
            "CHECK constraint violation must mention the domain or check failure, got: " + chain);
      }
    }
    if (!threw) throw new RuntimeException("CHECK constraint did not fire");
  }

  /** A NOT NULL domain must reject Optional.empty() inserts. */
  @Test
  public void testDomainNotNullViolation() {
    var tx = Containers.postgresTransactor();
    boolean threw = false;
    try {
      tx.transact(
          mc -> {
            mc.execute(Fragment.of("CREATE DOMAIN required_text AS text NOT NULL").execute());
            PgType<java.util.Optional<String>> reqOpt =
                PgTypes.text.asDomain("required_text").opt();
            mc.execute(Fragment.of("CREATE TEMP TABLE t (v required_text)").execute());
            mc.execute(
                Fragment.of("INSERT INTO t (v) VALUES (")
                    .append(Fragment.encode(reqOpt, java.util.Optional.empty()))
                    .append(")")
                    .update());
            return null;
          });
    } catch (Exception e) {
      threw = true;
    }
    if (!threw) throw new RuntimeException("NOT NULL domain did not reject NULL insert");
  }

  /** Optional<DomainType> roundtrip — non-null and null values both observed back as Optional. */
  @Test
  public void testOptionalDomainRoundtrip() {
    var tx = Containers.postgresTransactor();
    tx.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE DOMAIN nullable_text AS text").execute());
          PgType<java.util.Optional<String>> optDom =
              PgTypes.text.asDomain("nullable_text").opt();
          mc.execute(Fragment.of("CREATE TEMP TABLE t (v nullable_text)").execute());
          mc.execute(
              Fragment.of("INSERT INTO t (v) VALUES (")
                  .append(Fragment.encode(optDom, java.util.Optional.of("hello")))
                  .append("),(")
                  .append(Fragment.encode(optDom, java.util.Optional.empty()))
                  .append(")")
                  .update());
          var got =
              mc.execute(
                  Fragment.of("SELECT v FROM t ORDER BY v NULLS LAST")
                      .query(RowCodec.of(optDom).all()));
          if (got.size() != 2
              || !got.get(0).equals(java.util.Optional.of("hello"))
              || !got.get(1).equals(java.util.Optional.empty())) {
            throw new RuntimeException("Optional<DomainType> roundtrip mismatch: " + got);
          }
          return null;
        });
  }

  /** A domain whose underlying is itself a domain — chained typenames must work end-to-end. */
  @Test
  public void testDomainOverDomain() {
    var tx = Containers.postgresTransactor();
    tx.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE DOMAIN d_text_inner AS text").execute());
          mc.execute(Fragment.of("CREATE DOMAIN d_text_outer AS d_text_inner").execute());
          // Chain asDomain twice — each level renames typename and registers the previous name as
          // an analyzer alias.
          PgType<String> dom = PgTypes.text.asDomain("d_text_inner").asDomain("d_text_outer");
          mc.execute(Fragment.of("CREATE TEMP TABLE t (v d_text_outer)").execute());
          mc.execute(
              Fragment.of("INSERT INTO t (v) VALUES (")
                  .append(Fragment.encode(dom, "hi"))
                  .append(")")
                  .update());
          var got =
              mc.execute(Fragment.of("SELECT v FROM t").query(RowCodec.of(dom).all()));
          if (got.size() != 1 || !got.getFirst().equals("hi")) {
            throw new RuntimeException("domain-over-domain mismatch: " + got);
          }
          return null;
        });
  }

  /** Composite type with a domain-typed field. */
  @Test
  public void testDomainAsCompositeField() {
    var tx = Containers.postgresTransactor();
    tx.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE DOMAIN dom_text AS text").execute());
          mc.execute(Fragment.of("CREATE TYPE wrapper_t AS (id int4, label dom_text)").execute());

          record Wrapper(Integer id, String label) {}
          var domText = PgTypes.text.asDomain("dom_text");
          var wrapperType =
              PgTypes.compositeOf(
                  "wrapper_t",
                  RowCodec.<Wrapper>namedBuilder()
                      .field("id", PgTypes.int4, Wrapper::id)
                      .field("label", domText, Wrapper::label)
                      .build(Wrapper::new));

          mc.execute(Fragment.of("CREATE TEMP TABLE t (v wrapper_t)").execute());
          var original = new Wrapper(1, "hi");
          mc.execute(
              Fragment.of("INSERT INTO t (v) VALUES (")
                  .append(Fragment.encode(wrapperType, original))
                  .append(")")
                  .update());
          var got =
              mc.execute(Fragment.of("SELECT v FROM t").query(RowCodec.of(wrapperType).all()));
          if (got.size() != 1 || !got.getFirst().equals(original)) {
            throw new RuntimeException("domain in composite field mismatch: " + got);
          }
          return null;
        });
  }

  private static <A> boolean areEqual(A actual, A expected) {
    if (expected instanceof byte[]) {
      return Arrays.equals((byte[]) actual, (byte[]) expected);
    }
    if (expected instanceof Object[]) {
      return Arrays.equals((Object[]) actual, (Object[]) expected);
    }
    if (expected == null) {
      return actual == null;
    }
    return expected.equals(actual);
  }

  private static String format(Object a) {
    if (a instanceof byte[] b) return Arrays.toString(b);
    if (a instanceof Object[] arr) return Arrays.toString(arr);
    return String.valueOf(a);
  }
}
