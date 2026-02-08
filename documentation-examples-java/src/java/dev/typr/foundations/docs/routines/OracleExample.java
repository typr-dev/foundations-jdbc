package dev.typr.foundations.docs.routines;

import dev.typr.foundations.*;

import java.math.BigDecimal;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class OracleExample {
    Transactor tx = null; // placeholder

    //start
    // Works with any database — just use the right types
    static final DbProcedure.Def2_1<BigDecimal, String, String> applyDiscount =
        DbProcedure.define("apply_discount")
            .in(OracleTypes.number)         // amount IN
            .inout(OracleTypes.varchar2)    // status INOUT
            .build();

    static final DbFunction.Def1<String, BigDecimal> getBalance =
        DbFunction.define("get_balance", OracleTypes.number)
            .in(OracleTypes.varchar2)       // account_id
            .build();
    //stop
}
