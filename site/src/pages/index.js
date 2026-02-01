import React from 'react';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import CodeBlock from '@theme/CodeBlock';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import styles from './index.module.css';

const typeGrid = [
  {
    db: 'PostgreSQL',
    link: '/docs/postgresql',
    types: [
      'int4[]', 'text[]', 'uuid[]',
      'int4range', 'tstzrange',
      'jsonb', 'hstore',
      'point', 'polygon', 'circle',
      'inet', 'cidr', 'macaddr',
      'tsvector', 'tsquery',
      'record', 'money',
      'enums', 'domains',
    ],
  },
  {
    db: 'DuckDB',
    link: '/docs/duckdb',
    types: [
      'LIST', 'MAP', 'STRUCT',
      'UNION', 'ENUM',
      'HUGEINT', 'UHUGEINT',
      'UTINYINT', 'UINTEGER',
      'BITSTRING', 'INTERVAL',
      'TIMESTAMP_NS',
      'JSON', 'UUID',
      'BLOB', 'DECIMAL',
    ],
  },
  {
    db: 'Oracle',
    link: '/docs/oracle',
    types: [
      'OBJECT', 'NESTED TABLE',
      'VARRAY', 'XMLTYPE',
      'INTERVAL YM', 'INTERVAL DS',
      'NUMBER(p,s)', 'BINARY_FLOAT',
      'CLOB', 'NCLOB', 'BLOB',
      'RAW', 'ROWID',
      'BOOLEAN', 'JSON',
      'TIMESTAMP WITH TZ',
    ],
  },
  {
    db: 'MariaDB',
    link: '/docs/mariadb',
    types: [
      'SET', 'ENUM', 'JSON',
      'UNSIGNED INT', 'UNSIGNED BIGINT',
      'GEOMETRY', 'POINT',
      'POLYGON', 'LINESTRING',
      'INET4', 'INET6',
      'YEAR', 'BIT(n)',
      'MEDIUMTEXT', 'LONGBLOB',
    ],
  },
  {
    db: 'SQL Server',
    link: '/docs/sqlserver',
    types: [
      'GEOGRAPHY', 'GEOMETRY',
      'HIERARCHYID',
      'DATETIMEOFFSET',
      'UNIQUEIDENTIFIER',
      'SQL_VARIANT', 'VECTOR',
      'ROWVERSION', 'XML',
      'MONEY', 'NVARCHAR(MAX)',
      'VARBINARY(MAX)',
    ],
  },
  {
    db: 'DB2',
    link: '/docs/db2',
    types: [
      'DECFLOAT', 'DECIMAL(p,s)',
      'GRAPHIC', 'VARGRAPHIC',
      'DBCLOB', 'CLOB', 'BLOB',
      'XML', 'ROWID',
      'BOOLEAN', 'BINARY',
      'TIMESTAMP(p)',
    ],
  },
];

const schemaSql = `CREATE TYPE dimensions AS (
    width   double precision,
    height  double precision,
    depth   double precision,
    unit    varchar(10)
);

CREATE TABLE product (
    id          serial PRIMARY KEY,
    name        text NOT NULL,
    price       numeric(10,2) NOT NULL,
    tags        text[],
    dimensions  dimensions,
    metadata    jsonb,
    created_at  timestamptz DEFAULT now()
);`;

const rowCode = {
  java: `record ProductRow(
  ProductId id,
  String name,
  BigDecimal price,
  Optional<String[]> tags,            // text[]
  Optional<Dimensions> dimensions,    // composite type
  Optional<Jsonb> metadata,           // jsonb
  Optional<OffsetDateTime> createdAt  // timestamptz
) {}`,
  kotlin: `data class ProductRow(
  val id: ProductId,
  val name: String,
  val price: BigDecimal,
  val tags: Array<String>?,            // text[]
  val dimensions: Dimensions?,         // composite type
  val metadata: Jsonb?,                // jsonb
  val createdAt: OffsetDateTime?       // timestamptz
)`,
  scala: `case class ProductRow(
  id: ProductId,
  name: String,
  price: BigDecimal,
  tags: Option[Array[String]],            // text[]
  dimensions: Option[Dimensions],         // composite type
  metadata: Option[Jsonb],                // jsonb
  createdAt: Option[OffsetDateTime]       // timestamptz
)`,
};

