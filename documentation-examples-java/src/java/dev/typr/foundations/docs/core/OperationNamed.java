package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import java.time.Duration;
import java.util.List;

@SuppressWarnings("unused")
public class OperationNamed {
  // start
  Operation<List<String>> users =
      Fragment.of("SELECT name FROM users")
          .query(RowCodec.of(PgTypes.text).all())
          .named("load-users")
          .timeout(Duration.ofSeconds(5));
  // stop
}
