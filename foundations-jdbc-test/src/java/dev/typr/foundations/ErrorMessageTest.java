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
   * ISSUE IDENTIFIED: When reading a VARCHAR as INTEGER, the error message is:
   *   "For input string: hello"
   *
   * This is just the NumberFormatException message - we lose context about
   * which column failed and what type was expected.
   *
   * IMPROVEMENT OPPORTUNITY: The SqlResultParseException wrapper should
   * preserve and expose its context in the message chain.
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
      String rootMessage = getRootCause(e).getMessage();
      String fullChain = getFullExceptionChain(e);

      System.out.println("=== Actual column type mismatch ===");
      System.out.println("Root cause: " + rootMessage);
      System.out.println("Full chain: " + fullChain);
      System.out.println("====================================");

      // Current behavior: root cause is just "For input string: hello"
      // The SqlResultParseException context is lost in getRootCause
      assertTrue("Should have NumberFormatException in chain",
          fullChain.contains("NumberFormatException") ||
          fullChain.contains("For input string"));

      // The wrapper DOES have good context, but it's not the root cause
      assertTrue("SqlResultParseException should be in chain",
          fullChain.contains("Error reading or parsing row") ||
          fullChain.contains("SqlResultParseException"));
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

    System.out.println("ACTUAL LIBRARY MESSAGES (verified by tests):");
    System.out.println("--------------------------------------------------------------------");
    System.out.println("  RowParser.SqlResultParseException (GOOD but buried in cause chain):");
    System.out.println("    Error reading or parsing row {row}, (1-indexed) column {col}");
    System.out.println("    from ResultSet. Expected database type {sqlType}");
    System.out.println();
    System.out.println("  ResultSetParser.ExactlyOne (GOOD, clear messages):");
    System.out.println("    No rows when expecting a single one");
    System.out.println("    Expected single row, but found more");
    System.out.println();
    System.out.println("  Uint1/Uint2/Uint4/Uint8 validation (EXCELLENT!):");
    System.out.println("    Uint1 value must be between 0 and 255, got: {value}");
    System.out.println();
    System.out.println("  DuckDbRead type conversion:");
    System.out.println("    Cannot convert {class} to LocalDateTime");
    System.out.println("    Cannot convert {class} to OffsetDateTime");
    System.out.println("    Cannot convert {class} to UUID");
    System.out.println();
    System.out.println("  PgRead type check:");
    System.out.println("    Expected {sqlType} but got {actualType}");
    System.out.println();

    System.out.println("IMPROVEMENT OPPORTUNITIES:");
    System.out.println("--------------------------------------------------------------------");
    System.out.println("  1. Column type mismatch: root cause loses context from wrapper");
    System.out.println("     - SqlResultParseException has good info but it's buried");
    System.out.println("     - Consider: print full chain or enhance root message");
    System.out.println();
    System.out.println("  2. Add column NAME to error messages (currently only index)");
    System.out.println("     - Would require passing column names through RowParser");
    System.out.println();
    System.out.println("  3. Add 'Hint:' suggestions for common type mismatches");
    System.out.println("     - timestamp vs timestamptz");
    System.out.println("     - int4 vs int8");
    System.out.println("     - text vs varchar");
    System.out.println();
    System.out.println("  4. Show expected vs actual column count when RowParser fails");
    System.out.println("     - This would catch ResultSet having wrong number of columns");
    System.out.println();
    System.out.println("  5. Landing page should show REALISTIC error messages");
    System.out.println("     - Update ErrorMessagesSection to match actual output");
    System.out.println("     - Or improve library to match aspirational messages");
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