const compositeCode = {
  java: `// The composite type becomes a record with its own PgStruct codec
record Dimensions(
  Double width, Double height, Double depth, String unit
) {}

// PgStruct handles PostgreSQL's composite wire format
static PgStruct<Dimensions> pgStruct = PgStruct.<Dimensions>builder("dimensions")
    .doubleField("width", PgTypes.float8, Dimensions::width)
    .doubleField("height", PgTypes.float8, Dimensions::height)
    .doubleField("depth", PgTypes.float8, Dimensions::depth)
    .stringField("unit", PgTypes.varchar, Dimensions::unit)
    .build(arr -> new Dimensions(
        (Double) arr[0], (Double) arr[1],
        (Double) arr[2], (String) arr[3]));

// Use as a PgType anywhere — row parsers, arrays, JSON
static PgType<Dimensions> pgType = pgStruct.asType();`,
  kotlin: `// The composite type becomes a data class with its own PgStruct codec
data class Dimensions(
  val width: Double, val height: Double,
  val depth: Double, val unit: String
)

// PgStruct handles PostgreSQL's composite wire format
val pgStruct: PgStruct<Dimensions> = PgStruct.builder<Dimensions>("dimensions")
    .doubleField("width", PgTypes.float8, Dimensions::width)
    .doubleField("height", PgTypes.float8, Dimensions::height)
    .doubleField("depth", PgTypes.float8, Dimensions::depth)
    .stringField("unit", PgTypes.varchar, Dimensions::unit)
    .build { arr -> Dimensions(
        arr[0] as Double, arr[1] as Double,
        arr[2] as Double, arr[3] as String) }

// Use as a PgType anywhere — row parsers, arrays, JSON
val pgType: PgType<Dimensions> = pgStruct.asType()`,
  scala: `// The composite type becomes a case class with its own PgStruct codec
case class Dimensions(
  width: Double, height: Double,
  depth: Double, unit: String
)

// PgStruct handles PostgreSQL's composite wire format
val pgStruct: PgStruct[Dimensions] = PgStruct.builder[Dimensions]("dimensions")
    .doubleField("width", PgTypes.float8, _.width)
    .doubleField("height", PgTypes.float8, _.height)
    .doubleField("depth", PgTypes.float8, _.depth)
    .stringField("unit", PgTypes.varchar, _.unit)
    .build(arr => Dimensions(
        arr(0).asInstanceOf[Double], arr(1).asInstanceOf[Double],
        arr(2).asInstanceOf[Double], arr(3).asInstanceOf[String]))

// Use as a PgType anywhere — row parsers, arrays, JSON
val pgType: PgType[Dimensions] = pgStruct.asType()`,
};

const queryCode = {
  java: `// Build small reusable filters
Fragment byName(String name) {
    return Fragment.of("name ILIKE ?").param(PgTypes.text, name);
}
Fragment cheaperThan(BigDecimal max) {
    return Fragment.of("price < ?").param(PgTypes.numeric, max);
}
Fragment amongTags(String tag) {
    return Fragment.of("tags @> ?").param(PgTypes.textArray, new String[]{tag});
}
Fragment createdAfter(OffsetDateTime date) {
    return Fragment.of("created_at > ?").param(PgTypes.timestamptz, date);
}

// Compose them dynamically — only include the filters that are present
List<Fragment> filters = Stream.of(
        Optional.of(byName("%widget%")),
        maxPrice.map(this::cheaperThan),
        tag.map(this::amongTags),
        since.map(this::createdAfter)
    )
    .flatMap(Optional::stream)
    .toList();

List<ProductRow> products = Fragment.of("SELECT * FROM product ")
    .append(Fragment.whereAnd(filters))
    .query(ProductRow.rowParser.list())
    .run(tx);`,
  kotlin: `// Build small reusable filters
fun byName(name: String) =
    Fragment.of("name ILIKE ?").param(PgTypes.text, name)

fun cheaperThan(max: BigDecimal) =
    Fragment.of("price < ?").param(PgTypes.numeric, max)

fun amongTags(tag: String) =
    Fragment.of("tags @> ?").param(PgTypes.textArray, arrayOf(tag))

fun createdAfter(date: OffsetDateTime) =
    Fragment.of("created_at > ?").param(PgTypes.timestamptz, date)

// Compose them dynamically — only include the filters that are present
val filters = listOfNotNull(
    byName("%widget%"),
    maxPrice?.let { cheaperThan(it) },
    tag?.let { amongTags(it) },
    since?.let { createdAfter(it) }
)

val products: List<ProductRow> = Fragment.of("SELECT * FROM product ")
    .append(Fragment.whereAnd(filters))
    .query(ProductRow.rowParser.list())
    .run(tx)`,
  scala: `import dev.typr.foundations.scala.FragmentInterpolator.sql

// Build small reusable filters
def byName(name: String) =
    sql"name ILIKE \${PgTypes.text.encode(name)}"

def cheaperThan(max: BigDecimal) =
    sql"price < \${PgTypes.numeric.encode(max)}"

def amongTags(tag: String) =
    sql"tags @> \${PgTypes.textArray.encode(Array(tag))}"

def createdAfter(date: OffsetDateTime) =
    sql"created_at > \${PgTypes.timestamptz.encode(date)}"

// Compose them dynamically — only include the filters that are present
val filters: List[Fragment] = List(
    Some(byName("%widget%")),
    maxPrice.map(cheaperThan),
    tag.map(amongTags),
    since.map(createdAfter)
).flatten

val products: List[ProductRow] =
    sql"SELECT * FROM product \${Fragment.whereAnd(filters)}"
        .query(ProductRow.rowParser.list())
        .run(tx)`,
};

