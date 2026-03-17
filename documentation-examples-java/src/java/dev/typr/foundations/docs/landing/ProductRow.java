package dev.typr.foundations.docs.landing;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@SuppressWarnings("unused")
public class ProductRow {
  // start
  record Product(int id, String name, BigDecimal price, Optional<Instant> createdAt) {}
  // stop
}
