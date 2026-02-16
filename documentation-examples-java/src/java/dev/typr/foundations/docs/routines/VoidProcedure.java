package dev.typr.foundations.docs.routines;

import dev.typr.foundations.*;

import java.sql.SQLException;

@SuppressWarnings("unused")
public class VoidProcedure {
    Transactor tx = null; // placeholder

    //start
    // A void procedure — no OUT parameters, just side effects
    static final DbProcedure.Def2_0<String, String> auditLog =
        DbProcedure.define("audit_log")
            .input(PgTypes.text)
            .input(PgTypes.text)
            .build();

    void logAction(
        String action, String details
    ) throws SQLException {
        auditLog
            .call(action, details)
            .transact(tx);
    }
    //stop
}
