import React, { useEffect, useRef, useState } from 'react';
import Link from '@docusaurus/Link';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';
import Layout from '@theme/Layout';
import CodeBlock from '@theme/CodeBlock';
import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import styles from './index.module.css';
import Snippet from '@site/src/components/Snippet';
import ThemedImg from '@site/src/components/ThemedImg';

/* ------------------------------------------------------------------
   Reveal — scroll-triggered fade + rise using IntersectionObserver
   ------------------------------------------------------------------ */
function Reveal({ children, delay = 0, as: Tag = 'div', className = '', ...rest }) {
  const ref = useRef(null);
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    if (typeof IntersectionObserver === 'undefined') {
      setVisible(true);
      return;
    }
    const el = ref.current;
    if (!el) return;
    const io = new IntersectionObserver(
      ([entry]) => {
        if (entry.isIntersecting) {
          setVisible(true);
          io.unobserve(el);
        }
      },
      { threshold: 0.12, rootMargin: '0px 0px -8% 0px' }
    );
    io.observe(el);
    return () => io.disconnect();
  }, []);

  return (
    <Tag
      ref={ref}
      className={`${styles.reveal} ${visible ? styles.isVisible : ''} ${className}`}
      style={delay ? { transitionDelay: `${delay}ms` } : undefined}
      {...rest}
    >
      {children}
    </Tag>
  );
}

/* ------------------------------------------------------------------
   Tip — inline info icon with hover/focus tooltip
   ------------------------------------------------------------------ */
function Tip({ children }) {
  return (
    <span className={styles.tipWrap} tabIndex={0}>
      <svg className={styles.tipIcon} viewBox="0 0 16 16" aria-hidden="true" focusable="false">
        <circle cx="8" cy="8" r="6.5" fill="none" stroke="currentColor" strokeWidth="1.1" />
        <circle cx="8" cy="5.1" r="0.9" fill="currentColor" />
        <rect x="7.25" y="7" width="1.5" height="4.8" rx="0.4" fill="currentColor" />
      </svg>
      <span className={styles.tipText} role="tooltip">{children}</span>
    </span>
  );
}

/* ------------------------------------------------------------------
   SectionHeader — title + lede (no chapter/kicker chrome)
   ------------------------------------------------------------------ */
function SectionHeader({ title, children }) {
  return (
    <header className={styles.sectionHead}>
      <Reveal>
        <h2 className={styles.sectionTitle}>{title}</h2>
      </Reveal>
      {children && (
        <Reveal delay={80}>
          <p className={styles.sectionLede}>{children}</p>
        </Reveal>
      )}
    </header>
  );
}

/* ------------------------------------------------------------------
   Data
   ------------------------------------------------------------------ */
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

const schemaSql = `CREATE TYPE plan_tier AS ENUM ('free', 'pro', 'team');

CREATE TABLE subscription (
    id            uuid PRIMARY KEY,
    email         text NOT NULL,
    plan          plan_tier NOT NULL,
    active_range  tstzrange NOT NULL,
    metadata      jsonb,
    cancelled_at  timestamptz
);`;

const quickstartJava = `import dev.typr.foundations.*;
import dev.typr.foundations.connect.*;

public class Main {
    public static void main(String[] args) {
        var tx = ConnectionSource.of(DuckDbConfig.inMemory().build()).transactor();
        int answer = Fragment.of("SELECT 42")
            .queryExactlyOne(DuckDbTypes.integer)
            .transact(tx);
        System.out.println("Result: " + answer);
    }
}`;

const quickstartKotlin = `import dev.typr.foundationskt.*
import dev.typr.foundationskt.connect.*

fun main() {
    val tx = ConnectionSource.of(DuckDbConfig.inMemory().build()).transactor()
    val answer: Int = sql { "SELECT 42" }
        .queryExactlyOne(DuckDbTypes.integer)
        .transact(tx)
    println("Result: $answer")
}`;

const quickstartScala = `import dev.typr.foundationssc.*
import dev.typr.foundationssc.Fragment.sql
import dev.typr.foundationssc.connect.*

@main def run(): Unit =
  val tx = ConnectionSource.of(DuckDbConfig.inMemory().build()).transactor()
  val answer: Int = sql"SELECT 42"
    .queryExactlyOne(DuckDbTypes.integer)
    .transact(tx)
  println(s"Result: $$answer")`;

/* ------------------------------------------------------------------
   Hero — cursor-tracked glow, precision grid, staggered entrance
   ------------------------------------------------------------------ */
