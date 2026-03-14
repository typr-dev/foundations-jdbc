package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Template;
import dev.typr.foundations.QueryAnalysis;
import dev.typr.foundations.QueryAnalyzer;

import java.sql.Connection;
import java.util.List;

@SuppressWarnings("unused")
public class QueryAnalysisAll {
    record User(int id, String name) {}

    static RowCodec<User> userCodec =
        RowCodec.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .build(User::new);

    Connection conn = null; // placeholder

    Template<String, Integer> insertUser =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .param(PgTypes.text)
            .append(") RETURNING id")
            .query(RowCodec.of(PgTypes.int4).exactlyOne());

    Operation<List<User>> allUsers =
        Fragment.of("SELECT id, name FROM users")
            .query(userCodec.all());

    //start
    void analyzeComposedOperation() {
        // Build a composed operation
        Operation<?> transaction =
            insertUser.on("Alice").productL(allUsers);

        // Analyze every SQL statement in the tree
        List<QueryAnalysis> results =
            QueryAnalyzer.analyze(transaction, conn);

        for (var analysis : results) {
            if (!analysis.succeeded()) {
                System.err.println(analysis.report());
            }
        }
    }
    //stop
}
