package dev.typr.foundationssc.docs.oracle
import dev.typr.foundations.{OracleType, OracleTypes, OracleVArray}

@SuppressWarnings(Array("unused"))
object VArrayTypes:
  //start
  // CREATE TYPE phone_list AS VARRAY(5) OF VARCHAR2(25);
  val phoneList: OracleType[java.util.List[String]] =
    OracleVArray.of("PHONE_LIST", 5, OracleTypes.varchar2(25))

  // CREATE TYPE score_array AS VARRAY(100) OF NUMBER;
  val scores: OracleType[java.util.List[java.math.BigDecimal]] =
    OracleVArray.of("SCORE_ARRAY", 100, OracleTypes.number)
  //stop
