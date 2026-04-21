package dev.typr.foundations.benchmark

import dev.typr.foundations.TransactorJdbc
import dev.typr.foundations.connect.PgConfig
import dev.typr.foundations.hikari.HikariDataSourceFactory
import dev.typr.foundations.hikari.PoolConfig
import dev.typr.foundations.hikari.PooledDataSource
import dev.typr.foundations.pg.PgPipelineConfig
import dev.typr.foundations.pg.PgPipelinePool
import io.vertx.core.Vertx
import io.vertx.pgclient.PgConnectOptions
import io.vertx.pgclient.PgPool
import io.vertx.sqlclient.PoolOptions
import jakarta.persistence.*
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.math.BigDecimal
import java.time.LocalDateTime

// ==================== Pool Creation Factories ====================

fun pgConfig(port: Int): PgConfig =
    PgConfig.builder(PG_HOST, port, PG_DB, PG_USER, PG_PASS).build()

fun pgConfigWithPrepare(port: Int): PgConfig =
    PgConfig.builder(PG_HOST, port, PG_DB, PG_USER, PG_PASS).prepareThreshold(1).build()

fun createHikariPool(port: Int, poolSize: Int): PooledDataSource =
    HikariDataSourceFactory.create(
        pgConfigWithPrepare(port),
        PoolConfig.builder()
            .maximumPoolSize(poolSize)
            .minimumIdle(poolSize)
            .connectionTimeout(java.time.Duration.ofSeconds(120))
            .build()
    )

fun createTransactor(port: Int, poolSize: Int): Pair<PooledDataSource, TransactorJdbc> {
    val ds = createHikariPool(port, poolSize)
    val tx = ds.transactor()
    return Pair(ds, tx)
}

fun createPipelinePool(port: Int, connectionCount: Int): PgPipelinePool =
    PgPipelinePool.create(
        pgConfig(port),
        PgPipelineConfig.builder()
            .connectionCount(connectionCount)
            .pipeliningLimit(256)
            .queryTimeout(java.time.Duration.ofMinutes(5))
            .build()
    )

fun createPipelinePoolWithTx(port: Int, connectionCount: Int, maxTxConns: Int): PgPipelinePool =
    PgPipelinePool.create(
        pgConfig(port),
        PgPipelineConfig.builder()
            .connectionCount(connectionCount)
            .maxTransactionConnections(maxTxConns)
            .pipeliningLimit(256)
            .queryTimeout(java.time.Duration.ofMinutes(5))
            .build()
    )

data class RawVertxPool(val pool: PgPool, val vertx: Vertx) : AutoCloseable {
    override fun close() {
        try {
            pool.close().toCompletionStage().toCompletableFuture()
                .get(5, java.util.concurrent.TimeUnit.SECONDS)
        } catch (_: Exception) {}
        try {
            vertx.close().toCompletionStage().toCompletableFuture()
                .get(5, java.util.concurrent.TimeUnit.SECONDS)
        } catch (_: Exception) {}
    }
}

fun createRawVertxPool(port: Int, poolSize: Int): RawVertxPool {
    val vertx = Vertx.vertx()
    val connectOptions = PgConnectOptions()
        .setHost(PG_HOST)
        .setPort(port)
        .setDatabase(PG_DB)
        .setUser(PG_USER)
        .setPassword(PG_PASS)
        .setCachePreparedStatements(true)
        .setPreparedStatementCacheMaxSize(256)
        .setPipeliningLimit(256)
    val pool = PgPool.pool(vertx, connectOptions, PoolOptions().setMaxSize(poolSize))
    return RawVertxPool(pool, vertx)
}

// ==================== Hibernate Entity Classes ====================

@Entity
@Table(name = "bench_read")
class BenchRowEntity {
    @Id
    var id: Int = 0
    var name: String = ""
    var value: BigDecimal = BigDecimal.ZERO
    var active: Boolean = false
    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.MIN
    var category: String = ""
}

@Entity
@Table(name = "bench_write")
class WriteRowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bench_write_seq")
    @SequenceGenerator(name = "bench_write_seq", sequenceName = "bench_write_id_seq", allocationSize = 50)
    var id: Long = 0
    var name: String = ""
    var value: BigDecimal = BigDecimal.ZERO
    var active: Boolean = false
    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.MIN
    var category: String = ""
}

// ==================== Hibernate SessionFactory ====================

fun createHibernateSessionFactory(port: Int, poolSize: Int): SessionFactory {
    val cfg = Configuration()
    cfg.setProperty("hibernate.connection.url", "jdbc:postgresql://$PG_HOST:$port/$PG_DB")
    cfg.setProperty("hibernate.connection.username", PG_USER)
    cfg.setProperty("hibernate.connection.password", PG_PASS)
    cfg.setProperty("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")
    cfg.setProperty("hibernate.hikari.maximumPoolSize", poolSize.toString())
    cfg.setProperty("hibernate.hikari.minimumIdle", poolSize.toString())
    cfg.setProperty("hibernate.hikari.connectionTimeout", "120000")
    cfg.setProperty("hibernate.show_sql", "false")
    cfg.setProperty("hibernate.hbm2ddl.auto", "none")
    cfg.setProperty("hibernate.jdbc.batch_size", "25")
    cfg.setProperty("hibernate.order_inserts", "true")
    cfg.addAnnotatedClass(BenchRowEntity::class.java)
    cfg.addAnnotatedClass(WriteRowEntity::class.java)
    return cfg.buildSessionFactory()
}
