package dev.typr.foundations;

import dev.typr.foundations.connect.DuckDbConfig;
import dev.typr.foundations.internal.ConnectionJdbc;
import java.sql.DriverManager;
import java.util.List;
import java.util.Optional;

public class ScreenshotApp {

  record User(Integer id, String name, Double score) {}

  record AuthorBook(Integer id, String name, String title) {}

  record ThreeInts(Integer a, Integer b, Integer c) {}

  public static void main(String[] args) throws Exception {
    try (var jdbcConn = DriverManager.getConnection("jdbc:duckdb:")) {
      jdbcConn.setAutoCommit(false);
      var conn = new ConnectionJdbc(jdbcConn);

      Fragment.of("CREATE TABLE users (id INTEGER, name VARCHAR, score DOUBLE)").update().run(conn);
      Fragment.of("INSERT INTO users VALUES (1, 'Alice', 95.5), (2, 'Bob', 87.2)").update().run(conn);

      // ── Section 1: Query Analysis Report (successful) ──────────────
      System.out.println("=== Section 1: Query Analysis -- All Types Match ===\n");

      var userCodec = RowCodec.<User>builder()
          .field(DuckDbTypes.integer, User::id)
          .field(DuckDbTypes.varchar, User::name)
          .field(DuckDbTypes.double_, User::score)
          .build(User::new);

      var goodQuery = Fragment.of("SELECT id, name, score FROM users").query(userCodec.all());
      var goodAnalysis = QueryAnalyzer.analyze(goodQuery, jdbcConn).getFirst();
      System.out.println(goodAnalysis.reportColored());

      // ── Section 2: Query Analysis Report (with errors) ─────────────
      System.out.println("\n=== Section 2: Query Analysis -- Type Mismatch Errors ===\n");

      var badCodec = RowCodec.<ThreeInts>builder()
          .field(DuckDbTypes.integer, ThreeInts::a)
          .field(DuckDbTypes.integer, ThreeInts::b)
          .field(DuckDbTypes.integer, ThreeInts::c)
          .build(ThreeInts::new);

      var badQuery = Fragment.of("SELECT id, name, score FROM users").query(badCodec.all());
      var badAnalysis = QueryAnalyzer.analyze(badQuery, jdbcConn).getFirst();
      System.out.println(badAnalysis.reportColored());

      // ── Section 2b: Query Analysis -- Nullability Mismatch ─────────
      System.out.println("\n=== Section 2b: Query Analysis -- Nullability Mismatch ===\n");

      // Construct a report showing what a nullability mismatch looks like
      // (DuckDB doesn't report nullability, but PostgreSQL/Oracle/DB2 do)
      var nullAnalysis = new QueryAnalysis(
          "SELECT u.id, u.name, b.title FROM users u LEFT JOIN books b ON u.id = b.author_id",
          java.util.Optional.of("findUserBooks"),
          List.of(),  // no parameters
          List.of(    // columns
              new Alignment.Both<>(DuckDbTypes.integer,
                  new JdbcMeta.ColumnMeta(1, java.sql.Types.INTEGER, "INTEGER",
                      java.sql.ResultSetMetaData.columnNoNulls, "id", "id")),
              new Alignment.Both<>(DuckDbTypes.varchar,
                  new JdbcMeta.ColumnMeta(2, java.sql.Types.VARCHAR, "VARCHAR",
                      java.sql.ResultSetMetaData.columnNoNulls, "name", "name")),
              new Alignment.Both<>(DuckDbTypes.varchar,
                  new JdbcMeta.ColumnMeta(3, java.sql.Types.VARCHAR, "VARCHAR",
                      java.sql.ResultSetMetaData.columnNullable, "title", "title"))
          ),
          true);
      System.out.println(nullAnalysis.reportColored());

      // ── Section 3: Runtime Parse Error ─────────────────────────────
      System.out.println("\n=== Section 3: Runtime Parse Error ===\n");

      try {
        var tx = Transactor.create(DuckDbConfig.builder(":memory:").build());
        tx.transact(c -> {
          Fragment.of("CREATE TABLE items (id INTEGER, label VARCHAR)").update().run(c);
          Fragment.of("INSERT INTO items VALUES (1, 'widget')").update().run(c);
          return Fragment.of("SELECT id, label FROM items")
              .query(RowCodec.<ThreeInts>builder()
                  .field(DuckDbTypes.integer, ThreeInts::a)
                  .field(DuckDbTypes.integer, ThreeInts::b)
                  .build((a, b) -> new ThreeInts(a, b, 0))
                  .all())
              .run(c);
        });
      } catch (DatabaseException e) {
        if (e.getCause() instanceof SqlResultParseException spe) {
          System.out.println(spe.detailedColored());
        } else {
          System.out.println(e.getMessage());
        }
      }

      // ── Section 4: PostgreSQL Error -- Syntax Error with Caret ─────
      System.out.println("\n=== Section 4: PostgreSQL Error -- Syntax Error ===\n");

      var syntaxError = new PgError(
          "ERROR", "syntax error at or near \"SELEC\"", "42601",
          Optional.empty(), Optional.empty(), Optional.of(1), Optional.empty(), Optional.empty(), Optional.empty(),
          Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
      System.out.println(syntaxError.formattedColored(Optional.of("SELEC id FROM users WHERE age > 30")));

      // ── Section 5: PostgreSQL Error -- Unique Constraint Violation ──
      System.out.println("\n=== Section 5: PostgreSQL Error -- Unique Violation ===\n");

      var uniqueError = new PgError(
          "ERROR", "duplicate key value violates unique constraint \"users_pkey\"", "23505",
          Optional.of("Key (id)=(1) already exists."), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
          Optional.of("public"), Optional.of("users"), Optional.of("id"), Optional.of("integer"), Optional.of("users_pkey"), Optional.empty(), Optional.empty(), Optional.empty());
      System.out.println(uniqueError.formattedColored());

      // ── Section 6: PostgreSQL Error -- Column Not Found with Hint ───
      System.out.println("\n=== Section 6: PostgreSQL Error -- Column Not Found ===\n");

      var colError = new PgError(
          "ERROR", "column \"agee\" does not exist", "42703",
          Optional.empty(), Optional.of("Perhaps you meant to reference the column \"age\"."),
          Optional.of(28), Optional.empty(), Optional.empty(), Optional.empty(),
          Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
      System.out.println(colError.formattedColored(Optional.of("SELECT id FROM users WHERE agee > 30")));
    }
  }
}