const parserCode = {
  java: `// The RowParser defines how to read and write every column
static RowParser<ProductRow> rowParser = RowParsers.of(
    ProductId.pgType,              // id
    PgTypes.text,                  // name
    PgTypes.numeric,               // price
    PgTypes.textArray.opt(),       // tags: text[]
    Dimensions.pgType.opt(),       // dimensions: composite
    PgTypes.jsonb.opt(),           // metadata: jsonb
    PgTypes.timestamptz.opt(),     // created_at: timestamptz
    ProductRow::new,
    p -> new Object[]{p.id(), p.name(), p.price(), p.tags(),
                      p.dimensions(), p.metadata(), p.createdAt()}
);

// Compose parsers for joins
RowParser<And<ProductRow, Optional<CategoryRow>>> joined =
    ProductRow.rowParser.leftJoined(CategoryRow.rowParser);`,
  kotlin: `// The RowParser defines how to read and write every column
val rowParser: RowParser<ProductRow> = RowParsers.of(
    ProductId.pgType,              // id
    PgTypes.text,                  // name
    PgTypes.numeric,               // price
    PgTypes.textArray.opt(),       // tags: text[]
    Dimensions.pgType.opt(),       // dimensions: composite
    PgTypes.jsonb.opt(),           // metadata: jsonb
    PgTypes.timestamptz.opt(),     // created_at: timestamptz
    ::ProductRow,
    { p -> arrayOf(p.id, p.name, p.price, p.tags,
                   p.dimensions, p.metadata, p.createdAt) }
)

// Compose parsers for joins
val joined: RowParser<And<ProductRow, ProductRow?>> =
    ProductRow.rowParser.leftJoined(CategoryRow.rowParser)`,
  scala: `// The RowParser defines how to read and write every column
val rowParser: RowParser[ProductRow] = RowParsers.of(
    ProductId.pgType,              // id
    PgTypes.text,                  // name
    PgTypes.numeric,               // price
    PgTypes.textArray.opt(),       // tags: text[]
    Dimensions.pgType.opt(),       // dimensions: composite
    PgTypes.jsonb.opt(),           // metadata: jsonb
    PgTypes.timestamptz.opt(),     // created_at: timestamptz
)(ProductRow.apply)(
    p => Array(p.id, p.name, p.price, p.tags,
               p.dimensions, p.metadata, p.createdAt)
)

// Compose parsers for joins
val joined: RowParser[And[ProductRow, Option[CategoryRow]]] =
    ProductRow.rowParser.leftJoined(CategoryRow.rowParser)`,
};

const transactorCode = {
  java: `// The Transactor manages connections and transactions
// You choose the strategy — it handles the lifecycle
var tx = connectionSource.transactor(Transactor.defaultStrategy());

// Everything inside runs in one transaction: begin, commit, close
List<ProductRow> products = tx.execute(conn ->
    Fragment.of("SELECT * FROM product WHERE price > ?")
        .param(PgTypes.numeric, minPrice)
        .query(ProductRow.rowParser.list())
        .runUnchecked(conn)
);

// Built-in strategies for common patterns
Transactor.defaultStrategy()         // begin → commit → close
Transactor.autoCommitStrategy()      // no transaction, just close
Transactor.rollbackOnErrorStrategy() // begin → commit, rollback on error → close
Transactor.testStrategy()            // begin → rollback → close (for tests)

// Or define your own with explicit hooks
new Transactor.Strategy(
    conn -> conn.setAutoCommit(false),  // before
    Connection::commit,                  // after (success)
    throwable -> { /* handle error */ }, // oops
    Connection::close                    // always (finally)
);`,
  kotlin: `// The Transactor manages connections and transactions
// You choose the strategy — it handles the lifecycle
val tx = connectionSource.transactor(Transactor.defaultStrategy())

// Everything inside runs in one transaction: begin, commit, close
val products: List<ProductRow> = tx.execute { conn ->
    Fragment.of("SELECT * FROM product WHERE price > ?")
        .param(PgTypes.numeric, minPrice)
        .query(ProductRow.rowParser.list())
        .runUnchecked(conn)
}

// Built-in strategies for common patterns
Transactor.defaultStrategy()         // begin → commit → close
Transactor.autoCommitStrategy()      // no transaction, just close
Transactor.rollbackOnErrorStrategy() // begin → commit, rollback on error → close
Transactor.testStrategy()            // begin → rollback → close (for tests)

// Or define your own with explicit hooks
Transactor.Strategy(
    { conn -> conn.autoCommit = false },  // before
    { conn -> conn.commit() },             // after (success)
    { throwable -> /* handle error */ },   // oops
    { conn -> conn.close() }               // always (finally)
)`,
  scala: `// The Transactor manages connections and transactions
// You choose the strategy — it handles the lifecycle
val tx = connectionSource.transactor(Transactor.defaultStrategy())

// Everything inside runs in one transaction: begin, commit, close
val products: List[ProductRow] = tx.execute(conn =>
    sql"SELECT * FROM product WHERE price > \${PgTypes.numeric.encode(minPrice)}"
        .query(ProductRow.rowParser.list())
        .runUnchecked(conn)
)

// Built-in strategies for common patterns
Transactor.defaultStrategy()         // begin → commit → close
Transactor.autoCommitStrategy()      // no transaction, just close
Transactor.rollbackOnErrorStrategy() // begin → commit, rollback on error → close
Transactor.testStrategy()            // begin → rollback → close (for tests)

// Or define your own with explicit hooks
Transactor.Strategy(
    conn => conn.setAutoCommit(false),  // before
    conn => conn.commit(),               // after (success)
    throwable => (),                     // oops
    conn => conn.close()                 // always (finally)
)`,
};

