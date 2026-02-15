package dev.typr.foundations.docs.duckdb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class ProductId(val value: Long)

    // Create DuckDbType from bigint
    val productIdType: DuckDbType<ProductId> = DuckDbTypes.bigint.transform(::ProductId, ProductId::value)
    //stop
}
