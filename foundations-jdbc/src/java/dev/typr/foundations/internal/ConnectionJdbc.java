package dev.typr.foundations.internal;

import dev.typr.foundations.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/** Connection backed by a JDBC Connection, executing via OperationRunner. */
public final class ConnectionJdbc implements dev.typr.foundations.Connection {

  private final java.sql.Connection conn;
  private final OperationRunner runner;

  /** Convenience: wraps a JDBC connection with default exception mapping. */
  public ConnectionJdbc(java.sql.Connection conn) {
    this(conn, DatabaseException.Jdbc::new);
  }

  public ConnectionJdbc(
      java.sql.Connection conn, Function<SQLException, DatabaseException> exceptionMapper) {
    this.conn = conn;
    this.runner =
        new OperationRunner(
            new JdbcOperationExecutor(conn, exceptionMapper), CombineStrategy.SEQUENTIAL);
  }

  @Override
  public <T> T execute(OperationRead<T> op) {
    return runner.run(op);
  }

  @Override
  public <T> T execute(Operation<T> op) {
    return runner.run(op);
  }

  @Override
  public <T> List<T> query(Fragment sql, RowCodec<T> codec) {
    return runner.run(sql.query(codec.all()));
  }

  @Override
  public <T> Optional<T> queryFirst(Fragment sql, RowCodec<T> codec) {
    return runner.run(sql.query(codec.first()));
  }

  @Override
  public int update(Fragment sql) {
    return runner.run(sql.update());
  }

  @Override
  public java.sql.Connection unwrap() {
    return conn;
  }
}
