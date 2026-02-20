package dev.typr.foundations.docs.analysis;

import dev.typr.foundations.Fragment;
import dev.typr.foundations.Operation;
import dev.typr.foundations.PgTypes;
import dev.typr.foundations.RowCodec;
import dev.typr.foundations.QueryAnalysis;
import dev.typr.foundations.QueryAnalyzer;
import dev.typr.foundations.DatabaseException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class QueryAnalysisTestSuite {
    record User(int id, String name, String email) {}
    record Product(int id, String name) {}

    private final DataSource testDataSource = null; // placeholder

    private final RowCodec<User> userCodec =
        RowCodec.<User>builder()
            .field(PgTypes.int4, User::id)
            .field(PgTypes.text, User::name)
            .field(PgTypes.text, User::email)
            .build(User::new);

    private final RowCodec<Product> productCodec =
        RowCodec.<Product>builder()
            .field(PgTypes.int4, Product::id)
            .field(PgTypes.text, Product::name)
            .build(Product::new);

    //start
    void allQueriesTypeCheck() {
        try (var conn = testDataSource.getConnection()) {
            // Collect all queries to check
            List<Operation.Query<?>> queries = List.of(
                Fragment.of("""
                        SELECT id, name, email
                        FROM users WHERE id =
                        """)
                    .value(PgTypes.int4, 1)
                    .query(userCodec.all()),
                Fragment.of("""
                        SELECT id, name
                        FROM products
                        WHERE name LIKE
                        """)
                    .value(PgTypes.text, "%widget%")
                    .query(productCodec.all())
            );

            // Analyze each one
            List<String> failures = new ArrayList<>();
            for (var query : queries) {
                QueryAnalysis analysis =
                    QueryAnalyzer.analyze(query, conn)
                        .getFirst();
                if (!analysis.succeeded()) {
                    failures.add(analysis.report());
                }
            }

            // Report all failures at once
            if (!failures.isEmpty()) {
                throw new AssertionError(
                    "Query type check failed:\n\n"
                        + String.join("\n\n", failures));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
    //stop
}
