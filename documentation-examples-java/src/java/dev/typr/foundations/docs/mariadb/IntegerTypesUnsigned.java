package dev.typr.foundations.docs.mariadb;

import dev.typr.foundations.MariaType;
import dev.typr.foundations.MariaTypes;
import dev.typr.foundations.data.Uint1;
import dev.typr.foundations.data.Uint4;
import dev.typr.foundations.data.Uint8;

@SuppressWarnings("unused")
public class IntegerTypesUnsigned {
  // start
  MariaType<Uint1> unsignedTiny = MariaTypes.tinyintUnsigned;
  MariaType<Uint4> unsignedInt = MariaTypes.intUnsigned;
  MariaType<Uint8> unsignedBig = MariaTypes.bigintUnsigned;
  // stop
}
