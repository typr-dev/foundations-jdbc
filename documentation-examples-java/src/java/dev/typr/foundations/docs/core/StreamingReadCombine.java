package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class StreamingReadCombine {
  Transactor tx = null; // placeholder

  // start
  // Open two cursors simultaneously on the same connection
  List<String> mergedNames() {
    var activeUsers =
        Fragment.of("SELECT name FROM users WHERE active").streamingQuery(PgTypes.text, 512);
    var archivedUsers =
        Fragment.of("SELECT name FROM archived_users").streamingQuery(PgTypes.text, 512);

    return activeUsers
        .combine(archivedUsers)
        .map(
            cursors -> {
              List<String> all = new ArrayList<>();
              all.addAll(cursors._1().toList());
              all.addAll(cursors._2().toList());
              return all;
            })
        .transact(tx);
  }
  // stop
}
