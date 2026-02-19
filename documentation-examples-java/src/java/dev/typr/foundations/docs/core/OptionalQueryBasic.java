package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.SqlTemplate;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class OptionalQueryBasic {
    record User(int id, String name, String email) {}

    static RowParser<User> userParser =
        RowParser.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    Transactor tx = null; // placeholder

    //start
    // A search with an optional name filter.
    // When present, the filter is applied; when absent, it's skipped.
    SqlTemplate<Optional<String>, List<User>> searchUsers =
        Fragment.of("""
                SELECT id, name, email
                FROM users WHERE 1=1
                """)
            .optionally(
                Fragment.of(" AND name ILIKE ").param(PgTypes.text))
            .query(userParser.all());

    // Apply filter
    List<User> filtered() throws SQLException {
        return searchUsers.on(Optional.of("%alice%")).transact(tx);
    }

    // Skip filter — returns all users
    List<User> all() throws SQLException {
        return searchUsers.on(Optional.empty()).transact(tx);
    }
    //stop
}
