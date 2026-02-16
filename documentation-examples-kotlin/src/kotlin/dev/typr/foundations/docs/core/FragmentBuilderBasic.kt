package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class FragmentBuilderBasic {
    //start
    fun example() {
        // Build fragments programmatically with the builder pattern
        val frag =
            Fragment.of("SELECT * FROM users WHERE id = ")
                .value(PgTypes.int4, 42)
                .append(" AND active = ")
                .value(PgTypes.bool, true)
    }
    //stop
}
