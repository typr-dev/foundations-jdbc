package dev.typr.foundations;

import dev.typr.foundations.internal.ConnectionJdbc;
import dev.typr.foundations.internal.ConnectionReadJdbc;
import java.sql.SQLException;

/**
 * Test-only transactor backed by a single JDBC connection. Does not manage transaction lifecycle —
 * the caller owns the connection.
 */
public final class TestTransactor {

  public static Transactor fromConnection(java.sql.Connection conn) {
    java.util.function.Function<SQLException, DatabaseException> mapper =
        DatabaseException.Jdbc::new;
    return new Transactor() {
      @Override
      public <T> T execute(Operation<T> op) {
        return new ConnectionJdbc(conn, mapper).execute(op);
      }

      @Override
      public <T> T transact(SqlFunction<Connection, T> fn) {
        try {
          return fn.apply(new ConnectionJdbc(conn, mapper));
        } catch (SQLException e) {
          throw new DatabaseException.Jdbc(e);
        }
      }

      @Override
      public <T> T transactRead(SqlFunction<ConnectionRead, T> fn) {
        try {
          return fn.apply(new ConnectionReadJdbc(conn, mapper));
        } catch (SQLException e) {
          throw new DatabaseException.Jdbc(e);
        }
      }
    };
  }
}
