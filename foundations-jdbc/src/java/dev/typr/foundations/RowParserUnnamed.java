package dev.typr.foundations;

import java.util.List;
import java.util.function.Function;

final class RowParserUnnamed<Row> extends RowParser<Row> {
  RowParserUnnamed(
      List<DbType<?>> columns, Function<Object[], Row> decode, Function<Row, Object[]> encode) {
    super(columns, decode, encode);
  }
}
