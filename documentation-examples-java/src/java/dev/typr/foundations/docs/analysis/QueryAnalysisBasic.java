package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.QueryChecker;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class QueryAnalysisBasic {
    record User(int id, String name, String email) {}

    private final Transactor transactor = null; // placeholder

    private final RowCodec<User> userRowCodec =
        RowCodec.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    //start
    void checkQueryManually() {
        var query =
            Fragment.of("""
                    SELECT id, name, email
                    FROM users WHERE id =
                    """)
                .value(PgTypes.int4, 1)
                .query(userRowCodec.all());

        QueryChecker checker = QueryChecker.create(transactor);
        checker.check(query);
    }
    //stop
}
