package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.QueryAnalysis;
import dev.typr.foundations.QueryAnalyzer;
import java.sql.Connection;

@SuppressWarnings("unused")
public class QueryAnalysisNamed {
    record User(int id, String name, String email) {}

    private final Connection connection = null; // placeholder
    private final int userId = 1;

    private final RowCodec<User> userRowCodec =
        RowCodec.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    //start
    void analyzeNamedQuery() {
        var query =
            Fragment.of("""
                    SELECT id, name, email
                    FROM users WHERE id =
                    """)
                .value(PgTypes.int4, userId)
                .query(userRowCodec.all())
                .named("findUserById");

        // The name shows up in the error report
        QueryAnalysis analysis =
            QueryAnalyzer.analyze(query, connection).getFirst();

        if (!analysis.succeeded()) {
            throw new AssertionError(analysis.report());
        }
    }
    //stop
}
