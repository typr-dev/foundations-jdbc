package dev.typr.foundations.docs.core;

import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;

@SuppressWarnings("unused")
public class SingleColumnParser {
    //start
    RowCodec<Integer> idParser = RowCodec.of(PgTypes.int4);
    //stop
}
