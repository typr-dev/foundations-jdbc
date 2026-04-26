package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.OperationRead;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.QueryAnalysis;
import dev.typr.foundations.QueryAnalyzer;
import dev.typr.foundations.RowCodec;
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

  OperationRead<Integer> insertUser(String name) {
    return Fragment.of("INSERT INTO users(name) VALUES(")
        .value(PgTypes.text, name)
        .append(") RETURNING id")
        .query(RowCodec.of(PgTypes.int4).exactlyOne());
  }

  OperationRead<List<User>> allUsers =
      Fragment.of("SELECT id, name FROM users").query(userCodec.all());

  // start
  void analyzeComposedOperation() {
    Operation<?> transaction = insertUser("Alice").productL(allUsers);

    // Analyze every SQL statement in the tree
    List<QueryAnalysis> results = QueryAnalyzer.analyze(transaction, conn);

    for (var analysis : results) {
      if (!analysis.succeeded()) {
        System.err.println(analysis.report());
      }
    }
  }
  // stop
}
