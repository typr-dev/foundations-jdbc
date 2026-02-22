package dev.typr.foundationskt.docs.db2

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class ProductId(val value: Long)

    // Create Db2Type from bigint
    val productIdType: Db2Type<ProductId> = Db2Types.bigint.transform(::ProductId, ProductId::value)
    //stop
}
