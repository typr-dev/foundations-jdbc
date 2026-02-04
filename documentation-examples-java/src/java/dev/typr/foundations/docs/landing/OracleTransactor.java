package dev.typr.foundations.docs.landing;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.oracle.OracleConfig;

import java.sql.SQLException;

@SuppressWarnings("unused")
public class OracleTransactor {
    //start
    // Oracle - typed config, no JDBC URL to remember
    Transactor tx = OracleConfig.builder("localhost", 1521, "xe", "app", "secret")
        .serviceName("XEPDB1")
        .build()
        .transactor();

    // Everything inside runs in one transaction
    String getGreeting() throws SQLException {
        return Fragment.lit("SELECT 'Hello from Oracle' FROM dual")
            .query(RowParser.of(OracleTypes.varchar2).exactlyOne())
            .transact(tx);
    }

    // Built-in strategies for common patterns
    Transactor.Strategy defaultStrategy = Transactor.defaultStrategy();         // begin -> commit -> close
    Transactor.Strategy autoCommit = Transactor.autoCommitStrategy();           // no transaction, just close
    Transactor.Strategy rollbackOnError = Transactor.rollbackOnErrorStrategy(); // begin -> commit, rollback on error -> close
    Transactor.Strategy test = Transactor.testStrategy();                       // begin -> rollback -> close (for tests)
    //stop
}
