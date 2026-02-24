package dev.typr.foundations.benchmark

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import javax.sql.DataSource

class JdbiBenchmark(ds: DataSource) : LibraryBenchmark {
    override val name = "JDBI"

    private val jdbi: Jdbi = Jdbi.create(ds)

    private val itemMapper = RowMapper { rs: ResultSet, _: StatementContext ->
        Item(
            id = rs.getInt("id"),
            name = rs.getString("name"),
            description = rs.getString("description"),
            category = rs.getString("category"),
            tags = rs.getString("tags"),
            quantity = rs.getInt("quantity"),
            price = rs.getBigDecimal("price"),
            weight = rs.getDouble("weight"),
            active = rs.getBoolean("active"),
            rating = rs.getDouble("rating"),
            createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
            updatedAt = rs.getTimestamp("updated_at").toLocalDateTime(),
            releaseDate = rs.getDate("release_date").toLocalDate(),
            sku = rs.getString("sku"),
            notes = rs.getString("notes"),
        )
    }

    override fun selectAll(): List<Item> {
        return jdbi.withHandle<List<Item>, RuntimeException> { handle ->
            handle.createQuery("SELECT $COLUMNS FROM items")
                .map(itemMapper)
                .list()
        }
    }
}
