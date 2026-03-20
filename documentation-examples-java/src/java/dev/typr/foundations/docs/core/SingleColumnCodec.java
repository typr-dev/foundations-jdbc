package dev.typr.foundations.docs.core;

import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;

@SuppressWarnings("unused")
public class SingleColumnCodec {
  // start
  RowCodec<Integer> idCodec = RowCodec.of(PgTypes.int4);
  // stop
}
