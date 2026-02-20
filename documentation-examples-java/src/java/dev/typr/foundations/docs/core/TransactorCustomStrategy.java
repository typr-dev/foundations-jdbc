package dev.typr.foundations.docs.core;

import dev.typr.foundations.Transactor;

import java.sql.Connection;

@SuppressWarnings("unused")
public class TransactorCustomStrategy {
    //start
    Transactor.Strategy customStrategy =
        Transactor.Strategy.empty()
            .withBefore(conn -> conn.setAutoCommit(false))
            .withAfter(Connection::commit)
            .withOops((conn, throwable) -> { /* handle error */ })
            .withAlways(Connection::close);
    //stop
}
