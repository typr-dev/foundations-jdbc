package dev.typr.foundations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

/**
 * End-to-end pipeline fuzz: for each {@code (PgType<T>, T)} pair, push the value through the
 * full encode → INSERT → SELECT → decode round-trip and assert the read-back equals the
 * original. Also exercises the array, multi-element array, and 2-D array forms — anywhere the
 * codec's {@code pgText} / {@code wireDecode} / record-text-parsing path runs is covered for
 * every value.
 *
 * <p>Intentionally adversarial inputs: empty strings, embedded delimiters, embedded braces,
 * embedded quotes, backslashes, NULL-literal-looking strings, control chars, multi-byte UTF-8,
 * emoji, RTL, combining marks, zero-width characters. If any of these break, the codec is wrong.
 *
 * <p>Pairs explicitly to a {@link PgType} (text, varchar, name, etc.) — PG's text array form is
 * type-dependent (delimiters, what gets quoted, how PG canonicalises elements), so we never
 * leave the type unstated.
 */
public class PgFuzzRoundtripTest {

  private static final AtomicInteger tableCounter = new AtomicInteger(0);

  /** A typed adversarial input. */
  public record Fuzz<T>(String label, PgType<T> type, T value) {}

  /**
   * Crazy-string corpus paired with a PgType. Same set is run scalar / 1-D array (singleton +
   * mixed) / 2-D array, plus once through a {@code text.asDomain(...)} wrapper.
   */
  static final List<Fuzz<?>> CASES =
      List.of(
          // Empty / whitespace
          new Fuzz<>("empty", PgTypes.text, ""),
          new Fuzz<>("single space", PgTypes.text, " "),
          new Fuzz<>("trailing space", PgTypes.text, "x "),
          new Fuzz<>("leading space", PgTypes.text, " x"),
          new Fuzz<>("only whitespace", PgTypes.text, "   "),
          new Fuzz<>("tab", PgTypes.text, "\t"),
          new Fuzz<>("mixed whitespace", PgTypes.text, " \t \r \t "),

          // Punctuation that PG's text-array scanner gives meaning
          new Fuzz<>("comma", PgTypes.text, "a,b"),
          new Fuzz<>("semicolon", PgTypes.text, "a;b"),
          new Fuzz<>("colon", PgTypes.text, "a:b"),
          new Fuzz<>("double colon (cast op)", PgTypes.text, "value::text"),
          new Fuzz<>("pipe", PgTypes.text, "a|b"),
          new Fuzz<>("opening brace", PgTypes.text, "{"),
          new Fuzz<>("closing brace", PgTypes.text, "}"),
          new Fuzz<>("brace pair", PgTypes.text, "{}"),
          new Fuzz<>("nested braces", PgTypes.text, "{{}}"),
          new Fuzz<>("looks like array literal", PgTypes.text, "{a,b,c}"),
          new Fuzz<>("looks like multi-dim", PgTypes.text, "{{a,b},{c,d}}"),
          new Fuzz<>("opening paren", PgTypes.text, "("),
          new Fuzz<>("looks like record", PgTypes.text, "(a,b)"),
          new Fuzz<>("looks like range", PgTypes.text, "[1,10)"),
          new Fuzz<>("just brackets", PgTypes.text, "[]"),

          // Quotes and backslashes — the array scanner's escape rules
          new Fuzz<>("single dquote", PgTypes.text, "\""),
          new Fuzz<>("paired dquotes", PgTypes.text, "\"\""),
          new Fuzz<>("dquote in middle", PgTypes.text, "a\"b"),
          new Fuzz<>("escaped dquote in middle", PgTypes.text, "a\\\"b"),
          new Fuzz<>("looks like quoted elem", PgTypes.text, "\"hello\""),
          new Fuzz<>("squote", PgTypes.text, "'"),
          new Fuzz<>("paired squotes", PgTypes.text, "''"),
          new Fuzz<>("apostrophe in word", PgTypes.text, "it's"),
          new Fuzz<>("backslash", PgTypes.text, "\\"),
          new Fuzz<>("paired backslash", PgTypes.text, "\\\\"),
          new Fuzz<>("backslash n literal", PgTypes.text, "\\n"),
          new Fuzz<>("backslash everything", PgTypes.text, "\\\"\\\\\\,\\}"),

          // PG-special literal lookalikes
          new Fuzz<>("NULL literal exact", PgTypes.text, "NULL"),
          new Fuzz<>("null lowercase", PgTypes.text, "null"),
          new Fuzz<>("nUlL mixed case", PgTypes.text, "nUlL"),
          new Fuzz<>("NULL with whitespace", PgTypes.text, " NULL "),
          new Fuzz<>("NULLNULL", PgTypes.text, "NULLNULL"),

          // Control characters
          new Fuzz<>("newline", PgTypes.text, "a\nb"),
          new Fuzz<>("CR", PgTypes.text, "a\rb"),
          new Fuzz<>("CRLF", PgTypes.text, "a\r\nb"),
          new Fuzz<>("vertical tab", PgTypes.text, "ab"),
          new Fuzz<>("form feed", PgTypes.text, "a\fb"),
          new Fuzz<>("DEL char", PgTypes.text, "ab"),
          new Fuzz<>("BEL char", PgTypes.text, "ab"),

          // Multi-byte / Unicode
          new Fuzz<>("registered ®", PgTypes.text, "®"),
          new Fuzz<>("emoji single", PgTypes.text, "😀"),
          new Fuzz<>("emoji ZWJ family", PgTypes.text, "👨‍👩‍👧"),
          new Fuzz<>("emoji skin-tone", PgTypes.text, "👋🏽"),
          new Fuzz<>("RTL Arabic", PgTypes.text, "مرحبا"),
          new Fuzz<>("RTL Hebrew", PgTypes.text, "שלום"),
          new Fuzz<>("CJK Chinese", PgTypes.text, "你好"),
          new Fuzz<>("CJK Japanese", PgTypes.text, "こんにちは"),
          new Fuzz<>("combining mark é", PgTypes.text, "é"),
          new Fuzz<>("zero-width joiner", PgTypes.text, "a‍B"),
          new Fuzz<>("zero-width space", PgTypes.text, "a​B"),
          new Fuzz<>("BOM mid-string", PgTypes.text, "a﻿B"),
          new Fuzz<>("soft hyphen", PgTypes.text, "a­B"),
          new Fuzz<>("non-breaking space", PgTypes.text, "a B"),
          new Fuzz<>("supplementary plane", PgTypes.text, "𐍈"),

          // PG name (identifier) type — limited length but same scanner rules
          new Fuzz<>("name with comma", PgTypes.name, "has,comma"),
          new Fuzz<>("name with brace", PgTypes.name, "has{brace}"),
          new Fuzz<>("name with quote", PgTypes.name, "has\"quote"),

          // varchar with precision — typename WithPrec so .array() peels precision for PG's
          // createArrayOf (which only accepts bare type names like "varchar", not "varchar(50)").
          new Fuzz<>(
              "varchar(50) with all the stuff",
              PgTypes.text.withTypename(PgTypename.of("varchar", 50)),
              "{a,b}\"\\,;`"),

          // Length stress
          new Fuzz<>("100x braces", PgTypes.text, "{".repeat(100) + "}".repeat(100)),
          new Fuzz<>("100x backslash quote", PgTypes.text, "\\\"".repeat(100)));

