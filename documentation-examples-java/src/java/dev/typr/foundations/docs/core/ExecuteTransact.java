package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;
import java.util.List;

@SuppressWarnings("unused")
public class ExecuteTransact {
  record City(String name, int population) {}

  static RowCodec<City> cityCodec =
      RowCodec.<City>builder()
          .field(PgTypes.text, City::name)
          .field(PgTypes.int4, City::population)
          .build(City::new);

  Transactor tx = null; // placeholder

  Operation<List<City>> findCities =
      Fragment.of(
              """
              SELECT name, population FROM city
              ORDER BY population DESC\
              """)
          .query(cityCodec.all());

  // start
  // Single-operation form: .transact(tx) handles commit/rollback/close.
  List<City> cities() {
    return findCities.transact(tx);
  }

  // Multiple operations in one transaction: pass a block of Connection-using
  // code to tx.execute(…). Each .run(conn) inside the block shares the same
  // Connection and therefore the same transaction.
  List<City> citiesWithCount() {
    return tx.execute(conn -> {
      var list = findCities.run(conn);
      long count = countCities.run(conn);
      System.out.println("rows: " + count);
      return list;
    });
  }
  // stop

  Operation<Long> countCities =
      Fragment.of("SELECT count(*) FROM city").queryExactlyOne(PgTypes.int8);
}
