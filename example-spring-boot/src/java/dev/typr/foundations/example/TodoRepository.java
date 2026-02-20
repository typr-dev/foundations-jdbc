package dev.typr.foundations.example;

import dev.typr.foundations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class TodoRepository {

    public record Todo(Integer id, String title, Boolean done) {}

    private static final RowCodecNamed<Todo> todoCodec = RowCodec.<Todo>namedBuilder()
            .field("id", DuckDbTypes.integer, Todo::id)
            .field("title", DuckDbTypes.varchar, Todo::title)
            .field("done", DuckDbTypes.boolean_, Todo::done)
            .build(Todo::new);

    private static final Operation.Query<List<Todo>> FIND_ALL =
            Fragment.of("SELECT ").append(todoCodec.columnList())
                    .append(" FROM todo ORDER BY id")
                    .query(todoCodec.all());

    private final Transactor tx;

    public TodoRepository(Transactor tx) {
        this.tx = tx;
    }

    public void createSchema() throws SQLException {
        tx.executeVoid(conn -> {
            try (var stmt = conn.createStatement()) {
                stmt.execute("CREATE SEQUENCE IF NOT EXISTS todo_id_seq START 1");
                stmt.execute("""
                        CREATE TABLE IF NOT EXISTS todo (
                            id    INTEGER DEFAULT nextval('todo_id_seq'),
                            title VARCHAR NOT NULL,
                            done  BOOLEAN NOT NULL DEFAULT false
                        )""");
            }
        });
    }

    public List<Todo> findAll() throws SQLException {
        return FIND_ALL.transact(tx);
    }

    public Todo create(String title) throws SQLException {
        return Fragment.of("INSERT INTO todo (title) VALUES (")
                .value(DuckDbTypes.varchar, title)
                .append(") RETURNING ").append(todoCodec.columnList())
                .query(todoCodec.exactlyOne())
                .transact(tx);
    }

    public void setDone(int id, boolean done) throws SQLException {
        Fragment.of("UPDATE todo SET done = ")
                .value(DuckDbTypes.boolean_, done)
                .append(" WHERE id = ").value(DuckDbTypes.integer, id)
                .execute()
                .transact(tx);
    }

    public void analyzeQueries() throws SQLException {
        tx.executeVoid(conn -> {
            var result = QueryAnalyzer.analyze(FIND_ALL.named("findAll"), conn).getFirst();
            if (result.succeeded()) {
                System.out.println("  All queries passed analysis.");
            } else {
                System.err.println(result.reportColored());
            }
        });
    }
}
