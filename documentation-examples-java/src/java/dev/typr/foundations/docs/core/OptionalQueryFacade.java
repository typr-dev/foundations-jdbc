package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.SqlTemplate;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class OptionalQueryFacade {
    record User(int id, String name, String email) {}

    static RowParser<User> userParser =
        RowParser.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    Transactor tx = null; // placeholder

    //start
    // Package filters into a record so callers see a clean API
    record UserSearch(
        Optional<String> name,
        Optional<String> email,
        boolean activeOnly
    ) {}

    // .from() maps getters to template params
    private static final SqlTemplate.From<UserSearch, List<User>>
        searchTemplate = Fragment.of("""
                SELECT id, name, email FROM users WHERE 1=1
                """)
            .optionally(
                Fragment.of(" AND name ILIKE ").param(PgTypes.text))
            .optionally(
                Fragment.of(" AND email ILIKE ").param(PgTypes.text))
            .optionally(
                Fragment.of(" AND active = TRUE"))
            .append(" ORDER BY name")
            .query(userParser.all())
            .from(UserSearch::name, UserSearch::email,
                UserSearch::activeOnly);

    // Callers just pass the record
    List<User> searchUsers(UserSearch search) throws SQLException {
        return searchTemplate.on(search).transact(tx);
    }

    List<User> example() throws SQLException {
        var search = new UserSearch(
            Optional.of("%alice%"), Optional.empty(), true);
        return searchUsers(search);
    }
    //stop
}
