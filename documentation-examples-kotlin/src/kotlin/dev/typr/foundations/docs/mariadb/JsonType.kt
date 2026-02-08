package dev.typr.foundations.docs.mariadb

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class JsonType {
    //start
    val jsonType: MariaType<Json> = MariaTypes.json

    val data: Json = Json("{\"name\": \"John\", \"age\": 30}")
    //stop
}
