package dev.typr.foundations.docs.mariadb

import dev.typr.foundations.MariaType
import dev.typr.foundations.MariaTypes

@Suppress("unused")
class BinaryTypes {
    //start
    val binaryType: MariaType<ByteArray> = MariaTypes.binary(16)
    val varbinaryType: MariaType<ByteArray> = MariaTypes.varbinary(255)
    val blobType: MariaType<ByteArray> = MariaTypes.blob
    //stop
}
