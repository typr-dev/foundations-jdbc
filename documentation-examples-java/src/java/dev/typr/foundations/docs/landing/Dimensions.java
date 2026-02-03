package dev.typr.foundations.docs.landing;

import dev.typr.foundations.PgStruct;
import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;

@SuppressWarnings("unused")
public class Dimensions {
    //start
    record Dim(
        Double width, Double height, Double depth, String unit
    ) {}

    // PgStruct handles PostgreSQL's composite wire format
    static PgStruct<Dim> pgStruct = PgStruct.<Dim>builder("dimensions")
        .field("width", PgTypes.float8, Dim::width)
        .field("height", PgTypes.float8, Dim::height)
        .field("depth", PgTypes.float8, Dim::depth)
        .field("unit", PgTypes.text, Dim::unit)
        .build(Dim::new);

    static PgType<Dim> pgType = pgStruct.asType();
    //stop
}
