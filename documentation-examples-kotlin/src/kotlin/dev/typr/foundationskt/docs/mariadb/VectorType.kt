package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.MariaType
import dev.typr.foundationskt.MariaTypes
import dev.typr.foundations.data.Vector

@Suppress("unused")
class VectorType {
    //start
    val embedding: MariaType<Vector> = MariaTypes.vector(1536)
    //stop
}
