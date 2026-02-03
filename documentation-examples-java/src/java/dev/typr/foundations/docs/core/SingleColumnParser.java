package dev.typr.foundations.docs.core;

import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;

@SuppressWarnings("unused")
public class SingleColumnParser {
    //start
    RowParser<Integer> idParser = RowParser.of(PgTypes.int4);
    //stop
}
