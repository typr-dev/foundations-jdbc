package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

import java.util.List;

@SuppressWarnings("unused")
public class StreamingReadBasic {
    Transactor tx = null; // placeholder

    //start
    // Stream rows lazily and materialize into a list
    List<String> allNames() {
        Operation<Cursor<String>> streaming =
            Fragment.of("SELECT name FROM users ORDER BY id")
                .streamingQuery(PgTypes.text, 512);

        return streaming
            .map(Cursor::toList)
            .transact(tx);
    }
    //stop
}
