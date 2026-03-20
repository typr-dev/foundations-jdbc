package dev.typr.foundations.example;

import dev.typr.foundations.*;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TodoRepository {

  public record Todo(Integer id, String title, Boolean done) {}

  private static final RowCodecNamed<Todo> todoCodec =
      RowCodec.<Todo>namedBuilder()
          .field("id", DuckDbTypes.integer, Todo::id)
          .field("title", DuckDbTypes.varchar, Todo::title)
          .field("done", DuckDbTypes.boolean_, Todo::done)
          .build(Todo::new);

  static final Operation<List<Todo>> selectAll =
      Fragment.of("SELECT ")
          .append(todoCodec.columnList())
          .append(" FROM todo ORDER BY id")
          .query(todoCodec.all());

  static final Template<String, Todo> insertByTitle =
      Fragment.of("INSERT INTO todo (title) VALUES (")
          .param(DuckDbTypes.varchar)
          .append(Fragment.of(") RETURNING ").append(todoCodec.columnList()))
          .query(todoCodec.exactlyOne());

  static final Template<Integer, Integer> setDoneById =
      Fragment.of("UPDATE todo SET done = true WHERE id = ").param(DuckDbTypes.integer).update();

  private final Transactor tx;

  public TodoRepository(Transactor tx) {
    this.tx = tx;
  }

  public void createSchema() {
    tx.executeVoid(
        conn -> {
          Fragment.of("CREATE SEQUENCE IF NOT EXISTS todo_id_seq START 1").execute().run(conn);
          Fragment.of(
                  """
                  CREATE TABLE IF NOT EXISTS todo (
                      id    INTEGER DEFAULT nextval('todo_id_seq'),
                      title VARCHAR NOT NULL,
                      done  BOOLEAN NOT NULL DEFAULT false
                  )\
                  """)
              .execute()
              .run(conn);
        });
  }

  public List<Todo> findAll() {
    return selectAll.transact(tx);
  }

  public Todo create(String title) {
    return insertByTitle.on(title).transact(tx);
  }

  public void setDone(int id) {
    setDoneById.on(id).transact(tx);
  }

  @Transactional
  public Todo createAndComplete(String title) {
    var todo = insertByTitle.on(title).transact(tx);
    setDoneById.on(todo.id()).transact(tx);
    return todo;
  }
}
