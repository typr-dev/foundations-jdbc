package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import dev.typr.foundationskt.docs.core.PersistedTypes.*

@Suppress("unused")
class PersistedCodecs {
    val venueIdType: DuckDbType<VenueId> = DuckDbTypes.bigint.transform(::VenueId, VenueId::value)

    //start
    val venueCodec: RowCodecNamed<Venue> =
        RowCodec.namedBuilder<Venue>()
            .field("name", DuckDbTypes.varchar, Venue::name)
            .field("capacity", DuckDbTypes.integer, Venue::capacity)
            .build(::Venue)

    val persistedVenueCodec: RowCodecNamed<PersistedVenue> =
        RowCodec.ofNamed("id", venueIdType)
            .join(venueCodec)
            .to({ (id, venue) -> PersistedVenue(id, venue) }, { Pair(it.id, it.venue) })
    //stop
}
