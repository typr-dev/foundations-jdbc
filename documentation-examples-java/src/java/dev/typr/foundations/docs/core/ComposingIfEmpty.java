package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.util.Optional;

@SuppressWarnings("unused")
public class ComposingIfEmpty {
  record User(int id, String name, String email) {}

  static RowCodec<User> userCodec =
      RowCodec.<User>builder()
          .field(PgTypes.int4, User::id)
          .field(PgTypes.text, User::name)
          .field(PgTypes.text, User::email)
          .build(User::new);

  Transactor tx = null; // placeholder
  String email = "alice@example.com";
  String name = "Alice";

  // start
  // Find-or-create pattern
  OperationRead<Optional<User>> findUser(String email) {
    return Fragment.of(
            """
            SELECT id, name, email
            FROM users WHERE email =
            """)
        .value(PgTypes.text, email)
        .query(userCodec.maxOne());
  }

  OperationRead<User> createUser(String name, String email) {
    return Fragment.of(
            """
            INSERT INTO users(name, email)
            VALUES(\
            """)
        .value(PgTypes.text, name)
        .append(", ")
        .value(PgTypes.text, email)
        .append(") RETURNING *")
        .query(userCodec.exactlyOne());
  }

  User findOrCreate() {
    return OperationRead.ifEmpty(findUser(email), createUser(name, email)).transact(tx);
  }
  // stop
}
