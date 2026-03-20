@file:Suppress("unused")
package dev.typr.foundationskt

object StreamingInsert {
    private fun <T> Iterator<T>.asMutable(): MutableIterator<T> = object : MutableIterator<T> {
        override fun hasNext(): Boolean = this@asMutable.hasNext()
        override fun next(): T = this@asMutable.next()
        override fun remove() = throw UnsupportedOperationException()
    }

    @JvmStatic
    fun <T> of(copyCommand: String, batchSize: Int, rows: Iterator<T>, text: dev.typr.foundations.PgText<T>): Operation.StreamingCopy =
        Operation.StreamingCopy(dev.typr.foundations.StreamingInsert.of(copyCommand, batchSize, rows.asMutable(), text))

    @JvmStatic
    fun <T> insert(copyCommand: String, batchSize: Int, rows: Iterator<T>, c: java.sql.Connection, t: dev.typr.foundations.PgText<T>): Long =
        dev.typr.foundations.StreamingInsert.insert(copyCommand, batchSize, rows.asMutable(), c, t)

    @JvmStatic
    fun <T> insertUnchecked(copyCommand: String, batchSize: Int, rows: Iterator<T>, c: java.sql.Connection, t: dev.typr.foundations.PgText<T>): Long =
        dev.typr.foundations.StreamingInsert.insertUnchecked(copyCommand, batchSize, rows.asMutable(), c, t)
}
