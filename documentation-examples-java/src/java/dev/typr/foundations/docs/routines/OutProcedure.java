package dev.typr.foundations.docs.routines;

import dev.typr.foundations.*;

@SuppressWarnings("unused")
public class OutProcedure {
  Transactor tx = null; // placeholder

  // start
  // OUT parameters — the builder tracks output types statically
  static final DbProcedure.Def1_2<Integer, String, String> getUser =
      DbProcedure.define("get_user_by_id")
          .input(PgTypes.int4)
          .out(PgTypes.text)
          .out(PgTypes.text)
          .build();

  // call() is fully typed — wrong argument types won't compile
  Tuple.Tuple2<String, String> findUser(int userId) {
    return getUser.call(userId).transact(tx);
  }
  // stop
}
