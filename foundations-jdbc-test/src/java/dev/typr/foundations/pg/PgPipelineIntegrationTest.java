package dev.typr.foundations.pg;

import dev.typr.foundations.Containers;
import dev.typr.foundations.Cursor;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.OperationRead;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.connect.PgConfig;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Test;

public class PgPipelineIntegrationTest {

  private static PgConfig pgConfig() {
    return PgConfig.builder(
            Containers.postgres().getHost(),
            Containers.postgres().getMappedPort(5432),
            Containers.postgres().getDatabaseName(),
            Containers.postgres().getUsername(),
            Containers.postgres().getPassword())
        .build();
  }

  private static PgPipelinePool createPool(PgPipelineConfig config) {
    return PgPipelinePool.create(pgConfig(), config);
  }

  private static PgPipelinePool createPool() {
    return createPool(PgPipelineConfig.builder().connectionCount(2).build());
  }

  private static Fragment insertInto(String table, int id) {
    return Fragment.of("INSERT INTO " + table + " (id) VALUES (")
        .append(Fragment.encode(PgTypes.int4, id))
        .append(Fragment.of(")"));
  }

  private static Fragment insertInto(String table, int id, String name) {
    return Fragment.of("INSERT INTO " + table + " (id, name) VALUES (")
        .append(Fragment.encode(PgTypes.int4, id))
        .append(Fragment.of(", "))
        .append(Fragment.encode(PgTypes.text, name))
        .append(Fragment.of(")"));
  }

  // ========== Transaction: commit ==========

  @Test
  public void transactionCommitsOnSuccess() {
    try (var pool = createPool()) {
      String table = "tx_commit_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int, name text)").execute());

      pool.transact(
          tx -> {
            tx.execute(insertInto(table, 1, "alice").update());
            tx.execute(insertInto(table, 2, "bob").update());
            return null;
          });

      List<Integer> ids =
          pool.query(
              Fragment.of("SELECT id FROM " + table + " ORDER BY id"), RowCodec.of(PgTypes.int4));
      assertEqual(ids, List.of(1, 2));

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Transaction: rollback on exception ==========

