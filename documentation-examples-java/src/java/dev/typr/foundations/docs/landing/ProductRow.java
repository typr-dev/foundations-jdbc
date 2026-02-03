package dev.typr.foundations.docs.landing;

import dev.typr.foundations.data.Jsonb;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

@SuppressWarnings("unused")
public class ProductRow {
    record ProductId(Integer value) {}
    record Dimensions(Double width, Double height, Double depth, String unit) {}

    //start
    record Product(
        ProductId id,
        String name,
        BigDecimal price,
        Optional<String[]> tags,            // text[]
        Optional<Dimensions> dimensions,    // composite type
        Optional<Jsonb> metadata,           // jsonb
        Optional<OffsetDateTime> createdAt  // timestamptz
    ) {}
    //stop
}
