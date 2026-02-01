import dev.typr.foundations.*
import dev.typr.foundations.connect.duckdb.*

fun main() {
    val tx = DuckDbConfig.builder(":memory:").build().transactor()
    val answer: Int = tx.execute { conn ->
        Fragment.lit("SELECT 42")
            .query(RowParsers.of(DuckDbTypes.integer).exactlyOne())
            .run(conn)
    }
    println("Result: $answer")
}
