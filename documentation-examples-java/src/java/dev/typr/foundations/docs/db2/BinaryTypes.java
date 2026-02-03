package dev.typr.foundations.docs.db2;

import dev.typr.foundations.Db2Type;
import dev.typr.foundations.Db2Types;

@SuppressWarnings("unused")
public class BinaryTypes {
    //start
    Db2Type<byte[]> binaryType = Db2Types.binary;
    Db2Type<byte[]> binary16 = Db2Types.binary(16);
    Db2Type<byte[]> varbinaryType = Db2Types.varbinary;
    Db2Type<byte[]> blobType = Db2Types.blob;
    //stop
}
