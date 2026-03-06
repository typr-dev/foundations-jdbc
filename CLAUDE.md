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

This project uses **Bleep** as the build tool. The build is defined in `bleep.yaml`.

### Common Commands
```bash
# Compile all library modules
bleep compile foundations-jdbc foundations-jdbc-hikari foundations-jdbc-spring foundations-jdbc-kotlin foundations-jdbc-scala

# Compile tests
bleep compile foundations-jdbc-test

# Run only embedded tests (no Docker needed)
bleep test foundations-jdbc-test -o dev.typr.foundations.DuckDbTypeTest
bleep test foundations-jdbc-test -o dev.typr.foundations.PgRecordParserTest

# Compile documentation examples
bleep compile documentation-examples-java documentation-examples-kotlin documentation-examples-scala
```

## Module Structure

```
foundations-jdbc/              # Core JDBC wrapper (Java 21)
└── src/java/                  # Hand-written sources

foundations-jdbc-hikari/        # HikariCP integration
└── src/java/

foundations-jdbc-kotlin/        # Kotlin wrapper
└── src/kotlin/                # Hand-written Kotlin sources

foundations-jdbc-scala/         # Scala 3 sources
└── src/scala/                 # Hand-written Scala sources

foundations-jdbc-test/          # Integration tests
├── src/java/
└── src/kotlin/

foundations-jdbc-scripts/       # Bleep publish scripts
└── src/scala/scripts/

foundations-jdbc-scripts-sourcegen/  # Bleep sourcegen scripts
└── src/scala/scripts/             # SourcegenJava, SourcegenKotlin, SourcegenScala
```

## Source Generation

Three BleepCodegenScript classes generate repetitive code on-the-fly during compilation:

- `SourcegenJava.scala` — Java generated files (Functions, Tuple, RowCodecBuilders, ParamBuilders, Template, etc.)
- `SourcegenKotlin.scala` — Kotlin generated files (RowCodecBuilders, RowCodecNamedBuilders, DbProcedure, DbFunction, etc.)
- `SourcegenScala.scala` — Scala generated files (RowCodecBuilders, RowCodecNamedBuilders, DbProcedure, DbFunction, etc.)

Generated files are NOT checked in — they are produced by bleep sourcegen during compilation. The original standalone scripts in `scripts/` are kept as reference but are no longer the primary source of truth.

## Development Rules
- Always run `bleep compile foundations-jdbc foundations-jdbc-hikari foundations-jdbc-spring foundations-jdbc-kotlin foundations-jdbc-scala` before committing
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

### Documentation Code Samples

**ALL documentation code samples MUST live in the example projects**, not inline in markdown:
- `documentation-examples-java/` - Java examples
- `documentation-examples-kotlin/` - Kotlin examples
- `documentation-examples-scala/` - Scala examples

Use `//start:snippet-name` and `//stop:snippet-name` markers in source files, then include via `<Snippet file="path/SnippetName" />` component. This ensures all code samples are compile-checked and stay in sync across all three languages.

## Dependencies

JDBC drivers are `provided` in foundations-jdbc - consumers provide their own driver at runtime.

Key dependencies:
- `org.jetbrains:annotations` - API dependency
- `com.zaxxer:HikariCP` - hikari module
- `org.scala-lang:scala3-library_3` - Scala module
