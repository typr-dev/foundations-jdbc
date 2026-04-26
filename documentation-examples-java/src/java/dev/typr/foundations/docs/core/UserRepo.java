package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
// start
public final class UserRepo {
  record User(int id, String name) {}

  static final RowCodecNamed<User> userCodec =
      RowCodec.<User>namedBuilder()
          .field("id", PgTypes.int4, User::id)
          .field("name", PgTypes.text, User::name)
          .build(User::new);

  static final OperationRead<List<User>> selectAll =
      Fragment.of("SELECT ")
          .append(userCodec.columnList())
          .append(" FROM users ORDER BY name")
          .query(userCodec.all())
          .named("UserRepo.selectAll");

  static OperationRead<Optional<User>> selectById(int id) {
    return Fragment.of("SELECT ")
        .append(userCodec.columnList())
        .append(" FROM users WHERE id = ")
        .value(PgTypes.int4, id)
        .query(userCodec.maxOne());
  }
}
// stop
