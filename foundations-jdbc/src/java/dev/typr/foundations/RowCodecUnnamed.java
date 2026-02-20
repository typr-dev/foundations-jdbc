package dev.typr.foundations;

import java.util.List;
import java.util.function.Function;

final class RowCodecUnnamed<Row> extends RowCodec<Row> {
  RowCodecUnnamed(
      List<DbType<?>> columns, Function<Object[], Row> decode, Function<Row, Object[]> encode) {
    super(columns, decode, encode);
  }
}
