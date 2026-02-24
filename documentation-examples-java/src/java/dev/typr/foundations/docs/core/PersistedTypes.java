package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;

@SuppressWarnings("unused")
public class PersistedTypes {
    //start
    public record VenueId(Long value) {}
    static DuckDbType<VenueId> venueIdType = DuckDbTypes.bigint.transform(VenueId::new, VenueId::value);

    public record Venue(String name, int capacity) {}
    public record PersistedVenue(VenueId id, Venue venue) {}
    //stop
}
