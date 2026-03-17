package dev.typr.foundations;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;

public class StreamingQueryTest {

  record Item(int id, String name) {}

  static final RowCodec<Item> itemCodec =
      RowCodec.<Item>builder()
          .field(DuckDbTypes.integer, Item::id)
          .field(DuckDbTypes.varchar, Item::name)
          .build(Item::new);

  static <T> T withConnection(SqlFunction<Connection, T> f) {
    try (var conn = DriverManager.getConnection("jdbc:duckdb:")) {
      conn.setAutoCommit(false);
      try {
        return f.apply(conn);
      } finally {
        conn.rollback();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void createAndPopulate(Connection conn, String table, int count) throws SQLException {
    conn.createStatement().execute("CREATE TABLE " + table + " (id INTEGER, name VARCHAR)");
    for (int i = 1; i <= count; i++) {
      conn.createStatement()
          .execute("INSERT INTO " + table + " VALUES (" + i + ", 'item" + i + "')");
    }
  }

  @Test
  public void testBasicCursorIteration() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t1", 3);
          var streaming =
              Fragment.of("SELECT id, name FROM t1 ORDER BY id").streamingQuery(itemCodec, 100);
          var cursor = streaming.run(conn);
          List<Item> items = new ArrayList<>();
          while (cursor.hasNext()) {
            items.add(cursor.next());
          }
          assertEquals(3, items.size());
          assertEquals(new Item(1, "item1"), items.get(0));
          assertEquals(new Item(2, "item2"), items.get(1));
          assertEquals(new Item(3, "item3"), items.get(2));
          return null;
        });
  }