  // ============================================================
  //  Scalar roundtrip
  // ============================================================

  @Test
  public void scalarRoundtrip() {
    var failures = new ArrayList<String>();
    Containers.postgresTransactor()
        .transact(
            mc -> {
              for (Fuzz<?> f : CASES) {
                try {
                  doScalar(mc, f);
                } catch (Throwable t) {
                  failures.add(f.label() + " (" + show(f.value()) + "): " + summarize(t));
                }
              }
              return null;
            });
    if (!failures.isEmpty()) {
      throw new RuntimeException(
          failures.size() + " scalar roundtrip failures:\n  " + String.join("\n  ", failures));
    }
  }

  private static <T> void doScalar(Connection mc, Fuzz<T> f) {
    String table = uniq("fuzz_scalar");
    String sqlType = f.type().typename().sqlType();
    mc.execute(Fragment.of("CREATE TEMP TABLE " + table + " (v " + sqlType + ")").execute());
    try {
      mc.execute(
          Fragment.of("INSERT INTO " + table + " (v) VALUES (")
              .append(Fragment.encode(f.type(), f.value()))
              .append(")")
              .update());
      var rows =
          mc.execute(Fragment.of("SELECT v FROM " + table).query(RowCodec.of(f.type()).all()));
      if (rows.size() != 1 || !valuesEqual(rows.getFirst(), f.value())) {
        throw new AssertionError(
            "expected " + show(f.value()) + " got " + show(rows.isEmpty() ? null : rows.getFirst()));
      }
    } finally {
      mc.execute(Fragment.of("DROP TABLE IF EXISTS " + table).execute());
    }
  }

