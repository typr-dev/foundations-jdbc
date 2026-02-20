package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import java.sql.Connection

@Suppress("unused")
class FragmentCombinators {
    lateinit var conn: Connection

    //start
    // Dynamic UPDATE — only set the fields that changed
    fun updateUser(userId: Int, newName: String?, newEmail: String?) {
        val sets = listOfNotNull(
            newName?.let { Sql { "name = ${PgTypes.text(it)}" } },
            newEmail?.let { Sql { "email = ${PgTypes.text(it)}" } }
        )

        if (sets.isNotEmpty()) {
            Sql { "UPDATE users ${Fragment.set(sets)} WHERE id = ${PgTypes.int4(userId)}" }
                .update()
                .run(conn)
        }
    }
    //stop
}
