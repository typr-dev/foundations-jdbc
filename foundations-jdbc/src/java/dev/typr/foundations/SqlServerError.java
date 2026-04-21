package dev.typr.foundations;

import org.jetbrains.annotations.Nullable;

/**
 * Structured SQL Server error with fields from the {@code SQLServerError} driver class.
 *
 * <p>Available via {@link DatabaseException.SqlServer#sqlServerError()}.
 *
 * <p>Fields correspond to the TDS ERROR token fields exposed by {@code
 * com.microsoft.sqlserver.jdbc.SQLServerError}.
 */
public record SqlServerError(
    @Nullable String message,
    int errorNumber,
    int errorSeverity,
    int errorState,
    @Nullable String serverName,
    @Nullable String procedureName,
    long lineNumber) {

  /**
   * Format this error for display.
   *
   * <p>Output format:
   *
   * <pre>
   * ERROR (severity 16): Invalid column name 'foo' [207]
   *   Procedure: my_proc, line 5
   *   Server: MY_SERVER
   * </pre>
   *
   * @param sql the SQL that caused the error (reserved for future caret rendering)
   * @return formatted multi-line error string
   */
  public String formatted(@Nullable String sql) {
    var sb = new StringBuilder();
    sb.append("ERROR (severity ").append(errorSeverity).append("): ");
    if (message != null) sb.append(message);
    sb.append(" [").append(errorNumber).append("]");

    if (procedureName != null) {
      sb.append("\n  Procedure: ").append(procedureName);
      if (lineNumber != 0) {
        sb.append(", line ").append(lineNumber);
      }
    }

    if (serverName != null) sb.append("\n  Server: ").append(serverName);

    return sb.toString();
  }

  /** Format without SQL context. */
  public String formatted() {
    return formatted(null);
  }
}