  // ============================================================
  //  Array roundtrip (singleton + multi-element)
  // ============================================================

  @Test
  public void arrayRoundtrip() {
    var failures = new ArrayList<String>();
    Containers.postgresTransactor()
        .transact(
            mc -> {
              for (Fuzz<?> f : CASES) {
                try {
                  doArraySingleton(mc, f);
                } catch (Throwable t) {
                  failures.add(
                      "array[singleton] " + f.label() + " (" + show(f.value()) + "): " + summarize(t));
                }
                try {
                  doArrayMulti(mc, f);
                } catch (Throwable t) {
                  failures.add(
                      "array[multi] " + f.label() + " (" + show(f.value()) + "): " + summarize(t));
                }
              }
              return null;
            });
    if (!failures.isEmpty()) {
      throw new RuntimeException(
          failures.size() + " array roundtrip failures:\n  " + String.join("\n  ", failures));
    }
  }

  private static <T> void doArraySingleton(Connection mc, Fuzz<T> f) {
    PgType<List<T>> arr = f.type().array();
    String table = uniq("fuzz_arr_one");
    String sqlType = arr.typename().sqlType();
    mc.execute(Fragment.of("CREATE TEMP TABLE " + table + " (v " + sqlType + ")").execute());
    try {
      List<T> values = singletonList(f.value());
      mc.execute(
          Fragment.of("INSERT INTO " + table + " (v) VALUES (")
              .append(Fragment.encode(arr, values))
              .append(")")
              .update());
      var rows =
          mc.execute(Fragment.of("SELECT v FROM " + table).query(RowCodec.of(arr).all()));
      if (rows.size() != 1) throw new AssertionError("expected 1 row, got " + rows.size());
      var got = rows.getFirst();
      if (got.size() != 1 || !valuesEqual(got.get(0), f.value())) {
        throw new AssertionError(
            "expected [" + show(f.value()) + "] got " + show(got));
      }
    } finally {
      mc.execute(Fragment.of("DROP TABLE IF EXISTS " + table).execute());
    }
  }

  private static <T> void doArrayMulti(Connection mc, Fuzz<T> f) {
    PgType<List<T>> arr = f.type().array();
    String table = uniq("fuzz_arr_multi");
    String sqlType = arr.typename().sqlType();
    mc.execute(Fragment.of("CREATE TEMP TABLE " + table + " (v " + sqlType + ")").execute());
    try {
      // Mix the fuzz value with two siblings that have meaning to the array scanner — a string
      // that LOOKS like a NULL marker and one with embedded delimiters/braces — so the
      // quoting/escaping has to disambiguate this entry from its neighbours.
      @SuppressWarnings("unchecked")
      T sibling1 = (T) "NULL";
      @SuppressWarnings("unchecked")
      T sibling2 = (T) "{a,b};\"\\";
      // Only valid for text-typed values; if T isn't String, fall back to a singleton multi.
      List<T> values;
      if (f.type() == PgTypes.text || f.type() == PgTypes.name
          || f.type().typename().sqlType().startsWith("varchar")) {
        values = Arrays.asList(f.value(), sibling1, sibling2);
      } else {
        values = singletonList(f.value());
      }

      mc.execute(
          Fragment.of("INSERT INTO " + table + " (v) VALUES (")
              .append(Fragment.encode(arr, values))
              .append(")")
              .update());
      var rows =
          mc.execute(Fragment.of("SELECT v FROM " + table).query(RowCodec.of(arr).all()));
      if (rows.size() != 1) throw new AssertionError("expected 1 row, got " + rows.size());
      var got = rows.getFirst();
      if (got.size() != values.size()) {
        throw new AssertionError(
            "size " + values.size() + " expected, got " + got.size() + ": " + show(got));
      }
      for (int i = 0; i < values.size(); i++) {
        if (!valuesEqual(got.get(i), values.get(i))) {
          throw new AssertionError(
              "element " + i + " mismatch: expected " + show(values.get(i)) + " got " + show(got.get(i)));
        }
      }
    } finally {
      mc.execute(Fragment.of("DROP TABLE IF EXISTS " + table).execute());
    }
  }

