package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.OracleVArray;
import java.util.List;

@SuppressWarnings("unused")
public class VArrayTypes {
  // start
  // CREATE TYPE phone_list AS VARRAY(5) OF VARCHAR2(25);
  static final OracleType<List<String>> phoneList =
      OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2(25));

  // CREATE TYPE score_array AS VARRAY(100) OF NUMBER;
  static final OracleType<List<java.math.BigDecimal>> scores =
      OracleVArray.of("SCORE_ARRAY", 100, OracleTypes.number);
  // stop
}
