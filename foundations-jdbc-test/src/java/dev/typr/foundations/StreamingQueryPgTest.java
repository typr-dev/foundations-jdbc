package dev.typr.foundations;

import java.sql.Connection;
import org.junit.Test;

import static org.junit.Assert.*;

public class StreamingQueryPgTest {

  static <T> T withConnection(SqlFunction<Connection, T> f) {
    return Containers.postgresTransactor().execute(f);
  }

  @Test
  public void testStreamingMillionRowsMerged() {
    withConnection(conn -> {
      var streaming1 = Fragment.of("SELECT i FROM generate_series(1, 1000000) AS s(i)")
          .streamingQuery(PgTypes.int4, 512);

      var streaming2 = Fragment.of("SELECT i FROM generate_series(1, 1000000) AS s(i)")
          .streamingQuery(PgTypes.int4, 512);

      long count = streaming1.combine(streaming2).map(t -> {
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
      }).run(conn);

      assertEquals(1_000_000L, count);
      return null;
    });
  }
}
