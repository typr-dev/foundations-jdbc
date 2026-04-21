package dev.typr.foundations;

import org.jetbrains.annotations.Nullable;

/**
 * Structured PostgreSQL error with all fields from the ErrorResponse wire protocol message.
 *
 * <p>Available on both PgPipe ({@link dev.typr.foundations.pg.PgPipelineException#pgError()}) and
 * JDBC ({@link DatabaseException#pgError()}) paths.
 *
 * <p>Fields follow the <a
 * href="https://www.postgresql.org/docs/current/protocol-error-fields.html">PostgreSQL protocol
 * error field documentation</a>.
 */
public record PgError(
    String severity,
    String message,
    String sqlState,
    @Nullable String detail,
    @Nullable String hint,
    @Nullable Integer position,
    @Nullable String where,
    @Nullable Integer internalPosition,
    @Nullable String internalQuery,
    @Nullable String schemaName,
    @Nullable String tableName,
    @Nullable String columnName,
    @Nullable String dataTypeName,
    @Nullable String constraintName,
    @Nullable String file,
    @Nullable Integer line,
    @Nullable String routine) {

  /**
   * Format this error for display, including a source-position caret when the position field is
   * present.
   *
   * @param sql the SQL that caused the error (used for caret rendering)
   * @return formatted multi-line error string
   */
  public String formatted(@Nullable String sql) {
    var sb = new StringBuilder();
    sb.append(severity).append(": ").append(message);
    if (sqlState != null) sb.append(" [").append(sqlState).append("]");

    if (sql != null && !sql.isEmpty() && position != null && position > 0) {
      sb.append('\n');
      appendCaret(sb, sql, position);
    }

    if (detail != null) sb.append("\n  Detail: ").append(detail);
    if (hint != null) sb.append("\n  Hint: ").append(hint);
    if (where != null) sb.append("\n  Where: ").append(where);

    if (internalQuery != null && internalPosition != null && internalPosition > 0) {
      sb.append("\n  Internal query:");
      sb.append('\n');
      appendCaret(sb, internalQuery, internalPosition);
    }

    if (schemaName != null) sb.append("\n  Schema: ").append(schemaName);
    if (tableName != null) sb.append("\n  Table: ").append(tableName);
    if (columnName != null) sb.append("\n  Column: ").append(columnName);
    if (dataTypeName != null) sb.append("\n  Type: ").append(dataTypeName);
    if (constraintName != null) sb.append("\n  Constraint: ").append(constraintName);

    return sb.toString();
  }

  /** Format without SQL context (no caret). */
  public String formatted() {
    return formatted(null);
  }

  /**
   * Append the SQL with a caret (└──) pointing at the given 1-based character position.
   *
   * <pre>
   * SELEC id FROM users WHERE age > 30
   * └───
   * </pre>
   */
  static void appendCaret(StringBuilder sb, String sql, int position1Based) {
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

    sb.append("  ").append(line).append('\n');

    if (adjustedPos < 0 || adjustedPos > line.length()) {
      return;
    }
    sb.append("  ");
    for (int i = 0; i < adjustedPos; i++) sb.append(' ');
    sb.append("└──");
  }
}
