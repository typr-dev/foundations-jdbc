package dev.typr.foundations.docs.landing;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.RowCodecNamed;

@SuppressWarnings("unused")
public class Dimensions {
  // start
  record Dim(Double width, Double height, Double depth, String unit) {}

  static RowCodecNamed<Dim> dimCodec =
      RowCodec.<Dim>namedBuilder()
          .field("width", PgTypes.float8, Dim::width)
          .field("height", PgTypes.float8, Dim::height)
          .field("depth", PgTypes.float8, Dim::depth)
          .field("unit", PgTypes.text, Dim::unit)
          .build(Dim::new);

  // Named composite — reads and writes via CREATE TYPE dimensions
  static PgType<Dim> pgType = PgTypes.compositeOf("dimensions", dimCodec);
  // stop
}