const newtypeCode = {
  java: `record ProductId(String value) {
    static PgType<ProductId> pgType =
        PgTypes.text.bimap(ProductId::new, ProductId::value);

    static PgType<ProductId[]> pgTypeArray =
        PgTypes.textArray.bimap(
            xs -> arrayMap.map(xs, ProductId::new, ProductId.class),
            xs -> arrayMap.map(xs, ProductId::value, String.class));
}`,
  kotlin: `data class ProductId(val value: String) {
    companion object {
        val pgType: PgType<ProductId> =
            PgTypes.text.bimap(::ProductId, ProductId::value)

        val pgTypeArray: PgType<Array<ProductId>> =
            PgTypes.textArray.bimap(
                { xs -> arrayMap.map(xs, ::ProductId, ProductId::class.java) },
                { xs -> arrayMap.map(xs, ProductId::value, String::class.java) })
    }
}`,
  scala: `case class ProductId(value: String) extends AnyVal

object ProductId:
    given pgType: PgType[ProductId] =
        PgTypes.text.bimap(ProductId.apply, _.value)

    given pgTypeArray: PgType[Array[ProductId]] =
        PgTypes.textArray.bimap(
            _.map(ProductId.apply),
            _.map(_.value))`,
};

const arrayCode = {
  java: `List<ProductRow> selectByIds(ProductId[] ids, Connection c) {
    return Fragment.interpolate(
        Fragment.lit("SELECT * FROM product WHERE id = ANY("),
        Fragment.encode(ProductId.pgTypeArray, ids),
        Fragment.lit(")")
    ).query(ProductRow.rowParser.all()).runUnchecked(c);
}`,
  kotlin: `fun selectByIds(ids: Array<ProductId>, c: Connection): List<ProductRow> =
    Fragment.interpolate(
        Fragment.lit("SELECT * FROM product WHERE id = ANY("),
        Fragment.encode(ProductId.pgTypeArray, ids),
        Fragment.lit(")")
    ).query(ProductRow.rowParser.all()).runUnchecked(c)`,
  scala: `import dev.typr.foundations.scala.FragmentInterpolator.sql

def selectByIds(ids: Array[ProductId])
    (using c: Connection): List[ProductRow] =
    sql"SELECT * FROM product WHERE id = ANY(\${Fragment.encode(ProductId.pgTypeArray, ids)})"
        .query(ProductRow.rowParser.all()).runUnchecked(c)`,
};

function ProblemSection() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>The Problem with JDBC</h2>
        <p className={styles.sectionSubtitle}>
          JDBC is notoriously difficult to use correctly. The API is verbose, error-prone,
          and makes it almost impossible to handle all column types properly.
        </p>
        <div className={styles.twoCol}>
          <div>
            <h3 style={{color: '#ef4444', fontSize: '1.2rem', marginBottom: '1rem'}}>What goes wrong</h3>
            <ul style={{listStyle: 'none', padding: 0, margin: 0}}>
              <li style={{marginBottom: '0.75rem', padding: '0.75rem 1rem', borderRadius: '8px', background: 'rgba(239, 68, 68, 0.08)', borderLeft: '3px solid #ef4444'}}>
                <strong>Null handling</strong> — Was that column nullable? Did you remember <code>wasNull()</code>?
              </li>
              <li style={{marginBottom: '0.75rem', padding: '0.75rem 1rem', borderRadius: '8px', background: 'rgba(239, 68, 68, 0.08)', borderLeft: '3px solid #ef4444'}}>
                <strong>Type conversions</strong> — Does <code>getObject()</code> return what you expect? Often not.
              </li>
              <li style={{marginBottom: '0.75rem', padding: '0.75rem 1rem', borderRadius: '8px', background: 'rgba(239, 68, 68, 0.08)', borderLeft: '3px solid #ef4444'}}>
                <strong>Resource management</strong> — Did you close that ResultSet? Statement? Connection?
              </li>
              <li style={{padding: '0.75rem 1rem', borderRadius: '8px', background: 'rgba(239, 68, 68, 0.08)', borderLeft: '3px solid #ef4444'}}>
                <strong>Database differences</strong> — Code that works on PostgreSQL may silently corrupt data on Oracle.
              </li>
            </ul>
          </div>
          <div>
            <h3 style={{color: '#22c55e', fontSize: '1.2rem', marginBottom: '1rem'}}>What we built</h3>
            <ul style={{listStyle: 'none', padding: 0, margin: 0}}>
              <li style={{marginBottom: '0.75rem', padding: '0.75rem 1rem', borderRadius: '8px', background: 'rgba(34, 197, 94, 0.08)', borderLeft: '3px solid #22c55e'}}>
                <strong>Type-safe database types</strong> — Every column type modeled correctly for each database.
              </li>
              <li style={{marginBottom: '0.75rem', padding: '0.75rem 1rem', borderRadius: '8px', background: 'rgba(34, 197, 94, 0.08)', borderLeft: '3px solid #22c55e'}}>
                <strong>Full roundtrip support</strong> — Read a value, write it back — no loss, no corruption.
              </li>
              <li style={{marginBottom: '0.75rem', padding: '0.75rem 1rem', borderRadius: '8px', background: 'rgba(34, 197, 94, 0.08)', borderLeft: '3px solid #22c55e'}}>
                <strong>Automatic resource management</strong> — Connections, statements, and result sets managed for you.
              </li>
              <li style={{padding: '0.75rem 1rem', borderRadius: '8px', background: 'rgba(34, 197, 94, 0.08)', borderLeft: '3px solid #22c55e'}}>
                <strong>Multi-database</strong> — PostgreSQL, MariaDB, DuckDB, Oracle, SQL Server, and DB2.
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  );
}

