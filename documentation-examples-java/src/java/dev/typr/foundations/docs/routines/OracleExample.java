package dev.typr.foundations.docs.routines;

import dev.typr.foundations.*;

import java.math.BigDecimal;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class OracleExample {
    Transactor tx = null; // placeholder

    //start
    // Works with any database — just use the right types
    void example() {
        var applyDiscount =
            DbProcedure.define("apply_discount")
                .input(OracleTypes.number)
                .inout(OracleTypes.varchar2)
                .build();

        var getBalance =
            DbFunction.define(
                    "get_balance", OracleTypes.number)
                .input(OracleTypes.varchar2)
                .build();
    }
    //stop
}
