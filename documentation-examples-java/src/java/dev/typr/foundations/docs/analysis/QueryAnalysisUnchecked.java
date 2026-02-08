package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.analysis.QueryAnalysis;
import dev.typr.foundations.analysis.QueryAnalyzer;
import java.sql.Connection;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class QueryAnalysisUnchecked {
    private final Connection connection = null; // placeholder

    //start
    record Stats(String name, int count) {}

    // .unchecked() skips type checking entirely for this column
    RowParser<Stats> statsParser = RowParser.<Stats>builder()
        .field(PgTypes.text, Stats::name)
        .field(PgTypes.int4.unchecked(), Stats::count)
        .build(Stats::new);
    //stop
}
