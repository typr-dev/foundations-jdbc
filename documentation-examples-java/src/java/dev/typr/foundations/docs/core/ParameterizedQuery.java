package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import java.util.Optional;

@SuppressWarnings("unused")
public class ParameterizedQuery {
  record City(String name, int population) {}

  static final RowCodecNamed<City> cityCodec =
      RowCodec.<City>namedBuilder()
          .field("name", DuckDbTypes.varchar, City::name)
          .field("population", DuckDbTypes.integer, City::population)
          .build(City::new);

  // start
  static OperationRead<Optional<City>> findCityByName(String name) {
    return Fragment.of("SELECT ")
        .append(cityCodec.columnList())
        .append(" FROM city WHERE name = ")
        .value(DuckDbTypes.varchar, name)
        .query(cityCodec.maxOne());
  }
  // stop
}
