package dev.typr.foundations.docs.postgresql

import dev.typr.foundations.PgType
import dev.typr.foundations.PgTypes

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class Email(val value: String)

    // Create PgType from text
    val emailType: PgType<Email> = PgTypes.text.bimap(::Email, Email::value)
    //stop
}
