package dev.typr.foundations.docs.landing

import dev.typr.kotlinfoundations.*
import dev.typr.foundations.Tuple
import java.sql.SQLException

@Suppress("unused")
class StoredProcedure {
    lateinit var tx: Transactor // placeholder

    //start
    companion object {
        // Define once, call many times — input and output types are baked in
        val getUser: DbProcedure.Def1_2<Int, String, String> =
            DbProcedure.define("get_user_by_id")
                .`in`(PgTypes.int4)
                .out(PgTypes.text)
                .out(PgTypes.text)
                .build()
    }

    // call() returns a ProcedureOp — use it like any other operation
    @Throws(SQLException::class)
    fun findUser(userId: Int): Tuple.Tuple2<String, String> =
        getUser.call(userId).transact(tx)
    //stop
}
