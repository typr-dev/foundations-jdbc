package dev.typr.foundations.example

import dev.typr.foundationskt.Transactor
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

class EventService(private val tx: Transactor) {

    fun findAllEvents(): List<Event> = allEvents.transact(tx)

    fun findEvent(id: EventId): Event? = eventById(id).transact(tx)

    fun createVenueWithEvent(
        venue: Venue,
        eventTitle: String, eventDescription: String?, eventDate: OffsetDateTime, doorOpen: LocalDate,
        basePrice: Money, eventTags: List<String>
    ): Pair<Venue, Event> = tx.transact { conn ->
        val created = createVenue(venue).run(conn)
        val event = createEvent(
            Event(
                id = EventId(0),
                venueId = created.id,
                title = eventTitle,
                description = eventDescription,
                status = EventStatus.DRAFT,
                date = eventDate,
                doorOpen = doorOpen,
                basePrice = basePrice,
                tags = eventTags,
                ratings = emptyList()
            )
        ).run(conn)
        Pair(created, event)
    }

    fun publishEvent(eventId: EventId): Event = tx.transact { conn ->
        val event = eventById(eventId).run(conn)
            ?: throw IllegalArgumentException("Event $eventId not found")
        require(event.status == EventStatus.DRAFT) { "Can only publish DRAFT events, got ${event.status}" }
        updateEventStatus(eventId, EventStatus.PUBLISHED).run(conn)
        eventById(eventId).run(conn)!!
    }

    fun purchaseTickets(
        eventId: EventId,
        purchases: List<TicketPurchaseRequest>
    ): List<Ticket> = tx.transact { conn ->
        val event = eventById(eventId).run(conn)
            ?: throw IllegalArgumentException("Event $eventId not found")
        require(event.status == EventStatus.PUBLISHED) { "Can only buy tickets for PUBLISHED events" }

        val venue = venueById(event.venueId).run(conn)!!
        val currentCount = countTicketsByEvent(eventId).run(conn)
        val totalAfter = currentCount + purchases.size
        require(totalAfter <= venue.capacity) {
            "Not enough capacity: ${venue.capacity - currentCount} seats left, ${purchases.size} requested"
        }

        val tickets = purchases.map { req ->
            val price = calculatePrice(event.basePrice, req.tier)
            purchaseTicket(eventId, req.tier, req.holderName, req.holderEmail, price, req.seatNumbers)
                .run(conn)
        }

        if (totalAfter.toInt() == venue.capacity) {
            updateEventStatus(eventId, EventStatus.SOLD_OUT).run(conn)
        }

        tickets
    }

    fun rateEvent(eventId: EventId, rating: Double) {
        require(rating in 1.0..5.0) { "Rating must be between 1 and 5" }
        addEventRating(eventId, rating).transact(tx)
    }

    fun getEventSummaries(): List<EventSummary> = eventSummaries.transact(tx)

    private fun calculatePrice(basePrice: Money, tier: TicketTier): Money = when (tier) {
        TicketTier.GENERAL -> basePrice
        TicketTier.VIP -> Money(basePrice.amount.multiply(BigDecimal("2.5")))
        TicketTier.BACKSTAGE -> Money(basePrice.amount.multiply(BigDecimal("5.0")))
    }
}

data class TicketPurchaseRequest(
    val tier: TicketTier,
    val holderName: String,
    val holderEmail: String?,
    val seatNumbers: List<Int>
)
