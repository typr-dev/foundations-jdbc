package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.analysis.QueryAnalysis;
import dev.typr.foundations.analysis.QueryAnalyzer;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class QueryAnalysisNamed {
    record User(int id, String name, String email) {}

    private final Connection connection = null; // placeholder
    private final int userId = 1;

    private final RowParser<User> userRowParser = RowParser.<User>builder()
        .field(PgTypes.int4, User::id)
        .field(PgTypes.text, User::name)
        .field(PgTypes.text, User::email)
        .build(User::new);

    //start
    void analyzeNamedQuery() throws SQLException {
        var query = Fragment.interpolate("SELECT id, name, email FROM users WHERE id = ")
            .param(PgTypes.int4, userId)
            .done()
            .query(userRowParser.all());

        // Give your query a name — it shows up in the error report
        QueryAnalysis analysis = QueryAnalyzer.analyze("findUserById", query, connection);

        if (!analysis.succeeded()) {
            throw new AssertionError(analysis.report());
        }
    }
    //stop
}
