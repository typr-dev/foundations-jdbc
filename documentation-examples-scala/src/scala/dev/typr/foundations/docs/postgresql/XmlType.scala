package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.{PgType, PgTypes}
import dev.typr.foundations.data.Xml

@SuppressWarnings(Array("unused"))
object XmlType:
  //start
  val xmlType: PgType[Xml] = PgTypes.xml
  val doc: Xml = new Xml("<root><child>text</child></root>")
  //stop
