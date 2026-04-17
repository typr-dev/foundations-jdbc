package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.Instant;
import java.util.List;

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
  RowTemplate.Update<Product> insertAll = Fragment.insertInto("product", productCodec);

  List<Integer> insertProducts(List<Product> products) {
    return insertAll.onMany(products.iterator()).run(conn);
  }

  // Batch insert — skip auto-generated ID column
  RowTemplate.Update<Product> insertAutoId = Fragment.insertInto("product", productCodec, "id");

  List<Integer> insertProductsAutoId(List<Product> products) {
    return insertAutoId.onMany(products.iterator()).run(conn);
  }
  // stop
}
