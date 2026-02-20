package dev.typr.foundations.docs.landing;

import dev.typr.foundations.*;

@SuppressWarnings("unused")
public class StoredProcedure {
    Transactor tx = null; // placeholder

    //start
    // Define once, call many times — input and output types are baked in
    static final DbProcedure.Def1_2<Integer, String, String> getUser =
        DbProcedure.define("get_user_by_id")
            .input(PgTypes.int4)
            .out(PgTypes.text)
            .out(PgTypes.text)
            .build();

    // call() returns an Operation — compose it like any other query
    Tuple.Tuple2<String, String> findUser(int userId) {
        return getUser.call(userId).transact(tx);
    }
    //stop
}
