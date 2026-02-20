package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import dev.typr.foundations.connect.*;

import java.sql.SQLException;

@SuppressWarnings("unused")
public class GettingStarted {
    //start
    Transactor tx =
        SingleConnectionDataSource.create(
                DuckDbConfig.inMemory().build())
            .transactor();

    int result = Fragment.of("SELECT 42")
        .query(RowCodec.of(DuckDbTypes.integer).exactlyOne())
        .transact(tx);
    //stop

    GettingStarted() throws SQLException {}
}
