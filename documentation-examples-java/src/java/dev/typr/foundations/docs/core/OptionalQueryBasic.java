package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Template;
import dev.typr.foundations.Transactor;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class OptionalQueryBasic {
    record User(int id, String name, String email) {}

    static RowCodec<User> userCodec =
        RowCodec.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    Transactor tx = null; // placeholder

    //start
    // A search with an optional name filter.
    // When present, the filter is applied; when absent, it's skipped.
    Template<Optional<String>, List<User>> searchUsers =
        Fragment.of("""
                SELECT id, name, email
                FROM users WHERE 1=1
                """)
            .optionally(
                Fragment.of(" AND name ILIKE ").param(PgTypes.text))
            .query(userCodec.all());

    // Apply filter
    List<User> filtered() {
        return searchUsers.on(Optional.of("%alice%")).transact(tx);
    }

    // Skip filter — returns all users
    List<User> all() {
        return searchUsers.on(Optional.empty()).transact(tx);
    }
    //stop
}
