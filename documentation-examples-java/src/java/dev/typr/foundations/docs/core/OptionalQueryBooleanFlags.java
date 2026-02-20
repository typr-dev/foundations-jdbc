package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Template;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class OptionalQueryBooleanFlags {
    record User(int id, String name, String email) {}

    static RowCodec<User> userCodec =
        RowCodec.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    Transactor tx = null; // placeholder

    //start
    Template<Boolean, List<User>> activeUsers =
        Fragment.of("""
                SELECT id, name, email
                FROM users WHERE 1=1
                """)
            .optionally(Fragment.of(" AND active = TRUE"))
            .query(userCodec.all());

    // Include the filter
    List<User> onlyActive() throws SQLException {
        return activeUsers.on(true).transact(tx);
    }

    // Skip the filter — returns all users
    List<User> all() throws SQLException {
        return activeUsers.on(false).transact(tx);
    }
    //stop
}
