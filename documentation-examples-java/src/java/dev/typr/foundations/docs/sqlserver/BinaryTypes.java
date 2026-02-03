package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;

@SuppressWarnings("unused")
public class BinaryTypes {
    //start
    SqlServerType<byte[]> binaryType = SqlServerTypes.binary(16);
    SqlServerType<byte[]> varbinaryType = SqlServerTypes.varbinary(255);
    SqlServerType<byte[]> varbinaryMax = SqlServerTypes.varbinaryMax;
    //stop
}
