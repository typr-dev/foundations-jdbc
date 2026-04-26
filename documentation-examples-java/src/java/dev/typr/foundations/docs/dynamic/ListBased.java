package dev.typr.foundations.docs.dynamic;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.OperationRead;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ListBased {
  record ProductRow(Integer id, String name, BigDecimal price) {}

  static RowCodec<ProductRow> codec =
      RowCodec.<ProductRow>builder()
          .field(PgTypes.int4, ProductRow::id)
          .field(PgTypes.text, ProductRow::name)
          .field(PgTypes.numeric, ProductRow::price)
          .build(ProductRow::new);

  // start
  // Build a list at runtime, then join with `Fragment.whereAnd`. Query Analysis
  // sees only the SQL shape constructed at scan time — runtime variants that
  // the test never built are not checked.
  static OperationRead.Query<List<ProductRow>> search(
      Optional<String> namePattern,
      Optional<BigDecimal> maxPrice) {
    List<Fragment> filters =
        Stream.of(
                namePattern.map(p -> Fragment.of("name ILIKE ").value(PgTypes.text, p)),
                maxPrice  .map(p -> Fragment.of("price < ")  .value(PgTypes.numeric, p)))
            .flatMap(Optional::stream)
            .toList();

    return Fragment.of("SELECT id, name, price FROM product ")
        .append(Fragment.whereAnd(filters))
        .query(codec.all());
  }
  // stop
}
