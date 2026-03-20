package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.data.NonEmptyString;

@SuppressWarnings("unused")
public class NonEmptyStringTypes {
  // start
  OracleType<NonEmptyString> nonEmpty = OracleTypes.varchar2NonEmpty(100);
  OracleType<NonEmptyString> nvarNonEmpty = OracleTypes.nvarchar2NonEmpty(100);
  // stop
}
