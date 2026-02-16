package dev.typr.foundations.docs.core;

import dev.typr.foundations.Transactor;

import java.sql.Connection;

@SuppressWarnings("unused")
public class TransactorCustomStrategy {
    //start
    Transactor.Strategy customStrategy =
        new Transactor.Strategy(
            conn -> conn.setAutoCommit(false),
            Connection::commit,
            throwable -> { /* handle error */ },
            Connection::close
        );
    //stop
}