  @Test
  public void testToList() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t2", 5);
          var streaming =
              Fragment.of("SELECT id, name FROM t2 ORDER BY id").streamingQuery(itemCodec, 100);
          var cursor = streaming.run(conn);
          List<Item> items = cursor.toList();
          assertEquals(5, items.size());
          assertEquals(new Item(1, "item1"), items.get(0));
          assertEquals(new Item(5, "item5"), items.get(4));
          return null;
        });
  }

  @Test
  public void testForEach() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t3", 4);
          var streaming =
              Fragment.of("SELECT id, name FROM t3 ORDER BY id").streamingQuery(itemCodec, 100);
          var cursor = streaming.run(conn);
          List<String> names = new ArrayList<>();
          cursor.forEach(item -> names.add(item.name()));
          assertEquals(List.of("item1", "item2", "item3", "item4"), names);
          return null;
        });
  }

  @Test
  public void testEnhancedForLoop() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t4", 3);
          var streaming =
              Fragment.of("SELECT id, name FROM t4 ORDER BY id").streamingQuery(itemCodec, 100);
          var cursor = streaming.run(conn);
          List<Item> items = new ArrayList<>();
          for (var item : cursor) {
            items.add(item);
          }
          assertEquals(3, items.size());
          return null;
        });
  }

  @Test
  public void testEmptyResultSet() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t5", 0);
          var streaming =
              Fragment.of("SELECT id, name FROM t5 ORDER BY id").streamingQuery(itemCodec, 100);
          var cursor = streaming.run(conn);
          assertFalse(cursor.hasNext());
          assertTrue(cursor.toList().isEmpty());
          return null;
        });
  }

  @Test
  public void testNoSuchElementOnExhaustedCursor() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t6", 1);
          var streaming =
              Fragment.of("SELECT id, name FROM t6 ORDER BY id").streamingQuery(itemCodec, 100);
          var cursor = streaming.run(conn);
          cursor.next();
          assertFalse(cursor.hasNext());
          assertThrows(NoSuchElementException.class, cursor::next);
          return null;
        });
  }

  @Test
  public void testMapWithToList() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t7", 3);
          var streaming =
              Fragment.of("SELECT id, name FROM t7 ORDER BY id").streamingQuery(itemCodec, 100);
          List<Item> result = streaming.map(Cursor::toList).run(conn);
          assertEquals(3, result.size());
          assertEquals(new Item(1, "item1"), result.get(0));
          return null;
        });
  }

  @Test
  public void testMapWithProcessing() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t8", 5);
          var streaming =
              Fragment.of("SELECT id, name FROM t8 ORDER BY id").streamingQuery(itemCodec, 100);
          long count =
              streaming
                  .map(
                      cursor -> {
                        long n = 0;
                        for (var item : cursor) {
                          n++;
                        }
                        return n;
                      })
                  .run(conn);
          assertEquals(5L, count);
          return null;
        });
  }

  @Test
  public void testCombineTwoStreamingOperations() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t9a", 2);
          conn.createStatement().execute("CREATE TABLE t9b (id INTEGER, name VARCHAR)");
          conn.createStatement().execute("INSERT INTO t9b VALUES (10, 'other1')");

          var s1 =
              Fragment.of("SELECT id, name FROM t9a ORDER BY id").streamingQuery(itemCodec, 100);
          var s2 =
              Fragment.of("SELECT id, name FROM t9b ORDER BY id").streamingQuery(itemCodec, 100);

          var combined =
              s1.combine(s2)
                  .map(
                      t -> {
                        List<Item> all = new ArrayList<>();
                        all.addAll(t._1().toList());
                        all.addAll(t._2().toList());
                        return all;
                      });

          List<Item> result = combined.run(conn);
          assertEquals(3, result.size());
          assertEquals(new Item(1, "item1"), result.get(0));
          assertEquals(new Item(2, "item2"), result.get(1));
          assertEquals(new Item(10, "other1"), result.get(2));
          return null;
        });
  }

  @Test
  public void testCombineStreamingWithNonStreaming() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t10", 3);

          var streaming =
              Fragment.of("SELECT id, name FROM t10 ORDER BY id").streamingQuery(itemCodec, 100);
          var countOp =
              Fragment.of("SELECT count(*)::INTEGER FROM t10").queryExactlyOne(DuckDbTypes.integer);

          var combined = streaming.map(Cursor::toList).combine(countOp);
          var result = combined.run(conn);
          assertEquals(3, result._1().size());
          assertEquals(Integer.valueOf(3), result._2());
          return null;
        });
  }

  @Test
  public void testStreamingQueryWithSingleColumnType() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t11", 3);

          var streaming =
              Fragment.of("SELECT name FROM t11 ORDER BY id")
                  .streamingQuery(DuckDbTypes.varchar, 100);
          List<String> names = streaming.map(Cursor::toList).run(conn);
          assertEquals(List.of("item1", "item2", "item3"), names);
          return null;
        });
  }

  @Test
  public void testCursorCloseIsIdempotent() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t12", 2);
          var streaming =
              Fragment.of("SELECT id, name FROM t12 ORDER BY id").streamingQuery(itemCodec, 100);
          var cursor = streaming.run(conn);
          cursor.next();
          cursor.close();
          cursor.close();
          assertFalse(cursor.hasNext());
          return null;
        });
  }

  @Test
  public void testTransactComposition() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t13", 3);
          var streaming =
              Fragment.of("SELECT id, name FROM t13 ORDER BY id").streamingQuery(itemCodec, 100);
          List<Item> result = streaming.map(Cursor::toList).run(conn);
          assertEquals(3, result.size());
          assertEquals(new Item(1, "item1"), result.get(0));
          assertEquals(new Item(3, "item3"), result.get(2));
          return null;
        });
  }

  @Test
  public void testCursorExplicitClose() {
    withConnection(
        conn -> {
          createAndPopulate(conn, "t14", 5);
          var streaming =
              Fragment.of("SELECT id, name FROM t14 ORDER BY id").streamingQuery(itemCodec, 100);
          var cursor = streaming.run(conn);
          assertEquals(new Item(1, "item1"), cursor.next());
          assertEquals(new Item(2, "item2"), cursor.next());
          cursor.close();
          assertFalse(cursor.hasNext());
          cursor.close();
          return null;
        });
  }
}
