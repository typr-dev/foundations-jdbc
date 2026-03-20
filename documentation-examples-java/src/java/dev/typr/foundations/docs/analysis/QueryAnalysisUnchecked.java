package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import java.sql.Connection;

@SuppressWarnings("unused")
public class QueryAnalysisUnchecked {
  private final Connection connection = null; // placeholder

  // start
  record Stats(String name, int count) {}

  // .unchecked() skips type checking entirely for this column
  RowCodec<Stats> statsCodec =
      RowCodec.<Stats>builder()
          .field(PgTypes.text, Stats::name)
          .field(PgTypes.int4.unchecked(), Stats::count)
          .build(Stats::new);
  // stop
}
