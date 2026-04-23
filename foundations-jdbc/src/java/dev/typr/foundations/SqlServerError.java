package dev.typr.foundations;

import dev.typr.foundations.internal.Str;
import java.util.Optional;

/**
 * Structured SQL Server error with fields from the {@code SQLServerError} driver class.
 *
 * <p>Available via {@link DatabaseException.SqlServer#sqlServerError()}.
 *
 * <p>Fields correspond to the TDS ERROR token fields exposed by {@code
 * com.microsoft.sqlserver.jdbc.SQLServerError}.
 */
public record SqlServerError(
    Optional<String> message,
    int errorNumber,
    int errorSeverity,
    int errorState,
    Optional<String> serverName,
    Optional<String> procedureName,
    long lineNumber) {

  /** Format this error for display (plain text). */
  public String formatted(Optional<String> sql) {
    return styledFormatted(sql).plainText();
  }

  /** Format without SQL context. */
  public String formatted() {
    return formatted(Optional.empty());
  }

  /** Format with ANSI colors. */
  public String formattedColored(Optional<String> sql) {
    return styledFormatted(sql).render();
  }

  /** Format with ANSI colors, no SQL context. */
  public String formattedColored() {
    return formattedColored(Optional.empty());
  }

  /** Styled format with rich terminal rendering. */
  public Str styledFormatted(Optional<String> sql) {
    var b = Str.builder();

    b.boldRed("ERROR").gray(" (severity " + errorSeverity + "): ");
    message.ifPresent(b::plain);
    b.gray(" [" + errorNumber + "]");

    procedureName.ifPresent(p -> {
      b.newline().gray("  Procedure: ").plain(p);
      if (lineNumber != 0) {
        b.gray(", line ").plain(String.valueOf(lineNumber));
      }
    });

    serverName.ifPresent(s -> b.newline().gray("  Server: ").plain(s));

    return b.build();
  }
}
