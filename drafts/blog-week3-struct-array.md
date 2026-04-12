# Stop Flattening Your Data

**Subtitle:** Your database can return rows containing arrays of rows. Your library should too.

---

## The Hook

You have three orders. Alice has two, Bob has one. You want each order with its customer info and line items. Maybe a discount code if one was applied. You JOIN everything together:

```
order | customer        | email             | product             | qty | price  | discount
------+-----------------+-------------------+---------------------+-----+--------+---------
1     | Alice Johnson   | alice@example.com | Mechanical Keyboard |   1 | 149.99 | SAVE10
1     | Alice Johnson   | alice@example.com | USB-C Hub           |   2 |  49.99 | SAVE10
1     | Alice Johnson   | alice@example.com | Monitor Arm         |   1 |  89.95 | SAVE10
2     | Alice Johnson   | alice@example.com | Standing Desk       |   1 | 599.00 | NULL
3     | Bob Smith       | bob@example.com   | Webcam              |   3 |  79.99 | NULL
3     | Bob Smith       | bob@example.com   | Mechanical Keyboard |   1 | 149.99 | NULL
```

Six rows for three orders. Alice's name appears four times. Her email four times. The discount code "SAVE10" is duplicated on every line item of order #1. Orders #2 and #3 have no discount, so you get NULL — four times. And this is a simple example. Add shipping address, product categories, order status history — every LEFT JOIN multiplies the columns and sprinkles more NULLs across the grid.

Then in Java you write the deduplication code. Group by order ID. Collect line items into a list. Check if the discount is null or not — but only on the first row per order, because it's the same on every row. Or is it? Better check all of them. Handle the LEFT JOIN nulls. Map the flat rows back into the nested domain objects you actually wanted.

This is not a hard problem. It's a tedious one. And it's entirely unnecessary.

---

## The Reveal

Same tables. Same data. Different query — `ARRAY()` collects the line items into a single column instead of flattening them into rows:

```sql
SELECT o.id, c.name, c.email,
       ARRAY(
           SELECT (ol.product_name, ol.quantity, ol.unit_price)
           FROM order_lines ol WHERE ol.order_id = o.id
       ) AS items,
       d.code AS discount
FROM orders o
JOIN customers c ON c.id = o.customer_id
LEFT JOIN discounts d ON d.order_id = o.id
```

Three rows. Three orders:

```
 id │ name          │ shipping_address                              │ items                                         │ discount
────┼───────────────┼───────────────────────────────────────────────┼───────────────────────────────────────────────┼─────────
  1 │ Alice Johnson │ (742 Evergreen Terrace,Springfield,IL,62704) │ {"(Mechanical Keyboard,1,149.99)",            │ SAVE10
    │               │                                               │  "(USB-C Hub,2,49.99)",                       │
    │               │                                               │  "(Monitor Arm,1,89.95)"}                     │
  2 │ Alice Johnson │ (742 Evergreen Terrace,Springfield,IL,62704) │ {"(Standing Desk,1,599.00)"}                  │ NULL
  3 │ Bob Smith     │ (221B Baker Street,London,UK,NW1 6XE)        │ {"(Webcam,3,79.99)",                          │ NULL
    │               │                                               │  "(Mechanical Keyboard,1,149.99)"}            │
```

That's what PostgreSQL actually returns. The address is one column. The line items are one column. The discount is one value per row — NULL means absent, not "duplicated across three line item rows."

But raw composite text format is ugly. With Foundations JDBC, those values arrive as typed Java/Kotlin objects:

```kotlin
data class OrderWithCustomer(
    val id: Int,
    val customer: String,
    val address: Address,          // one value, not 4 columns
    val items: Array<LineItem>,    // one value, not N rows
    val discount: String?          // nullable — once per order
)
```

