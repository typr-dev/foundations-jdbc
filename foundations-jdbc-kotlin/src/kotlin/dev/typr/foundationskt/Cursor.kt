@file:Suppress("unused")
package dev.typr.foundationskt

class Cursor<Row>(val underlying: dev.typr.foundations.Cursor<Row>) : Iterator<Row>, AutoCloseable {

    override fun hasNext(): Boolean = underlying.hasNext()

    override fun next(): Row = underlying.next()

    fun asSequence(): Sequence<Row> = generateSequence {
        if (hasNext()) next() else null
    }

    fun toList(): List<Row> = underlying.toList()

    fun forEach(action: (Row) -> Unit) {
        while (hasNext()) {
            action(next())
        }
    }

    override fun close() {
        underlying.close()
    }
}
