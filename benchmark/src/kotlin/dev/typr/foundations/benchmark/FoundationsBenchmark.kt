package dev.typr.foundations.benchmark

import dev.typr.foundationskt.*

class FoundationsBenchmark(ds: dev.typr.foundations.connect.ConnectionSource) : LibraryBenchmark {
    override val name = "Foundations JDBC"

    private val tx = Transactor(ds.transactor(dev.typr.foundations.Transactor.autoCommitStrategy()))

    private val itemCodec: RowCodec<Item> = RowCodec.builder<Item>()
        .field(DuckDbTypes.integer, Item::id)
        .field(DuckDbTypes.varchar, Item::name)
        .field(DuckDbTypes.varchar, Item::description)
        .field(DuckDbTypes.varchar, Item::category)
        .field(DuckDbTypes.varchar, Item::tags)
        .field(DuckDbTypes.integer, Item::quantity)
        .field(DuckDbTypes.decimal, Item::price)
        .field(DuckDbTypes.double_, Item::weight)
        .field(DuckDbTypes.boolean_, Item::active)
        .field(DuckDbTypes.double_, Item::rating)
        .field(DuckDbTypes.timestamp, Item::createdAt)
        .field(DuckDbTypes.timestamp, Item::updatedAt)
        .field(DuckDbTypes.date, Item::releaseDate)
        .field(DuckDbTypes.varchar, Item::sku)
        .field(DuckDbTypes.varchar, Item::notes)
        .build(::Item)

    private val selectAll: Operation<List<Item>> =
        Fragment.of("SELECT $COLUMNS FROM items").query(itemCodec.all())

    override fun selectAll(): List<Item> = tx.execute(selectAll)
}
