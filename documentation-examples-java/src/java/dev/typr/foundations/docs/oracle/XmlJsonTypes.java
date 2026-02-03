package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.data.Json;

@SuppressWarnings("unused")
public class XmlJsonTypes {
    //start
    OracleType<String> xmlType = OracleTypes.xmlType;
    OracleType<Json> jsonType = OracleTypes.json;

    Json data = new Json("{\"name\": \"Oracle\"}");
    //stop
}