function ErrorMessagesSection() {
  return (
    <section className={styles.sectionDark}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Clear Error Messages</h2>
        <p className={styles.sectionSubtitle}>
          When things go wrong, you get helpful messages that tell you exactly what happened — not a cryptic stack trace.
        </p>
        <div style={{maxWidth: '700px', margin: '0 auto', fontFamily: 'var(--ifm-font-family-monospace)', fontSize: '0.9rem', lineHeight: '1.7', whiteSpace: 'pre-wrap', background: 'rgba(0,0,0,0.2)', borderRadius: '12px', padding: '2rem'}}>
          <span style={{color: '#ef4444', fontWeight: 600}}>Column type mismatch at index 3 (name):</span>{"\n"}
          {"  Expected: varchar (PgTypes.text)"}{"\n"}
          {"  Actual: integer"}{"\n"}
          {"\n"}
          <span style={{color: '#ef4444', fontWeight: 600}}>Row parsing failed:</span>{"\n"}
          {"  Expected 5 columns, got 4"}{"\n"}
          {"  Missing column at index 4"}{"\n"}
          {"\n"}
          <span style={{color: '#ef4444', fontWeight: 600}}>Type conversion error:</span>{"\n"}
          {"  Cannot read column 'created_at' as OffsetDateTime"}{"\n"}
          {"  Database type: timestamp without time zone"}{"\n"}
          <span style={{color: '#22c55e', fontWeight: 600}}>{"  Hint: Use PgTypes.timestamp instead of PgTypes.timestamptz"}</span>
        </div>
      </div>
    </section>
  );
}

function JsonCodecsSection() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>JSON Codecs</h2>
        <p className={styles.sectionSubtitle}>
          Every database type includes a JSON codec. No Jackson, Gson, or other dependencies required — just a built-in <code>JsonValue</code> sealed interface.
        </p>
        <div className={styles.twoCol}>
          <div>
            <CodeBlock language="java" title="Built-in JSON for every type">
              {`// Every type has a built-in JSON codec
PgType<Integer> intType = PgTypes.int4;
JsonValue json = intType.json().toJson(42);
Integer value = intType.json().fromJson(json);

// Works for complex types too
PgType<int[]> arrayType = PgTypes.int4ArrayUnboxed;
JsonValue arrayJson = arrayType.json()
    .toJson(new int[]{1, 2, 3});`}
            </CodeBlock>
          </div>
          <div>
            <CodeBlock language="java" title="MULTISET-like nested queries">
              {`// Parse nested data from JSON
JsonRowType<Email> emailJsonType =
    JsonRowType.of(emailParser, List.of("id", "email"));

// Use it like any other DbType
PgType<List<Email>> emailListType =
    emailJsonType.pgList();

// Works on every database:
// emailJsonType.mariaList()
// emailJsonType.oracleList()
// emailJsonType.duckDbList()
// emailJsonType.sqlServerList()`}
            </CodeBlock>
          </div>
        </div>
      </div>
    </section>
  );
}

function StreamingInsertsSection() {
  const streamingCode = {
    java: `// Stream millions of product records via PostgreSQL COPY protocol
Iterator<ProductRow> products = loadProductsFromFile();

long inserted = streamingInsert.insertUnchecked(
    "COPY product(id, name, price, tags, dimensions, metadata, created_at) FROM STDIN",
    1000,                   // batch size
    products,
    connection,
    ProductRow.pgText        // PgText<ProductRow> encodes to COPY format
);`,
    kotlin: `// Stream millions of product records via PostgreSQL COPY protocol
val products: Iterator<ProductRow> = loadProductsFromFile()

val inserted: Long = streamingInsert.insertUnchecked(
    "COPY product(id, name, price, tags, dimensions, metadata, created_at) FROM STDIN",
    1000,                   // batch size
    products,
    connection,
    ProductRow.pgText        // PgText<ProductRow> encodes to COPY format
)`,
    scala: `// Stream millions of product records via PostgreSQL COPY protocol
val products: Iterator[ProductRow] = loadProductsFromFile()

val inserted: Long = streamingInsert.insertUnchecked(
    "COPY product(id, name, price, tags, dimensions, metadata, created_at) FROM STDIN",
    1000,                   // batch size
    products.asJava,
    connection,
    ProductRow.pgText        // PgText[ProductRow] encodes to COPY format
)`,
  };

  return (
    <section className={styles.sectionDark}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>
          Streaming Inserts <span style={{display: 'inline-block', padding: '0.15rem 0.5rem', borderRadius: '4px', fontSize: '0.7rem', fontWeight: 600, background: 'rgba(37, 99, 235, 0.2)', color: '#60a5fa', verticalAlign: 'middle', marginLeft: '0.5rem'}}>PostgreSQL</span>
        </h2>
        <p className={styles.sectionSubtitle}>
          Insert large datasets without loading everything into memory using PostgreSQL's COPY protocol.
          The <code>PgText</code> codec encodes each row to the COPY wire format — tabs, escaping, nulls, all handled correctly.
        </p>
        <div className={styles.centeredCode}>
          <Tabs groupId="language">
            <TabItem value="java" label="Java">
              <CodeBlock language="java">{streamingCode.java}</CodeBlock>
            </TabItem>
            <TabItem value="kotlin" label="Kotlin">
              <CodeBlock language="kotlin">{streamingCode.kotlin}</CodeBlock>
            </TabItem>
            <TabItem value="scala" label="Scala">
              <CodeBlock language="scala">{streamingCode.scala}</CodeBlock>
            </TabItem>
          </Tabs>
        </div>
      </div>
    </section>
  );
}