function Hero() {
  const ref = useRef(null);

  useEffect(() => {
    const el = ref.current;
    if (!el) return;
    let raf = 0;
    const onMove = (e) => {
      cancelAnimationFrame(raf);
      raf = requestAnimationFrame(() => {
        const r = el.getBoundingClientRect();
        const x = ((e.clientX - r.left) / r.width) * 100;
        const y = ((e.clientY - r.top) / r.height) * 100;
        el.style.setProperty('--x', `${x}%`);
        el.style.setProperty('--y', `${y}%`);
      });
    };
    el.addEventListener('mousemove', onMove);
    return () => {
      el.removeEventListener('mousemove', onMove);
      cancelAnimationFrame(raf);
    };
  }, []);

  return (
    <header ref={ref} className={styles.hero}>
      <div className={styles.heroAurora} aria-hidden="true" />
      <div className={styles.heroGrid} aria-hidden="true" />
      <div className={styles.heroGlow} aria-hidden="true" />

      <div className={`${styles.heroInner} ${styles.heroEnter}`}>
        <div className={styles.heroMeta}>
          <span>A database library for the JVM</span>
        </div>

        <h1 className={styles.heroTitle}>
          Your SQL is wrong. You’ll find out in <em>tests</em>, not at 2 AM.
        </h1>

        <p className={styles.heroTagline}>
          Every type your database has, modeled exactly.
          Every query, verified against a real schema before production.
          Every transaction, explicit.
          No annotations, no reflection, no pages.
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

      <div className={styles.heroFacts}>
        <div className={styles.heroFactsRow}>
          <span className={styles.heroFactsLabel}>Languages</span>
          <div className={styles.heroFactsItems}>
            <span>Java</span>
            <span>Kotlin</span>
            <span>Scala</span>
          </div>
        </div>
        <div className={styles.heroFactsRow}>
          <span className={styles.heroFactsLabel}>Databases</span>
          <div className={styles.heroFactsItems}>
            <span>PostgreSQL</span>
            <span>MariaDB</span>
            <span>DuckDB</span>
            <span>Oracle</span>
            <span>SQL Server</span>
            <span>DB2</span>
          </div>
        </div>
      </div>
    </header>
  );
}

/* ------------------------------------------------------------------
   Quickstart
   ------------------------------------------------------------------ */
function QuickstartSection() {
  const { siteConfig } = useDocusaurusContext();
  const version = siteConfig.customFields.jdbcVersion;

  const langConfigs = {
    java: { code: quickstartJava, title: 'Main.java', lang: 'java', artifact: 'foundations-jdbc' },
    kotlin: { code: quickstartKotlin, title: 'Main.kt', lang: 'kotlin', artifact: 'foundations-jdbc-kotlin' },
    scala: { code: quickstartScala, title: 'Main.scala', lang: 'scala', artifact: 'foundations-jdbc-scala_3' },
  };

  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>From <em>zero</em> to query in under a minute</>}>
          DuckDB runs in-memory, no database server needed.
          A <code>Fragment</code> is a typed SQL building block.
        </SectionHeader>

        <Reveal>
          <div className={styles.quickstartFrame}>
            <Tabs groupId="language">
              {Object.entries(langConfigs).map(([key, cfg]) => (
                <TabItem key={key} value={key} label={key.charAt(0).toUpperCase() + key.slice(1)}>
                  <div className={styles.quickstartGrid}>
                    <div>
                      <CodeBlock language={cfg.lang} title={cfg.title}>
                        {cfg.code}
                      </CodeBlock>
                    </div>
                    <div className={styles.quickstartSide}>
                      <Tabs>
                        <TabItem value="gradle" label="Gradle">
                          <CodeBlock language="kotlin" title="build.gradle.kts">
                            {`dependencies {\n    implementation("dev.typr.foundations:${cfg.artifact}:${version}")\n    // Add your driver\n    runtimeOnly("org.duckdb:duckdb_jdbc:1.1.3")\n}`}
                          </CodeBlock>
                        </TabItem>
                        <TabItem value="maven" label="Maven">
                          <CodeBlock language="xml" title="pom.xml">
                            {`<dependency>\n  <groupId>dev.typr.foundations</groupId>\n  <artifactId>${cfg.artifact}</artifactId>\n  <version>${version}</version>\n</dependency>\n<dependency>\n  <groupId>org.duckdb</groupId>\n  <artifactId>duckdb_jdbc</artifactId>\n  <version>1.1.3</version>\n</dependency>`}
                          </CodeBlock>
                        </TabItem>
                        {key === 'scala' && (
                          <TabItem value="sbt" label="sbt">
                            <CodeBlock language="scala" title="build.sbt">
                              {`libraryDependencies ++= Seq(\n  "dev.typr.foundations" % "${cfg.artifact}" % "${version}",\n  "org.duckdb" % "duckdb_jdbc" % "1.1.3" % Runtime\n)`}
                            </CodeBlock>
                          </TabItem>
                        )}
                      </Tabs>
                    </div>
                  </div>
                </TabItem>
              ))}
            </Tabs>
          </div>
        </Reveal>

        <Reveal delay={120}>
          <p className={styles.readMoreCenter}>
            Prefer a working app?{' '}
            <Link to="https://github.com/typr-dev/foundations-jdbc/tree/main/example-kotlin" className={styles.readMore}>
              example-kotlin
            </Link>
            {' · '}
            <Link to="https://github.com/typr-dev/foundations-jdbc/tree/main/example-spring-boot" className={styles.readMore}>
              example-spring-boot
            </Link>
          </p>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Problem / Solution
   ------------------------------------------------------------------ */
