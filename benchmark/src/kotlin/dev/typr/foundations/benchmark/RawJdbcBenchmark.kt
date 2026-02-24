package dev.typr.foundations.benchmark

import javax.sql.DataSource

class RawJdbcBenchmark(private val ds: DataSource) : LibraryBenchmark {
    override val name = "Raw JDBC"

    override fun selectAll(): List<Item> {
        ds.connection.use { conn ->
            conn.prepareStatement(SELECT_SQL).use { ps ->
                ps.executeQuery().use { rs ->
                    val items = mutableListOf<Item>()
                    while (rs.next()) {
                        items.add(
                            Item(
                                id = rs.getInt(1),
                                name = rs.getString(2),
                                description = rs.getString(3),
                                category = rs.getString(4),
                                tags = rs.getString(5),
                                quantity = rs.getInt(6),
                                price = rs.getBigDecimal(7),
                                weight = rs.getDouble(8),
                                active = rs.getBoolean(9),
                                rating = rs.getDouble(10),
                                createdAt = rs.getTimestamp(11).toLocalDateTime(),
                                updatedAt = rs.getTimestamp(12).toLocalDateTime(),
                                releaseDate = rs.getDate(13).toLocalDate(),
                                sku = rs.getString(14),
                                notes = rs.getString(15),
                            )
                        )
                    }
                    return items
                }
            }
        }
    }
}
