package dev.typr.foundations;

import dev.typr.foundations.connect.DuckDbConfig;
import dev.typr.foundations.connect.OracleConfig;
import dev.typr.foundations.connect.SingleConnectionDataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 * Verification test for the "Stop Flattening Your Data" blog post. Run with: bleep test
 * foundations-jdbc-test -o dev.typr.foundations.StructArrayBlogTest
 */
public class StructArrayBlogTest {

  public static void main(String[] args) {
    new StructArrayBlogTest().test();
  }

  // ======================================================================
  //  SHARED DOMAIN TYPES
  // ======================================================================

  record Address(String street, String city, String state, String zip) {}

  record LineItem(String productName, int quantity, BigDecimal unitPrice) {}

  record Customer(int id, String name, String email, Address shippingAddress) {}

  record Product(int id, String name, BigDecimal price, String[] tags) {}

  record Order(int id, int customerId, LineItem[] items) {}

  // Deep nesting: Department → Employee[] → Skill (struct inside array inside struct)
  record Skill(String name, int level) {}

  record Employee(String name, String role, Skill[] skills) {}

  record Department(String name, Employee[] members) {}

  // ======================================================================
  //  POSTGRESQL TYPES
  // ======================================================================

  static final PgStruct<Address> pgAddress =
      PgStructBuilders.<Address>builder("address")
          .field("street", PgTypes.text, Address::street)
          .field("city", PgTypes.text, Address::city)
          .field("state", PgTypes.text, Address::state)
          .field("zip", PgTypes.text, Address::zip)
          .build(Address::new);

  static final PgType<Address> pgAddressType = pgAddress.asType();

  static final PgStruct<LineItem> pgLineItem =
      PgStructBuilders.<LineItem>builder("line_item")
          .field("product_name", PgTypes.text, LineItem::productName)
          .field("quantity", PgTypes.int4, LineItem::quantity)
          .field("unit_price", PgTypes.numeric, LineItem::unitPrice)
          .build(LineItem::new);

  static final PgType<LineItem> pgLineItemType = pgLineItem.asType();

  static final PgType<LineItem[]> pgLineItemArrayType =
      pgLineItemType.array(
          PgRead.readCompositeArray(pgLineItemType.pgCompositeText(), LineItem[]::new),
          LineItem[]::new);

  // Deep nesting PG types
  static final PgStruct<Skill> pgSkill =
      PgStructBuilders.<Skill>builder("skill")
          .field("name", PgTypes.text, Skill::name)
          .field("level", PgTypes.int4, Skill::level)
          .build(Skill::new);

  static final PgStruct<Employee> pgEmployee =
      PgStructBuilders.<Employee>builder("employee")
          .field("name", PgTypes.text, Employee::name)
          .field("role", PgTypes.text, Employee::role)
          .nestedArrayField("skills", pgSkill, Employee::skills, Skill[]::new)
          .build(Employee::new);

  static final PgStruct<Department> pgDepartment =
      PgStructBuilders.<Department>builder("department")
          .field("name", PgTypes.text, Department::name)
          .nestedArrayField("members", pgEmployee, Department::members, Employee[]::new)
          .build(Department::new);

  static final PgType<Department> pgDepartmentType = pgDepartment.asType();

  static final RowCodecNamed<Customer> pgCustomerCodec =
      RowCodec.<Customer>namedBuilder()
          .field("id", PgTypes.int4, Customer::id)
          .field("name", PgTypes.text, Customer::name)
          .field("email", PgTypes.text, Customer::email)
          .field("shipping_address", pgAddressType, Customer::shippingAddress)
          .build(Customer::new);

  static final RowCodecNamed<Product> pgProductCodec =
      RowCodec.<Product>namedBuilder()
          .field("id", PgTypes.int4, Product::id)
          .field("name", PgTypes.text, Product::name)
          .field("price", PgTypes.numeric, Product::price)
          .field("tags", PgTypes.textArray, Product::tags)
          .build(Product::new);

  static final RowCodecNamed<Order> pgOrderCodec =
      RowCodec.<Order>namedBuilder()
          .field("id", PgTypes.int4, Order::id)
          .field("customer_id", PgTypes.int4, Order::customerId)
          .field("items", pgLineItemArrayType, Order::items)
          .build(Order::new);

