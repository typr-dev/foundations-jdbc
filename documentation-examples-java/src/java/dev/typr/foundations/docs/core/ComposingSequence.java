package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.OperationRead;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;
import java.util.List;

@SuppressWarnings("unused")
public class ComposingSequence {
  Transactor tx = null; // placeholder

  // start
  // Execute a list of operations and collect all results
  List<String> names = List.of("Alice", "Bob", "Charlie");

  List<Integer> insertAll() {
    List<OperationRead<Integer>> inserts =
        names.stream()
            .<OperationRead<Integer>>map(
                name ->
                    Fragment.of(
                            """
                            INSERT INTO users(name)
                            VALUES(\
                            """)
                        .value(PgTypes.text, name)
                        .append(") RETURNING id")
                        .query(RowCodec.of(PgTypes.int4).exactlyOne()))
            .toList();

    return OperationRead.sequence(inserts).transactRead(tx);
  }
  // stop
}
