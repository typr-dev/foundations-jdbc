package dev.typr.foundationskt.docs.postgresql

import dev.typr.foundationskt.*

@Suppress("unused")
class PgDomainTypeScalar {
    //start
    // PG schema:  CREATE DOMAIN person_name AS varchar(100);
    data class Name(val value: String) {
        companion object {
            val pgType: PgType<Name> =
                PgTypes.text.asDomain("person_name", ::Name, Name::value)
        }
    }
    //stop
}
