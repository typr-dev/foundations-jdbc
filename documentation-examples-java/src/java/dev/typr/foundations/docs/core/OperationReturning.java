package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class OperationReturning {
  record User(int id, String name) {}

  static RowCodec<User> userCodec =
      RowCodec.<User>builder()
          .field(PgTypes.int4, User::id)
          .field(PgTypes.text, User::name)
          .build(User::new);

  // start
  // INSERT ... RETURNING id, name
  Operation<List<User>> insertedUsers =
      Fragment.of("INSERT INTO users (name) VALUES ('alice') RETURNING id, name")
          .updateReturning(userCodec.all());

  Operation<User> insertedUser =
      Fragment.of("INSERT INTO users (name) VALUES ('alice') RETURNING id, name")
          .updateReturning(userCodec.exactlyOne());

  // For databases that use generated keys instead of RETURNING (SQL Server, MariaDB)
  Operation<Integer> generatedId =
      Fragment.of("INSERT INTO users (name) VALUES ('alice')")
          .updateReturningGeneratedKeys(
              new String[] {"id"}, RowCodec.of(PgTypes.int4).exactlyOne());
  // stop
}
