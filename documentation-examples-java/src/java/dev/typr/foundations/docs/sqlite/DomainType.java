package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;

@SuppressWarnings("unused")
public class DomainType {
  // start
  // Wrapper type
  public record ProductId(Long value) {}

  // Build a SqliteType from `integer` via a bidirectional transform
  SqliteType<ProductId> productIdType =
      SqliteTypes.integer.transform(ProductId::new, ProductId::value);
  // stop
}