  // ============================================================
  //  Domain roundtrip — text-typed values through asDomain wrapper
  // ============================================================

  @Test
  public void domainRoundtrip() {
    var failures = new ArrayList<String>();
    Containers.postgresTransactor()
        .transact(
            mc -> {
              mc.execute(Fragment.of("CREATE DOMAIN pgtt_fuzz_dom AS text").execute());
              PgType<String> dom = PgTypes.text.asDomain("pgtt_fuzz_dom");
              for (Fuzz<?> f : CASES) {
                if (f.type() != PgTypes.text) continue; // only text-typed values relevant for this domain
                @SuppressWarnings("unchecked")
                Fuzz<String> tf = (Fuzz<String>) f;
                try {
                  doDomainScalar(mc, tf, dom);
                } catch (Throwable t) {
                  failures.add(
                      "domain scalar " + tf.label() + " (" + show(tf.value()) + "): " + summarize(t));
                }
                try {
                  doDomainArray(mc, tf, dom);
                } catch (Throwable t) {
                  failures.add(
                      "domain array " + tf.label() + " (" + show(tf.value()) + "): " + summarize(t));
                }
              }
              return null;
            });
    if (!failures.isEmpty()) {
      throw new RuntimeException(
          failures.size() + " domain roundtrip failures:\n  " + String.join("\n  ", failures));
    }
  }

  private static void doDomainScalar(Connection mc, Fuzz<String> f, PgType<String> dom) {
    String table = uniq("fuzz_dom_scalar");
    mc.execute(Fragment.of("CREATE TEMP TABLE " + table + " (v pgtt_fuzz_dom)").execute());
    try {
      mc.execute(
          Fragment.of("INSERT INTO " + table + " (v) VALUES (")
              .append(Fragment.encode(dom, f.value()))
              .append(")")
              .update());
      var rows =
          mc.execute(Fragment.of("SELECT v FROM " + table).query(RowCodec.of(dom).all()));
      if (rows.size() != 1 || !valuesEqual(rows.getFirst(), f.value())) {
        throw new AssertionError(
            "expected " + show(f.value()) + " got " + show(rows.isEmpty() ? null : rows.getFirst()));
      }
    } finally {
      mc.execute(Fragment.of("DROP TABLE IF EXISTS " + table).execute());
    }
  }

  private static void doDomainArray(Connection mc, Fuzz<String> f, PgType<String> dom) {
    PgType<List<String>> arr = dom.array();
    String table = uniq("fuzz_dom_arr");
    mc.execute(Fragment.of("CREATE TEMP TABLE " + table + " (v pgtt_fuzz_dom[])").execute());
    try {
      List<String> values = Arrays.asList(f.value(), "NULL", "{a,b};\"\\");
      mc.execute(
          Fragment.of("INSERT INTO " + table + " (v) VALUES (")
              .append(Fragment.encode(arr, values))
              .append(")")
              .update());
      var rows =
          mc.execute(Fragment.of("SELECT v FROM " + table).query(RowCodec.of(arr).all()));
      if (rows.size() != 1) throw new AssertionError("expected 1 row, got " + rows.size());
      var got = rows.getFirst();
      if (got.size() != values.size()) {
        throw new AssertionError(
            "size " + values.size() + " expected, got " + got.size() + ": " + show(got));
      }
      for (int i = 0; i < values.size(); i++) {
        if (!valuesEqual(got.get(i), values.get(i))) {
          throw new AssertionError(
              "element " + i + " mismatch: expected " + show(values.get(i)) + " got " + show(got.get(i)));
        }
      }
    } finally {
      mc.execute(Fragment.of("DROP TABLE IF EXISTS " + table).execute());
    }
  }

  // ============================================================
  //  Nested structures — composites holding crazy strings
  // ============================================================

