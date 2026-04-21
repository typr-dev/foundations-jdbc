package dev.typr.foundations.pg;

/**
 * Infrastructure error from the PgPipe connection pool — connection refused, authentication
 * failure, socket closed, FATAL server error, etc.
 *
 * <p>Query errors (syntax error, constraint violation, etc.) are thrown as {@link
 * dev.typr.foundations.DatabaseException.Postgres} instead.
 */
public final class PgPipelineException extends RuntimeException {

  public PgPipelineException(String message) {
    super(message);
  }

  public PgPipelineException(String message, Throwable cause) {
    super(message, cause);
  }

  public PgPipelineException(Throwable cause) {
    super(cause);
  }
}
