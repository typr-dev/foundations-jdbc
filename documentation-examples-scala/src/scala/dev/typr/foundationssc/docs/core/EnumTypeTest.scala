package dev.typr.foundationssc.docs.core

import dev.typr.foundationssc.*
import dev.typr.foundationssc.connect.*

/** Verification that plain Scala 3 enums work with ofEnum — no extends java.lang.Enum needed. */
@SuppressWarnings(Array("unused"))
object EnumTypeTest:
  // Plain Scala 3 enum — NO extends java.lang.Enum
  enum Status:
    case PENDING, ACTIVE, COMPLETED

  // Just pass .values — works for any Scala 3 enum
  val pgStatus: PgType[Status] = PgTypes.ofEnum("status", Status.values)
  val duckStatus: DuckDbType[Status] = DuckDbTypes.ofEnum("status", Status.values)
  val mariaStatus: MariaType[Status] = MariaTypes.ofEnum(Status.values)
  val sqlServerStatus: SqlServerType[Status] = SqlServerTypes.ofEnum(Status.values)
  val db2Status: Db2Type[Status] = Db2Types.ofEnum(Status.values)
  val oracleStatus: OracleType[Status] = OracleTypes.ofEnum(Status.values)

  // Runtime verification with DuckDB
  def main(args: Array[String]): Unit =
    val tx = ConnectionSource.of(DuckDbConfig.inMemory().build()).transactor()

    tx.transact { conn =>
      Fragment.of("CREATE TYPE status AS ENUM('PENDING', 'ACTIVE', 'COMPLETED')").execute().run(conn)
      Fragment.of("CREATE TABLE enum_test (id INTEGER, status status)").execute().run(conn)

      Fragment.of("INSERT INTO enum_test VALUES (1, ").value(duckStatus, Status.ACTIVE).append(")").execute().run(conn)
      Fragment.of("INSERT INTO enum_test VALUES (2, ").value(duckStatus, Status.PENDING).append(")").execute().run(conn)

      val result = Fragment
        .of("SELECT status FROM enum_test WHERE id = 1")
        .queryExactlyOne(duckStatus)
        .run(conn)
      assert(result == Status.ACTIVE, s"Expected ACTIVE, got $result")

      val all = Fragment
        .of("SELECT status FROM enum_test ORDER BY id")
        .queryAll(duckStatus)
        .run(conn)
      assert(all == List(Status.ACTIVE, Status.PENDING), s"Expected [ACTIVE, PENDING], got $all")
    }

    println("Scala 3 enum test passed — no extends java.lang.Enum needed!")
