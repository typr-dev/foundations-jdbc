package dev.typr.foundations.example

import dev.typr.kotlinfoundations.*
import java.sql.Connection

object VenueRepo {
    private val selectAll = Fragment.of("SELECT ")
        .append(venueParser.columnList).append(" FROM venue ORDER BY name")
        .query(venueParser.all())

    private val selectByIdTemplate = Fragment.of("SELECT ")
        .append(venueParser.columnList).append(" FROM venue WHERE id = ")
        .param(venueIdType)
        .query(venueParser.maxOne())

    private val insertTemplate = Fragment.of("INSERT INTO venue (")
        .append(venueParser.columnList).append(") VALUES (nextval('venue_id_seq'), ")
        .paramRow(venueParser, "id")
        .append(")")
        .append(" RETURNING ").append(venueParser.columnList)
        .query(venueParser.exactlyOne())

    fun findAllOp(): Operation.Query<List<Venue>> = selectAll

    fun findAll(conn: Connection): List<Venue> =
        selectAll.run(conn)

    fun findById(id: VenueId, conn: Connection): Venue? =
        selectByIdTemplate.on(id).run(conn)

    fun create(venue: Venue, conn: Connection): Venue =
        insertTemplate.on(venue).run(conn)

    fun analyzeQueries(conn: Connection): List<QueryAnalysis> = listOf(
        QueryAnalyzer.analyze("VenueRepo.selectAll", selectAll, conn),
        QueryAnalyzer.analyze("VenueRepo.selectById",
            selectByIdTemplate.fragment().query(venueParser.maxOne()), conn),
        QueryAnalyzer.analyze("VenueRepo.insertReturning",
            insertTemplate.fragment().query(venueParser.exactlyOne()), conn),
    ).flatten()
}
