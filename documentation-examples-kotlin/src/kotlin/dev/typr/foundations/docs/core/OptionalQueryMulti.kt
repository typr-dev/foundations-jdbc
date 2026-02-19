package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*

@Suppress("unused")
class OptionalQueryMulti {
    data class User(val id: Int, val name: String, val email: String)

    val userParser: RowParser<User> =
        RowParser.builder<User>()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(::User)

    lateinit var tx: Transactor
    lateinit var checker: dev.typr.foundations.QueryChecker

    //start
    // Multiple optional filters — each independently present or absent
    val search: SqlTemplate.Query3<String?, String?, Boolean, List<User>> =
        Fragment.of("SELECT id, name, email FROM users WHERE 1=1")
            .optionally(
                Fragment.of(" AND name ILIKE ").param(PgTypes.text))
            .optionally(
                Fragment.of(" AND email ILIKE ").param(PgTypes.text))
            .optionally(
                Fragment.of(" AND active = TRUE"))
            .append(" ORDER BY name")
            .query(userParser.all())

    // Each combination is type-safe
    fun example(): List<User> =
        search.on("%alice%", null, true).transact(tx)

    // Query analysis expands all 2³ = 8 combinations automatically
    fun verifyAllVariants() {
        checker.check(search)
    }
    //stop
}
