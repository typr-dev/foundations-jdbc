package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;

import java.sql.Connection;
import java.time.Instant;
import java.util.List;

@SuppressWarnings("unused")
public class FragmentBuilding {
    record User(Integer id, String name, String status, Instant createdAt) {}

    RowParser<User> userParser = RowParser.<User>builder()
        .field(PgTypes.int4, User::id)
        .field(PgTypes.text, User::name)
        .field(PgTypes.text, User::status)
        .field(PgTypes.timestamptz, User::createdAt)
        .build(User::new);

    Connection connection = null; // placeholder
    Integer userId = 1;
    Instant cutoffDate = Instant.now();

    //start
    Fragment query = Fragment.interpolate("SELECT * FROM users WHERE id = ")
        .param(PgTypes.int4, userId)
        .sql(" AND status = ")
        .param(PgTypes.text, "active")
        .sql(" AND created_at > ")
        .param(PgTypes.timestamptz, cutoffDate)
        .done();

    // Execute safely — parameters are bound, not interpolated
    List<User> users = query.query(userParser.all()).runUnchecked(connection);
    //stop
}
