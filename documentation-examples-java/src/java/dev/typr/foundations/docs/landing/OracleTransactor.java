package dev.typr.foundations.docs.landing;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;
import dev.typr.foundations.connect.OracleConfig;

import java.sql.SQLException;

@SuppressWarnings("unused")
public class OracleTransactor {
    //start
    // Oracle - typed config, no JDBC URL to remember
    Transactor tx =
        OracleConfig.builder(
                "localhost", 1521, "xe", "app", "secret")
            .serviceName("XEPDB1")
            .transactor();

    // Everything inside runs in one transaction
    String getGreeting() throws SQLException {
        return Fragment
            .of("SELECT 'Hello from Oracle' FROM dual")
            .query(RowCodec.of(OracleTypes.varchar2)
                .exactlyOne())
            .transact(tx);
    }

    //stop
}
