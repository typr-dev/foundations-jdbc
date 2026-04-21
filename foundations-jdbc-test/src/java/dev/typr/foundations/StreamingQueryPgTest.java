package dev.typr.foundations;

import static org.junit.Assert.*;

import org.junit.Test;

public class StreamingQueryPgTest {

  @Test
  public void testStreamingMillionRowsMerged() {
    var streaming1 =
        Fragment.of("SELECT i FROM generate_series(1, 1000000) AS s(i)")
            .streamingQuery(PgTypes.int4, 512);

    var streaming2 =
        Fragment.of("SELECT i FROM generate_series(1, 1000000) AS s(i)")
            .streamingQuery(PgTypes.int4, 512);

    long count =
        Containers.postgresTransactor()
            .execute(
                streaming1
                    .combine(streaming2)
                    .map(
                        t -> {
                          long n = 0;
                          var c1 = t._1();
                          var c2 = t._2();
                          while (c1.hasNext() && c2.hasNext()) {
                            int row1 = c1.next();
                            int row2 = c2.next();
                            assertEquals(row1, row2);
                            n++;
                          }
                          assertFalse(c1.hasNext());
                          assertFalse(c2.hasNext());
                          return n;
                        }));

    assertEquals(1_000_000L, count);
  }
}
