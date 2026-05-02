package dev.typr.foundationskt.docs.sqlite

import dev.typr.foundationskt.*

@Suppress("unused")
class EnumType {
    //start
    enum class Status { PENDING, ACTIVE, COMPLETED }

    // SQLite has no native enum — pair with `CHECK (col IN ('PENDING','ACTIVE','COMPLETED'))` in DDL.
    val statusType: SqliteType<Status> = SqliteTypes.ofEnum<Status>()
    //stop
}
