package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
public class ReadonlyComposition {
  Transactor tx = null; // placeholder

  OperationRead<List<Integer>> findIds =
      Fragment.of("SELECT id FROM users").query(RowCodec.of(PgTypes.int4).all());
  OperationRead<Long> countUsers =
      Fragment.of("SELECT count(*) FROM users").queryExactlyOne(PgTypes.int8);

  Operation<Integer> insertUser =
      Fragment.of("INSERT INTO users(name) VALUES('Alice')").update();

  //start
  // Combining read-only operations yields OperationRead
  OperationRead<Tuple.Tuple2<List<Integer>, Long>> bothReads =
      findIds.combine(countUsers);

  // Mixing in a write operation yields Operation (not OperationRead)
  Operation<Tuple.Tuple2<Integer, List<Integer>>> writeAndRead =
      insertUser.combine(findIds);

  // transactRead works for read-only compositions
  Tuple.Tuple2<List<Integer>, Long> readResult = bothReads.transactRead(tx);

  // transact required when writes are involved
  Tuple.Tuple2<Integer, List<Integer>> writeResult = writeAndRead.transact(tx);
  //stop
}
