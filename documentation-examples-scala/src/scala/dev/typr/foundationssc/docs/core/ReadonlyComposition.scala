package dev.typr.foundationssc.docs.core

import dev.typr.foundationssc.*

@SuppressWarnings(Array("unused"))
object ReadonlyComposition:
  private val tx: Transactor = null // placeholder

  val findIds: OperationRead[List[Int]] =
    Fragment.of("SELECT id FROM users").query(RowCodec.of(PgTypes.int4).all())
  val countUsers: OperationRead[Long] =
    Fragment.of("SELECT count(*) FROM users").queryExactlyOne(PgTypes.int8)

  val insertUser: Operation[Int] =
    Fragment.of("INSERT INTO users(name) VALUES('Alice')").update()

  // start
  // Combining read-only operations yields OperationRead
  val bothReads: OperationRead[(List[Int], Long)] =
    findIds.combine(countUsers)

  // Mixing in a write operation yields Operation (not OperationRead)
  val writeAndRead: Operation[(Int, List[Int])] =
    insertUser.combine(findIds)

  // transactRead works for read-only compositions
  val readResult: (List[Int], Long) = bothReads.transactRead(tx)

  // transact required when writes are involved
  val writeResult: (Int, List[Int]) = writeAndRead.transact(tx)
  // stop
