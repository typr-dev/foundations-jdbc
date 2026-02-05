package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.data.*

import javax.sql.DataSource
import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object SpringTransactorExample:
  //start
  // With Spring - inject the transactor, use @Transactional
  // @Service
  class OrderService(tx: Transactor):  // Injected by Spring

    // @Transactional
    @throws[SQLException]
    def getGreeting(): String =
      Fragment.lit("SELECT 'Hello from Oracle' FROM dual")
        .query(RowParser.of(OracleTypes.varchar2).exactlyOne())
        .transact(tx)  // Joins Spring's transaction

  // Configuration - just register the bean
  // @Bean
  def transactor(dataSource: DataSource): Transactor =
    dev.typr.foundations.spring.SpringTransactor.create(dataSource)
  //stop
