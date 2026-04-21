package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import java.time.Duration

@Suppress("unused")
class OperationNamed {
    //start
    val users: OperationRead<List<String>> =
        sql { "SELECT name FROM users" }
            .query(RowCodec.of(PgTypes.text).all())
            .named("load-users")
            .timeout(Duration.ofSeconds(5))
    //stop
}
