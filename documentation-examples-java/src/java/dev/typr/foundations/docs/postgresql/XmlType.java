package dev.typr.foundations.docs.postgresql;

import dev.typr.foundations.PgType;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.data.Xml;

@SuppressWarnings("unused")
public class XmlType {
  // start
  PgType<Xml> xmlType = PgTypes.xml;
  Xml doc = new Xml("<root><child>text</child></root>");
  // stop
}
