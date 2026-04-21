package dev.typr.foundations.example

import dev.typr.foundationskt.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Instant

class EventService(private val tx: Transactor) {

    fun findAllEvents(): List<Event> = EventRepo.allEvents.transact(tx)

    fun findEvent(id: EventId): Event? = EventRepo.eventById.on(id).transact(tx)

    fun createVenueWithEvent(
        venue: Venue,
        eventTitle: String, eventDescription: String?, eventDate: Instant, doorOpen: LocalDate,
        basePrice: Money, eventTags: List<String>
    ): Pair<Venue, Event> = tx.transact { mc ->
        val created = VenueRepo.createVenue.on(venue).run(mc)
        val event = EventRepo.createEvent.on(
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
        ).run(mc)
        Pair(created, event)
    }

    fun publishEvent(eventId: EventId): Event = tx.transact { mc ->
        val event = EventRepo.eventById.on(eventId).run(mc)
            ?: throw IllegalArgumentException("Event $eventId not found")
        require(event.status == EventStatus.DRAFT) { "Can only publish DRAFT events, got ${event.status}" }
        EventRepo.updateEventStatus.on(EventStatus.PUBLISHED, eventId).run(mc)
        EventRepo.eventById.on(eventId).run(mc)!!
    }

    fun purchaseTickets(
        eventId: EventId,
        purchases: List<TicketPurchaseRequest>
    ): List<Ticket> = tx.transact { mc ->
        val event = EventRepo.eventById.on(eventId).run(mc)
            ?: throw IllegalArgumentException("Event $eventId not found")
        require(event.status == EventStatus.PUBLISHED) { "Can only buy tickets for PUBLISHED events" }

        val venue = VenueRepo.venueById.on(event.venueId).run(mc)!!
        val currentCount = TicketRepo.countTicketsByEvent.on(eventId).run(mc)
        val totalAfter = currentCount + purchases.size
        require(totalAfter <= venue.capacity) {
            "Not enough capacity: ${venue.capacity - currentCount} seats left, ${purchases.size} requested"
        }

        val tickets = purchases.map { req ->
            val price = calculatePrice(event.basePrice, req.tier)
            TicketRepo.purchaseTicket(eventId, req.tier, req.holderName, req.holderEmail, price, req.seatNumbers).run(mc)
        }

        if (totalAfter.toInt() == venue.capacity) {
            EventRepo.updateEventStatus.on(EventStatus.SOLD_OUT, eventId).run(mc)
        }

        tickets
    }

    fun rateEvent(eventId: EventId, rating: Double) {
        require(rating in 1.0..5.0) { "Rating must be between 1 and 5" }
        EventRepo.addEventRating.on(rating, eventId).transact(tx)
    }

    fun getEventSummaries(): List<EventSummary> = TicketRepo.eventSummaries.transact(tx)

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