  // ======================================================================
  //  DUCKDB TYPES
  // ======================================================================

  static final DuckDbStruct<Address> duckAddress =
      DuckDbStruct.<Address>builder("address")
          .field("street", DuckDbTypes.varchar, Address::street)
          .field("city", DuckDbTypes.varchar, Address::city)
          .field("state", DuckDbTypes.varchar, Address::state)
          .field("zip", DuckDbTypes.varchar, Address::zip)
          .build(Address::new);

  static final DuckDbType<Address> duckAddressType = duckAddress.asType();

  static final DuckDbStruct<LineItem> duckLineItem =
      DuckDbStruct.<LineItem>builder("line_item")
          .field("product_name", DuckDbTypes.varchar, LineItem::productName)
          .field("quantity", DuckDbTypes.integer, LineItem::quantity)
          .field("unit_price", DuckDbTypes.decimal, LineItem::unitPrice)
          .build(LineItem::new);

  static final DuckDbType<LineItem> duckLineItemType = duckLineItem.asType();

  // Deep nesting DuckDB types
  static final DuckDbStruct<Skill> duckSkill =
      DuckDbStruct.<Skill>builder("skill")
          .field("name", DuckDbTypes.varchar, Skill::name)
          .field("level", DuckDbTypes.integer, Skill::level)
          .build(Skill::new);

  static final DuckDbType<Skill> duckSkillType = duckSkill.asType();

  // ======================================================================
  //  ORACLE TYPES
  // ======================================================================

  record OracleAddress(String street, String city) {}

  record OracleLineItem(Long productId, Integer quantity) {}

  static final OracleType<OracleAddress> oracleAddressType =
      OracleObject.<OracleAddress>builder("BLOG_ADDRESS_T")
          .field("STREET", OracleTypes.varchar2(100), OracleAddress::street)
          .field("CITY", OracleTypes.varchar2(50), OracleAddress::city)
          .build(OracleAddress::new)
          .asType();

  static final OracleType<OracleLineItem> oracleLineItemType =
      OracleObject.<OracleLineItem>builder("BLOG_LINE_ITEM_T")
          .field("PRODUCT_ID", OracleTypes.numberLong, OracleLineItem::productId)
          .field("QUANTITY", OracleTypes.numberInt, OracleLineItem::quantity)
          .build(OracleLineItem::new)
          .asType();

  static final OracleType<List<OracleLineItem>> oracleLineItemsType =
      OracleNestedTable.of("BLOG_LINE_ITEMS_T", oracleLineItemType);

  // Deep nesting: NESTED TABLE → OBJECT → VARRAY of OBJECT (3 levels)
  record OracleSkill(String name, Integer level) {}

  record OracleEmployee(String name, String role, List<OracleSkill> skills) {}

  static final OracleType<OracleSkill> oracleSkillType =
      OracleObject.<OracleSkill>builder("BLOG_SKILL_T")
          .field("NAME", OracleTypes.varchar2(50), OracleSkill::name)
          .field("LEVEL_NUM", OracleTypes.numberInt, OracleSkill::level)
          .build(OracleSkill::new)
          .asType();

  static final OracleType<List<OracleSkill>> oracleSkillsType =
      OracleVArray.of("BLOG_SKILLS_T", 20, oracleSkillType);

  static final OracleType<OracleEmployee> oracleEmployeeType =
      OracleObject.<OracleEmployee>builder("BLOG_EMPLOYEE_T")
          .field("NAME", OracleTypes.varchar2(100), OracleEmployee::name)
          .field("ROLE_NAME", OracleTypes.varchar2(50), OracleEmployee::role)
          .field("SKILLS", oracleSkillsType, OracleEmployee::skills)
          .build(OracleEmployee::new)
          .asType();

  static final OracleType<List<OracleEmployee>> oracleEmployeesType =
      OracleNestedTable.of("BLOG_EMPLOYEES_T", oracleEmployeeType);

  // ======================================================================
  //  TEST ENTRY
  // ======================================================================

  @Test
  public void test() {
    section("POSTGRESQL");
    testPostgres();

    section("DUCKDB");
    testDuckDb();

    section("ORACLE");
    testOracle();
  }

