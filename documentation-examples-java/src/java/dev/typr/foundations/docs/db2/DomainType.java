package dev.typr.foundations.docs.db2;

import dev.typr.foundations.Db2Type;
import dev.typr.foundations.Db2Types;

@SuppressWarnings("unused")
public class DomainType {
    //start
    // Wrapper type
    public record ProductId(Long value) {}

    // Create Db2Type from bigint
    Db2Type<ProductId> productIdType = Db2Types.bigint.transform(ProductId::new, ProductId::value);
    //stop
}
