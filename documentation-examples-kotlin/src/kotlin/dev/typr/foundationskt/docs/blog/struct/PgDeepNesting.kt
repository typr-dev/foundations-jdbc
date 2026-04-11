package dev.typr.foundationskt.docs.blog.struct

import dev.typr.foundationskt.*

@Suppress("unused")
class PgDeepNesting {
    //start
    data class Skill(val name: String, val level: Int)
    data class Employee(val name: String, val role: String, val skills: Array<Skill>)
    data class Department(val name: String, val members: Array<Employee>)

    val skillType: PgType<Skill> = PgTypes.compositeOf(
        RowCodec.namedBuilder<Skill>()
            .field("name", PgTypes.text, Skill::name)
            .field("level", PgTypes.int4, Skill::level)
            .build(::Skill))

    val employeeType: PgType<Employee> = PgTypes.compositeOf(
        RowCodec.namedBuilder<Employee>()
            .field("name", PgTypes.text, Employee::name)
            .field("role", PgTypes.text, Employee::role)
            .field("skills", skillType.array(), Employee::skills)
            .build(::Employee))

    val departmentType: PgType<Department> = PgTypes.compositeOf(
        RowCodec.namedBuilder<Department>()
            .field("name", PgTypes.text, Department::name)
            .field("members", employeeType.array(), Department::members)
            .build(::Department))
    //stop
}
