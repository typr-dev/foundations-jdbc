package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*

@Suppress("unused")
class PgDomainTypeArray {
    //start
    // Wrap once at the scalar level — the array codec carries the wrapper through
    // .array(), so no list-level bijection is needed.
    data class Name(val value: String) {
        companion object {
            val pgType: PgType<Name> =
                PgTypes.text.asDomain("person_name", ::Name, Name::value)
            val pgArrayType: PgType<List<Name>> = pgType.array()
        }
    }
    //stop
}
