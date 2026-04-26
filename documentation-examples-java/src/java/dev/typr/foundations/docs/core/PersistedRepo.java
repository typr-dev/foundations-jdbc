package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import dev.typr.foundations.docs.core.PersistedTypes.*;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
// start
class VenueRepo {
  private static final DuckDbType<VenueId> venueIdType =
      DuckDbTypes.bigint.transform(VenueId::new, VenueId::value);

  private static final RowCodecNamed<Venue> venueCodec =
      RowCodec.<Venue>namedBuilder()
          .field("name", DuckDbTypes.varchar, Venue::name)
          .field("capacity", DuckDbTypes.integer, Venue::capacity)
          .build(Venue::new);

  private static final RowCodecNamed<PersistedVenue> persistedVenueCodec =
      RowCodec.<VenueId>ofNamed("id", venueIdType)
          .join(venueCodec)
          .to(
              Bijection.of(
                  t -> new PersistedVenue(t._1(), t._2()), pv -> Tuple.of(pv.id(), pv.venue())));

  static final RowParamBuilder<Venue> insert =
      Fragment.insertIntoReturning("venue", venueCodec, persistedVenueCodec);

  static OperationRead.Query<PersistedVenue> insert(Venue venue) {
    return insert.updateReturning(venue, persistedVenueCodec.exactlyOne());
  }

  static final OperationRead<List<PersistedVenue>> selectAll =
      Fragment.of("SELECT ")
          .append(persistedVenueCodec.columnList())
          .append(" FROM venue")
          .query(persistedVenueCodec.all());

  static OperationRead.Query<Optional<PersistedVenue>> selectById(VenueId id) {
    return Fragment.of("SELECT ")
        .append(persistedVenueCodec.columnList())
        .append(" FROM venue WHERE id = ")
        .value(venueIdType, id)
        .query(persistedVenueCodec.maxOne());
  }
}
// stop
