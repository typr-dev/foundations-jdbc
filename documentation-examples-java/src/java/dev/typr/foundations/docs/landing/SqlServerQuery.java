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
  // Build a dynamic query with the `optionally` DSL — only present filters reach the SQL.
  // Each `.optionally().append(...)` is a branch point Query Analysis expands and verifies
  // against the schema, so every possible 2^N shape is checked at test time.
  List<OrderRow> orders =
      Fragment.of("SELECT id, name, price FROM orders WHERE 1 = 1")
          .optionally(name)      .append(" AND name LIKE ", SqlServerTypes.nvarchar)
          .optionally(maxPrice)  .append(" AND price < ",   SqlServerTypes.decimal)
          .optionally(onlyActive).append(" AND active = 1")
          .query(orderRowCodec.all())
          .run(conn);
  // stop
}
