---
title: Spring Boot
---

# Spring Boot

The `foundations-jdbc-spring` module provides auto-configuration. Add it to your dependencies:

```kotlin
implementation("dev.typr:foundations-jdbc-spring:version")
```

A `Transactor` bean is automatically created when a `DataSource` is available. Simply inject it:

```java
@Service
public class ProductService {
    private final Transactor tx;

    public ProductService(Transactor tx) {
        this.tx = tx;
    }

    public List<Product> findAll() throws SQLException {
        return selectAll.transact(tx);
    }
}
```

The Spring transactor automatically adapts to the current transaction context:
- Inside `@Transactional`: joins the existing Spring-managed transaction
- Outside `@Transactional`: manages its own transaction (begin/commit/rollback)

If you need to configure the transactor manually:

```java
@Configuration
public class AppConfig {
    @Bean
    public Transactor transactor(DataSource dataSource) {
        return SpringTransactor.create(dataSource);
    }
}
```
