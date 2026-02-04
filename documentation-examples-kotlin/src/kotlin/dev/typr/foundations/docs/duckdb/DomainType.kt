package dev.typr.foundations.docs.duckdb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class ProductId(val value: Long)

    // Create DuckDbType from bigint
    val productIdType: DuckDbType<ProductId> = DuckDbTypes.bigint.bimap(::ProductId, ProductId::value)
    //stop
}
