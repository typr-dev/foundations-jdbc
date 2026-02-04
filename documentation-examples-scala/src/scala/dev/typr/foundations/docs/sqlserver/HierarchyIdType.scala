package dev.typr.foundations.docs.sqlserver
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*



@SuppressWarnings(Array("unused"))
object HierarchyIdType:
  //start
  val hierarchyType: SqlServerType[HierarchyId] = SqlServerTypes.hierarchyid
  //stop
