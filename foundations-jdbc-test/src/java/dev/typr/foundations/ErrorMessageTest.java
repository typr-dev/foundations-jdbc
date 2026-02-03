package dev.typr.foundations;

import static org.junit.Assert.*;

import dev.typr.foundations.connect.duckdb.DuckDbConfig;
import org.junit.Test;

/**
 * Tests for error messages produced by the library.
 *
 * <p>These tests verify that error messages are helpful and match what's
 * documented on the landing page. If a test fails, either:
 * <ol>
 *   <li>Fix the error message in the library code, or
 *   <li>Update the landing page to match actual behavior
 * </ol>
 *
 * <p>Landing page error messages are in: site/src/pages/index.js (ErrorMessagesSection)
 */
public class ErrorMessageTest {

  // ==========================================================================
  // Row Parsing Errors
  // ==========================================================================

  /**
   * Verifies the Query Analysis-aligned error format for column parse errors.
   *
   * <p>Format matches Query Analysis style:
   * <pre>
   * Column 2: parse error
   *    │ Expected type: INTEGER
   *    │ Row: 0
   *    └ NumberFormatException: For input string: "hello"
   * </pre>
   */
  @Test
  public void testColumnTypeMismatch_messageFormat() {
    var tx = DuckDbConfig.builder(":memory:").build().transactor();

    try {
      tx.execute(conn -> {
        Fragment.lit("CREATE TABLE test_err (id INTEGER, name VARCHAR)")
            .update()
            .run(conn);
        Fragment.lit("INSERT INTO test_err VALUES (1, 'hello')")
            .update()
            .run(conn);

        // Try to read the VARCHAR column as INTEGER - should fail
        return Fragment.lit("SELECT id, name FROM test_err")
            .query(RowParser.<Integer>builder()
                .field(DuckDbTypes.integer, x -> x)
                .field(DuckDbTypes.integer, x -> x)  // Wrong type for 'name'!
                .build((id, name) -> id)
                .all())
            .run(conn);
      });
      fail("Expected SQLException for type mismatch");
    } catch (Exception e) {
      String fullChain = getFullExceptionChain(e);

      System.out.println("=== Actual column type mismatch ===");
      System.out.println(fullChain);
      System.out.println("====================================");

      // Verify Query Analysis-aligned format
      assertTrue("Should have 'Column X: parse error' header",
          fullChain.contains("Column") && fullChain.contains("parse error"));
      assertTrue("Should have box-drawing characters for structure",
          fullChain.contains("│") && fullChain.contains("└"));
      assertTrue("Should show expected type",
          fullChain.contains("Expected type:"));
      assertTrue("Should show row number",
          fullChain.contains("Row:"));
      assertTrue("Should include cause exception type and message",
          fullChain.contains("NumberFormatException") &&
          fullChain.contains("For input string"));
    }
  }

  @Test
  public void testExactlyOne_noRows_messageFormat() {
    var tx = DuckDbConfig.builder(":memory:").build().transactor();

    try {
      tx.execute(conn -> {
        Fragment.lit("CREATE TABLE empty_table (id INTEGER)")
            .update()
            .run(conn);

        return Fragment.lit("SELECT id FROM empty_table")
            .query(RowParser.of(DuckDbTypes.integer).exactlyOne())
            .run(conn);
      });
      fail("Expected SQLException for no rows");
    } catch (Exception e) {
      String message = getRootCause(e).getMessage();

      System.out.println("=== Actual 'no rows' message ===");
      System.out.println(message);
      System.out.println("=================================");

      // This is a good, clear message
      assertEquals("No rows when expecting a single one", message);
    }
  }

  @Test
  public void testExactlyOne_tooManyRows_messageFormat() {
    var tx = DuckDbConfig.builder(":memory:").build().transactor();

    try {
      tx.execute(conn -> {
        Fragment.lit("CREATE TABLE multi_table (id INTEGER)")
            .update()
            .run(conn);
        Fragment.lit("INSERT INTO multi_table VALUES (1), (2)")
            .update()
            .run(conn);

        return Fragment.lit("SELECT id FROM multi_table")
            .query(RowParser.of(DuckDbTypes.integer).exactlyOne())
            .run(conn);
      });
      fail("Expected SQLException for too many rows");
    } catch (Exception e) {
      String message = getRootCause(e).getMessage();

      System.out.println("=== Actual 'too many rows' message ===");
      System.out.println(message);
      System.out.println("=======================================");

      // This is a good, clear message
      assertEquals("Expected single row, but found more", message);
    }
  }

  @Test
  public void testMaxOne_tooManyRows_messageFormat() {
    var tx = DuckDbConfig.builder(":memory:").build().transactor();

    try {
      tx.execute(conn -> {
        Fragment.lit("CREATE TABLE multi_table2 (id INTEGER)")
            .update()
            .run(conn);
        Fragment.lit("INSERT INTO multi_table2 VALUES (1), (2)")
            .update()
            .run(conn);

        return Fragment.lit("SELECT id FROM multi_table2")
            .query(RowParser.of(DuckDbTypes.integer).maxOne())
            .run(conn);
      });
      fail("Expected SQLException for too many rows");
    } catch (Exception e) {
      String message = getRootCause(e).getMessage();

      System.out.println("=== Actual 'maxOne too many rows' message ===");
      System.out.println(message);
      System.out.println("==============================================");

      assertEquals("Expected single row, but found more", message);
    }
  }

