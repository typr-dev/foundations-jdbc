package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Template;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class TemplateThenFrom {
    record NewUser(int id, String name) {}

    static RowCodec<NewUser> newUserCodec =
        RowCodec.<NewUser>builder()
            .field(PgTypes.int4, NewUser::id)
            .field(PgTypes.text, NewUser::name)
            .build(NewUser::new);

    Transactor tx = null; // placeholder

    //start
    // 1-param template: insert user, return id and name
    Template<String, NewUser> insertUser =
        Fragment.of("INSERT INTO users(name) VALUES(")
            .param(PgTypes.text)
            .append(") RETURNING id, name")
            .query(newUserCodec.exactlyOne());

    // 2-param template: log the creation with both fields
    Template.Update2<Integer, String> logCreation =
        Fragment.of("INSERT INTO audit_log(user_id, username) VALUES(")
            .param(PgTypes.int4)
            .append(", ")
            .param(PgTypes.text)
            .append(")")
            .update();

    // Chain: .from() adapts the 2-param template to accept NewUser
    int insertAndLog() {
        return insertUser.on("Alice")
            .then(logCreation.from(NewUser::id, NewUser::name))
            .transact(tx);
    }
    //stop
}
