package dev.typr.foundations.docs.dynamic;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.OperationRead;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import java.math.BigDecimal;
import java.util.Optional;

@SuppressWarnings("unused")
public class WhenDsl {
  record ProductRow(Integer id, String name, BigDecimal price) {}

  static RowCodec<ProductRow> codec =
      RowCodec.<ProductRow>builder()
          .field(PgTypes.int4, ProductRow::id)
          .field(PgTypes.text, ProductRow::name)
          .field(PgTypes.numeric, ProductRow::price)
          .build(ProductRow::new);

  // start
  // Three optional filters → 2^3 = 8 SQL shapes Query Analysis verifies.
  static OperationRead.Query<java.util.List<ProductRow>> search(
      Optional<String> namePattern,
      Optional<BigDecimal> maxPrice,
      boolean onlyActive) {
    return Fragment.of("SELECT id, name, price FROM product WHERE 1 = 1")
        .optionally(namePattern).append(" AND name ILIKE ", PgTypes.text)
        .optionally(maxPrice)  .append(" AND price < ",   PgTypes.numeric)
        .optionally(onlyActive).append(" AND active = TRUE")
        .query(codec.all());
  }
  // stop
}
