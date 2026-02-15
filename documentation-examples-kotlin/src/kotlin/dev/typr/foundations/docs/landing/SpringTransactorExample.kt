package dev.typr.foundations.docs.landing

import dev.typr.foundationskt.*
import dev.typr.foundationskt.data.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Suppress("unused")
class SpringTransactorExample {
    //start
    @Service
    class OrderService(private val tx: Transactor) {

        @Transactional
        fun getGreeting(): String = Sql { "SELECT 'Hello from Oracle' FROM dual" }
            .query(RowParser.of(OracleTypes.varchar2).exactlyOne())
            .transact(tx)
    }
    //stop
}
