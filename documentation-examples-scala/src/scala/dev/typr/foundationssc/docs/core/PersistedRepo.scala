package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*
import dev.typr.foundationssc.docs.core.PersistedTypes.*

@SuppressWarnings(Array("unused"))
//start
object VenueRepo:
  private val venueIdType = DuckDbTypes.bigint.transform(VenueId.apply, _.value)

  private val venueCodec = RowCodec
    .namedBuilder[Venue]()
    .field("name", DuckDbTypes.varchar)(_.name)
    .field("capacity", DuckDbTypes.integer)(_.capacity)
    .build(Venue.apply)

  private val persistedVenueCodec =
    RowCodec
      .ofNamed("id", venueIdType)
      .join(venueCodec)
      .to(PersistedVenue.apply, pv => (pv.id, pv.venue))

  val insert: RowTemplate.Query[Venue, PersistedVenue] =
    Fragment.insertIntoReturning("venue", venueCodec, persistedVenueCodec)

  val selectAll: Operation[List[PersistedVenue]] =
    sql"SELECT ${persistedVenueCodec.columnList} FROM venue"
      .query(persistedVenueCodec.all())

  val selectById: Template[VenueId, Option[PersistedVenue]] =
    sql"SELECT ${persistedVenueCodec.columnList} FROM venue WHERE id = "
      .param(venueIdType)
      .query(persistedVenueCodec.maxOne())
//stop
