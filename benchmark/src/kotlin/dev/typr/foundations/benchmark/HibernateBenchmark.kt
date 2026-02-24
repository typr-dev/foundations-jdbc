package dev.typr.foundations.benchmark

import jakarta.persistence.*
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import javax.sql.DataSource

@Entity
@Table(name = "items")
class ItemEntity {
    @Id
    var id: Int = 0
    var name: String = ""
    var description: String = ""
    var category: String = ""
    var tags: String = ""
    var quantity: Int = 0
    var price: BigDecimal = BigDecimal.ZERO
    var weight: Double = 0.0
    var active: Boolean = false
    var rating: Double = 0.0

    @Column(name = "created_at")
    var createdAt: LocalDateTime = LocalDateTime.MIN

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.MIN

    @Column(name = "release_date")
    var releaseDate: LocalDate = LocalDate.MIN

    var sku: String = ""
    var notes: String = ""

    fun toItem(): Item = Item(
        id = id,
        name = name,
        description = description,
        category = category,
        tags = tags,
        quantity = quantity,
        price = price,
        weight = weight,
        active = active,
        rating = rating,
        createdAt = createdAt,
        updatedAt = updatedAt,
        releaseDate = releaseDate,
        sku = sku,
        notes = notes,
    )
}

fun buildSessionFactory(ds: DataSource): SessionFactory {
    val registry = StandardServiceRegistryBuilder()
        .applySetting("hibernate.connection.datasource", ds)
        .applySetting("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
        .applySetting("hibernate.show_sql", false)
        .applySetting("hibernate.hbm2ddl.auto", "none")
        .build()
    return MetadataSources(registry)
        .addAnnotatedClass(ItemEntity::class.java)
        .buildMetadata()
        .buildSessionFactory()
}

class HibernateEntityBenchmark(ds: DataSource) : LibraryBenchmark {
    override val name = "Hibernate (entity)"

    private val sessionFactory: SessionFactory = buildSessionFactory(ds)

    override fun selectAll(): List<Item> {
        return sessionFactory.openSession().use { session ->
            session.createNativeQuery("SELECT $COLUMNS FROM items", ItemEntity::class.java)
                .resultList
                .map { it.toItem() }
        }
    }
}

class HibernateNativeBenchmark(ds: DataSource) : LibraryBenchmark {
    override val name = "Hibernate (native)"

    private val sessionFactory: SessionFactory = buildSessionFactory(ds)

    override fun selectAll(): List<Item> {
        return sessionFactory.openSession().use { session ->
            session.createNativeQuery("SELECT $COLUMNS FROM items", Array<Any>::class.java)
                .resultList
                .map { row ->
                    Item(
                        id = (row[0] as Number).toInt(),
                        name = row[1] as String,
                        description = row[2] as String,
                        category = row[3] as String,
                        tags = row[4] as String,
                        quantity = (row[5] as Number).toInt(),
                        price = row[6] as BigDecimal,
                        weight = (row[7] as Number).toDouble(),
                        active = row[8] as Boolean,
                        rating = (row[9] as Number).toDouble(),
                        createdAt = (row[10] as java.sql.Timestamp).toLocalDateTime(),
                        updatedAt = (row[11] as java.sql.Timestamp).toLocalDateTime(),
                        releaseDate = (row[12] as java.sql.Date).toLocalDate(),
                        sku = row[13] as String,
                        notes = row[14] as String,
                    )
                }
        }
    }
}
