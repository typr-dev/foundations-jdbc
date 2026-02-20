package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Template;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.Optional;

@SuppressWarnings("unused")
public class TemplateBasic {
    record User(int id, String name, String email) {}

    static RowCodec<User> userCodec =
        RowCodec.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    Transactor tx = null; // placeholder

    //start
    // Define a reusable template — SQL structure is fixed, values come later
    Template<String, Optional<User>> findByEmail =
        Fragment.of("""
                SELECT id, name, email
                FROM users WHERE email =
                """)
            .param(PgTypes.text)
            .query(userCodec.maxOne());

    // Fill the template with a value to get a concrete operation
    Optional<User> findAlice() throws SQLException {
        return findByEmail.on("alice@example.com").transact(tx);
    }

    // Reuse the same template with different values
    Optional<User> findBob() throws SQLException {
        return findByEmail.on("bob@example.com").transact(tx);
    }
    //stop
}
