package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.data.*

@SuppressWarnings(Array("unused"))
object PersistedTypes:
  //start
  case class VenueId(value: Long)
  val venueIdType: DuckDbType[VenueId] = DuckDbTypes.bigint.transform(VenueId.apply, _.value)

  case class Venue(name: String, capacity: Int)
  case class PersistedVenue(id: VenueId, venue: Venue)
  //stop
