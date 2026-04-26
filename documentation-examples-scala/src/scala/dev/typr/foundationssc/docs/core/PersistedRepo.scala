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

  def insert(venue: Venue): OperationRead.Query[PersistedVenue] =
    Fragment.insertOneReturning("venue", venueCodec, venue, persistedVenueCodec)

  val selectAll: OperationRead[List[PersistedVenue]] =
    sql"SELECT ${persistedVenueCodec.columnList} FROM venue"
      .query(persistedVenueCodec.all())

  def selectById(id: VenueId): OperationRead.Query[Option[PersistedVenue]] =
    sql"SELECT ${persistedVenueCodec.columnList} FROM venue WHERE id = ${venueIdType(id)}"
      .query(persistedVenueCodec.maxOne())
//stop
