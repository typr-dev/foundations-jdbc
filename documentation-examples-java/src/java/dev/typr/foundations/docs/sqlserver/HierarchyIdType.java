package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;
import dev.typr.foundations.data.HierarchyId;

@SuppressWarnings("unused")
public class HierarchyIdType {
  // start
  SqlServerType<HierarchyId> hierarchyType = SqlServerTypes.hierarchyid;
  // stop
}
