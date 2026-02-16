package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

@SuppressWarnings("unused")
public class FragmentBuilderBasic {
    //start
    void example() {
        // Build fragments programmatically with the builder pattern
        var frag =
            Fragment.of("""
                    SELECT * FROM users
                    WHERE id =
                    """)
                .value(PgTypes.int4, 42)
                .append(" AND active = ")
                .value(PgTypes.bool, true);
    }
    //stop
}
