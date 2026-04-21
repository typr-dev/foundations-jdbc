package dev.typr.foundations;

import dev.typr.foundations.internal.Str;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Exception thrown when parsing a column from a ResultSet fails. Provides multiple rendering
 * options via {@link ColumnParseError}.
 *
 * <p>Usage:
 *
 * <pre>{@code
 * try {
 *     rowCodec.parse(rs);
 * } catch (SqlResultParseException e) {
 *     // Detailed with colors (for terminal)
 *     System.err.println(e.detailed().render());
 *
 *     // Brief without colors (for logs)
 *     logger.error(e.brief().plainText());
 * }
 * }</pre>
 */
public class SqlResultParseException extends SQLException {
  private final ColumnParseError error;

  public SqlResultParseException(
      ResultSet rs, int row, int column, DbType<?> tpe, Exception cause) {
    super(ColumnParseError.from(rs, row, column, tpe, cause).detailed().plainText(), cause);
    this.error = ColumnParseError.from(rs, row, column, tpe, cause);
  }

  /** Get the structured error information */
  public ColumnParseError error() {
    return error;
  }

  /** Detailed multi-line format (styled) */
  public Str detailed() {
    return error.detailed();
  }

  /** Brief single-line format (styled) */
  public Str brief() {
    return error.brief();
  }

  /** Detailed with ANSI colors */
  public String detailedColored() {
    return error.detailed().render();
  }

  /** Detailed without colors */
  public String detailedPlain() {
    return error.detailed().plainText();
  }

  /** Brief with ANSI colors */
  public String briefColored() {
    return error.brief().render();
  }

  /** Brief without colors */
  public String briefPlain() {
    return error.brief().plainText();
  }
}
