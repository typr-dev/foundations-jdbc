package dev.typr.foundationssc.docs.routines
import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object OutProcedure:
  private val tx: Transactor = null // placeholder

  // start
  // OUT parameters — the builder tracks output types statically
  val getUser =
    DbProcedure
      .define("get_user_by_id")
      .input(PgTypes.int4) // user_id IN
      .out(PgTypes.text) // name OUT
      .out(PgTypes.text) // email OUT
      .build()

  // call() is fully typed — wrong argument types won't compile
  def findUser(userId: Int): (String, String) =
    getUser.call(userId).transact(tx)
  // stop
