package dev.typr.foundations.docs.core;

import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Tuple;
import java.util.Optional;

@SuppressWarnings("unused")
public class ComposingCodecs {
  record ProductRow(Integer id, String name) {}

  record CategoryRow(Integer id, String categoryName) {}

  RowCodec<ProductRow> productCodec =
      RowCodec.<ProductRow>builder()
          .field(PgTypes.int4, ProductRow::id)
          .field(PgTypes.text, ProductRow::name)
          .build(ProductRow::new);

  RowCodec<CategoryRow> categoryCodec =
      RowCodec.<CategoryRow>builder()
          .field(PgTypes.int4, CategoryRow::id)
          .field(PgTypes.text, CategoryRow::categoryName)
          .build(CategoryRow::new);

  // start
  // Inner join — both sides always present
  RowCodec<Tuple.Tuple2<ProductRow, CategoryRow>> innerJoined = productCodec.joined(categoryCodec);

  // Left join — right side is Optional (nullable in Kotlin, Option in Scala)
  RowCodec<Tuple.Tuple2<ProductRow, Optional<CategoryRow>>> leftJoined =
      productCodec.leftJoined(categoryCodec);
  // stop
}
