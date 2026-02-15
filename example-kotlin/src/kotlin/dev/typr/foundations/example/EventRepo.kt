package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.sql.Connection

object EventRepo {
    private val selectAll = Sql { "SELECT ${eventParser.columnList} FROM event ORDER BY date" }
        .query(eventParser.all())

    private val selectByIdTemplate = Fragment.of("SELECT ")
        .append(eventParser.columnList).append(" FROM event WHERE id = ")
        .param(eventIdType)
        .query(eventParser.maxOne())

    private val selectByStatusTemplate = Fragment.of("SELECT ")
        .append(eventParser.columnList).append(" FROM event WHERE status = ")
        .param(eventStatusType)
        .query(eventParser.all())

    private val selectByVenueTemplate = Fragment.of("SELECT ")
        .append(eventParser.columnList).append(" FROM event WHERE venue_id = ")
        .param(venueIdType)
        .query(eventParser.all())

    private val insertTemplate = Fragment.of("INSERT INTO event (")
        .append(eventParser.columnList).append(") VALUES (nextval('event_id_seq'), ")
        .paramRow(eventParser, "id")
        .append(")")
        .append(" RETURNING ").append(eventParser.columnList)
        .query(eventParser.exactlyOne())

    private val updateStatusTemplate = Fragment.of("UPDATE event SET status = ")
        .param(eventStatusType).append(" WHERE id = ")
        .param(eventIdType)
        .update()

    private val addRatingTemplate = Fragment.of("UPDATE event SET ratings = list_append(ratings, ")
        .param(DuckDbTypes.double_).append(") WHERE id = ")
        .param(eventIdType)
        .update()

    fun findByIdOp(id: EventId): Operation.Query<Event?> =
        selectByIdTemplate.on(id)

    fun findAll(conn: Connection): List<Event> =
        selectAll.run(conn)

    fun findById(id: EventId, conn: Connection): Event? =
        findByIdOp(id).run(conn)

    fun findByStatus(status: EventStatus, conn: Connection): List<Event> =
        selectByStatusTemplate.on(status).run(conn)

    fun findByVenue(venueId: VenueId, conn: Connection): List<Event> =
        selectByVenueTemplate.on(venueId).run(conn)

    fun create(event: Event, conn: Connection): Event =
        insertTemplate.on(event).run(conn)

    fun updateStatus(id: EventId, status: EventStatus, conn: Connection): Int =
        updateStatusTemplate.on(status, id).run(conn)

    fun addRatingOp(id: EventId, rating: Double): Operation.Update =
        addRatingTemplate.on(rating, id)

    fun addRating(id: EventId, rating: Double, conn: Connection): Int =
        addRatingOp(id, rating).run(conn)

    fun analyzeQueries(conn: Connection): List<QueryAnalysis> = listOf(
        QueryAnalyzer.analyze("EventRepo.selectAll", selectAll, conn),
        QueryAnalyzer.analyze("EventRepo.selectById", selectByIdTemplate, conn),
        QueryAnalyzer.analyze("EventRepo.selectByStatus", selectByStatusTemplate, conn),
        QueryAnalyzer.analyze("EventRepo.selectByVenue", selectByVenueTemplate, conn),
        QueryAnalyzer.analyze("EventRepo.insertReturning", insertTemplate, conn),
        QueryAnalyzer.analyze("EventRepo.updateStatus", updateStatusTemplate, conn),
        QueryAnalyzer.analyze("EventRepo.addRating", addRatingTemplate, conn),
    ).flatten()
}
