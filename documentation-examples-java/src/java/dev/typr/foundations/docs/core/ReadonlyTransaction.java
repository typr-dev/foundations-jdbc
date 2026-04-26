package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class ReadonlyTransaction {
  record User(String name, String email) {}

  static RowCodec<User> userCodec =
      RowCodec.<User>builder()
          .field(PgTypes.text, User::name)
          .field(PgTypes.text, User::email)
          .build(User::new);

  Transactor tx = null; // placeholder

  OperationRead<List<User>> findAll =
      Fragment.of("SELECT name, email FROM users").query(userCodec.all());

  //start
  // Single read operation — no transaction overhead
  List<User> allUsers() {
    return findAll.transactRead(tx);
  }
  //stop
}
