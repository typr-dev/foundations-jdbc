package dev.typr.foundations.docs.landing;

import dev.typr.foundations.Connection;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.SqlServerTypes;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class SqlServerQuery {
  record OrderRow(Integer id, String name, BigDecimal price) {}

  static RowCodec<OrderRow> orderRowCodec = null; // placeholder
  Optional<String> name = Optional.empty();
  Optional<BigDecimal> maxPrice = Optional.empty();
  boolean onlyActive = false;
  Connection conn = null; // placeholder

  // start
  // Reusable filter functions
  static Fragment matchingName(Fragment f, Optional<String> name) {
    return f.optionally(name).append(" AND name LIKE ", SqlServerTypes.nvarchar);
  }

  static Fragment cheaperThan(Fragment f, Optional<BigDecimal> max) {
    return f.optionally(max).append(" AND price < ", SqlServerTypes.decimal);
  }

  static Fragment activeOnly(Fragment f, boolean active) {
    return f.optionally(active).append(" AND active = 1");
  }

  // Compose with .pipe()
  List<OrderRow> orders =
      Fragment.of("SELECT id, name, price FROM orders WHERE 1 = 1")
          .pipe(f -> matchingName(f, name))
          .pipe(f -> cheaperThan(f, maxPrice))
          .pipe(f -> activeOnly(f, onlyActive))
          .query(orderRowCodec.all())
          .run(conn);
  // stop
}
