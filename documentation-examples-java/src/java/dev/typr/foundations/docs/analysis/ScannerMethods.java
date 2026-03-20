package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.*;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class ScannerMethods {
  record City(int id, String name) {}

  static RowCodec<City> cityCodec =
      RowCodec.<City>builder()
          .field(PgTypes.int4, City::id)
          .field(PgTypes.text, City::name)
          .build(City::new);

  // start
  // Fields — discovered automatically
  final Operation<List<City>> allCities =
      Fragment.of("SELECT id, name FROM cities").query(cityCodec.all());

  // No-arg methods — discovered automatically
  Operation<List<City>> activeCities() {
    return Fragment.of("SELECT id, name FROM cities WHERE active").query(cityCodec.all());
  }

  // Methods with parameters — dummy arguments constructed automatically
  Operation<Optional<City>> findByName(String name) {
    return Fragment.of("SELECT id, name FROM cities WHERE name = ")
        .value(PgTypes.text, name)
        .query(cityCodec.maxOne());
  }

  // Templates — also discovered
  Template<Integer, Optional<City>> findById =
      Fragment.of("SELECT id, name FROM cities WHERE id = ")
          .param(PgTypes.int4)
          .query(cityCodec.maxOne());
  // stop
}
