package dev.typr.foundations.docs.core;

import dev.typr.foundations.Connection;
import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import java.time.Instant;
import java.util.List;

@SuppressWarnings("unused")
public class FragmentBuilding {
  record User(Integer id, String name, String status, Instant createdAt) {}

  RowCodec<User> userCodec =
      RowCodec.<User>builder()
          .field(PgTypes.int4, User::id)
          .field(PgTypes.text, User::name)
          .field(PgTypes.text, User::status)
          .field(PgTypes.timestamptz, User::createdAt)
          .build(User::new);

  Connection connection = null; // placeholder
  Integer userId = 1;
  Instant cutoffDate = Instant.now();

  // start
  Fragment query =
      Fragment.of(
              """
              SELECT * FROM users
              WHERE id =
              """)
          .value(PgTypes.int4, userId)
          .append(" AND status = ")
          .value(PgTypes.text, "active")
          .append(" AND created_at > ")
          .value(PgTypes.timestamptz, cutoffDate);

  // Execute safely — parameters are bound, not interpolated
  List<User> users = query.query(userCodec.all()).run(connection);
  // stop
}
