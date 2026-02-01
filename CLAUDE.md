# foundations-jdbc - Type-Safe JDBC Wrapper for JVM Languages

## Project Overview

foundations-jdbc is a standalone JDBC wrapper library with perfect type modeling for all supported databases. It provides type-safe database access primitives for Java, Scala, and Kotlin.

### Supported Databases
- **PostgreSQL** - full support including domains, enums, arrays, JSON, UUID
- **MariaDB/MySQL** - including unsigned types and MySQL-specific features
- **DuckDB** - embedded analytical database
- **SQL Server** - T-SQL specific features
- **Oracle** - including OBJECT and MULTISET types
- **DB2** - IBM DB2 support

## Build System

This project uses **Gradle** as the build tool.

### Common Commands
```bash
# Compile all modules
./gradlew compileJava compileScala compileTestJava

# Run tests (requires databases running)
./gradlew test

# Run only embedded tests (no Docker needed)
./gradlew :foundations-jdbc-test:test --tests "dev.typr.foundations.DuckDbTypeTest"
./gradlew :foundations-jdbc-test:test --tests "dev.typr.foundations.PgRecordParserTest"

# Regenerate RowParsers.java and Tuple.java
scala-cli scripts/sourcegen.scala
```

## Module Structure

```
foundations-jdbc/              # Core JDBC wrapper (Java 21)
├── src/java/                  # Hand-written sources
└── generated-and-checked-in/  # RowParsers.java, Tuple.java

foundations-jdbc-hikari/        # HikariCP integration
└── src/java/

foundations-jdbc-scala/         # Scala sources (shared)
└── src/scala/

foundations-jdbc-scala_3/       # Scala 3 build (uses sources from foundations-jdbc-scala/)

foundations-jdbc-test/          # Integration tests
└── src/java/

scripts/
└── sourcegen.scala            # scala-cli script for code generation
```

## Source Generation

`scripts/sourcegen.scala` is a standalone scala-cli script that generates:
- `RowParsers.java` - Type-safe row parser factory methods (arities 1-99)
- `Tuple.java` - Sealed tuple interfaces with records (arities 1-100)

Run with: `scala-cli scripts/sourcegen.scala`

Output goes to `foundations-jdbc/generated-and-checked-in/`.

## Development Rules
- Always run `./gradlew compileJava compileScala compileTestJava` before committing
- **NEVER REPORT SUCCESS IF ITS NOT A SUCCESS.**
- YOU ARE NOT UNDER ANY CIRCUMSTANCE ALLOWED TO CAST TO CHEAT THE TYPE SYSTEM. IF YOU COME ACROSS A SITUATION WHERE YOU HAVE NO OTHER CHOICE, STOP AND ASK USER
- NEVER EVER PERFORM DESTRUCTIVE GIT ACTIONS IN GIT WHERE CHANGES ARE IRREVOCABLY LOST. GIT CHECKOUT FILE? STASH CHANGES INSTEAD. GIT RESET HARD? A STASH INSTEAD
- **Only push to main when the user explicitly requests it.** Before ANY git push, ALWAYS run `git branch --show-current` to verify the branch. By default, create feature branches and PRs.
- WHEN YOU CHANGE CODE, NEVER LEAVE DANGLING COMMENTS DESCRIBING HOW IT WAS BEFORE OR WHY YOU MADE A CHANGE. WE HAVE GIT FOR THAT
- UNDER NO CIRCUMSTANCE, EVER. WILL CLAUDE GIVE UP AND REVERT ALL THE FILES
- NEVER HIDE PROBLEMS BY WORKING AROUND THEM. When you discover an issue, IMMEDIATELY TELL THE USER.

## Documentation Site

The documentation site is built with Docusaurus 3 and deployed to GitHub Pages.

```bash
cd site
npm install
npm run build    # Build static site
npm run start    # Development server
```

Site content lives in `site/docs/` (markdown files).
Deployed to https://typr-dev.github.io/foundations-jdbc/

## Dependencies

JDBC drivers are `compileOnly` in foundations-jdbc - consumers provide their own driver at runtime.

Key dependencies:
- `org.jetbrains:annotations` - API dependency
- `com.zaxxer:HikariCP` - hikari module
- `org.scala-lang:scala3-library_3` - Scala module
