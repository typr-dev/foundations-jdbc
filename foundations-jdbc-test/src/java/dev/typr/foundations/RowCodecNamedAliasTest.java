package dev.typr.foundations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import org.junit.Test;

/**
 * Pure unit tests for {@link RowCodecNamed#aliased} and the duplicate-detection behavior of
 * {@link RowCodecNamed#columnList()}. No database connection required — only inspects column
 * names and rendered SQL fragments.
 */
public class RowCodecNamedAliasTest {

  record Emp(int id, String name, String department) {}

  record Dept(int id, String name) {}

  static final RowCodecNamed<Emp> empCodec =
      RowCodec.<Emp>namedBuilder()
          .field("id", DuckDbTypes.integer, Emp::id)
          .field("name", DuckDbTypes.varchar, Emp::name)
          .field("department", DuckDbTypes.varchar, Emp::department)
          .build(Emp::new);

  static final RowCodecNamed<Dept> deptCodec =
      RowCodec.<Dept>namedBuilder()
          .field("id", DuckDbTypes.integer, Dept::id)
          .field("name", DuckDbTypes.varchar, Dept::name)
          .build(Dept::new);

  @Test
  public void aliasedPrefixesEveryColumnName() {
    var aliased = empCodec.aliased("e");
    assertEquals(List.of("e.id", "e.name", "e.department"), aliased.columnNames());
  }

  @Test
  public void aliasedPreservesColumnCountAndTypes() {
    var aliased = empCodec.aliased("e");
    assertEquals(empCodec.columns().size(), aliased.columns().size());
    for (int i = 0; i < empCodec.columns().size(); i++) {
      assertEquals(empCodec.columns().get(i), aliased.columns().get(i));
    }
  }

  @Test
  public void columnListOnAliasedCodecEmitsQualifiedNames() {
    assertEquals("e.id, e.name, e.department", empCodec.aliased("e").columnList().render());
  }

  @Test
  public void columnListOnUndupedJoinEmitsBothSidesConcatenated() {
    // A join with no collisions — keep the current unaliased behavior.
    RowCodecNamed<Dept> deptRenamed =
        RowCodec.<Dept>namedBuilder()
            .field("dept_id", DuckDbTypes.integer, Dept::id)
            .field("dept_name", DuckDbTypes.varchar, Dept::name)
            .build(Dept::new);
    var joined = empCodec.join(deptRenamed);
    assertEquals("id, name, department, dept_id, dept_name", joined.columnList().render());
  }

  @Test
  public void columnListOnDuplicateJoinThrowsWithDuplicateNames() {
    // empCodec and deptCodec share "id" and "name" — calling columnList() without
    // aliasing first should throw with an actionable message.
    var joined = empCodec.leftJoinedNamed(deptCodec);
    try {
      joined.columnList();
      fail("expected IllegalStateException on duplicate column names");
    } catch (IllegalStateException e) {
      var message = e.getMessage();
      assertTrue("message mentions duplicates: " + message, message.contains("[id, name]"));
      assertTrue("message mentions .aliased: " + message, message.contains("aliased"));
    }
  }

  @Test
  public void aliasBeforeJoinResolvesDuplicatesAndRendersQualifiedSql() {
    var joined = empCodec.aliased("e").leftJoinedNamed(deptCodec.aliased("d"));
    assertEquals(
        List.of("e.id", "e.name", "e.department", "d.id", "d.name"), joined.columnNames());
    assertEquals(
        "e.id, e.name, e.department, d.id, d.name", joined.columnList().render());
  }

  @Test
  public void aliasedAliasedChainsPrefixes() {
    // Defensive: calling .aliased twice is weird but should be deterministic rather than
    // silently replacing. Prefixes stack, so a user can see what they did.
    var twice = empCodec.aliased("a").aliased("b");
    assertEquals(List.of("b.a.id", "b.a.name", "b.a.department"), twice.columnNames());
  }
}
