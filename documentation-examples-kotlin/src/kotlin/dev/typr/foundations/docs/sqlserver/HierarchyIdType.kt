package dev.typr.foundations.docs.sqlserver

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import dev.typr.foundations.data.HierarchyId

@Suppress("unused")
class HierarchyIdType {
    //start
    val hierarchyType: SqlServerType<HierarchyId> = SqlServerTypes.hierarchyid
    //stop
}
