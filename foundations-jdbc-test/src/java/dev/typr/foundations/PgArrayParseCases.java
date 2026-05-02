package dev.typr.foundations;

import java.util.Arrays;
import java.util.List;

/**
 * Shared test inputs for {@link PgRecordParser#parseArray} — consumed by both the pure-unit
 * {@link PgRecordParserTest} (parser self-consistency) and {@link PgRecordParserAgreementTest}
 * (parser-vs-live-PG cross-check). Single source of truth so both paths exercise exactly the
 * same strings.
 */
public final class PgArrayParseCases {

  /**
   * One parseArray expectation. The PG textual array form is type-dependent (delimiter, quoting,
   * how PG canonicalises elements) — so each case names the {@code castSqlType} PG should
   * interpret it as. The parser still operates purely textually with {@code delimiter}; PG runs
   * {@code SELECT $input::castSqlType} and we cross-check.
   *
   * <p>{@code pgVerify=false} skips the PG cross-check for cases PG can't accept under any
   * sensible array cast (e.g. jsonb-leaf bare-nested confuses PG's text[] rectangularity check).
   */
  public record Case(
      String input,
      char delimiter,
      String castSqlType,
      List<String> expected,
      boolean pgVerify) {}

  private static Case textArr(String input, List<String> expected) {
    return new Case(input, ',', "text[]", expected, true);
  }

  private static Case boxArr(String input, List<String> expected) {
    return new Case(input, ';', "box[]", expected, true);
  }

  /** Parser-only — PG rejects this literal under any sensible array cast. */
  private static Case parserOnly(String input, List<String> expected) {
    return new Case(input, ',', "text[]", expected, false);
  }

  /** 1-D, comma-delimited cases — the bulk of real-world array text PG emits as text[]. */
  public static final List<Case> ONE_DIM =
      List.of(
          textArr("{}", List.of()),
          textArr("{1,2,3}", List.of("1", "2", "3")),
          textArr("{a,b,c}", List.of("a", "b", "c")),
          textArr("{42}", List.of("42")),
          textArr("{\"hello\"}", List.of("hello")),
          textArr("{NULL}", Arrays.asList((String) null)),
          textArr("{a,NULL,c}", Arrays.asList("a", null, "c")),
          textArr("{NULL,NULL}", Arrays.asList(null, null)),
          textArr("{\"a,b\",c}", List.of("a,b", "c")),
          textArr("{\"hello, world\",\"foo, bar\"}", List.of("hello, world", "foo, bar")),
          textArr("{\"{not nested}\"}", List.of("{not nested}")),
          textArr("{\"{a,b}\",c}", List.of("{a,b}", "c")),
          textArr("{\"{\\\"k\\\": 1}\"}", List.of("{\"k\": 1}")),
          textArr(
              "{\"{\\\"a\\\": 1}\",\"{\\\"b\\\": 2}\"}", List.of("{\"a\": 1}", "{\"b\": 2}")),
          textArr("{\"[1,10)\"}", List.of("[1,10)")),
          textArr("{\"[1,10)\",\"[20,30)\"}", List.of("[1,10)", "[20,30)")),
          textArr("  {1,2,3}  ", List.of("1", "2", "3")));

  /**
   * Bare-nested multi-dim cases — at the top level we expect each {@code {…}} sub-array as a
   * single element (re-parse to descend). PG sees these as rectangular N-dim arrays under the
   * text[] cast.
   */
  public static final List<Case> BARE_NESTED =
      List.of(
          textArr("{{1,2},{3,4}}", List.of("{1,2}", "{3,4}")),
          textArr("{{a,b,c}}", List.of("{a,b,c}")),
          textArr("{{1}}", List.of("{1}")),
          textArr("{{a,b},c,d}", List.of("{a,b}", "c", "d")),
          textArr("{a,{b,c},d}", List.of("a", "{b,c}", "d")),
          textArr("{a,b,{c,d}}", List.of("a", "b", "{c,d}")),
          textArr(
              "{{{1,2},{3,4}},{{5,6},{7,8}}}",
              List.of("{{1,2},{3,4}}", "{{5,6},{7,8}}")),
          textArr("{{{a}}}", List.of("{{a}}")),
          textArr("{{\"a,b\",c},{d}}", List.of("{\"a,b\",c}", "{d}")),
          textArr("{{\"{not array}\",x},{y}}", List.of("{\"{not array}\",x}", "{y}")),
          // jsonb[][] real-world shape — parser must handle it, but PG rejects this literal as
          // text[] because the inner {"k":...} confuses its rectangularity check.
          parserOnly(
              "{{\"{\\\"k\\\": 1}\"},{\"{\\\"k\\\": 2}\"}}",
              List.of("{\"{\\\"k\\\": 1}\"}", "{\"{\\\"k\\\": 2}\"}")),
          textArr("{{},{}}", List.of("{}", "{}")),
          textArr("{{},{a}}", List.of("{}", "{a}")),
          textArr("{{a,b},NULL,{c,d}}", Arrays.asList("{a,b}", null, "{c,d}")),
          textArr("{{\"a\\\"b\",c}}", List.of("{\"a\\\"b\",c}")));

  /** ';' delimiter cases — geometric arrays (box, etc.) where PG's typdelim is ';'. */
  public static final List<Case> SEMI_DELIM =
      List.of(
          boxArr("{(1,2),(3,4)}", List.of("(1,2),(3,4)")),
          boxArr("{(1,2),(3,4);(5,6),(7,8)}", List.of("(1,2),(3,4)", "(5,6),(7,8)")));

  private PgArrayParseCases() {}
}
