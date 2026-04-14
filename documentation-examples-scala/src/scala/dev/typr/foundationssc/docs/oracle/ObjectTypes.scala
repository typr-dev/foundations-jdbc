package dev.typr.foundationssc.docs.oracle
import dev.typr.foundations.{OracleType, OracleTypes, RowCodec}

@SuppressWarnings(Array("unused"))
object ObjectTypes:
  // start
  // CREATE TYPE coordinates_t AS OBJECT (
  //     latitude  NUMBER(9,6),
  //     longitude NUMBER(9,6)
  // );
  case class Coordinates(latitude: java.math.BigDecimal, longitude: java.math.BigDecimal)

  val coordinatesType: OracleType[Coordinates] =
    OracleTypes.compositeOf(
      "COORDINATES_T",
      RowCodec
        .namedBuilder[Coordinates]()
        .field("LATITUDE", OracleTypes.number(9, 6), (c: Coordinates) => c.latitude)
        .field("LONGITUDE", OracleTypes.number(9, 6), (c: Coordinates) => c.longitude)
        .build((lat: java.math.BigDecimal, lon: java.math.BigDecimal) => Coordinates(lat, lon))
    )

  // CREATE TYPE address_t AS OBJECT (
  //     street   VARCHAR2(100),
  //     city     VARCHAR2(50),
  //     location coordinates_t   -- nested OBJECT
  // );
  case class Address(street: String, city: String, location: Coordinates)

  val addressType: OracleType[Address] =
    OracleTypes.compositeOf(
      "ADDRESS_T",
      RowCodec
        .namedBuilder[Address]()
        .field("STREET", OracleTypes.varchar2(100), (a: Address) => a.street)
        .field("CITY", OracleTypes.varchar2(50), (a: Address) => a.city)
        .field("LOCATION", coordinatesType, (a: Address) => a.location)
        .build((street: String, city: String, loc: Coordinates) => Address(street, city, loc))
    )
  // stop
