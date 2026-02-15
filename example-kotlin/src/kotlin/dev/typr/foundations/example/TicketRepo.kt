package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.math.BigDecimal
import java.sql.Connection
import java.time.OffsetDateTime
import java.util.*

object TicketRepo {
    private val selectByEventTemplate = Fragment.of("SELECT ")
        .append(ticketParser.columnList).append(" FROM ticket WHERE event_id = ")
        .param(eventIdType).append(" ORDER BY purchased")
        .query(ticketParser.all())

    private val selectByIdTemplate = Fragment.of("SELECT ")
        .append(ticketParser.columnList).append(" FROM ticket WHERE id = ")
        .param(ticketIdType)
        .query(ticketParser.maxOne())

    private val insertTemplate = Fragment.of("INSERT INTO ticket (")
        .append(ticketParser.columnList).append(") VALUES (")
        .paramRow(ticketParser)
        .append(")")
        .append(" RETURNING ").append(ticketParser.columnList)
        .query(ticketParser.exactlyOne())

    private val countByEventTemplate = Fragment.of("SELECT count(*) FROM ticket WHERE event_id = ")
        .param(eventIdType)
        .query(RowParser.of(DuckDbTypes.bigint).exactlyOne())

    private val revenueByEventTemplate = Fragment.of("SELECT coalesce(sum(price), 0) FROM ticket WHERE event_id = ")
        .param(eventIdType)
        .query(RowParser.of(moneyType).exactlyOne())

    private val summaryByEvent = Sql { """SELECT e.id, e.title, v.name, count(t.id), coalesce(sum(t.price), 0)
           FROM event e
           JOIN venue v ON e.venue_id = v.id
           LEFT JOIN ticket t ON t.event_id = e.id
           GROUP BY e.id, e.title, v.name
           ORDER BY e.title""" }
        .query(eventSummaryParser.all())

    fun findByEventOp(eventId: EventId): Operation.Query<List<Ticket>> =
        selectByEventTemplate.on(eventId)

    fun findByEvent(eventId: EventId, conn: Connection): List<Ticket> =
        findByEventOp(eventId).run(conn)

    fun findById(id: TicketId, conn: Connection): Ticket? =
        selectByIdTemplate.on(id).run(conn)

    fun purchase(
        eventId: EventId, tier: TicketTier, holderName: String, holderEmail: String?,
        price: Money, seatNumbers: List<Int>, conn: Connection
    ): Ticket {
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
        return insertTemplate.on(ticket).run(conn)
    }

    fun countByEvent(eventId: EventId, conn: Connection): Long =
        countByEventTemplate.on(eventId).run(conn)

    fun revenueByEvent(eventId: EventId, conn: Connection): Money =
        revenueByEventTemplate.on(eventId).run(conn)

    fun eventSummariesOp(): Operation.Query<List<EventSummary>> = summaryByEvent

    fun eventSummaries(conn: Connection): List<EventSummary> =
        summaryByEvent.run(conn)

    fun analyzeQueries(conn: Connection): List<QueryAnalysis> = listOf(
        QueryAnalyzer.analyze("TicketRepo.selectByEvent", selectByEventTemplate, conn),
        QueryAnalyzer.analyze("TicketRepo.selectById", selectByIdTemplate, conn),
        QueryAnalyzer.analyze("TicketRepo.insertReturning", insertTemplate, conn),
        QueryAnalyzer.analyze("TicketRepo.countByEvent", countByEventTemplate, conn),
        QueryAnalyzer.analyze("TicketRepo.revenueByEvent", revenueByEventTemplate, conn),
        QueryAnalyzer.analyze("TicketRepo.summaryByEvent", summaryByEvent, conn),
    ).flatten()
}
