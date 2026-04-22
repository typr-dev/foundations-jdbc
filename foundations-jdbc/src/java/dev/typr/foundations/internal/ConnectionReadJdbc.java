package dev.typr.foundations.internal;

import dev.typr.foundations.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/** ConnectionRead backed by a JDBC Connection, executing via OperationRunner. */
public final class ConnectionReadJdbc implements ConnectionRead {

  private final OperationRunner runner;

  public ConnectionReadJdbc(java.sql.Connection conn) {
    this(conn, DatabaseException.Jdbc::new);
  }

  public ConnectionReadJdbc(
      java.sql.Connection conn, Function<SQLException, DatabaseException> exceptionMapper) {
    this.runner =
        new OperationRunner(
            new JdbcOperationExecutor(conn, exceptionMapper), CombineStrategy.SEQUENTIAL);
  }

  @Override
  public <T> T execute(OperationRead<T> op) {
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
}