function NoReflectionSection() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>No Reflection</h2>
        <p className={styles.sectionSubtitle}>
          The entire library is reflection-free. All type information is preserved at compile time.
        </p>
        <div className={styles.featureGrid}>
          <div className={styles.featureCard}>
            <h3>GraalVM native-image</h3>
            <p>Build native executables with instant startup. No reflection configuration needed.</p>
          </div>
          <div className={styles.featureCard}>
            <h3>ProGuard / R8</h3>
            <p>Full minification and optimization support. No keep rules for reflection targets.</p>
          </div>
          <div className={styles.featureCard}>
            <h3>Static analysis</h3>
            <p>Complete visibility into code paths. Tools can trace every call without dead ends.</p>
          </div>
        </div>
      </div>
    </section>
  );
}

function Hero() {
  return (
    <header className={styles.hero}>
      <div className={styles.heroInner}>
        <p className={styles.heroLabel}>A functional JDBC library for the JVM</p>
        <h1 className={styles.heroTitle}>
          What if JDBC just worked the way you think it should?
        </h1>
        <p className={styles.heroTagline}>
          Every type your database has, as a real typed value. Queries that compose. Transactions you control. No annotations, no reflection, no surprises. Just the database library you've been looking for.
        </p>
        <div className={styles.heroButtons}>
          <Link className={styles.btnPrimary} to="/docs/">
            Get Started
          </Link>
          <Link className={styles.btnSecondary} to="https://github.com/typr-dev/foundations-jdbc">
            GitHub
          </Link>
        </div>
      </div>
    </header>
  );
}

