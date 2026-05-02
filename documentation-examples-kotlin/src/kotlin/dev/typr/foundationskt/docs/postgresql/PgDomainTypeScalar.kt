package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*

@Suppress("unused")
class PgDomainTypeScalar {
    //start
    // PG schema:  CREATE DOMAIN person_name AS varchar(100);
    data class Name(val value: String) {
        companion object {
            val pgType: PgType<Name> =
                PgTypes.text.transform(::Name, Name::value).asDomain("person_name")
        }
    }
    //stop
}
