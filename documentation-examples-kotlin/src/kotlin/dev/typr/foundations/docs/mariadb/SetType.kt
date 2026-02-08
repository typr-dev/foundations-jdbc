package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.kotlinfoundations.data.MariaSet

@Suppress("unused")
class SetType {
    //start
    val setType: MariaType<MariaSet> = MariaTypes.set

    // Create and use sets
    val values: MariaSet = MariaSet.of("read", "write")
    val csv: String = values.toCommaSeparated()
    //stop
}
