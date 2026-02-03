package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;

@SuppressWarnings("unused")
public class BinaryTypes {
    //start
    MariaType<byte[]> binaryType = MariaTypes.binary(16);
    MariaType<byte[]> varbinaryType = MariaTypes.varbinary(255);
    MariaType<byte[]> blobType = MariaTypes.blob;
    //stop
}
