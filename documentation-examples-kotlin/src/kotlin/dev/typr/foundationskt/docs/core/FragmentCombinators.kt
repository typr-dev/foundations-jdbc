package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import java.sql.Connection

@Suppress("unused")
class FragmentCombinators {
    lateinit var conn: Connection

    //start
    // Dynamic UPDATE — only set the fields that changed
    fun updateUser(userId: Int, newName: String?, newEmail: String?) {
        val sets = listOfNotNull(
            newName?.let { sql { "name = ${PgTypes.text(it)}" } },
            newEmail?.let { sql { "email = ${PgTypes.text(it)}" } }
        )

        if (sets.isNotEmpty()) {
            sql { "UPDATE users ${Fragment.set(sets)} WHERE id = ${PgTypes.int4(userId)}" }
                .update()
                .run(conn)
        }
    }
    //stop
}
