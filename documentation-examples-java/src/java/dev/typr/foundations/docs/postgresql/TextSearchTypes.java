package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;

@SuppressWarnings("unused")
public class TextSearchTypes {
    //start
    // Text search types are available via PgTypes
    // Note: tsvector and tsquery have specialized handling
    PgType<String> textType = PgTypes.text;
    //stop
}
