package dev.typr.foundationskt.docs.sqlite

import dev.typr.foundationskt.*

@Suppress("unused")
class DomainType {
    //start
    data class ProductId(val value: Long)

    val productIdType: SqliteType<ProductId> =
        SqliteTypes.integer.transform(::ProductId, ProductId::value)
    //stop
}
