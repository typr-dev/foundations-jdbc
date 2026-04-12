package dev.typr.foundations.example

import dev.typr.foundationskt.*

// ─── Domain type mappings ───────────────────────────────────────────

val eventIdType: DuckDbType<EventId> = DuckDbTypes.bigint.transform(::EventId, EventId::value)
val venueIdType: DuckDbType<VenueId> = DuckDbTypes.bigint.transform(::VenueId, VenueId::value)
val ticketIdType: DuckDbType<TicketId> = DuckDbTypes.uuid.transform(::TicketId, TicketId::value)
val moneyType: DuckDbType<Money> = DuckDbTypes.decimal(12, 2).transform(::Money, Money::amount)

// ─── Enum types ─────────────────────────────────────────────────────

val eventStatusType: DuckDbType<EventStatus> = DuckDbTypes.ofEnum("event_status", EventStatus::valueOf)
val ticketTierType: DuckDbType<TicketTier> = DuckDbTypes.ofEnum("ticket_tier", TicketTier::valueOf)

// ─── Struct type ────────────────────────────────────────────────────

val addressType: DuckDbType<Address> = DuckDbTypes.compositeOf("address",
    RowCodec.namedBuilder<Address>()
        .field("street", DuckDbTypes.varchar, Address::street)
        .field("city", DuckDbTypes.varchar, Address::city)
        .field("state", DuckDbTypes.varchar, Address::state)
        .field("zip", DuckDbTypes.varchar, Address::zip)
        .field("country", DuckDbTypes.varchar, Address::country)
        .build(::Address))

// ─── Row codecs ─────────────────────────────────────────────────────

val venueCodec: RowCodecNamed<Venue> = RowCodec.namedBuilder<Venue>()
    .field("id", venueIdType, Venue::id)
    .field("name", DuckDbTypes.varchar, Venue::name)
    .field("address", addressType, Venue::address)
    .field("capacity", DuckDbTypes.integer, Venue::capacity)
    .field("tags", DuckDbTypes.listVarchar, Venue::tags)
    .field("metadata", DuckDbTypes.varchar.mapTo(DuckDbTypes.varchar), Venue::metadata)
    .build(::Venue)

val eventCodec: RowCodecNamed<Event> = RowCodec.namedBuilder<Event>()
    .field("id", eventIdType, Event::id)
    .field("venue_id", venueIdType, Event::venueId)
    .field("title", DuckDbTypes.varchar, Event::title)
    .field("description", DuckDbTypes.varchar.opt(), Event::description)
    .field("status", eventStatusType, Event::status)
    .field("date", DuckDbTypes.timestamptz, Event::date)
    .field("door_open", DuckDbTypes.date, Event::doorOpen)
    .field("base_price", moneyType, Event::basePrice)
    .field("tags", DuckDbTypes.listVarchar, Event::tags)
    .field("ratings", DuckDbTypes.listDouble, Event::ratings)
    .build(::Event)

val ticketCodec: RowCodecNamed<Ticket> = RowCodec.namedBuilder<Ticket>()
    .field("id", ticketIdType, Ticket::id)
    .field("event_id", eventIdType, Ticket::eventId)
    .field("tier", ticketTierType, Ticket::tier)
    .field("holder_name", DuckDbTypes.varchar, Ticket::holderName)
    .field("holder_email", DuckDbTypes.varchar.opt(), Ticket::holderEmail)
    .field("price", moneyType, Ticket::price)
    .field("purchased", DuckDbTypes.timestamptz, Ticket::purchased)
    .field("seat_numbers", DuckDbTypes.listInteger, Ticket::seatNumbers)
    .build(::Ticket)

val eventSummaryCodec: RowCodecNamed<EventSummary> = RowCodec.namedBuilder<EventSummary>()
    .field("event_id", eventIdType, EventSummary::eventId)
    .field("title", DuckDbTypes.varchar, EventSummary::title)
    .field("venue_name", DuckDbTypes.varchar, EventSummary::venueName)
    .field("tickets_sold", DuckDbTypes.bigint, EventSummary::ticketsSold)
    .field("total_revenue", moneyType, EventSummary::totalRevenue)
    .build(::EventSummary)
