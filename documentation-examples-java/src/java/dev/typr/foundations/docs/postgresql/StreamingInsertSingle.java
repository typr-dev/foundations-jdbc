package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.*;
import dev.typr.foundations.connect.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class StreamingInsertSingle {

    //start
    // Insert a list of strings using COPY
    long insertNames(List<String> names, Transactor tx) throws SQLException {
        return streamingInsert
            .of("COPY users(name) FROM STDIN", 1000, names.iterator(), PgTypes.text.pgText())
            .transact(tx);
    }
    //stop
}
