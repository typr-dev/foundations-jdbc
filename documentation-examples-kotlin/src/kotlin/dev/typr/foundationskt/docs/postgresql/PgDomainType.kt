package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*

@Suppress("unused")
class PgDomainType {
    //start:scalar
    // PG schema:  CREATE DOMAIN person_name AS varchar(100);
    data class Name(val value: String) {
        companion object {
            val pgType: PgType<Name> =
                PgTypes.text.transform(::Name, Name::value).asDomain("person_name")
        }
    }
    //stop:scalar

    //start:array
    // Arrays of domains "just work" — `.array()` composes after `.asDomain(...)`. Use
    // `.transform(...)` at the list level if you want to map the container to a different
    // wrapper type without changing the schema.
    data class MiddleName(val value: Name)

    val middleNames: PgType<List<MiddleName>> =
        Name.pgType
            .array()
            .transform(
                { ns -> ns.map(::MiddleName) },
                { ms -> ms.map(MiddleName::value) }
            )
    //stop:array
}
