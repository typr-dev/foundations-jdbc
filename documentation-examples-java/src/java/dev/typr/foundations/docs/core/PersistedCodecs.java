package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import dev.typr.foundations.docs.core.PersistedTypes.*;

@SuppressWarnings("unused")
public class PersistedCodecs {
    static DuckDbType<VenueId> venueIdType = DuckDbTypes.bigint.transform(VenueId::new, VenueId::value);

    //start
    static RowCodecNamed<Venue> venueCodec =
        RowCodec.<Venue>namedBuilder()
            .field("name", DuckDbTypes.varchar, Venue::name)
            .field("capacity", DuckDbTypes.integer, Venue::capacity)
            .build(Venue::new);

    static RowCodecNamed<PersistedVenue> persistedVenueCodec =
        RowCodec.<VenueId>ofNamed("id", venueIdType)
            .join(venueCodec)
            .to(Bijection.of(
                t -> new PersistedVenue(t._1(), t._2()),
                pv -> Tuple.of(pv.id(), pv.venue())));
    //stop
}
