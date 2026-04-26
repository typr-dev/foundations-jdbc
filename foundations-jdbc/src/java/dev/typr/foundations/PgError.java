package dev.typr.foundations;

import dev.typr.foundations.internal.Str;
import java.util.Optional;

/**
 * Structured PostgreSQL error with all fields from the ErrorResponse wire protocol message.
 *
 * <p>Fields follow the <a
 * href="https://www.postgresql.org/docs/current/protocol-error-fields.html">PostgreSQL protocol
 * error field documentation</a>.
 */
public record PgError(
    String severity,
    String message,
    String sqlState,
    Optional<String> detail,
    Optional<String> hint,
    Optional<Integer> position,
    Optional<String> where,
    Optional<Integer> internalPosition,
    Optional<String> internalQuery,
    Optional<String> schemaName,
    Optional<String> tableName,
    Optional<String> columnName,
    Optional<String> dataTypeName,
    Optional<String> constraintName,
    Optional<String> file,
    Optional<Integer> line,
    Optional<String> routine) {

  /** Format this error for display (plain text). */
  public String formatted(Optional<String> sql) {
    return styledFormatted(sql).plainText();
  }

  /** Format without SQL context (no caret). */
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

    b.boldRed(severity + ": ").plain(message);
    b.gray(" [" + sqlState + "]");

    if (sql.isPresent() && !sql.get().isEmpty() && position.isPresent() && position.get() > 0) {
      b.newline();
      appendStyledCaret(b, sql.get(), position.get());
    }

    detail.ifPresent(d -> b.newline().gray("  Detail: ").plain(d));
    hint.ifPresent(h -> b.newline().gray("  Hint: ").cyan(h));
    where.ifPresent(w -> b.newline().gray("  Where: ").plain(w));

    if (internalQuery.isPresent() && internalPosition.isPresent() && internalPosition.get() > 0) {
      b.newline().gray("  Internal query:");
      b.newline();
      appendStyledCaret(b, internalQuery.get(), internalPosition.get());
    }

    schemaName.ifPresent(s -> b.newline().gray("  Schema: ").plain(s));
    tableName.ifPresent(t -> b.newline().gray("  Table: ").plain(t));
    columnName.ifPresent(c -> b.newline().gray("  Column: ").plain(c));
    dataTypeName.ifPresent(d -> b.newline().gray("  Type: ").plain(d));
    constraintName.ifPresent(c -> b.newline().gray("  Constraint: ").yellow(c));

    return b.build();
  }

  private static void appendStyledCaret(Str.Builder b, String sql, int position1Based) {
    String trimmed = sql.stripLeading();
    int leadingStripped = sql.length() - trimmed.length();
    int adjustedPos = position1Based - 1 - leadingStripped;

    String line = trimmed.lines().findFirst().orElse(trimmed);
    int lineOffset = 0;

    for (String l : trimmed.split("\n")) {
      if (adjustedPos < lineOffset + l.length() + 1) {
        line = l.stripTrailing();
        adjustedPos -= lineOffset;
        break;
      }
      lineOffset += l.length() + 1;
    }

    b.plain("  ").gray(line).newline();

    if (adjustedPos >= 0 && adjustedPos <= line.length()) {
      b.plain("  ");
      for (int i = 0; i < adjustedPos; i++) b.plain(" ");
      b.red("└──");
    }
  }
}
