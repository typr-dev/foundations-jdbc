package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

@SuppressWarnings("unused")
public class StreamingReadProcess {
    Transactor tx = null; // placeholder

    //start
    // Process rows lazily without loading all into memory
    long countExpensiveProducts() {
        Operation<Cursor<Integer>> streaming =
            Fragment.of("SELECT price FROM products")
                .streamingQuery(PgTypes.int4, 512);

        return streaming.map(cursor -> {
            long count = 0;
            for (var price : cursor) {
                if (price > 100) count++;
            }
            return count;
        }).transact(tx);
    }
    //stop
}
