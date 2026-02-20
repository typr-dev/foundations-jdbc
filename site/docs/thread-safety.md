---
title: Thread Safety
---

# Thread Safety

`Transactor` is stateless and thread-safe. Share a single instance across your application.

Each call to `transactor.execute(...)` obtains its own connection from the underlying `ConnectionSource`. Connections are not shared across operations or threads.

Do not share `Connection` objects across threads. If you need to run multiple operations on the same connection (e.g., within a transaction), do so within a single `execute` block:

```java
tx.execute(conn -> {
    var user = findUser.runChecked(conn);
    var orders = findOrders.runChecked(conn);
    return new Dashboard(user, orders);
});
```
