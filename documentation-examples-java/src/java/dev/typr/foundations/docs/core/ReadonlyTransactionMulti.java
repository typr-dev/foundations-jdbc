package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class ReadonlyTransactionMulti {
  record User(String name, String email) {}

  static RowCodec<User> userCodec =
      RowCodec.<User>builder()
          .field(PgTypes.text, User::name)
          .field(PgTypes.text, User::email)
          .build(User::new);

  Transactor tx = null; // placeholder

  OperationRead<List<User>> findAll =
      Fragment.of("SELECT name, email FROM users").query(userCodec.all());

  OperationRead<Long> countUsers =
      Fragment.of("SELECT count(*) FROM users").queryExactlyOne(PgTypes.int8);

  OperationRead<List<User>> findRecent =
      Fragment.of("SELECT name, email FROM users ORDER BY created_at DESC LIMIT 10")
          .query(userCodec.all());

  //start
  // Multiple reads in one session — same connection, auto-commit mode
  record Dashboard(List<User> users, long count, List<User> recent) {}

  Dashboard dashboard() {
    return tx.transactRead(conn -> {
      var users = conn.execute(findAll);
      var count = conn.execute(countUsers);
      var recent = conn.execute(findRecent);
      return new Dashboard(users, count, recent);
    });
  }
  //stop
}
