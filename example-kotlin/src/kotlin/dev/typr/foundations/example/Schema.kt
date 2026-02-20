package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.sql.Connection

object Schema {
    private val DDL = listOf(
        "CREATE TYPE event_status AS ENUM ('DRAFT', 'PUBLISHED', 'CANCELLED', 'SOLD_OUT', 'COMPLETED')",
        "CREATE TYPE ticket_tier AS ENUM ('GENERAL', 'VIP', 'BACKSTAGE')",
        "CREATE SEQUENCE venue_id_seq START 1",
        """
        CREATE TABLE venue (
            id              BIGINT DEFAULT nextval('venue_id_seq'),
            name            VARCHAR NOT NULL,
            address         STRUCT(street VARCHAR, city VARCHAR, state VARCHAR, zip VARCHAR, country VARCHAR) NOT NULL,
            capacity        INTEGER NOT NULL,
            tags            VARCHAR[] NOT NULL,
            metadata        MAP(VARCHAR, VARCHAR) NOT NULL
        )
        """,
        "CREATE SEQUENCE event_id_seq START 1",
        """
        CREATE TABLE event (
            id              BIGINT DEFAULT nextval('event_id_seq'),
            venue_id        BIGINT NOT NULL,
            title           VARCHAR NOT NULL,
            description     VARCHAR,
            status          event_status NOT NULL DEFAULT 'DRAFT',
            date            TIMESTAMPTZ NOT NULL,
            door_open       DATE NOT NULL,
            base_price      DECIMAL(12,2) NOT NULL,
            tags            VARCHAR[] NOT NULL DEFAULT [],
            ratings         DOUBLE[] NOT NULL DEFAULT []
        )
        """,
        """
        CREATE TABLE ticket (
            id              UUID,
            event_id        BIGINT NOT NULL,
            tier            ticket_tier NOT NULL,
            holder_name     VARCHAR NOT NULL,
            holder_email    VARCHAR,
            price           DECIMAL(12,2) NOT NULL,
            purchased       TIMESTAMPTZ NOT NULL,
            seat_numbers    INTEGER[] NOT NULL DEFAULT []
        )
        """
    )

    fun apply(conn: Connection) {
        DDL.forEach { Fragment.of(it).execute().run(conn) }
    }
}
