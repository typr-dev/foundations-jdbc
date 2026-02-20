package dev.typr.foundations.docs.core;

import dev.typr.foundations.Transactor;

import java.sql.Connection;

@SuppressWarnings("unused")
public class TransactorCustomStrategy {
    //start
    Transactor.Strategy customStrategy =
        Transactor.Strategy.empty()
            .replaceBefore(conn -> conn.setAutoCommit(false))
            .replaceAfter(Connection::commit)
            .replaceOops((conn, throwable) -> { /* handle error */ })
            .replaceAlways(Connection::close);
    //stop
}
