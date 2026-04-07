package dev.typr.foundationskt.docs.blog.struct

import dev.typr.foundationskt.*

@Suppress("unused")
class PgDeepNesting {
    //start
    data class Skill(val name: String, val level: Int)
    data class Employee(val name: String, val role: String, val skills: Array<Skill>)
    data class Department(val name: String, val members: Array<Employee>)

    val skillStruct = PgStruct.builder<Skill>("skill")
        .field("name", PgTypes.text, Skill::name)
        .field("level", PgTypes.int4, Skill::level)
        .build(::Skill)

    val employeeStruct = PgStruct.builder<Employee>("employee")
        .field("name", PgTypes.text, Employee::name)
        .field("role", PgTypes.text, Employee::role)
        .nestedArrayField("skills", skillStruct, Employee::skills) { arrayOfNulls(it) }
        .build(::Employee)

    val departmentStruct = PgStruct.builder<Department>("department")
        .field("name", PgTypes.text, Department::name)
        .nestedArrayField("members", employeeStruct, Department::members) { arrayOfNulls(it) }
        .build(::Department)

    val departmentType: PgType<Department> = departmentStruct.asType()
    //stop
}
