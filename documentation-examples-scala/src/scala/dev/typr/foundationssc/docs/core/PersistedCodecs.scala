package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*
import dev.typr.foundationssc.docs.core.PersistedTypes.*

@SuppressWarnings(Array("unused"))
object PersistedCodecs:
  val venueIdType: DuckDbType[VenueId] = DuckDbTypes.bigint.transform(VenueId.apply, _.value)

  // start
  val venueCodec: RowCodecNamed[Venue] = RowCodec
    .namedBuilder[Venue]()
    .field("name", DuckDbTypes.varchar)(_.name)
    .field("capacity", DuckDbTypes.integer)(_.capacity)
    .build(Venue.apply)

  val persistedVenueCodec: RowCodecNamed[PersistedVenue] =
    RowCodec
      .ofNamed("id", venueIdType)
      .join(venueCodec)
      .to(PersistedVenue.apply, pv => (pv.id, pv.venue))
  // stop
