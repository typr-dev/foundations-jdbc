package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes
import dev.typr.foundations.data.Xml

@Suppress("unused")
class XmlType {
    //start
    val xmlType: PgType<Xml> = PgTypes.xml
    val doc: Xml = Xml("<root><child>text</child></root>")
    //stop
}
