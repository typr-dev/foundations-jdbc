package dev.typr.foundations.docs.landing;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.QueryAnalyzer;
import dev.typr.foundations.RowCodec;
import java.sql.Connection;
import java.util.List;

@SuppressWarnings("unused")
class QueryAnalysisExample {
  record User(Integer id, String name, Integer createdAt, String email) {}

  Connection connection = null; // placeholder

  RowCodec<User> userCodec =
      RowCodec.<User>builder()
          .field(PgTypes.int4, User::id)
          .field(PgTypes.text, User::name)
          .field(PgTypes.int4, User::createdAt)
          .field(PgTypes.text, User::email)
          .build(User::new);

  // start
  // Your query looks fine at compile time...
  Operation.Query<List<User>> query =
      Fragment.of(
              """
              SELECT id, name, created_at, email
              FROM users WHERE active =
              """)
          .value(PgTypes.bool, true)
          .query(userCodec.all());

  // But Query Analysis catches the bugs in your tests
  void check() {
    dev.typr.foundations.QueryAnalysis analysis =
        QueryAnalyzer.analyze(query, connection).getFirst();
    if (!analysis.succeeded()) {
      throw new AssertionError(analysis.report());
    }
  }
  // stop
}