  /**
   * Composite-with-text-field roundtrip — exercises the {@link PgRecordParser#parse} record-text
   * pipeline (different from {@link PgRecordParser#parseArray}) for every adversarial string.
   * Composites encode each field through quoting + backslash-escaping that's a separate set of
   * rules from the array form, so a string that survives array tests can still break composites.
   */
  @Test
  public void compositeRoundtrip() {
    var failures = new ArrayList<String>();
    Containers.postgresTransactor()
        .transact(
            mc -> {
              mc.execute(Fragment.of("CREATE TYPE pgtt_fuzz_one AS (v text)").execute());
              PgType<OneField> oneType =
                  PgTypes.compositeOf(
                      "pgtt_fuzz_one",
                      RowCodec.<OneField>namedBuilder()
                          .field("v", PgTypes.text, OneField::v)
                          .build(OneField::new));
              for (Fuzz<?> f : CASES) {
                if (f.type() != PgTypes.text) continue;
                @SuppressWarnings("unchecked")
                Fuzz<String> tf = (Fuzz<String>) f;
                try {
                  doCompositeOne(mc, tf, oneType);
                } catch (Throwable t) {
                  failures.add(
                      "composite(text) " + tf.label() + " (" + show(tf.value()) + "): "
                          + summarize(t));
                }
              }
              return null;
            });
    if (!failures.isEmpty()) {
      throw new RuntimeException(
          failures.size() + " composite roundtrip failures:\n  " + String.join("\n  ", failures));
    }
  }

  /**
   * Two-field composite — the per-field quoting has to survive a sibling that ALSO contains
   * adversarial chars. Catches encoders that handle "string at start" and "string at end"
   * differently.
   */
  @Test
  public void compositeTwoFieldRoundtrip() {
    var failures = new ArrayList<String>();
    Containers.postgresTransactor()
        .transact(
            mc -> {
              mc.execute(Fragment.of("CREATE TYPE pgtt_fuzz_two AS (a text, b text)").execute());
              PgType<TwoField> twoType =
                  PgTypes.compositeOf(
                      "pgtt_fuzz_two",
                      RowCodec.<TwoField>namedBuilder()
                          .field("a", PgTypes.text, TwoField::a)
                          .field("b", PgTypes.text, TwoField::b)
                          .build(TwoField::new));
              for (Fuzz<?> f : CASES) {
                if (f.type() != PgTypes.text) continue;
                @SuppressWarnings("unchecked")
                Fuzz<String> tf = (Fuzz<String>) f;
                // sibling intentionally has braces, commas, quotes, backslashes
                String sibling = "{x,y};\"\\";
                try {
                  doCompositeTwo(mc, tf.label(), tf.value(), sibling, twoType);
                } catch (Throwable t) {
                  failures.add(
                      "composite(a,b) " + tf.label() + " a=" + show(tf.value()) + " b=" + show(sibling)
                          + ": " + summarize(t));
                }
              }
              return null;
            });
    if (!failures.isEmpty()) {
      throw new RuntimeException(
          failures.size() + " composite(two) roundtrip failures:\n  " + String.join("\n  ", failures));
    }
  }

  /**
   * Array of composites — composites are quoted-and-escaped as ARRAY ELEMENTS, then their fields
   * are quoted-and-escaped INSIDE the composite. Two layers of escape rules; either layer
   * misbehaving on a crazy string surfaces here.
   */
  @Test
  public void arrayOfCompositeRoundtrip() {
    var failures = new ArrayList<String>();
    Containers.postgresTransactor()
        .transact(
            mc -> {
              mc.execute(Fragment.of("CREATE TYPE pgtt_fuzz_one AS (v text)").execute());
              PgType<OneField> oneType =
                  PgTypes.compositeOf(
                      "pgtt_fuzz_one",
                      RowCodec.<OneField>namedBuilder()
                          .field("v", PgTypes.text, OneField::v)
                          .build(OneField::new));
              PgType<List<OneField>> arr = oneType.array();
              for (Fuzz<?> f : CASES) {
                if (f.type() != PgTypes.text) continue;
                @SuppressWarnings("unchecked")
                Fuzz<String> tf = (Fuzz<String>) f;
                try {
                  doArrayOfComposite(mc, tf, oneType, arr);
                } catch (Throwable t) {
                  failures.add(
                      "array<composite> " + tf.label() + " (" + show(tf.value()) + "): "
                          + summarize(t));
                }
              }
              return null;
            });
    if (!failures.isEmpty()) {
      throw new RuntimeException(
          failures.size() + " array<composite> roundtrip failures:\n  "
              + String.join("\n  ", failures));
    }
  }

