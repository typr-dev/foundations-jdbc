package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

@SuppressWarnings("unused")
public class StreamingReadFootgun {
  Transactor tx = null; // placeholder

  // start
  // WRONG: the cursor escapes the transaction — connection is already closed!
  Cursor<String> broken() {
    return Fragment.of("SELECT name FROM users")
        .streamingQuery(PgTypes.text, 512)
        .transact(tx); // connection closes here, cursor is dead
  }

  // CORRECT: process the cursor inside map, before the connection closes
  long correct() {
    return Fragment.of("SELECT name FROM users")
        .streamingQuery(PgTypes.text, 512)
        .map(
            cursor -> {
              long count = 0;
              for (var name : cursor) count++;
              return count;
            })
        .transact(tx);
  }
  // stop
}
