package dev.typr.foundationskt.docs.blog.struct

import dev.typr.foundationskt.*

@Suppress("unused")
class OracleObjectExample {
    //start
    data class Skill(val name: String, val level: Int)
    data class Employee(val name: String, val role: String, val skills: List<Skill>)

    val skillType: OracleType<Skill> = OracleObject.builder<Skill>("SKILL_T")
        .field("NAME", OracleTypes.varchar2(50), Skill::name)
        .field("LEVEL_NUM", OracleTypes.numberInt, Skill::level)
        .build(::Skill)
        .asType()

    val skillsType: OracleType<List<Skill>> = OracleVArray.of("SKILLS_T", 20, skillType)

    val employeeType: OracleType<Employee> = OracleObject.builder<Employee>("EMPLOYEE_T")
        .field("NAME", OracleTypes.varchar2(100), Employee::name)
        .field("ROLE_NAME", OracleTypes.varchar2(50), Employee::role)
        .field("SKILLS", skillsType, Employee::skills)
        .build(::Employee)
        .asType()

    val employeesType: OracleType<List<Employee>> = OracleNestedTable.of("EMPLOYEES_T", employeeType)
    //stop
}
