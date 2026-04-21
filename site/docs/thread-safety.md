---
title: Thread Safety
---

# Thread Safety

`Transactor` is stateless and thread-safe. Share a single instance across your application.

Each call to `transactor.execute(...)` obtains its own connection from the underlying `ConnectionSource`. Connections are not shared across operations or threads.

Do not share connections across threads. If you need to run multiple operations on the same connection (e.g., within a transaction), do so within a single `transact` block:

```java
tx.transact(mc -> {
    var user = mc.execute(findUser);
    var orders = mc.execute(findOrders);
    return new Dashboard(user, orders);
});
```
