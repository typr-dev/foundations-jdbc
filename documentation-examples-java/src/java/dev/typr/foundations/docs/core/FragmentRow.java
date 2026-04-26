package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.math.BigDecimal;
import java.time.Instant;

@SuppressWarnings("unused")
public class FragmentRow {
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
  Product insert(Product product) {
    return Fragment.insertIntoReturning("product", productCodec).updateReturning(product).run(conn);
  }

  // Skip columns with database defaults — pass column names to except
  Product insertWithDefault(Product product) {
    return Fragment.insertIntoReturning("product", productCodec, "id").updateReturning(product).run(conn);
  }
  // stop
}