```
orders[0] = OrderWithCustomer(
    id = 1,
    customer = "Alice Johnson",
    address = Address("742 Evergreen Terrace", "Springfield", "IL", "62704"),
    items = [
        LineItem("Mechanical Keyboard", 1, 149.99),
        LineItem("USB-C Hub", 2, 49.99),
        LineItem("Monitor Arm", 1, 89.95)
    ],
    discount = "SAVE10"
)

orders[1] = OrderWithCustomer(
    id = 2,
    customer = "Alice Johnson",
    address = Address("742 Evergreen Terrace", "Springfield", "IL", "62704"),
    items = [LineItem("Standing Desk", 1, 599.00)],
    discount = null
)

orders[2] = OrderWithCustomer(
    id = 3,
    customer = "Bob Smith",
    address = Address("221B Baker Street", "London", "UK", "NW1 6XE"),
    items = [
        LineItem("Webcam", 3, 79.99),
        LineItem("Mechanical Keyboard", 1, 149.99)
    ],
    discount = null
)
```

Three objects. Each one self-contained. The address is a typed record. The line items are a typed array. The discount is nullable — `String?` in Kotlin, `Optional<String>` in Java. No deduplication. No grouping. No "check the first row and hope the rest match."

---

## You Don't Need to Change Anything in the Database

No new types. No schema changes. Your tables stay exactly as they are:

```sql
CREATE TABLE orders (id serial PRIMARY KEY, customer_id int REFERENCES customers(id));
CREATE TABLE order_lines (
    id serial PRIMARY KEY,
    order_id int REFERENCES orders(id),
    product_name text NOT NULL,
    quantity int NOT NULL,
    unit_price numeric(10,2) NOT NULL
);
```

`ARRAY()` subquery assembles the tree from your flat tables. The row constructor `(a, b, c)` builds an anonymous record — no `CREATE TYPE` needed:

```sql
SELECT o.id, o.customer,
       ARRAY(
           SELECT (ol.product_name, ol.quantity, ol.unit_price)
           FROM order_lines ol
           WHERE ol.order_id = o.id
       ) AS items
FROM orders o
```

Normal tables. Normal indexes. Normal foreign keys. PostgreSQL returns the anonymous records in its composite text format. Foundations JDBC's `PgRecordParser` parses them directly into your Kotlin data classes — no intermediate step, no JSON, no manual mapping.

---

## How the Java/Kotlin Side Works

The Kotlin type definitions:

```kotlin
// [Snippet: PgDomain types]
data class Address(val street: String, val city: String, val state: String, val zip: String)
data class LineItem(val product: ProductInfo, val quantity: Int, val unitPrice: BigDecimal)

// [Snippet: PgDomain structs]
val addressStruct = PgStructBuilders.builder<Address>("address")
    .field("street", PgTypes.text, Address::street)
    // ...
    .build(::Address)

val lineItemStruct = PgStructBuilders.builder<LineItem>("line_item")
    .nestedField("product", productInfoStruct, LineItem::product)  // struct inside struct
    .field("quantity", PgTypes.int4, LineItem::quantity)
    .field("unit_price", PgTypes.numeric, LineItem::unitPrice)
    .build(::LineItem)
```

Point: the builder is explicit. No annotations. No reflection. The type system tracks every field.

---

## Deep Nesting — The Payoff

This is where it gets interesting. PostgreSQL composite types can contain arrays, and those arrays can contain composites that contain arrays. Arbitrary depth.

```sql
CREATE TYPE skill AS (name text, level int);
CREATE TYPE employee AS (name text, role text, skills skill[]);
CREATE TYPE department AS (name text, members employee[]);
```

One query, two rows:

```sql
SELECT data FROM departments
```

```
departments[0] = Department(
    name = "Engineering",
    members = [
        Employee("Alice", "Lead", skills = [Skill("Java", 9), Skill("PostgreSQL", 8), Skill("Kotlin", 7)]),
        Employee("Bob", "Senior",   skills = [Skill("Python", 8), Skill("Docker", 7)]),
        Employee("Carol", "Junior", skills = [Skill("JavaScript", 6)])
    ]
)

departments[1] = Department(
    name = "Design",
    members = [
        Employee("Dave", "Lead",   skills = [Skill("Figma", 9), Skill("CSS", 8)]),
        Employee("Eve", "Senior",  skills = [Skill("Illustrator", 8), Skill("Figma", 7), Skill("Motion", 6)])
    ]
)
```

Two rows. Five employees. Eleven skills. All in the right places. No flattening, no deduplication, no reconstruction.

The Java code that reads this:

