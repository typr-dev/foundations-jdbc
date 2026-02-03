package dev.typr.foundations.docs.duckdb;

import dev.typr.foundations.DuckDbType;
import dev.typr.foundations.DuckDbTypes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
public class MapTypes {
    //start
    // Create map types using the mapTo() combinator
    DuckDbType<Map<String, Integer>> mapStrInt = DuckDbTypes.varchar.mapTo(DuckDbTypes.integer);
    DuckDbType<Map<String, String>> mapStrStr = DuckDbTypes.varchar.mapTo(DuckDbTypes.varchar);
    DuckDbType<Map<UUID, LocalTime>> mapUuidTime = DuckDbTypes.uuid.mapTo(DuckDbTypes.time);

    // Works with any combination of types
    DuckDbType<Map<String, LocalDate>> mapStrDate = DuckDbTypes.varchar.mapTo(DuckDbTypes.date);
    //stop
}
