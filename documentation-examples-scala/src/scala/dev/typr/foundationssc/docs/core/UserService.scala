package dev.typr.foundationssc.docs.core
import dev.typr.foundationssc.*
import dev.typr.foundationssc.docs.core.UserRepo.User

@SuppressWarnings(Array("unused"))
//start
class UserService(tx: Transactor):
  def listUsers(): List[User] =
    UserRepo.selectAll.transact(tx)

  def findUser(id: Int): Option[User] =
    UserRepo.selectById.on(id).transact(tx)
//stop
