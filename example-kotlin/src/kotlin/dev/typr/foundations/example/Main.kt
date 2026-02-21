package dev.typr.foundations.example

import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

fun main() {
    val ds = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build())
    val tx = ds.transactor(Transactor.autoCommitStrategy())

    // ── Apply schema ────────────────────────────────────────────────
    println("=== Applying schema ===")
    tx.transact { conn -> Schema.apply(conn) }
    println("Schema applied.\n")

    // ── Query analysis ──────────────────────────────────────────────
    println("=== Running query analysis ===")
    val analyzables = AnalyzableScanner.scan("dev.typr.foundations.example")
    val checker = QueryChecker.create(tx)
    checker.checkAll(analyzables)
    println("All ${analyzables.size} queries passed analysis.\n")

    val service = EventService(tx)

    // ── Business operations ─────────────────────────────────────────
    println("=== Creating venue + event ===")
    val (venue, event) = service.createVenueWithEvent(
        venue = Venue(
            id = VenueId(0),
            name = "The Grand Hall",
            address = Address("123 Main St", "Springfield", "IL", "62701", "US"),
            capacity = 5,
            tags = listOf("indoor", "historic", "downtown"),
            metadata = mapOf("parking" to "garage", "wifi" to "free", "accessibility" to "wheelchair")
        ),
        eventTitle = "Kotlin Night Live",
        eventDescription = "An evening of live coding and music",
        eventDate = OffsetDateTime.parse("2026-03-15T19:00:00Z"),
        doorOpen = LocalDate.of(2026, 3, 15),
        basePrice = Money(BigDecimal("49.99")),
        eventTags = listOf("kotlin", "live-coding", "music")
    )
    println("Created venue: ${venue.name} (id=${venue.id.value})")
    println("  Address: ${venue.address}")
    println("  Tags: ${venue.tags}")
    println("  Metadata: ${venue.metadata}")
    println("Created event: ${event.title} (id=${event.id.value})")
    println("  Status: ${event.status}, Date: ${event.date}")
    println("  Base price: ${event.basePrice.amount}\n")

    // ── Publish event ───────────────────────────────────────────────
    println("=== Publishing event ===")
    val published = service.publishEvent(event.id)
    println("Event status: ${published.status}\n")

    // ── Purchase tickets ────────────────────────────────────────────
    println("=== Purchasing tickets ===")
    val tickets = service.purchaseTickets(event.id, listOf(
        TicketPurchaseRequest(TicketTier.GENERAL, "Alice Johnson", "alice@example.com", listOf(1, 2)),
        TicketPurchaseRequest(TicketTier.VIP, "Bob Smith", null, listOf(3)),
        TicketPurchaseRequest(TicketTier.BACKSTAGE, "Charlie Brown", "charlie@example.com", listOf(4)),
    ))
    for (t in tickets) {
        println("  Ticket ${t.id.value}: ${t.holderName} [${t.tier}] \$${t.price.amount} seats=${t.seatNumbers}")
    }
    println()

    // ── Rate the event ──────────────────────────────────────────────
    println("=== Rating event ===")
    service.rateEvent(event.id, 4.5)
    service.rateEvent(event.id, 5.0)
    service.rateEvent(event.id, 3.5)
    val rated = EventRepo.eventById.on(event.id).transact(tx)!!
    println("Ratings: ${rated.ratings}\n")

    // ── Purchase remaining to trigger SOLD_OUT ──────────────────────
    println("=== Filling remaining seats ===")
    val remaining = service.purchaseTickets(event.id, listOf(
        TicketPurchaseRequest(TicketTier.GENERAL, "Diana Prince", "diana@example.com", listOf(5)),
        TicketPurchaseRequest(TicketTier.GENERAL, "Eve Adams", "eve@example.com", emptyList()),
    ))
    println("  ${remaining.size} more tickets purchased")
    val soldOut = EventRepo.eventById.on(event.id).transact(tx)!!
    println("  Event status now: ${soldOut.status}\n")

    // ── Try to buy when sold out ────────────────────────────────────
    println("=== Attempting purchase on sold-out event ===")
    try {
        service.purchaseTickets(event.id, listOf(
            TicketPurchaseRequest(TicketTier.GENERAL, "Frank Zappa", null, emptyList())
        ))
        println("  ERROR: Should have thrown!")
    } catch (e: Exception) {
        println("  Correctly rejected: ${e.message}\n")
    }

    // ── Event summaries (JOIN query) ────────────────────────────────
    println("=== Event summaries ===")
    val summaries = service.getEventSummaries()
    for (s in summaries) {
        println("  ${s.title} @ ${s.venueName}: ${s.ticketsSold} tickets, \$${s.totalRevenue.amount} revenue")
    }
    println()

    // ── Read back all data ──────────────────────────────────────────
    println("=== All venues ===")
    VenueRepo.allVenues.transact(tx).forEach { v ->
        println("  ${v.name}: capacity=${v.capacity}, tags=${v.tags}")
    }

    println("\n=== All tickets for event ===")
    TicketRepo.ticketsByEvent.on(event.id).transact(tx).forEach { t ->
        println("  ${t.holderName} [${t.tier}] \$${t.price.amount}")
    }

    println("\nDone!")
}
