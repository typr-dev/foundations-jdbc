package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.QueryChecker;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Template;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class OptionalQueryMulti {
    record User(int id, String name, String email) {}

    static RowCodec<User> userCodec =
        RowCodec.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    Transactor tx = null; // placeholder
    QueryChecker checker = null; // placeholder

    //start
    // Multiple optional filters — each independently present or absent
    Template.Query3<Optional<String>, Optional<String>, Boolean, List<User>>
        search = Fragment.of("""
                SELECT id, name, email FROM users WHERE 1=1
                """)
            .optionally(
                Fragment.of(" AND name ILIKE ").param(PgTypes.text))
            .optionally(
                Fragment.of(" AND email ILIKE ").param(PgTypes.text))
            .optionally(
                Fragment.of(" AND active = TRUE"))
            .append(" ORDER BY name")
            .query(userCodec.all());

    // Each combination is type-safe
    List<User> example() throws SQLException {
        return search
            .on(Optional.of("%alice%"), Optional.empty(), true)
            .transact(tx);
    }

    // Query analysis expands all 2³ = 8 combinations automatically
    void verifyAllVariants() throws SQLException {
        checker.check(search);
    }
    //stop
}
