import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*

fun main() {
    val tx = SimpleDataSource.create(DuckDbConfig.inMemory().build()).transactor()
    val answer: Int = tx.transact { conn ->
        sql { "SELECT 42" }
            .query(RowCodec.of(DuckDbTypes.integer).exactlyOne())
            .run(conn)
    }
    println("Result: $answer")
}
