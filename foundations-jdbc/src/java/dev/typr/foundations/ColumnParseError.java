package dev.typr.foundations;

import dev.typr.foundations.internal.Str;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import org.jetbrains.annotations.Nullable;

/**
 * Describes a column parsing error with all relevant context. Can render as styled text (with or
 * without ANSI colors).
 *
 * <p>Usage:
 *
 * <pre>{@code
 * ColumnParseError error = ColumnParseError.from(rs, row, column, dbType, cause);
 *
 * // Detailed format (multi-line, like Query Analysis)
 * System.out.println(error.detailed().render());      // With colors
 * System.out.println(error.detailed().plainText());   // Without colors
 *
 * // Brief format (single line)
 * System.out.println(error.brief().render());         // With colors
 * System.out.println(error.brief().plainText());      // Without colors
 * }</pre>
 */
public record ColumnParseError(
    int column,
    @Nullable String columnName,
    @Nullable String expectedType,
    boolean expectedNullable,
    @Nullable String actualType,
    @Nullable Boolean actualNullable,
    @Nullable String valuePreview,
    int row,
    @Nullable Exception cause) {

  // ==================== Factory ====================

  public static ColumnParseError from(
      ResultSet rs, int row, int column, DbType<?> tpe, Exception cause) {

    String columnName = null;
    String actualType = null;
    Boolean actualNullable = null;
    String valuePreview = null;

    // Extract metadata
    try {
      ResultSetMetaData meta = rs.getMetaData();
      columnName = meta.getColumnName(column);
      actualType = meta.getColumnTypeName(column);
      int nullable = meta.isNullable(column);
      if (nullable != ResultSetMetaData.columnNullableUnknown) {
        actualNullable = (nullable == ResultSetMetaData.columnNullable);
      }
    } catch (Exception ignored) {
    }

    // Try to get value preview
    try {
      String strVal = rs.getString(column);
      if (strVal == null) {
        valuePreview = "null";
      } else if (strVal.length() <= 50) {
        valuePreview = strVal;
      } else {
        valuePreview = strVal.substring(0, 50) + "... (" + strVal.length() + " chars)";
      }
    } catch (Exception e1) {
      try {
        byte[] bytes = rs.getBytes(column);
        if (bytes == null) {
          valuePreview = "null";
        } else {
          valuePreview = bytesToHex(bytes, 25);
        }
      } catch (Exception ignored) {
      }
    }

    String expectedType = tpe != null ? tpe.typename().sqlType() : null;
    boolean expectedNullable = tpe != null && tpe.isNullable();

    return new ColumnParseError(
        column,
        columnName,
        expectedType,
        expectedNullable,
        actualType,
        actualNullable,
        valuePreview,
        row,
        cause);
  }

  // ==================== Rendering ====================

  /**
   * Detailed multi-line format, similar to Query Analysis output.
   *
   * <pre>
   * Failed to read column 3 'created_at'
   *    │ Expected: timestamptz
   *    │ Actual:   TIMESTAMP (nullable)
   *    │ Value:    "2024-01-15 10:30:00"
   *    │ Row: 0
   *    └ SQLException: Cannot convert Timestamp to OffsetDateTime
   * </pre>
   */
  public Str detailed() {
    var b = Str.builder();

    // Header
    b.boldRed("Failed to read column ").yellow(String.valueOf(column));
    if (columnName != null && !columnName.isEmpty()) {
      b.boldRed(" '").cyan(columnName).boldRed("'");
    }
    b.newline();

    // Expected type
    if (expectedType != null) {
      b.gray("   │ ").plain("Expected: ").green(expectedType);
      if (expectedNullable) {
        b.gray(" (nullable)");
      }
      b.newline();
    }

    // Actual type
    if (actualType != null) {
      b.gray("   │ ").plain("Actual:   ").red(actualType);
      if (actualNullable != null) {
        b.gray(actualNullable ? " (nullable)" : " (not null)");
      }
      b.newline();
    }

    // Value preview
    if (valuePreview != null) {
      b.gray("   │ ").plain("Value:    ");
      if ("null".equals(valuePreview)) {
        b.yellow("null");
      } else {
        b.yellow("\"" + valuePreview + "\"");
      }
      b.newline();
    }

    // Row
    b.gray("   │ ").plain("Row: ").yellow(String.valueOf(row)).newline();

    // Cause
    if (cause != null) {
      b.gray("   └ ").red(cause.getClass().getSimpleName());
      if (cause.getMessage() != null) {
        b.plain(": ").plain(cause.getMessage());
      }
    }

    return b.build();
  }

  /**
   * Brief single-line format for compact display.
   *
   * <pre>
   * Column 3 'created_at': expected timestamptz, got TIMESTAMP (value: "2024-01-15...")
   * </pre>
   */
  public Str brief() {
    var b = Str.builder();

    b.plain("Column ").yellow(String.valueOf(column));
    if (columnName != null && !columnName.isEmpty()) {
      b.plain(" '").cyan(columnName).plain("'");
    }
    b.plain(": ");

    if (expectedType != null && actualType != null) {
      b.plain("expected ").green(expectedType).plain(", got ").red(actualType);
    } else if (expectedType != null) {
      b.plain("expected ").green(expectedType);
    } else if (actualType != null) {
      b.plain("got ").red(actualType);
    }

    if (valuePreview != null && !"null".equals(valuePreview)) {
      String preview =
          valuePreview.length() > 20 ? valuePreview.substring(0, 20) + "..." : valuePreview;
      b.plain(" (value: ").yellow("\"" + preview + "\"").plain(")");
    }

    return b.build();
  }

  // ==================== Helpers ====================

  private static String bytesToHex(byte[] bytes, int maxBytes) {
    int len = Math.min(bytes.length, maxBytes);
    StringBuilder sb = new StringBuilder("0x");
    for (int i = 0; i < len; i++) {
      sb.append(String.format("%02X", bytes[i]));
    }
    if (bytes.length > maxBytes) {
      sb.append("... (" + bytes.length + " bytes)");
    }
    return sb.toString();
  }
}
