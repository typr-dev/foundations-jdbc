package dev.typr.foundations.docs.postgresql
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object XmlType:
  //start
  val xmlType: PgType[Xml] = PgTypes.xml
  val doc: Xml = new Xml("<root><child>text</child></root>")
  //stop
