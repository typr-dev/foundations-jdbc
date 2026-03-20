package dev.typr.foundations.docs.landing;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;

@SuppressWarnings("unused")
public class WrapperType {
  // start
  record ProductId(Integer value) {
    // MariaDB int -> wraps to your domain type
    static MariaType<ProductId> mariaType =
        MariaTypes.int_.transform(ProductId::new, ProductId::value);
  }
  // stop
}
