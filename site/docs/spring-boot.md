---
title: Spring Boot
---

import Snippet from '@site/src/components/Snippet';

# Spring Boot

The `foundations-jdbc-spring` module integrates foundations-jdbc with Spring Boot. It provides auto-configuration that creates a `Transactor` bean backed by your Spring-managed `DataSource`, and automatically participates in `@Transactional` contexts.

## Dependencies

Add the Spring integration module alongside `spring-boot-starter-jdbc`:

import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';

<Tabs groupId="lang">
<TabItem value="java" label="Java">

**Gradle:**
```kotlin
implementation("dev.typr:foundations-jdbc-spring:VERSION")
implementation("org.springframework.boot:spring-boot-starter-jdbc")
```

**Maven:**
```xml
<dependency>
    <groupId>dev.typr</groupId>
    <artifactId>foundations-jdbc-spring</artifactId>
    <version>VERSION</version>
</dependency>
```

</TabItem>
<TabItem value="kotlin" label="Kotlin">

**Gradle:**
```kotlin
implementation("dev.typr:foundations-jdbc-spring:VERSION")
implementation("dev.typr:foundations-jdbc-kotlin:VERSION")
implementation("org.springframework.boot:spring-boot-starter-jdbc")
```

**Maven:**
```xml
<dependency>
    <groupId>dev.typr</groupId>
    <artifactId>foundations-jdbc-spring</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>
    <groupId>dev.typr</groupId>
    <artifactId>foundations-jdbc-kotlin</artifactId>
    <version>VERSION</version>
</dependency>
```

</TabItem>
<TabItem value="scala" label="Scala">

**Gradle:**
```kotlin
implementation("dev.typr:foundations-jdbc-spring:VERSION")
implementation("dev.typr:foundations-jdbc-scala_3:VERSION")
implementation("org.springframework.boot:spring-boot-starter-jdbc")
```

**Maven:**
```xml
<dependency>
    <groupId>dev.typr</groupId>
    <artifactId>foundations-jdbc-spring</artifactId>
    <version>VERSION</version>
</dependency>
<dependency>
    <groupId>dev.typr</groupId>
    <artifactId>foundations-jdbc-scala_3</artifactId>
    <version>VERSION</version>
</dependency>
```

</TabItem>
</Tabs>

:::tip No HikariCP module needed
You do **not** need `foundations-jdbc-hikari`. Spring Boot's `spring-boot-starter-jdbc` already configures a HikariCP connection pool. The Spring transactor obtains connections through Spring's `DataSource`, which is already pooled.
:::

## Auto-Configuration

`TransactorAutoConfiguration` creates a `Transactor` bean automatically when:

- A `DataSource` bean is present (`@ConditionalOnBean(DataSource.class)`)
- No existing `Transactor` bean is defined (`@ConditionalOnMissingBean(Transactor.class)`)

Just configure your datasource in `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=myuser
spring.datasource.password=mypassword
```

Then inject the `Transactor` directly into your services:

<Snippet file="landing/SpringTransactorExample" />

## Transaction Behavior

The `SpringTransactor` adapts its behavior based on the current Spring transaction context:

| Context | Behavior |
|---------|----------|
| Inside `@Transactional` | Joins the existing Spring-managed transaction. Does not change auto-commit, commit, or rollback — Spring controls the transaction lifecycle. |
| Outside `@Transactional` | Manages its own transaction: sets auto-commit to false, commits on success, rolls back on error, and releases the connection. |

This means `@Transactional` works exactly as you'd expect. Multiple `transact()` calls inside a `@Transactional` method share the same connection and transaction:

```java
@Service
public class OrderService {
    private final Transactor tx;

    public OrderService(Transactor tx) {
        this.tx = tx;
    }

    @Transactional
    public void placeOrder(Order order) {
        insertOrder.bind(order).transact(tx);
        updateInventory.bind(order.itemId()).transact(tx);
        // both share the same transaction — committed together by Spring
    }

    public List<Order> listOrders() {
        // no @Transactional — the transactor manages its own transaction
        return selectOrders.transact(tx);
    }
}
```

Database errors throw `DatabaseException`, which is unchecked — so `@Transactional` rolls back automatically without any extra configuration.

## Manual Configuration

If you need to customize the transactor (e.g., different datasource, adding a query listener), disable auto-configuration by defining your own `Transactor` bean:

```java
@Configuration
public class AppConfig {
    @Bean
    public Transactor transactor(DataSource dataSource) {
        return SpringTransactor.create(dataSource);
    }
}
```

Since `TransactorAutoConfiguration` uses `@ConditionalOnMissingBean`, your custom bean takes precedence.

## Example Project

See the [`example-spring-boot`](https://github.com/typr-dev/foundations-jdbc/tree/main/example-spring-boot) directory for a working Spring Boot application using DuckDB with foundations-jdbc.
