package dev.typr.foundations.docs.landing

import dev.typr.kotlinfoundations.*
import dev.typr.kotlinfoundations.data.*
import javax.sql.DataSource

@Suppress("unused")
class SpringTransactorExample {
    //start
    // With Spring - inject the transactor, use @Transactional
    // @Service
    class OrderService(private val tx: Transactor) {  // Injected by Spring

        // @Transactional
        fun getGreeting(): String = Fragment.lit("SELECT 'Hello from Oracle' FROM dual")
            .query(RowParser.of(OracleTypes.varchar2).exactlyOne())
            .transact(tx)  // Joins Spring's transaction
    }

    // Configuration - just register the bean
    // @Bean
    fun transactor(dataSource: DataSource): Transactor =
        dev.typr.foundations.spring.SpringTransactor.create(dataSource)
    //stop
}
