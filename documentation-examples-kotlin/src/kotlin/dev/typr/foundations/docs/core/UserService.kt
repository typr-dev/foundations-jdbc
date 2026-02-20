package dev.typr.foundations.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundations.docs.core.UserRepo.User

@Suppress("unused")
//start
class UserService(private val tx: Transactor) {
    fun listUsers(): List<User> =
        UserRepo.selectAll.transact(tx)

    fun findUser(id: Int): User? =
        UserRepo.selectById.on(id).transact(tx)
}
//stop
