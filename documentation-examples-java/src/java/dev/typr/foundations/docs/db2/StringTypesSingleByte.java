package dev.typr.foundations.docs.db2;

import dev.typr.foundations.Db2Type;
import dev.typr.foundations.Db2Types;

@SuppressWarnings("unused")
public class StringTypesSingleByte {
  // start
  Db2Type<String> charType = Db2Types.char_;
  Db2Type<String> char10 = Db2Types.char_(10);
  Db2Type<String> varcharType = Db2Types.varchar;
  Db2Type<String> varchar255 = Db2Types.varchar(255);
  Db2Type<String> clobType = Db2Types.clob;
  // stop
}
