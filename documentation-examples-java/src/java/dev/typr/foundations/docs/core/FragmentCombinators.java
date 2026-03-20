package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class FragmentCombinators {
  Connection conn = null; // placeholder

  // start
  // Dynamic UPDATE — only set the fields that changed
  void updateUser(int userId, String newName, String newEmail) {
    List<Fragment> sets = new ArrayList<>();
    if (newName != null) sets.add(Fragment.of("name = ").value(PgTypes.text, newName));
    if (newEmail != null) sets.add(Fragment.of("email = ").value(PgTypes.text, newEmail));

    if (!sets.isEmpty()) {
      Fragment.of("UPDATE users ")
          .append(Fragment.set(sets)) // SET name = ?, email = ?
          .append(" WHERE id = ")
          .value(PgTypes.int4, userId)
          .update()
          .run(conn);
    }
  }
  // stop
}
