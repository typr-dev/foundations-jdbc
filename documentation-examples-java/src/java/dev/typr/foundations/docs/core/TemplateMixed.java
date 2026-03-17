package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Template;
import dev.typr.foundations.Transactor;
import java.util.List;

@SuppressWarnings("unused")
public class TemplateMixed {
  record User(int id, String name, String status) {}

  static RowCodec<User> userCodec =
      RowCodec.<User>builder()
          .field(PgTypes.int4, User::id)
          .field(PgTypes.text, User::name)
          .field(PgTypes.text, User::status)
          .build(User::new);

  Transactor tx = null; // placeholder

  // start
  // Mix bound and unbound parameters in the same template.
  // Status is fixed at "active"; name filter and limit vary per call.
  Template.Query2<String, Integer, List<User>> activeUsersByName =
      Fragment.of(
              """
              SELECT id, name, status
              FROM users WHERE status =
              """)
          .value(PgTypes.text, "active")
          .append(" AND name ILIKE ")
          .param(PgTypes.text)
          .append(" ORDER BY name LIMIT ")
          .param(PgTypes.int4)
          .query(userCodec.all());

  List<User> example() {
    return activeUsersByName.on("%alice%", 10).transact(tx);
  }
  // stop
}
