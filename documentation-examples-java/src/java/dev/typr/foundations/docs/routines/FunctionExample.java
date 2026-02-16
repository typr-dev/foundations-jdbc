package dev.typr.foundations.docs.routines;

import dev.typr.foundations.*;

import java.math.BigDecimal;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class FunctionExample {
    Transactor tx = null; // placeholder

    // Functions use SELECT instead of CALL — every DbType reads correctly
    static final DbFunction.Def2<BigDecimal, String, BigDecimal> calcTax =
        DbFunction.define("calculate_tax", PgTypes.numeric)
            .in(PgTypes.numeric)    // amount
            .in(PgTypes.text)       // region
            .build();

    // Zero-argument function
    static final DbFunction.Def0<Integer> nextId =
        DbFunction.define("next_id", PgTypes.int4)
            .build();

    //start
    BigDecimal calculateTax(BigDecimal amount, String region) throws SQLException {
        return calcTax.call(amount, region).transact(tx);
    }
    //stop
}
