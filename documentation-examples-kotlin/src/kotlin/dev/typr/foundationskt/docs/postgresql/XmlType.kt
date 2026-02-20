package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class XmlType {
    //start
    val xmlType: PgType<Xml> = PgTypes.xml
    val doc: Xml = Xml("<root><child>text</child></root>")
    //stop
}