  // ======================================================================
  //  POSTGRESQL
  // ======================================================================

  void testPostgres() {
    var tx = Containers.postgresTransactor();

    tx.execute(
        conn -> {
          var stmt = conn.createStatement();
          stmt.execute("DROP TABLE IF EXISTS orders CASCADE");
          stmt.execute("DROP TABLE IF EXISTS products CASCADE");
          stmt.execute("DROP TABLE IF EXISTS customers CASCADE");
          stmt.execute("DROP TABLE IF EXISTS departments CASCADE");
          stmt.execute("DROP TYPE IF EXISTS line_item CASCADE");
          stmt.execute("DROP TYPE IF EXISTS address CASCADE");
          stmt.execute("DROP TYPE IF EXISTS department CASCADE");
          stmt.execute("DROP TYPE IF EXISTS employee CASCADE");
          stmt.execute("DROP TYPE IF EXISTS skill CASCADE");

          stmt.execute(
              """
              CREATE TYPE address AS (
                  street text, city text, state text, zip text
              )""");
          stmt.execute(
              """
              CREATE TYPE line_item AS (
                  product_name text, quantity int, unit_price numeric(10,2)
              )""");
          stmt.execute("CREATE TYPE skill AS (name text, level int)");
          stmt.execute("CREATE TYPE employee AS (name text, role text, skills skill[])");
          stmt.execute("CREATE TYPE department AS (name text, members employee[])");

          stmt.execute(
              """
              CREATE TABLE customers (
                  id serial PRIMARY KEY,
                  name text NOT NULL,
                  email text NOT NULL,
                  shipping_address address NOT NULL
              )""");
          stmt.execute(
              """
              CREATE TABLE products (
                  id serial PRIMARY KEY,
                  name text NOT NULL,
                  price numeric(10,2) NOT NULL,
                  tags text[] NOT NULL DEFAULT '{}'
              )""");
          stmt.execute(
              """
              CREATE TABLE orders (
                  id serial PRIMARY KEY,
                  customer_id int NOT NULL REFERENCES customers(id),
                  items line_item[] NOT NULL
              )""");
          stmt.execute("CREATE TABLE departments (id serial PRIMARY KEY, data department NOT NULL)");

          // Sample data
          stmt.execute(
              """
              INSERT INTO customers (name, email, shipping_address) VALUES
                  ('Alice Johnson', 'alice@example.com',
                   ROW('742 Evergreen Terrace', 'Springfield', 'IL', '62704')::address),
                  ('Bob Smith', 'bob@example.com',
                   ROW('221B Baker Street', 'London', 'UK', 'NW1 6XE')::address)
              """);
          stmt.execute(
              """
              INSERT INTO products (name, price, tags) VALUES
                  ('Mechanical Keyboard', 149.99, ARRAY['electronics', 'peripherals', 'gaming']),
                  ('Standing Desk', 599.00, ARRAY['furniture', 'ergonomic', 'office']),
                  ('USB-C Hub', 49.99, ARRAY['electronics', 'peripherals', 'accessories']),
                  ('Monitor Arm', 89.95, ARRAY['furniture', 'ergonomic', 'accessories']),
                  ('Webcam', 79.99, ARRAY['electronics', 'peripherals'])
              """);
          stmt.execute(
              """
              INSERT INTO orders (customer_id, items) VALUES
                  (1, ARRAY[
                      ROW('Mechanical Keyboard', 1, 149.99)::line_item,
                      ROW('USB-C Hub', 2, 49.99)::line_item,
                      ROW('Monitor Arm', 1, 89.95)::line_item
                  ]),
                  (1, ARRAY[ROW('Standing Desk', 1, 599.00)::line_item]),
                  (2, ARRAY[
                      ROW('Webcam', 3, 79.99)::line_item,
                      ROW('Mechanical Keyboard', 1, 149.99)::line_item
                  ])
              """);

          // Deep nesting data
          stmt.execute(
              """
              INSERT INTO departments (data) VALUES
                  (ROW('Engineering', ARRAY[
                      ROW('Alice', 'Lead', ARRAY[ROW('Java', 9)::skill, ROW('PostgreSQL', 8)::skill, ROW('Kotlin', 7)::skill])::employee,
                      ROW('Bob', 'Senior', ARRAY[ROW('Python', 8)::skill, ROW('Docker', 7)::skill])::employee,
                      ROW('Carol', 'Junior', ARRAY[ROW('JavaScript', 6)::skill])::employee
                  ])::department),
                  (ROW('Design', ARRAY[
                      ROW('Dave', 'Lead', ARRAY[ROW('Figma', 9)::skill, ROW('CSS', 8)::skill])::employee,
                      ROW('Eve', 'Senior', ARRAY[ROW('Illustrator', 8)::skill, ROW('Figma', 7)::skill, ROW('Motion', 6)::skill])::employee
                  ])::department)
              """);

          conn.commit();
          return null;
        });

    // 1. The JOIN contrast
    subsection("The JOIN contrast: flattened vs structured");
    tx.execute(
        conn -> {
          System.out.println("Flattened (6 rows, customer duplicated on every line):");
          var rs =
              conn.createStatement()
                  .executeQuery(
                      """
                      SELECT o.id AS order_id, c.name, c.email,
                             (li).product_name, (li).quantity, (li).unit_price
                      FROM orders o
                      JOIN customers c ON c.id = o.customer_id
                      CROSS JOIN LATERAL unnest(o.items) AS li
                      ORDER BY o.id""");
          System.out.println(
              "  order | customer        | email             | product             | qty | price");
          System.out.println(
              "  ------+-----------------+-------------------+---------------------+-----+------");
          while (rs.next()) {
            System.out.printf(
                "  %-5d | %-15s | %-17s | %-19s | %3d | %s%n",
                rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getString(4), rs.getInt(5), rs.getBigDecimal(6));
          }

          record OrderWithCustomer(
              int orderId, String customer, Address address, LineItem[] items) {}

          var codec =
              RowCodec.<OrderWithCustomer>namedBuilder()
                  .field("id", PgTypes.int4, OrderWithCustomer::orderId)
                  .field("name", PgTypes.text, OrderWithCustomer::customer)
                  .field("shipping_address", pgAddressType, OrderWithCustomer::address)
                  .field("items", pgLineItemArrayType, OrderWithCustomer::items)
                  .build(OrderWithCustomer::new);

          System.out.println("\nStructured (3 rows, no duplication):");
          List<OrderWithCustomer> structured =
              Fragment.of(
                      """
                      SELECT o.id, c.name, c.shipping_address, o.items
                      FROM orders o JOIN customers c ON c.id = o.customer_id
                      ORDER BY o.id""")
                  .query(codec.all())
                  .run(conn);

          for (var o : structured) {
            System.out.println("  Order #" + o.orderId() + " — " + o.customer());
            var a = o.address();
            System.out.println("    Ship to: " + a.street() + ", " + a.city() + " " + a.state());
            for (var li : o.items()) {
              System.out.println(
                  "    " + li.quantity() + "x " + li.productName() + " @ $" + li.unitPrice());
            }
          }
          return null;
        });

    // 2. Array parameters
    subsection("Array parameters: WHERE id = ANY(?)");
    tx.execute(
        conn -> {
          var ids = new Integer[] {1, 3, 5};
          var products =
              Fragment.of("SELECT id, name, price, tags FROM products WHERE id = ANY(")
                  .value(PgTypes.int4Array, ids)
                  .append(")")
                  .query(pgProductCodec.all())
                  .run(conn);
          for (var p : products)
            System.out.println(
                "  " + p.name() + " ($" + p.price() + ") " + Arrays.toString(p.tags()));
          return null;
        });

    // 3. Deep nesting: struct → array of structs → array of structs
    subsection("Deep nesting: Department → Employee[] → Skill[]");
    tx.execute(
        conn -> {
          record DeptRow(int id, Department data) {}

          var codec =
              RowCodec.<DeptRow>namedBuilder()
                  .field("id", PgTypes.int4, DeptRow::id)
                  .field("data", pgDepartmentType, DeptRow::data)
                  .build(DeptRow::new);

          var departments =
              Fragment.of("SELECT id, data FROM departments ORDER BY id")
                  .query(codec.all())
                  .run(conn);

          for (var row : departments) {
            var dept = row.data();
            System.out.println("  " + dept.name() + " (" + dept.members().length + " people)");
            for (var emp : dept.members()) {
              var skillStr =
                  Arrays.stream(emp.skills())
                      .map(s -> s.name() + ":" + s.level())
                      .reduce((a, b) -> a + ", " + b)
                      .orElse("");
              System.out.println("    " + emp.name() + " (" + emp.role() + ") [" + skillStr + "]");
            }
          }
          System.out.println("\n  2 departments, 2 rows. Each row carries the full team tree.");
          return null;
        });

    // 4. Writing deep nested structs
    subsection("Writing deep nested structs");
    tx.execute(
        conn -> {
          var dept =
              new Department(
                  "Data Science",
                  new Employee[] {
                    new Employee(
                        "Frank",
                        "Lead",
                        new Skill[] {
                          new Skill("Python", 9),
                          new Skill("SQL", 8),
                          new Skill("Statistics", 7)
                        }),
                    new Employee("Grace", "Senior", new Skill[] {new Skill("R", 8)})
                  });

          Fragment.of("INSERT INTO departments (data) VALUES (")
              .value(pgDepartmentType, dept)
              .append(")")
              .update()
              .run(conn);

          // Read it back
          var readBack =
              Fragment.of("SELECT data FROM departments WHERE (data).name = ")
                  .value(PgTypes.text, "Data Science")
                  .query(RowCodec.of(pgDepartmentType).all())
                  .run(conn)
                  .getFirst();

          System.out.println("  Wrote and read back: " + readBack.name());
          for (var emp : readBack.members()) {
            System.out.println(
                "    "
                    + emp.name()
                    + " — "
                    + emp.skills().length
                    + " skill(s): "
                    + Arrays.stream(emp.skills())
                        .map(Skill::name)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse(""));
          }
          return null;
        });

    // 5. UNNEST batch insert
    subsection("UNNEST: batch insert from parallel arrays");
    tx.execute(
        conn -> {
          var names = new String[] {"Laptop Stand", "Cable Organizer", "Desk Pad"};
          var prices =
              new BigDecimal[] {
                new BigDecimal("39.99"), new BigDecimal("12.99"), new BigDecimal("24.95")
              };
          int n =
              Fragment.of("INSERT INTO products (name, price) SELECT * FROM unnest(")
                  .value(PgTypes.textArray, names)
                  .append(", ")
                  .value(PgTypes.numericArray, prices)
                  .append(")")
                  .update()
                  .run(conn);
          System.out.println("  Inserted " + n + " products via UNNEST");
          return null;
        });

    // 6. Ad-hoc structured queries on NORMAL TABLES (the main win)
    subsection("Ad-hoc structured result from normalized tables");
    tx.execute(
        conn -> {
          var stmt = conn.createStatement();
          // Normal tables — no composite types in storage
          stmt.execute("DROP TABLE IF EXISTS norm_order_lines CASCADE");
          stmt.execute("DROP TABLE IF EXISTS norm_orders CASCADE");
          stmt.execute(
              "CREATE TABLE norm_orders (id serial PRIMARY KEY, customer text NOT NULL)");
          stmt.execute(
              """
              CREATE TABLE norm_order_lines (
                  id serial PRIMARY KEY,
                  order_id int REFERENCES norm_orders(id),
                  product_name text NOT NULL,
                  quantity int NOT NULL,
                  unit_price numeric(10,2) NOT NULL
              )""");
          stmt.execute(
              "INSERT INTO norm_orders (customer) VALUES ('Alice'), ('Bob')");
          stmt.execute(
              """
              INSERT INTO norm_order_lines (order_id, product_name, quantity, unit_price) VALUES
                  (1, 'Keyboard', 1, 149.99),
                  (1, 'USB Hub', 2, 49.99),
                  (1, 'Monitor Arm', 1, 89.95),
                  (2, 'Desk', 1, 599.00)
              """);
          conn.commit();

          // No CREATE TYPE needed — anonymous records work directly
          record NormOrder(int id, String customer, LineItem[] items) {}

          var codec =
              RowCodec.<NormOrder>namedBuilder()
                  .field("id", PgTypes.int4, NormOrder::id)
                  .field("customer", PgTypes.text, NormOrder::customer)
                  .field("items", pgLineItemArrayType, NormOrder::items)
                  .build(NormOrder::new);

          // ARRAY() with row constructor — picks the columns, returns anonymous records
          var orders =
              Fragment.of(
                      """
                      SELECT o.id, o.customer,
                             ARRAY(
                                 SELECT (ol.product_name, ol.quantity, ol.unit_price)
                                 FROM norm_order_lines ol
                                 WHERE ol.order_id = o.id
                             ) AS items
                      FROM norm_orders o
                      ORDER BY o.id""")
                  .query(codec.all())
                  .run(conn);

          System.out.println("  Normal tables. No CREATE TYPE. Structured result via ARRAY():");
          for (var o : orders) {
            System.out.println("  Order #" + o.id() + " — " + o.customer());
            for (var li : o.items()) {
              System.out.println(
                  "    " + li.quantity() + "x " + li.productName() + " @ $" + li.unitPrice());
            }
          }
          System.out.println(
              "\n  No schema changes. Your tables stay normalized. Your queries return trees.");
          return null;
        });
  }

