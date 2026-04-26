package dev.typr.foundations;

import static org.junit.Assert.*;

import java.util.Optional;
import org.junit.Test;

public class PgErrorTest {

  @Test
  public void formattedIncludesAllFields() {
    var err =
        new PgError(
            "ERROR",
            "duplicate key value violates unique constraint \"users_pkey\"",
            "23505",
            Optional.of("Key (id)=(1) already exists."),
            Optional.of("Consider using ON CONFLICT."),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of("public"),
            Optional.of("users"),
            Optional.of("id"),
            Optional.of("integer"),
            Optional.of("users_pkey"),
            Optional.of("nbtinsert.c"),
            Optional.of(570),
            Optional.of("_bt_check_unique"));

    String formatted = err.formatted();
    assertTrue(formatted.contains("ERROR: duplicate key value violates unique constraint"));
    assertTrue(formatted.contains("[23505]"));
    assertTrue(formatted.contains("Detail: Key (id)=(1) already exists."));
    assertTrue(formatted.contains("Hint: Consider using ON CONFLICT."));
    assertTrue(formatted.contains("Schema: public"));
    assertTrue(formatted.contains("Table: users"));
    assertTrue(formatted.contains("Column: id"));
    assertTrue(formatted.contains("Type: integer"));
    assertTrue(formatted.contains("Constraint: users_pkey"));
  }

  @Test
  public void formattedOmitsNullFields() {
    var err =
        new PgError(
            "ERROR",
            "syntax error at or near \"SELEC\"",
            "42601",
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String formatted = err.formatted();
    assertTrue(formatted.contains("ERROR: syntax error"));
    assertTrue(formatted.contains("[42601]"));
    assertFalse(formatted.contains("Detail:"));
    assertFalse(formatted.contains("Schema:"));
    assertFalse(formatted.contains("Table:"));
  }

  @Test
  public void caretPointsToCorrectPosition() {
    var err =
        new PgError(
            "ERROR",
            "syntax error at or near \"SELEC\"",
            "42601",
            Optional.empty(),
            Optional.empty(),
            Optional.of(1),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String formatted = err.formatted(Optional.of("SELEC id FROM users WHERE age > 30"));
    assertTrue(formatted.contains("SELEC id FROM users WHERE age > 30"));
    assertTrue(formatted.contains("└──"));

    String[] lines = formatted.split("\n");
    String sqlLine = null;
    String caretLine = null;
    for (int i = 0; i < lines.length - 1; i++) {
      if (lines[i].contains("SELEC id")) {
        sqlLine = lines[i];
        caretLine = lines[i + 1];
        break;
      }
    }
    assertNotNull(sqlLine);
    assertNotNull(caretLine);
    int caretPos = caretLine.indexOf('└');
    int sPos = sqlLine.indexOf('S');
    assertEquals(sPos, caretPos);
  }

  @Test
  public void caretAtMiddleOfStatement() {
    var err =
        new PgError(
            "ERROR",
            "column \"agee\" does not exist",
            "42703",
            Optional.empty(),
            Optional.of("Perhaps you meant to reference the column \"age\"."),
            Optional.of(32),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String sql = "SELECT id FROM users WHERE agee > 30";
    String formatted = err.formatted(Optional.of(sql));

    assertTrue(formatted.contains("└──"));
    assertTrue(formatted.contains("Hint: Perhaps you meant"));

    String[] lines = formatted.split("\n");
    for (int i = 0; i < lines.length - 1; i++) {
      if (lines[i].contains("SELECT id")) {
        String caretLine = lines[i + 1];
        int caretPos = caretLine.indexOf('└') - 2;
        assertEquals(31, caretPos);
        break;
      }
    }
  }

  @Test
  public void caretWithLeadingWhitespace() {
    var err =
        new PgError(
            "ERROR",
            "syntax error",
            "42601",
            Optional.empty(),
            Optional.empty(),
            Optional.of(5),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String sql = "    SELEC id FROM users";
    String formatted = err.formatted(Optional.of(sql));
    assertTrue(formatted.contains("SELEC id FROM users"));
    assertTrue(formatted.contains("└──"));
  }

  @Test
  public void internalQueryWithPosition() {
    var err =
        new PgError(
            "ERROR",
            "division by zero",
            "22012",
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.of("PL/pgSQL function my_func() line 3 at assignment"),
            Optional.of(5),
            Optional.of("SELECT 1/0"),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String formatted = err.formatted();
    assertTrue(formatted.contains("Where: PL/pgSQL function"));
    assertTrue(formatted.contains("Internal query:"));
    assertTrue(formatted.contains("SELECT 1/0"));
  }

  @Test
  public void testCaretEmptySql() {
    var err =
        new PgError(
            "ERROR",
            "syntax error at end of input",
            "42601",
            Optional.empty(),
            Optional.empty(),
            Optional.of(1),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String formatted = err.formatted(Optional.of(""));
    assertTrue(formatted.contains("ERROR: syntax error at end of input"));
    assertFalse(formatted.contains("└──"));
  }

  @Test
  public void testCaretSingleChar() {
    var err =
        new PgError(
            "ERROR",
            "syntax error",
            "42601",
            Optional.empty(),
            Optional.empty(),
            Optional.of(1),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String formatted = err.formatted(Optional.of("X"));
    assertTrue(formatted.contains("X"));
    assertTrue(formatted.contains("└──"));

    String[] lines = formatted.split("\n");
    for (int i = 0; i < lines.length - 1; i++) {
      if (lines[i].stripLeading().startsWith("X")) {
        String caretLine = lines[i + 1];
        int caretPos = caretLine.indexOf('└');
        int xPos = lines[i].indexOf('X');
        assertEquals(xPos, caretPos);
        break;
      }
    }
  }

  @Test
  public void testCaretPositionBeyondSql() {
    var err =
        new PgError(
            "ERROR",
            "syntax error",
            "42601",
            Optional.empty(),
            Optional.empty(),
            Optional.of(100),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String formatted = err.formatted(Optional.of("SELECT 1+2"));
    assertTrue(formatted.contains("SELECT 1+2"));
    assertFalse(formatted.contains("└──"));
  }

  @Test
  public void testCaretMultiLineSql() {
    var err =
        new PgError(
            "ERROR",
            "column \"baz\" does not exist",
            "42703",
            Optional.empty(),
            Optional.empty(),
            Optional.of(25),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String sql = "SELECT foo, bar,\n        baz FROM t";
    String formatted = err.formatted(Optional.of(sql));
    assertTrue(formatted.contains("└──"));
    assertTrue(formatted.contains("baz FROM t"));
  }

  @Test
  public void testEmptySeverity() {
    var err =
        new PgError(
            "",
            "some message",
            "42601",
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty());

    String formatted = err.formatted();
    assertTrue(formatted.contains("some message"));
    assertTrue(formatted.contains("[42601]"));
  }

  @Test
  public void testEmptyMessage() {
    var err =
        new PgError(
            "ERROR", "", "42601", Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty());

    String formatted = err.formatted();
    assertTrue(formatted.contains("ERROR"));
    assertTrue(formatted.contains("[42601]"));
  }
}
