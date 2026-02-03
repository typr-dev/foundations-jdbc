package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;

@SuppressWarnings("unused")
public class StringTypes {
    //start
    PgType<String> textType = PgTypes.text;
    PgType<String> charType = PgTypes.bpchar(10);  // char(10)
    //stop
}
