package dev.typr.foundations;

import java.sql.SQLException;
import org.jetbrains.annotations.Nullable;

/**
 * Unchecked wrapper for {@link SQLException} thrown at the execution boundary.
 *
 * <p>JDBC errors during query execution are almost never recoverable by application code. This
 * unchecked exception removes the need for {@code throws SQLException} on every call site, and
 * works correctly with Spring's {@code @Transactional} (which only rolls back unchecked exceptions
 * by default).
 *
 * <p>To distinguish specific errors (e.g. constraint violations), inspect the SQL state:
 *
 * <pre>{@code
 * try {
 *     insertUser.on(user).transact(tx);
 * } catch (DatabaseException e) {
 *     if (e.sqlState() != null && e.sqlState().startsWith("23")) {
 *         // constraint violation (unique, foreign key, etc.)
 *     }
 * }
 * }</pre>
 */
public class DatabaseException extends RuntimeException {

  public DatabaseException(SQLException cause) {
    super(cause.getMessage(), cause);
  }

  public DatabaseException(String message, SQLException cause) {
    super(message, cause);
  }

  /**
   * The SQLState code from the underlying {@link SQLException}, or {@code null} if the driver did
   * not provide one.
   *
   * <p>Standard SQL state classes: "23" = integrity constraint violation, "42" = syntax error, etc.
   */
  public @Nullable String sqlState() {
    return sqlException().getSQLState();
  }

  /** The vendor-specific error code from the underlying {@link SQLException}. */
  public int vendorCode() {
    return sqlException().getErrorCode();
  }

  /** The original {@link SQLException}. */
  public SQLException sqlException() {
    return (SQLException) getCause();
  }
}
