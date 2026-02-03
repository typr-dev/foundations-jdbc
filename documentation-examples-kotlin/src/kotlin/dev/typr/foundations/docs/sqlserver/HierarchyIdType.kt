package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.SqlServerType
import dev.typr.foundations.SqlServerTypes
import dev.typr.foundations.data.HierarchyId

@Suppress("unused")
class HierarchyIdType {
    //start
    val hierarchyType: SqlServerType<HierarchyId> = SqlServerTypes.hierarchyid
    //stop
}
