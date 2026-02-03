package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

@SuppressWarnings("unused")
public class DomainType {
    //start
    // Wrapper type
    public record ProductId(Long value) {}

    // Create DuckDbType from bigint
    DuckDbType<ProductId> productIdType = DuckDbTypes.bigint.bimap(ProductId::new, ProductId::value);
    //stop
}
