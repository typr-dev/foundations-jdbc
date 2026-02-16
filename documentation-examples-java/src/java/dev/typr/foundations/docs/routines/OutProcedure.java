package dev.typr.foundations.docs.routines;

import dev.typr.foundations.*;

import java.sql.SQLException;

@SuppressWarnings("unused")
public class OutProcedure {
    Transactor tx = null; // placeholder

    // OUT parameters — the builder tracks output types statically
    static final DbProcedure.Def1_2<Integer, String, String> getUser =
        DbProcedure.define("get_user_by_id")
            .in(PgTypes.int4)       // user_id IN
            .out(PgTypes.text)      // name OUT
            .out(PgTypes.text)      // email OUT
            .build();

    //start
    // call() is fully typed — wrong argument types won't compile
    Tuple.Tuple2<String, String> findUser(int userId) throws SQLException {
        return getUser.call(userId).transact(tx);
    }
    //stop
}
