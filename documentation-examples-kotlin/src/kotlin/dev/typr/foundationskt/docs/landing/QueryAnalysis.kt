package dev.typr.foundationskt.docs.landing

import dev.typr.foundationskt.*

@Suppress("unused")
class QueryAnalysisExample {
    lateinit var transactor: Transactor

    val productCodec: RowCodec<Product> =
        RowCodec.builder<Product>()
            .field(DuckDbTypes.integer, Product::id)
            .field(DuckDbTypes.integer, Product::name)
            .field(DuckDbTypes.double_, Product::price)
            .build(::Product)

    //start
    // name is VARCHAR in the database, but declared as Int here
    data class Product(val id: Int, val name: Int, val price: Double)

    val listProductsBad: OperationRead.Query<List<Product>> =
        Fragment.of("SELECT id, name, price FROM products")
            .query(productCodec.all())

    fun check() {
        val checker: QueryChecker = QueryChecker.create(transactor)
        checker.check(listProductsBad)
    }
    //stop
}
