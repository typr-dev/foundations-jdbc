package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class WrapperType {
    //start
    data class ProductId(val value: Int) {
        companion object {
            // MariaDB int -> wraps to your domain type
            val mariaType: MariaType<ProductId> =
                MariaTypes.int_.bimap(::ProductId, ProductId::value)
        }
    }
    //stop
}
