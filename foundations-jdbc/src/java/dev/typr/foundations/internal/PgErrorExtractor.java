package dev.typr.foundations.internal;

import dev.typr.foundations.DatabaseException;
import dev.typr.foundations.PgError;
import java.sql.SQLException;
import java.util.Optional;
import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

/**
 * Maps PostgreSQL JDBC driver exceptions to {@link DatabaseException.Postgres} with structured
 * {@link PgError} fields. Directly references the PostgreSQL driver ({@code provided} dependency) —
 * only loaded when {@link dev.typr.foundations.connect.PgConfig#mapException} is called.
 */
public final class PgErrorExtractor {

  public static DatabaseException mapException(SQLException e) {
    Optional<PgError> pgError = extract(e);
    return pgError
        .<DatabaseException>map(pg -> new DatabaseException.Postgres(pg, null, e))
        .orElseGet(() -> new DatabaseException.Jdbc(e));
  }

  static Optional<PgError> extract(SQLException e) {
    if (!(e instanceof PSQLException pse)) return Optional.empty();
    ServerErrorMessage sem = pse.getServerErrorMessage();
    if (sem == null) return Optional.empty();
    int position = sem.getPosition();
    int internalPosition = sem.getInternalPosition();
    int line = sem.getLine();
    return Optional.of(
        new PgError(
            sem.getSeverity(),
            sem.getMessage(),
            sem.getSQLState(),
            sem.getDetail(),
            sem.getHint(),
            position == 0 ? null : position,
            sem.getWhere(),
            internalPosition == 0 ? null : internalPosition,
            sem.getInternalQuery(),
            sem.getSchema(),
            sem.getTable(),
            sem.getColumn(),
            sem.getDatatype(),
            sem.getConstraint(),
            sem.getFile(),
            line == 0 ? null : line,
            sem.getRoutine()));
  }
}
