package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.math.BigDecimal
import java.sql.Connection
import java.time.OffsetDateTime
import java.util.*

fun ticketsByEvent(eventId: EventId): Operation.Query<List<Ticket>> =
    Sql { "SELECT ${ticketParser.columnList} FROM ticket WHERE event_id = ${eventIdType(eventId)} ORDER BY purchased" }
        .query(ticketParser.all())

fun ticketById(id: TicketId): Operation.Query<Ticket?> =
    Sql { "SELECT ${ticketParser.columnList} FROM ticket WHERE id = ${ticketIdType(id)}" }
        .query(ticketParser.maxOne())

fun insertTicket(ticket: Ticket): Operation.Query<Ticket> {
    val values = Fragment.of("").row(ticketParser, ticket)
    return Sql { "INSERT INTO ticket (${ticketParser.columnList}) VALUES ($values) RETURNING ${ticketParser.columnList}" }
        .query(ticketParser.exactlyOne())
}

fun countTicketsByEvent(eventId: EventId): Operation.Query<Long> =
    Sql { "SELECT count(*) FROM ticket WHERE event_id = ${eventIdType(eventId)}" }
        .queryOne(DuckDbTypes.bigint)

fun revenueByEvent(eventId: EventId): Operation.Query<Money> =
    Sql { "SELECT coalesce(sum(price), 0) FROM ticket WHERE event_id = ${eventIdType(eventId)}" }
        .queryOne(moneyType)

val eventSummaries: Operation.Query<List<EventSummary>> =
    Sql { """SELECT e.id, e.title, v.name, count(t.id), coalesce(sum(t.price), 0)
           FROM event e
           JOIN venue v ON e.venue_id = v.id
           LEFT JOIN ticket t ON t.event_id = e.id
           GROUP BY e.id, e.title, v.name
           ORDER BY e.title""" }
        .query(eventSummaryParser.all())

fun purchaseTicket(
    eventId: EventId, tier: TicketTier, holderName: String, holderEmail: String?,
    price: Money, seatNumbers: List<Int>
): Operation.Query<Ticket> {
    val ticket = Ticket(
        id = TicketId(UUID.randomUUID()),
        eventId = eventId,
        tier = tier,
        holderName = holderName,
        holderEmail = holderEmail,
        price = price,
        purchased = OffsetDateTime.now(),
        seatNumbers = seatNumbers
    )
    return insertTicket(ticket)
}

fun analyzeTicketQueries(conn: Connection): List<QueryAnalysis> = listOf(
    QueryAnalyzer.analyze(ticketsByEvent(EventId(0)).named("ticketsByEvent"), conn),
    QueryAnalyzer.analyze(ticketById(TicketId(UUID.randomUUID())).named("ticketById"), conn),
    QueryAnalyzer.analyze(insertTicket(Ticket(TicketId(UUID.randomUUID()), EventId(0), TicketTier.GENERAL, "", null, Money(BigDecimal.ZERO), OffsetDateTime.now(), emptyList())).named("insertTicket"), conn),
    QueryAnalyzer.analyze(countTicketsByEvent(EventId(0)).named("countTicketsByEvent"), conn),
    QueryAnalyzer.analyze(revenueByEvent(EventId(0)).named("revenueByEvent"), conn),
    QueryAnalyzer.analyze(eventSummaries.named("eventSummaries"), conn),
).flatten()
