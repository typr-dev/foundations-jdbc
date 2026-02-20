package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.math.BigDecimal
import java.sql.Connection
import java.time.OffsetDateTime
import java.util.*

fun ticketsByEvent(eventId: EventId): Operation.Query<List<Ticket>> =
    sql { "SELECT ${ticketCodec.columnList} FROM ticket WHERE event_id = ${eventIdType(eventId)} ORDER BY purchased" }
        .query(ticketCodec.all())

fun ticketById(id: TicketId): Operation.Query<Ticket?> =
    sql { "SELECT ${ticketCodec.columnList} FROM ticket WHERE id = ${ticketIdType(id)}" }
        .query(ticketCodec.maxOne())

fun insertTicket(ticket: Ticket): Operation.Query<Ticket> {
    val values = Fragment.row(ticketCodec, ticket)
    return sql { "INSERT INTO ticket (${ticketCodec.columnList}) VALUES ($values) RETURNING ${ticketCodec.columnList}" }
        .query(ticketCodec.exactlyOne())
}

fun countTicketsByEvent(eventId: EventId): Operation.Query<Long> =
    sql { "SELECT count(*) FROM ticket WHERE event_id = ${eventIdType(eventId)}" }
        .queryExactlyOne(DuckDbTypes.bigint)

fun revenueByEvent(eventId: EventId): Operation.Query<Money> =
    sql { "SELECT coalesce(sum(price), 0) FROM ticket WHERE event_id = ${eventIdType(eventId)}" }
        .queryExactlyOne(moneyType)

val eventSummaries: Operation.Query<List<EventSummary>> =
    sql { """SELECT e.id, e.title, v.name, count(t.id), coalesce(sum(t.price), 0)
           FROM event e
           JOIN venue v ON e.venue_id = v.id
           LEFT JOIN ticket t ON t.event_id = e.id
           GROUP BY e.id, e.title, v.name
           ORDER BY e.title""" }
        .query(eventSummaryCodec.all())

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
