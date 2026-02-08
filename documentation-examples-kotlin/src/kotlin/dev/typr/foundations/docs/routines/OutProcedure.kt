package dev.typr.foundations.docs.routines

import dev.typr.kotlinfoundations.*
import java.sql.SQLException

@Suppress("unused")
class OutProcedure {
    private val tx: Transactor? = null // placeholder

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
    @Throws(SQLException::class)
    fun findUser(userId: Int): Tuple2<String, String> =
        getUser.call(userId).transact(tx!!)
    //stop
}
