package dev.typr.foundations.docs.postgresql

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*

@Suppress("unused")
class DomainType {
    //start
    // Wrapper type
    data class Email(val value: String)

    // Create PgType from text
    val emailType: PgType<Email> = PgTypes.text.bimap(::Email, Email::value)
    //stop
}
