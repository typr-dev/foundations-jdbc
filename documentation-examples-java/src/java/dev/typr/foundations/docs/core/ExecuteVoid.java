package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class ExecuteVoid {
    Transactor tx = null; // placeholder

    //start
    void applySchema() {
        tx.executeVoid(conn -> {
            Fragment.of("CREATE TABLE users (id INT, name VARCHAR(100))").execute().run(conn);
            Fragment.of("CREATE INDEX idx_users_name ON users (name)").execute().run(conn);
        });
    }
    //stop
}
