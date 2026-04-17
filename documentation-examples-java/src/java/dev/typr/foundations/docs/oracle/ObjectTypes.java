package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.RowCodec;
import java.math.BigDecimal;

@SuppressWarnings("unused")
public class ObjectTypes {
  // start
  // CREATE TYPE coordinates_t AS OBJECT (
  //     latitude  NUMBER(9,6),
  //     longitude NUMBER(9,6)
  // );
  record Coordinates(BigDecimal latitude, BigDecimal longitude) {}

  static final OracleType<Coordinates> coordinatesType =
      OracleTypes.compositeOf(
          "COORDINATES_T",
          RowCodec.<Coordinates>namedBuilder()
              .field("LATITUDE", OracleTypes.numberOf(9, 6), Coordinates::latitude)
              .field("LONGITUDE", OracleTypes.numberOf(9, 6), Coordinates::longitude)
              .build(Coordinates::new));

  // CREATE TYPE address_t AS OBJECT (
  //     street   VARCHAR2(100),
  //     city     VARCHAR2(50),
  //     location coordinates_t   -- nested OBJECT
  // );
  record Address(String street, String city, Coordinates location) {}

  static final OracleType<Address> addressType =
      OracleTypes.compositeOf(
          "ADDRESS_T",
          RowCodec.<Address>namedBuilder()
              .field("STREET", OracleTypes.varchar2Of(100), Address::street)
              .field("CITY", OracleTypes.varchar2Of(50), Address::city)
              .field("LOCATION", coordinatesType, Address::location)
              .build(Address::new));
  // stop
}
