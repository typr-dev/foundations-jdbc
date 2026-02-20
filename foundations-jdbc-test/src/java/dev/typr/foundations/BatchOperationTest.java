package dev.typr.foundations;

import static org.junit.Assert.*;

import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class BatchOperationTest {

  record Item(String name, int quantity) {}

  record IdItem(int id, String name, int quantity) {}

  private static final AtomicInteger seq = new AtomicInteger(0);

  private static String t() {
    return "bt_" + seq.incrementAndGet();
  }

  // ==================== DuckDB ====================

  @Test
  public void duckdb() throws Exception {
    try (Connection conn = DriverManager.getConnection("jdbc:duckdb:")) {
      var ip =
          RowCodec.<Item>namedBuilder()
              .field("name", DuckDbTypes.varchar, Item::name)
              .field("quantity", DuckDbTypes.integer, Item::quantity)
              .build(Item::new);
      var pp =
          RowCodec.<IdItem>namedBuilder()
              .field("id", DuckDbTypes.integer, IdItem::id)
              .field("name", DuckDbTypes.varchar, IdItem::name)
              .field("quantity", DuckDbTypes.integer, IdItem::quantity)
              .build(IdItem::new);

      // updateMany
      String t1 = t();
      conn.createStatement()
          .execute("CREATE TABLE " + t1 + " (name VARCHAR, quantity INTEGER)");
      assertBatchInsert(conn, ip, t1, 3);

      // onMany — all columns
      String t2 = t();
      conn.createStatement()
          .execute("CREATE TABLE " + t2 + " (id INTEGER, name VARCHAR, quantity INTEGER)");
      assertOnMany(conn, pp, t2, 3);

      // onMany — skip id (sequence-generated)
      String t3 = t();
      conn.createStatement().execute("CREATE SEQUENCE " + t3 + "_seq START 1");
      conn.createStatement()
          .execute(
              "CREATE TABLE "
                  + t3
                  + " (id INTEGER DEFAULT nextval('"
                  + t3
                  + "_seq'), name VARCHAR, quantity INTEGER)");
      assertOnManySkipId(conn, pp, t3);

      // updateReturningEach
      String t4 = t();
      conn.createStatement()
          .execute("CREATE TABLE " + t4 + " (name VARCHAR, quantity INTEGER)");
      assertReturningEach(conn, ip, t4);

      // single .on() then .onMany() on same template
      String t5 = t();
      conn.createStatement()
          .execute("CREATE TABLE " + t5 + " (id INTEGER, name VARCHAR, quantity INTEGER)");
      assertSingleThenBatch(conn, pp, t5);

      // empty batch
      String t6 = t();
      conn.createStatement()
          .execute("CREATE TABLE " + t6 + " (name VARCHAR, quantity INTEGER)");
      int[] empty =
          Fragment.of("INSERT INTO " + t6 + " (name, quantity) VALUES (?, ?)")
              .updateMany(ip, Collections.<Item>emptyIterator())
              .runChecked(conn);
      assertEquals(0, empty.length);
    }
  }

  // ==================== PostgreSQL ====================

  @Test
  public void postgres() throws Exception {
    Containers.postgresTransactor()
        .execute(
            conn -> {
              var ip =
                  RowCodec.<Item>namedBuilder()
                      .field("name", PgTypes.text, Item::name)
                      .field("quantity", PgTypes.int4, Item::quantity)
                      .build(Item::new);
              var pp =
                  RowCodec.<IdItem>namedBuilder()
                      .field("id", PgTypes.int4, IdItem::id)
                      .field("name", PgTypes.text, IdItem::name)
                      .field("quantity", PgTypes.int4, IdItem::quantity)
                      .build(IdItem::new);

              String t1 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t1 + " (name TEXT, quantity INTEGER)");
              assertBatchInsert(conn, ip, t1, 3);

              String t2 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t2 + " (id INTEGER, name TEXT, quantity INTEGER)");
              assertOnMany(conn, pp, t2, 3);

              String t3 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE "
                          + t3
                          + " (id SERIAL, name TEXT, quantity INTEGER)");
              assertOnManySkipId(conn, pp, t3);

              String t4 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t4 + " (name TEXT, quantity INTEGER)");
              assertReturningEach(conn, ip, t4);

              String t5 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t5 + " (id INTEGER, name TEXT, quantity INTEGER)");
              assertSingleThenBatch(conn, pp, t5);

              return null;
            });
  }

  // ==================== MariaDB ====================

  @Test
  public void mariadb() throws Exception {
    Containers.mariadbTransactor()
        .execute(
            conn -> {
              var ip =
                  RowCodec.<Item>namedBuilder()
                      .field("name", MariaTypes.varchar, Item::name)
                      .field("quantity", MariaTypes.int_, Item::quantity)
                      .build(Item::new);
              var pp =
                  RowCodec.<IdItem>namedBuilder()
                      .field("id", MariaTypes.int_, IdItem::id)
                      .field("name", MariaTypes.varchar, IdItem::name)
                      .field("quantity", MariaTypes.int_, IdItem::quantity)
                      .build(IdItem::new);

              String t1 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t1 + " (name VARCHAR(200), quantity INT)");
              assertBatchInsert(conn, ip, t1, 3);

              String t2 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t2 + " (id INT, name VARCHAR(200), quantity INT)");
              assertOnMany(conn, pp, t2, 3);

              String t3 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE "
                          + t3
                          + " (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(200), quantity INT)");
              assertOnManySkipId(conn, pp, t3);

              String t4 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t4 + " (name VARCHAR(200), quantity INT)");
              assertReturningEach(conn, ip, t4);

              String t5 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t5 + " (id INT, name VARCHAR(200), quantity INT)");
              assertSingleThenBatch(conn, pp, t5);

              return null;
            });
  }

  // ==================== SQL Server ====================

  @Test
  public void sqlserver() throws Exception {
    Containers.sqlserverTransactor()
        .execute(
            conn -> {
              var ip =
                  RowCodec.<Item>namedBuilder()
                      .field("name", SqlServerTypes.varchar, Item::name)
                      .field("quantity", SqlServerTypes.int_, Item::quantity)
                      .build(Item::new);
              var pp =
                  RowCodec.<IdItem>namedBuilder()
                      .field("id", SqlServerTypes.int_, IdItem::id)
                      .field("name", SqlServerTypes.varchar, IdItem::name)
                      .field("quantity", SqlServerTypes.int_, IdItem::quantity)
                      .build(IdItem::new);

              String t1 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t1 + " (name VARCHAR(200), quantity INT)");
              assertBatchInsert(conn, ip, t1, 3);

              String t2 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t2 + " (id INT, name VARCHAR(200), quantity INT)");
              assertOnMany(conn, pp, t2, 3);

              String t3 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE "
                          + t3
                          + " (id INT IDENTITY(1,1), name VARCHAR(200), quantity INT)");
              assertOnManySkipId(conn, pp, t3);

              String t4 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t4 + " (id INT, name VARCHAR(200), quantity INT)");
              assertSingleThenBatch(conn, pp, t4);

              return null;
            });
  }

  // ==================== Oracle ====================

  @Test
  public void oracle() throws Exception {
    Containers.oracleTransactor()
        .execute(
            conn -> {
              var ip =
                  RowCodec.<Item>namedBuilder()
                      .field("name", OracleTypes.varchar2, Item::name)
                      .field("quantity", OracleTypes.numberInt, Item::quantity)
                      .build(Item::new);
              var pp =
                  RowCodec.<IdItem>namedBuilder()
                      .field("id", OracleTypes.numberInt, IdItem::id)
                      .field("name", OracleTypes.varchar2, IdItem::name)
                      .field("quantity", OracleTypes.numberInt, IdItem::quantity)
                      .build(IdItem::new);

              String t1 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE " + t1 + " (name VARCHAR2(200), quantity NUMBER(10))");
              assertBatchInsert(conn, ip, t1, 3);

              String t2 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE "
                          + t2
                          + " (id NUMBER(10), name VARCHAR2(200), quantity NUMBER(10))");
              assertOnMany(conn, pp, t2, 3);

              String t3 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE "
                          + t3
                          + " (id NUMBER(10) GENERATED ALWAYS AS IDENTITY, name VARCHAR2(200), quantity NUMBER(10))");
              assertOnManySkipId(conn, pp, t3);

              String t4 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE "
                          + t4
                          + " (id NUMBER(10), name VARCHAR2(200), quantity NUMBER(10))");
              assertSingleThenBatch(conn, pp, t4);

              return null;
            });
  }

  // ==================== DB2 ====================

  @Test
  public void db2() throws Exception {
    Containers.db2Transactor()
        .execute(
            conn -> {
              var ip =
                  RowCodec.<Item>namedBuilder()
                      .field("name", Db2Types.varchar, Item::name)
                      .field("quantity", Db2Types.integer, Item::quantity)
                      .build(Item::new);
              var pp =
                  RowCodec.<IdItem>namedBuilder()
                      .field("id", Db2Types.integer, IdItem::id)
                      .field("name", Db2Types.varchar, IdItem::name)
                      .field("quantity", Db2Types.integer, IdItem::quantity)
                      .build(IdItem::new);

              String t1 = t();
              conn.createStatement()
                  .execute("CREATE TABLE " + t1 + " (name VARCHAR(200), quantity INTEGER)");
              assertBatchInsert(conn, ip, t1, 3);

              String t2 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE "
                          + t2
                          + " (id INTEGER, name VARCHAR(200), quantity INTEGER)");
              assertOnMany(conn, pp, t2, 3);

              String t3 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE "
                          + t3
                          + " (id INTEGER GENERATED ALWAYS AS IDENTITY, name VARCHAR(200), quantity INTEGER)");
              assertOnManySkipId(conn, pp, t3);

              String t4 = t();
              conn.createStatement()
                  .execute(
                      "CREATE TABLE "
                          + t4
                          + " (id INTEGER, name VARCHAR(200), quantity INTEGER)");
              assertSingleThenBatch(conn, pp, t4);

              return null;
            });
  }

  // ==================== Shared assertion helpers ====================

  private static final List<Item> ITEMS =
      List.of(new Item("apple", 10), new Item("banana", 20), new Item("cherry", 30));

  private static final List<IdItem> ID_ITEMS =
      List.of(new IdItem(1, "widget", 100), new IdItem(2, "gadget", 200), new IdItem(3, "doohickey", 300));

  static void assertBatchInsert(
      Connection conn, RowCodecNamed<Item> parser, String table, int expectedCount)
      throws SQLException {
    Fragment insert =
        Fragment.of("INSERT INTO " + table + " (name, quantity) VALUES (?, ?)");
    int[] counts = insert.updateMany(parser, ITEMS.iterator()).runChecked(conn);
    assertEquals(expectedCount, counts.length);

    List<Item> result =
        Fragment.of("SELECT name, quantity FROM " + table + " ORDER BY name")
            .query(parser.all())
            .runChecked(conn);
    assertEquals(expectedCount, result.size());
    assertEquals("apple", result.get(0).name());
    assertEquals(10, result.get(0).quantity());
    assertEquals("banana", result.get(1).name());
    assertEquals(20, result.get(1).quantity());
    assertEquals("cherry", result.get(2).name());
    assertEquals(30, result.get(2).quantity());
  }

  static void assertOnMany(
      Connection conn, RowCodecNamed<IdItem> parser, String table, int expectedCount)
      throws SQLException {
    RowTemplate.Update<IdItem> template =
        Fragment.of("INSERT INTO " + table + " (")
            .append(parser.columnList())
            .append(") VALUES (")
            .paramRow(parser)
            .append(")")
            .update();

    int[] counts = template.onMany(ID_ITEMS.iterator()).runChecked(conn);
    assertEquals(expectedCount, counts.length);

    List<IdItem> result =
        Fragment.of("SELECT id, name, quantity FROM " + table + " ORDER BY id")
            .query(parser.all())
            .runChecked(conn);
    assertEquals(expectedCount, result.size());
    assertEquals(1, result.get(0).id());
    assertEquals("widget", result.get(0).name());
    assertEquals(100, result.get(0).quantity());
    assertEquals(2, result.get(1).id());
    assertEquals("gadget", result.get(1).name());
    assertEquals(3, result.get(2).id());
    assertEquals("doohickey", result.get(2).name());
  }

  static void assertOnManySkipId(
      Connection conn, RowCodecNamed<IdItem> parser, String table) throws SQLException {
    RowTemplate.Update<IdItem> template =
        Fragment.of("INSERT INTO " + table + " (name, quantity) VALUES (")
            .paramRow(parser, "id")
            .append(")")
            .update();

    var items =
        List.of(
            new IdItem(0, "widget", 100),
            new IdItem(0, "gadget", 200),
            new IdItem(0, "doohickey", 300));
    int[] counts = template.onMany(items.iterator()).runChecked(conn);
    assertEquals(3, counts.length);

    List<IdItem> result =
        Fragment.of("SELECT id, name, quantity FROM " + table + " ORDER BY id")
            .query(parser.all())
            .runChecked(conn);
    assertEquals(3, result.size());
    assertEquals(1, result.get(0).id());
    assertEquals("widget", result.get(0).name());
    assertEquals(2, result.get(1).id());
    assertEquals("gadget", result.get(1).name());
    assertEquals(3, result.get(2).id());
    assertEquals("doohickey", result.get(2).name());
  }

  static void assertReturningEach(
      Connection conn, RowCodecNamed<Item> parser, String table) throws SQLException {
    Fragment insert =
        Fragment.of(
            "INSERT INTO "
                + table
                + " (name, quantity) VALUES (?, ?) RETURNING name, quantity");
    List<Item> returned =
        insert.updateReturningEach(parser, ITEMS.iterator()).runChecked(conn);
    assertEquals(3, returned.size());
    assertEquals("apple", returned.get(0).name());
    assertEquals(10, returned.get(0).quantity());
    assertEquals("banana", returned.get(1).name());
    assertEquals(20, returned.get(1).quantity());
    assertEquals("cherry", returned.get(2).name());
    assertEquals(30, returned.get(2).quantity());
  }

  static void assertSingleThenBatch(
      Connection conn, RowCodecNamed<IdItem> parser, String table) throws SQLException {
    RowTemplate.Update<IdItem> template =
        Fragment.of("INSERT INTO " + table + " (")
            .append(parser.columnList())
            .append(") VALUES (")
            .paramRow(parser)
            .append(")")
            .update();

    template.on(new IdItem(1, "first", 10)).runChecked(conn);

    var batch = List.of(new IdItem(2, "second", 20), new IdItem(3, "third", 30));
    template.onMany(batch.iterator()).runChecked(conn);

    List<IdItem> result =
        Fragment.of("SELECT id, name, quantity FROM " + table + " ORDER BY id")
            .query(parser.all())
            .runChecked(conn);
    assertEquals(3, result.size());
    assertEquals("first", result.get(0).name());
    assertEquals("second", result.get(1).name());
    assertEquals("third", result.get(2).name());
  }
}
