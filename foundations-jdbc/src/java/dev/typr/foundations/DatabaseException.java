package dev.typr.foundations;

import java.sql.SQLException;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;

/**
 * Database error thrown at the execution boundary. Sealed with two subtypes:
 *
 * <ul>
 *   <li>{@link Postgres} — structured PostgreSQL error with all ErrorResponse fields
 *   <li>{@link SqlServer} — structured SQL Server error with TDS ERROR token fields
 *   <li>{@link Jdbc} — wraps a {@link SQLException} for other databases
 * </ul>
 *
 * <p>Pattern match to handle specific cases:
 *
 * <pre>{@code
 * try {
 *     insertUser.on(user).transactRead(tx);
 * } catch (DatabaseException.Postgres pg) {
 *     System.out.println(pg.pgError().constraintName());
 * } catch (DatabaseException.SqlServer ss) {
 *     System.out.println(ss.sqlServerError().errorNumber());
 * } catch (DatabaseException.Jdbc jdbc) {
 *     System.out.println(jdbc.sqlException().getSQLState());
 * }
 * }</pre>
 *
 * <p>Or catch the base type and inspect the SQL state:
 *
 * <pre>{@code
 * try {
 *     insertUser.on(user).transactRead(tx);
 * } catch (DatabaseException e) {
 *     if ("23505".equals(e.sqlState())) { /* unique violation *{@literal /} }
 * }
 * }</pre>
 */
public abstract sealed class DatabaseException extends RuntimeException
    permits DatabaseException.Postgres, DatabaseException.SqlServer, DatabaseException.Jdbc {

  DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }

  DatabaseException(String message) {
    super(message);
  }

  /**
   * The SQL state code, or {@code null} if not available.
   *
   * <p>Standard SQL state classes: "23" = integrity constraint violation, "42" = syntax error, etc.
   */
  public abstract @Nullable String sqlState();

  /** PostgreSQL error with all structured ErrorResponse fields. */
  public static final class Postgres extends DatabaseException {
    private final PgError pgError;
    private final @Nullable String sql;

    public Postgres(PgError pgError, @Nullable String sql, @Nullable Throwable cause) {
      super(pgError.formatted(sql), cause);
      this.pgError = pgError;
      this.sql = sql;
    }

    public Postgres(PgError pgError, @Nullable String sql) {
      this(pgError, sql, null);
    }

    /** All structured error fields from the PostgreSQL ErrorResponse. */
    public PgError pgError() {
      return pgError;
    }

    /** The SQL that caused the error, if available. */
    public Optional<String> sql() {
      return Optional.ofNullable(sql);
    }

    @Override
    public @Nullable String sqlState() {
      return pgError.sqlState();
    }
  }

  /** SQL Server error with structured fields from the TDS ERROR token. */
  public static final class SqlServer extends DatabaseException {
    private final SqlServerError sqlServerError;
    private final @Nullable String sql;

    public SqlServer(
        SqlServerError sqlServerError, @Nullable String sql, @Nullable Throwable cause) {
      super(sqlServerError.formatted(sql), cause);
      this.sqlServerError = sqlServerError;
      this.sql = sql;
    }

    public SqlServer(SqlServerError sqlServerError, @Nullable String sql) {
      this(sqlServerError, sql, null);
    }

    /** All structured error fields from the SQL Server error. */
    public SqlServerError sqlServerError() {
      return sqlServerError;
    }

    /** The SQL that caused the error, if available. */
    public Optional<String> sql() {
      return Optional.ofNullable(sql);
    }

    @Override
    public @Nullable String sqlState() {
      Throwable cause = getCause();
      if (cause instanceof java.sql.SQLException sqle) {
        return sqle.getSQLState();
      }
      return null;
    }
  }

  /** Generic JDBC error wrapping a {@link SQLException}. */
  public static final class Jdbc extends DatabaseException {
    public Jdbc(SQLException cause) {
      super(cause.getMessage(), cause);
    }

    public Jdbc(String message, SQLException cause) {
      super(message, cause);
    }

    /** The original {@link SQLException}. */
    public SQLException sqlException() {
      return (SQLException) getCause();
    }

    @Override
    public @Nullable String sqlState() {
      return sqlException().getSQLState();
    }

    /** The vendor-specific error code. */
    public int vendorCode() {
      return sqlException().getErrorCode();
    }
  }
}
