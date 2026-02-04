import React from 'react';
import Link from '@docusaurus/Link';
import Layout from '@theme/Layout';
import CodeBlock from '@theme/CodeBlock';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import styles from './index.module.css';
import Snippet from '@site/src/components/Snippet';

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


const quickstartKotlin = `import dev.typr.foundations.*
import dev.typr.foundations.connect.duckdb.*

fun main() {
    val tx = DuckDbConfig.builder(":memory:").build().transactor()
    val answer: Int = tx.execute { conn ->
        Fragment.lit("SELECT 42")
            .query(RowParser.of(DuckDbTypes.integer).exactlyOne())
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
    {
      title: 'Query Analysis',
      description: 'Verify your SQL at test time. Parameter types, column types, nullability — all checked against the real database schema. Catch bugs before production.',
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
        <div style={{maxWidth: '700px', margin: '0 auto', fontFamily: 'var(--ifm-font-family-monospace)', fontSize: '0.85rem', lineHeight: '1.7', whiteSpace: 'pre', background: '#1e293b', borderRadius: '12px', padding: '1.5rem', border: '1px solid #334155', boxShadow: '0 10px 25px -5px rgba(0, 0, 0, 0.3)'}}>
          <span style={{color: '#f87171', fontWeight: 600}}>Failed to read column </span><span style={{color: '#fbbf24'}}>3</span><span style={{color: '#f87171', fontWeight: 600}}> '</span><span style={{color: '#60a5fa'}}>created_at</span><span style={{color: '#f87171', fontWeight: 600}}>'</span>{"\n"}
          <span style={{color: '#64748b'}}>{"   │ "}</span><span style={{color: '#e2e8f0'}}>Expected: </span><span style={{color: '#4ade80'}}>timestamptz</span>{"\n"}
          <span style={{color: '#64748b'}}>{"   │ "}</span><span style={{color: '#e2e8f0'}}>Actual:   </span><span style={{color: '#f87171'}}>timestamp</span><span style={{color: '#64748b'}}> (nullable)</span>{"\n"}
          <span style={{color: '#64748b'}}>{"   │ "}</span><span style={{color: '#e2e8f0'}}>Value:    </span><span style={{color: '#fbbf24'}}>"2024-01-15 10:30:00"</span>{"\n"}
          <span style={{color: '#64748b'}}>{"   │ "}</span><span style={{color: '#e2e8f0'}}>Row: </span><span style={{color: '#fbbf24'}}>0</span>{"\n"}
          <span style={{color: '#64748b'}}>{"   └ "}</span><span style={{color: '#f87171'}}>SQLException</span><span style={{color: '#94a3b8'}}>: Cannot convert LocalDateTime to OffsetDateTime</span>
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
            <Snippet file="landing/ProductRow" />
          </div>
        </div>
        <div style={{marginTop: '2rem'}}>
          <Snippet file="landing/ProductRowParser" />
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
            <Snippet file="landing/Dimensions" />
          </div>
          <div>
            <h3 style={{fontSize: '1.1rem', fontWeight: 700, marginBottom: '0.75rem'}}>
              Wrapper types <span className={styles.dbBadge}>MariaDB</span>
            </h3>
            <p style={{color: '#94a3b8', fontSize: '0.95rem', marginBottom: '1rem'}}>
              Call <code>bimap</code> (two-way mapping) on a base type — you get a full codec that works in row parsers, arrays, and JSON.
            </p>
            <Snippet file="landing/WrapperType" />
          </div>
          <div>
            <h3 style={{fontSize: '1.1rem', fontWeight: 700, marginBottom: '0.75rem'}}>
              Arrays <span className={styles.dbBadge}>DuckDB</span>
            </h3>
            <p style={{color: '#94a3b8', fontSize: '0.95rem', marginBottom: '1rem'}}>
              Pass arrays directly — no <code>createArrayOf</code>, no type name strings, no connection reference.
            </p>
            <Snippet file="landing/DuckDbArray" />
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
          <Snippet file="landing/SqlServerQuery" />
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
          Use Spring's <code>@Transactional</code> if that's your style, or manage transactions explicitly with <code>Transactor</code>.
          Either way, you get typed builders for every database and full control over the lifecycle — here with Oracle.
        </p>
        <Tabs groupId="transactor-style" className={styles.centeredTabs}>
          <TabItem value="spring" label="Spring Integration" default>
            <div className={styles.centeredCode}>
              <Snippet file="landing/SpringTransactorExample" />
            </div>
          </TabItem>
          <TabItem value="explicit" label="Explicit">
            <div className={styles.centeredCode}>
              <Snippet file="landing/OracleTransactor" />
            </div>
          </TabItem>
        </Tabs>
      </div>
    </section>
  );
}


function QueryAnalysisReport() {
  const gray = {color: '#64748b'};
  const cyan = {color: '#22d3ee'};
  const green = {color: '#4ade80'};
  const red = {color: '#f87171'};
  const yellow = {color: '#fbbf24'};
  const bold = {fontWeight: 600};
  const white = {color: '#f8fafc'};
  const boldRed = {color: '#f87171', fontWeight: 600};
  const boldGreen = {color: '#4ade80', fontWeight: 600};

  return (
    <>
      <span style={cyan}>╔══════════════════════════════════════════════════════════════════════════════╗</span>{"\n"}
      <span style={cyan}>║</span><span style={bold}>  Query Analysis Report                                                       </span><span style={cyan}>║</span>{"\n"}
      <span style={cyan}>╚══════════════════════════════════════════════════════════════════════════════╝</span>{"\n"}
      {"\n"}
      <span style={bold}>SQL:</span>{"\n"}
      <span style={gray}>  SELECT id, name, created_at, email FROM users WHERE active = ?</span>{"\n"}
      {"\n"}
      <span style={gray}>┌─ </span><span style={bold}>Parameters </span><span style={gray}>─────────────────────────────────────────────────────────────────┐</span>{"\n"}
      <span style={gray}>│  </span><span style={green}>✓</span><span style={white}> param[</span><span style={yellow}>1</span><span style={white}>]: </span><span style={cyan}>boolean             </span><span style={gray}> → </span><span style={white}>bool                                    </span><span style={gray}> │</span>{"\n"}
      <span style={gray}>└──────────────────────────────────────────────────────────────────────────────┘</span>{"\n"}
      {"\n"}
      <span style={gray}>┌─ </span><span style={bold}>Columns </span><span style={gray}>────────────────────────────────────────────────────────────────────┐</span>{"\n"}
      <span style={gray}>│  </span><span style={green}>✓</span><span style={white}> col[</span><span style={yellow}>1</span><span style={white}>]: </span><span style={cyan}>int4                </span><span style={gray}> → </span><span style={white}>id : int4                               </span><span style={gray}> │</span>{"\n"}
      <span style={gray}>│  </span><span style={green}>✓</span><span style={white}> col[</span><span style={yellow}>2</span><span style={white}>]: </span><span style={cyan}>text                </span><span style={gray}> → </span><span style={white}>name : text                             </span><span style={gray}> │</span>{"\n"}
      <span style={gray}>│  </span><span style={red}>✗</span><span style={white}> col[</span><span style={yellow}>3</span><span style={white}>]: </span><span style={cyan}>int4                </span><span style={gray}> → </span><span style={white}>created_at : timestamptz                </span><span style={gray}> │</span>{"\n"}
      <span style={gray}>│  </span><span style={red}>✗</span><span style={white}> col[</span><span style={yellow}>4</span><span style={white}>]: </span><span style={cyan}>text                </span><span style={gray}> → </span><span style={white}>email : text (nullable)                 </span><span style={gray}> │</span>{"\n"}
      <span style={gray}>└──────────────────────────────────────────────────────────────────────────────┘</span>{"\n"}
      {"\n"}
      <span style={boldRed}>✗ 2 error(s) found:</span>{"\n"}
      {"\n"}
      <span style={white}>  </span><span style={yellow}>1</span><span style={white}>. Column </span><span style={yellow}>3</span><span style={white}> '</span><span style={cyan}>created_at</span><span style={white}>': type mismatch</span>{"\n"}
      <span style={gray}>     │ </span><span style={white}>Declared: </span><span style={green}>int4</span><span style={gray}> (JDBC: INTEGER)</span>{"\n"}
      <span style={gray}>     │ </span><span style={white}>Returned: </span><span style={red}>timestamptz</span><span style={gray}> (JDBC: TIMESTAMP_WITH_TIMEZONE)</span>{"\n"}
      <span style={gray}>     └ </span><span style={white}>The declared type cannot read from TIMESTAMP_WITH_TIMEZONE</span>{"\n"}
      {"\n"}
      <span style={white}>  </span><span style={yellow}>2</span><span style={white}>. Column </span><span style={yellow}>4</span><span style={white}> '</span><span style={cyan}>email</span><span style={white}>': nullability mismatch</span>{"\n"}
      <span style={gray}>     │ </span><span style={white}>The database says this column is nullable</span>{"\n"}
      <span style={gray}>     │ </span><span style={white}>But the type </span><span style={green}>text</span><span style={white}> is not Optional</span>{"\n"}
      <span style={gray}>     └ </span><span style={white}>Use </span><span style={cyan}>.opt()</span><span style={white}> to make the type nullable</span>
    </>
  );
}

function QueryAnalysisSection() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <div style={{textAlign: 'center', marginBottom: '1rem'}}>
          <span style={{
            background: 'linear-gradient(135deg, #22c55e 0%, #16a34a 100%)',
            color: 'white',
            padding: '0.35rem 1rem',
            borderRadius: '9999px',
            fontSize: '0.85rem',
            fontWeight: 600,
            letterSpacing: '0.02em'
          }}>NEW</span>
        </div>
        <h2 className={styles.sectionTitle}>Find SQL bugs at test time, not 2 AM</h2>
        <p className={styles.sectionSubtitle}>
          <strong>Query Analysis</strong> verifies your SQL against the actual database schema.
          Wrong column type? Missing <code>.opt()</code> on a nullable column? Parameter count mismatch?
          <strong> Catch it in tests, not in production.</strong>
        </p>
        <div style={{marginBottom: '2rem'}}>
          <Snippet file="landing/QueryAnalysis" />
        </div>
        <div style={{
          maxWidth: '850px',
          margin: '0 auto',
          fontFamily: 'var(--ifm-font-family-monospace)',
          fontSize: '0.75rem',
          lineHeight: '1.5',
          whiteSpace: 'pre',
          background: '#1e293b',
          borderRadius: '12px',
          padding: '1.5rem',
          overflow: 'auto',
          border: '1px solid #475569',
          boxShadow: '0 25px 50px -12px rgba(0, 0, 0, 0.25)',
          color: '#f8fafc'
        }}>
          <QueryAnalysisReport />
        </div>
        <div style={{marginTop: '2.5rem', textAlign: 'center'}}>
          <div style={{
            display: 'inline-block',
            background: '#0f172a',
            border: '1px solid #22c55e',
            borderRadius: '12px',
            padding: '1.25rem 2rem',
            maxWidth: '700px'
          }}>
            <p style={{color: '#f8fafc', fontSize: '1rem', margin: 0, lineHeight: 1.6}}>
              <strong style={{color: '#4ade80'}}>No other Java SQL library does this.</strong>{' '}
              jOOQ validates DSL at compile time but can't check hand-written SQL.
              Hibernate validates annotations at startup but not query correctness.
              <strong style={{color: '#4ade80'}}> foundations-jdbc validates your actual queries against your actual database.</strong>
            </p>
          </div>
        </div>
        <div style={{textAlign: 'center', marginTop: '1.5rem'}}>
          <Link className={styles.btnSecondary} to="/docs/query-analysis">
            Learn more about Query Analysis →
          </Link>
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
  const cellStyle = (color) => {
    if (color === 'green') return {background: 'rgba(34, 197, 94, 0.25)'};
    if (color === 'yellow') return {background: 'rgba(234, 179, 8, 0.25)'};
    if (color === 'red') return {background: 'rgba(239, 68, 68, 0.25)'};
    return {};
  };

  return (
    <section className={styles.sectionDark}>
      <div className={styles.container}>
        <h2 className={styles.sectionTitle}>How it compares</h2>
        <div style={{maxWidth: '900px', margin: '0 auto', overflowX: 'auto'}}>
          <table className={styles.comparisonTable}>
            <thead>
              <tr>
                <th></th>
                <th>Foundations</th>
                <th>Hibernate</th>
                <th>JDBI</th>
                <th>JdbcTemplate</th>
                <th>Exposed</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Approach</td>
                <td>SQL + typed codecs</td>
                <td>ORM with entity mapping</td>
                <td>SQL + annotations</td>
                <td>SQL + RowMapper</td>
                <td>Kotlin DSL</td>
              </tr>
              <tr>
                <td>Languages</td>
                <td>Java, Kotlin, Scala</td>
                <td>Java, Kotlin</td>
                <td>Java, Kotlin</td>
                <td>Java, Kotlin</td>
                <td>Kotlin only</td>
              </tr>
              <tr>
                <td>Database portability</td>
                <td style={cellStyle('yellow')}>Database-specific*</td>
                <td style={cellStyle('green')}>HQL abstracts over DBs</td>
                <td style={cellStyle('green')}>Raw SQL (portable enough)</td>
                <td style={cellStyle('green')}>Raw SQL (portable enough)</td>
                <td style={cellStyle('green')}>DSL is mostly portable</td>
              </tr>
              <tr>
                <td>Type model</td>
                <td style={cellStyle('green')}>Every database type</td>
                <td style={cellStyle('yellow')}>Java types only</td>
                <td style={cellStyle('yellow')}>Basic + custom</td>
                <td style={cellStyle('red')}>Basic Java types</td>
                <td style={cellStyle('yellow')}>Kotlin types + custom</td>
              </tr>
              <tr>
                <td>Composites, arrays, ranges</td>
                <td style={cellStyle('green')}>First-class</td>
                <td style={cellStyle('yellow')}>Via UserType</td>
                <td style={cellStyle('yellow')}>Manual mapping</td>
                <td style={cellStyle('red')}>Raw JDBC only</td>
                <td style={cellStyle('yellow')}>Custom column types</td>
              </tr>
              <tr>
                <td>Reflection</td>
                <td style={cellStyle('green')}>None</td>
                <td style={cellStyle('red')}>Heavy</td>
                <td style={cellStyle('yellow')}>Moderate</td>
                <td style={cellStyle('green')}>None (manual mapper)</td>
                <td style={cellStyle('yellow')}>DAO layer</td>
              </tr>
              <tr>
                <td>Query type checking</td>
                <td style={cellStyle('green')}>At test time</td>
                <td style={cellStyle('red')}>No</td>
                <td style={cellStyle('red')}>No</td>
                <td style={cellStyle('red')}>No</td>
                <td style={cellStyle('yellow')}>DSL only (compile)</td>
              </tr>
              <tr>
                <td>Type-safe nullable columns</td>
                <td style={cellStyle('green')}>Optional&lt;T&gt; / T? / Option[T]</td>
                <td style={cellStyle('yellow')}>@Column(nullable)</td>
                <td style={cellStyle('red')}>Manual null checks</td>
                <td style={cellStyle('red')}>Manual null checks</td>
                <td style={cellStyle('green')}>T? in Kotlin</td>
              </tr>
              <tr>
                <td>Code generation</td>
                <td>Optional (Typr)</td>
                <td style={cellStyle('red')}>Not supported</td>
                <td style={cellStyle('red')}>Not supported</td>
                <td style={cellStyle('red')}>Not supported</td>
                <td style={cellStyle('red')}>Not supported</td>
              </tr>
            </tbody>
          </table>
          <p style={{fontSize: '0.85rem', color: '#94a3b8', marginTop: '1rem', maxWidth: '700px', margin: '1rem auto 0'}}>
            * If you need to switch databases, regenerating code from your new schema may be more reliable than hoping the leaky abstraction holds.
          </p>
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
        <QueryAnalysisSection />
        <TypeShowcase />
        <ComparisonSection />
        <CTA />
      </main>
    </Layout>
  );
}
