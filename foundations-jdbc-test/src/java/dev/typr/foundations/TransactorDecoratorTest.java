package dev.typr.foundations;

import static org.junit.Assert.*;

import dev.typr.foundations.connect.DuckDbConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TransactorDecoratorTest {

  private static Transactor createDuckDbTransactor() {
    var config = DuckDbConfig.inMemory().build();
    return Transactor.create(config);
  }

  private static QueryListener recordingListener(List<String> events) {
    return new QueryListener() {
      @Override
      public void beforeQuery(String sql, java.util.Optional<String> name) {
        events.add("before:" + sql);
      }

      @Override
      public void afterQuery(QueryEvent event) {
        events.add("after:" + event.sql());
      }

      @Override
      public void failedQuery(QueryEvent event) {
        events.add("failed:" + event.sql());
      }

      @Override
      public void afterTransaction(TransactionEvent event) {
        events.add("afterTx");
      }

      @Override
      public void failedTransaction(TransactionEvent event) {
        events.add("failedTx");
      }
    };
  }

  @Test
  public void testDoubleRollbackOnly() {
    var tx = createDuckDbTransactor();
    var rollback1 = tx.rollbackOnly();
    var rollback2 = rollback1.rollbackOnly();

    rollback2.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE TABLE dr_test(id INTEGER)").execute());
          mc.execute(Fragment.of("INSERT INTO dr_test VALUES (1)").update());
          return null;
        });

    List<Integer> rows =
        tx.execute(
            Fragment.of(
                    "SELECT count(*) FROM information_schema.tables WHERE table_name = 'dr_test'")
                .query(RowCodec.of(DuckDbTypes.integer).all()));
    assertEquals(0, (int) rows.getFirst());
  }

  @Test
  public void testDoubleWithListener() {
    var tx = createDuckDbTransactor();
    List<String> events1 = new ArrayList<>();
    List<String> events2 = new ArrayList<>();

    var decorated =
        tx.withListener(recordingListener(events1)).withListener(recordingListener(events2));

    decorated.execute(Fragment.of("SELECT 1").query(RowCodec.of(DuckDbTypes.integer).all()));

    assertFalse(events1.isEmpty());
    assertFalse(events2.isEmpty());

    assertTrue(events1.stream().anyMatch(e -> e.startsWith("before:")));
    assertTrue(events2.stream().anyMatch(e -> e.startsWith("before:")));
  }

  @Test
  public void testRollbackOnlyThenListener() {
    var tx = createDuckDbTransactor();
    List<String> events = new ArrayList<>();

    var decorated = tx.rollbackOnly().withListener(recordingListener(events));

    decorated.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE TABLE rbtl_test(id INTEGER)").execute());
          return null;
        });

    List<Integer> rows =
        tx.execute(
            Fragment.of(
                    "SELECT count(*) FROM information_schema.tables WHERE table_name = 'rbtl_test'")
                .query(RowCodec.of(DuckDbTypes.integer).all()));
    assertEquals(0, (int) rows.getFirst());

    assertTrue(events.stream().anyMatch(e -> e.equals("afterTx")));
  }

  @Test
  public void testListenerThenRollbackOnly() {
    var tx = createDuckDbTransactor();
    List<String> events = new ArrayList<>();

    var decorated = tx.withListener(recordingListener(events)).rollbackOnly();

    decorated.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE TABLE ltrb_test(id INTEGER)").execute());
          return null;
        });

    List<Integer> rows =
        tx.execute(
            Fragment.of(
                    "SELECT count(*) FROM information_schema.tables WHERE table_name = 'ltrb_test'")
                .query(RowCodec.of(DuckDbTypes.integer).all()));
    assertEquals(0, (int) rows.getFirst());

    assertTrue(events.stream().anyMatch(e -> e.equals("afterTx")));
  }

  @Test
  public void testRollbackOnlyActuallyRollsBack() {
    var tx = createDuckDbTransactor();

    tx.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE TABLE rb_actual(id INTEGER)").execute());
          return null;
        });

    tx.rollbackOnly()
        .transact(
            mc -> {
              mc.execute(Fragment.of("INSERT INTO rb_actual VALUES (42)").update());
              return null;
            });

    List<Integer> counts =
        tx.execute(
            Fragment.of("SELECT count(*) FROM rb_actual")
                .query(RowCodec.of(DuckDbTypes.integer).all()));
    assertEquals(0, (int) counts.getFirst());
  }

  @Test
  public void testListenersAreCalled() {
    var tx = createDuckDbTransactor();
    List<String> events = new ArrayList<>();

    var decorated = tx.withListener(recordingListener(events));

    decorated.transact(
        mc -> {
          mc.execute(Fragment.of("SELECT 1").query(RowCodec.of(DuckDbTypes.integer).all()));
          return null;
        });

    assertTrue(events.contains("before:SELECT 1"));
    assertTrue(events.stream().anyMatch(e -> e.startsWith("after:SELECT 1")));
    assertTrue(events.contains("afterTx"));
  }

  @Test
  public void testDoubleDecorationDoesNotNest() {
    var tx = createDuckDbTransactor();
    List<String> events1 = new ArrayList<>();
    List<String> events2 = new ArrayList<>();

    var first = tx.withListener(recordingListener(events1));
    var second = first.rollbackOnly().withListener(recordingListener(events2));

    second.transact(
        mc -> {
          mc.execute(Fragment.of("CREATE TABLE dd_test(id INTEGER)").execute());
          return null;
        });

    List<Integer> rows =
        tx.execute(
            Fragment.of(
                    "SELECT count(*) FROM information_schema.tables WHERE table_name = 'dd_test'")
                .query(RowCodec.of(DuckDbTypes.integer).all()));
    assertEquals(0, (int) rows.getFirst());

    assertFalse(events1.isEmpty());
    assertFalse(events2.isEmpty());
  }
}