  // ======================================================================
  //  DUCKDB
  // ======================================================================

  void testDuckDb() {
    var tx = SingleConnectionDataSource.create(DuckDbConfig.inMemory().build()).transactor();

    tx.execute(
        conn -> {
          var stmt = conn.createStatement();
          stmt.execute(
              """
              CREATE TABLE orders (
                  id INTEGER PRIMARY KEY,
                  customer VARCHAR NOT NULL,
                  shipping STRUCT(street VARCHAR, city VARCHAR, state VARCHAR, zip VARCHAR),
                  items STRUCT(product_name VARCHAR, quantity INTEGER, unit_price DECIMAL(10,2))[]
              )""");
          stmt.execute(
              """
              CREATE TABLE departments (
                  id INTEGER PRIMARY KEY,
                  data STRUCT(
                      name VARCHAR,
                      members STRUCT(
                          name VARCHAR,
                          role VARCHAR,
                          skills STRUCT(name VARCHAR, level INTEGER)[]
                      )[]
                  )
              )""");

          stmt.execute(
              """
              INSERT INTO orders VALUES
                  (1, 'Alice',
                   {'street': '742 Evergreen Terrace', 'city': 'Springfield', 'state': 'IL', 'zip': '62704'},
                   [{'product_name': 'Keyboard', 'quantity': 1, 'unit_price': 149.99},
                    {'product_name': 'USB-C Hub', 'quantity': 2, 'unit_price': 49.99}]),
                  (2, 'Bob',
                   {'street': '221B Baker St', 'city': 'London', 'state': 'UK', 'zip': 'NW1'},
                   [{'product_name': 'Desk', 'quantity': 1, 'unit_price': 599.00}])
              """);
          stmt.execute(
              """
              INSERT INTO departments VALUES
                  (1, {'name': 'Engineering', 'members': [
                      {'name': 'Alice', 'role': 'Lead', 'skills': [{'name': 'Java', 'level': 9}, {'name': 'SQL', 'level': 8}]},
                      {'name': 'Bob', 'role': 'Senior', 'skills': [{'name': 'Python', 'level': 8}]}
                  ]}),
                  (2, {'name': 'Design', 'members': [
                      {'name': 'Carol', 'role': 'Lead', 'skills': [{'name': 'Figma', 'level': 9}, {'name': 'CSS', 'level': 8}]}
                  ]})
              """);
          return null;
        });

    // 1. Typed STRUCT reads
    subsection("DuckDB: orders with typed struct reads");
    tx.execute(
        conn -> {
          var rs = conn.createStatement().executeQuery("SELECT id, customer, shipping, items FROM orders ORDER BY id");
          while (rs.next()) {
            var addr = duckAddressType.read().read(rs, 3);
            System.out.println("  Order #" + rs.getInt(1) + " — " + rs.getString(2));
            System.out.println("    Ship to: " + addr.street() + ", " + addr.city() + " " + addr.state());
            // items as typed array
            var itemsRaw = rs.getString(4);
            System.out.println("    Items: " + itemsRaw);
          }
          return null;
        });

    // 2. Deep nesting: STRUCT → STRUCT[] → STRUCT[]
    subsection("DuckDB: deep nesting STRUCT → STRUCT[] → STRUCT[]");
    tx.execute(
        conn -> {
          var rs = conn.createStatement().executeQuery("SELECT data FROM departments ORDER BY id");
          while (rs.next()) {
            // Read raw to show the full structure
            var raw = rs.getString(1);
            System.out.println("  " + raw);
          }
          return null;
        });
  }

