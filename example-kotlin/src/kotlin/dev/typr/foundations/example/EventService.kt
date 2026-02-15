package dev.typr.foundations.example

import dev.typr.foundationskt.Transactor
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

class EventService(private val tx: Transactor) {

    fun createVenueWithEvent(
        venue: Venue,
        eventTitle: String, eventDescription: String?, eventDate: OffsetDateTime, doorOpen: LocalDate,
        basePrice: Money, eventTags: List<String>
    ): Pair<Venue, Event> = tx.transact { conn ->
        val created = VenueRepo.create(venue, conn)
        val event = EventRepo.create(
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
            ), conn
        )
        Pair(created, event)
    }

    fun publishEvent(eventId: EventId): Event = tx.transact { conn ->
        val event = EventRepo.findById(eventId, conn)
            ?: throw IllegalArgumentException("Event $eventId not found")
        require(event.status == EventStatus.DRAFT) { "Can only publish DRAFT events, got ${event.status}" }
        EventRepo.updateStatus(eventId, EventStatus.PUBLISHED, conn)
        EventRepo.findById(eventId, conn)!!
    }

    fun purchaseTickets(
        eventId: EventId,
        purchases: List<TicketPurchaseRequest>
    ): List<Ticket> = tx.transact { conn ->
        val event = EventRepo.findById(eventId, conn)
            ?: throw IllegalArgumentException("Event $eventId not found")
        require(event.status == EventStatus.PUBLISHED) { "Can only buy tickets for PUBLISHED events" }

        val venue = VenueRepo.findById(event.venueId, conn)!!
        val currentCount = TicketRepo.countByEvent(eventId, conn)
        val totalAfter = currentCount + purchases.size
        require(totalAfter <= venue.capacity) {
            "Not enough capacity: ${venue.capacity - currentCount} seats left, ${purchases.size} requested"
        }

        val tickets = purchases.map { req ->
            val price = calculatePrice(event.basePrice, req.tier)
            TicketRepo.purchase(eventId, req.tier, req.holderName, req.holderEmail, price, req.seatNumbers, conn)
        }

        if (totalAfter.toInt() == venue.capacity) {
            EventRepo.updateStatus(eventId, EventStatus.SOLD_OUT, conn)
        }

        tickets
    }

    fun rateEvent(eventId: EventId, rating: Double) {
        require(rating in 1.0..5.0) { "Rating must be between 1 and 5" }
        EventRepo.addRatingOp(eventId, rating).transact(tx)
    }

    fun getEventSummaries(): List<EventSummary> =
        TicketRepo.eventSummariesOp().transact(tx)

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
