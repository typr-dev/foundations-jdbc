package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.QueryAnalysis;
import dev.typr.foundations.QueryAnalyzer;
import java.math.BigDecimal;
import java.sql.Connection;

@SuppressWarnings("unused")
public class QueryAnalysisNullableOk {
    private final Connection connection = null; // placeholder

    //start
    record OrderRow(int userId, String userName, BigDecimal orderTotal) {}

    // The LEFT JOIN makes o.total nullable in the result set,
    // but .nullableOk() tells analysis we'll handle it
    RowCodec<OrderRow> orderCodec =
        RowCodec.<OrderRow>builder()
            .field(PgTypes.int4, OrderRow::userId)
            .field(PgTypes.text, OrderRow::userName)
            .field(PgTypes.numeric.nullableOk(),
                OrderRow::orderTotal)
            .build(OrderRow::new);

    void analyzeLeftJoin() {
        var query = Fragment.of("""
            SELECT u.id, u.name, o.total
            FROM users u
            LEFT JOIN orders o ON u.id = o.user_id
            """).query(orderCodec.all());

        QueryAnalysis analysis =
            QueryAnalyzer.analyze(query, connection)
                .getFirst();
        if (!analysis.succeeded()) {
            throw new AssertionError(analysis.report());
        }
    }
    //stop
}
