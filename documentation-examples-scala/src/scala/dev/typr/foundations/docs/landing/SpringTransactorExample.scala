package dev.typr.foundations.docs.landing
import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.data.*
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
