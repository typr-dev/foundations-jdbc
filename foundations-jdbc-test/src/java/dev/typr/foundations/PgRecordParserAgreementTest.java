package dev.typr.foundations;

import java.util.List;
import org.junit.Test;

/**
 * Cross-checks {@link PgRecordParser#parseArray} against a live PostgreSQL — for every input in
 * the shared {@link PgArrayParseCases} corpus, ask PG to parse the same string and compare.
 * PG's {@code text[]} (and {@code box[]} for the geometric case) is the reference; if our parser
 * disagrees on the number, identity, or contents of elements, the parser is wrong.
 *
 * <p>Same strings as {@link PgRecordParserTest} — running the unit and agreement tests in
 * lockstep means a new test case is verified twice (parser self-consistent, parser matches PG)
 * with one entry in the corpus.
 *
 * <p>Single {@code @Test} method by design: a fresh JVM has to stand up the testcontainers PG
 * before any case runs; splitting into per-phase methods would cold-start the suite-idle
 * timeout on each one.
 */
public class PgRecordParserAgreementTest {

  @Test
  public void parseArrayAgreesWithPostgres() {
    Containers.postgresTransactor()
        .transact(
            mc -> {
              System.out.println("[agreement] one-dim cases (" + PgArrayParseCases.ONE_DIM.size() + ")");
              checkOneDim(mc);
              System.out.println("[agreement] bare-nested cases (" + PgArrayParseCases.BARE_NESTED.size() + ")");
              checkMultiDim(mc);
              System.out.println("[agreement] semicolon-delim cases (" + PgArrayParseCases.SEMI_DELIM.size() + ")");
              checkBoxDelim(mc);
              return null;
            });
  }

  private static void checkOneDim(Connection mc) {
    for (var c : PgArrayParseCases.ONE_DIM) {
      if (!c.pgVerify()) continue;
      List<String> ours = PgRecordParser.parseArray(c.input(), c.delimiter());
      List<String> pg =
          mc.execute(
                  Fragment.of("SELECT unnest(")
                      .value(PgTypes.text, c.input())
                      .append("::" + c.castSqlType() + ")")
                      .query(RowCodec.of(PgTypes.text.opt()).all()))
              .stream()
              .map(o -> o.orElse(null))
              .toList();
      if (!equal(pg, ours)) {
        throw new AssertionError(
            "PG and parser disagree on "
                + c.input()
                + " (cast "
                + c.castSqlType()
                + "):\n  parser: "
                + ours
                + "\n  pg:     "
                + pg);
      }
    }
  }

  private static void checkMultiDim(Connection mc) {
    for (var c : PgArrayParseCases.BARE_NESTED) {
      if (!c.pgVerify()) continue;
      List<Integer> shape = uniformBareNestedShape(c.input());
      if (shape == null) {
        System.out.println("  [skip non-uniform] " + c.input());
        continue;
      }
      String expected = shape.stream().map(n -> "[1:" + n + "]").reduce("", String::concat);
      System.out.println("  [check uniform=" + expected + "] " + c.input());
      String dims;
      try {
        dims =
            mc.execute(
                    Fragment.of("SELECT array_dims(")
                        .value(PgTypes.text, c.input())
                        .append("::" + c.castSqlType() + ")")
                        .query(RowCodec.of(PgTypes.text.opt()).all()))
                .getFirst()
                .orElse(null);
      } catch (RuntimeException e) {
        throw new RuntimeException(
            "PG rejected " + c.input() + " as " + c.castSqlType() + ": " + e.getMessage(), e);
      }
      if (!expected.equals(dims)) {
        throw new RuntimeException(
            "PG sees different dims for "
                + c.input()
                + ":\n  parser shape="
                + shape
                + " (expected "
                + expected
                + ")\n  pg dims="
                + dims);
      }
    }
  }

  private static void checkBoxDelim(Connection mc) {
    for (var c : PgArrayParseCases.SEMI_DELIM) {
      if (!c.pgVerify()) continue;
      int parsed = PgRecordParser.parseArray(c.input(), c.delimiter()).size();
      int pgLen =
          mc.execute(
                  Fragment.of("SELECT array_length(")
                      .value(PgTypes.text, c.input())
                      .append("::" + c.castSqlType() + ", 1)")
                      .query(RowCodec.of(PgTypes.int4).all()))
              .getFirst();
      if (parsed != pgLen) {
        throw new AssertionError(
            "PG and parser disagree on "
                + c.input()
                + " (cast "
                + c.castSqlType()
                + "): parser="
                + parsed
                + " pg="
                + pgLen);
      }
    }
  }

  /**
   * Recursively walk an array literal, returning the length at each dimension if the structure is
   * a uniform N-dim rectangle of bare-nested sub-arrays (PG's text[] rectangularity rule), or
   * {@code null} if any level is jagged / mixes bare-nested with scalar siblings / has empty
   * sub-arrays. Only the uniform shape can be cast to text[] for PG's {@code array_dims} to
   * agree, so non-uniform cases are skipped against PG.
   */
  private static List<Integer> uniformBareNestedShape(String input) {
    var dims = new java.util.ArrayList<Integer>();
    String cur = input.trim();
    while (true) {
      List<String> elems = PgRecordParser.parseArray(cur);
      if (elems.isEmpty()) return null;
      dims.add(elems.size());
      String first = elems.get(0);
      if (first == null || !first.startsWith("{") || !first.endsWith("}")) {
        for (String s : elems) {
          if (s != null && (s.startsWith("{") || s.endsWith("}"))) return null;
        }
        return dims;
      }
      int firstLen = PgRecordParser.parseArray(first).size();
      for (String s : elems) {
        if (s == null || !s.startsWith("{") || !s.endsWith("}")) return null;
        if (PgRecordParser.parseArray(s).size() != firstLen) return null;
      }
      cur = first;
    }
  }

  private static boolean equal(List<?> a, List<?> b) {
    if (a.size() != b.size()) return false;
    for (int i = 0; i < a.size(); i++) {
      Object ai = a.get(i);
      Object bi = b.get(i);
      if (ai == null && bi == null) continue;
      if (ai == null || bi == null) return false;
      if (!ai.equals(bi)) return false;
    }
    return true;
  }
}