function Features() {
  const features = [
    {
      title: 'Not an ORM',
      description: 'No entity manager, no session, no lazy loading, no surprises. You write SQL, you get typed results. That\'s it.',
    },
    {
      title: 'Queries are values',
      description: 'Fragments and row parsers are immutable values you compose, pass around, and run when you\'re ready. No type-class machinery required — just functions and values.',
    },
    {
      title: 'Full roundtrip fidelity',
      description: 'Read a value from the database and write it back without loss or corruption. Every type is modeled exactly as the database defines it.',
    },
    {
      title: 'No reflection, no magic',
      description: 'Zero reflection, zero bytecode generation, zero annotation processing. Works with GraalVM native-image out of the box. You can read every line of what runs.',
    },
    {
      title: 'Composable',
      description: 'Row parsers compose. Join two parsers for a joined query. Left join gives you Optional on the right side. Fragments compose with and(), or(), whereAnd(). It\'s just functions.',
    },
    {
      title: 'Java, Kotlin, Scala',
      description: 'Core library in Java. Kotlin gets nullable types natively. Scala gets Option types and string interpolation. Same concepts, idiomatic in each language.',
    },
  ];

  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>What this is</h2>
        <p className={styles.sectionSubtitle}>
          A complete database library built on functional principles. Not the lowest common denominator — the full power of your database, with the safety and composability of functional programming.
        </p>
        <div className={styles.featureGrid}>
          {features.map(({ title, description }) => (
            <div key={title} className={styles.featureCard}>
              <h3>{title}</h3>
              <p>{description}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}

function ConnectionShowcase() {
  const connectionCode = {
    java: `// Typed config — no JDBC URL to remember
var config = PostgresConfig.builder("localhost", 5432, "mydb", "user", "pass")
    .sslmode(PgSslMode.REQUIRE)
    .reWriteBatchedInserts(true)
    .build();

// For scripts and tests — simple, non-pooled
var tx = config.transactor(Transactor.defaultStrategy());

// For production — HikariCP connection pool
var pool = PooledDataSource.create(config,
    ConnectionSettings.builder()
        .transactionIsolation(TransactionIsolation.READ_COMMITTED)
        .build(),
    PoolConfig.builder()
        .maximumPoolSize(20)
        .idleTimeout(Duration.ofMinutes(10))
        .build());

var tx = pool.transactor();`,
    kotlin: `// Typed config — no JDBC URL to remember
val config = PostgresConfig.builder("localhost", 5432, "mydb", "user", "pass")
    .sslmode(PgSslMode.REQUIRE)
    .reWriteBatchedInserts(true)
    .build()

// For scripts and tests — simple, non-pooled
val tx = config.transactor(Transactor.defaultStrategy())

// For production — HikariCP connection pool
val pool = PooledDataSource.create(config,
    ConnectionSettings.builder()
        .transactionIsolation(TransactionIsolation.READ_COMMITTED)
        .build(),
    PoolConfig.builder()
        .maximumPoolSize(20)
        .idleTimeout(Duration.ofMinutes(10))
        .build())

val tx = pool.transactor()`,
    scala: `// Typed config — no JDBC URL to remember
val config = PostgresConfig.builder("localhost", 5432, "mydb", "user", "pass")
    .sslmode(PgSslMode.REQUIRE)
    .reWriteBatchedInserts(true)
    .build()

// For scripts and tests — simple, non-pooled
val tx = config.transactor(Transactor.defaultStrategy())

// For production — HikariCP connection pool
val pool = PooledDataSource.create(config,
    ConnectionSettings.builder()
        .transactionIsolation(TransactionIsolation.READ_COMMITTED)
        .build(),
    PoolConfig.builder()
        .maximumPoolSize(20)
        .idleTimeout(Duration.ofMinutes(10))
        .build())

val tx = pool.transactor()`,
  };

  return (
    <section className={styles.sectionDark}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>No more JDBC URL archaeology</h2>
        <p className={styles.sectionSubtitle}>
          Typed builders for every database — PostgreSQL, MariaDB, Oracle, SQL Server, DuckDB, DB2.
          Every driver property has a real method with documentation. SSL modes are enums, not strings you hope are spelled right.
          Go from config to connection pool to transactor in a few lines.
        </p>
        <div className={styles.centeredCode}>
          <Tabs groupId="language">
            <TabItem value="java" label="Java">
              <CodeBlock language="java">{connectionCode.java}</CodeBlock>
            </TabItem>
            <TabItem value="kotlin" label="Kotlin">
              <CodeBlock language="kotlin">{connectionCode.kotlin}</CodeBlock>
            </TabItem>
            <TabItem value="scala" label="Scala">
              <CodeBlock language="scala">{connectionCode.scala}</CodeBlock>
            </TabItem>
          </Tabs>
        </div>
      </div>
    </section>
  );
}

function SchemaShowcase() {
  return (
    <section className={styles.sectionDark}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Start with your schema</h2>
        <p className={styles.sectionSubtitle}>
          Take a PostgreSQL table that uses a composite type, arrays, and jsonb.
          This is the running example for everything below.
        </p>
        <div className={styles.twoCol}>
          <div className={styles.twoColLeft}>
            <CodeBlock language="sql" title="Your database schema">{schemaSql}</CodeBlock>
          </div>
          <div className={styles.twoColRight}>
            <Tabs groupId="language">
              <TabItem value="java" label="Java">
                <CodeBlock language="java" title="How you represent it">{rowCode.java}</CodeBlock>
              </TabItem>
              <TabItem value="kotlin" label="Kotlin">
                <CodeBlock language="kotlin" title="How you represent it">{rowCode.kotlin}</CodeBlock>
              </TabItem>
              <TabItem value="scala" label="Scala">
                <CodeBlock language="scala" title="How you represent it">{rowCode.scala}</CodeBlock>
              </TabItem>
            </Tabs>
          </div>
        </div>
      </div>
    </section>
  );
}

function CompositeShowcase() {
  return (
    <section className={styles.sectionDark}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Composite types become real types</h2>
        <p className={styles.sectionSubtitle}>
          The <code>dimensions</code> composite type doesn't become a string or a map — it becomes a record
          with typed fields. <code>PgStruct</code> handles PostgreSQL's composite wire format with typed field builders,
          and gives you a <code>PgType</code> you can use anywhere.
        </p>
        <div className={styles.centeredCode}>
          <Tabs groupId="language">
            <TabItem value="java" label="Java">
              <CodeBlock language="java">{compositeCode.java}</CodeBlock>
            </TabItem>
            <TabItem value="kotlin" label="Kotlin">
              <CodeBlock language="kotlin">{compositeCode.kotlin}</CodeBlock>
            </TabItem>
            <TabItem value="scala" label="Scala">
              <CodeBlock language="scala">{compositeCode.scala}</CodeBlock>
            </TabItem>
          </Tabs>
        </div>
      </div>
    </section>
  );
}

function NewTypeShowcase() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Wrapper types that work everywhere</h2>
        <p className={styles.sectionSubtitle}>
          A <code>ProductId</code> is just a <code>String</code> underneath, but the type system keeps them apart.
          Call <code>bimap</code> on the base type with a constructor and an extractor — you get a full codec
          that works in row parsers, arrays, JSON, and composite types. All of them. Guaranteed.
        </p>
        <div className={styles.centeredCode}>
          <Tabs groupId="language">
            <TabItem value="java" label="Java">
              <CodeBlock language="java">{newtypeCode.java}</CodeBlock>
            </TabItem>
            <TabItem value="kotlin" label="Kotlin">
              <CodeBlock language="kotlin">{newtypeCode.kotlin}</CodeBlock>
            </TabItem>
            <TabItem value="scala" label="Scala">
              <CodeBlock language="scala">{newtypeCode.scala}</CodeBlock>
            </TabItem>
          </Tabs>
        </div>
      </div>
    </section>
  );
}

function ArrayShowcase() {
  return (
    <section className={styles.sectionDark}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Arrays without the pain</h2>
        <p className={styles.sectionSubtitle}>
          Passing arrays to JDBC normally means <code>createArrayOf</code>, type name strings, and a connection reference just to build the parameter.
          Here you just pass <code>ProductId[]</code> directly — the codec from <code>bimap</code> handles the rest.
        </p>
        <div className={styles.centeredCode}>
          <Tabs groupId="language">
            <TabItem value="java" label="Java">
              <CodeBlock language="java">{arrayCode.java}</CodeBlock>
            </TabItem>
            <TabItem value="kotlin" label="Kotlin">
              <CodeBlock language="kotlin">{arrayCode.kotlin}</CodeBlock>
            </TabItem>
            <TabItem value="scala" label="Scala">
              <CodeBlock language="scala">{arrayCode.scala}</CodeBlock>
            </TabItem>
          </Tabs>
        </div>
      </div>
    </section>
  );
}

function ParserShowcase() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Every column has a type</h2>
        <p className={styles.sectionSubtitle}>
          The <code>RowParser</code> maps each column to a <code>DbType</code> that knows exactly how to read and write its value.
          No <code>getObject()</code> guessing, no <code>wasNull()</code> checking. Parsers compose for joins — left join gives you <code>Optional</code> on the right side.
        </p>
        <div className={styles.centeredCode}>
          <Tabs groupId="language">
            <TabItem value="java" label="Java">
              <CodeBlock language="java">{parserCode.java}</CodeBlock>
            </TabItem>
            <TabItem value="kotlin" label="Kotlin">
              <CodeBlock language="kotlin">{parserCode.kotlin}</CodeBlock>
            </TabItem>
            <TabItem value="scala" label="Scala">
              <CodeBlock language="scala">{parserCode.scala}</CodeBlock>
            </TabItem>
          </Tabs>
        </div>
      </div>
    </section>
  );
}

