package dev.typr.foundations.docs.db2;

import dev.typr.foundations.Db2Type;
import dev.typr.foundations.Db2Types;
import dev.typr.foundations.data.Xml;

@SuppressWarnings("unused")
public class SpecialTypes {
  // start
  Db2Type<Xml> xmlType = Db2Types.xml;
  Db2Type<byte[]> rowidType = Db2Types.rowid;
  // stop
}