  // ======================================================================
  //  ORACLE
  // ======================================================================

  void testOracle() {
    var pool = Containers.oraclePool();
    var tx = pool.transactor(Transactor.testStrategy());

    // Setup types and table
    tx.execute(
        conn -> {
          var stmt = conn.createStatement();
          // Clean up (reverse dependency order)
          tryExec(stmt, "DROP TABLE blog_departments CASCADE CONSTRAINTS");
          tryExec(stmt, "DROP TYPE BLOG_EMPLOYEES_T FORCE");
          tryExec(stmt, "DROP TYPE BLOG_EMPLOYEE_T FORCE");
          tryExec(stmt, "DROP TYPE BLOG_SKILLS_T FORCE");
          tryExec(stmt, "DROP TYPE BLOG_SKILL_T FORCE");
          tryExec(stmt, "DROP TABLE blog_orders CASCADE CONSTRAINTS");
          tryExec(stmt, "DROP TYPE BLOG_LINE_ITEMS_T FORCE");
          tryExec(stmt, "DROP TYPE BLOG_LINE_ITEM_T FORCE");
          tryExec(stmt, "DROP TABLE blog_customers CASCADE CONSTRAINTS");
          tryExec(stmt, "DROP TYPE BLOG_ADDRESS_T FORCE");

          stmt.execute(
              """
              CREATE TYPE BLOG_ADDRESS_T AS OBJECT (
                  STREET VARCHAR2(100),
                  CITY VARCHAR2(50)
              )""");
          stmt.execute(
              """
              CREATE TYPE BLOG_LINE_ITEM_T AS OBJECT (
                  PRODUCT_ID NUMBER,
                  QUANTITY NUMBER
              )""");
          stmt.execute("CREATE TYPE BLOG_LINE_ITEMS_T AS TABLE OF BLOG_LINE_ITEM_T");
          stmt.execute(
              """
              CREATE TABLE blog_customers (
                  id NUMBER PRIMARY KEY,
                  name VARCHAR2(100) NOT NULL,
                  address BLOG_ADDRESS_T
              )""");
          stmt.execute(
              """
              CREATE TABLE blog_orders (
                  id NUMBER PRIMARY KEY,
                  customer_id NUMBER NOT NULL,
                  items BLOG_LINE_ITEMS_T
              ) NESTED TABLE items STORE AS blog_order_items_store""");

          // Deep nesting: NESTED TABLE → OBJECT → VARRAY of OBJECT
          stmt.execute(
              """
              CREATE TYPE BLOG_SKILL_T AS OBJECT (
                  NAME VARCHAR2(50),
                  LEVEL_NUM NUMBER
              )""");
          stmt.execute("CREATE TYPE BLOG_SKILLS_T AS VARRAY(20) OF BLOG_SKILL_T");
          stmt.execute(
              """
              CREATE TYPE BLOG_EMPLOYEE_T AS OBJECT (
                  NAME VARCHAR2(100),
                  ROLE_NAME VARCHAR2(50),
                  SKILLS BLOG_SKILLS_T
              )""");
          stmt.execute("CREATE TYPE BLOG_EMPLOYEES_T AS TABLE OF BLOG_EMPLOYEE_T");
          stmt.execute(
              """
              CREATE TABLE blog_departments (
                  id NUMBER PRIMARY KEY,
                  name VARCHAR2(100) NOT NULL,
                  members BLOG_EMPLOYEES_T
              ) NESTED TABLE members STORE AS blog_dept_members_store""");

          conn.commit();
          return null;
        });

    // Insert data — need raw connection for Oracle STRUCT creation
    pool.transactor(Transactor.testStrategy()).execute(
        conn -> {
          var raw = conn.unwrap(oracle.jdbc.OracleConnection.class);
          // Insert customer with OBJECT type address
          var ps1 = raw.prepareStatement("INSERT INTO blog_customers VALUES (1, 'Alice', ?)");
          oracleAddressType.write().set(ps1, 1, new OracleAddress("742 Evergreen Terrace", "Springfield"));
          ps1.executeUpdate();

          // Insert order with NESTED TABLE of line items
          var ps2 = raw.prepareStatement("INSERT INTO blog_orders VALUES (1, 1, ?)");
          oracleLineItemsType.write().set(ps2, 1,
              List.of(new OracleLineItem(101L, 2), new OracleLineItem(202L, 5)));
          ps2.executeUpdate();

          conn.commit();
          return null;
        });

    // Read back
    subsection("Oracle: OBJECT type (nested struct)");
    tx.execute(
        conn -> {
          var rs =
              conn.createStatement()
                  .executeQuery("SELECT id, name, address FROM blog_customers");
          while (rs.next()) {
            String name = rs.getString(2);
            var addr = oracleAddressType.read().read(rs, 3);
            System.out.println("  " + name + ": " + addr.street() + ", " + addr.city());
          }
          return null;
        });

    subsection("Oracle: NESTED TABLE (array of objects)");
    tx.execute(
        conn -> {
          var rs =
              conn.createStatement()
                  .executeQuery("SELECT id, customer_id, items FROM blog_orders");
          while (rs.next()) {
            long id = rs.getLong(1);
            long custId = rs.getLong(2);
            var items = oracleLineItemsType.read().read(rs, 3);
            System.out.println("  Order #" + id + " (customer " + custId + "):");
            for (var li : items) {
              System.out.println("    product=" + li.productId() + " qty=" + li.quantity());
            }
          }
          return null;
        });

    // Deep nesting: NESTED TABLE → OBJECT → VARRAY of OBJECT
    subsection("Oracle: deep nesting — NESTED TABLE → OBJECT → VARRAY of OBJECT");
    pool.transactor(Transactor.testStrategy()).execute(
        conn -> {
          var raw = conn.unwrap(oracle.jdbc.OracleConnection.class);

          var engineering =
              List.of(
                  new OracleEmployee(
                      "Alice",
                      "Lead",
                      List.of(new OracleSkill("Java", 9), new OracleSkill("SQL", 8))),
                  new OracleEmployee(
                      "Bob", "Senior", List.of(new OracleSkill("Python", 8))));

          var ps =
              raw.prepareStatement(
                  "INSERT INTO blog_departments VALUES (1, 'Engineering', ?)");
          oracleEmployeesType.write().set(ps, 1, engineering);
          ps.executeUpdate();

          conn.commit();
          return null;
        });

    tx.execute(
        conn -> {
          var rs =
              conn.createStatement()
                  .executeQuery("SELECT id, name, members FROM blog_departments");
          while (rs.next()) {
            String name = rs.getString(2);
            var members = oracleEmployeesType.read().read(rs, 3);
            System.out.println("  " + name + " (" + members.size() + " people)");
            for (var emp : members) {
              var skillStr =
                  emp.skills().stream()
                      .map(s -> s.name() + ":" + s.level())
                      .reduce((a, b) -> a + ", " + b)
                      .orElse("");
              System.out.println(
                  "    " + emp.name() + " (" + emp.role() + ") [" + skillStr + "]");
            }
          }
          System.out.println(
              "\n  3 levels deep: NESTED TABLE → OBJECT → VARRAY of OBJECT");
          return null;
        });
  }

  // ======================================================================
  //  HELPERS
  // ======================================================================

  static void tryExec(Statement stmt, String sql) {
    try {
      stmt.execute(sql);
    } catch (SQLException ignored) {
    }
  }

  static void section(String name) {
    System.out.println("\n" + "=".repeat(70));
    System.out.println("  " + name);
    System.out.println("=".repeat(70));
  }

  static void subsection(String name) {
    System.out.println("\n--- " + name + " ---\n");
  }
}