function QueryShowcase() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Queries are values you compose</h2>
        <p className={styles.sectionSubtitle}>
          Build fragments, combine them, pass them to functions, return them from functions.
          Parameters are always bound and typed. Run when you're ready — against a connection or a <code>Transactor</code> that manages the lifecycle for you.
        </p>
        <div className={styles.centeredCode}>
          <Tabs groupId="language">
            <TabItem value="java" label="Java">
              <CodeBlock language="java">{queryCode.java}</CodeBlock>
            </TabItem>
            <TabItem value="kotlin" label="Kotlin">
              <CodeBlock language="kotlin">{queryCode.kotlin}</CodeBlock>
            </TabItem>
            <TabItem value="scala" label="Scala">
              <CodeBlock language="scala">{queryCode.scala}</CodeBlock>
            </TabItem>
          </Tabs>
        </div>
      </div>
    </section>
  );
}

function TransactorShowcase() {
  return (
    <section className={styles.sectionDark}>

      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Transactions you can see</h2>
        <p className={styles.sectionSubtitle}>
          No <code>@Transactional</code> annotation deciding your transaction boundaries somewhere else.
          No implicit session flushing at unpredictable times.
          The <code>Transactor</code> makes the lifecycle explicit: before, after, oops, always — four hooks you control.
        </p>
        <div className={styles.centeredCode}>
          <Tabs groupId="language">
            <TabItem value="java" label="Java">
              <CodeBlock language="java">{transactorCode.java}</CodeBlock>
            </TabItem>
            <TabItem value="kotlin" label="Kotlin">
              <CodeBlock language="kotlin">{transactorCode.kotlin}</CodeBlock>
            </TabItem>
            <TabItem value="scala" label="Scala">
              <CodeBlock language="scala">{transactorCode.scala}</CodeBlock>
            </TabItem>
          </Tabs>
        </div>
      </div>
    </section>
  );
}

function TypeShowcase() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Six databases, full type fidelity</h2>
        <p className={styles.sectionSubtitle}>
          This example used PostgreSQL, but the same approach works across all supported databases.
          Full roundtrip fidelity for every type each one supports.
        </p>
        <div className={styles.typeGrid}>
          {typeGrid.map(({ db, link, types }) => (
            <Link to={link} key={db} className={styles.typeCard}>
              <h3 className={styles.typeCardTitle}>{db}</h3>
              <div className={styles.typeTags}>
                {types.map((t) => (
                  <span key={t} className={styles.typeTag}>{t}</span>
                ))}
              </div>
            </Link>
          ))}
        </div>
        <p style={{textAlign: 'center', marginTop: '2rem', color: '#64748b', fontSize: '1.05rem', fontStyle: 'italic'}}>
          More databases coming soon — the architecture is designed to make adding new ones straightforward.
        </p>
      </div>
    </section>
  );
}

function CTA() {
  return (
    <section className={styles.cta}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Designed for code generation</h2>
        <p className={styles.sectionSubtitle}>
          We obsessed over eliminating edge cases and making every API behave consistently across all databases and all types.
          No special cases, no surprising behavior, no workarounds. This makes Foundations JDBC a premier target for code generation.
        </p>
        <p style={{textAlign: 'center', color: '#94a3b8', fontSize: '1.05rem', maxWidth: '700px', margin: '0 auto 3rem', lineHeight: '1.6'}}>
          All the code you've seen above — row parsers, fragments, transactors, streaming inserts —
          can be generated automatically from your database schema
          by <Link to="https://typr.dev" style={{color: '#60a5fa', fontWeight: 600}}>Typr</Link>,
          our type-safe database code generator for the JVM.
        </p>
        <div className={styles.heroButtons}>
          <Link className={styles.btnPrimary} to="/docs/">
            Get started with Foundations
          </Link>
          <Link className={styles.btnPrimary} to="https://typr.dev">
            Explore Typr
          </Link>
        </div>
      </div>
    </section>
  );
}

export default function Home() {
  return (
    <Layout title="A functional JDBC library for the JVM" description="Functional programming meets JDBC. Composable queries, full type safety, and every data structure your database actually has — for Java, Kotlin, and Scala.">
      <Hero />
      <main>
        <ProblemSection />
        <Features />
        <SchemaShowcase />
        <ParserShowcase />
        <CompositeShowcase />
        <NewTypeShowcase />
        <ArrayShowcase />
        <QueryShowcase />
        <TransactorShowcase />
        <ErrorMessagesSection />
        <JsonCodecsSection />
        <StreamingInsertsSection />
        <NoReflectionSection />
        <TypeShowcase />
        <ConnectionShowcase />
        <CTA />
      </main>
    </Layout>
  );
}
