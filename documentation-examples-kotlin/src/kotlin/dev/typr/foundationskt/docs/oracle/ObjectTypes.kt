package dev.typr.foundationskt.docs.oracle

import dev.typr.foundations.OracleObject
import dev.typr.foundations.OracleType
import dev.typr.foundations.OracleTypes
import java.math.BigDecimal

@Suppress("unused")
class ObjectTypes {
    //start
    // CREATE TYPE coordinates_t AS OBJECT (
    //     latitude  NUMBER(9,6),
    //     longitude NUMBER(9,6)
    // );
    data class Coordinates(val latitude: BigDecimal, val longitude: BigDecimal)

    val coordinatesType: OracleType<Coordinates> =
        OracleObject.builder<Coordinates>("COORDINATES_T")
            .field("LATITUDE", OracleTypes.number(9, 6), Coordinates::latitude)
            .field("LONGITUDE", OracleTypes.number(9, 6), Coordinates::longitude)
            .build(::Coordinates)
            .asType()

    // CREATE TYPE address_t AS OBJECT (
    //     street   VARCHAR2(100),
    //     city     VARCHAR2(50),
    //     location coordinates_t   -- nested OBJECT
    // );
    data class Address(val street: String, val city: String, val location: Coordinates)

    val addressType: OracleType<Address> =
        OracleObject.builder<Address>("ADDRESS_T")
            .field("STREET", OracleTypes.varchar2(100), Address::street)
            .field("CITY", OracleTypes.varchar2(50), Address::city)
            .field("LOCATION", coordinatesType, Address::location)
            .build(::Address)
            .asType()
    //stop
}
