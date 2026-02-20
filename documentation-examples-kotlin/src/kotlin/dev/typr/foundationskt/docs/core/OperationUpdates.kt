package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*

@Suppress("unused")
class OperationUpdates {
    val fragment: Fragment = sql {
        "DELETE FROM users WHERE last_login < now() - interval '1 year'" }

    //start
    // Get the affected row count
    val rowCount: Operation<Int> = fragment.update()

    // Ignore the row count (DDL, fire-and-forget DML)
    val ignored: Operation<Unit> = fragment.execute()
    //stop
}
