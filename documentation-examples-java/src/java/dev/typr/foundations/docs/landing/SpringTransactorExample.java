package dev.typr.foundations.docs.landing;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.OracleTypes;
import dev.typr.foundations.RowParser;
import dev.typr.foundations.Transactor;

import javax.sql.DataSource;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class SpringTransactorExample {
    //start
    // With Spring - inject the transactor, use @Transactional
    // @Service
    class OrderService {
        private final Transactor tx;  // Injected by Spring

        OrderService(Transactor tx) {
            this.tx = tx;
        }

        // @Transactional
        String getGreeting() throws SQLException {
            return Fragment.lit("SELECT 'Hello from Oracle' FROM dual")
                .query(RowParser.of(OracleTypes.varchar2).exactlyOne())
                .transact(tx);  // Joins Spring's transaction
        }
    }

    // Configuration - just register the bean
    // @Bean
    Transactor transactor(DataSource dataSource) {
        return dev.typr.foundations.spring.SpringTransactor.create(dataSource);
    }
    //stop
}
