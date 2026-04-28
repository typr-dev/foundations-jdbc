package dev.typr.foundations.docs.landing;

import dev.typr.foundations.*;
import java.util.List;

@SuppressWarnings("unused")
class QueryAnalysisExample {
  Transactor transactor = null; // placeholder

  RowCodec<Product> productCodec =
      RowCodec.<Product>builder()
          .field(DuckDbTypes.integer, Product::id)
          .field(DuckDbTypes.integer, Product::name)
          .field(DuckDbTypes.double_, Product::price)
          .build(Product::new);

  // start
  // name is VARCHAR in the database, but declared as INTEGER here
  record Product(Integer id, Integer name, Double price) {}

  OperationRead.Query<List<Product>> listProductsBad =
      Fragment.of("SELECT id, name, price FROM products")
          .query(productCodec.all());

  void check() {
    QueryChecker checker = QueryChecker.create(transactor);
    checker.check(listProductsBad);
  }
  // stop
}
