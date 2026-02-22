package dev.typr.foundationskt.docs.core

import dev.typr.foundationskt.*
import dev.typr.foundationskt.docs.core.UserRepo.User

@Suppress("unused")
//start
class UserService(private val tx: Transactor) {
    fun listUsers(): List<User> =
        UserRepo.selectAll.transact(tx)

    fun findUser(id: Int): User? =
        UserRepo.selectById.on(id).transact(tx)
}
//stop
