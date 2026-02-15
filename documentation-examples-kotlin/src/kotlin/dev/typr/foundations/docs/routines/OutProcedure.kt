package dev.typr.foundations.docs.routines

import dev.typr.foundationskt.*

@Suppress("unused")
class OutProcedure {
    private lateinit var tx: Transactor

    //start
    companion object {
        // OUT parameters — the builder tracks output types statically
        val getUser: DbProcedure.Def1_2<Int, String, String> =
            DbProcedure.define("get_user_by_id")
                .`in`(PgTypes.int4)       // user_id IN
                .out(PgTypes.text)        // name OUT
                .out(PgTypes.text)        // email OUT
                .build()
    }

    // call() is fully typed — wrong argument types won't compile
    fun findUser(userId: Int) =
        getUser.call(userId).transact(tx)
    //stop
}
