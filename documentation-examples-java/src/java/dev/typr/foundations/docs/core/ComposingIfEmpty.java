package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.SqlTemplate;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.Optional;

@SuppressWarnings("unused")
public class ComposingIfEmpty {
    record User(int id, String name, String email) {}

    static RowCodec<User> userParser =
        RowCodec.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    Transactor tx = null; // placeholder
    String email = "alice@example.com";
    String name = "Alice";

    //start
    // Find-or-create pattern
    SqlTemplate<String, Optional<User>> findUser =
        Fragment.of("""
                SELECT id, name, email
                FROM users WHERE email =
                """)
            .param(PgTypes.text)
            .query(userParser.maxOne());

    SqlTemplate.Query2<String, String, User> createUser =
        Fragment.of("""
                INSERT INTO users(name, email)
                VALUES(\
                """)
            .param(PgTypes.text).append(", ")
            .param(PgTypes.text)
            .append(") RETURNING *")
            .query(userParser.exactlyOne());

    User findOrCreate() throws SQLException {
        return Operation.ifEmpty(
            findUser.on(email),
            createUser.on(name, email)
        ).transact(tx);
    }
    //stop
}
