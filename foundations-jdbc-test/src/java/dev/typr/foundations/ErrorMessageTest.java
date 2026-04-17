package dev.typr.foundations;

import static org.junit.Assert.*;

import dev.typr.foundations.connect.DuckDbConfig;
import org.junit.Test;

/**
 * Tests for error messages produced by the library.
 *
 * <p>These tests verify that error messages are helpful and match what's documented on the landing
 * page. If a test fails, either:
 *
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
   *
   * <pre>
   * Column 2: parse error
   *    │ Expected type: INTEGER
   *    │ Row: 0
   *    └ NumberFormatException: For input string: "hello"
   * </pre>
   */
  @Test
  public void testColumnTypeMismatch_messageFormat() {
    var tx = Transactor.create(DuckDbConfig.builder(":memory:").build());

    try {
      tx.execute(
          conn -> {
            Fragment.of("CREATE TABLE test_err (id INTEGER, name VARCHAR)").update().run(conn);
            Fragment.of("INSERT INTO test_err VALUES (1, 'hello')").update().run(conn);

            // Try to read the VARCHAR column as INTEGER - should fail
            return Fragment.of("SELECT id, name FROM test_err")
                .query(
                    RowCodec.<Integer>builder()
                        .field(DuckDbTypes.integer, x -> x)
                        .field(DuckDbTypes.integer, x -> x) // Wrong type for 'name'!
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
      assertTrue(
          "Should have 'Failed to read column' header",
          fullChain.contains("Failed to read column"));
      assertTrue("Should include column name from metadata", fullChain.contains("'name'"));
      assertTrue(
          "Should have box-drawing characters for structure",
          fullChain.contains("│") && fullChain.contains("└"));
      assertTrue("Should show expected type", fullChain.contains("Expected:"));
      assertTrue(
          "Should show actual type with nullability from metadata",
          fullChain.contains("Actual:")
              && fullChain.contains("VARCHAR")
              && fullChain.contains("nullable"));
      assertTrue(
          "Should show the actual value",
          fullChain.contains("Value:") && fullChain.contains("\"hello\""));
      assertTrue("Should show row number", fullChain.contains("Row:"));
      assertTrue(
          "Should include cause exception type and message",
          fullChain.contains("NumberFormatException") && fullChain.contains("For input string"));
    }
  }

  @Test
  public void testExactlyOne_noRows_messageFormat() {
    var tx = Transactor.create(DuckDbConfig.builder(":memory:").build());

    try {
      tx.execute(
          conn -> {
            Fragment.of("CREATE TABLE empty_table (id INTEGER)").update().run(conn);

            return Fragment.of("SELECT id FROM empty_table")
                .query(RowCodec.of(DuckDbTypes.integer).exactlyOne())
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
    var tx = Transactor.create(DuckDbConfig.builder(":memory:").build());

    try {
      tx.execute(
          conn -> {
            Fragment.of("CREATE TABLE multi_table (id INTEGER)").update().run(conn);
            Fragment.of("INSERT INTO multi_table VALUES (1), (2)").update().run(conn);

            return Fragment.of("SELECT id FROM multi_table")
                .query(RowCodec.of(DuckDbTypes.integer).exactlyOne())
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
    var tx = Transactor.create(DuckDbConfig.builder(":memory:").build());

    try {
      tx.execute(
          conn -> {
            Fragment.of("CREATE TABLE multi_table2 (id INTEGER)").update().run(conn);
            Fragment.of("INSERT INTO multi_table2 VALUES (1), (2)").update().run(conn);

            return Fragment.of("SELECT id FROM multi_table2")
                .query(RowCodec.of(DuckDbTypes.integer).maxOne())
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
      new dev.typr.foundations.data.Uint1((short) 256); // Max is 255
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
   * This test documents the gap between landing page claims and reality. Run this test to see what
   * messages the library actually produces.
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
    System.out.println("  RowCodec.SqlResultParseException:");
    System.out.println("    Failed to read column 3 'created_at'");
    System.out.println("       │ Expected: timestamptz");
    System.out.println("       │ Actual:   TIMESTAMP (nullable)");
    System.out.println("       │ Value:    \"2024-01-15 10:30:00\"");
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
    System.out.println("     - Would require passing column names through RowCodec");
    System.out.println();
    System.out.println("  2. Add 'Hint:' suggestions for common type mismatches");
    System.out.println("     - timestamp vs timestamptz");
    System.out.println("     - int4 vs int8");
    System.out.println();
    System.out.println("  3. Show expected vs actual column count when RowCodec fails");
    System.out.println("     - This would catch ResultSet having wrong number of columns");
    System.out.println();
  }

  // ==========================================================================
  // Verify Landing Page Accuracy
  // ==========================================================================

  /**
   * Print the EXACT output that matches the landing page. Run this test to verify landing page
   * accuracy.
   */
  @Test
  public void printExactErrorMessageForLandingPage() {
    var tx = Transactor.create(DuckDbConfig.builder(":memory:").build());

    try {
      tx.execute(
          conn -> {
            // Create table - simulate the landing page scenario
            Fragment.of("CREATE TABLE users (id INTEGER, name VARCHAR, created_at VARCHAR)")
                .update()
                .run(conn);
            Fragment.of("INSERT INTO users VALUES (1, 'Alice', '2024-01-15 10:30:00')")
                .update()
                .run(conn);

            // Try to read VARCHAR 'created_at' as timestamptz - this will fail
            record UserRow(Integer id, String name, java.time.Instant createdAt) {}
            return Fragment.of("SELECT id, name, created_at FROM users")
                .query(
                    RowCodec.<UserRow>builder()
                        .field(DuckDbTypes.integer, UserRow::id)
                        .field(DuckDbTypes.varchar, UserRow::name)
                        .field(
                            DuckDbTypes.timestamptz,
                            UserRow::createdAt) // Wrong! created_at is VARCHAR
                        .build(UserRow::new)
                        .all())
                .run(conn);
          });
      fail("Expected DatabaseException wrapping SqlResultParseException");
    } catch (DatabaseException de) {
      assertTrue(de.getCause() instanceof RowCodec.SqlResultParseException);
      var e = (RowCodec.SqlResultParseException) de.getCause();
      System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
      System.out.println("║  LANDING PAGE ERROR MESSAGE (copy this exactly)              ║");
      System.out.println("╚══════════════════════════════════════════════════════════════╝\n");

      System.out.println("=== Plain text (for landing page) ===");
      System.out.println(e.detailedPlain());

      System.out.println("\n=== With ANSI colors (for terminal) ===");
      System.out.println(e.detailedColored());

      System.out.println("\n=== Brief plain ===");
      System.out.println(e.briefPlain());

      System.out.println("\n=== Brief colored ===");
      System.out.println(e.briefColored());

      System.out.println("\n=== getMessage() (should be detailedPlain) ===");
      System.out.println(e.getMessage());
    } catch (Exception e) {
      fail("Unexpected exception type: " + e.getClass().getName() + ": " + e.getMessage());
    }
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
