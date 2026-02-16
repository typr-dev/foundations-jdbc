package dev.typr.foundations.docs.oracle;

import dev.typr.foundations.OracleType;
import dev.typr.foundations.OracleTypes;

@SuppressWarnings("unused")
public class DomainType {
    //start
    // Wrapper type
    public record EmployeeId(Long value) {}

    // Create OracleType from NUMBER
    OracleType<EmployeeId> empIdType =
        OracleTypes.numberLong.transform(EmployeeId::new, EmployeeId::value);
    //stop
}
