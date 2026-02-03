package dev.typr.foundations.kotlin

import dev.typr.foundations.Fragment
import dev.typr.foundations.Operation

/**
 * Extension method on Fragment to accept Kotlin ResultSetParser.
 * Returns an Operation.Query that can be executed via Transactor.
 */
fun <T> Fragment.query(parser: ResultSetParser<T>): Operation.Query<T> =
    this.query(parser.underlying)