  /**
   * Composite with an ARRAY field of crazy strings — inverse of the above. Field-quoting wraps
   * an already-quoted-and-escaped array literal; the composite-text decoder has to peel one
   * layer and the array-text decoder the next.
   */
  @Test
  public void compositeWithArrayFieldRoundtrip() {
    var failures = new ArrayList<String>();
    Containers.postgresTransactor()
        .transact(
            mc -> {
              mc.execute(Fragment.of("CREATE TYPE pgtt_fuzz_arr_field AS (xs text[])").execute());
              PgType<ArrField> t =
                  PgTypes.compositeOf(
                      "pgtt_fuzz_arr_field",
                      RowCodec.<ArrField>namedBuilder()
                          .field("xs", PgTypes.text.array(), ArrField::xs)
                          .build(ArrField::new));
              for (Fuzz<?> f : CASES) {
                if (f.type() != PgTypes.text) continue;
                @SuppressWarnings("unchecked")
                Fuzz<String> tf = (Fuzz<String>) f;
                try {
                  // 3 elements, all containing the fuzzed value mixed with sibling weirdness so
                  // the array-quoting and composite-quoting must compose correctly.
                  List<String> xs = Arrays.asList(tf.value(), "NULL", "{a,b};\"\\");
                  doCompositeWithArrayField(mc, tf.label(), xs, t);
                } catch (Throwable th) {
                  failures.add(
                      "composite(array field) " + tf.label() + " (" + show(tf.value()) + "): "
                          + summarize(th));
                }
              }
              return null;
            });
    if (!failures.isEmpty()) {
      throw new RuntimeException(
          failures.size() + " composite(array field) roundtrip failures:\n  "
              + String.join("\n  ", failures));
    }
  }

  public record OneField(String v) {}

  public record TwoField(String a, String b) {}

  public record ArrField(List<String> xs) {}

  private static void doCompositeOne(Connection mc, Fuzz<String> f, PgType<OneField> type) {
    String table = uniq("fuzz_comp_one");
    mc.execute(Fragment.of("CREATE TEMP TABLE " + table + " (v pgtt_fuzz_one)").execute());
    try {
      OneField original = new OneField(f.value());
      mc.execute(
          Fragment.of("INSERT INTO " + table + " (v) VALUES (")
              .append(Fragment.encode(type, original))
              .append(")")
              .update());
      var rows =
          mc.execute(Fragment.of("SELECT v FROM " + table).query(RowCodec.of(type).all()));
      if (rows.size() != 1 || !valuesEqual(rows.getFirst(), original)) {
        throw new AssertionError(
            "expected " + show(f.value()) + " got " + show(rows.isEmpty() ? null : rows.getFirst().v()));
      }
    } finally {
      mc.execute(Fragment.of("DROP TABLE IF EXISTS " + table).execute());
    }
  }

  private static void doCompositeTwo(
      Connection mc, String label, String a, String b, PgType<TwoField> type) {
    String table = uniq("fuzz_comp_two");
    mc.execute(Fragment.of("CREATE TEMP TABLE " + table + " (v pgtt_fuzz_two)").execute());
    try {
      TwoField original = new TwoField(a, b);
      mc.execute(
          Fragment.of("INSERT INTO " + table + " (v) VALUES (")
              .append(Fragment.encode(type, original))
              .append(")")
              .update());
      var rows =
          mc.execute(Fragment.of("SELECT v FROM " + table).query(RowCodec.of(type).all()));
      if (rows.size() != 1 || !valuesEqual(rows.getFirst(), original)) {
        throw new AssertionError(
            "expected (a=" + show(a) + ", b=" + show(b) + ") got "
                + show(rows.isEmpty() ? null : rows.getFirst()));
      }
    } finally {
      mc.execute(Fragment.of("DROP TABLE IF EXISTS " + table).execute());
    }
  }

