package dev.typr.foundations.docs.landing;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.Transactor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@SuppressWarnings("unused")
public class SpringTransactorExample {
    //start
    @Service
    class OrderService {
        private final Transactor tx;

        OrderService(Transactor tx) {
            this.tx = tx;
        }

        @Transactional
        String getGreeting() throws SQLException {
            return Fragment
                .of("SELECT 'Hello from Oracle' FROM dual")
                .query(RowCodec.of(OracleTypes.varchar2)
                    .exactlyOne())
                .transact(tx);
        }
    }
    //stop
}
