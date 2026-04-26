package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class BatchOperations {
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
  // Batch insert — all columns as parameters
  RowParamBuilder<Product> insertAll = Fragment.insertInto("product", productCodec);

  Optional<int[]> insertProducts(List<Product> products) {
    return insertAll.updateMany(products.iterator()).run(conn);
  }

  // Batch insert — skip auto-generated ID column
  RowParamBuilder<Product> insertAutoId = Fragment.insertInto("product", productCodec, "id");

  Optional<int[]> insertProductsAutoId(List<Product> products) {
    return insertAutoId.updateMany(products.iterator()).run(conn);
  }
  // stop
}
