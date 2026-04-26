package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.time.Instant
import java.util.*

object TicketRepo {
    fun ticketsByEvent(eventId: EventId): OperationRead<List<Ticket>> =
        sql { "SELECT ${ticketCodec.columnList} FROM ticket WHERE event_id = " }
            .value(eventIdType, eventId)
            .query(ticketCodec.all())

    fun ticketById(id: TicketId): OperationRead<Ticket?> =
        sql { "SELECT ${ticketCodec.columnList} FROM ticket WHERE id = " }
            .value(ticketIdType, id)
            .query(ticketCodec.maxOne())

    fun countTicketsByEvent(eventId: EventId): OperationRead<Long> =
        sql { "SELECT count(*) FROM ticket WHERE event_id = " }
            .value(eventIdType, eventId)
            .query(RowCodec.of(DuckDbTypes.bigint).exactlyOne())

    fun revenueByEvent(eventId: EventId): OperationRead<Money> =
        sql { "SELECT coalesce(sum(price), 0) FROM ticket WHERE event_id = " }
            .value(eventIdType, eventId)
            .query(RowCodec.of(moneyType).exactlyOne())

    val eventSummaries: OperationRead<List<EventSummary>> =
        sql {
            """SELECT e.id, e.title, v.name, count(t.id), coalesce(sum(t.price), 0)
               FROM event e
               JOIN venue v ON e.venue_id = v.id
               LEFT JOIN ticket t ON t.event_id = e.id
               GROUP BY e.id, e.title, v.name
               ORDER BY e.title"""
        }
            .query(eventSummaryCodec.all())

    fun purchaseTicket(
        eventId: EventId, tier: TicketTier, holderName: String, holderEmail: String?,
        price: Money, seatNumbers: List<Int>
    ): OperationRead.Query<Ticket> = Fragment.insertOneReturning(
        "ticket", ticketCodec,
        Ticket(
            id = TicketId(UUID.randomUUID()),
            eventId = eventId,
            tier = tier,
            holderName = holderName,
            holderEmail = holderEmail,
            price = price,
            purchased = Instant.now(),
            seatNumbers = seatNumbers
        )
    )

}