function ProblemSection() {
  const bad = [
    {
      title: 'Queries are unchecked strings',
      body: 'Rename a column in the schema and nothing fails until production. No library catches this at test time.',
    },
    {
      title: 'Nullability is invisible',
      body: 'A nullable column and a non-nullable column have the same Java type. Nothing in the API tells you which columns can be null.',
    },
    {
      title: 'Type fidelity is lost',
      body: (<>Your DuckDB <code>STRUCT</code> becomes a shapeless <code>Object</code>. Your PostgreSQL <code>int4range</code> becomes a string you have to parse yourself. The database has real types. Your library ignores them.</>),
    },
    {
      title: 'DB-specific features are second-class',
      body: (<>Libraries target the lowest common denominator. PostgreSQL arrays, Oracle <code>MULTISET</code>, MariaDB unsigned types. All require escape hatches.</>),
    },
  ];

  const good = [
    {
      title: 'Query Analysis catches bugs in tests',
      body: 'Validate every query against a real database in your test suite. Schema changes break tests, not production.',
    },
    {
      title: 'Nullable means Optional',
      body: (<><code>.opt()</code> changes the return type to <code>Optional</code> / <code>T?</code> / <code>Option[T]</code>. If the type isn\'t optional, the column is guaranteed non-null.</>),
    },
    {
      title: 'Every database type, modeled exactly',
      body: 'Composite types, domains, enums, arrays, intervals. All first-class, with full roundtrip fidelity.',
    },
    {
      title: 'Database-specific by design',
      body: (<>Dedicated type classes for each database. <code>PgTypes</code>, <code>OracleTypes</code>, <code>MariaDbTypes</code>: use your database's full feature set.</>),
    },
  ];

  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>What existing libraries <em>still</em> get wrong</>}>
          ORMs and query builders solve the verbosity of raw JDBC. But fundamental problems remain:
          problems that surface in production as silent data corruption, runtime exceptions, and database lock-in.
        </SectionHeader>

        <div className={styles.diagnosisGrid}>
          <Reveal className={styles.diagnosisHead}>
            <div className={`${styles.colHead} ${styles.colHeadRed}`}>
              <span className={styles.colHeadDot} />
              <span>What goes wrong</span>
            </div>
          </Reveal>
          <Reveal delay={120} className={styles.diagnosisHead}>
            <div className={`${styles.colHead} ${styles.colHeadGreen}`}>
              <span className={styles.colHeadDot} />
              <span>foundations</span>
            </div>
          </Reveal>

          {bad.map((p, i) => (
            <React.Fragment key={i}>
              <Reveal>
                <div className={`${styles.dossierCard} ${styles.dossierBad}`}>
                  <div className={styles.dossierHead}>
                    <span className={styles.dossierKicker}>
                      <span className={styles.dossierDot} />
                      <span>Symptom</span>
                    </span>
                    <span className={styles.dossierNum}>{String(i + 1).padStart(2, '0')}</span>
                  </div>
                  <h4 className={styles.dossierTitle}>{p.title}</h4>
                  <p className={styles.dossierBody}>{p.body}</p>
                  <div className={styles.dossierAccent} aria-hidden="true" />
                </div>
              </Reveal>
              <Reveal delay={120}>
                <div className={`${styles.dossierCard} ${styles.dossierGood}`}>
                  <div className={styles.dossierHead}>
                    <span className={styles.dossierKicker}>
                      <span className={styles.dossierDot} />
                      <span>Remedy</span>
                    </span>
                    <span className={styles.dossierNum}>{String(i + 1).padStart(2, '0')}</span>
                  </div>
                  <h4 className={styles.dossierTitle}>{good[i].title}</h4>
                  <p className={styles.dossierBody}>{good[i].body}</p>
                  <div className={styles.dossierAccent} aria-hidden="true" />
                </div>
              </Reveal>
            </React.Fragment>
          ))}
        </div>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Query Analysis
   ------------------------------------------------------------------ */
function QueryAnalysisReport() {
  return <ThemedImg
    light="/img/qa-check-report-light.png"
    dark="/img/qa-check-report-dark.png"
    alt="Query Analysis Report showing type and nullability mismatches"
    style={{ maxWidth: '100%', borderRadius: '8px' }}
  />;
}

function QueryAnalysisSection() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Find SQL bugs at <em>test time</em>, not 2 AM</>}>
          <strong>Query Analysis</strong> verifies your SQL against the actual database schema.
          Wrong column type? Missing <code>.opt()</code> on a nullable column? Parameter count mismatch?
          <strong> Catch it in tests, not in production.</strong>
        </SectionHeader>

        <div className={styles.qaSplit}>
          <Reveal className={styles.qaSplitItem}>
            <Snippet file="landing/QueryAnalysis" />
          </Reveal>
          <Reveal delay={80} className={styles.qaSplitItem}>
            <QueryAnalysisReport />
          </Reveal>
        </div>

        <Reveal delay={160}>
          <div className={styles.pullQuote}>
            <strong>No other Java SQL library does this.</strong>{' '}
            jOOQ validates DSL at compile time but can't check hand-written SQL.
            Hibernate validates annotations at startup but not query correctness.{' '}
            <strong>foundations-jdbc validates your actual queries against your actual database.</strong>
          </div>
        </Reveal>

        <Reveal delay={200}>
          <p style={{ textAlign: 'center', marginTop: '2rem', color: 'var(--ink-400)', fontSize: '0.9rem' }}>
            The depth of analysis depends on what each database's JDBC driver reports.{' '}
            <Link to="/docs/query-analysis-database-behavior" className={styles.linkPrimary}>See database-specific behavior</Link>.
          </p>
          <div style={{ textAlign: 'center', marginTop: '1.25rem' }}>
            <Link className={styles.btnSecondary} to="/docs/query-analysis">
              Learn more about Query Analysis →
            </Link>
          </div>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Error messages
   ------------------------------------------------------------------ */
function ErrorMessagesSection() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Messages that <em>actually</em> help</>}>
          When things go wrong, you get helpful messages that tell you exactly what happened,
          not a cryptic stack trace.
        </SectionHeader>

        <Reveal>
          <div style={{ display: 'flex', gap: '1.5rem', flexWrap: 'wrap', justifyContent: 'center', alignItems: 'start' }}>
            <ThemedImg light="/img/qa-runtime-error-light.png" dark="/img/qa-runtime-error-dark.png" alt="Runtime parse error with detailed context" />
            <ThemedImg light="/img/pg-error-hint-light.png" dark="/img/pg-error-hint-dark.png" alt="PostgreSQL error with hint" />
          </div>
        </Reveal>

        <Reveal delay={80}>
          <p className={styles.readMoreCenter}>
            <Link to="/docs/error-handling" className={styles.readMore}>
              Read the full documentation
            </Link>
          </p>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Features
   ------------------------------------------------------------------ */
