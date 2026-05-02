package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*

@Suppress("unused")
class PgDomainTypeArray {
    //start
    // Arrays of domains "just work" — `.array()` composes after `.asDomain(...)`. Use
    // `.transform(...)` at the list level to map the container to a different wrapper type
    // without changing the schema.
    data class Name(val value: String) {
        companion object {
            val pgType: PgType<Name> =
                PgTypes.text.transform(::Name, Name::value).asDomain("person_name")
        }
    }

    data class MiddleName(val value: Name)

    val middleNames: PgType<List<MiddleName>> =
        Name.pgType
            .array()
            .transform(
                { ns -> ns.map(::MiddleName) },
                { ms -> ms.map(MiddleName::value) }
            )
    //stop
}
