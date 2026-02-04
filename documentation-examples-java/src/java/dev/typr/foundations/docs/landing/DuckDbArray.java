package dev.typr.foundations.docs.landing;

import dev.typr.foundations.DuckDbTypes;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class DuckDbArray {
    Transactor tx = null; // placeholder

    //start
    // DuckDB arrays are first-class typed values
    List<String[]> getTagSets() throws SQLException {
        return Fragment.lit("SELECT tags FROM posts WHERE published = true")
            .query(RowParser.of(DuckDbTypes.varcharArray).all())
            .transact(tx);
    }
    //stop
}
