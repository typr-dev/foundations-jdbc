package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.Template;
import dev.typr.foundations.Transactor;

@SuppressWarnings("unused")
public class TemplateFrom {
    Transactor tx = null; // placeholder

    //start
    // A record gives names to each template parameter
    record InsertUser(String name, String email) {}

    // .from() maps record fields to template params
    Template.From<InsertUser, Integer> insertUser =
        Fragment.of("INSERT INTO users(name, email) VALUES(")
            .param(PgTypes.text)
            .append(", ")
            .param(PgTypes.text)
            .append(")")
            .update()
            .from(InsertUser::name, InsertUser::email);

    // Callers pass the record
    int createUser() {
        return insertUser.on(new InsertUser("Alice", "alice@example.com"))
            .transact(tx);
    }
    //stop
}
