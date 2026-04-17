package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class BinaryTypes {
    //start
    val binaryType: MariaType<ByteArray> = MariaTypes.binaryOf(16)
    val varbinaryType: MariaType<ByteArray> = MariaTypes.varbinaryOf(255)
    val blobType: MariaType<ByteArray> = MariaTypes.blob
    //stop
}
