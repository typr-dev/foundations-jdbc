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
  TemplateRead.Query1<String, Optional<User>> findUser =
      Fragment.of(
              """
              SELECT id, name, email
              FROM users WHERE email =
              """)
          .param(PgTypes.text)
          .query(userCodec.maxOne());

  TemplateRead.Query2<String, String, User> createUser =
      Fragment.of(
              """
              INSERT INTO users(name, email)
              VALUES(\
              """)
          .param(PgTypes.text)
          .append(", ")
          .param(PgTypes.text)
          .append(") RETURNING *")
          .query(userCodec.exactlyOne());

  User findOrCreate() {
    return OperationRead.ifEmpty(findUser.on(email), createUser.on(name, email)).transact(tx);
  }
  // stop
}