  private static void doArrayOfComposite(
      Connection mc, Fuzz<String> f, PgType<OneField> oneType, PgType<List<OneField>> arr) {
    String table = uniq("fuzz_arr_comp");
    mc.execute(Fragment.of("CREATE TEMP TABLE " + table + " (v pgtt_fuzz_one[])").execute());
    try {
      List<OneField> original =
          Arrays.asList(
              new OneField(f.value()),
              new OneField("NULL"),
              new OneField("{a,b};\"\\"));
      mc.execute(
          Fragment.of("INSERT INTO " + table + " (v) VALUES (")
              .append(Fragment.encode(arr, original))
              .append(")")
              .update());
      var rows =
          mc.execute(Fragment.of("SELECT v FROM " + table).query(RowCodec.of(arr).all()));
      if (rows.size() != 1) throw new AssertionError("expected 1 row, got " + rows.size());
      var got = rows.getFirst();
      if (got.size() != original.size()) {
        throw new AssertionError(
            "size " + original.size() + " expected, got " + got.size() + ": " + show(got));
      }
      for (int i = 0; i < original.size(); i++) {
        if (!valuesEqual(got.get(i), original.get(i))) {
          throw new AssertionError(
              "element " + i + " mismatch: expected " + show(original.get(i).v()) + " got "
                  + show(got.get(i).v()));
        }
      }
    } finally {
      mc.execute(Fragment.of("DROP TABLE IF EXISTS " + table).execute());
    }
  }

  private static void doCompositeWithArrayField(
      Connection mc, String label, List<String> xs, PgType<ArrField> type) {
    String table = uniq("fuzz_comp_arr_field");
    mc.execute(Fragment.of("CREATE TEMP TABLE " + table + " (v pgtt_fuzz_arr_field)").execute());
    try {
      ArrField original = new ArrField(xs);
      mc.execute(
          Fragment.of("INSERT INTO " + table + " (v) VALUES (")
              .append(Fragment.encode(type, original))
              .append(")")
              .update());
      var rows =
          mc.execute(Fragment.of("SELECT v FROM " + table).query(RowCodec.of(type).all()));
      if (rows.size() != 1) throw new AssertionError("expected 1 row, got " + rows.size());
      var got = rows.getFirst();
      if (got.xs().size() != xs.size()) {
        throw new AssertionError(
            "size " + xs.size() + " expected, got " + got.xs().size() + ": " + show(got.xs()));
      }
      for (int i = 0; i < xs.size(); i++) {
        if (!valuesEqual(got.xs().get(i), xs.get(i))) {
          throw new AssertionError(
              "element " + i + " mismatch: expected " + show(xs.get(i)) + " got "
                  + show(got.xs().get(i)));
        }
      }
    } finally {
      mc.execute(Fragment.of("DROP TABLE IF EXISTS " + table).execute());
    }
  }

  // ============================================================
  //  Helpers
  // ============================================================

  private static String uniq(String prefix) {
    return prefix + "_" + tableCounter.incrementAndGet();
  }

  private static <T> List<T> singletonList(T value) {
    return Arrays.asList(value);
  }

  private static boolean valuesEqual(Object a, Object b) {
    if (a == null) return b == null;
    return a.equals(b);
  }

  private static String show(Object v) {
    if (v == null) return "<null>";
    if (v instanceof String s) {
      var sb = new StringBuilder("\"");
      for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (c < 0x20 || c == 0x7f) sb.append(String.format("\\u%04x", (int) c));
        else if (c == '"') sb.append("\\\"");
        else if (c == '\\') sb.append("\\\\");
        else sb.append(c);
      }
      return sb.append("\"").toString();
    }
    if (v instanceof List<?> list) {
      var sb = new StringBuilder("[");
      for (int i = 0; i < list.size(); i++) {
        if (i > 0) sb.append(", ");
        sb.append(show(list.get(i)));
      }
      return sb.append("]").toString();
    }
    return String.valueOf(v);
  }

  private static String summarize(Throwable t) {
    var sb = new StringBuilder(String.valueOf(t.getMessage()));
    Throwable c = t.getCause();
    while (c != null) {
      sb.append(" | caused by ").append(c.getClass().getSimpleName()).append(": ").append(c.getMessage());
      c = c.getCause();
    }
    return sb.toString();
  }
}
