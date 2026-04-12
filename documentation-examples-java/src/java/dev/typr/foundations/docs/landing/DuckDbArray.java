package dev.typr.foundations.docs.landing;

import dev.typr.foundations.DuckDbTypes;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;
import java.util.List;

@SuppressWarnings("unused")
public class DuckDbArray {
  Transactor tx = null; // placeholder

  // start
  // DuckDB arrays are first-class typed values
  List<String[]> getTagSets() {
    return Fragment.of("SELECT tags FROM posts WHERE published = true")
        .query(RowCodec.of(DuckDbTypes.varchar.array()).all())
        .transact(tx);
  }
  // stop
}
