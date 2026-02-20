package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class SetType {
    //start
    val setType: MariaType<MariaSet> = MariaTypes.set

    // Create and use sets
    val values: MariaSet = MariaSet.of("read", "write")
    val csv: String = values.toCommaSeparated()
    //stop
}
