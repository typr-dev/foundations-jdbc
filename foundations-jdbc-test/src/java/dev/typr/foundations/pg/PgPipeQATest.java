package dev.typr.foundations.pg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import dev.typr.foundations.Containers;
import dev.typr.foundations.DatabaseException;
import dev.typr.foundations.DbType;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.OperationRead;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.Procedure;
import dev.typr.foundations.QueryChecker;
import dev.typr.foundations.QueryChecker.QueryCheckFailedException;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.connect.PgConfig;
import java.util.List;
import java.util.Optional;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class PgPipeQATest {

  private static PgPipelinePool pool;

  @BeforeClass
  public static void setup() {
    PgConfig pgConfig =
        PgConfig.builder(
                Containers.postgres().getHost(),
                Containers.postgres().getMappedPort(5432),
                Containers.postgres().getDatabaseName(),
                Containers.postgres().getUsername(),
                Containers.postgres().getPassword())
            .build();
    pool = PgPipelinePool.create(pgConfig, PgPipelineConfig.builder().connectionCount(4).build());
  }

  @AfterClass
  public static void teardown() {
    if (pool != null) {
      pool.close();
    }
  }

  // ========== Transaction modes ==========

  @Test
  public void testExecuteSingleQuery() {
    System.out.println("--- testExecuteSingleQuery ---");
    String table = "qa_exec_single_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int, name text)").execute());
    pool.execute(
        Fragment.of("INSERT INTO " + table + " VALUES (1, 'alice'), (2, 'bob'), (3, 'carol')")
            .update());

    List<String> names =
        pool.execute(
            Fragment.of("SELECT name FROM " + table + " ORDER BY id")
                .query(RowCodec.of(PgTypes.text).all()));
    System.out.println("names = " + names);
    assertEquals(List.of("alice", "bob", "carol"), names);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testReadonlyTransactMultiQuery() {
    System.out.println("--- testReadonlyTransactMultiQuery ---");
    String table = "qa_readonly_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
    pool.execute(Fragment.of("INSERT INTO " + table + " VALUES (10), (20), (30)").update());

    List<Integer> result =
        pool.transactRead(
            rc -> {
              List<Integer> ids =
                  rc.query(
                      Fragment.of("SELECT id FROM " + table + " ORDER BY id"),
                      RowCodec.of(PgTypes.int4));
              Integer sum =
                  rc.execute(
                      Fragment.of("SELECT sum(id)::int4 FROM " + table)
                          .query(RowCodec.of(PgTypes.int4).exactlyOne()));
              System.out.println("ids = " + ids + ", sum = " + sum);
              assertEquals(List.of(10, 20, 30), ids);
              assertEquals((Integer) 60, sum);
              return ids;
            });
    assertEquals(3, result.size());

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testTransactWriteAndRead() {
    System.out.println("--- testTransactWriteAndRead ---");
    String table = "qa_transact_wr_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int, name text)").execute());

    List<String> names =
        pool.transact(
            mc -> {
              mc.execute(
                  Fragment.of("INSERT INTO " + table + " (id, name) VALUES (")
                      .append(Fragment.encode(PgTypes.int4, 1))
                      .append(Fragment.of(", "))
                      .append(Fragment.encode(PgTypes.text, "hello"))
                      .append(Fragment.of(")"))
                      .update());
              mc.execute(
                  Fragment.of("INSERT INTO " + table + " (id, name) VALUES (")
                      .append(Fragment.encode(PgTypes.int4, 2))
                      .append(Fragment.of(", "))
                      .append(Fragment.encode(PgTypes.text, "world"))
                      .append(Fragment.of(")"))
                      .update());
              return mc.execute(
                  Fragment.of("SELECT name FROM " + table + " ORDER BY id")
                      .query(RowCodec.of(PgTypes.text).all()));
            });
    System.out.println("names = " + names);
    assertEquals(List.of("hello", "world"), names);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testTransactRollsBackOnException() {
    System.out.println("--- testTransactRollsBackOnException ---");
    String table = "qa_rollback_exc_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
    pool.execute(Fragment.of("INSERT INTO " + table + " VALUES (0)").update());

    try {
      pool.transact(
          mc -> {
            mc.execute(
                Fragment.of("INSERT INTO " + table + " VALUES (")
                    .append(Fragment.encode(PgTypes.int4, 99))
                    .append(Fragment.of(")"))
                    .update());
            throw new RuntimeException("deliberate failure");
          });
      fail("Should have thrown");
    } catch (RuntimeException e) {
      System.out.println("caught: " + e.getMessage());
      assertEquals("deliberate failure", e.getMessage());
    }

    List<Integer> ids =
        pool.query(
            Fragment.of("SELECT id FROM " + table + " ORDER BY id"), RowCodec.of(PgTypes.int4));
    System.out.println("ids after rollback = " + ids);
    assertEquals(List.of(0), ids);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testRollbackOnlyMode() {
    System.out.println("--- testRollbackOnlyMode ---");
    String table = "qa_rollback_only_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());

    pool.transactRollback(
        mc -> {
          mc.execute(
              Fragment.of("INSERT INTO " + table + " VALUES (")
                  .append(Fragment.encode(PgTypes.int4, 42))
                  .append(Fragment.of(")"))
                  .update());
          List<Integer> inside =
              mc.query(Fragment.of("SELECT id FROM " + table), RowCodec.of(PgTypes.int4));
          System.out.println("inside rollbackOnly tx = " + inside);
          assertEquals(List.of(42), inside);
          return null;
        });

    List<Integer> after =
        pool.query(Fragment.of("SELECT id FROM " + table), RowCodec.of(PgTypes.int4));
    System.out.println("after rollbackOnly = " + after);
    assertEquals(List.of(), after);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  // ========== Combine/fan-out ==========

  @Test
  public void testCombineTwoQueries() {
    System.out.println("--- testCombineTwoQueries ---");
    Operation<Integer> op1 = Fragment.of("SELECT 10").query(RowCodec.of(PgTypes.int4).exactlyOne());
    Operation<Integer> op2 = Fragment.of("SELECT 20").query(RowCodec.of(PgTypes.int4).exactlyOne());

    var result = pool.execute(op1.combine(op2));
    System.out.println("combined = (" + result._1() + ", " + result._2() + ")");
    assertEquals((Integer) 10, result._1());
    assertEquals((Integer) 20, result._2());
  }

  @Test
  public void testCombineThreeQueries() {
    System.out.println("--- testCombineThreeQueries ---");
    OperationRead<Integer> op1 =
        Fragment.of("SELECT 1").query(RowCodec.of(PgTypes.int4).exactlyOne());
    OperationRead<Integer> op2 =
        Fragment.of("SELECT 2").query(RowCodec.of(PgTypes.int4).exactlyOne());
    OperationRead<Integer> op3 =
        Fragment.of("SELECT 3").query(RowCodec.of(PgTypes.int4).exactlyOne());

    var combined12 = op1.combine(op2);
    var combined123 = combined12.combine(op3);

    var result = pool.transactRead(rc -> rc.execute(combined123));
    System.out.println(
        "three-way = (" + result._1()._1() + ", " + result._1()._2() + ", " + result._2() + ")");
    assertEquals((Integer) 1, result._1()._1());
    assertEquals((Integer) 2, result._1()._2());
    assertEquals((Integer) 3, result._2());
  }

  @Test
  public void testCombineWithMapping() {
    System.out.println("--- testCombineWithMapping ---");
    record Pair(int a, String b) {}
    Operation<Integer> op1 = Fragment.of("SELECT 42").query(RowCodec.of(PgTypes.int4).exactlyOne());
    Operation<String> op2 =
        Fragment.of("SELECT 'hello'").query(RowCodec.of(PgTypes.text).exactlyOne());

    Operation<Pair> combined = op1.combineWith(op2, Pair::new);
    Pair result = pool.execute(combined);
    System.out.println("combineWith = Pair(" + result.a() + ", " + result.b() + ")");
    assertEquals(42, result.a());
    assertEquals("hello", result.b());
  }

  // ========== Error handling ==========

  @Test
  public void testConstraintViolationProducesPostgresException() {
    System.out.println("--- testConstraintViolationProducesPostgresException ---");
    String table = "qa_constraint_" + System.nanoTime();
    pool.execute(
        Fragment.of("CREATE TABLE " + table + " (id int PRIMARY KEY, name text)").execute());
    pool.execute(Fragment.of("INSERT INTO " + table + " VALUES (1, 'first')").update());

    try {
      pool.execute(Fragment.of("INSERT INTO " + table + " VALUES (1, 'duplicate')").update());
      fail("Should have thrown");
    } catch (DatabaseException.Postgres pg) {
      System.out.println("sqlState = " + pg.pgError().sqlState());
      System.out.println("constraintName = " + pg.pgError().constraintName());
      System.out.println("tableName = " + pg.pgError().tableName());
      System.out.println("detail = " + pg.pgError().detail());
      assertEquals("23505", pg.pgError().sqlState());
      assertNotNull(pg.pgError().constraintName());
      assertEquals(table, pg.pgError().tableName());
      assertNotNull(pg.pgError().detail());
      assertTrue(pg.pgError().detail().contains("1"));
    }

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testSyntaxErrorWithPositionCaret() {
    System.out.println("--- testSyntaxErrorWithPositionCaret ---");
    try {
      pool.execute(Fragment.of("SELEC id FROM users WHERE age > 30").update());
      fail("Should have thrown");
    } catch (DatabaseException.Postgres pg) {
      System.out.println("position = " + pg.pgError().position());
      System.out.println("formatted = " + pg.getMessage());
      assertNotNull(pg.pgError().position());
      assertTrue(pg.getMessage().contains("\u2514"));
    }
  }

  @Test
  public void testSealedExceptionPatternMatch() {
    System.out.println("--- testSealedExceptionPatternMatch ---");
    try {
      pool.execute(Fragment.of("SELECT * FROM nonexistent_table_xyz_999").update());
      fail("Should have thrown");
    } catch (DatabaseException e) {
      String result =
          switch (e) {
            case DatabaseException.Postgres pg -> "postgres:" + pg.pgError().sqlState();
            case DatabaseException.SqlServer ss -> "sqlserver:" + ss.sqlServerError();
            case DatabaseException.Jdbc jdbc -> "jdbc:" + jdbc.sqlException().getSQLState();
          };
      System.out.println("pattern match = " + result);
      assertTrue(result.startsWith("postgres:"));
      assertTrue(result.contains("42P01"));
    }
  }

  // ========== simpleExecute ==========

  @Test
  public void testSimpleExecuteMultiStatement() {
    System.out.println("--- testSimpleExecuteMultiStatement ---");
    String tableA = "qa_simple_a_" + System.nanoTime();
    String tableB = "qa_simple_b_" + System.nanoTime();

    pool.simpleExecute(
        "CREATE TABLE " + tableA + " (id int); CREATE TABLE " + tableB + " (id int)");

    pool.execute(Fragment.of("INSERT INTO " + tableA + " VALUES (1)").update());
    pool.execute(Fragment.of("INSERT INTO " + tableB + " VALUES (2)").update());

    List<Integer> idsA =
        pool.query(Fragment.of("SELECT id FROM " + tableA), RowCodec.of(PgTypes.int4));
    List<Integer> idsB =
        pool.query(Fragment.of("SELECT id FROM " + tableB), RowCodec.of(PgTypes.int4));
    System.out.println("tableA = " + idsA + ", tableB = " + idsB);
    assertEquals(List.of(1), idsA);
    assertEquals(List.of(2), idsB);

    pool.simpleExecute("DROP TABLE " + tableA + "; DROP TABLE " + tableB);
  }

  @Test
  public void testSimpleExecuteDDL() {
    System.out.println("--- testSimpleExecuteDDL ---");
    String table = "qa_simple_ddl_" + System.nanoTime();

    pool.simpleExecute(
        "CREATE TABLE "
            + table
            + " (id int, name text); "
            + "INSERT INTO "
            + table
            + " VALUES (1, 'hello')");

    List<String> names =
        pool.query(Fragment.of("SELECT name FROM " + table), RowCodec.of(PgTypes.text));
    System.out.println("names = " + names);
    assertEquals(List.of("hello"), names);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  // ========== QueryChecker via PgPipe ==========

  @Test
  public void testPgPipeQueryCheckerPassesValidQuery() {
    System.out.println("--- testPgPipeQueryCheckerPassesValidQuery ---");
    String table = "qa_checker_valid_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int, name text)").execute());

    QueryChecker checker = PgPipeQueryChecker.create(pool);
    Operation<List<String>> op =
        Fragment.of("SELECT name FROM " + table + " WHERE id = ")
            .append(Fragment.encode(PgTypes.int4, 1))
            .query(RowCodec.of(PgTypes.text).all());
    checker.check(op);
    System.out.println("valid query check passed");

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testPgPipeQueryCheckerFailsTypeMismatch() {
    System.out.println("--- testPgPipeQueryCheckerFailsTypeMismatch ---");
    String table = "qa_checker_mismatch_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int, name text)").execute());

    QueryChecker checker = PgPipeQueryChecker.create(pool);
    Operation<List<Integer>> op =
        Fragment.of("SELECT name FROM " + table).query(RowCodec.of(PgTypes.int4).all());

    try {
      checker.check(op);
      fail("Should have thrown QueryCheckFailedException");
    } catch (QueryCheckFailedException e) {
      System.out.println(
          "type mismatch detected: "
              + e.getMessage().substring(0, Math.min(200, e.getMessage().length())));
      assertTrue(e.getMessage().contains("check failed"));
    }

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testPgPipeQueryCheckerFunction() {
    System.out.println("--- testPgPipeQueryCheckerFunction ---");
    pool.simpleExecute(
        "CREATE OR REPLACE FUNCTION qa_add_ints(a int, b int) RETURNS int AS $$ "
            + "SELECT a + b $$ LANGUAGE sql");

    QueryChecker checker = PgPipeQueryChecker.create(pool);
    Procedure<Integer> func =
        Procedure.buildFunction(
            "qa_add_ints",
            List.of(
                dev.typr.foundations.ParamDef.input(PgTypes.int4),
                dev.typr.foundations.ParamDef.input(PgTypes.int4)),
            PgTypes.int4);
    checker.checkRoutine(func);
    System.out.println("function check passed for qa_add_ints");

    pool.simpleExecute("DROP FUNCTION qa_add_ints(int, int)");
  }

  // ========== Redacted types ==========

  @Test
  public void testRedactedTypeHidesInInterpolation() {
    System.out.println("--- testRedactedTypeHidesInInterpolation ---");
    DbType<String> redactedText = PgTypes.text.redacted();
    Fragment frag =
        Fragment.of("INSERT INTO users (pass) VALUES (")
            .append(Fragment.encode(redactedText, "s3cr3t"))
            .append(Fragment.of(")"));

    String interpolated = frag.renderInterpolated();
    System.out.println("interpolated = " + interpolated);
    assertTrue(interpolated.contains("<redacted>"));
    assertTrue(!interpolated.contains("s3cr3t"));
  }

  @Test
  public void testRedactedTypeStillWritesToDb() {
    System.out.println("--- testRedactedTypeStillWritesToDb ---");
    String table = "qa_redacted_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (secret text)").execute());

    DbType<String> redactedText = PgTypes.text.redacted();
    pool.execute(
        Fragment.of("INSERT INTO " + table + " VALUES (")
            .append(Fragment.encode(redactedText, "topsecret123"))
            .append(Fragment.of(")"))
            .update());

    List<String> rows =
        pool.query(Fragment.of("SELECT secret FROM " + table), RowCodec.of(PgTypes.text));
    System.out.println("read back = " + rows);
    assertEquals(List.of("topsecret123"), rows);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  // ========== Convenience methods ==========

  @Test
  public void testQueryConvenience() {
    System.out.println("--- testQueryConvenience ---");
    String table = "qa_query_conv_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
    pool.execute(Fragment.of("INSERT INTO " + table + " VALUES (1), (2), (3)").update());

    List<Integer> ids =
        pool.query(
            Fragment.of("SELECT id FROM " + table + " ORDER BY id"), RowCodec.of(PgTypes.int4));
    System.out.println("query convenience = " + ids);
    assertEquals(List.of(1, 2, 3), ids);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testQueryFirstConvenience() {
    System.out.println("--- testQueryFirstConvenience ---");
    String table = "qa_queryfirst_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
    pool.execute(Fragment.of("INSERT INTO " + table + " VALUES (42), (99)").update());

    Optional<Integer> first =
        pool.queryFirst(
            Fragment.of("SELECT id FROM " + table + " ORDER BY id"), RowCodec.of(PgTypes.int4));
    System.out.println("queryFirst = " + first);
    assertEquals(Optional.of(42), first);

    Optional<Integer> empty =
        pool.queryFirst(
            Fragment.of("SELECT id FROM " + table + " WHERE id = 999"), RowCodec.of(PgTypes.int4));
    System.out.println("queryFirst empty = " + empty);
    assertEquals(Optional.empty(), empty);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testUpdateConvenience() {
    System.out.println("--- testUpdateConvenience ---");
    String table = "qa_update_conv_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
    pool.execute(Fragment.of("INSERT INTO " + table + " VALUES (1), (2), (3)").update());

    int count = pool.update(Fragment.of("DELETE FROM " + table + " WHERE id > 1"));
    System.out.println("update count = " + count);
    assertEquals(2, count);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  // ========== Pool metrics ==========

  @Test
  public void testPoolMetrics() {
    System.out.println("--- testPoolMetrics ---");
    int connCount = pool.connectionCount();
    int activeCount = pool.activeConnectionCount();
    int idleCount = pool.idleConnectionCount();

    System.out.println("connectionCount = " + connCount);
    System.out.println("activeConnectionCount = " + activeCount);
    System.out.println("idleConnectionCount = " + idleCount);

    assertTrue(connCount > 0);
    assertTrue(activeCount >= 0);
    assertTrue(idleCount >= 0);
    assertTrue(idleCount <= connCount);
    assertEquals(4, connCount);
  }

  // ========== Edge cases ==========

  @Test
  public void testNullValues() {
    System.out.println("--- testNullValues ---");
    String table = "qa_nulls_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int, name text)").execute());

    pool.execute(
        Fragment.of("INSERT INTO " + table + " VALUES (")
            .append(Fragment.encode(PgTypes.int4, 1))
            .append(Fragment.of(", "))
            .append(Fragment.encode(PgTypes.text.opt(), Optional.empty()))
            .append(Fragment.of(")"))
            .update());

    pool.execute(
        Fragment.of("INSERT INTO " + table + " VALUES (")
            .append(Fragment.encode(PgTypes.int4, 2))
            .append(Fragment.of(", "))
            .append(Fragment.encode(PgTypes.text.opt(), Optional.of("present")))
            .append(Fragment.of(")"))
            .update());

    RowCodec<Optional<String>> codec = RowCodec.of(PgTypes.text.opt());
    List<Optional<String>> results =
        pool.query(Fragment.of("SELECT name FROM " + table + " ORDER BY id"), codec);
    System.out.println("nulls = " + results);
    assertEquals(2, results.size());
    assertEquals(Optional.empty(), results.get(0));
    assertEquals(Optional.of("present"), results.get(1));

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testLargeResultSet() {
    System.out.println("--- testLargeResultSet ---");
    String table = "qa_large_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());

    pool.execute(
        Fragment.of("INSERT INTO " + table + " SELECT generate_series(1, 10000)").update());

    List<Integer> ids =
        pool.query(
            Fragment.of("SELECT id FROM " + table + " ORDER BY id"), RowCodec.of(PgTypes.int4));
    System.out.println("large result size = " + ids.size());
    assertEquals(10000, ids.size());
    assertEquals((Integer) 1, ids.get(0));
    assertEquals((Integer) 10000, ids.get(9999));

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }

  @Test
  public void testEmptyResultSet() {
    System.out.println("--- testEmptyResultSet ---");
    String table = "qa_empty_" + System.nanoTime();
    pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());

    List<Integer> ids =
        pool.query(
            Fragment.of("SELECT id FROM " + table + " WHERE false"), RowCodec.of(PgTypes.int4));
    System.out.println("empty result = " + ids);
    assertEquals(List.of(), ids);

    pool.execute(Fragment.of("DROP TABLE " + table).execute());
  }
}
