package dev.typr.foundations.docs.duckdb

import dev.typr.foundations.DuckDbType
import dev.typr.foundations.DuckDbTypes

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class ProductId(val value: Long)

    // Create DuckDbType from bigint
    val productIdType: DuckDbType<ProductId> = DuckDbTypes.bigint.bimap(::ProductId, ProductId::value)
    //stop
}
