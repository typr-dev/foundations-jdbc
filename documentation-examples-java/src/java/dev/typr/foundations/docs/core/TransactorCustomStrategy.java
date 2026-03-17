package dev.typr.foundations.docs.core;

import dev.typr.foundations.Transactor;
import java.sql.Connection;

@SuppressWarnings("unused")
public class TransactorCustomStrategy {
  // start
  Transactor.Strategy customStrategy =
      Transactor.Strategy.empty()
          .replaceOnBegin(conn -> conn.setAutoCommit(false))
          .replaceOnSuccess(Connection::commit)
          .replaceOnFailure(
              (conn, throwable) -> {
                /* handle error */
              })
          .replaceOnComplete(Connection::close);
  // stop
}
