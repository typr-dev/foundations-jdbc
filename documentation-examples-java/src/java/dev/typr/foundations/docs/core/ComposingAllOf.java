package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class ComposingAllOf {
    Transactor tx = null; // placeholder

    //start
    // Run multiple writes in one transaction, discard individual results
    Operation<Integer> insertUser =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .value(PgTypes.text, "Alice")
            .append(")")
            .update();
    Operation<Integer> insertAudit =
        Fragment.of("""
                INSERT INTO audit_log(action)
                VALUES(\
                """)
            .value(PgTypes.text, "user_created")
            .append(")")
            .update();
    Operation<Integer> updateStats =
        Fragment.of("""
                UPDATE stats
                SET user_count = user_count + 1""")
            .update();

    void createUserWithAudit() {
        Operation.allOf(
            insertUser, insertAudit, updateStats
        ).transact(tx);
    }
    //stop
}
