package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.data.Json

@Suppress("unused")
class JsonType {
    //start
    val jsonType: MariaType<Json> = MariaTypes.json

    val data: Json = Json("{\"name\": \"John\", \"age\": 30}")
    //stop
}