  // ==========================================================================
  // Input Validation Errors - These are GOOD examples!
  // ==========================================================================

  @Test
  public void testUnsignedIntOverflow_messageFormat() {
    try {
      new dev.typr.foundations.data.Uint1((short) 256);  // Max is 255
      fail("Expected IllegalArgumentException for overflow");
    } catch (IllegalArgumentException e) {
      System.out.println("=== Actual unsigned int overflow message ===");
      System.out.println(e.getMessage());
      System.out.println("=============================================");

      // This is a GREAT error message - includes the actual value!
      assertEquals("Uint1 value must be between 0 and 255, got: 256", e.getMessage());
    }
  }

  @Test
  public void testUnsignedIntNegative_messageFormat() {
    try {
      new dev.typr.foundations.data.Uint1((short) -1);
      fail("Expected IllegalArgumentException for negative value");
    } catch (IllegalArgumentException e) {
      System.out.println("=== Actual unsigned int negative message ===");
      System.out.println(e.getMessage());
      System.out.println("=============================================");

      // This is a GREAT error message - includes the actual value!
      assertEquals("Uint1 value must be between 0 and 255, got: -1", e.getMessage());
    }
  }

  // ==========================================================================
  // Summary: Documented vs Actual Error Messages
  // ==========================================================================

  /**
   * This test documents the gap between landing page claims and reality.
   * Run this test to see what messages the library actually produces.
   */
  @Test
  public void documentErrorMessageGap() {
    System.out.println("\n");
    System.out.println("╔══════════════════════════════════════════════════════════════════╗");
    System.out.println("║  Error Message Documentation                                      ║");
    System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    System.out.println();

    System.out.println("LANDING PAGE CLAIMS (site/src/pages/index.js ErrorMessagesSection):");
    System.out.println("--------------------------------------------------------------------");
    System.out.println("  Column type mismatch at index 3 (name):");
    System.out.println("    Expected: varchar (PgTypes.text)");
    System.out.println("    Actual: integer");
    System.out.println();
    System.out.println("  Row parsing failed:");
    System.out.println("    Expected 5 columns, got 4");
    System.out.println("    Missing column at index 4");
    System.out.println();
    System.out.println("  Type conversion error:");
    System.out.println("    Cannot read column 'created_at' as OffsetDateTime");
    System.out.println("    Database type: timestamp without time zone");
    System.out.println("    Hint: Use PgTypes.timestamp instead of PgTypes.timestamptz");
    System.out.println();

    System.out.println("ACTUAL LIBRARY MESSAGES (Query Analysis-aligned format):");
    System.out.println("--------------------------------------------------------------------");
    System.out.println("  RowParser.SqlResultParseException:");
    System.out.println("    Column 3: parse error");
    System.out.println("       │ Expected type: timestamptz");
    System.out.println("       │ Row: 0");
    System.out.println("       └ SQLException: Cannot convert Timestamp to OffsetDateTime");
    System.out.println();
    System.out.println("  ResultSetParser.ExactlyOne:");
    System.out.println("    No rows when expecting a single one");
    System.out.println("    Expected single row, but found more");
    System.out.println();
    System.out.println("  Uint1/Uint2/Uint4/Uint8 validation:");
    System.out.println("    Uint1 value must be between 0 and 255, got: 256");
    System.out.println();

    System.out.println("REMAINING IMPROVEMENT OPPORTUNITIES:");
    System.out.println("--------------------------------------------------------------------");
    System.out.println("  1. Add column NAME to error messages (currently only index)");
    System.out.println("     - Would require passing column names through RowParser");
    System.out.println();
    System.out.println("  2. Add 'Hint:' suggestions for common type mismatches");
    System.out.println("     - timestamp vs timestamptz");
    System.out.println("     - int4 vs int8");
    System.out.println();
    System.out.println("  3. Show expected vs actual column count when RowParser fails");
    System.out.println("     - This would catch ResultSet having wrong number of columns");
    System.out.println();
  }

  // ==========================================================================
  // Helper Methods
  // ==========================================================================

  private Throwable getRootCause(Throwable t) {
    while (t.getCause() != null && t.getCause() != t) {
      t = t.getCause();
    }
    return t;
  }

  private String getFullExceptionChain(Throwable t) {
    StringBuilder sb = new StringBuilder();
    while (t != null) {
      sb.append(t.getClass().getSimpleName()).append(": ").append(t.getMessage());
      t = t.getCause();
      if (t != null) {
        sb.append("\n  Caused by: ");
      }
    }
    return sb.toString();
  }
}