```kotlin
val pgDepartment = PgStructBuilders.builder<Department>("department")
    .field("name", PgTypes.text, Department::name)
    .nestedArrayField("members", pgEmployee, Department::members, Array<Employee>::new)
    .build(::Department)
```

That's it. `nestedArrayField` handles the full recursive parsing of PostgreSQL's composite text format. No JSON. No intermediate deserialization. The data arrives as typed Java records all the way down.

---

## Writing Structured Data

It works both ways. Write a department with its full team tree in a single INSERT:

```kotlin
val dept = Department("Data Science", arrayOf(
    Employee("Frank", "Lead", arrayOf(Skill("Python", 9), Skill("SQL", 8), Skill("Statistics", 7))),
    Employee("Grace", "Senior", arrayOf(Skill("R", 8)))
))

sql { "INSERT INTO departments (data) VALUES (${pgDepartmentType(dept)})" }
    .update().transact(tx)
```

One parameter. One round-trip. The entire nested tree serialized correctly.

---

## Array Parameters — The Everyday Win

Every JDBC developer has written this:

```kotlin
// The horror
val placeholders = ids.joinToString(",") { "?" }
val sql = "SELECT * FROM products WHERE id IN ($placeholders)"
// Then bind each parameter individually in a loop...
```

With typed arrays, it's one parameter:

```kotlin
// [Snippet: ArrayParameters pg-where-any]
fun fetchProducts(ids: Array<Int>): List<String> =
    sql { "SELECT name FROM products WHERE id = ANY(${PgTypes.int4Array(ids)})" }
        .queryAll(RowCodec.of(PgTypes.text))
        .transact(tx)
```

One typed parameter. No string building. No off-by-one. Works with any array type — `int4[]`, `text[]`, `uuid[]`.

Wrap your domain types for extra safety. `transform` derives a new codec from an existing one:

```kotlin
// [Snippet: ArrayParameters tag-type]
@JvmInline
value class Tag(val value: String)

val tagType: PgType<Tag> = PgTypes.text.transform(::Tag, Tag::value)
val tagArrayType: PgType<Array<Tag>> = PgTypes.textArray.transform(
    { strings -> Array(strings.size) { Tag(strings[it]) } },
    { tags -> Array(tags.size) { tags[it].value } }
)
```

Now filter by array overlap — "find products matching any of these tags":

```kotlin
// [Snippet: ArrayParameters pg-array-column]
fun findByTags(tags: Array<Tag>): List<String> =
    sql { "SELECT name FROM products WHERE tags && ${tagArrayType(tags)}" }
        .queryAll(RowCodec.of(PgTypes.text))
        .transact(tx)
```

`&&` is PostgreSQL's array overlap operator. One typed parameter. The `Tag` wrapper ensures you can't accidentally pass a product name where a tag is expected.

---

## UNNEST — Batch Insert from Arrays

Turn parallel arrays into rows with UNNEST — one INSERT for N products:

```kotlin
val names = arrayOf("Laptop Stand", "Cable Organizer", "Desk Pad")
val prices = arrayOf(BigDecimal("39.99"), BigDecimal("12.99"), BigDecimal("24.95"))

sql { "INSERT INTO products (name, price) SELECT * FROM unnest(${PgTypes.textArray(names)}, ${PgTypes.numericArray(prices)})" }
    .update().transact(tx)
```

Three rows inserted. One statement. No loop.

---

## Same Patterns, Different Databases

### DuckDB

DuckDB has inline struct types — no separate `CREATE TYPE` needed (PostgreSQL requires it, Oracle requires it). You define the structure right in the table DDL:

```sql
CREATE TABLE orders (
    id INTEGER PRIMARY KEY,
    customer VARCHAR NOT NULL,
    shipping STRUCT(street VARCHAR, city VARCHAR, state VARCHAR, zip VARCHAR),
    items STRUCT(product_name VARCHAR, quantity INTEGER, unit_price DECIMAL(10,2))[]
);
```

```kotlin
val addressStruct = DuckDbStruct.builder<Address>("address")
    .field("street", DuckDbTypes.varchar, Address::street)
    .field("city", DuckDbTypes.varchar, Address::city)
    // ...
    .build(::Address)
```

