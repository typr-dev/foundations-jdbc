import React from 'react';
import Link from '@docusaurus/Link';
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

const parserCode = {
  java: `static RowParser<ProductRow> rowParser = RowParsers.of(
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
  kotlin: `val rowParser: RowParser<ProductRow> = RowParsers.of(
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
  scala: `val rowParser: RowParser[ProductRow] = RowParsers.of(
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

const compositeCode = {
  java: `record Dimensions(
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

static PgType<Dimensions> pgType = pgStruct.asType();`,
  kotlin: `data class Dimensions(
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

val pgType: PgType<Dimensions> = pgStruct.asType()`,
  scala: `case class Dimensions(
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

val pgType: PgType[Dimensions] = pgStruct.asType()`,
};

const wrapperCode = {
  java: `record ProductId(Integer value) {
    // MariaDB int → wraps to your domain type
    static MariaType<ProductId> mariaType =
        MariaTypes.int_.bimap(ProductId::new, ProductId::value);
}`,
  kotlin: `data class ProductId(val value: Int) {
    companion object {
        // MariaDB int → wraps to your domain type
        val mariaType: MariaType<ProductId> =
            MariaTypes.int_.bimap(::ProductId, ProductId::value)
    }
}`,
  scala: `case class ProductId(value: Int) extends AnyVal

object ProductId:
    // MariaDB int → wraps to your domain type
    given mariaType: MariaType[ProductId] =
        MariaTypes.int_.bimap(ProductId.apply, _.value)`,
};

const arrayCode = {
  java: `// DuckDB arrays are first-class typed values
List<String[]> tagSets = tx.execute(conn ->
    Fragment.lit("SELECT tags FROM posts WHERE published = true")
        .query(RowParsers.of(DuckDbTypes.varcharArray).all())
        .run(conn));`,
  kotlin: `// DuckDB arrays are first-class typed values
val tagSets: List<Array<String>> = tx.execute { conn ->
    Fragment.lit("SELECT tags FROM posts WHERE published = true")
        .query(RowParsers.of(DuckDbTypes.varcharArray).all())
        .run(conn)
}`,
  scala: `import dev.typr.foundations.scala.FragmentInterpolator.sql

// DuckDB arrays are first-class typed values
val tagSets: List[Array[String]] = tx.execute(conn =>
    sql"SELECT tags FROM posts WHERE published = true"
        .query(RowParsers.of(DuckDbTypes.varcharArray).all())
        .run(conn))`,
};

const queryCode = {
  java: `// Build small reusable filters — SQL Server example
Fragment byName(String name) {
    return Fragment.interpolate("name LIKE ")
        .param(SqlServerTypes.nvarchar, name).done();
}
Fragment cheaperThan(BigDecimal max) {
    return Fragment.interpolate("price < ")
        .param(SqlServerTypes.decimal, max).done();
}

// Compose dynamically — only include the filters that are present
List<Fragment> filters = Stream.of(
        Optional.of(byName("%widget%")),
        maxPrice.map(this::cheaperThan)
    )
    .flatMap(Optional::stream)
    .toList();

List<OrderRow> orders = Fragment.interpolate("SELECT * FROM orders ")
    .param(Fragment.whereAnd(filters)).done()
    .query(OrderRow.rowParser.all())
    .run(conn);`,
  kotlin: `// Build small reusable filters — SQL Server example
fun byName(name: String) =
    Fragment.interpolate("name LIKE ")
        .param(SqlServerTypes.nvarchar, name).done()

fun cheaperThan(max: BigDecimal) =
    Fragment.interpolate("price < ")
        .param(SqlServerTypes.decimal, max).done()

// Compose dynamically — only include the filters that are present
val filters = listOfNotNull(
    byName("%widget%"),
    maxPrice?.let { cheaperThan(it) }
)

val orders: List<OrderRow> = Fragment.interpolate("SELECT * FROM orders ")
    .param(Fragment.whereAnd(filters)).done()
    .query(OrderRow.rowParser.all())
    .run(conn)`,
  scala: `import dev.typr.foundations.scala.FragmentInterpolator.sql

// Build small reusable filters — SQL Server example
def byName(name: String) =
    sql"name LIKE \${Fragment.encode(SqlServerTypes.nvarchar, name)}"

def cheaperThan(max: BigDecimal) =
    sql"price < \${Fragment.encode(SqlServerTypes.decimal, max)}"

// Compose dynamically — only include the filters that are present
val filters: List[Fragment] = List(
    Some(byName("%widget%")),
    maxPrice.map(cheaperThan)
).flatten

val orders: List[OrderRow] =
    sql"SELECT * FROM orders \${Fragment.whereAnd(filters)}"
        .query(OrderRow.rowParser.all())
        .run(conn)`,
};

const transactorCode = {
  java: `// Oracle — typed config, no JDBC URL to remember
var tx = OracleConfig.builder("localhost", 1521, "xe", "app", "secret")
    .serviceName("XEPDB1")
    .build()
    .transactor();

// Everything inside runs in one transaction
String greeting = tx.execute(conn ->
    Fragment.lit("SELECT 'Hello from Oracle' FROM dual")
        .query(RowParsers.of(OracleTypes.varchar2).exactlyOne())
        .run(conn));

// Built-in strategies for common patterns
Transactor.defaultStrategy()         // begin → commit → close
Transactor.autoCommitStrategy()      // no transaction, just close
Transactor.rollbackOnErrorStrategy() // begin → commit, rollback on error → close
Transactor.testStrategy()            // begin → rollback → close (for tests)`,
  kotlin: `// Oracle — typed config, no JDBC URL to remember
val tx = OracleConfig.builder("localhost", 1521, "xe", "app", "secret")
    .serviceName("XEPDB1")
    .build()
    .transactor()

// Everything inside runs in one transaction
val greeting: String = tx.execute { conn ->
    Fragment.lit("SELECT 'Hello from Oracle' FROM dual")
        .query(RowParsers.of(OracleTypes.varchar2).exactlyOne())
        .run(conn)
}

// Built-in strategies for common patterns
Transactor.defaultStrategy()         // begin → commit → close
Transactor.autoCommitStrategy()      // no transaction, just close
Transactor.rollbackOnErrorStrategy() // begin → commit, rollback on error → close
Transactor.testStrategy()            // begin → rollback → close (for tests)`,
  scala: `import dev.typr.foundations.scala.FragmentInterpolator.sql

// Oracle — typed config, no JDBC URL to remember
val tx = OracleConfig.builder("localhost", 1521, "xe", "app", "secret")
    .serviceName("XEPDB1")
    .build()
    .transactor()

// Everything inside runs in one transaction
val greeting: String = tx.execute(conn =>
    sql"SELECT 'Hello from Oracle' FROM dual"
        .query(RowParsers.of(OracleTypes.varchar2).exactlyOne())
        .run(conn))

// Built-in strategies for common patterns
Transactor.defaultStrategy()         // begin → commit → close
Transactor.autoCommitStrategy()      // no transaction, just close
Transactor.rollbackOnErrorStrategy() // begin → commit, rollback on error → close
Transactor.testStrategy()            // begin → rollback → close (for tests)`,
};

const quickstartKotlin = `import dev.typr.foundations.*
import dev.typr.foundations.connect.duckdb.*

fun main() {
    val tx = DuckDbConfig.builder(":memory:").build().transactor()
    val answer: Int = tx.execute { conn ->
        Fragment.lit("SELECT 42")
            .query(RowParsers.of(DuckDbTypes.integer).exactlyOne())
            .run(conn)
    }
    println("Result: $answer")
}`;

function Hero() {
  return (
    <header className={styles.hero}>
      <div className={styles.heroInner}>
        <p className={styles.heroLabel}>A functional JDBC library for the JVM</p>
        <h1 className={styles.heroTitle}>
          What if JDBC just worked the way you think it should?
        </h1>
        <p className={styles.heroTagline}>
          Every type your database has, as a real typed value. Queries that compose. Transactions you control. No annotations, no reflection, no surprises.
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

function QuickstartSection() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Quick start</h2>
        <p className={styles.sectionSubtitle}>
          DuckDB runs in-memory — no database server needed.
          A <code>Fragment</code> is a typed SQL building block.
        </p>
        <div className={styles.quickstartGrid}>
          <div className={styles.quickstartCode}>
            <CodeBlock language="kotlin" title="Main.kt">
              {quickstartKotlin}
            </CodeBlock>
          </div>
          <div className={styles.quickstartSide}>
            <Tabs>
              <TabItem value="gradle" label="Gradle">
                <CodeBlock language="kotlin" title="build.gradle.kts">
                  {`dependencies {
    implementation("dev.typr:foundations-jdbc:0.1.0-SNAPSHOT")
    // Add your driver
    runtimeOnly("org.duckdb:duckdb_jdbc:1.1.3")
}`}
                </CodeBlock>
              </TabItem>
              <TabItem value="maven" label="Maven">
                <CodeBlock language="xml" title="pom.xml">
                  {`<dependency>
  <groupId>dev.typr</groupId>
  <artifactId>foundations-jdbc</artifactId>
  <version>0.1.0-SNAPSHOT</version>
</dependency>
<dependency>
  <groupId>org.duckdb</groupId>
  <artifactId>duckdb_jdbc</artifactId>
  <version>1.1.3</version>
</dependency>`}
                </CodeBlock>
              </TabItem>
            </Tabs>
          </div>
        </div>
      </div>
    </section>
  );
}

function ProblemSection() {
  return (
    <section className={styles.sectionDark}>
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

function Features() {
  const features = [
    {
      title: 'Full roundtrip fidelity',
      description: 'Read a value from the database and write it back without loss or corruption. Every type is modeled exactly as the database defines it.',
    },
    {
      title: 'Queries are values',
      description: 'Fragments and row parsers are immutable values you compose, pass around, and run when you\'re ready. Just functions and values.',
    },
    {
      title: 'Composable',
      description: 'Row parsers compose. Join two parsers for a joined query. Left join gives you Optional on the right side. Fragments compose with and(), or(), whereAnd(). It\'s just functions.',
    },
    {
      title: 'No reflection, no magic',
      description: 'Zero reflection, zero bytecode generation, zero annotation processing. Works with GraalVM native-image out of the box. You can read every line of what runs.',
    },
    {
      title: 'Not an ORM',
      description: 'No entity manager, no session, no lazy loading, no surprises. You write SQL, you get typed results. That\'s it.',
    },
    {
      title: 'Java, Kotlin, Scala',
      description: 'Core library in Java. Kotlin gets nullable types natively. Scala gets Option types and string interpolation. Same concepts, idiomatic in each language.',
    },
  ];

  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Design Philosophy</h2>
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

function SchemaAndParsers() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Start with your schema</h2>
        <p className={styles.sectionSubtitle}>
          Take a PostgreSQL table with a composite type, arrays, and jsonb.
          The <code>RowParser</code> maps each column to a <code>DbType</code> that knows exactly how to read and write its value.
          No <code>getObject()</code> guessing, no <code>wasNull()</code> checking.
        </p>
        <div className={styles.twoCol}>
          <div className={styles.twoColLeft}>
            <CodeBlock language="sql" title="Your database schema">{schemaSql}</CodeBlock>
          </div>
          <div className={styles.twoColRight}>
            <Tabs groupId="language">
              <TabItem value="java" label="Java">
                <CodeBlock language="java" title="Row type">{rowCode.java}</CodeBlock>
              </TabItem>
              <TabItem value="kotlin" label="Kotlin">
                <CodeBlock language="kotlin" title="Row type">{rowCode.kotlin}</CodeBlock>
              </TabItem>
              <TabItem value="scala" label="Scala">
                <CodeBlock language="scala" title="Row type">{rowCode.scala}</CodeBlock>
              </TabItem>
            </Tabs>
          </div>
        </div>
        <div style={{marginTop: '2rem'}}>
          <Tabs groupId="language">
            <TabItem value="java" label="Java">
              <CodeBlock language="java" title="RowParser — every column has a type">{parserCode.java}</CodeBlock>
            </TabItem>
            <TabItem value="kotlin" label="Kotlin">
              <CodeBlock language="kotlin" title="RowParser — every column has a type">{parserCode.kotlin}</CodeBlock>
            </TabItem>
            <TabItem value="scala" label="Scala">
              <CodeBlock language="scala" title="RowParser — every column has a type">{parserCode.scala}</CodeBlock>
            </TabItem>
          </Tabs>
        </div>
      </div>
    </section>
  );
}

function TypeBuildingBlocks() {
  return (
    <section className={styles.sectionDark}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Type building blocks</h2>
        <p className={styles.sectionSubtitle}>
          Composite types, wrapper types, and arrays — each database has its own type system,
          and each one is modeled faithfully.
        </p>
        <div className={styles.typeBlocksGrid}>
          <div>
            <h3 style={{fontSize: '1.1rem', fontWeight: 700, marginBottom: '0.75rem'}}>
              Composite types <span className={styles.dbBadge}>PostgreSQL</span>
            </h3>
            <p style={{color: '#94a3b8', fontSize: '0.95rem', marginBottom: '1rem'}}>
              The <code>dimensions</code> composite type becomes a record with typed fields. <code>PgStruct</code> handles the wire format.
            </p>
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
          <div>
            <h3 style={{fontSize: '1.1rem', fontWeight: 700, marginBottom: '0.75rem'}}>
              Wrapper types <span className={styles.dbBadge}>MariaDB</span>
            </h3>
            <p style={{color: '#94a3b8', fontSize: '0.95rem', marginBottom: '1rem'}}>
              Call <code>bimap</code> (two-way mapping) on a base type — you get a full codec that works in row parsers, arrays, and JSON.
            </p>
            <Tabs groupId="language">
              <TabItem value="java" label="Java">
                <CodeBlock language="java">{wrapperCode.java}</CodeBlock>
              </TabItem>
              <TabItem value="kotlin" label="Kotlin">
                <CodeBlock language="kotlin">{wrapperCode.kotlin}</CodeBlock>
              </TabItem>
              <TabItem value="scala" label="Scala">
                <CodeBlock language="scala">{wrapperCode.scala}</CodeBlock>
              </TabItem>
            </Tabs>
          </div>
          <div>
            <h3 style={{fontSize: '1.1rem', fontWeight: 700, marginBottom: '0.75rem'}}>
              Arrays <span className={styles.dbBadge}>DuckDB</span>
            </h3>
            <p style={{color: '#94a3b8', fontSize: '0.95rem', marginBottom: '1rem'}}>
              Pass arrays directly — no <code>createArrayOf</code>, no type name strings, no connection reference.
            </p>
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
          Parameters are always bound and typed. Works across databases — here with SQL Server.
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
          The <code>Transactor</code> makes the lifecycle explicit: before, after, oops, always — four hooks you control.
          Typed builders for every database — here with Oracle.
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
          The same approach works across all supported databases.
          Full roundtrip fidelity for every type each one supports. More databases coming soon.
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
      </div>
    </section>
  );
}

function ComparisonSection() {
  return (
    <section className={styles.sectionDark}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>How it compares</h2>
        <div style={{maxWidth: '800px', margin: '0 auto'}}>
          <table className={styles.comparisonTable}>
            <thead>
              <tr>
                <th></th>
                <th>Foundations</th>
                <th>jOOQ</th>
                <th>Hibernate</th>
                <th>JDBI</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Approach</td>
                <td>SQL + typed codecs</td>
                <td>DSL that generates SQL</td>
                <td>ORM with entity mapping</td>
                <td>SQL + annotations</td>
              </tr>
              <tr>
                <td>Type model</td>
                <td>Every database type</td>
                <td>Wide coverage</td>
                <td>Java types only</td>
                <td>Basic + custom</td>
              </tr>
              <tr>
                <td>Composites, arrays, ranges</td>
                <td>First-class</td>
                <td>Partial</td>
                <td>Via UserType</td>
                <td>Manual mapping</td>
              </tr>
              <tr>
                <td>Reflection</td>
                <td>None</td>
                <td>None</td>
                <td>Heavy</td>
                <td>Moderate</td>
              </tr>
              <tr>
                <td>Connection pooling</td>
                <td>Optional module</td>
                <td>Built-in</td>
                <td>Built-in</td>
                <td>External</td>
              </tr>
              <tr>
                <td>Languages</td>
                <td>Java, Kotlin, Scala</td>
                <td>Java, Kotlin</td>
                <td>Java, Kotlin</td>
                <td>Java, Kotlin</td>
              </tr>
              <tr>
                <td>Code generation</td>
                <td>Optional (Typr)</td>
                <td>Required for full DSL</td>
                <td>Not needed</td>
                <td>Not needed</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>
  );
}

function CTA() {
  return (
    <section className={styles.cta}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>Ready to try it?</h2>
        <p className={styles.sectionSubtitle}>
          Foundations works great on its own. For larger codebases,{' '}
          <Link to="https://typr.dev" style={{color: '#60a5fa', fontWeight: 600}}>Typr</Link>{' '}
          can generate all the code you see above from your database schema.
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
        <QuickstartSection />
        <ProblemSection />
        <Features />
        <ErrorMessagesSection />
        <SchemaAndParsers />
        <TypeBuildingBlocks />
        <QueryShowcase />
        <TransactorShowcase />
        <TypeShowcase />
        <ComparisonSection />
        <CTA />
      </main>
    </Layout>
  );
}
