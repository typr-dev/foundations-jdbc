package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class FragmentComposing {
  record ProductRow(Integer id, String name, BigDecimal price) {}

  static RowCodec<ProductRow> rowCodec =
      RowCodec.<ProductRow>builder()
          .field(PgTypes.int4, ProductRow::id)
          .field(PgTypes.text, ProductRow::name)
          .field(PgTypes.numeric, ProductRow::price)
          .build(ProductRow::new);

  Transactor tx = null; // placeholder
  Optional<String> namePattern = Optional.of("%widget%");
  Optional<BigDecimal> maxPrice = Optional.of(new BigDecimal("100"));

  // start
  // Compose dynamic filters with the `optionally` DSL — each `.optionally().append(...)`
  // is a branch point Query Analysis verifies against the schema, even when
  // the runtime never takes that branch at this call site.
  List<ProductRow> query() {
    return tx.execute(
        Fragment.of("SELECT id, name, price FROM product WHERE 1 = 1")
            .optionally(namePattern).append(" AND name ILIKE ", PgTypes.text)
            .optionally(maxPrice)  .append(" AND price < ",   PgTypes.numeric)
            .query(rowCodec.all()));
  }
  // stop
}
