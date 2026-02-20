package dev.typr.foundations.docs.core;

import dev.typr.foundations.*;
import dev.typr.foundations.docs.core.UserRepo.User;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
//start
public final class UserService {
    private final Transactor tx;

    public UserService(Transactor tx) {
        this.tx = tx;
    }

    public List<User> listUsers() {
        return UserRepo.selectAll.transact(tx);
    }

    public Optional<User> findUser(int id) {
        return UserRepo.selectById.on(id).transact(tx);
    }
}
//stop
