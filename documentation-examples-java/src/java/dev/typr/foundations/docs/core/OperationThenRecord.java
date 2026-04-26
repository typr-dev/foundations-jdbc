package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.OperationRead;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class OperationThenRecord {
  record NewUser(int id, String name) {}

  static RowCodec<NewUser> newUserCodec =
      RowCodec.<NewUser>builder()
          .field(PgTypes.int4, NewUser::id)
          .field(PgTypes.text, NewUser::name)
          .build(NewUser::new);

  Transactor tx = null; // placeholder

  // start
  // Insert and return the new user (id + name).
  OperationRead<NewUser> insertUser(String name) {
    return Fragment.of("INSERT INTO users(name) VALUES(")
        .value(PgTypes.text, name)
        .append(") RETURNING id, name")
        .query(newUserCodec.exactlyOne());
  }

  // Log the creation, taking the new user record as input.
  Operation<Integer> logCreation(NewUser user) {
    return Fragment.of("INSERT INTO audit_log(user_id, username) VALUES(")
        .value(PgTypes.int4, user.id())
        .append(", ")
        .value(PgTypes.text, user.name())
        .append(")")
        .update();
  }

  // Chain: insertUser → returned NewUser → logCreation.
  // .then(fn) feeds the first op's result into the second op as a method call.
  int insertAndLog() {
    return tx.execute(insertUser("Alice").then(this::logCreation));
  }
  // stop
}