function Features() {
  const features = [
    {
      title: 'Composable, top to bottom',
      description: (
        <>
          <code>DbType</code>, <code>Fragment</code>, <code>RowCodec</code>, <code>Operation</code>: every layer composes.
          Codecs join for tuples; left joins wrap the right side in <code>Optional</code>.
          Combine independent operations with <code>combine</code>, chain dependent ones with <code>then</code>.
          The library can tell the difference, and the optimizer will too.
        </>
      ),
    },
    {
      title: 'Full roundtrip fidelity',
      description: 'Read a value from the database, write it back unchanged. Every type modeled exactly as the database defines it: composites, ranges, arrays, enums, domains.',
    },
    {
      title: 'Read or write, in the type',
      description: (
        <>
          Readonly transactions are first-class. <code>transactRead</code> hands you a <code>ConnectionRead</code>; <code>transact</code> hands you a <code>Connection</code>.
          Operations declare what they need. The type system tells the library, and the next reviewer, what's allowed.
        </>
      ),
    },
    {
      title: 'No reflection, no magic',
      description: 'Zero runtime reflection, zero bytecode generation, zero annotation processing. GraalVM native-image works out of the box. You can read every line of what runs.',
    },
    {
      title: 'Not an ORM',
      description: "No entity manager, no session, no lazy loading, no dirty checking, no surprises. You write SQL, you get typed results. That's it.",
    },
    {
      title: 'Query Analysis',
      description: 'Verify SQL at test time. Parameter types, column types, nullability: all checked against the real database schema. Catch bugs before production.',
    },
  ];

  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Functional to the <em>foundations</em></>}>
          A database library built on functional principles.
          Fragments, codecs, types, operations: each one is a value you compose.
          Same primitives top to bottom.
        </SectionHeader>

        <Reveal>
          <div className={styles.featureGrid}>
            {features.map(({ title, description }) => (
              <div key={title} className={styles.featureCard}>
                <h3>{title}</h3>
                <p>{description}</p>
              </div>
            ))}
          </div>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Schema + codecs
   ------------------------------------------------------------------ */
