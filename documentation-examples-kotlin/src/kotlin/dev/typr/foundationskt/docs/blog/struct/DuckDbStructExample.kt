package dev.typr.foundationskt.docs.blog.struct

import dev.typr.foundationskt.*
import java.math.BigDecimal

@Suppress("unused")
class DuckDbStructExample {
    //start
    data class Address(val street: String, val city: String, val zip: String)

    val addressType: DuckDbType<Address> = DuckDbTypes.compositeOf("address",
        RowCodec.namedBuilder<Address>()
            .field("street", DuckDbTypes.varchar, Address::street)
            .field("city", DuckDbTypes.varchar, Address::city)
            .field("zip", DuckDbTypes.varchar, Address::zip)
            .build(::Address))
    //stop
}