  @Test
  public void transactionRollsBackOnException() {
    try (var pool = createPool()) {
      String table = "tx_rollback_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());

      pool.execute(insertInto(table, 0).update());

      try {
        pool.transact(
            tx -> {
              tx.execute(insertInto(table, 1).update());
              tx.execute(insertInto(table, 2).update());
              throw new RuntimeException("deliberate failure");
            });
        throw new AssertionError("Should have thrown");
      } catch (RuntimeException e) {
        assertEqual(e.getMessage(), "deliberate failure");
      }

      List<Integer> ids =
          pool.query(
              Fragment.of("SELECT id FROM " + table + " ORDER BY id"), RowCodec.of(PgTypes.int4));
      assertEqual(ids, List.of(0));

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Transaction: return value ==========

  @Test
  public void transactionReturnsValue() {
    try (var pool = createPool()) {
      String table = "tx_return_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id serial, name text)").execute());

      String result =
          pool.transact(
              tx -> {
                tx.execute(insertInto(table, 1, "hello").update());
                return tx.execute(
                        Fragment.of("SELECT name FROM " + table + " LIMIT 1")
                            .query(RowCodec.of(PgTypes.text).first()))
                    .orElse("missing");
              });
      assertEqual(result, "hello");

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Transaction: isolation ==========

  @Test
  public void transactionIsolation() throws Exception {
    try (var pool =
        createPool(
            PgPipelineConfig.builder().connectionCount(3).maxTransactionConnections(2).build())) {
      String table = "tx_isolation_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());

      CyclicBarrier barrier = new CyclicBarrier(2);
      AtomicReference<List<Integer>> outsideView = new AtomicReference<>();

      Thread txThread =
          Thread.ofVirtual()
              .start(
                  () -> {
                    pool.transact(
                        tx -> {
                          tx.execute(insertInto(table, 42).update());
                          try {
                            barrier.await();
                          } catch (Exception e) {
                            throw new RuntimeException(e);
                          }
                          try {
                            barrier.await();
                          } catch (Exception e) {
                            throw new RuntimeException(e);
                          }
                          return null;
                        });
                  });

      barrier.await();

      outsideView.set(
          pool.query(Fragment.of("SELECT id FROM " + table), RowCodec.of(PgTypes.int4)));

      barrier.await();
      txThread.join();

      assertEqual(outsideView.get(), List.of());

      List<Integer> afterCommit =
          pool.query(Fragment.of("SELECT id FROM " + table), RowCodec.of(PgTypes.int4));
      assertEqual(afterCommit, List.of(42));

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Combine: parallel outside transaction ==========

  @Test
  public void combineRunsParallelOutsideTransaction() {
    try (var pool = createPool()) {
      Operation<Integer> op1 =
          Fragment.of("SELECT 1").query(RowCodec.of(PgTypes.int4).exactlyOne());
      Operation<Integer> op2 =
          Fragment.of("SELECT 2").query(RowCodec.of(PgTypes.int4).exactlyOne());

      var result = pool.execute(op1.combine(op2));
      assertEqual((Object) result._1(), 1);
      assertEqual((Object) result._2(), 2);
    }
  }

  // ========== Combine: sequential inside transaction ==========

  @Test
  public void combineRunsSequentialInsideTransaction() {
    try (var pool = createPool()) {
      String table = "tx_combine_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());

      pool.transact(
          tx -> {
            Operation<Integer> ins1 = insertInto(table, 10).update();
            Operation<Integer> ins2 = insertInto(table, 20).update();
            tx.execute(ins1.combine(ins2));
            return null;
          });

      List<Integer> values =
          pool.query(
              Fragment.of("SELECT id FROM " + table + " ORDER BY id"), RowCodec.of(PgTypes.int4));
      assertEqual(values, List.of(10, 20));

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Error: SQL error propagation ==========

  @Test
  public void sqlErrorPropagation() {
    try (var pool = createPool()) {
      try {
        pool.execute(Fragment.of("SELECT * FROM nonexistent_table_xyz").update());
        throw new AssertionError("Should have thrown");
      } catch (PgPipelineException e) {
        assertTrue(
            e.getMessage().contains("nonexistent_table_xyz") || e.getMessage().contains("42P01"));
      }
    }
  }

  // ========== Error: pool recovery ==========

  @Test
  public void poolRecoveryAfterSqlError() {
    try (var pool = createPool()) {
      try {
        pool.execute(Fragment.of("SELECT * FROM nonexistent_table_xyz").update());
      } catch (PgPipelineException ignored) {
      }

      List<Integer> result = pool.query(Fragment.of("SELECT 42"), RowCodec.of(PgTypes.int4));
      assertEqual(result, List.of(42));
    }
  }

  // ========== Error: multiple sequential errors ==========

  @Test
  public void multipleErrorsDontBreakPool() {
    try (var pool = createPool()) {
      for (int i = 0; i < 10; i++) {
        try {
          pool.execute(Fragment.of("INVALID SQL " + i).update());
        } catch (PgPipelineException ignored) {
        }
      }

      Integer result =
          pool.execute(Fragment.of("SELECT 1").query(RowCodec.of(PgTypes.int4).exactlyOne()));
      assertEqual((Object) result, (Object) 1);
    }
  }

  // ========== Concurrent: many queries in parallel ==========

  @Test
  public void concurrentQueries() throws Exception {
    try (var pool = createPool(PgPipelineConfig.builder().connectionCount(4).build())) {
      int threadCount = 20;
      CountDownLatch latch = new CountDownLatch(threadCount);
      AtomicInteger successes = new AtomicInteger(0);
      AtomicReference<Throwable> failure = new AtomicReference<>();

      for (int i = 0; i < threadCount; i++) {
        int val = i;
        Thread.ofVirtual()
            .start(
                () -> {
                  try {
                    Integer result =
                        pool.execute(
                            Fragment.of("SELECT ")
                                .append(Fragment.encode(PgTypes.int4, val))
                                .query(RowCodec.of(PgTypes.int4).exactlyOne()));
                    if (result == val) successes.incrementAndGet();
                  } catch (Throwable t) {
                    failure.compareAndSet(null, t);
                  } finally {
                    latch.countDown();
                  }
                });
      }

      latch.await();
      if (failure.get() != null) {
        throw new AssertionError("Concurrent query failed", failure.get());
      }
      assertEqual((Object) successes.get(), threadCount);
    }
  }

  // ========== Concurrent: many transactions in parallel ==========

  @Test
  public void concurrentTransactions() throws Exception {
    try (var pool =
        createPool(
            PgPipelineConfig.builder().connectionCount(4).maxTransactionConnections(3).build())) {
      String table = "conc_tx_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());

      int txCount = 10;
      CountDownLatch latch = new CountDownLatch(txCount);
      AtomicReference<Throwable> failure = new AtomicReference<>();

      for (int i = 0; i < txCount; i++) {
        int val = i;
        Thread.ofVirtual()
            .start(
                () -> {
                  try {
                    pool.transact(
                        tx -> {
                          tx.execute(insertInto(table, val).update());
                          return null;
                        });
                  } catch (Throwable t) {
                    failure.compareAndSet(null, t);
                  } finally {
                    latch.countDown();
                  }
                });
      }

      latch.await();
      if (failure.get() != null) {
        throw new AssertionError("Concurrent transaction failed", failure.get());
      }

      List<Integer> ids =
          pool.query(
              Fragment.of("SELECT id FROM " + table + " ORDER BY id"), RowCodec.of(PgTypes.int4));
      assertEqual((Object) ids.size(), txCount);

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Connection lifecycle: short max lifetime ==========

  @Test
  public void connectionReplacedOnMaxLifetime() throws Exception {
    try (var pool =
        createPool(
            PgPipelineConfig.builder()
                .connectionCount(1)
                .maxLifetime(Duration.ofSeconds(2))
                .idleTimeout(Duration.ofMinutes(10))
                .validationInterval(Duration.ofMillis(500))
                .build())) {

      Integer r1 =
          pool.execute(Fragment.of("SELECT 1").query(RowCodec.of(PgTypes.int4).exactlyOne()));
      assertEqual((Object) r1, 1);

      Thread.sleep(3000);

      Integer r2 =
          pool.execute(Fragment.of("SELECT 2").query(RowCodec.of(PgTypes.int4).exactlyOne()));
      assertEqual((Object) r2, 2);
    }
  }

  // ========== Connection lifecycle: short idle timeout ==========

  @Test
  public void connectionReplacedOnIdleTimeout() throws Exception {
    try (var pool =
        createPool(
            PgPipelineConfig.builder()
                .connectionCount(1)
                .maxLifetime(Duration.ofMinutes(10))
                .idleTimeout(Duration.ofSeconds(2))
                .validationInterval(Duration.ofMillis(500))
                .build())) {

      pool.execute(Fragment.of("SELECT 1").query(RowCodec.of(PgTypes.int4).exactlyOne()));

      Thread.sleep(3000);

      Integer r =
          pool.execute(Fragment.of("SELECT 42").query(RowCodec.of(PgTypes.int4).exactlyOne()));
      assertEqual((Object) r, 42);
    }
  }

  // ========== Parameterized queries ==========

  @Test
  public void parameterizedQueryRoundtrip() {
    try (var pool = createPool()) {
      String table = "param_test_" + System.nanoTime();
      pool.execute(
          Fragment.of("CREATE TABLE " + table + " (id int, name text, score float8)").execute());

      pool.execute(
          Fragment.of("INSERT INTO " + table + " VALUES (")
              .append(Fragment.encode(PgTypes.int4, 1))
              .append(Fragment.of(", "))
              .append(Fragment.encode(PgTypes.text, "alice"))
              .append(Fragment.of(", "))
              .append(Fragment.encode(PgTypes.float8, 95.5))
              .append(Fragment.of(")"))
              .update());

      record Row(int id, String name, double score) {}
      RowCodec<Row> codec =
          RowCodec.<Row>builder()
              .field(PgTypes.int4, Row::id)
              .field(PgTypes.text, Row::name)
              .field(PgTypes.float8, Row::score)
              .build(Row::new);

      List<Row> rows =
          pool.execute(
              Fragment.of("SELECT id, name, score FROM " + table + " WHERE name = ")
                  .append(Fragment.encode(PgTypes.text, "alice"))
                  .query(codec.all()));
      assertEqual((Object) rows.size(), 1);
      assertEqual((Object) rows.get(0).id(), 1);
      assertEqual(rows.get(0).name(), "alice");
      assertTrue(Math.abs(rows.get(0).score() - 95.5) < 0.001);

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Null handling ==========

  @Test
  public void nullParameterHandling() {
    try (var pool = createPool()) {
      String table = "null_test_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int, name text)").execute());

      pool.execute(
          Fragment.of("INSERT INTO " + table + " VALUES (")
              .append(Fragment.encode(PgTypes.int4, 1))
              .append(Fragment.of(", "))
              .append(Fragment.encode(PgTypes.text.opt(), Optional.empty()))
              .append(Fragment.of(")"))
              .update());

      RowCodec<Optional<String>> codec = RowCodec.of(PgTypes.text.opt());
      List<Optional<String>> rows = pool.query(Fragment.of("SELECT name FROM " + table), codec);
      assertEqual((Object) rows.size(), 1);
      assertEqual(rows.get(0), Optional.empty());

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Prepared statement cache ==========

  @Test
  public void preparedStatementCacheReuse() {
    try (var pool = createPool()) {
      for (int i = 0; i < 10; i++) {
        Integer result =
            pool.execute(
                Fragment.of("SELECT ")
                    .append(Fragment.encode(PgTypes.int4, i))
                    .query(RowCodec.of(PgTypes.int4).exactlyOne()));
        assertEqual(result, i);
      }
    }
  }

  // ========== ifEmpty combinator ==========

  @Test
  public void ifEmptyCombinator() {
    try (var pool = createPool()) {
      String table = "ifempty_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());

      Operation<Integer> op =
          Operation.ifEmpty(
              Fragment.of("SELECT id FROM " + table).query(RowCodec.of(PgTypes.int4).first()),
              OperationRead.pure(999));
      Integer result = pool.execute(op);
      assertEqual((Object) result, 999);

      pool.execute(insertInto(table, 42).update());
      result = pool.execute(op);
      assertEqual((Object) result, 42);

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== mapped operation ==========

  @Test
  public void mappedOperation() {
    try (var pool = createPool()) {
      Operation<String> op =
          Fragment.of("SELECT 42")
              .query(RowCodec.of(PgTypes.int4).exactlyOne())
              .map(i -> "answer:" + i);
      assertEqual(pool.execute(op), "answer:42");
    }
  }

  // ========== voided / execute ==========

  @Test
  public void voidedOperation() {
    try (var pool = createPool()) {
      String table = "voided_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
      pool.execute(Fragment.of("INSERT INTO " + table + " VALUES (1)").execute());

      List<Integer> rows =
          pool.query(Fragment.of("SELECT id FROM " + table), RowCodec.of(PgTypes.int4));
      assertEqual(rows, List.of(1));

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Transaction: SQL error inside tx triggers rollback ==========

  @Test
  public void transactionRollsBackOnSqlError() {
    try (var pool = createPool()) {
      String table = "tx_sql_err_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int UNIQUE)").execute());

      try {
        pool.transact(
            tx -> {
              tx.execute(insertInto(table, 1).update());
              // Duplicate key — triggers SQL error
              tx.execute(insertInto(table, 1).update());
              return null;
            });
        throw new AssertionError("Should have thrown");
      } catch (PgPipelineException e) {
        assertTrue(e.getMessage().contains("duplicate") || e.getMessage().contains("23505"));
      }

      // Table should be empty — everything was rolled back
      List<Integer> ids =
          pool.query(Fragment.of("SELECT id FROM " + table), RowCodec.of(PgTypes.int4));
      assertEqual(ids, List.of());

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Exhaustion strategy: THROW ==========

  @Test
  public void throwStrategyFailsFastWhenAllTxSlotsUsed() throws Exception {
    try (var pool =
        createPool(
            PgPipelineConfig.builder()
                .connectionCount(1)
                .maxTransactionConnections(1)
                .exhaustionStrategy(PgExhaustionStrategy.THROW)
                .build())) {
      CyclicBarrier barrier = new CyclicBarrier(2);
      AtomicReference<Throwable> secondTxError = new AtomicReference<>();
      CountDownLatch done = new CountDownLatch(1);

      Thread.ofVirtual()
          .start(
              () -> {
                pool.transact(
                    tx -> {
                      try {
                        barrier.await();
                        barrier.await();
                      } catch (Exception e) {
                        throw new RuntimeException(e);
                      }
                      return null;
                    });
              });

      barrier.await();

      Thread.ofVirtual()
          .start(
              () -> {
                try {
                  pool.transact(tx -> null);
                } catch (Throwable t) {
                  secondTxError.set(t);
                } finally {
                  done.countDown();
                }
              });

      done.await();
      barrier.await();

      assertTrue(secondTxError.get() instanceof PgPipelineException);
      assertTrue(secondTxError.get().getMessage().contains("Max transaction connections reached"));
    }
  }

  // ========== Pool: close and reuse prevention ==========

  @Test
  public void poolCloseIsIdempotent() {
    var pool = createPool();
    pool.execute(Fragment.of("SELECT 1").query(RowCodec.of(PgTypes.int4).exactlyOne()));
    pool.close();
    pool.close(); // should not throw
  }

  // ========== Streaming cursors ==========

  @Test
  public void streamingCursorReadsAllRows() {
    try (var pool = createPool()) {
      String table = "cursor_all_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
      for (int i = 1; i <= 100; i++) {
        pool.execute(insertInto(table, i).update());
      }

      Operation<Cursor<Integer>> streamOp =
          new OperationRead.Streaming<>(
              Fragment.of("SELECT id FROM " + table + " ORDER BY id"),
              RowCodec.of(PgTypes.int4),
              10);

      try (Cursor<Integer> cursor = pool.execute(streamOp)) {
        List<Integer> collected = cursor.toList();
        assertEqual((Object) collected.size(), 100);
        assertEqual((Object) collected.get(0), 1);
        assertEqual((Object) collected.get(99), 100);
      }

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  @Test
  public void streamingCursorPartialConsumption() {
    try (var pool = createPool()) {
      String table = "cursor_partial_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
      for (int i = 1; i <= 50; i++) {
        pool.execute(insertInto(table, i).update());
      }

      Operation<Cursor<Integer>> streamOp =
          new OperationRead.Streaming<>(
              Fragment.of("SELECT id FROM " + table + " ORDER BY id"),
              RowCodec.of(PgTypes.int4),
              5);

      try (Cursor<Integer> cursor = pool.execute(streamOp)) {
        List<Integer> firstFew = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
          assertTrue(cursor.hasNext());
          firstFew.add(cursor.next());
        }
        assertEqual((Object) firstFew.size(), 7);
        assertEqual((Object) firstFew.get(0), 1);
        assertEqual((Object) firstFew.get(6), 7);
      }

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  @Test
  public void streamingCursorEmptyResult() {
    try (var pool = createPool()) {
      String table = "cursor_empty_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());

      Operation<Cursor<Integer>> streamOp =
          new OperationRead.Streaming<>(
              Fragment.of("SELECT id FROM " + table), RowCodec.of(PgTypes.int4), 10);

      try (Cursor<Integer> cursor = pool.execute(streamOp)) {
        List<Integer> collected = cursor.toList();
        assertEqual((Object) collected.size(), 0);
      }

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  @Test
  public void streamingCursorPoolStillWorksAfterCursor() {
    try (var pool = createPool()) {
      String table = "cursor_pool_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
      for (int i = 1; i <= 20; i++) {
        pool.execute(insertInto(table, i).update());
      }

      try (Cursor<Integer> cursor =
          pool.execute(
              new OperationRead.Streaming<>(
                  Fragment.of("SELECT id FROM " + table + " ORDER BY id"),
                  RowCodec.of(PgTypes.int4),
                  5))) {
        cursor.toList();
      }

      Integer result =
          pool.execute(Fragment.of("SELECT 42").query(RowCodec.of(PgTypes.int4).exactlyOne()));
      assertEqual((Object) result, 42);

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  @Test
  public void streamingCursorExactBatchSize() {
    try (var pool = createPool()) {
      String table = "cursor_exact_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int)").execute());
      for (int i = 1; i <= 10; i++) {
        pool.execute(insertInto(table, i).update());
      }

      try (Cursor<Integer> cursor =
          pool.execute(
              new OperationRead.Streaming<>(
                  Fragment.of("SELECT id FROM " + table + " ORDER BY id"),
                  RowCodec.of(PgTypes.int4),
                  10))) {
        List<Integer> collected = cursor.toList();
        assertEqual((Object) collected.size(), 10);
      }

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Stress test: 1000 concurrent operations ==========

  @Test
  public void stressTest() throws Exception {
    try (var pool =
        createPool(
            PgPipelineConfig.builder().connectionCount(8).maxTransactionConnections(6).build())) {

      String table = "torture_" + System.nanoTime();
      pool.execute(Fragment.of("CREATE TABLE " + table + " (id int, name text)").execute());

      int totalOps = 1000;
      CountDownLatch latch = new CountDownLatch(totalOps);
      AtomicReference<Throwable> firstFailure = new AtomicReference<>();
      AtomicInteger plainQuerySuccesses = new AtomicInteger();
      AtomicInteger txCommitSuccesses = new AtomicInteger();
      AtomicInteger txAppExceptionCount = new AtomicInteger();
      AtomicInteger txSqlErrorCount = new AtomicInteger();

      // Distribution: 250 plain queries, 350 tx commits, 200 tx app-exceptions, 200 tx SQL errors
      for (int i = 0; i < totalOps; i++) {
        int idx = i;
        Thread.ofVirtual()
            .start(
                () -> {
                  try {
                    if (idx < 250) {
                      // Plain query — SELECT idx, expect idx back
                      Integer result =
                          pool.execute(
                              Fragment.of("SELECT ")
                                  .append(Fragment.encode(PgTypes.int4, idx))
                                  .query(RowCodec.of(PgTypes.int4).exactlyOne()));
                      if (result != idx) {
                        firstFailure.compareAndSet(
                            null,
                            new AssertionError("Plain query: expected " + idx + ", got " + result));
                      } else {
                        plainQuerySuccesses.incrementAndGet();
                      }

                    } else if (idx < 600) {
                      // Transaction commit — insert a row
                      pool.transact(
                          tx -> {
                            tx.execute(insertInto(table, idx, "committed").update());
                            return null;
                          });
                      txCommitSuccesses.incrementAndGet();

                    } else if (idx < 800) {
                      // Transaction with app-level exception — should rollback
                      try {
                        pool.transact(
                            tx -> {
                              tx.execute(insertInto(table, idx, "should_rollback_app").update());
                              throw new RuntimeException("deliberate-app-" + idx);
                            });
                        firstFailure.compareAndSet(
                            null, new AssertionError("App exception tx " + idx + " did not throw"));
                      } catch (RuntimeException e) {
                        if (e.getMessage().contains("deliberate-app-")) {
                          txAppExceptionCount.incrementAndGet();
                        } else {
                          firstFailure.compareAndSet(null, e);
                        }
                      }

                    } else {
                      // Transaction with SQL error — should rollback
                      try {
                        pool.transact(
                            tx -> {
                              tx.execute(insertInto(table, idx, "should_rollback_sql").update());
                              tx.execute(
                                  Fragment.of(
                                          "INSERT INTO nonexistent_table_" + idx + " VALUES (1)")
                                      .update());
                              return null;
                            });
                        firstFailure.compareAndSet(
                            null, new AssertionError("SQL error tx " + idx + " did not throw"));
                      } catch (PgPipelineException e) {
                        txSqlErrorCount.incrementAndGet();
                      }
                    }
                  } catch (Throwable t) {
                    firstFailure.compareAndSet(null, t);
                  } finally {
                    latch.countDown();
                  }
                });
      }

      latch.await();

      if (firstFailure.get() != null) {
        throw new AssertionError("Stress test failure", firstFailure.get());
      }

      // Verify counts
      assertEqual((Object) plainQuerySuccesses.get(), 250);
      assertEqual((Object) txCommitSuccesses.get(), 350);
      assertEqual((Object) txAppExceptionCount.get(), 200);
      assertEqual((Object) txSqlErrorCount.get(), 200);

      // Verify database state: only committed rows should exist
      List<Integer> committedIds =
          pool.query(
              Fragment.of("SELECT id FROM " + table + " ORDER BY id"), RowCodec.of(PgTypes.int4));
      assertEqual((Object) committedIds.size(), 350);

      // Verify no rolled-back rows leaked
      List<String> names =
          pool.query(Fragment.of("SELECT DISTINCT name FROM " + table), RowCodec.of(PgTypes.text));
      assertEqual((Object) names.size(), 1);
      assertEqual(names.get(0), "committed");

      // Pool still healthy after all the chaos
      Integer healthCheck =
          pool.execute(Fragment.of("SELECT 1").query(RowCodec.of(PgTypes.int4).exactlyOne()));
      assertEqual((Object) healthCheck, 1);

      pool.execute(Fragment.of("DROP TABLE " + table).execute());
    }
  }

  // ========== Helpers ==========

  private static void assertEqual(Object actual, Object expected) {
    if (expected == null && actual == null) return;
    if (expected == null || actual == null) {
      throw new AssertionError("Expected: " + expected + ", Actual: " + actual);
    }
    if (!expected.equals(actual)) {
      throw new AssertionError("Expected: " + expected + ", Actual: " + actual);
    }
  }

  private static void assertTrue(boolean value) {
    if (!value) throw new AssertionError("Expected true");
  }
}
