package dev.typr.foundations.internal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dev.typr.foundations.DatabaseException;
import dev.typr.foundations.SqlServerError;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Maps SQL Server JDBC driver exceptions to {@link DatabaseException.SqlServer} with structured
 * {@link SqlServerError} fields. Directly references the SQL Server driver ({@code provided}
 * dependency) — only loaded when {@link dev.typr.foundations.connect.SqlServerConfig#mapException}
 * is called.
 */
public final class SqlServerErrorExtractor {

  public static DatabaseException mapException(SQLException e) {
    Optional<SqlServerError> error = extract(e);
    return error
        .<DatabaseException>map(
            err -> new DatabaseException.SqlServer(err, Optional.empty(), Optional.of(e)))
        .orElseGet(() -> new DatabaseException.Jdbc(e));
  }

  static Optional<SqlServerError> extract(SQLException e) {
    if (!(e instanceof SQLServerException sse)) return Optional.empty();
    var serverError = sse.getSQLServerError();
    if (serverError == null) return Optional.empty();
    String serverName = serverError.getServerName();
    String procedureName = serverError.getProcedureName();
    return Optional.of(
        new SqlServerError(
            Optional.ofNullable(serverError.getErrorMessage()),
            serverError.getErrorNumber(),
            serverError.getErrorSeverity(),
            serverError.getErrorState(),
            Optional.ofNullable(serverName).filter(s -> !s.isEmpty()),
            Optional.ofNullable(procedureName).filter(s -> !s.isEmpty()),
            serverError.getLineNumber()));
  }
}
