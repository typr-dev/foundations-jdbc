package dev.typr.foundations.docs.landing
import dev.typr.foundationssc.*

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object StoredProcedure:
  var tx: Transactor = null // placeholder

  //start
  // Define once, call many times — input and output types are baked in
  val getUser: DbProcedure.Def1_2[Int, String, String] =
    DbProcedure.define("get_user_by_id")
      .in(PgTypes.int4)
      .out(PgTypes.text)
      .out(PgTypes.text)
      .build()

  // call() returns a ProcedureOp — use it like any other operation
  @throws[SQLException]
  def findUser(userId: Int): Tuple.Tuple2[String, String] =
    getUser.call(userId).transact(tx)
  //stop
