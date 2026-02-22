package dev.typr.foundationskt.docs.mariadb

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class JsonType {
    //start
    val jsonType: MariaType<Json> = MariaTypes.json

    val data: Json = Json("{\"name\": \"John\", \"age\": 30}")
    //stop
}
