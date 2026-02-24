package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class PersistedTypes {
    //start
    data class VenueId(val value: Long)

    val venueIdType: DuckDbType<VenueId> = DuckDbTypes.bigint.transform(::VenueId, VenueId::value)

    data class Venue(val name: String, val capacity: Int)

    data class PersistedVenue(val id: VenueId, val venue: Venue)
    //stop
}
