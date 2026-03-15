package dev.typr.foundationskt.docs.oracle

import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes
import dev.typr.foundations.OracleVArray
import java.math.BigDecimal

@Suppress("unused")
class VArrayTypes {
    //start
    // CREATE TYPE phone_list AS VARRAY(5) OF VARCHAR2(25);
    val phoneList: OracleType<List<String>> =
        OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2(25))

    // CREATE TYPE score_array AS VARRAY(100) OF NUMBER;
    val scores: OracleType<List<BigDecimal>> =
        OracleVArray.of("SCORE_ARRAY", 100, OracleTypes.number)
    //stop
}
