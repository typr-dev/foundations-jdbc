package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.kotlinfoundations.data.Xml

@Suppress("unused")
class XmlType {
    //start
    val xmlType: PgType<Xml> = PgTypes.xml
    val doc: Xml = Xml("<root><child>text</child></root>")
    //stop
}
