package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.SqlTemplate;
import dev.typr.foundations.Transactor;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("unused")
public class SqlTemplateMixed {
    record User(int id, String name, String status) {}

    static RowParser<User> userParser = RowParser.<User>builder()
        .field(PgTypes.int4, User::id)
        .field(PgTypes.text, User::name)
        .field(PgTypes.text, User::status)
        .build(User::new);

    Transactor tx = null; // placeholder

    //start
    // Mix bound and unbound parameters in the same template
    // The status is fixed at "active", but the limit varies per call
    SqlTemplate<Integer, List<User>> activeUsersWithLimit =
        Fragment.of("SELECT id, name, status FROM users WHERE status = ")
            .value(PgTypes.text, "active")
            .append(" ORDER BY name LIMIT ")
            .param(PgTypes.int4)
            .query(userParser.all());

    List<User> topTen() throws SQLException {
        return activeUsersWithLimit.on(10).transact(tx);
    }

    List<User> topFifty() throws SQLException {
        return activeUsersWithLimit.on(50).transact(tx);
    }
    //stop
}
