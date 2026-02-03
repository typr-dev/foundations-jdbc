package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.MariaType
import dev.typr.foundations.MariaTypes
import dev.typr.foundations.data.maria.MariaSet

@Suppress("unused")
class SetType {
    //start
    val setType: MariaType<MariaSet> = MariaTypes.set

    // Create and use sets
    val values: MariaSet = MariaSet.of("read", "write")
    val csv: String = values.toCommaSeparated()
    //stop
}
