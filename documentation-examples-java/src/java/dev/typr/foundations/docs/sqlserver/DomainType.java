package dev.typr.foundations.docs.sqlserver;

import dev.typr.foundations.SqlServerType;
import dev.typr.foundations.SqlServerTypes;

@SuppressWarnings("unused")
public class DomainType {
    //start
    // Wrapper type
    public record OrderId(Integer value) {}

    // Create SqlServerType from INT
    SqlServerType<OrderId> orderIdType =
        SqlServerTypes.int_.bimap(OrderId::new, OrderId::value);
    //stop
}
