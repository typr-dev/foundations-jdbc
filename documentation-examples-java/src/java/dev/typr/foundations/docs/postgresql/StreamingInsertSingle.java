package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.*;
import java.util.Iterator;

@SuppressWarnings("unused")
public class StreamingInsertSingle {

  // start
  // Insert a list of strings using COPY
  long insertNames(Iterator<String> names, Transactor tx) {
    return StreamingInsert.of("COPY users(name) FROM STDIN", 1000, names, PgTypes.text.pgText())
        .transact(tx);
  }
  // stop
}
