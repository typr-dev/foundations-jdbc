package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleObject;
import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class ObjectTypes {
    //start
    // CREATE TYPE coordinates_t AS OBJECT (
    //     latitude  NUMBER(9,6),
    //     longitude NUMBER(9,6)
    // );
    record Coordinates(BigDecimal latitude, BigDecimal longitude) {}

    static final OracleType<Coordinates> coordinatesType =
        OracleObject.<Coordinates>builder("COORDINATES_T")
            .field("LATITUDE", OracleTypes.number(9, 6), Coordinates::latitude)
            .field("LONGITUDE", OracleTypes.number(9, 6), Coordinates::longitude)
            .build(Coordinates::new)
            .asType();

    // CREATE TYPE address_t AS OBJECT (
    //     street   VARCHAR2(100),
    //     city     VARCHAR2(50),
    //     location coordinates_t   -- nested OBJECT
    // );
    record Address(String street, String city, Coordinates location) {}

    static final OracleType<Address> addressType =
        OracleObject.<Address>builder("ADDRESS_T")
            .field("STREET", OracleTypes.varchar2(100), Address::street)
            .field("CITY", OracleTypes.varchar2(50), Address::city)
            .field("LOCATION", coordinatesType, Address::location)
            .build(Address::new)
            .asType();
    //stop
}
