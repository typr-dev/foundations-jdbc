package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;

@SuppressWarnings("unused")
public class StringTypes {
    //start
    MariaType<String> charType = MariaTypes.char_(10);
    MariaType<String> varcharType = MariaTypes.varchar(255);
    MariaType<String> textType = MariaTypes.text;
    MariaType<String> longType = MariaTypes.longtext;
    //stop
}
