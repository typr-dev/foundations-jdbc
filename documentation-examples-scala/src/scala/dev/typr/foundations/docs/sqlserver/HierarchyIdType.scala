package dev.typr.foundations.docs.sqlserver

import dev.typr.foundations.{SqlServerType, SqlServerTypes}
import dev.typr.foundations.data.HierarchyId

@SuppressWarnings(Array("unused"))
object HierarchyIdType:
  //start
  val hierarchyType: SqlServerType[HierarchyId] = SqlServerTypes.hierarchyid
  //stop
