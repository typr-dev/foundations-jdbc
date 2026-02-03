package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.NonEmptyBlob;
import dev.typr.foundations.NonEmptyString;
import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

@SuppressWarnings("unused")
public class LobTypes {
    //start
    OracleType<String> clobType = OracleTypes.clob;
    OracleType<String> nclobType = OracleTypes.nclob;
    OracleType<byte[]> blobType = OracleTypes.blob;

    // Non-empty variants
    OracleType<NonEmptyString> clobNonEmpty = OracleTypes.clobNonEmpty;
    OracleType<NonEmptyBlob> blobNonEmpty = OracleTypes.blobNonEmpty;
    //stop
}
