package dev.typr.foundations.docs.postgresql

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class Email(val value: String)

    // Create PgType from text
    val emailType: PgType<Email> = PgTypes.text.transform(::Email, Email::value)
    //stop
}
