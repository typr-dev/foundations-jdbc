package dev.typr.foundations.docs.db2

import dev.typr.foundations.Db2Type
import dev.typr.foundations.Db2Types

@Suppress("unused")
class BinaryTypes {
    //start
    val binaryType: Db2Type<ByteArray> = Db2Types.binary
    val binary16: Db2Type<ByteArray> = Db2Types.binary(16)
    val varbinaryType: Db2Type<ByteArray> = Db2Types.varbinary
    val blobType: Db2Type<ByteArray> = Db2Types.blob
    //stop
}
