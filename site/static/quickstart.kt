import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*

fun main() {
    val tx = SimpleDataSource.create(DuckDbConfig.inMemory().build()).transactor()
    val answer: Int = tx.transact { conn ->
        Fragment.of("SELECT 42")
            .query(RowParser.of(DuckDbTypes.integer).exactlyOne())
            .run(conn)
    }
    println("Result: $answer")
}
