package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.math.BigDecimal;
import java.time.Instant;

@SuppressWarnings("unused")
public class FragmentRowGeneratedKeys {
  record Product(Integer id, String name, BigDecimal price, Instant createdAt) {}

  RowCodecNamed<Product> productCodec =
      RowCodec.<Product>namedBuilder()
          .field("id", PgTypes.int4, Product::id)
          .field("name", PgTypes.text, Product::name)
          .field("price", PgTypes.numeric, Product::price)
          .field("created_at", PgTypes.timestamptz, Product::createdAt)
          .build(Product::new);

  Connection conn = null; // placeholder

  // start
  // For databases without RETURNING (DB2, Oracle, SQL Server, MariaDB):
  int insertGeneratedKey(Product product) {
    return Fragment.insertInto("product", productCodec, "id")
        .updateOneGenerated(
            product,
            new String[] {"id"},
            RowCodec.of(PgTypes.int4).exactlyOne())
        .run(conn);
  }
  // stop
}
