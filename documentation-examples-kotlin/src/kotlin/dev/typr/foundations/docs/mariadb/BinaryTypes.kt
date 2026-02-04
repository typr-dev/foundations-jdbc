package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class BinaryTypes {
    //start
    val binaryType: MariaType<ByteArray> = MariaTypes.binary(16)
    val varbinaryType: MariaType<ByteArray> = MariaTypes.varbinary(255)
    val blobType: MariaType<ByteArray> = MariaTypes.blob
    //stop
}
