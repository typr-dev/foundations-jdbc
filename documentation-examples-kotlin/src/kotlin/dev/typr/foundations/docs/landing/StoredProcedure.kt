package dev.typr.foundations.docs.landing

import dev.typr.kotlinfoundations.*

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
    fun findUser(userId: Int) =
        getUser.call(userId).transact(tx)
    //stop
}
