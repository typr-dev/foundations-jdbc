package dev.typr.foundationskt.docs.blog.struct

import dev.typr.foundationskt.*

@Suppress("unused")
class ArrayParameter {
    val tx: Transactor = null!! // placeholder

    //start
    fun fetchProducts(ids: Array<Int>): List<String> =
        sql { "SELECT name FROM products WHERE id = ANY(${PgTypes.int4Array(ids)})" }
            .queryAll(RowCodec.of(PgTypes.text))
            .transact(tx)
    //stop
}
