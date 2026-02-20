package dev.typr.foundationssc.docs.postgresql
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*



@SuppressWarnings(Array("unused"))
object XmlType:
  //start
  val xmlType: PgType[Xml] = PgTypes.xml
  val doc: Xml = new Xml("<root><child>text</child></root>")
  //stop
