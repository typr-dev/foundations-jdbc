package dev.typr.foundations.benchmark

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet
import javax.sql.DataSource

class JdbcTemplateBenchmark(ds: DataSource) : LibraryBenchmark {
    override val name = "JdbcTemplate"

    private val template = JdbcTemplate(ds)

    private val itemMapper = RowMapper { rs: ResultSet, _: Int ->
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
        return template.query("SELECT $COLUMNS FROM items", itemMapper)
    }
}
