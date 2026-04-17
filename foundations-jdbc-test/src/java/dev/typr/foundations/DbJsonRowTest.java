package dev.typr.foundations;

import dev.typr.foundations.connect.DuckDbConfig;
import dev.typr.foundations.data.Json;
import dev.typr.foundations.data.JsonValue;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;

/**
 * Tests for DbJsonRow — roundtripping arrays of structs as JSON across the JDBC boundary.
 *
 * <p>Uses DuckDB (embedded, no Docker) to verify the full flow: insert data → aggregate with
 * json_group_array → parse with RowCodec-derived codec.
 */
public class DbJsonRowTest {

  record OrderLine(String product, int qty, BigDecimal price) {}

  // Define once — used for both ResultSet reading and JSON parsing
  static final RowCodecNamed<OrderLine> lineCodec =
      RowCodec.<OrderLine>namedBuilder()
          .field("product", DuckDbTypes.varchar, OrderLine::product)
          .field("qty", DuckDbTypes.integer, OrderLine::qty)
          .field("price", DuckDbTypes.decimalOf(10, 2), OrderLine::price)
          .build(OrderLine::new);

  static final DbJson<List<OrderLine>> linesCodec = DbJsonRow.jsonArray(lineCodec).list();

  static Transactor newDuckDbTransactor() throws Exception {
    var f = java.io.File.createTempFile("duckdb_test", ".db");
    f.delete(); // DuckDB needs a non-existent path
    f.deleteOnExit();
    return Transactor.create(DuckDbConfig.builder(f.getAbsolutePath()).build());
  }

  @Test
  public void roundtripThroughJsonColumn() throws Exception {
    var tx = newDuckDbTransactor();

    List<OrderLine> original =
        List.of(
            new OrderLine("Widget", 3, new BigDecimal("9.99")),
            new OrderLine("Gadget", 1, new BigDecimal("24.50")),
            new OrderLine("Sprocket", 12, new BigDecimal("0.75")));

    String json = linesCodec.toJson(original).encode();

    Fragment.of("CREATE TABLE orders (lines JSON)").update().transact(tx);

    Fragment.of("INSERT INTO orders (lines) VALUES (")
        .value(DuckDbTypes.json, new Json(json))
        .append(")")
        .update()
        .transact(tx);

    Json fromDb =
        Fragment.of("SELECT lines FROM orders")
            .query(RowCodec.of(DuckDbTypes.json).exactlyOne())
            .transact(tx);

    List<OrderLine> decoded = linesCodec.fromJson(JsonValue.parse(fromDb.value()));
    assertEqual(original, decoded);
  }

  @Test
  public void aggregateChildRowsAsJson() throws Exception {
    var tx = newDuckDbTransactor();

    Fragment.of("CREATE TABLE customers (id INTEGER, name VARCHAR)").update().transact(tx);
    Fragment.of(
            "CREATE TABLE order_lines (customer_id INTEGER, product VARCHAR, qty INTEGER, price"
                + " DECIMAL(10,2))")
        .update()
        .transact(tx);
    Fragment.of("INSERT INTO customers VALUES (1, 'Alice'), (2, 'Bob')").update().transact(tx);
    Fragment.of(
            "INSERT INTO order_lines VALUES "
                + "(1, 'Widget', 3, 9.99), (1, 'Gadget', 1, 24.50), (2, 'Sprocket', 12, 0.75)")
        .update()
        .transact(tx);

    // Single query: parent rows with child rows aggregated as JSON
    record CustomerWithLines(String name, Json linesJson) {}

    RowCodec<CustomerWithLines> customerCodec =
        RowCodec.<CustomerWithLines>builder()
            .field(DuckDbTypes.varchar, CustomerWithLines::name)
            .field(DuckDbTypes.json, CustomerWithLines::linesJson)
            .build(CustomerWithLines::new);

    List<CustomerWithLines> customers =
        Fragment.of(
                "SELECT c.name, "
                    + "(SELECT json_group_array(json_array(l.product, l.qty, l.price)) "
                    + " FROM order_lines l WHERE l.customer_id = c.id) "
                    + "FROM customers c ORDER BY c.id")
            .query(customerCodec.all())
            .transact(tx);

    // Alice has 2 order lines
    CustomerWithLines alice = customers.get(0);
    List<OrderLine> aliceLines = linesCodec.fromJson(JsonValue.parse(alice.linesJson().value()));
    if (!alice.name().equals("Alice")) throw new AssertionError("Expected Alice");
    if (aliceLines.size() != 2) throw new AssertionError("Expected 2 lines for Alice");
    if (!aliceLines.get(0).product().equals("Widget")) throw new AssertionError("Expected Widget");
    if (aliceLines.get(0).qty() != 3) throw new AssertionError("Expected qty 3");
    if (!aliceLines.get(1).product().equals("Gadget")) throw new AssertionError("Expected Gadget");

    // Bob has 1 order line
    CustomerWithLines bob = customers.get(1);
    List<OrderLine> bobLines = linesCodec.fromJson(JsonValue.parse(bob.linesJson().value()));
    if (!bob.name().equals("Bob")) throw new AssertionError("Expected Bob");
    if (bobLines.size() != 1) throw new AssertionError("Expected 1 line for Bob");
    if (!bobLines.get(0).product().equals("Sprocket"))
      throw new AssertionError("Expected Sprocket");
    if (bobLines.get(0).qty() != 12) throw new AssertionError("Expected qty 12");
  }

  @Test
  public void objectEncoding() throws Exception {
    DbJson<List<OrderLine>> objectCodec = DbJsonRow.jsonObject(lineCodec).list();

    List<OrderLine> original =
        List.of(
            new OrderLine("Widget", 3, new BigDecimal("9.99")),
            new OrderLine("Gadget", 1, new BigDecimal("24.50")));

    String json = objectCodec.toJson(original).encode();

    var tx = newDuckDbTransactor();
    Fragment.of("CREATE TABLE orders (lines JSON)").update().transact(tx);

    Fragment.of("INSERT INTO orders (lines) VALUES (")
        .value(DuckDbTypes.json, new Json(json))
        .append(")")
        .update()
        .transact(tx);

    Json fromDb =
        Fragment.of("SELECT lines FROM orders")
            .query(RowCodec.of(DuckDbTypes.json).exactlyOne())
            .transact(tx);

    List<OrderLine> decoded = objectCodec.fromJson(JsonValue.parse(fromDb.value()));
    assertEqual(original, decoded);
  }

  private static void assertEqual(List<OrderLine> expected, List<OrderLine> actual) {
    if (expected.size() != actual.size()) {
      throw new AssertionError(
          "Size mismatch: expected " + expected.size() + " but got " + actual.size());
    }
    for (int i = 0; i < expected.size(); i++) {
      OrderLine e = expected.get(i);
      OrderLine a = actual.get(i);
      if (!e.product().equals(a.product())
          || e.qty() != a.qty()
          || e.price().compareTo(a.price()) != 0) {
        throw new AssertionError("Mismatch at index " + i + ": expected " + e + " but got " + a);
      }
    }
  }
}