function SchemaAndCodecs() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Start with your <em>schema</em></>}>
          A real PostgreSQL table: a <code>uuid</code>, an enum, a <code>tstzrange</code>, a <code>jsonb</code>, two nullable columns.
          The <code>RowCodec</code> maps each to a <code>DbType</code> that knows exactly how to read and write its value.
          No <code>getObject()</code> guessing, no <code>wasNull()</code> checks.
        </SectionHeader>

        <div className={styles.twoCol}>
          <Reveal>
            <CodeBlock language="sql" title="Your database schema">{schemaSql}</CodeBlock>
          </Reveal>
          <Reveal delay={120}>
            <Snippet file="landing/SubscriptionRow" />
          </Reveal>
        </div>
        <Reveal delay={200}>
          <div className={styles.schemaStack}>
            <Snippet file="landing/SubscriptionRowCodec" />
          </div>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Type building blocks
   ------------------------------------------------------------------ */
function TypeBuildingBlocks() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Building blocks, <em>faithfully modeled</em></>}>
          Composite types, wrapper types, and arrays: each database has its own type system,
          and each one is modeled faithfully.
        </SectionHeader>

        <Reveal>
          <Tabs groupId="type-blocks" className={styles.centeredTabs}>
            <TabItem value="composite" label="Composite Types" default>
              <p style={{ color: 'var(--ink-300)', fontSize: '0.95rem', margin: '1rem 0 1.25rem' }}>
                The <code>dimensions</code> composite type becomes a record with typed fields. <code>PgStruct</code> handles the wire format.
              </p>
              <CodeBlock language="sql" title="PostgreSQL DDL">
                {`CREATE TYPE dimensions AS (\n    width   double precision,\n    height  double precision,\n    depth   double precision,\n    unit    varchar(10)\n);`}
              </CodeBlock>
              <Snippet file="landing/Dimensions" />
            </TabItem>
            <TabItem value="wrapper" label="Wrapper Types">
              <p style={{ color: 'var(--ink-300)', fontSize: '0.95rem', margin: '1rem 0 1.25rem' }}>
                Call <code>transform</code> (two-way mapping) on a base type. You get a full codec that works in row codecs, arrays, and JSON.
              </p>
              <CodeBlock language="sql" title="MariaDB DDL">
                {`CREATE TABLE products (\n    id   INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n    name VARCHAR(255) NOT NULL\n);`}
              </CodeBlock>
              <Snippet file="landing/WrapperType" />
            </TabItem>
            <TabItem value="arrays" label="Arrays">
              <p style={{ color: 'var(--ink-300)', fontSize: '0.95rem', margin: '1rem 0 1.25rem' }}>
                Pass arrays directly. No <code>createArrayOf</code>, no type name strings, no connection reference.
              </p>
              <CodeBlock language="sql" title="DuckDB DDL">
                {`CREATE TABLE posts (\n    id        INTEGER,\n    tags      VARCHAR[],\n    published BOOLEAN\n);`}
              </CodeBlock>
              <Snippet file="landing/DuckDbArray" />
            </TabItem>
          </Tabs>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Query showcase
   ------------------------------------------------------------------ */
function QueryShowcase() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Queries are <em>values</em> you compose</>}>
          Build fragments, combine them, pass them to functions, return them from functions.
          The <code>optionally</code> DSL gives you optional filters as branch points, and Query Analysis
          verifies <em>every</em> resulting SQL shape, not just the one your test happens to take.
        </SectionHeader>

        <Reveal>
          <div className={styles.centeredCode}>
            <Snippet file="landing/SqlServerQuery" />
          </div>
        </Reveal>

        <Reveal delay={80}>
          <p className={styles.readMoreCenter}>
            <Link to="/docs/dynamic-queries" className={styles.readMore}>
              Read the full documentation
            </Link>
          </p>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Transactor showcase
   ------------------------------------------------------------------ */
function TransactorShowcase() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Transactions you can <em>see</em></>}>
          Use Spring's <code>@Transactional</code> if that's your style, or manage transactions explicitly with <code>Transactor</code>.
          Either way, you get typed builders for every database and full control over the lifecycle. Here with Oracle.
        </SectionHeader>

        <Reveal>
          <Tabs groupId="transactor-style" className={styles.centeredTabs}>
            <TabItem value="explicit" label="Explicit" default>
              <div className={styles.centeredCode}>
                <Snippet file="landing/OracleTransactor" />
              </div>
            </TabItem>
            <TabItem value="spring" label="Spring Integration">
              <div className={styles.centeredCode}>
                <Snippet file="landing/SpringTransactorExample" />
              </div>
            </TabItem>
          </Tabs>
        </Reveal>

        <Reveal delay={80}>
          <p className={styles.readMoreCenter}>
            <Link to="/docs/transactors" className={styles.readMore}>
              Read the full documentation
            </Link>
          </p>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   JSON
   ------------------------------------------------------------------ */