Same builder pattern. Same typed records. DuckDB also has MAP and UNION types with full support.

Deep nesting works identically — `STRUCT → STRUCT[] → STRUCT[]`:

```
{name=Engineering, members=[{name=Alice, role=Lead, skills=[{name=Java, level=9}, {name=SQL, level=8}]}, ...]}
```

### Oracle

Oracle uses OBJECT types and NESTED TABLEs — different syntax, same concept:

```sql
CREATE TYPE SKILL_T AS OBJECT (NAME VARCHAR2(50), LEVEL_NUM NUMBER);
CREATE TYPE SKILLS_T AS VARRAY(20) OF SKILL_T;
CREATE TYPE EMPLOYEE_T AS OBJECT (NAME VARCHAR2(100), ROLE_NAME VARCHAR2(50), SKILLS SKILLS_T);
CREATE TYPE EMPLOYEES_T AS TABLE OF EMPLOYEE_T;
```

```kotlin
val oracleSkillType = OracleTypes.compositeOf("BLOG_SKILL_T",
    RowCodec.namedBuilder<OracleSkill>()
        .field("NAME", OracleTypes.varchar2(50), OracleSkill::name)
        .field("LEVEL_NUM", OracleTypes.numberInt, OracleSkill::level)
        .build(::OracleSkill))

val oracleSkillsType = OracleVArray.of("BLOG_SKILLS_T", 20, oracleSkillType)

val oracleEmployeeType = OracleTypes.compositeOf("BLOG_EMPLOYEE_T",
    RowCodec.namedBuilder<OracleEmployee>()
        .field("NAME", OracleTypes.varchar2(100), OracleEmployee::name)
        .field("ROLE_NAME", OracleTypes.varchar2(50), OracleEmployee::role)
        .field("SKILLS", oracleSkillsType, OracleEmployee::skills)  // VARRAY of OBJECT
        .build(::OracleEmployee))

val oracleEmployeesType = OracleNestedTable.of("BLOG_EMPLOYEES_T", oracleEmployeeType)
```

Three levels deep: NESTED TABLE → OBJECT → VARRAY of OBJECT. All typed. All round-trippable.

```
Engineering (2 people)
  Alice (Lead) [Java:9, SQL:8]
  Bob (Senior) [Python:8]
```

---

## The Cross-Database Picture

| What | PostgreSQL | DuckDB | Oracle |
|---|---|---|---|
| Composite type | `CREATE TYPE ... AS (...)` | `STRUCT(...)` | `CREATE TYPE ... AS OBJECT (...)` |
| Array of scalars | `text[]`, `int4[]` | `VARCHAR[]`, `INTEGER[]` | `VARRAY(n) OF ...` |
| Array of composites | `employee[]` | `STRUCT(...)[]` | `TABLE OF object_t` |
| Deep nesting | Composites containing arrays of composites | STRUCT containing STRUCT[] containing STRUCT[] | OBJECT containing VARRAY of OBJECT |
| Builder | `PgTypes.compositeOf()` | `DuckDbStruct.builder()` | `OracleTypes.compositeOf()` |

Same pattern. Same builder API. Different databases, each with their own syntax and capabilities, but the same idea: **define the structure once, read and write it as typed values.**

MariaDB and SQL Server don't support composite types. For those databases, JSON aggregation (covered in [a future post]) is the alternative.

---

## What This Changes

The rectangular grid isn't a law of nature. It's a limitation of JDBC libraries that only understand flat rows. Your database already supports structured data. PostgreSQL has had composite types since version 8.0 (2005). Oracle OBJECT types since 8i (1997). DuckDB STRUCTs since day one.

Every time you write code to deduplicate a JOIN result, or loop over N+1 queries to avoid the rectangle, or serialize to JSON and back — you're working around a problem that doesn't exist. The database can return your data in its natural shape. All you need is a library that speaks the language.

---

**Links:**
- [PostgreSQL type docs](https://foundations.typr.dev/docs/postgresql)
- [DuckDB type docs](https://foundations.typr.dev/docs/duckdb)
- [Oracle type docs](https://foundations.typr.dev/docs/oracle)
- GitHub: https://github.com/typr-dev/foundations-jdbc
