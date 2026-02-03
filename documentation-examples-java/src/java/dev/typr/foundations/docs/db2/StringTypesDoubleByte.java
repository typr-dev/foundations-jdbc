package dev.typr.foundations.docs.db2;

import dev.typr.foundations.Db2Type;
import dev.typr.foundations.Db2Types;

@SuppressWarnings("unused")
public class StringTypesDoubleByte {
    //start
    Db2Type<String> graphicType = Db2Types.graphic;
    Db2Type<String> graphic10 = Db2Types.graphic(10);
    Db2Type<String> vargraphicType = Db2Types.vargraphic;
    Db2Type<String> dbclobType = Db2Types.dbclob;
    //stop
}