function JsonSection() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Built-in <em>JSON</em> codecs</>}>
          All databases can transfer data as JSON, and now you can use it uniformly.
          Your <code>RowCodec</code> doubles as a JSON codec with zero extra code.
          Aggregate child rows with <code>json_agg()</code>, <code>JSON_ARRAYAGG</code>,
          or <code>FOR JSON</code> and parse them with the same types.
        </SectionHeader>

        <Reveal>
          <div className={styles.centeredCode}>
            <Snippet file="landing/JsonCodecs" />
          </div>
        </Reveal>

        <Reveal delay={80}>
          <p className={styles.readMoreCenter}>
            <Link to="/docs/json" className={styles.readMore}>
              Learn more
            </Link>
          </p>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Stored procedures
   ------------------------------------------------------------------ */
function StoredProcedureSection() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Call any <em>routine</em> like a typed function</>}>
          Define a procedure or function once. The builder tracks IN and OUT types statically.
          Functions use <code>SELECT</code> so every <code>DbType</code> reads correctly through the normal codec path.
          OUT params use a <code>CallableStatement</code> adapter that reuses the same <code>DbRead</code> logic.
        </SectionHeader>

        <Reveal>
          <div className={styles.centeredCode}>
            <Snippet file="landing/StoredProcedure" />
          </div>
        </Reveal>

        <Reveal delay={80}>
          <p className={styles.readMoreCenter}>
            <Link to="/docs/stored-procedures" className={styles.readMore}>
              Read the full documentation
            </Link>
          </p>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Type showcase — six databases
   ------------------------------------------------------------------ */
