package dev.typr.foundations.docs.db2;

import dev.typr.foundations.Db2Type;
import dev.typr.foundations.Db2Types;
import java.util.Optional;

@SuppressWarnings("unused")
public class NullableType {
  // start
  Db2Type<Integer> notNull = Db2Types.integer;
  Db2Type<Optional<Integer>> nullable = Db2Types.integer.opt();
  // stop
}
