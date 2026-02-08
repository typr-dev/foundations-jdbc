package dev.typr.foundations.docs.landing
import dev.typr.scalafoundations.*
import dev.typr.scalafoundations.Fragment.sql
import dev.typr.scalafoundations.data.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.sql.SQLException

@SuppressWarnings(Array("unused"))
object SpringTransactorExample:
  //start
  @Service
  class OrderService(tx: Transactor):

    @Transactional
    @throws[SQLException]
    def getGreeting(): String =
      sql"SELECT 'Hello from Oracle' FROM dual"
        .query(RowParser.of(OracleTypes.varchar2).exactlyOne())
        .transact(tx)
  //stop
