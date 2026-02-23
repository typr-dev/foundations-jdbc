package dev.typr.foundations.example

import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.*

// ─── Domain wrapper types ───────────────────────────────────────────

data class EventId(val value: Long)
data class VenueId(val value: Long)
data class TicketId(val value: UUID)
data class Money(val amount: BigDecimal)

// ─── Enums ──────────────────────────────────────────────────────────

enum class EventStatus { DRAFT, PUBLISHED, CANCELLED, SOLD_OUT, COMPLETED }
enum class TicketTier { GENERAL, VIP, BACKSTAGE }

// ─── Struct: Venue address ──────────────────────────────────────────

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val zip: String,
    val country: String
)

// ─── Row types ──────────────────────────────────────────────────────

data class Venue(
    val id: VenueId,
    val name: String,
    val address: Address,
    val capacity: Int,
    val tags: List<String>,
    val metadata: Map<String, String>
)

data class Event(
    val id: EventId,
    val venueId: VenueId,
    val title: String,
    val description: String?,
    val status: EventStatus,
    val date: OffsetDateTime,
    val doorOpen: LocalDate,
    val basePrice: Money,
    val tags: List<String>,
    val ratings: List<Double>
)

data class Ticket(
    val id: TicketId,
    val eventId: EventId,
    val tier: TicketTier,
    val holderName: String,
    val holderEmail: String?,
    val price: Money,
    val purchased: OffsetDateTime,
    val seatNumbers: List<Int>
)

data class EventSummary(
    val eventId: EventId,
    val title: String,
    val venueName: String,
    val ticketsSold: Long,
    val totalRevenue: Money
)
