package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.time.Instant
import java.util.*

object TicketRepo {
    val ticketsByEvent: Template<EventId, List<Ticket>> =
        sql { "SELECT ${ticketCodec.columnList} FROM ticket WHERE event_id = " }
            .param(eventIdType)
            .query(ticketCodec.all())

    val ticketById: Template<TicketId, Ticket?> =
        sql { "SELECT ${ticketCodec.columnList} FROM ticket WHERE id = " }
            .param(ticketIdType)
            .query(ticketCodec.maxOne())

    val countTicketsByEvent: Template<EventId, Long> =
        sql { "SELECT count(*) FROM ticket WHERE event_id = " }
            .param(eventIdType)
            .query(RowCodec.of(DuckDbTypes.bigint).exactlyOne())

    val revenueByEvent: Template<EventId, Money> =
        sql { "SELECT coalesce(sum(price), 0) FROM ticket WHERE event_id = " }
            .param(eventIdType)
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

    val insertTicket: RowTemplate.Query<Ticket, Ticket> =
        Fragment.insertIntoReturning("ticket", ticketCodec)

    fun purchaseTicket(
        eventId: EventId, tier: TicketTier, holderName: String, holderEmail: String?,
        price: Money, seatNumbers: List<Int>
    ): OperationRead.Query<Ticket> = insertTicket.on(
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
