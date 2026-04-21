package dev.typr.foundations.docs.core;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.OperationRead;
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

  OperationRead<List<City>> findCities =
      Fragment.of(
              """
              SELECT name, population FROM city
              ORDER BY population DESC\
              """)
          .query(cityCodec.all());

  // start
  // Single-operation form: .transactRead(tx) handles commit/rollback/close.
  List<City> cities() {
    return findCities.transactRead(tx);
  }

  // Multiple operations in one transaction: pass a block to tx.transact(…).
  // Each mc.execute(…) inside the block shares the same transaction.
  List<City> citiesWithCount() {
    return tx.transact(
        mc -> {
          var list = mc.execute(findCities);
          long count = mc.execute(countCities);
          System.out.println("rows: " + count);
          return list;
        });
  }

  // stop

  OperationRead<Long> countCities =
      Fragment.of("SELECT count(*) FROM city").queryExactlyOne(PgTypes.int8);
}
