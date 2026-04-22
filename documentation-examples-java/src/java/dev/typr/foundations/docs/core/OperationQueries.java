package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class OperationQueries {
  record User(int id, String name) {}

  static RowCodec<User> userCodec =
      RowCodec.<User>builder()
          .field(PgTypes.int4, User::id)
          .field(PgTypes.text, User::name)
          .build(User::new);

  Fragment fragment = Fragment.of("SELECT id, name FROM users");

  // start
  // Multi-column: pass a RowCodec with a result mode
  OperationRead<List<User>> allUsers = fragment.query(userCodec.all());
  OperationRead<Optional<User>> maybeUser = fragment.query(userCodec.maxOne());
  OperationRead<User> oneUser = fragment.query(userCodec.exactlyOne());

  // Single-column: shorthand methods skip the codec
  OperationRead<List<Integer>> allIds = Fragment.of("SELECT id FROM users").queryAll(PgTypes.int4);
  OperationRead<Optional<String>> maybeName =
      Fragment.of("SELECT name FROM users LIMIT 1").queryMaxOne(PgTypes.text);
  OperationRead<Integer> count =
      Fragment.of("SELECT count(*) FROM users").queryExactlyOne(PgTypes.int4);
  // stop
}
