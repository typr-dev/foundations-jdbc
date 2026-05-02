package dev.typr.foundations.docs.sqlite;

import dev.typr.foundations.SqliteType;
import dev.typr.foundations.SqliteTypes;

@SuppressWarnings("unused")
public class StringTypes {
  // start
  SqliteType<String> textType = SqliteTypes.text; // TEXT (canonical)
  SqliteType<String> varcharType = SqliteTypes.varcharOf(255); // VARCHAR(255) — length is a label
  SqliteType<String> charType = SqliteTypes.charOf(10);
  SqliteType<String> clobType = SqliteTypes.clob;
  // stop
}
