package dev.typr.foundationskt.docs.db2

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class BinaryTypes {
    //start
    val binaryType: Db2Type<ByteArray> = Db2Types.binary
    val binary16: Db2Type<ByteArray> = Db2Types.binaryOf(16)
    val varbinaryType: Db2Type<ByteArray> = Db2Types.varbinary
    val blobType: Db2Type<ByteArray> = Db2Types.blob
    //stop
}