function TypeShowcase() {
  return (
    <section className={styles.section}>
      <div className={styles.container}>
        <SectionHeader title={<>Six databases, <em>full type fidelity</em></>}>
          The same approach works across all supported databases.
          Full roundtrip fidelity for every type each one supports. More databases coming soon.
        </SectionHeader>

        <div className={styles.typeGrid}>
          {typeGrid.map(({ db, link, types }, i) => (
            <Reveal key={db} delay={(i % 3) * 80}>
              <Link to={link} className={styles.typeCard}>
                <div className={styles.typeCardHead}>
                  <h3 className={styles.typeCardTitle}>{db}</h3>
                  <span className={styles.typeCardArrow}>VIEW →</span>
                </div>
                <div className={styles.typeTags}>
                  {types.map((t) => (
                    <span key={t} className={styles.typeTag}>{t}</span>
                  ))}
                </div>
              </Link>
            </Reveal>
          ))}
        </div>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Comparison
   ------------------------------------------------------------------ */
function ComparisonSection() {
  const cellClass = (color) => {
    if (color === 'green') return styles.cellGreen;
    if (color === 'yellow') return styles.cellYellow;
    if (color === 'red') return styles.cellRed;
    return '';
  };

  const FN = {
    langs: (
      <>jOOQ ships a <code>KotlinGenerator</code> (data classes) and <code>ScalaGenerator</code> / <code>Scala3Generator</code> (case classes), so Kotlin and Scala callers get generated code in their own language. Caveats: arrays still surface as Java arrays / <code>List</code>, Scala nullable columns are not mapped to <code>Option[T]</code>, and Kotlin non-null types for NOT NULL columns require opt-in flags (<code>kotlinNotNullRecordAttributes</code> et al.) that don't apply to derived columns. Foundations ships dedicated Kotlin and Scala wrappers, native collections, <code>T?</code> in Kotlin, and <code>Option[T]</code> in Scala, idiomatic by default, no flags required.</>
    ),
    portability: (
      <>Type references are explicit and searchable: find all <code>PgTypes.</code> and replace with <code>MariaTypes.</code>. Then run Query Analysis to verify every query against the new database at test time. More manual than hoping an abstraction holds, but nothing slips through unchecked.</>
    ),
    compJooq: (
      <>jOOQ has first-class PostgreSQL array support. PostgreSQL and Oracle composite UDTs are generated by codegen. PostgreSQL ranges are available via the <code>jooq-postgres-extensions</code> module (not the core DSL). Other vendor-specific types (hstore, geometric) typically require custom bindings.</>
    ),
    compHib: (
      <>Hibernate 6.2+ has <code>@Struct</code> for composites and built-in basic array mapping. Ranges still need third-party libraries (Hypersistence Utils).</>
    ),
    compExp: (
      <>Exposed has built-in array support. Ranges and composite types require custom <code>ColumnType</code> implementations.</>
    ),
    reflection: (
      <>Reflection affects GraalVM native-image compatibility, startup time, and debuggability. Libraries using runtime proxies or bytecode generation require additional configuration for native compilation.</>
    ),
    reflJooq: (
      <>jOOQ's DSL runs without reflection, but the default record-to-POJO mapper (<code>DefaultRecordMapper</code>) uses reflection for constructor and setter lookup, and GraalVM native-image requires reflection configuration for jOOQ internals and generated classes.</>
    ),
    checkJooq: (
      <>jOOQ's compile-time checking applies to its DSL. Queries written as plain SQL strings (<code>DSL.sql(...)</code>, <code>.fetch(String)</code>, or <code>resultQuery</code>) are not type-checked. jOOQ's SQL parser can parse and transform SQL strings but does not validate them against the schema. Foundations validates hand-written SQL against the real database schema at test time.</>
    ),
    checkHib: (
      <><code>@CheckHQL</code> (6.3+) validates HQL at compile time against the entity metamodel, not the database schema. Not enabled by default.</>
    ),
    nullJooq: (
      <>jOOQ's <code>Field&lt;T&gt;</code> is not null-aware: nullable and non-null columns share the same type. <code>record.getValue(field)</code> returns plain <code>T</code>, possibly <code>null</code> at runtime regardless of the schema. Java has no <code>Optional&lt;T&gt;</code> codegen; JSR-305 <code>@Nullable</code> annotations can be emitted as an opt-in. The Kotlin generator can emit non-null Kotlin types for NOT NULL columns via <code>kotlinNotNullRecordAttributes</code> / <code>kotlinNotNullPojoAttributes</code>, but these are off by default and don't help with derived columns (LEFT JOIN, UNION, DEFAULT, IDENTITY). Scala generators do not map nullable columns to <code>Option[T]</code>.</>
    ),
    cgJooq: (
      <>jOOQ Open Source Edition (Apache-2.0) supports PostgreSQL, MySQL/MariaDB, SQLite, H2, Derby, HSQLDB, Firebird, DuckDB, YugabyteDB, Trino, and ClickHouse. Commercial licenses (Express / Professional / Enterprise, per-developer) are required for Oracle, SQL Server, and CockroachDB (Express+); Redshift (Professional+); and DB2, Sybase, Snowflake, Teradata, Vertica, HANA, Exasol, BigQuery, and Databricks (Enterprise only).</>
    ),
    cgHib: <>Hibernate Tools generates entity classes from database schemas.</>,
    cgExp: <>Official JetBrains plugin generates Exposed table definitions from database schemas.</>,
  };

  return (
    <section className={`${styles.section} ${styles.sectionPaper}`}>
      <div className={styles.container}>
        <SectionHeader title={<>How it <em>compares</em></>} />

        <Reveal>
          <div className={styles.comparisonWrap}>
            <table className={styles.comparisonTable}>
              <thead>
                <tr>
                  <th></th>
                  <th>Foundations</th>
                  <th>jOOQ</th>
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
                  <td>Type-safe DSL + codegen</td>
                  <td>ORM with entity mapping</td>
                  <td>SQL + annotations</td>
                  <td>SQL + RowMapper</td>
                  <td>Kotlin DSL</td>
                </tr>
                <tr>
                  <td>Languages</td>
                  <td className={cellClass('green')}>Java, Kotlin, Scala</td>
                  <td className={cellClass('yellow')}>Java, Kotlin, Scala<Tip>{FN.langs}</Tip></td>
                  <td>Java, Kotlin</td>
                  <td>Java, Kotlin</td>
                  <td>Java, Kotlin</td>
                  <td>Kotlin only</td>
                </tr>
                <tr>
                  <td>Database portability</td>
                  <td className={cellClass('yellow')}>Database-specific<Tip>{FN.portability}</Tip></td>
                  <td className={cellClass('green')}>Multi-dialect DSL</td>
                  <td className={cellClass('yellow')}>HQL: dialect leaks at runtime</td>
                  <td className={cellClass('yellow')}>Raw SQL: portable until it isn't</td>
                  <td className={cellClass('yellow')}>Raw SQL: portable until it isn't</td>
                  <td className={cellClass('yellow')}>DSL: dialect-specific extensions</td>
                </tr>
                <tr>
                  <td>Type model</td>
                  <td className={cellClass('green')}>Every database type</td>
                  <td className={cellClass('green')}>Every database type</td>
                  <td className={cellClass('yellow')}>Java types only</td>
                  <td className={cellClass('yellow')}>Basic + custom</td>
                  <td className={cellClass('red')}>Basic Java types</td>
                  <td className={cellClass('yellow')}>Kotlin types + custom</td>
                </tr>
                <tr>
                  <td>Composites, arrays, ranges</td>
                  <td className={cellClass('green')}>First-class</td>
                  <td className={cellClass('yellow')}>Partial<Tip>{FN.compJooq}</Tip></td>
                  <td className={cellClass('yellow')}>Partial<Tip>{FN.compHib}</Tip></td>
                  <td className={cellClass('yellow')}>Manual mapping</td>
                  <td className={cellClass('red')}>Raw JDBC only</td>
                  <td className={cellClass('yellow')}>Partial<Tip>{FN.compExp}</Tip></td>
                </tr>
                <tr>
                  <td>Reflection<Tip>{FN.reflection}</Tip></td>
                  <td className={cellClass('green')}>None</td>
                  <td className={cellClass('yellow')}>DSL-only<Tip>{FN.reflJooq}</Tip></td>
                  <td className={cellClass('red')}>Heavy</td>
                  <td className={cellClass('yellow')}>Moderate</td>
                  <td className={cellClass('green')}>None (manual mapper)</td>
                  <td className={cellClass('yellow')}>DAO layer</td>
                </tr>
                <tr>
                  <td>Query type checking</td>
                  <td className={cellClass('green')}>At test time (hand-written SQL)</td>
                  <td className={cellClass('yellow')}>DSL only (compile)<Tip>{FN.checkJooq}</Tip></td>
                  <td className={cellClass('yellow')}>Opt-in<Tip>{FN.checkHib}</Tip></td>
                  <td className={cellClass('red')}>No</td>
                  <td className={cellClass('red')}>No</td>
                  <td className={cellClass('yellow')}>DSL only (compile)</td>
                </tr>
                <tr>
                  <td>Type-safe nullable columns</td>
                  <td className={cellClass('green')}>Optional&lt;T&gt; / T? / Option[T]</td>
                  <td className={cellClass('red')}>Not type-safe<Tip>{FN.nullJooq}</Tip></td>
                  <td className={cellClass('yellow')}>@Column(nullable)</td>
                  <td className={cellClass('red')}>Manual null checks</td>
                  <td className={cellClass('red')}>Manual null checks</td>
                  <td className={cellClass('green')}>T? in Kotlin</td>
                </tr>
                <tr>
                  <td>Code generation</td>
                  <td className={cellClass('yellow')}>Coming soon</td>
                  <td className={cellClass('green')}>Mature, schema-driven<Tip>{FN.cgJooq}</Tip></td>
                  <td>Reverse engineering<Tip>{FN.cgHib}</Tip></td>
                  <td className={cellClass('red')}>Not supported</td>
                  <td className={cellClass('red')}>Not supported</td>
                  <td>Gradle plugin<Tip>{FN.cgExp}</Tip></td>
                </tr>
              </tbody>
            </table>
          </div>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   CTA
   ------------------------------------------------------------------ */
function CTA() {
  return (
    <section className={styles.cta}>
      <div className={styles.container}>
        <SectionHeader
          number="14"
          kicker="Coda"
          title={<>Ready to <em>try it?</em></>}
        >
          Foundations JDBC is open source, MIT-licensed, and ready to use today.
        </SectionHeader>

        <Reveal>
          <div className={styles.ctaButtons}>
            <Link className={styles.btnPrimary} to="/docs/">
              Get Started
            </Link>
            <Link className={styles.btnSecondary} to="https://github.com/typr-dev/foundations-jdbc">
              GitHub
            </Link>
          </div>
        </Reveal>

        <Reveal delay={120}>
          <div className={styles.comingSoon}>
            <h2 className={styles.comingSoonLabel}>Coming <em>soon</em></h2>
            <div className={styles.comingSoonGrid}>
              <div className={styles.comingSoonCard}>
                <strong>World-class codegen with a SQL DSL</strong>
                <p>
                  Generate all the RowCodecs, type definitions, and repository scaffolding you see above, directly from your database schema. Write queries in a type-safe SQL DSL that composes like the language it's embedded in.
                </p>
              </div>
              <div className={styles.comingSoonCard}>
                <strong>A native PostgreSQL driver for the JVM</strong>
                <p>
                  We've been working on something that fundamentally changes what's possible with PostgreSQL on the JVM. It bypasses JDBC entirely, speaks the PostgreSQL wire protocol directly, and unlocks a class of optimizations that no connection pool or driver can offer today. The same Fragments, RowCodecs, and Operations you write today will run on it without changing a line of code.
                </p>
              </div>
            </div>
          </div>
        </Reveal>
      </div>
    </section>
  );
}

/* ------------------------------------------------------------------
   Page
   ------------------------------------------------------------------ */
export default function Home() {
  return (
    <Layout
      title="A database library for the JVM"
      description="Composable queries, full type safety, and every data structure your database actually has. For Java, Kotlin, and Scala."
    >
      <div className={styles.page}>
        <Hero />
        <main>
          <QueryAnalysisSection />
          <ProblemSection />
          <QuickstartSection />
          <ErrorMessagesSection />
          <Features />
          <SchemaAndCodecs />
          <TypeBuildingBlocks />
          <QueryShowcase />
          <TransactorShowcase />
          <JsonSection />
          <StoredProcedureSection />
          <TypeShowcase />
          <ComparisonSection />
          <CTA />
        </main>
      </div>
    </Layout>
  );
}
